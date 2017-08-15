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
import android.util.Log;

import com.ubtechinc.alpha.model.speech.SlotValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import timber.log.Timber;

/**
 * @desc: 离线语义信息解析
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public class OfflineSemanticResolver extends MixSemanticResolver{
    public static final String TAG = OfflineSemanticResolver.class.getSimpleName();

    public OfflineSemanticResolver(Context context){
        super(context);
    }

    @Override
    public MixSemantic resolve(String originStr) {
        Timber.i("Offline semantic resolver...");
        OfflineSemantic semantic = new OfflineSemantic();
        JSONTokener tokener = new JSONTokener(originStr);
        JSONObject joResult;
        try {
            // FIXME: 2017/5/22 代码来自原主服务
            joResult = new JSONObject(tokener);
            if(!joResult.has("ls")) return null;
            semantic.from = MixSemantic.FROM_LOCAL;
            JSONArray words = joResult.getJSONArray("ws");
            StringBuilder speechResult = new StringBuilder();
            for (int i = 0; i < words.length(); i++) {
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                for (int j = 0; j < items.length(); j++) {
                    JSONObject obj = items.getJSONObject(j);
                    if (obj.getString("w").contains("nomatch")) {
                        return null;
                    }
                    speechResult.append(obj.getString("w"));
                }
                if (i == 0) {
                    String slot = words.getJSONObject(i).getString("slot");
                    @SlotValue.Type int slotType = SlotValue.valueToType(slot);
                    semantic.slot = slotType;
                }
            }
            semantic.speechResult = speechResult.toString();
            final int score = Integer.parseInt(joResult.getString("sc"));
            semantic.semanticValid = score >= 50;
        } catch (JSONException e) {
            Log.w(TAG, "Resolver exception: " +e.getMessage());
            return semantic;
        }
        return semantic;
    }
}
