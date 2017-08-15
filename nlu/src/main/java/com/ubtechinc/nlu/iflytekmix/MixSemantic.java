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

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @desc: 混合语义：语义信息来自离线、网络、本地excel三者取一？
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public class MixSemantic {

    public static final String[] froms = {"预置","离线","科飞服务器", "APK内的excel", "UBT服务器","Emot", "图灵"};
    public static final int FROM_LOCAL = 1;
    public static final int FROM_SERVER = 2;
    public static final int FROM_EXCEL =3;
    public static final int FROM_UBT = 4;
    public static final int FROM_EMOT = 5;
    public static final int FROM_TUNLING = 6;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {FROM_LOCAL, FROM_SERVER, FROM_EXCEL, FROM_UBT, FROM_EMOT, FROM_TUNLING})
    public @interface From {}

    @From
    int from;

    boolean semanticValid;

    @NonNull String speechResult;

    @Nullable String answerText;

    public int getFrom() {
        return from;
    }

    public boolean isSemanticValid() {
        return semanticValid;
    }

    public String getSpeechResult() {
        return speechResult;
    }

    public void setSpeechResult(@NonNull String speechResult) {
        this.speechResult = speechResult;
    }

    @Nullable
    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(@Nullable String answerText) {
        this.answerText = answerText;
    }

    public void setFrom(@From int from) {
        this.from = from;
    }

    public void setSemanticValid(boolean semanticValid) {
        this.semanticValid = semanticValid;
    }

    public String fromStr(){
        return froms[from];
    }
}
