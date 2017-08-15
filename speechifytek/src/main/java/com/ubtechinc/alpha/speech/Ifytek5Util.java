package com.ubtechinc.alpha.speech;

import android.content.Context;
import android.text.TextUtils;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.Setting;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.constant.MemoryConstant;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtech.utilcode.utils.thread.ThreadPool;
import com.ubtechinc.alpha.event.SpeechBuildEvent;
import com.ubtechinc.alpha.speech.ifytek.IfytekUtil;
import com.ubtechinc.alpha.speech.recoder.IPcmListener;
import com.ubtechinc.alpha.speech.recoder.IRecoder;
import com.ubtechinc.alpha.speech.recoder.IfytekAlsaRecoder;
import com.ubtechinc.alpha.speech.recoder.RecoderProxy;
import com.ubtechinc.alpha.speech.recognizer.IGrammarListener;
import com.ubtechinc.alpha.speech.recognizer.IRecognizerListener;
import com.ubtechinc.alpha.speech.recognizer.ISpeechRecognizer;
import com.ubtechinc.alpha.speech.recognizer.IfytekRecognizer;
import com.ubtechinc.alpha.speech.recognizer.RecongnizerProxy;
import com.ubtechinc.alpha.speech.ttser.ITTSListener;
import com.ubtechinc.alpha.speech.ttser.ITtser;
import com.ubtechinc.alpha.speech.ttser.IfytekTtser;
import com.ubtechinc.alpha.speech.ttser.TtserProxy;
import com.ubtechinc.alpha.speech.wakeuper.CaeWakeuper;
import com.ubtechinc.alpha.speech.wakeuper.IWakeuper;
import com.ubtechinc.alpha.speech.wakeuper.IWakeuperListener;
import com.ubtechinc.alpha.speech.wakeuper.WakeuperProxy;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public class Ifytek5Util implements SpeechUtil {
    private static final int ERROR_RETRY_TIMEOUT = 1 * 1000 * 60;
    private static final String TAG = "IfytekRecongnizer";
    private Context mContext;
    private IWakeuper iWakeuper;//唤醒模块
    private IRecoder iRecoder;//录音模块
    private ITtser iTtser;//tts模块
    private ISpeechRecognizer iRecognizer;//语音模块

    private AtomicBoolean mWakeuperEnabled = new AtomicBoolean(false);

    private IRecognizerListener mRecognizerListener;
    private IGrammarListener mGrammarListener;
    private IPcmListener mPcmListener;
    private IWakeuperListener mWakeuperListener;
    private IPcmListener mInnerPcmListener = new IPcmListener() {
        @Override
        public void onPcmData(final byte[] data, final int length) {
            if (mWakeuperEnabled.get())
                iWakeuper.writeAudio(data, length);
            if (mPcmListener != null){
                ThreadPool.runOnNonUIThread(new Runnable() {
                    @Override
                    public void run() {
                        //byte[] newdata = new byte[data.length];
                        //CAEJni.CAEExtract16K(data, data.length, 2, newdata ,new CAEJni.OutValues());
                        //mPcmListener.onPcmData(newdata, newdata.length);
                        mPcmListener.onPcmData(data, length);
                    }
                });
            }
        }
    };
    private IRecognizerListener mInnerRecognizerListener = new IRecognizerListener() {
        @Override
        public void onBegin() {
            if (mRecognizerListener != null){
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (mRecognizerListener != null){
                            mRecognizerListener.onBegin();
                        }
                    }
                },10);
            }
        }

        @Override
        public void onEnd() {
            if (mRecognizerListener != null){
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (mRecognizerListener != null){
                            mRecognizerListener.onEnd();
                        }
                    }
                },10);
            }
        }

        @Override
        public void onResult(final String result, final boolean b) {
            if (mRecognizerListener != null){
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (mRecognizerListener != null){
                            mRecognizerListener.onResult(result, b);
                        }
                    }
                },10);
            }
        }

        @Override
        public void onError(final int errCode) {
            if (mRecognizerListener != null) {
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (mRecognizerListener != null) {
                            mRecognizerListener.onError(errCode);
                        }
                    }
                }, 10);
            }
            if (!timeout){
                restartRecognize(false);
            }
        }
    };

    public Ifytek5Util(Context context) {
        this.mContext = context;
        IfytekUtil.createUtility(mContext);
        //设置讯飞日志过滤
        Setting.setLogLevel(Setting.LOG_LEVEL.normal);
        iRecoder = new RecoderProxy(new IfytekAlsaRecoder());
        iWakeuper = new WakeuperProxy(new CaeWakeuper(context));
        iWakeuper.init();
    }

    @Override
    public void init() {
        LogUtils.i(TAG,"初始化讯飞语音引擎");
        iTtser = new TtserProxy(new IfytekTtser(mContext));
        iRecognizer = new RecongnizerProxy(new IfytekRecognizer(mContext));
        iWakeuper.setWakeupListener(new IWakeuperListener() {
            @Override
            public void onWakeup(String resultStr, int soundAngle) {
                if (iRecognizer.isListening()){
                    iRecognizer.cancel();
                }
                if (mWakeuperListener != null){
                    mWakeuperListener.onWakeup(resultStr, soundAngle);
                }
            }

            @Override
            public void onError(int errCode) {
                iRecoder.stopRecording();
                iRecoder.startRecording();
            }

            @Override
            public void onAudio(byte[] data, int dataLen, int param1, int param2) {
                if (iRecognizer != null && iRecognizer.isListening()){
                    iRecognizer.writeAudio(data,0, dataLen);
                }
            }
        });
        iRecoder.setPcmListener(mInnerPcmListener);
        iRecoder.startRecording();//默认开启录音
        NotificationCenter.defaultCenter().subscriber(SpeechBuildEvent.class, mSubscriber);
    }

    @Override
    public void release() {
        LogUtils.i(TAG, "释放讯飞语音引擎资源");
        if (iRecoder != null) {
            iRecoder.destroy();
            iRecoder = null;
        }
        if (iWakeuper != null){
            iWakeuper.destroy();
        }
        if (iTtser != null) {
            iTtser.destroy();
            iTtser = null;
        }
        if (iRecognizer != null) {
            iRecognizer.cancel();
            iRecognizer.destroy();
            iRecognizer = null;
        }
        NotificationCenter.defaultCenter().unsubscribe(SpeechBuildEvent.class, mSubscriber);
    }

    @Override
    public void buildGrammar(String grammarStr, IGrammarListener listener) {
        this.mGrammarListener = listener;
        iRecognizer.buildGrammar(grammarStr,mGrammarListener);
    }

    @Override
    public void initParam() {
        if (iTtser != null) {
            iTtser.init(); //初始化默认发音人
        }
        if (iRecognizer != null){
            iRecognizer.init();
        }
    }

    @Override
    public void startTts(String text, ITTSListener listener) {
        if (iTtser != null) {
            iTtser.startSpeaking(text,listener);
        }
    }

    @Override
    public void stopTts() {
        if (iTtser != null) {
            iTtser.stopSpeaking();
        }
    }

    private void restartRecognize(boolean cancelTask) {
        if (iRecognizer != null) {
            iRecognizer.cancel();
            iRecognizer.stopListening();
            if (cancelTask) {
                timer.cancel();
                timer = new Timer("Grammar_TimerThread");
                timeout = false;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        timeout = true;
                        iRecognizer.stopListening();
                    }
                }, ERROR_RETRY_TIMEOUT);
            }
            iRecognizer.startListening(mInnerRecognizerListener);
        }
    }

    @Override
    public void startRecognize(IRecognizerListener listener) {
        LogUtils.i(TAG,"启动语音引擎...");
        this.mRecognizerListener = listener;
        restartRecognize(true);
    }

    @Override
    public void stopRecognize() {
        LogUtils.i(TAG,"关闭语音引擎...");
        iRecognizer.cancel();
        iRecognizer.stopListening();
    }

    @Override
    public void enterWakeUp(IWakeuperListener listener) {
        LogUtils.i(TAG,"进入唤醒模式...");
        this.mWakeuperListener = listener;
        if (!iRecoder.isRecording()){
            iRecoder.startRecording();
        }
        this.mWakeuperEnabled.set(true);
    }

    @Override
    public void exitWakeUp() {
        LogUtils.i(TAG,"退出唤醒模式...");
        this.mWakeuperListener = null;
        this.mWakeuperEnabled.set(false);
    }

    @Override
    public void enterRecoding(IPcmListener listener) {
        LogUtils.i(TAG,"进入录音模式...");
        this.mPcmListener = listener;
        if (!iRecoder.isRecording()){
            iRecoder.startRecording();
        }
    }

    @Override
    public void exitRecoding() {
        LogUtils.i(TAG,"退出录音模式...");
        this.mPcmListener = null;
        if (iRecoder.isRecording()){
            iRecoder.stopRecording();
        }
    }

    @Override
    public void setVoiceName(String voiceName) {
        if (iTtser != null) iTtser.setTtsVoicer(voiceName);
    }

    @Override
    public String getCurVoiceName() {
        return iTtser ==null? "": iTtser.getTtsVoicer();
    }

    @Override
    public void setTtsSpeed(String speed) {
        if (iTtser != null)
            iTtser.setTtsSpeed(speed);
    }

    @Override
    public String getTtsSpeed() {
        return iTtser == null? "": iTtser.getTtsSpeed();
    }

    @Override
    public void setTtsVolume(String volume) {
        if (iTtser != null)
            iTtser.setTtsVolume(volume);
    }

    @Override
    public String getTtsVolume() {
        return iTtser == null? "": iTtser.getTtsVolume();
    }

    private Subscriber<SpeechBuildEvent> mSubscriber = new Subscriber<SpeechBuildEvent>() {

        @Override
        public void onEvent(SpeechBuildEvent event) {
            String content = event.content;
            if (TextUtils.isEmpty(content)) {
                return;
            }
            LogUtils.i(TAG,"本地语法重新构建 内容:" + content);
            //构建离线语法
            int ret = iRecognizer.buildGrammar(content, mGrammarListener);
            if (ret != ErrorCode.SUCCESS) {
                LogUtils.i(TAG,"本地语法构建失败,错误码：" + ret);
            } else {
                LogUtils.i(TAG,"本地语法构建成功" + ret);
            }
        }
    };
    private Timer timer = new Timer("Grammar_TimerThread");
    private volatile boolean timeout = false;

}
