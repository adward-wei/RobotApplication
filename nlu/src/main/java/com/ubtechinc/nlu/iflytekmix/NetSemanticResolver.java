/*
 *
 *  *
 *  *  *
 *  *  * Copyright (c) 2008-2017 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *  *
 *  *
 *
 */
package com.ubtechinc.nlu.iflytekmix;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.ubtechinc.nlu.iflytekmix.pojos.Data;
import com.ubtechinc.nlu.iflytekmix.pojos.Semantic;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import timber.log.Timber;

 /**
 * @desc: 网络语义信息解析
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public class NetSemanticResolver extends MixSemanticResolver {
    private static final String TAG = NetSemanticResolver.class.getSimpleName();
    private Gson gson;

    public NetSemanticResolver(Context context) {
        super(context);
        gson = new Gson();
    }

    @Override
    public MixSemantic resolve(String jsonStr) {
        Timber.i("Net semantic resolver...");
        JSONTokener tokener = new JSONTokener(jsonStr);
        JSONObject joResult;
        NetSemantic semantic = new NetSemantic();
        try {
            joResult = new JSONObject(tokener);
            if (!joResult.has("rc")) return null;
            final int rc = joResult.getInt("rc");
            semantic.from = MixSemantic.FROM_SERVER;
            semantic.speechResult = joResult.getString("text");
            semantic.semanticValid = rc == 0;
            if (rc == 0 ){
                semantic.operation = joResult.getString("operation");
                semantic.service = joResult.getString("service");
                if (joResult.has("answer")){
                    JSONObject charObj = joResult.getJSONObject("answer");
                    semantic.answerText = charObj.getString("text");
                    //讯飞又修改数据返回格式了
                    if (isGoodJsonObject(semantic.answerText)){
                        JSONObject jsonObject = new JSONObject(semantic.answerText);
                        if (jsonObject.has("unknown")) {
                            semantic.answerText = jsonObject.getString("unknown");
                        }else {
                            semantic.answerText = "";
                        }
                    }
                }
                try {
                    if (joResult.has("semantic") && (joResult.get("semantic") instanceof JSONObject)) {
                        semantic.semantic = gson.fromJson(joResult.getJSONObject("semantic").toString(), Semantic.class);
                    }
                    if (joResult.has("data") && (joResult.get("data") instanceof JSONObject)) {
                        semantic.data = gson.fromJson(joResult.getJSONObject("data").toString(), Data.class);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            Log.w(TAG, "Resolver exception: " + e.getMessage());
            return semantic.speechResult != null ? semantic : null;
        }
        return semantic;
    }

     public static boolean isGoodJsonObject(String json) {
         if (TextUtils.isEmpty(json)) return false;
         try {
             new JsonParser().parse(json);
             new JSONObject(json);
             return true;
         } catch (JsonParseException e) {
             Log.w(TAG, "bad json: " + json);
             return false;
         } catch (JSONException e) {
             Log.w(TAG, "bad json: " + json);
             return false;
         }
     }
}
