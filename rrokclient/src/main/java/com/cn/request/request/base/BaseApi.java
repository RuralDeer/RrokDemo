package com.cn.request.request.base;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Date: 2019/8/1
 * <p>
 * Time: 3:20 PM
 * <p>
 * author: 鹿文龙
 */
public interface BaseApi {

	/**
	 * 多文件上传
	 *
	 * @param url
	 * @param parts
	 * @param map
	 * @return
	 */
	@Multipart
	@POST
	Observable<ResponseBody> uploadFile(@HeaderMap HashMap<String, String> headers, @NonNull @Url String url, @QueryMap HashMap<String, String> map, @Part List<MultipartBody.Part> parts);

	/**
	 * 文件下载
	 *
	 * @param url
	 * @param map
	 * @return
	 */
	@Streaming
	@GET
	Observable<ResponseBody> downLoadFile(@HeaderMap HashMap<String, String> headers, @NonNull @Url String url, @QueryMap HashMap<String, String> map);

}
