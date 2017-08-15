package com.ubtechinc.alpha.behavior;

import android.text.TextUtils;
import android.util.Log;

import com.ubtech.utilcode.utils.CloseUtils;
import com.ubtech.utilcode.utils.thread.Future;
import com.ubtech.utilcode.utils.thread.ThreadPool;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/5
 * @modifier:
 * @modify_time:
 */

public final class BehaviorInflater {

    public static Behavior loadBehaviorFromXml(String path){
        if(TextUtils.isEmpty(path)) return null;
        File file = new File(path);
        final FileInputStream fin;
        try {
            fin = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.e("", e.getMessage());
            return null;
        }
        Future<Behavior> future = ThreadPool.getInstance().submit(new ThreadPool.Job<Behavior>() {
            @Override
            public Behavior run(ThreadPool.JobContext jc) {
                SaxBehaviorService saxBehaviorService = new SaxBehaviorService();
                try {
                    return saxBehaviorService.getBehavior(fin);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    CloseUtils.closeIOQuietly(fin);
                }
                return null;
            }
        });
        return future.get();
    }

    public static void loadBehaviorFromXml(String path, final BehaviorLoadListener listener){
        if(TextUtils.isEmpty(path)){
            if (listener != null){
                listener.onLoadCompleted(null);
            }
            return ;
        }
        File file = new File(path);
        final FileInputStream fin;
        try {
            fin = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.e("", e.getMessage());
            if (listener != null){
                listener.onLoadCompleted(null);
            }
            return;
        }
        ThreadPool.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                SaxBehaviorService saxBehaviorService = new SaxBehaviorService();
                try {
                    Behavior ret = saxBehaviorService.getBehavior(fin);
                    if (listener != null){
                        listener.onLoadCompleted(ret);
                    }
                    return;
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    CloseUtils.closeIOQuietly(fin);
                }
                if (listener != null){
                    listener.onLoadCompleted(null);
                }
            }
        });
    }
}
