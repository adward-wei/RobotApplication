package com.ubtechinc.alpha.network.business;
import com.ubtechinc.alpha.network.module.GetTokenModule;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;

/**
 * Created by hongjie.xiang on 2017/6/1.
 */

public class GetToken {
    private static final String TAG = "GetToken";
    private static GetToken sInstance;
    public static GetToken getInstance() {
        if (sInstance == null) {
            synchronized (GetToken.class) {

                if (sInstance == null) {
                    sInstance = new GetToken();
                }
            }
        }
        return sInstance;
    }

    public void requestGetToken(int type, ResponseListener<GetTokenModule.Response> listener) {
        GetTokenModule.Request request=new GetTokenModule.Request();
        request.setFileType(type);
        HttpProxy.get().doGet(request, listener);
    }

}
