package com.ubtechinc.alpha2ctrlapp.events;

/**
 * @author：tanghongyu
 * @date：6/24/2017 2:07 PM
 * @modifier：tanghongyu
 * @modify_date：6/24/2017 2:07 PM
 * [A brief description]
 * version
 */

public class RobotControlEvent {

    private int action;
    public static final int CONNECT_SUCCESS = 1;
    public static final int CONNECT_FAIL = 2;
    public static final int DISCONNECT_SUCCESS = 3;
    public static final int DISCONNECT_FAIL = 4;

    public RobotControlEvent(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
