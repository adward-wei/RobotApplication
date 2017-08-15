package com.ubtechinc.alpha.speech;

import android.content.Context;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.ThreadPool;
import com.ubtechinc.alpha.speech.recoder.IPcmListener;
import com.ubtechinc.alpha.speech.recoder.IRecoder;
import com.ubtechinc.alpha.speech.recoder.IfytekAlsaRecoder;
import com.ubtechinc.alpha.speech.recoder.RecoderProxy;
import com.ubtechinc.alpha.speech.recognizer.IGrammarListener;
import com.ubtechinc.alpha.speech.recognizer.IRecognizerListener;
import com.ubtechinc.alpha.speech.recognizer.ISpeechRecognizer;
import com.ubtechinc.alpha.speech.recognizer.NuanceRecongnizer;
import com.ubtechinc.alpha.speech.recognizer.RecongnizerProxy;
import com.ubtechinc.alpha.speech.ttser.ITTSListener;
import com.ubtechinc.alpha.speech.ttser.ITtser;
import com.ubtechinc.alpha.speech.ttser.NuanceTtser;
import com.ubtechinc.alpha.speech.ttser.TtserProxy;
import com.ubtechinc.alpha.speech.wakeuper.IWakeuper;
import com.ubtechinc.alpha.speech.wakeuper.IWakeuperListener;
import com.ubtechinc.alpha.speech.wakeuper.NuanceWakeuper;
import com.ubtechinc.alpha.speech.wakeuper.WakeuperProxy;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/30
 * @modifier:
 * @modify_time:
 */

public class NuanceUtil implements SpeechUtil {
    private static final String TAG = "NuanceUtil";
    private Context mContext;
    private IWakeuper iWakeuper;//唤醒模块
    private IRecoder iRecoder;//录音模块
    private ITtser iTtser;//tts模块
    private ISpeechRecognizer iRecognizer;//语音模块

    private volatile boolean mWakeuperEnabled;

    private IRecognizerListener mRecognizerListener;
    private IGrammarListener mGrammarListener;
    private IPcmListener mPcmListener;
    private IWakeuperListener mWakeuperListener;
    private IPcmListener mInnerPcmListener = new IPcmListener() {
        @Override
        public void onPcmData(final byte[] data, final int length) {
            if (mWakeuperEnabled)
                iWakeuper.writeAudio(data, length);
            if (mPcmListener != null){
                ThreadPool.runOnNonUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mPcmListener.onPcmData(data, length);
                    }
                });
            }
        }
    };

    public NuanceUtil(Context context) {
        this.mContext = context;
        iRecoder = new RecoderProxy(new IfytekAlsaRecoder(16000));
        iWakeuper = new WakeuperProxy(new NuanceWakeuper(context));
        iWakeuper.init();
    }

    @Override
    public void init() {
        LogUtils.i( "初始化Nuance语音引擎");
        iTtser = new TtserProxy(new NuanceTtser(mContext));
        iRecognizer = new RecongnizerProxy(new NuanceRecongnizer(mContext));
        iWakeuper.setWakeupListener(new IWakeuperListener() {
            @Override
            public void onWakeup(String resultStr, int soundAngle) {
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
    }

    @Override
    public void release() {
        LogUtils.i(TAG, "释放Nuance语音引擎资源");
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
            iTtser.startSpeaking(text, listener);
        }
    }

    @Override
    public void stopTts() {
        if (iTtser != null) {
            iTtser.stopSpeaking();
        }
    }

    private void restartRecognize() {
        iRecognizer.stopListening();
        iRecognizer.cancel();
        iRecognizer.startListening(mRecognizerListener);
    }

    @Override
    public void startRecognize(IRecognizerListener listener){
        LogUtils.i(TAG,"启动语音引擎...");
        this.mRecognizerListener = listener;
        restartRecognize();
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
        this.mWakeuperEnabled = true;
    }

    @Override
    public void exitWakeUp() {
        LogUtils.i(TAG,"退出唤醒模式...");
        this.mWakeuperListener = null;
        this.mWakeuperEnabled = false;
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
}
