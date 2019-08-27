package com.cn.demo;

import com.cn.demo.bean.TestBean;
import com.cn.demo.bean.UserBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Date: 2019/7/9
 * <p>
 * Time: 4:45 PM
 * <p>
 * author: 鹿文龙
 */
public interface Api {

	String BASE_URL = "http://192.168.1.115:8080/";

	@GET("getUsers")
	Observable<List<TestBean>> get(@Query("page") int page, @Query("offeset") int offeset);

	@FormUrlEncoded
	@POST("postUsers")
	Observable<List<TestBean>> post(@Field("page") int page, @Field("offeset") int offeset);

	@POST("postUsers")
	Observable<List<TestBean>> postBody(@Body String string);

	@POST("postUsers")
	Observable<List<TestBean>> postBody(@Body RequestBody body);


	@GET("getCookie")
	Observable<UserBean> login(@Query("username") String username, @Query("password") String password);


}
