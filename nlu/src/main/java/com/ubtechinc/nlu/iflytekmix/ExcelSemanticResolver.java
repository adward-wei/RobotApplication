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
import android.support.annotation.Nullable;
import android.util.Log;

import com.ubtechinc.nlu.utils.CosineSimilarAlgorithm;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


 /**
 * @desc: 本地语义解析: 根据excel中语义解析
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public class ExcelSemanticResolver extends MixSemanticResolver {
    private static final String TAG = ExcelSemanticResolver.class.getSimpleName();
    private static ExcelSemanticHelper helper;
    private static final byte[] lock = new byte[0];

    private double[] mSimilarValueArray;
    private CosineSimilarAlgorithm mCosineSimilarAlgorithm;
    private final String speechResult;

    public ExcelSemanticResolver(Context context, @Nullable String speechResult) {
        super(context);
        mCosineSimilarAlgorithm = new CosineSimilarAlgorithm();
        this.speechResult = speechResult;
        if (helper == null){
            synchronized (lock){
                if (helper == null) helper = new ExcelSemanticHelper(cxt);
            }
        }
    }

    @Override
    public MixSemantic resolve(String originStr) {
        Timber.i("Local semantic resolver...");
        if (!helper.isInited()) {
            helper.readConfig();
            helper.clear();
        }
        final List<ExcelSemantic> localSemantics = helper.getLocalSemantic();
        if (localSemantics == null) return null;
        ExcelSemantic semantic = null;
        mSimilarValueArray = new double[localSemantics.size()];
        ArrayList<ExcelSemantic> tempSemantics = new ArrayList<>(localSemantics);
        try {
            for (int i = 0; i < tempSemantics.size(); i++) {
                //找相似问题
                semantic =  tempSemantics.get(i);
                if (getMostSimilarityContents( i,semantic.speechResult, speechResult)) {
                    break;
                }else {
                    semantic = null;
                }
            }
            /** 没有完全一致的结果，则寻找相似度最高的结果 **/
            if(semantic == null) {
                int idx = getSimilarityIndex(0.8);
                if(idx > -1) {
                    semantic =  tempSemantics.get(idx);
                }
            }
        } catch (Exception e) {
            Timber.w("Exception: %s",e.getMessage());
        }

        if (semantic != null){
            semantic.service = ExcelSemantic.TypeNames[semantic.type];
            semantic.from = MixSemantic.FROM_EXCEL;
            semantic.semanticValid = true;
        }

        return semantic;
    }

    private boolean getMostSimilarityContents(int row, String contents, String recognizeResult) {
        //先计算该问题的相似度
        double similarity = mCosineSimilarAlgorithm.getSimilarity(contents, recognizeResult);
        if(similarity > 0.95) {//相似度很高则直接返回
            return true;
        } else {//相似度不高则先存起来，后面遍历相似度最高的返回结果
            mSimilarValueArray[row] = similarity;
        }
        return false;
    }

    private int getSimilarityIndex(double similarity) {
        int correctIndex = -1;
        int idx ;
        int length = mSimilarValueArray.length;
        double maxSimilarity = 0.0;
        for(idx = 0; idx < length; idx++) {
            double value = mSimilarValueArray[idx];
            if(value > maxSimilarity ) {
                maxSimilarity = value;
                correctIndex = idx;
            }
        }
        if(maxSimilarity > similarity) {
            Log.v(TAG, "maxSimilarity=" + maxSimilarity);
            return correctIndex;
        } else {
            return -1;
        }
    }
}
