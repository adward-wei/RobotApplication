package com.ubtechinc.alpha.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.robotinfo.RobotLanguage;
import com.ubtechinc.alpha2services.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wzt
 * @date 2017/5/25
 * @Description 中英文字符串工具类
 * @modifier
 * @modify_time
 */

public class StringUtil {
    private static final String TAG = "StringUtil";
    /** 一个Integer对应一个string或者string[]，即&lt;Integer, Object&gt; */
    public static HashMap<Integer, Object> stringCustom;
    /** 标识当前显示的语系；默认值为英文。 */
    public static String language = RobotLanguage.EN;

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
//            return "";
            return AlphaApplication.getContext().getString(strId);
        }
    }

    /** 获取指定id的字符串数组。 */
    public static String[] getStringArray(int strArrId) {
        return (String[]) stringCustom.get(strArrId);
    }

    private static String[] readStringArray(XmlResourceParser xmlParser)
            throws XmlPullParserException, IOException {
        String[] arr = null;
        LinkedList<String> list = new LinkedList<String>();
        String tagName, tagValue;
        while (true) {
            xmlParser.next();
            tagName = xmlParser.getName();
            if ("string-array".equals(tagName)) {
                arr = new String[list.size()];
                // 这个函数设计得好奇怪，传参和返参都一样。
                // list.toArray(arr);作用同下：
                arr = list.toArray(arr);
                break;
            }
            tagName = xmlParser.getName();
            if ((xmlParser.getEventType() == XmlResourceParser.START_TAG) && tagName.equals("item")) {
                xmlParser.next();
                tagValue = xmlParser.getText();
                list.add(tagValue);
                // Log.d("ANDROID_LAB", tagName + "=" + tagValue);
            }
        }
        return arr;
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
                }else if (eventType == XmlResourceParser.START_TAG) {
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
                    } else if ("string-array".equals(tagName)) {
                        attName = xmlParser.getAttributeName(0);
                        attValue = xmlParser.getAttributeValue(0);
                        identifier = res.getIdentifier(attValue, "array", pkg);
                        String[] arr = readStringArray(xmlParser);
                        hashMap.put(identifier, arr);
                        // Log.d("ANDROID_LAB", "[Start tag]" + tagName + " " +
                        // attName + "="
                        // + attValue);
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
        if (language.equals(RobotLanguage.CN)) {
            stringCustom = readStringsXML(context, R.xml.chinese);
            StringUtil.language = language;
        } else if (language.equals(RobotLanguage.EN)) {
            stringCustom = readStringsXML(context, R.xml.english);
            StringUtil.language = language;
        } else {
            
        }
    }

    public static boolean isContainsChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())    {
            flg = true;
        }
        return flg;
    }
    //设置系统的语言
    public static void setLanguage(Locale locale) {
        LogUtils.i(locale.toString());
        try {
            Object objIActMag, objActMagNative;
            Class clzIActMag = Class.forName("android.app.IActivityManager");
            Class clzActMagNative = Class.forName("android.app.ActivityManagerNative");
            Method mtdActMagNative$getDefault = clzActMagNative.getDeclaredMethod("getDefault");
            // IActivityManager iActMag = ActivityManagerNative.getDefault();
            objIActMag = mtdActMagNative$getDefault.invoke(clzActMagNative);
            // Configuration config = iActMag.getConfiguration();
            Method mtdIActMag$getConfiguration = clzIActMag.getDeclaredMethod("getConfiguration");
            Configuration config = (Configuration) mtdIActMag$getConfiguration.invoke(objIActMag);
            config.locale = locale;
            // iActMag.updateConfiguration(config);
            // 此处需要声明权限:android.permission.CHANGE_CONFIGURATION
            // 会重新调用 onCreate();
            Class[] clzParams = { Configuration.class };
            Method mtdIActMag$updateConfiguration = clzIActMag.getDeclaredMethod(
                    "updateConfiguration", clzParams);
            mtdIActMag$updateConfiguration.invoke(objIActMag, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
