package com.ubtechinc.alpha.Configuration;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;

import com.ubtech.utilcode.utils.JsonUtils;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.robotinfo.RobotConfiguration;
import com.ubtechinc.alpha.robotinfo.RobotLanguage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @desc : 读取配置文件
 * @author: wzt
 * @time : 2017/5/25
 * @modifier:
 * @modify_time:
 */

public class ConfigurationJsonReadUtil {

    private static final String TAG = "ConfigurationJsonReadUtil";
    public static final String ACTION_DIRECTORY = "actions";
    public static final String CONFIGURATION_NAME = "/service_config.json";
    private static final boolean isUsingBase64 = false;
    private String SDPath;
    private Context mContext;

    public ConfigurationJsonReadUtil(Context context) {
        mContext = context;
        SDPath = Environment.getExternalStorageDirectory().getPath() + "/";
    }

    public boolean createConFigJson() {
        boolean isCreateSuccess = true;
        File file = new File(SDPath + ACTION_DIRECTORY);
        if(file == null) {
            isCreateSuccess = false;
        } else {
            if(!file.exists()) {
                file.mkdir();
            }
            file = null;
            file = new File(SDPath + ACTION_DIRECTORY + CONFIGURATION_NAME);
            if (file == null) {
                isCreateSuccess = false;
            } else {
                if(!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        isCreateSuccess = false;
                        e.printStackTrace();
                    }
                }
            }
        }
        return isCreateSuccess;
    }

    public boolean isConfigJsonExist() {
        File file = new File(SDPath + ACTION_DIRECTORY + CONFIGURATION_NAME);
        return file.exists();
    }

    public boolean writeConfigJson(String defaultLanguage, String defApp) {
        if((defaultLanguage == null || "".equals(defaultLanguage))) {
            return false;
        }
        boolean isWriteSuccess = true;
        File file = new File(SDPath + ACTION_DIRECTORY + CONFIGURATION_NAME);
        if(!file.exists()) {
            if(!createConFigJson()) {
                isWriteSuccess = false;
                return isWriteSuccess;
            }
        }

        FileWriter fw = null;
        if(defApp == null || "".equals(defApp)) {
            if(RobotLanguage.CN.equals(defaultLanguage)) {
                if(RobotConfiguration.get().isBusiness){
//                    defApp = Alpha2Application.BUSSINESS_CHAT;
                }else{
//                    defApp = Alpha2Application.CHINESE_CHAT;
                }

            } else {
//                defApp = Alpha2Application.ENGLISH_CHAT;
            }
        }

        RobotConfiguration configuration = RobotConfiguration.get();
        configuration.asr_Language = defaultLanguage;
        configuration.default_App = defApp;
        String content = JsonUtils.object2Json(configuration);
        LogUtils.i( "!!! content=" + content);
        try {
            fw = new FileWriter(file);
            if(isUsingBase64) {
                fw.write(Base64.encodeToString(content.getBytes(), 0));
                //fw.write("\r\n");
                //fw.write(Base64.encodeToString(defApp.getBytes(), 0));
            } else {
                fw.write(content);
                //fw.write("\r\n");
                //fw.write(defApp);
            }
            fw.flush();
        } catch (IOException e) {
            isWriteSuccess = false;
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isWriteSuccess;
    }

    public String readConfigJson() {
        StringBuilder sb = new StringBuilder();
        File file = new File(SDPath + ACTION_DIRECTORY + CONFIGURATION_NAME);
        if(file.exists()) {
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String str = null;
                while((str=br.readLine()) != null) {
                    if(isUsingBase64) {
                        sb.append(new String(Base64.decode(str, 0)));
                    } else {
                        sb.append(str);
                    }
                    LogUtils.i( "!!! str=" + str);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public boolean isConfigFileExist() {
        File file = new File(SDPath + ACTION_DIRECTORY + CONFIGURATION_NAME);
        if(file.exists()) {
            LogUtils.i( "isConfigFileExist true");
            return true;
        } else {
            LogUtils.i( "isConfigFileExist false");
            return false;
        }
    }
}
