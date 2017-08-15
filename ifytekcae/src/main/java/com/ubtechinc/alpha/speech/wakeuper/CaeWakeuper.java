package com.ubtechinc.alpha.speech.wakeuper;

import android.content.Context;

import com.iflytek.cae.CAEEngine;
import com.iflytek.cae.CAEError;
import com.iflytek.cae.CAEListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.ubtech.utilcode.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @desc : 科大讯飞cae唤醒
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public class CaeWakeuper implements IWakeuper {
    private static final String TAG = "IWakeuper";
    private static final String CN_WAKEUP_NIHAO_ALPHA = "ivw/56652373.jet";
    private final CAEEngine mCAEEngine;
    private final String mResPath;
    private static String DEFAULT_WAKEUP_WORD = CN_WAKEUP_NIHAO_ALPHA;
    private static int mEngineType = CAEEngine.SINGLE_WAKEUP;
    private static int mWakeupThresholdMic5 = 25;
    private final Context mContext;

    public CaeWakeuper(Context mContext) {
        this.mContext = mContext;
        mResPath = ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, DEFAULT_WAKEUP_WORD);
        mCAEEngine = CAEEngine.createInstance(mResPath, mEngineType);
        mCAEEngine.setShowCAELog(false);
    }

    @Override
    public void init() {
        if(mEngineType == CAEEngine.THREE_WAKEUP) {
            mCAEEngine.setCAEWParam("ivw_threshold_1".getBytes(), String.valueOf(mWakeupThresholdMic5).getBytes());
            mCAEEngine.setCAEWParam("ivw_threshold_2".getBytes(), String.valueOf(mWakeupThresholdMic5).getBytes());
            mCAEEngine.setCAEWParam("ivw_threshold_3".getBytes(), String.valueOf(mWakeupThresholdMic5).getBytes());
        }
        LogUtils.i(TAG,"mWakeupThresholdMic5=" + mWakeupThresholdMic5);
        if (null == mCAEEngine){
            LogUtils.i(TAG,"CAEEngine create null mResPath = "+mResPath);
        }
    }

    @Override
    public void writeAudio(byte[] pcmData, int dataLen) {
        mCAEEngine.writeAudio(pcmData, dataLen);
    }

    @Override
    public void setWakeupListener(final IWakeuperListener listener) {
        mCAEEngine.setCAEListener(new CAEListener() {
            @Override
            public void onWakeup(String jsonResult) {
                //main thread
                if (listener != null) {
                    JSONObject json;
                    try {
                        json = new JSONObject(jsonResult);
                        final String angle = json.getString("angle");
                        int soundAngle = Integer.parseInt(angle);
                        LogUtils.d("MicArray wakeup. result=" + soundAngle + " data " + jsonResult);
                        listener.onWakeup(jsonResult, soundAngle);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onAudio(byte[] data, int datalen, int i1, int i2) {
                //main thread
                if (null != listener){
                    listener.onAudio(data, datalen, i1, i2);
                }
            }

            @Override
            public void onError(CAEError caeError) {
                //main thread
                LogUtils.d(TAG, "cae wakeup error:"+caeError.getDescription());
                if (null != listener){
                    mCAEEngine.reset();
                    mCAEEngine.setCAEListener(this);
                    listener.onError(caeError.getErrorCode());
                }
            }
        });
    }

    @Override
    public void reset() {
        mCAEEngine.reset();
    }

    @Override
    public void destroy() {
        mCAEEngine.destroy();
    }

    @Override
    public boolean isWakeup() {
        return mCAEEngine.isWakeup();
    }
}
