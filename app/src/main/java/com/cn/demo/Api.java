package com.cn.demo;

import com.cn.demo.bean.TestBean;
import com.cn.demo.bean.UserBean;
import com.cn.request.mock.anno.Mock;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
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

    String BASE_URL = "http://192.168.234.105:8080/";

    String mockUrl = "https://easy-mock.com/mock/5d652f42c5c10b3c6a5875b8/example/getUsers";

    @Mock(value = mockUrl, enable = true)
    @GET("getUsers")
    Observable<List<TestBean>> get(@Query("page") int page, @Query("offeset") int offeset);
    
    @Headers("key-1:header-1123131")
    @GET("getTest")
    Observable<String> getTest(@Query("key-1") String value);

    @FormUrlEncoded
    @POST("test")
    Observable<String> postTest(@Field("new_key") String s);

    @Mock(value = "test/data.json", enable = true)
    @FormUrlEncoded
    @POST("postUsers")
    Observable<List<TestBean>> post(@Field("page") int page, @Field("offeset") int offeset);

    @POST("postUsers")
    Observable<List<TestBean>> postBody(@Body String string);

    @POST("postUsers")
    Observable<List<TestBean>> postBody(@Body RequestBody body);


    @GET("getCookie")
    Observable<UserBean> getCookie();


}
