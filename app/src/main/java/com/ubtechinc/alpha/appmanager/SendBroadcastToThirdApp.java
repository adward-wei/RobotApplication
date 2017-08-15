package com.ubtechinc.alpha.appmanager;

import android.content.Intent;
import android.os.Bundle;

import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.model.StaticValue;
import com.ubtechinc.alpha.model.app.UbtAppData;

/**
 * Created by ubt on 2017/6/13.
 */

public class SendBroadcastToThirdApp {
    public void getAppButtonEvent(int cmd, byte[] datas, String packageName) {
        UbtAppData developer = new UbtAppData();
        developer.setCmd(cmd);
        developer.setDatas(datas);
        developer.setPackageName(packageName);

        Intent intent = new Intent(developer.getPackageName()
                + StaticValue.APP_BUTTON_EVENT);
        Bundle bundle = new Bundle();
        bundle.putSerializable("appdata", developer);
        intent.putExtras(bundle);
        AlphaApplication.getContext().sendBroadcast(intent);
    }
}
