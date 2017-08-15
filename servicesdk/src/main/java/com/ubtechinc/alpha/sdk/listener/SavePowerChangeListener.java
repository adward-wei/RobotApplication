package com.ubtechinc.alpha.sdk.listener;

/**
 * @desc : 监听机器人进入省电模式
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/6/6
 * @modifier:
 * @modify_time:
 */

public interface SavePowerChangeListener {
    void savePower(boolean enable);
}
