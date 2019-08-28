package com.cn.request.func.file;

import com.cn.request.call.ApiBaseFileResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Date: 2019/8/7
 * <p>
 * Time: 10:09 AM
 * <p>
 * author: 鹿文龙
 */
public class FileConvertFunc implements Function<ResponseBody, File> {

	private String destFileDir;
	private String destFileName;
	private ApiBaseFileResult apiBaseFileResult;

	public FileConvertFunc(String destFileDir, String destFileName, ApiBaseFileResult apiBaseFileResult) {
		this.destFileDir = destFileDir;
		this.destFileName = destFileName;
		this.apiBaseFileResult = apiBaseFileResult;
	}

	@Override
	public File apply(ResponseBody responseBody) throws Exception {
		InputStream is = null;
		byte[] buf = new byte[2048];
		int len = 0;
		FileOutputStream fos = null;
		try {
			is = responseBody.byteStream();
			final long total = responseBody.contentLength();
			long sum = 0;

			File dir = new File(destFileDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, destFileName);
			fos = new FileOutputStream(file);
			while ((len = is.read(buf)) != -1) {
				sum += len;
				fos.write(buf, 0, len);
				final long finalSum = sum;
				apiBaseFileResult.onProgress(file, (int) (finalSum * 100 / total), total);
			}
			fos.flush();
			return file;

		} finally {
			try {
				if (is != null) {is.close();}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fos != null) {fos.close();}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
