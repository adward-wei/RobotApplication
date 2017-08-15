package com.ubtechinc.alpha.affairdispatch;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;
import com.ubtechinc.alpha2services.BuildConfig;

/**
 * Created by bob.xu on 2017/7/6.
 */

public class TtsAffairManager extends BaseAffairManager{

    private static TtsAffairManager sAffairManager;
    public static final String TAG = "TtsAffairManager";

    private TtsAffairManager() {
        super();
    }

    public static TtsAffairManager getInstance() {
        if(sAffairManager == null) {
            synchronized (TtsAffairManager.class) {
                if(sAffairManager == null) {
                    sAffairManager = new TtsAffairManager();
                }
            }
        }
        return sAffairManager;
    }

    @Override
    public synchronized void processAffair(BaseAffair event) {
        super.processAffair(event);
    }

    @Override
    public void onAffairTimeOut(AbstractAffair event) {
        LogUtils.d(TAG,"onAffairTimeOut---affair :"+event);
        if (mWorkingAffair == event) {
            //把错误数据存储到文件
            LogUtils.writeLog2File("ttsAffairError",getWorkingAffairDesc());
            //恢复，保证后面的tts可正常播报
            mWorkingAffair = null;
            if(BuildConfig.DEBUG) {
                SpeechServiceProxy.getInstance().speechStartTTS("tts播报事务处理模块发生错误，请及时联系开发同学", null);
            }
            processNext();
        }
    }
}
