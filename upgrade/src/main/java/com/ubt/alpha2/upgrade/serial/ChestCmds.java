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

package com.ubt.alpha2.upgrade.serial;

/**
 * @desc : 1、命令以问答模式进行
 *         2、大于0x80命令是硬件主动产生的:以CHES开头
 *         3、软件发送的命令：以CMD开头
 *         4、设置命令，需要根据命令参数判断是否结束
 *         5、读取命令，以串口返回数据则表明命令结束
 *
 *
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/18
 * @modifier:
 * @modify_time:
 */
public class ChestCmds{
    /**
     * 胸部开机命令
     */
    public static final byte CMD_START = 0x01;//
    /**
     * 胸部关机命令
     */
    public static final byte CMD_STOP = 0x02;
    /**
     * 舵机写数据: 指定一组舵机按照给定时间移动同时转动
     */
    public static final byte CMD_SEND_DATA_TO_MOTOR = 0x03;
    /**
     * 设置舵机电量和距离：参数1，电量(10表示10%);参数2, 距离(单位cm),范围(10~50)
     */
    public static final byte CMD_SET_POWER_DISTANCE = 0x04;
    /**
     * 设置单个舵机角度 参数：id号(1BYTE) 参数：角度(2BYTE) 高位在前，低位在后
     */
    public static final byte CMD_SET_MOTOR_ANGLE = 0x05;
    /**
     * 读取舵机角度，参数：ID(1b)
     */
    public static final byte CMD_READ_MOTOR_ANGLE = 0x06;
    /**
     * 暂停命令：硬件未实现
     */
    @Deprecated
    public static final byte CMD_PAUSE_MOTOR = 0x07;
    /**
     * 继续执行：硬件未实现
     */
    @Deprecated
    public static final byte CMD_RESUME_MOTOR = 0x08;
    /**
     * 停止命令：硬件未实现
     */
    @Deprecated
    public static final byte CMD_STOP_MOTOR = 0x09;
    /**
     * 修改id: 参数1：旧id号， 参数2：新ID号
     */
    public static final byte CMD_MODIFY_ID = 0x0A;
    /**
     * 设置舵机led灯：参数1：舵机ID， 参数2：0--关闭 1--打开
     */
    @Deprecated
    public static final byte CMD_SET_MOTORLED = 0x0B;
    /**
     * 设置校正角度:参数1：舵机id, 参数2：角度高8位，参数3：角度低8位
     */
    public static final byte CMD_SET_ADJUST_ANGLE = 0x0C;
    /**
     * 读取校正角度：参数1：ID
     */
    public static final byte CMD_READ_ADJUST_ANGLE = 0x0D;
    /**
     * 读取舵机版本：参数1：舵机id
     */
    public static final byte CMD_READ_MOTOR_VERSION = 0x0E;
    /**
     * 升级舵机开始：参数1：单个升级时舵机ID 参数2-5：文件大小 参数6：批量升级时舵机id个数 参数7-x：批量升级的舵机id
     * pc端使用
     */
    public static final byte CMD_UPGRADE_MOTOR_START = 0x10;
    /**
     * PC端使用的升级命令
     */
    public static final byte CMD_UPGRADE_MOTOR_PAGE = 0x11;
    /**
     *  心跳命令：未实现
     */
    @Deprecated
    public static final byte CMD_HEARTBEAT = 0x13;
    /**
     * wifi连接（android->stm32） 参数1:0----wifi断开连接 1----wifi已连接
     */
    public static final byte CMD_SET_WIFI_STATUS = 0x14;
    /**
     * 跳舞（android->stm32） 参数1: 0------跳舞结束 1------跳舞开始
     */
    @Deprecated
    public static final byte CMD_DANCE = 0x15;
    /** 蓝牙连接 1----bt连接 0----bt断开 **/
    @Deprecated
    public static final byte CMD_SET_BT_STATUS = 0x16;
    /**
     * 不掉电回读舵机角度:参数同0x06
     */
    public static final byte CMD_READ_BACK_MOTOR_ANGLE = 0x17;
    /**
     * 校准压力零点值：参数1：0--左右手 1--右手
     */
    @Deprecated
    public static final byte CMD_ADJUST_PRESSZERO_VALUE = 0x18;
    /**
     * 开关舵机电源 参数1: 0-------关闭舵机电源 1-------打开舵机电源
     */
    public static final byte CMD_SET_MOTOR_POWER = 0x19;
    /**
     * 闹钟设置：参数1：闹钟状态，0--未结束 1--结束 2--删除所有
     *          参数2：关机状态， 0--关机 1--保持开机
     *          参数3：是否重复， 0--不重复，1--按天重复， 2--按星期重复
     *          参数4：年
     *          参数5：月
     *          参数6：日
     *          参数7：星期
     *          参数8：时
     *          参数9：分
     */
    public static final byte CMD_SET_ALARM = 0x1A;
    /**
     *闹钟结束：参数1：年
     *         参数2：月
     *         参数3：日
     *         参数4：星期
     *         参数5：时
     *         参数6：分
     */
    public static final byte CMD_END_ALARM = 0x1B;
    /**
     *时间校准：参数1：年
     *         参数2：月
     *         参数3：日
     *         参数4：星期
     *         参数5：时
     *         参数6：分
     *         参数7：秒
     */
    public static final byte CMD_AJUST_TIME = 0x1C;
    /**
     * 读取时间：参数1：年
     *         参数2：月
     *         参数3：日
     *         参数4：星期
     *         参数5：时
     *         参数6：分
     *         参数7：秒
     *
     */
    public static final byte CMD_READ_TIME = 0x1D;
    /**
     * 读取闹钟：参数1：闹钟状态，0--未结束 1--结束 2--删除所有
     *          参数2：关机状态， 0--关机 1--保持开机
     *          参数3：是否重复， 0--不重复，1--按天重复， 2--按星期重复
     *          参数4：年
     *          参数5：月
     *          参数6：日
     *          参数7：星期
     *          参数8：时
     *          参数9：分
     */
    public static final byte CMD_READ_ALARM = 0x1E;
    /**
     * 读取舵机硬件信息：硬件未实现
     */
    public static final byte CMD_MOTOR_HARDWARE_VERSION = 0x21;
    /**
     * 开始升级：参数1-4:文件大小,单位字节.
     */
    public static final byte CMD_START_UPGRADE = 0x30;
    /**
     * 0x31:升级数据包: 参数1-2:数据包page数
     *                参数:3-66: 共64字节数据,不到64字节用00填满
     */
    public static final byte CMD_UPGRADE_PAGE = 0X31;
    /**
     * 0x32:结束升级
     */
    public static final byte CMD_END_UPGRADE = 0x32;
    /**
     *  读胸部版本: 硬件未实现
     */
    public static final byte CMD_READ_VERSION = 0x33;
    /**
     * 设置所有舵机---和0x03一样的命令
     */
    public static final byte CMD_SET_ALL_ANGLE = 0x34;
    /**
     * 打开边充边玩功能：参数1：0--关闭 1--打开 2--查询
     */
    public static final byte CMD_SET_CHARGE_PLAYING_MODE = 0x35;
    /**
     * 写eeprom：参数1：要写的数据长度
     *                     参数2-n：要写的数据，最长32个字节
     */
    public static final byte CMD_WRITE_EEPROM = 0x36;
    /**
     * 读eeprom：
     */
    public static final byte CMD_READ_EEPROM = 0x37;
    /**
     * 蹲下：参数与0x03一样
     */
    @Deprecated
    public static final byte CMD_SQUAT = (byte) 0x38;
    /**
     * 电池包信息：参数1：1--读电池包生成日期
     *                  2--读电池包序列号
     *                  3--读电池包软件banbe
     *                  4--读电池包厂商
     *                  5--显示电量
     *                  6--温度值
     *                  7--写电池包序列号
     *           参数2-9：电池包的序列号数据
     */
    public static final byte CMD_POWER_INFO = 0x39;
    /**
     * 休眠模式：参数1： 0--退出休眠模式 1--进入休眠模式
     *          参数2：0--不关闭舵机电源 1--关闭舵机电源
     */
    public static final byte CMD_SET_SAVE_POWER = (byte) 0x40;

    /**
     * 读取手部软件版本信息： 参数1：0--左手 1--右手
     *                     参数2：0--boot boot模块
     *                           1--app  app模块
     *                           2--hardware 硬件模块
     */
    @Deprecated
    public static final byte CMD_READ_HAND_VERSION = 0x41;
    /**
     * 读取胸部boot版本信息： 硬件未实现
     */
    @Deprecated
    public static final byte CMD_READ_BOOT_VERSION = 0x42;
    /**
     * 读取胸部软件版本信息： 硬件未实现
     */
    @Deprecated
    public static final byte CMD_READ_MOTOR_HARDWARE_VERSION = 0x43;
    /**
     * muted 按键 控制： 参数1：0--关 1--开
     */
    public static final byte CMD_SET_MUTE_KEY = 0x44;

    /** 电池包升级开始 **/
    public static final byte CHEST_BATTERY_UPDATE_START = (byte) 0x45;
    /** 电池包升级数据 **/
    public static final byte CHEST_BATTERY_UPDATE_PAGE = (byte) 0x46;
    /** 电池包升级结束 **/
    public static final byte CHEST_BATTERY_UPDATE_END = (byte) 0x47;
    /** 关机设置 **/
    public static final byte CHEST_SHUTDOWN_SETTING = (byte) 0x49;

    //#######################################################
    //#######################################################
    //#######################################################
    //###############  硬件主动上报命令 #######################
    //#######################################################
    //#######################################################
    //#######################################################
    //#######################################################

    /** 电量 **/
    public static final byte CHES_SEND_POWER = (byte) 0x80;
    /**有障碍物*/
    @Deprecated
    public static final byte CHES_SEND_OBSTACLE = (byte) 0x81;
    /**舵机角度 参数1： ID(1BYTE) 参数2：角度（1BYTE）*/
    @Deprecated
    public static final byte CHES_SEND_ANGLEINFO = (byte) 0x82;
    /**关机命令*/
    public static final byte CHES_SEND_SHUTDWON = (byte) 0x83;
    /** 舵机温度高,需要休息*/
    @Deprecated
    public static final byte CHES_MOTOR_TEMP_BEYOND = (byte) 0x84;
    /** 舵机升级成功命令*/
    public static final byte CHES_MOTOR_UPGRADE = (byte) 0x86;
    /** 闹钟触发 **/
    @Deprecated
    public static final byte CHES_SEND_ALARM = (byte) 0x87;
    /**手部触摸*/
    @Deprecated
    public static final byte CHEST_TOUCH_BOARD = (byte)0x89;
    /** 超声检查上报 **/
    public static final byte CHES_SONAR_DISTANCE = (byte) 0x8B;
    /** 电源插入或拔出 **/
    public static final byte CHES_DC_STATE = (byte) 0x8A;
    /** 禁止边充边玩*/
    public static final byte CHES_CHARGE_PLAY_RSP = (byte) 0x8C;
    /** 电源温度过高提示 **/
    public static final byte CHES_DC_TEMP_BEYOND = (byte) 0x8D;
    /** mute 键状态：0--松开 1--按下*/
    public static final byte CHES_MUTE_KEY_STATE = (byte)0x91;
    /**红外检查：0--检查接触， 1--检测有效*/
    public static final byte CHES_PIR_STATE = (byte)0x93;
    /**电池包升级*/
    public static final byte CHES_BATTERY_UPGRADE = (byte) 0x94;
    /**数据发送到头部（stm32->android）*/
    public static final byte CHES_SEND_TRANSFORM = (byte) 0x96;
    /**跌倒 参数1:0-前跌 1--后跌*/
    @Deprecated
    public static final byte CHES_SEND_FALLDOWN = (byte) 0x97;
}
