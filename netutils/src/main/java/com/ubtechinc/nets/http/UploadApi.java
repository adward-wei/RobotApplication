package com.ubtechinc.nets.http;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @desc : 文件上传API
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/15
 * @modifier:
 * @modify_time:
 */

public interface UploadApi {

    @Multipart
    @POST()
    Observable<ResponseBody> upLoadImage(
            @Url String url,
            @Part("description") String description,
            @Part(value = "image", encoding = "8-bit") RequestBody image);


    @Multipart
    @POST()
    Observable<ResponseBody> upLoadFile(
            @Url String url,
            @Part("description") String description,
            @Part(value = "file", encoding = "8-bit") RequestBody file);
}
