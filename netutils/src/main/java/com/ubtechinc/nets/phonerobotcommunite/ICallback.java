package com.ubtechinc.nets.phonerobotcommunite;

import com.ubtechinc.nets.http.ThrowableWrapper;

/**
 * @ClassName ICallback
 * @date 5/16/2017
 * @author tanghongyu
 * @Description
 * @modifier
 * @modify_time
 */
public interface  ICallback<T> {



    /** 对返回数据进行操作的回调， UI线程 */
    public  void onSuccess(T data);



    /** 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程 */
    public void onError(ThrowableWrapper e);


}
