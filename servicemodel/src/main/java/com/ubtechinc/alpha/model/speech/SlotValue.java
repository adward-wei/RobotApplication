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
package com.ubtechinc.alpha.model.speech;

import android.support.annotation.IntDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @desc: bnf语法槽
 * @author: Logic.peng
 * @email : 2091843903@qq.com
 * @time : 2017/3/22
 * @modifier:
 * @modify_time:
 */

public final class SlotValue {
    public static final int NULL = 0;
    public static final int NAME = 1;
    public static final int AGE = 2;
    public static final int WHERE = 3;
    public static final int WIFI = 4;
    public static final int SERVER = 5;
    public static final int MASTER = 6;
    public static final int POWER = 7;
    public static final int APP = 8;
    public static final int CAPACITY = 9;
    public static final int SLEEP = 10;
    public static final int PICTURE = 11;
    public static final int CONNECT = 12;
    public static final int VOLUMEUP = 13;
    public static final int VOLUMEDOWN = 14;
    public static final int POWEROFF = 15;
    public static final int FACE = 16;
    public static final int BEGINCONNECT = 17;
    public static final int TIME = 18;
    public static final int DANCE = 19;
    public static final int STORY = 20;
    public static final int NOD = 21;
    public static final int SHAKE = 22;
    public static final int WELCOME = 23;
    public static final int RIGHTBECK = 24;
    public static final int LEFTBECK = 25;
    public static final int STOOP = 26;
    public static final int LOOKUP = 27;
    public static final int LEFTLEGLIFTS = 28;
    public static final int RIGHTLEGLIFTS = 29;
    public static final int KONGFU = 30;
    public static final int RIGHTKICK = 31;
    public static final int LEFTKICK = 32;
    public static final int HEADDROP = 33;
    public static final int RIGHTHANDSUP = 34;
    public static final int LEFTHANDSUP = 35;
    public static final int HANDSUP = 36;
    public static final int HANDSRAISE = 37;
    public static final int ACTINGCUTE = 38;
    public static final int WINK = 39;
    public static final int SMILE = 40;
    public static final int CROUCH = 41;
    public static final int STANDUP = 42;
    public static final int DENY = 43;
    public static final int APPLAUD = 44;
    public static final int BACKWALK = 45;
    public static final int FRONTWALK = 46;
    public static final int FRONTLOOK = 47;
    public static final int BACKLOOK = 48;
    public static final int RIGHTLOOK = 49;
    public static final int LEFTLOOK = 50;
    public static final int RIGHTWALK = 51;
    public static final int LEFTWALK = 52;
    public static final int RIGHTPUNCH = 53;
    public static final int LEFTPUNCH = 54;
    public static final int RIGHTHEADUP = 55;
    public static final int LEFTHEADUP = 56;
    public static final int RIGHTTURN = 57;
    public static final int LEFTTURN = 58;
    public static final int SHAKEHANDS = 59;
    public static final int BORING = 60;
    public static final int AGREE = 61;
    public static final int SAD = 62;
    public static final int THINKING = 63;
    public static final int HAPPY = 64;
    public static final int GREET = 65;
    public static final int LAUGH = 66;
    public static final int HEARTTIRED = 67;
    public static final int HAPPYNEWYEAR = 68;
    public static final int HUM = 69;
    public static final int GRIEVANCE = 70;
    public static final int PERFECT = 71;
    public static final int SHARP = 72;
    public static final int ROCK = 73;
    public static final int TFBOY = 74;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {
            NULL,// 无结果
            NAME,// 本地语法意图
            AGE,
            WHERE,//来自哪里
            WIFI,
            SERVER,
            MASTER,
            POWER,
            APP,
            CAPACITY,
            SLEEP,
            PICTURE,
            CONNECT,
            VOLUMEUP,
            VOLUMEDOWN,
            POWEROFF,
            FACE,
            BEGINCONNECT,
            TIME,
            DANCE,
            STORY,
            NOD,
            SHAKE,
            WELCOME,
            RIGHTBECK,
            LEFTBECK,
            STOOP,
            LOOKUP,
            LEFTLEGLIFTS,
            RIGHTLEGLIFTS,
            KONGFU,
            RIGHTKICK,
            LEFTKICK,
            HEADDROP,
            RIGHTHANDSUP,
            LEFTHANDSUP,
            HANDSUP,
            HANDSRAISE,
            ACTINGCUTE,
            WINK,
            SMILE,
            CROUCH,
            STANDUP,
            DENY,
            APPLAUD,
            BACKWALK,
            FRONTWALK,
            FRONTLOOK,
            BACKLOOK,
            RIGHTLOOK,
            LEFTLOOK,
            RIGHTWALK,
            LEFTWALK,
            RIGHTPUNCH,
            LEFTPUNCH,
            RIGHTHEADUP,
            LEFTHEADUP,
            RIGHTTURN,
            LEFTTURN,
            SHAKEHANDS,
            BORING,
            AGREE,
            SAD,
            THINKING,
            HAPPY,
            GREET,
            LAUGH,
            HEARTTIRED,
            HAPPYNEWYEAR,
            HUM,
            GRIEVANCE,
            PERFECT,
            SHARP,
            ROCK,
            TFBOY,})

    public @interface Type {
    }

    public static final String[] VALUES = {
            "",
            "<name>",
            "<age>",
            "<where>",
            "<wifi>",
            "<server>",
            "<master>",
            "<power>",
            "<app>",
            "<capacity>",
            "<sleep>",
            "<picture>",
            "<connect>",
            "<volumeup>",
            "<volumedown>",
            "<poweroff>",
            "<face>",
            "<beginconnect>",
            "<time>",
            "<dance>",
            "<story>",
            "<nod>",
            "<shake>",
            "<welcome>",
            "<rightbeck>",
            "<leftbeck>",
            "<stoop>",
            "<lookup>",
            "<leftleglifts>",
            "<rightleglifts>",
            "<kongfu>",
            "<rightkick>",
            "<leftkick>",
            "<headdrop>",
            "<righthandsup>",
            "<lefthandsup>",
            "<handsup>",
            "<handsraise>",
            "<actingcute>",
            "<wink>",
            "<smile>",
            "<crouch>",
            "<standup>",
            "<deny>",
            "<applaud>",
            "<backwalk>",
            "<frontwalk>",
            "<frontlook>",
            "<backlook>",
            "<rightlook>",
            "<leftlook>",
            "<rightwalk>",
            "<leftwalk>",
            "<rightpunch>",
            "<leftpunch>",
            "<rightheadup>",
            "<leftheadup>",
            "<rightturn>",
            "<leftturn>",
            "<shakehands>",
            "<boring>",
            "<agree>",
            "<sad>",
            "<thinking>",
            "<happy>",
            "<greet>",
            "<laugh>",
            "<hearttired>",
            "<happynewyear>",
            "<hum>",
            "<grievance>",
            "<perfect>",
            "<sharp>",
            "<rock>",
            "<tfboy>",
    };

    public static
    @Type
    int valueToType(String v) {
        if (TextUtils.isEmpty(v)) return NULL;
        for (@Type int i = 1; i < VALUES.length; i++) {
            if (VALUES[i].equalsIgnoreCase(v)) {
                return i;
            }
        }
        return NULL;
    }

    public static boolean isDefaultSlot(@Type int value){
        return (value == SlotValue.WIFI || value == SlotValue.SERVER ||
                value == SlotValue.MASTER || value == SlotValue.POWER ||
                value == SlotValue.APP || value == SlotValue.CAPACITY ||
                value == SlotValue.SLEEP || value == SlotValue.PICTURE ||
                value == SlotValue.CONNECT || value == SlotValue.VOLUMEUP ||
                value == SlotValue.VOLUMEDOWN || value == SlotValue.POWEROFF ||
                value == SlotValue.BEGINCONNECT);
    }
}
