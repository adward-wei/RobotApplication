package com.ubtechinc.alpha.affairdispatch;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.speech.SpeechServiceProxy;
import com.ubtechinc.alpha2services.BuildConfig;

/**
 * Created by bob.xu on 2017/7/6.
 */

public class ActionAffairManager extends BaseAffairManager {
    private static ActionAffairManager sAffairManager;
    public static final String TAG = "ActionAffairManager";

    public static ActionAffairManager getInstance() {
        if(sAffairManager == null) {
            synchronized (ActionAffairManager.class) {
                if(sAffairManager == null) {
                    sAffairManager = new ActionAffairManager();
                }
            }
        }
        return sAffairManager;
    }

    @Override
    public void onAffairTimeOut(AbstractAffair event) {
        LogUtils.d(TAG,"onAffairTimeOut---affair :"+event);
        if (mWorkingAffair == event) {
            //把错误数据存储到文件
            LogUtils.writeLog2File("actionAffairError",getWorkingAffairDesc());
            //恢复，保证后面的动作可以使用
            mWorkingAffair = null;
            if(BuildConfig.DEBUG) {
                SpeechServiceProxy.getInstance().speechStartTTS("动作事务处理模块发生错误，请及时联系开发同学", null);
            }
            processNext();
        }
    }

}
