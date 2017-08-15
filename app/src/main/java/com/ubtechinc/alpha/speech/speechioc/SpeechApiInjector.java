package com.ubtechinc.alpha.speech.speechioc;

import android.content.Context;

import com.ubtechinc.alpha.speech.SpeechContext;
import com.ubtechinc.alpha2services.BuildConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @desc : 语音注入module
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/8/10
 * @modifier:
 * @modify_time:
 */
public class SpeechApiInjector {
    private final Context mContext;

    public SpeechApiInjector(Context context) {
        this.mContext = context;
    }

    public SpeechContext provideSpeechApi() {
        try {
            Class<SpeechContext> clazz = (Class<SpeechContext>) Class.forName(BuildConfig.SpeechEngine);
            Constructor<SpeechContext> con = clazz.getConstructor(Context.class);
            return con.newInstance(mContext);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SpeechContext provideSpeechApi(String className){
        try {
            Class<SpeechContext> clazz = (Class<SpeechContext>) Class.forName(className);
            Constructor<SpeechContext> con = clazz.getConstructor(Context.class);
            return con.newInstance(mContext);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}

