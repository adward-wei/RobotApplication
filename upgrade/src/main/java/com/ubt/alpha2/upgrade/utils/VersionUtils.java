package com.ubt.alpha2.upgrade.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ubt.alpha2.upgrade.bean.VersionConfigs;

import java.lang.reflect.Type;

/**
 * Created by ubt on 2017/6/28.
 */

public class VersionUtils {
    private String TAG = "主服务升级";
    // true表示远程版本大，即需要升级 主服务
    public boolean compareVersion(String remote, String verLocal) {
        if (remote == null || verLocal == null) {
            return false;
        }
        if (remote.equals("") || verLocal.equals("")) {
            return false;
        }
        String[] lVersion = verLocal.split("\\.");
        String[] wVersion = remote.split("\\.");
        for (int i = 0; i < lVersion.length; i++) {
            if (Integer.parseInt(wVersion[i]) > Integer.parseInt(lVersion[i])) {
                return true;
            } else if (Integer.parseInt(wVersion[i]) == Integer
                    .parseInt(lVersion[i])) {
                continue;
            } else {
                return false;
            }
        }
        return false;
    }

    //获取版本配置
    public static VersionConfigs getVersionConfigs(String path) {
        String localConfigsString = FileManagerUtils.readConfigFile(path);
        if(localConfigsString == null){
            LogUtils.d(" localConfigsString= null");
            return null;
        }
        LogUtils.d(" ConfigsString="+localConfigsString);

        Type classType = new TypeToken<VersionConfigs>() {
        }.getType();
        Gson gson = new Gson();
        VersionConfigs versionConfigs = gson.fromJson(
                localConfigsString, classType);
        return versionConfigs;
    }
}
