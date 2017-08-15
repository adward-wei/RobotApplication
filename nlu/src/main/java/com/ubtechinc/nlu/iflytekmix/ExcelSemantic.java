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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * @desc: 在Apk中用excel保存了一些语义
 * @author: Logic
 * @email : 2091843903@qq.com
 * @time  : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public class ExcelSemantic extends NetSemantic {
    public static  final int TYPE_NONE = -1;
    public static  final int TYPE_JOKE = 0;
    public static  final int TYPE_ROBOT_ACTION = 1;
    public static  final int TYPE_STORY = 2;
    public static  final int TYPE_CHAT = 3;
    public static  final int TYPE_FUNCTION = 4;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {TYPE_NONE, TYPE_JOKE,TYPE_ROBOT_ACTION,TYPE_STORY,TYPE_CHAT,TYPE_FUNCTION})
    public @interface LocalSemanticType{}
    public static final String[] TypeNames = {
                                            "JOKE",
                                            "ROBOT_ACTION",
                                            "STORY",
                                            "CHAT",
                                            "FUNCTION",};

    @NonNull
    final ArrayList<String> answers = new ArrayList<>();

    String slot;

     @LocalSemanticType int type;

    public static @LocalSemanticType int namesToType(String name){
        @LocalSemanticType int type = TYPE_NONE;
        for (@LocalSemanticType int i = 0 ; i < TypeNames.length; i ++ ){
            if (TypeNames[i].equalsIgnoreCase(name)){
                type = i;
            }
        }
        return type;
    }

    public int getType() {
        return type;
    }

    @NonNull
    public ArrayList<String> getAnswers() {
        return answers;
    }

    public String getSlot() {
        return slot;
    }
}


