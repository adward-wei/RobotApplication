package com.ubtechinc.alpha.speech.recognizer;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.ResourceUtil;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.Future;
import com.ubtech.utilcode.utils.thread.ThreadPool;

/**
 * @desc : 科大讯飞语言模块包装类
 *          需要使用离线命令词时，需先构建离线命令次，再设置grammar参数
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/30
 * @modifier:
 * @modify_time:
 */

public final class IfytekRecognizer implements ISpeechRecognizer {
    private static final String TAG = "IfytekRecongnizer";
    private static final String GRAMMAR_TYPE_BNF = "bnf";
    private static final String AGRAMMAR_TYPE_BNF = "abnf";
    public static final String DEFAULT_SPEECH_TIMEOUT = "45000";
    public static final String DEFAULT_VAD_BOS = "3000";
    public static final String DEFAULT_VAD_EOS = "1000";
    public static final String DEFAULT_SAMPLE_RATE = "16000";
    public static final String DEFAULT_AUDIO_FORMAT = "pcm";

    private final Context mContext;
    private final SpeechRecognizer proxy;

    private String mGrammarStr; //bnf or abnf语法内容
    private String mGrammarEngineType = SpeechConstant.TYPE_MIX; //语法类型
    private String mGrammarResultType = "json";// 返回结果格式，支持：xml,json
    private String grammarBuildPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/test5mic";

    private IGrammarListener grammarInitListener;
    private IRecognizerListener mRecognizerListener;
    private volatile boolean recording;
    private volatile boolean isEngineInited = false;
    private byte[] mLock = new byte[0];

    private volatile EngineType engineType = EngineType.CLOUD;

    private GrammarListener mGrammarBnfBuildListener = new GrammarListener() {
        @Override
        public void onBuildFinish(String grammarId, SpeechError speechError) {
            LogUtils.i(TAG,"科大讯飞 grammar build finish...");
            if (speechError == null) {
                LogUtils.e(TAG, "科大讯飞本地语法构建成功 " + "grammarId:" + grammarId);
                if (grammarInitListener != null) {
                    grammarInitListener.onBuildFinish(grammarId, 0);
                }
            } else {
                LogUtils.e(TAG,"科大讯飞本地语法构建失败:" + speechError.getErrorDescription());
                if (grammarInitListener != null) {
                    if (grammarInitListener != null) {
                        grammarInitListener.onBuildFinish(grammarId, speechError.getErrorCode());
                    }
                }
            }
        }
    };

    private RecognizerListener mInnerRecognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            recording = true;
            LogUtils.v(TAG,"科大讯飞..当前音量为：" + i + ",in onVolumeChanged mInnerRecognizerListener.");
        }

        @Override
        public void onBeginOfSpeech() {
            LogUtils.i(TAG, "科大讯飞语音识别录音开始...");
            recording = true;
            if (mRecognizerListener != null){
                mRecognizerListener.onBegin();
            }
        }

        @Override
        public void onEndOfSpeech() {
            recording = false;
            LogUtils.i(TAG,"科大讯飞语音识别录音结束...");
            if (mRecognizerListener != null){
                mRecognizerListener.onEnd();
            }
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            recording = false;//这里语音引擎不在接收数据
            LogUtils.d(TAG, "科大讯飞 识别结果:" + recognizerResult.getResultString() +
                       " onSerialCommandResult in mInnerRecognizerListener.");
            if (mRecognizerListener != null){
                mRecognizerListener.onResult(recognizerResult.getResultString(), b);
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            recording = false;
            LogUtils.w(TAG,"科大讯飞识别错误:" + speechError.getErrorDescription() +
                    ",in  mInnerRecognizerListener." + ", errcode:"+speechError.getErrorCode());
            if (mRecognizerListener != null){
                mRecognizerListener.onError(speechError.getErrorCode());
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
            LogUtils.v(TAG,"科大讯飞 ..onEvent in  mInnerRecognizerListener.");
        }
    };

    public IfytekRecognizer(Context context) {
        this.mContext = context;
        proxy = SpeechRecognizer.createRecognizer(mContext, new InitListener() {
            @Override
            public void onInit(int errcode) {
                if (errcode != ErrorCode.SUCCESS) {
                    LogUtils.i(TAG,"init ifytek recognizer fail:" + errcode);
                    return;
                }
                LogUtils.i(TAG, "科大讯飞语音引擎初始化Success...");
                //必须是收到初始化成功的回调后，再设置参数
                setGrammarParam();
                synchronized (mLock){
                    isEngineInited = true;
                    mLock.notify();
                }
            }
        });
    }

    @Override
    public void init() {
        if (isEngineInited) setGrammarParam();;
    }

    @Override
    public int buildGrammar(final String grammarStr, final IGrammarListener listener) {
        this.grammarInitListener = listener;
        if (TextUtils.isEmpty(grammarStr)){
            listener.onBuildFinish("", -1);
            return -1;
        }
        if (isEngineInited) {
            return buildGrammarInner(grammarStr, listener);
        }else {
            Future<Integer> f = ThreadPool.getInstance().submit(new ThreadPool.Job<Integer>() {
                @Override
                public Integer run(ThreadPool.JobContext jc) {
                    synchronized (mLock) {
                        if (!isEngineInited) {
                            try {
                                mLock.wait(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return buildGrammarInner(grammarStr, listener);
                }
            });
            return f.get();
        }
    }

    private int buildGrammarInner(String grammarStr, IGrammarListener listener) {
        this.mGrammarStr = grammarStr;
        proxy.setParameter(SpeechConstant.PARAMS, null);
        proxy.setParameter(SpeechConstant.TEXT_ENCODING,"utf-8");
        //构建命令词时这里必须设置为离线引擎
        proxy.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        // 设置语法构建路径
        proxy.setParameter(ResourceUtil.GRM_BUILD_PATH, grammarBuildPath);
        //指定更新词典时候更新哪个语法
        //proxy.setParameter(SpeechConstant.GRAMMAR_LIST, "call");
        // 设置终端级在线命令词识别
        //if(SpeechConstant.TYPE_CLOUD.equals(mGrammarEngineType)){
        //  proxy.setParameter(SpeechConstant.CLOUD_GRAMMAR, grammarId);
        //}
        //设置本地识别资源
        proxy.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
        int ret = proxy.buildGrammar(GRAMMAR_TYPE_BNF, mGrammarStr, mGrammarBnfBuildListener);
        LogUtils.i(TAG, ret == ErrorCode.SUCCESS ? "科大讯飞本地语法构建成功..."
                : "科大讯飞本地语法构建失败,错误码：" + ret);

        setGrammarParam();
        if (ret != 0) listener.onBuildFinish("", ret);
        return ret;
    }

    public synchronized void setEngineType(EngineType mode){
        setGrammarParam();
    }

    @Override
    public EngineType getCurSpeechMode() {
        return engineType;
    }

    @Override
    public int startListening(IRecognizerListener listener) {
        this.mRecognizerListener = listener;
        recording = true;
        return proxy.startListening(mInnerRecognizerListener);
    }

    @Override
    public int writeAudio(byte[] data, int start, int end) {
        return proxy.writeAudio(data, start, end);
    }

    @Override
    public void stopListening() {
        recording = false;
        proxy.stopListening();
    }

    @Override
    public boolean isListening() {
        return recording;
    }

    @Override
    public void cancel() {
        recording = false;
        proxy.cancel();
    }

    @Override
    public boolean destroy() {
        recording = false;
        proxy.cancel();
        return proxy.destroy();
    }

//    private void setIatParam() {//听写参数
//        LogUtils.i(TAG, "set iat param...");
//        // 清空参数
//        proxy.setParameter(SpeechConstant.PARAMS, null);
//        // 设置引擎，听写只支持云端
//        proxy.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
//        //短信和日常用语：iat (默认) 视频：video  地图：poi  音乐：music
//        proxy.setParameter(SpeechConstant.DOMAIN, "iat");
//        //设置语言
//        proxy.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//        //设置方言->>普通话：mandarin(默认);粤 语:cantonese;四川话:lmz;河南话:henanese
//        proxy.setParameter(SpeechConstant.ACCENT, "mandarin");
//        //设置标点符号
//        proxy.setParameter(SpeechConstant.ASR_PTT, "1");
//        setAsrCommonParamters();
//    }

    private void setGrammarParam() {//语法引擎参数
        LogUtils.i(TAG, "set asr param...");
        //清空参数
        proxy.setParameter(SpeechConstant.PARAMS, null);
        //设置引擎类型
        proxy.setParameter(SpeechConstant.ENGINE_TYPE, mGrammarEngineType);
        //设置引擎模式,已在application设置
        //proxy.setParameter(SpeechConstant.ENGINE_MODE, SpeechConstant.MODE_MSC);
        if (mGrammarEngineType.equals(SpeechConstant.TYPE_MIX)) {
            //设置在线语义参数：
            //设置是否进行语义识别
            proxy.setParameter("asr_sch", "1");
            //设置开放语义协议版本号
            proxy.setParameter(SpeechConstant.NLP_VERSION, "2.0");
            //混合模式的类型
            //1）realtime 实时返回本地结果，优先使用本地识别引擎
            //2) delay 延时等待云端结果，即等待云端超时返回本地结果
            proxy.setParameter(SpeechConstant.MIXED_TYPE, "realtime");
            //混合超时，仅在delay类型下生效
            //proxy.setParameter(SpeechConstant.MIXED_TIMEOUT, "2500");
        }
        // 设置本地识别资源
        proxy.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
        // 设置语法构建路径
        proxy.setParameter(ResourceUtil.GRM_BUILD_PATH, grammarBuildPath);
        setAsrCommonParamters();
    }

    private void setAsrCommonParamters() {
        //设置返回结果格式
        proxy.setParameter(SpeechConstant.RESULT_TYPE, mGrammarResultType);
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        proxy.setParameter(SpeechConstant.ASR_PTT, "1");
        //音频采用率
        proxy.setParameter(SpeechConstant.SAMPLE_RATE, DEFAULT_SAMPLE_RATE);
        proxy.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        // 设置识别的阈值
        proxy.setParameter(SpeechConstant.MIXED_THRESHOLD, "30");
        proxy.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");
        //只有设置这个属性为1时,VAD_BOS  VAD_EOS才会生效,
        //且RecognizerListener.onVolumeChanged才有音量返回;
        //默认：1
        proxy.setParameter(SpeechConstant.VAD_ENABLE, "1");
        // 设置语音前端点->静音超时时间，即用户多长时间不说话则当做超时处理
        proxy.setParameter(SpeechConstant.VAD_BOS, DEFAULT_VAD_BOS);
        // 设置语音后端点->后端点静音检测时间，即用户停止说话多长时间内即认为不再输入
        proxy.setParameter(SpeechConstant.VAD_EOS, DEFAULT_VAD_EOS);
        //设置录取音频的最长时间，在录音模式时，当录音时长超过这个值时，sdk自动结束录音
        proxy.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, DEFAULT_SPEECH_TIMEOUT);
        //音频源来源，默认-1表示麦克风输入，通过writeAudio函数送入音频;
        //-2表示音频源来自本地文件，即先把音频保存到指定文件，告诉文件路径让语音引擎读文件
        proxy.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        // 设置本地识别使用语法id（如果使用云端，这里需注释掉）
        proxy.setParameter(SpeechConstant.LOCAL_GRAMMAR, "call");
        //设置音频保存路径，保存音频格式支持pcm、wav，
        // WRITE_EXTERNAL_STORAGE权限
        //即通过麦克风输入的录音数据，识别同时保存到文件中.
        proxy.setParameter(SpeechConstant.AUDIO_FORMAT, DEFAULT_AUDIO_FORMAT);
        proxy.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()
                + "/iflytek/" + /*(isSTTEnable() ? "IatRecognize.pcm" : */"grammarRecognize.pcm");
        /**针对在远场识别效果不理想的情况，建议使用远场识别引擎*/
        proxy.setParameter("ent", "smsfar16k");
        proxy.setParameter("domain", "fariat");
        proxy.setParameter("aue", "speex-wb;10");
    }

    //获取识别资源路径
    private String getResourcePath(){
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "asr/common.jet"));
        return tempBuffer.toString();
    }
}
