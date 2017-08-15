package com.ubtechinc.nets.im.event;

/**
 * @author：tanghongyu
 * @date：6/21/2017 7:56 PM
 * @modifier：tanghongyu
 * @modify_date：6/21/2017 7:56 PM
 * [A brief description]
 * version
 */

public class IMStateChange {


    public static final int STATE_CONNECTED = 0;
    public static final int STATE_DISCONNECTED = 1;
    public static final int STATE_WIFI_NEED_AUTH = 2;
    public static final int STATE_FORCE_OFFLINE = 3;
    public static final int STATE_USER_SIG_EXPIRED = 4;

    int state;
    public IMStateChange(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
