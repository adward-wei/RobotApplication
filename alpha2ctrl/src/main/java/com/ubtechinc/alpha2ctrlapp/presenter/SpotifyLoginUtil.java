package com.ubtechinc.alpha2ctrlapp.presenter;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.util.Log;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class SpotifyLoginUtil {
	 public static final String TAG=SpotifyLoginUtil.class.getSimpleName();

     public static byte[] packetTokenStrToBytes(String token,int expireIn) 
     {
    	 byte[] datas=null;
    	 StringBuilder sb = new StringBuilder();
     	 sb.append("{"
     			  +"\"robotApps\": ["
     	          +"{"
     	          +"\"item\": ["
     	          +"{"
     	          +"\"editText\": \"1\","
     	          +"\"id\": \"0\","
     	          +"\"textView\": \"Have Login\""
     	          +"},"
     	          +"{"
    	          +"\"editText\": \""+token+"\","
    	          +"\"id\": \"1\","
    	          +"\"textView\": \"Token \""
    	          +"},"
     	          +"{"
     	          +"\"editText\": \""+expireIn+"\","
     	          +"\"id\": \"2\","
     	          +"\"textView\": \"Token expire\""
     	          +"}"
     	          +"],"
     	          +"\"marginTop\": \"1\""
     	          +"}"
     	          +"],"
     	          +"\"version\": \"1.0.0.0\""
     	          +"}"
     	);
     	try {
			datas=sb.toString().getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	return datas;
     	
     }
     public static int getLoginresult(String json)
     {
    	 int isLogin=0;
    	 try {
			JSONObject js=new JSONObject(json);
			JSONArray modelJsArr=js.getJSONArray("robotApps");
			JSONArray itemJsArr=(modelJsArr.getJSONObject(0)).getJSONArray("item");
			JSONObject loginJs=itemJsArr.getJSONObject(0);
			isLogin=loginJs.getInt("editText");
			Log.i(TAG, "isLogin=="+isLogin);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 return isLogin;
     }
     public static boolean isExist(String cmdName,Context context){
         ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
         List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
         String cmpNameTemp = null;
         if(null != runningTaskInfos){
                 cmpNameTemp=(runningTaskInfos.get(0).topActivity).toString();
                 Logger.d("isExist","isExist:"+cmpNameTemp);
         }
        if(null == cmpNameTemp)return false;
        return cmpNameTemp.contains(cmdName);
 }
}
