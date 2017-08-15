package com.ubtechinc.alpha2ctrlapp.events;

import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotApp;

/**
 * @author：tanghongyu
 * @date：6/15/2017 2:43 PM
 * @modifier：tanghongyu
 * @modify_date：6/15/2017 2:43 PM
 * [A brief description]
 * version
 */

public class AppDownloadStatusChangeEvent {
    private RobotApp appEntrityInfo;

    public RobotApp getAppEntrityInfo() {
        return appEntrityInfo;
    }

    public void setAppEntrityInfo(RobotApp appEntrityInfo) {
        this.appEntrityInfo = appEntrityInfo;
    }
}
