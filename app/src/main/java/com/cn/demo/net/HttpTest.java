package com.cn.demo.net;

import com.cn.request.enums.CacheMode;
import com.cn.request.factory.RxScheduler;
import com.cn.request.transformer.CacheTransformer;

import io.reactivex.Observable;

/**
 * Date: 2019-08-20
 * <p>
 * Time: 11:28
 * <p>
 * author: 鹿文龙
 */
public class HttpTest {

	public static <T> Observable<T> transformer(Observable<T> observable) {
		return observable
			.compose(CacheTransformer.<T>obsTransformer(CacheMode.NONE_CACHE))
			.compose(RxScheduler.<T>Obs_io_main());
	}
}
