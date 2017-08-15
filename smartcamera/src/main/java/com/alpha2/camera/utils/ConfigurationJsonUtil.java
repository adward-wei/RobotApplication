package com.alpha2.camera.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;


import com.alpha2.camera.ui.SmartCameraActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @Description
 * @Date 2016/9/12
 * @Author WangZT
 * @Modifier by
 */
public class ConfigurationJsonUtil {

    private static final String TAG = "ConfigurationJsonUtil";
    private static final String DIRECTORY = "actions";
    private static final String CONFIGURATION_NAME = "/service_config.json";
    private static final boolean isUsingBase64 = false;
    private String SDPath;
    private Context mContext;
    private static ConfigurationJsonUtil mConfigurationJsonUtil;
    public ConfigurationJsonUtil(Context context) {
        mContext = context;
        SDPath = Environment.getExternalStorageDirectory().getPath() + "/";
    }

    public static ConfigurationJsonUtil getConfigurationJsonUtil(Context context) {
        if (mConfigurationJsonUtil == null)
            mConfigurationJsonUtil = new ConfigurationJsonUtil(context);

        return mConfigurationJsonUtil;
    }
    public boolean createConFigFile() {
        boolean isCreateSuccess = true;
        File file = new File(SDPath + DIRECTORY);
        if(file == null) {
            isCreateSuccess = false;
        } else {
            if(!file.exists()) {
                file.mkdir();
            }
            file = null;
            file = new File(SDPath + DIRECTORY + CONFIGURATION_NAME);
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

    public boolean isConfigFileExist() {
        File file = new File(SDPath + DIRECTORY + CONFIGURATION_NAME);
        return file.exists();
    }

    public boolean writeConfigFile(String defaultLanguage, String defApp) {
        if((defaultLanguage == null || "".equals(defaultLanguage))) {
            return false;
        }
        boolean isWriteSuccess = true;
        File file = new File(SDPath + DIRECTORY + CONFIGURATION_NAME);
        if(!file.exists()) {
            if(!createConFigFile()) {
                isWriteSuccess = false;
                return isWriteSuccess;
            }
        }

        FileWriter fw = null;
        if(defApp == null || "".equals(defApp)) {
            if(SmartCameraActivity.CN.equals(defaultLanguage)) {
//                if(Alpha2Application.mBussinessVersion){
//                    defApp = Alpha2Application.BUSSINESS_CHAT;
//                }else{
//                    defApp = Alpha2Application.CHINESE_CHAT;
//                }

            } else {
        //        defApp = Alpha2Application.ENGLISH_CHAT;
            }
        }

        try {
            fw = new FileWriter(file);
            if(isUsingBase64) {
                fw.write(Base64.encodeToString(defaultLanguage.getBytes(), 0));
                fw.write("\r\n");
                fw.write(Base64.encodeToString(defApp.getBytes(), 0));
            } else {
                fw.write(defaultLanguage);
                fw.write("\r\n");
                fw.write(defApp);
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

    public String readConfigFile() {
        StringBuilder sb = new StringBuilder();
        File file = new File(SDPath + DIRECTORY + CONFIGURATION_NAME);
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
                    Log.d(TAG, "!!! str=" + str);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
