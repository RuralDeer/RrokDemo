package com.cn.request.request.download;

import com.cn.HttpClient;
import com.cn.request.func.download.FileConvertFunc;
import com.cn.request.model.DownloadModel;
import com.cn.request.request.base.BaseApi;
import com.cn.request.request.base.BaseRequest;
import com.cn.request.transformer.RxScheduler;

import io.reactivex.Observable;

/**
 * Date: 2019/8/1
 * <p>
 * Time: 4:52 PM
 * <p>
 * author: 鹿文龙
 */
public class DownloadRequest extends BaseRequest<DownloadRequest> {

    public DownloadRequest(String url) {
        super(url);
    }

    public Observable<DownloadModel> tObservable(String destFileDir, String destFileName) {
        return HttpClient.create(BaseApi.class)
                .downLoadFile(headers, url, params)
                .flatMap(new FileConvertFunc(destFileDir, destFileName))
                .compose(RxScheduler.<DownloadModel>obsIoMain());
    }
}
