package com.ubtechinc.alpha.Configuration;

import android.content.Context;

/**
 * @desc : 加载配置文件信息到内存
 * @author: wzt
 * @time : 2017/5/25
 * @modifier:
 * @modify_time:
 */

public class ConfigurationLoad {
    private static ConfigurationLoad sConfigurationLoad;
    private Context mContext;

    private ConfigurationLoad(Context context) {
        mContext = context.getApplicationContext();
    }

    public static ConfigurationLoad get(Context context) {
        if(sConfigurationLoad == null) {
            synchronized (ConfigurationLoad.class) {
                if(sConfigurationLoad == null)
                    sConfigurationLoad = new ConfigurationLoad(context);
            }
        }

        return sConfigurationLoad;
    }

    public void load() {
        ConfigurationJsonReadUtil readUtil = new ConfigurationJsonReadUtil(mContext);
        String info = readUtil.readConfigJson();

        ConfigurationParse parse = new ConfigurationParse();
        parse.parseConfigJson(info);
    }
}
