package com.ubtechinc.alpha.wificonnect;

import android.app.Application;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.affairdispatch.AffairConstruct;
import com.ubtechinc.alpha.affairdispatch.affair.TtsAffair;
import com.ubtechinc.alpha.affairdispatch.constants.AffairPriority;
import com.ubtechinc.alpha.affairdispatch.constants.AffairType;
import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;
import com.ubtechinc.alpha.utils.StringUtil;
import com.ubtechinc.alpha2services.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author wzt
 * @date 2017/6/15
 * @Description 联网的几个定时器
 * @modifier
 * @modify_time
 */

public class ConnectionTimer {
    private static final String TAG = "ConnectionTimer";

    // 超时时长常量
    private static final int WIFI_TIMEOUT = 1000 * 120;
    private static final int SCANHINT_TIME_PERIOD = 20 * 1000;
    private static final int QRHINT_TIME_PERIOD = 10 * 1000;

    private static ConnectionTimer sConnectionTimer;

    private Timer mConnectionTimer;
    private Timer mHintTimer;

    private ConnectionTimer() {
    }

    public static ConnectionTimer get() {
        if (sConnectionTimer == null) {
            synchronized (ConnectionTimer.class) {
                if (sConnectionTimer == null)
                    sConnectionTimer = new ConnectionTimer();
            }
        }

        return sConnectionTimer;
    }

    // 联网超时时间，目前为2分钟
    public void startConnectionTimer() {
        if (mConnectionTimer == null) {
            mConnectionTimer = new Timer();
        }

        LogUtils.i(TAG, "TIMER START");

        mConnectionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                LogUtils.i(TAG, "Timer out IS :   " + WIFI_TIMEOUT);
                String text1 = StringUtil.getString(R.string.connection_timeout);
                if (text1 != null) {
                   playTTS(text1);
                }
                Alpha2Connection.getInstance(null).stopNetworkConnection(false);
            }
        }, WIFI_TIMEOUT);
    }

    public void stopConnectionTimer() {
        if (mConnectionTimer != null) {
            mConnectionTimer.cancel();
            mConnectionTimer.purge();
            mConnectionTimer = null;

        }
    }

    // 联网过程中每隔20秒进行TTS提示，从打开蓝牙开始计时
    public void startConnectionHintTimer() {
        if (mHintTimer == null) {
            mHintTimer = new Timer();
        }

        LogUtils.i(TAG, "startConnectionHintTimer Scan hint timer START");
        mHintTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                LogUtils.i(TAG, "request scan QR code " + SCANHINT_TIME_PERIOD);
                playTTS(StringUtil.getString(R.string.begin_connection_0));
            }
        }, SCANHINT_TIME_PERIOD, SCANHINT_TIME_PERIOD);
    }

    public void stopConnectionHintTimer() {
        if (mHintTimer != null) {
            mHintTimer.cancel();
            mHintTimer.purge();
            mHintTimer = null;
        }
    }


    private void playTTS(String text) {
        SpeechServiceProxy.getInstance().speechStartTTS(text,null);
    }
}


