package com.cn.request.call;
import com.cn.request.model.ApiResponse;

/**
 * Date: 2019/7/10
 * <p>
 * Time: 2:28 PM
 * <p>
 * author: 鹿文龙
 */
public abstract class ApiBaseResult<T> {

	public abstract void onSuccess(ApiResponse<T> apiResponse);

	public abstract void onFailure(Throwable throwable);

	public abstract void onComplete();
}
