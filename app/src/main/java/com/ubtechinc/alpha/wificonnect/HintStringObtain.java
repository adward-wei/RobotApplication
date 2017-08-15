package com.ubtechinc.alpha.wificonnect;

import com.ubtechinc.alpha.robotinfo.RobotConfiguration;
import com.ubtechinc.alpha.utils.StringUtil;
import com.ubtechinc.alpha2services.R;
import java.util.ArrayList;
import java.util.Random;

/**
 * @date 2017/6/13
 * @author wzt
 * @Description 获取用于TTS播报的字符串
 * @modifier
 * @modify_time
 */

public class HintStringObtain {
    private static final String TAG = "HintStringObtain";

    public static ArrayList<String> getRandomHint(String hintEvent) {
        Random random = new Random();
        int select = random.nextInt(3);
        ArrayList<String> infos = new ArrayList<String>();
        String hintString = "";
        String actionString = "";
        if (RobotConfiguration.get().isBusiness) {//商演版本不需要网络播报
            hintEvent = "";
        } else if ("wakeup".equals(hintEvent)) {

            switch (select) {
                case 0:
                    hintString = StringUtil.getString(R.string.hint_wakeup_0);
                    actionString = StringUtil.getString(R.string.action_wakeup_0);
                    break;
                case 1:
                    hintString = StringUtil.getString(R.string.hint_wakeup_1);
                    actionString = StringUtil.getString(R.string.action_wakeup_1);
                    break;
                case 2:
                    hintString = StringUtil.getString(R.string.hint_wakeup_2);
                    actionString = StringUtil.getString(R.string.action_wakeup_1);
                    break;
            }
        } else if ("not_register".equals(hintEvent)) {
            switch (select) {
                case 0:
                    hintString = StringUtil.getString(R.string.hint_not_register);
                    break;
                case 1:
                    hintString = StringUtil.getString(R.string.hint_not_register);
                    break;
                case 2:
                    hintString = StringUtil.getString(R.string.hint_not_register);
                    break;
            }
        } else if ("have_register".equals(hintEvent)) {
            switch (select) {
                case 0:
                    hintString = StringUtil.getString(R.string.hint_have_register);
                    break;
                case 1:
                    hintString = StringUtil.getString(R.string.hint_have_register);
                    break;
                case 2:
                    hintString = StringUtil.getString(R.string.hint_have_register);
                    break;
            }
        } else if ("connect_wifi_succeed".equals(hintEvent)) {
            switch (select) {
                case 0:
                    hintString = StringUtil
                            .getString(R.string.hint_connect_wifi_suceed_0);
                    actionString = StringUtil
                            .getString(R.string.action_connect_wifi_suceed_0);
                    break;
                case 1:
                    hintString = StringUtil
                            .getString(R.string.hint_connect_wifi_suceed_1);
                    actionString = StringUtil
                            .getString(R.string.action_connect_wifi_suceed_1);
                    break;
                case 2:
                    hintString = StringUtil
                            .getString(R.string.hint_connect_wifi_suceed_2);
                    actionString = StringUtil
                            .getString(R.string.action_connect_wifi_suceed_2);
                    break;
            }
        } else if ("no_network".equals(hintEvent)) {
            switch (select) {
                case 0:
                    hintString = StringUtil.getString(R.string.hint_no_network_0);
                    actionString = StringUtil.getString(R.string.action_no_network_0);
                    break;
                case 1:
                    hintString = StringUtil.getString(R.string.hint_no_network_1);
                    actionString = StringUtil.getString(R.string.action_no_network_1);
                    break;
                case 2:
                    hintString = StringUtil.getString(R.string.hint_no_network_2);
                    actionString = StringUtil.getString(R.string.action_no_network_2);
                    break;
            }
        } else if ("serviceupdate".equals(hintEvent)) {
            hintString = StringUtil.getString(R.string.mainservice_update);
        }
        if ("".equals(hintString)) {
            infos.add(hintEvent);
        } else {
            infos.add(hintString);
        }
        infos.add(actionString);
        return infos;
    }
}
