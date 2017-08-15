/*
 *
 *  *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.speech.ifytek;

import com.ubtechinc.alpha.speech.dispatch.Slot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date 2017/2/28
 * @author paul.zhang@ubtrobot.com
 * @Description 解析工具类
 * @modifier
 * @modify_time
 */

public class JsonParser {

	public static String parseIatResult(String json) {
		if (isOfflineGrammar(json)){
			return getOfflineSpeechWords(json);
		}else {
			return getNetSpeechWords(json);
		}
	}

	public static int getScoreOfGrammar(String jsonStr) {
		int score = -1;
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(jsonStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsonObj.has("sc")) {
			score = jsonObj.optInt("sc");
		}
		return score;
	}

	public static boolean isOfflineGrammar(String jsonStr){
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(jsonStr);
		}catch (JSONException e){
			e.printStackTrace();
		}
		return jsonObj.has("ls");
	}

	public static Slot getSlotFromAsrResult(String json) {
		Slot slot = null;
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);
			JSONArray words = joResult.optJSONArray("ws");
			if (words != null && words.length() == 1) {
				// 转写结果词，默认使用第一个结果
				JSONObject item = words.getJSONObject(0);
				if (item.opt("slot") == null) return null;
				String slotName = item.getString("slot");
//				int slotId = getAppId(slotName);
				JSONArray cwJsArr = item.getJSONArray("cw");
				int len = cwJsArr.length();
				String content = cwJsArr.getJSONObject(0).getString("w");

				slot = new Slot();
//				slot.setAppId(slotId);
				slot.setName(slotName);
				slot.setContent(content);

				int[] ids;
				if (slotName.equals("<appContext>")) {
					ids = new int[len];
					for (int i = 0; i < len; i++) {
						ids[i] = cwJsArr.getJSONObject(i).getInt("id");
					}
					slot.setIds(ids);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			slot = null;
		}
		return slot;
	}

	//获取用户说的话
	public static String getOfflineSpeechWords(String json){
		JSONTokener tokener = new JSONTokener(json);
		JSONObject joResult;
		try {
			joResult = new JSONObject(tokener);
			if (joResult.opt("ws") == null) return "";
			JSONArray words = joResult.getJSONArray("ws");
			StringBuilder speechResult = new StringBuilder();
			for (int i = 0; i < words.length(); i++) {
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				for (int j = 0; j < items.length(); j++) {
					JSONObject obj = items.getJSONObject(j);
					if (obj.getString("w").contains("nomatch")) {
						return "";
					}
					speechResult.append(obj.getString("w"));
				}
			}
			return speechResult.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 需先判断是不是网络语义才能调用该方法
	 * @param result
	 * @return
	 */
	public static String getNetSpeechWords(String result) {
		JSONTokener tokener = new JSONTokener(result);
		JSONObject joResult;
		try {
			joResult = new JSONObject(tokener);
			if (joResult.opt("text") == null) return "";
			return joResult.getString("text");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getCmdOnline(String jsonStr) {
		String text;
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(jsonStr);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		text = jsonObj.optString("text");
		return text;
	}

	public static int getAppId(String slot) {
		int appId = -1;
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(slot);
		if (m.find()) {
			appId = Integer.parseInt(m.group(0));
		}
		return appId;
	}
}
