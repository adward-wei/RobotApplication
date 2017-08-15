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

package com.ubtechinc.alpha.cmds.header;

import com.ubtechinc.alpha.cmds.SerialCommand;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;

/**
 * @desc : 头部命令集
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/19
 * @modifier:
 * @modify_time:
 */

public abstract class HeadCmds extends SerialCommand{
    // 头部命令
    /**耳朵灯*/
    public static final byte CMD_OPEN_LED_EAR = 0x01;
    /**眼睛灯*/
    public static final byte CMD_OPEN_LED_EYE = 0x02;
    /**嘴灯*/
    public static final byte CMD_OPEN_LED_MOUTH = 0x03;
    /**设置红外距离（0~15cm）*/
    @Deprecated
    public static final byte CMD_SET_INFRARED = 0x04;
    /**开机命令*/
    public static final byte CMD_STARTUP = 0x05;
    /**暂停执行*/
    @Deprecated
    public static final byte CMD_PAUSE = 0x06;
    /**继续执行*/
    @Deprecated
    public static final byte CMD_RESUME = 0x07;
    /**停止执行*/
    @Deprecated
    public static final byte CMD_STOP = 0x08;
    /**耳朵灯：呼吸效果*/
    public static final byte CMD_EAR_LED_BREATH = 0x0A;
    /**眼睛灯：眨眼效果*/
    public static final byte CMD_EYE_LED_BLINK = 0x0B;
    /**跳舞（android->stm32） 参数1: 0------跳舞结束 1------跳舞开始*/
    @Deprecated
    public static final byte CMD_DANCE = 0x15;
    /**关机命令*/
    public static final byte CMD_SHUTDOWN = 0x16;
    /** 风扇*/
    @Deprecated
    public static final byte CMD_CONTROL_BLOWER = 0x18;
    /** 功放 **/
    @Deprecated
    public static final byte CMD_SEND_AMP = 0X19;
    /**蓝牙状态*/
    public static final byte CMD_OPEN_BT =0x20;
    /** 系统更新 **/
    @Deprecated
    public static final byte CMD_SYSTEM_REBOOT = 0X21;
    /**读取lis3dh*/
    public static final byte CMD_READ_LIS3DH = 0x25;
    /**设置mic信号bypass*/
    public static final byte CMD_CONTROL_BYPASS =0x27;
    /**开始升级 参数:1---4,文件大小,单位字节.*/
    public static final byte CMD_START_UPGRADE = 0x30;
    /**0x31:升级数据包 参数:1-2,数据包page 参数:3-66,共64字节数据,不到64字节用00填满*/
    public static final byte CMD_UPGRADE_PAGE = 0X31;
    /**0x32:结束升级*/
    public static final byte CMD_END_UPGRADE = 0x32;
    /** 读头部版本 **/
    public static final byte CMD_READ_VERSION = 0x33;


    //###########################################################
    //###########################################################
    //######################  硬件主动上报  ######################
    //###########################################################
    //###########################################################

    /**有障碍物*/
    @Deprecated
    public static final byte HEADER_SEND_OBSTACLE = (byte) 0x80;
    /**按键:取值：01-06：对应按键的短按和长按*/
    public static final byte HEADER_SEND_KEY = (byte) 0x81;
    /**数据发到胸部（stm32->android）*/
    public static final byte HEADER_SEND_TRANSFORM = (byte) 0x96;
    /** 高温报警 **/
    @Deprecated
    public static final byte HEADER_HIGHT_TEMP = (byte) 0x84;
    /**声音方向检测*/
    public static final byte HEADER_SOUND_DIRECTION=(byte) 0x82;
    /**跌到检测*/
    @Deprecated
    public static final byte HEADER_FALL_DERECTION=(byte) 0x83;


    //##########################################################
    //####################### 5麦机器特殊命令 ####################
    //##########################################################

    /** 5麦机器前按键短按 **/
    public static final int MIC5_KEY_FORMOR_SHORT = 0x3c;
    /** 5麦机器前按键长按 **/
    public static final int MIC5_KEY_FORMOR_LONG = 0x40;
    /** 5麦机器后按键短按 **/
    public static final int MIC5_KEY_BACK_SHORT = 0x3d;
    /** 5麦机器后按键短按 **/
    public static final int MIC5_KEY_BACK_LONG = 0x41;
    /** 5麦机器拍头 **/
    public static final int MIC5_KEY_INTERRUPT = 0x42;


    public HeadCmds(SerialCommandReceiver receiver, byte[] param) {
        super(receiver, param);
    }

    @Override
    protected @SerialConstants.SerialType int getSerialType() {
        return SerialConstants.TYPE_HEADER;
    }
}
