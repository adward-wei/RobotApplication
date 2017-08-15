package com.alpha2.camera.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.alpha2.camera.ui.SmartCameraActivity;
import com.ubtech.smartcamera.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by wzt on 2016/7/21.
 */
public class StringUtil {
    /** 一个Integer对应一个string或者string[]，即&lt;Integer, Object&gt;。 */
    public static HashMap<Integer, Object> stringCustom;
    /** 标识当前显示的语系；默认值为英文。 */
    public static String language = SmartCameraActivity.EN;

    public static String getLanguage() {
        return language;
    }

    public static void initLanguage(Context context) {
        stringCustom = readStringsXML(context, R.xml.english);
    }

    /** 获取指定id的字符串。 */
    public static String getString(int strId) {
        if(stringCustom != null) {
            return (String) stringCustom.get(strId);
        } else {
            return "";
        }
    }

    /** 获取指定id的字符串数组。 */
    public static String[] getStringArray(int strArrId) {
        return (String[]) stringCustom.get(strArrId);
    }

    public String ReadFile(String path){
        File file = new File(path);
        BufferedReader reader = null;
        String laststr = "";
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return laststr;
    }

    private static HashMap<Integer, Object> readStringsXML(Context context, int xmlId) {
        HashMap<Integer, Object> hashMap = new HashMap<Integer, Object>();
        Resources res = context.getResources();
        String pkg = context.getPackageName();
        XmlResourceParser xmlParser = context.getResources().getXml(xmlId);
        try {
            String tagName, attName, attValue, tagValue;
            int identifier = -1;
            int eventType = xmlParser.next();
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_DOCUMENT) {
                    // Log.d("ANDROID_LAB", "[Start document]");
                } else if (eventType == XmlResourceParser.END_DOCUMENT) {
                    // Log.d("ANDROID_LAB", "[End document]");
                } else if (eventType == XmlResourceParser.START_TAG) {
                    tagName = xmlParser.getName();
                    if ("string".equals(tagName)) {
                        attName = xmlParser.getAttributeName(0);
                        attValue = xmlParser.getAttributeValue(0);
                        eventType = xmlParser.next();
                        if (eventType == XmlResourceParser.TEXT) {
                            tagValue = xmlParser.getText();
                            // Log.d("ANDROID_LAB", "[Start tag]" + tagName +
                            // " " + attName + "="
                            // + attValue + " tagValue=" + tagValue);
                            identifier = res.getIdentifier(attValue, "string", pkg);
                            hashMap.put(identifier, tagValue);
                            // Log.d("ANDROID_LAB",
                            // Integer.toHexString(identifier) + " " + attValue
                            // + "=" + tagValue);
                        }
                    }
                } else if (eventType == XmlResourceParser.END_TAG) {
                    // Log.d("ANDROID_LAB", "[End tag]" + xmlParser.getName());
                } else if (eventType == XmlResourceParser.TEXT) {
                    // Log.d("ANDROID_LAB", "[Text]" + xmlParser.getText());
                }
                eventType = xmlParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    /**
     * 设置新的语系。<br/>
     * 根据新设定值对stringCustom进行更新。<br/>
     *
     * @param context
     *            调用者。
     * @param language
     *            语系名称。
     *
     * */
    public static void setLanguage(Context context, String language) {
        if (language.equals(SmartCameraActivity.CN)) {
            stringCustom = readStringsXML(context, R.xml.chinese);
            StringUtil.language = language;
        } else if (language.equals(SmartCameraActivity.EN)) {
            stringCustom = readStringsXML(context, R.xml.english);
            StringUtil.language = language;
        } else {
            if (StringUtil.language.equals(SmartCameraActivity.EN) == false) {
                stringCustom = readStringsXML(context, R.xml.english);
                StringUtil.language = SmartCameraActivity.EN;
            }
        }
    }
}
