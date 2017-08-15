package com.ubtechinc.nets.http;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @desc : restful http接口定义
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/15
 * @modifier:
 * @modify_time:
 */

public interface RestApi {

    @POST()
    @FormUrlEncoded
    Observable<ResponseBody> doPostWithForm(@Url String url, @FieldMap Map<String, String> maps);

    @POST()
    Observable<ResponseBody> doPostWithJson(@Url String url, @Body RequestBody body);

    @PUT()
    @FormUrlEncoded
    Observable<ResponseBody> doPutWithForm(@Url String url, @FieldMap Map<String, String> maps);

    @PUT()
    Observable<RequestBody> doPutWithJson(@Url String  url, @Body RequestBody body);

    @GET()
    Observable<ResponseBody> doGet(@Url String url, @QueryMap Map<String, String> maps);

    @DELETE()
    Observable<ResponseBody> doDelete(@Url String url, @QueryMap Map<String, String> maps);

}
