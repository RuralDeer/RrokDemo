package com.cn.request.func.download;

import com.cn.request.model.DownloadModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Date: 2019/8/7
 * <p>
 * Time: 10:09 AM
 * <p>
 * author: 鹿文龙
 */
public class FileConvertFunc implements Function<ResponseBody, ObservableSource<DownloadModel>> {

    private String destFileDir;
    private String destFileName;

    public FileConvertFunc(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    @Override
    public ObservableSource<DownloadModel> apply(final ResponseBody responseBody) throws Exception {
        final DownloadModel downloadModel = new DownloadModel();
        return Observable.create(new ObservableOnSubscribe<DownloadModel>() {
            @Override
            public void subscribe(ObservableEmitter<DownloadModel> emitter) throws Exception {

                InputStream inputStream = null;
                long currentSize = 0;
                long responseLength = 0;
                FileOutputStream fos = null;
                try {
                    byte[] buf = new byte[2048];
                    int len = 0;
                    responseLength = responseBody.contentLength();
                    inputStream = responseBody.byteStream();
                    final File file = createFile();
                    downloadModel.file = file;
                    downloadModel.fileLength = responseLength;

                    fos = new FileOutputStream(file);
                    int progress = 0;
                    int lastProgress = 0;
                    long startTime = System.currentTimeMillis(); // 开始下载时获取开始时间
                    while ((len = inputStream.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        currentSize += len;
                        lastProgress = progress;
                        progress = (int) (currentSize * 100 / responseLength);
                        long curTime = System.currentTimeMillis();
                        long usedTime = (curTime - startTime) / 1000;
                        if (usedTime == 0) {
                            usedTime = 1;
                        }
                        long speed = (currentSize / usedTime); // 平均每秒下载速度
                        // 如果进度与之前进度相等，则不更新，如果更新太频繁，则会造成界面卡顿
                        if (progress > 0 && progress != lastProgress) {
                            downloadModel.speed = speed;
                            downloadModel.progress = progress;
                            downloadModel.currentSize = currentSize;
                            emitter.onNext(downloadModel);
                        }
                    }
                    fos.flush();
                    downloadModel.file = file;
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private File createFile() {
        File dir = new File(destFileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(destFileDir, destFileName);
    }
}
