package com.ubtechinc.alpha2ctrlapp.entity.business.robot;

/**
 * @author：ubt
 * @date：2017/3/13 10:37
 * @modifier：ubt
 * @modify_date：2017/3/13 10:37
 * [A brief description]
 * version
 */

public class GetLogByMobileEntrity {

    /**
     * 机器人是否接收到了抓log的命令，true表示是
     **/

    private int minute2wait;



    public int getMinute2wait() {
        return minute2wait;
    }

    public void setMinute2wait(int minute2wait) {
        this.minute2wait = minute2wait;
    }
}
