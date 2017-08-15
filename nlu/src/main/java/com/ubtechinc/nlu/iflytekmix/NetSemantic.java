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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ubtechinc.nlu.iflytekmix.pojos.Data;
import com.ubtechinc.nlu.iflytekmix.pojos.Semantic;

/**
 * @desc: 通过网络得到的科飞语义信息
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public class NetSemantic extends MixSemantic {
    @NonNull
    String operation;
    @NonNull
    String service;
    @Nullable
    Semantic semantic;
    @Nullable
    Data data;

    @NonNull
    public String getOperation() {
        return operation;
    }

    @NonNull
    public String getService() {
        return service;
    }

    @Nullable
    public Semantic getSemantic() {
        return semantic;
    }

    @Nullable
    public Data getData() {
        return data;
    }
}
