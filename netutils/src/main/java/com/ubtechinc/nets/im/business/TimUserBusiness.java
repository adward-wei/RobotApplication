package com.ubtechinc.nets.im.business;

import com.tencent.TIMManager;

/**
 * Created by Administrator on 2017/3/6.
 */
public class TimUserBusiness {

    public static  String getCurrentUsers(){
        return TIMManager.getInstance().getLoginUser();
    }
}
