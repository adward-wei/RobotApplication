package com.ubtechinc.alpha.task;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.ubtech.utilcode.utils.NetworkUtils;
import com.ubtech.utilcode.utils.RandomUtil;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.WifiControl;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtech.utilcode.utils.thread.ThreadPool;
import com.ubtechinc.alpha.behavior.RobotTakeARest;
import com.ubtechinc.alpha.event.OfflineSlotEvent;
import com.ubtechinc.alpha.event.PowerOffEvent;
import com.ubtechinc.alpha.model.speech.SlotValue;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.alpha.serverlibutil.aidl.ITtsCallBackListener;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;
import com.ubtechinc.alpha.utils.Constants;
import com.ubtechinc.alpha.utils.SoundVolumesUtils;
import com.ubtechinc.alpha.utils.StorageUtil;
import com.ubtechinc.alpha.utils.StringUtil;
import com.ubtechinc.alpha.wificonnect.Alpha2Connection;
import com.ubtechinc.alpha2services.R;
import com.ubtechinc.nets.im.business.LoginBussiness;

import java.text.DecimalFormat;

import static com.ubtechinc.alpha.model.speech.SlotValue.ACTINGCUTE;
import static com.ubtechinc.alpha.model.speech.SlotValue.AGE;
import static com.ubtechinc.alpha.model.speech.SlotValue.AGREE;
import static com.ubtechinc.alpha.model.speech.SlotValue.APP;
import static com.ubtechinc.alpha.model.speech.SlotValue.APPLAUD;
import static com.ubtechinc.alpha.model.speech.SlotValue.BACKLOOK;
import static com.ubtechinc.alpha.model.speech.SlotValue.BACKWALK;
import static com.ubtechinc.alpha.model.speech.SlotValue.BEGINCONNECT;
import static com.ubtechinc.alpha.model.speech.SlotValue.BORING;
import static com.ubtechinc.alpha.model.speech.SlotValue.CAPACITY;
import static com.ubtechinc.alpha.model.speech.SlotValue.CONNECT;
import static com.ubtechinc.alpha.model.speech.SlotValue.CROUCH;
import static com.ubtechinc.alpha.model.speech.SlotValue.DANCE;
import static com.ubtechinc.alpha.model.speech.SlotValue.DENY;
import static com.ubtechinc.alpha.model.speech.SlotValue.FACE;
import static com.ubtechinc.alpha.model.speech.SlotValue.FRONTLOOK;
import static com.ubtechinc.alpha.model.speech.SlotValue.FRONTWALK;
import static com.ubtechinc.alpha.model.speech.SlotValue.GREET;
import static com.ubtechinc.alpha.model.speech.SlotValue.GRIEVANCE;
import static com.ubtechinc.alpha.model.speech.SlotValue.HANDSRAISE;
import static com.ubtechinc.alpha.model.speech.SlotValue.HANDSUP;
import static com.ubtechinc.alpha.model.speech.SlotValue.HAPPY;
import static com.ubtechinc.alpha.model.speech.SlotValue.HAPPYNEWYEAR;
import static com.ubtechinc.alpha.model.speech.SlotValue.HEADDROP;
import static com.ubtechinc.alpha.model.speech.SlotValue.HEARTTIRED;
import static com.ubtechinc.alpha.model.speech.SlotValue.HUM;
import static com.ubtechinc.alpha.model.speech.SlotValue.KONGFU;
import static com.ubtechinc.alpha.model.speech.SlotValue.LAUGH;
import static com.ubtechinc.alpha.model.speech.SlotValue.LEFTBECK;
import static com.ubtechinc.alpha.model.speech.SlotValue.LEFTHANDSUP;
import static com.ubtechinc.alpha.model.speech.SlotValue.LEFTHEADUP;
import static com.ubtechinc.alpha.model.speech.SlotValue.LEFTKICK;
import static com.ubtechinc.alpha.model.speech.SlotValue.LEFTLEGLIFTS;
import static com.ubtechinc.alpha.model.speech.SlotValue.LEFTLOOK;
import static com.ubtechinc.alpha.model.speech.SlotValue.LEFTPUNCH;
import static com.ubtechinc.alpha.model.speech.SlotValue.LEFTTURN;
import static com.ubtechinc.alpha.model.speech.SlotValue.LEFTWALK;
import static com.ubtechinc.alpha.model.speech.SlotValue.LOOKUP;
import static com.ubtechinc.alpha.model.speech.SlotValue.MASTER;
import static com.ubtechinc.alpha.model.speech.SlotValue.NAME;
import static com.ubtechinc.alpha.model.speech.SlotValue.NOD;
import static com.ubtechinc.alpha.model.speech.SlotValue.NULL;
import static com.ubtechinc.alpha.model.speech.SlotValue.PERFECT;
import static com.ubtechinc.alpha.model.speech.SlotValue.PICTURE;
import static com.ubtechinc.alpha.model.speech.SlotValue.POWER;
import static com.ubtechinc.alpha.model.speech.SlotValue.POWEROFF;
import static com.ubtechinc.alpha.model.speech.SlotValue.RIGHTBECK;
import static com.ubtechinc.alpha.model.speech.SlotValue.RIGHTHANDSUP;
import static com.ubtechinc.alpha.model.speech.SlotValue.RIGHTHEADUP;
import static com.ubtechinc.alpha.model.speech.SlotValue.RIGHTKICK;
import static com.ubtechinc.alpha.model.speech.SlotValue.RIGHTLEGLIFTS;
import static com.ubtechinc.alpha.model.speech.SlotValue.RIGHTLOOK;
import static com.ubtechinc.alpha.model.speech.SlotValue.RIGHTPUNCH;
import static com.ubtechinc.alpha.model.speech.SlotValue.RIGHTTURN;
import static com.ubtechinc.alpha.model.speech.SlotValue.RIGHTWALK;
import static com.ubtechinc.alpha.model.speech.SlotValue.ROCK;
import static com.ubtechinc.alpha.model.speech.SlotValue.SAD;
import static com.ubtechinc.alpha.model.speech.SlotValue.SERVER;
import static com.ubtechinc.alpha.model.speech.SlotValue.SHAKE;
import static com.ubtechinc.alpha.model.speech.SlotValue.SHAKEHANDS;
import static com.ubtechinc.alpha.model.speech.SlotValue.SHARP;
import static com.ubtechinc.alpha.model.speech.SlotValue.SLEEP;
import static com.ubtechinc.alpha.model.speech.SlotValue.SMILE;
import static com.ubtechinc.alpha.model.speech.SlotValue.STANDUP;
import static com.ubtechinc.alpha.model.speech.SlotValue.STOOP;
import static com.ubtechinc.alpha.model.speech.SlotValue.STORY;
import static com.ubtechinc.alpha.model.speech.SlotValue.TFBOY;
import static com.ubtechinc.alpha.model.speech.SlotValue.THINKING;
import static com.ubtechinc.alpha.model.speech.SlotValue.TIME;
import static com.ubtechinc.alpha.model.speech.SlotValue.VOLUMEDOWN;
import static com.ubtechinc.alpha.model.speech.SlotValue.VOLUMEUP;
import static com.ubtechinc.alpha.model.speech.SlotValue.WELCOME;
import static com.ubtechinc.alpha.model.speech.SlotValue.WHERE;
import static com.ubtechinc.alpha.model.speech.SlotValue.WIFI;
import static com.ubtechinc.alpha.model.speech.SlotValue.WINK;

/**
 * @desc : 处理讯飞离线命令词
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/8/8
 * @modifier:
 * @modify_time:
 */

public final class ProxyIfytekOfflineSlotProcessImpl extends AbstractProxyService {
    private final Context cxt;

    public ProxyIfytekOfflineSlotProcessImpl(Context cxt) {
        this.cxt = cxt;
    }

    @Override
    public void registerEvent() {
        NotificationCenter.defaultCenter().subscriber(OfflineSlotEvent.class, mKeyPressSubscriber);
    }

    @Override
    public void unregisterEvent() {
        NotificationCenter.defaultCenter().unsubscribe(OfflineSlotEvent.class, mKeyPressSubscriber);
    }

    private Subscriber<OfflineSlotEvent> mKeyPressSubscriber = new Subscriber<OfflineSlotEvent>() {
        @Override
        public void onEvent(OfflineSlotEvent event) {
            processOfflineSlot(event.slot);
        }
    };

    private void processOfflineSlot(@SlotValue.Type int type) {
        String ttsText;
        switch (type){
            case NULL:
            case NAME:
            case AGE:
            case WHERE:
                break;
            case WIFI:
                WifiControl wifiControl = WifiControl.get(cxt);
                if (wifiControl.isWifiConnect()){
                    ttsText = String.format(getRandomString(R.array.offline_wifi), addBlank(wifiControl.getSSID()));
                    SpeechServiceProxy.getInstance().speechStartTTS(ttsText,null);
                }else {
                    startWifiPing();
                }
                break;
            case SERVER:
                if (LoginBussiness.getInstance(cxt).isLoginIM()){
                    SpeechServiceProxy.getInstance().speechStartTTS(getRandomString(R.array.offline_have_loginned), null);
                }else {
                    SpeechServiceProxy.getInstance().speechStartTTS(getRandomString(R.array.offline_have_notlogin), null);
                }
                break;
            case MASTER:
                String master = SPUtils.get().getString(Constants.MASTER_NAME);
                if (TextUtils.isEmpty(master)) {
                    SpeechServiceProxy.getInstance().speechStartTTS(getRandomString(R.array.offline_unbounded), null);
                }else {
                    SpeechServiceProxy.getInstance().speechStartTTS(String.format(getRandomString(R.array.offline_bound_to),
                            master), null);
                }
                break;
            case POWER:
                String format;
                if (RobotState.get().getPowerValue() <= 40){
                    format = getRandomString(R.array.offline_power_5_40);
                }else if (RobotState.get().getPowerValue() <= 75){
                    format = getRandomString(R.array.offline_power_40_75);
                }else {
                    format = StringUtil.getString(R.string.offline_power_normal);
                }
                SpeechServiceProxy.getInstance().speechStartTTS(String.format(format, RobotState.get().getPowerValue()),null);
                break;
            case APP:
                SpeechServiceProxy.getInstance().speechStartTTS(String.format(getRandomString(R.array.offline_app),
                        StringUtil.getString(R.string.app_name)),null);
                break;
            case CAPACITY:
                long avail = StorageUtil.getAvailableExternalMemorySize()/(1024*1024);
                SpeechServiceProxy.getInstance().speechStartTTS(String.format(getRandomString(avail >= 1024?R.array.offline_capacity_higher:
                                R.array.offline_capacity_lower),
                        new DecimalFormat("#.##").format( avail>-1024 ? avail/1024.0: avail)),null);

                break;
            case SLEEP:
                SpeechServiceProxy.getInstance().speechStartTTS(getRandomString(R.array.sleep), new ITtsCallBackListener() {
                    @Override
                    public void onBegin() throws RemoteException {

                    }

                    @Override
                    public void onEnd() throws RemoteException {
                        RobotTakeARest.instance().start(true);
                    }

                    @Override
                    public IBinder asBinder() {
                        return null;
                    }
                });
                break;
            case PICTURE:
                SpeechServiceProxy.getInstance().speechStartTTS(getRandomString(R.array.offline_HasPicture),null);
                break;
            case CONNECT:
                startWifiPing();
                break;
            case VOLUMEUP:
                SoundVolumesUtils.get(cxt).addVolume(2);
                break;
            case VOLUMEDOWN:
                SoundVolumesUtils.get(cxt).mulVolume(2);
                break;
            case POWEROFF:
                SpeechServiceProxy.getInstance().speechStartTTS(getRandomString(R.array.robotShutDown), new ITtsCallBackListener() {
                    @Override
                    public void onBegin() throws RemoteException {

                    }

                    @Override
                    public void onEnd() throws RemoteException {
                        NotificationCenter.defaultCenter().publish(new PowerOffEvent());
                    }

                    @Override
                    public IBinder asBinder() {
                        return null;
                    }
                });
                break;
            case FACE:
                break;
            case BEGINCONNECT:
                if (!Alpha2Connection.getInstance(cxt).isConnectingNow() && !NetworkUtils.isWifiConnected())
                    Alpha2Connection.getInstance(cxt).beginNetworkConnection();
                break;
            case TIME:
                break;
            case DANCE:
            case STORY:
            case NOD:
            case SHAKE:
            case WELCOME:
            case RIGHTBECK:
            case LEFTBECK:
            case STOOP:
            case LOOKUP:
            case LEFTLEGLIFTS:
            case RIGHTLEGLIFTS:
            case KONGFU:
            case RIGHTKICK:
            case LEFTKICK:
            case HEADDROP:
            case RIGHTHANDSUP:
            case LEFTHANDSUP:
            case HANDSUP:
            case HANDSRAISE:
            case ACTINGCUTE:
            case WINK:
            case SMILE:
            case CROUCH:
            case STANDUP:
            case DENY:
            case APPLAUD:
            case BACKWALK:
            case FRONTWALK:
            case FRONTLOOK:
            case BACKLOOK:
            case RIGHTLOOK:
            case LEFTLOOK:
            case RIGHTWALK:
            case LEFTWALK:
            case RIGHTPUNCH:
            case LEFTPUNCH:
            case RIGHTHEADUP:
            case LEFTHEADUP:
            case RIGHTTURN:
            case LEFTTURN:
            case SHAKEHANDS:
            case BORING:
            case AGREE:
            case SAD:
            case THINKING:
            case HAPPY:
            case GREET:
            case LAUGH:
            case HEARTTIRED:
            case HAPPYNEWYEAR:
            case HUM:
            case GRIEVANCE:
            case PERFECT:
            case SHARP:
            case ROCK:
            case TFBOY:
                break;
        }
    }

    private void startWifiPing() {
        ThreadPool.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                final boolean iswifiok = WifiControl.ping();
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        SpeechServiceProxy.getInstance().speechStartTTS(iswifiok?
                                getRandomString(R.array.offline_net_connect_success):
                                getRandomString(R.array.offline_net_connect_fail),null);

                    }
                });
            }
        });
    }

    private static String addBlank(String orign){
        String result = null;
        for(int i = 0; i < orign.length(); i++){
            result += orign.charAt(i) + " ";
        }
        return result;
    }

    private static String getRandomString(int strResIds){
        String[] strs = StringUtil.getStringArray(strResIds);
        return strs[RandomUtil.randInt(strs.length)];
    }
}
