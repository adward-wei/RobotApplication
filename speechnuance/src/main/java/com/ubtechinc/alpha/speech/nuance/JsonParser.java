/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.speech.nuance;

import com.google.gson.Gson;
import com.nuance.dragon.toolkit.cloudservices.recognizer.CloudRecognitionResult;
import com.nuance.dragon.toolkit.vocon.VoconResult;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.speech.dispatch.Slot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2017/2/28
 * @Description 解析工具类
 * @modifier
 * @modify_time
 */

public class JsonParser {

	public static String getCloudResult(CloudRecognitionResult result) {
		String ret = null;
		try {
			ret = result.getDictionary().getSequence("transcriptions").getString(0).value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String getAction(JSONObject obj) {
		try {
			if (obj.has("_items")) {
				JSONArray items = obj.getJSONArray("_items");
				JSONObject currentItem = items.getJSONObject(0);
				return currentItem.getString("_name");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getLocalResult(String result) {
		String content = "";
		try {
			JSONArray array = new JSONArray(result);
			JSONObject obj = array.getJSONObject(0);
			String rule = obj.get("_startRule").toString().replace("grammarall#", "");
			String action = getAction(obj).replace("grammarall#", "");
			content = result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return content;
	}

	public static int getAppId(String result) {
		int appId = 0;
		try {
			JSONArray array = new JSONArray(result);
			JSONObject obj = array.getJSONObject(0);
			String action = getAction(obj).replace("grammarall#", "");
			Pattern p = Pattern.compile("(\\d+)");
			Matcher m = p.matcher(action);
			if (m.find()) {
				appId = Integer.parseInt(m.group(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appId;
	}

	public static String parseLocalResult(VoconResult result) {
		int appId = 0;
		String ret = "";
		JSONArray array = result.getChoiceList();
		try {
			JSONObject obj = array.getJSONObject(0);
			String rule = obj.get("_startRule").toString().replace("grammarall#", "");
			String action = getAction(obj).replace("grammarall#", "");
			String speechContent = result.toString();
			Pattern p = Pattern.compile("(\\d+)");
			Matcher m = p.matcher(action);
			if (m.find()) {
				appId = Integer.parseInt(m.group(0));
			}
			Slot s = new Slot();
			s.setAppId(appId);
			s.setName(array.toString());
			s.setContent(speechContent);
			ret = new Gson().toJson(s);
			LogUtils.e("paul", "Local_Result:rule:" + rule + " action:" + action+ " tag:" + speechContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
