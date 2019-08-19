package com.cn.request.call;

import org.reactivestreams.Subscription;

/**
 * Date: 2019/7/19
 * <p>
 * Time: 11:36 AM
 * <p>
 * author: 鹿文龙
 */
public abstract class ApiFlowResult<T> extends ApiBaseResult<T> {

	/**
	 * 背压模式
	 *
	 * @param subscription
	 */
	public abstract void onSubscription(Subscription subscription);

}
