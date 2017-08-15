package com.ubtechinc.alpha.speech.recognizer;

import android.content.Context;

import com.nuance.dragon.toolkit.audio.AudioChunk;
import com.nuance.dragon.toolkit.audio.AudioType;
import com.nuance.dragon.toolkit.audio.SpeechDetectionListener;
import com.nuance.dragon.toolkit.audio.pipes.BufferingDuplicatorPipe;
import com.nuance.dragon.toolkit.audio.pipes.SpeexEncoderPipe;
import com.nuance.dragon.toolkit.cloudservices.CloudConfig;
import com.nuance.dragon.toolkit.cloudservices.CloudServices;
import com.nuance.dragon.toolkit.cloudservices.DictionaryParam;
import com.nuance.dragon.toolkit.cloudservices.NMTConfig;
import com.nuance.dragon.toolkit.cloudservices.SSLConfig;
import com.nuance.dragon.toolkit.cloudservices.recognizer.CloudRecognitionError;
import com.nuance.dragon.toolkit.cloudservices.recognizer.CloudRecognitionResult;
import com.nuance.dragon.toolkit.cloudservices.recognizer.CloudRecognizer;
import com.nuance.dragon.toolkit.cloudservices.recognizer.RecogSpec;
import com.nuance.dragon.toolkit.data.Data;
import com.nuance.dragon.toolkit.file.FileManager;
import com.nuance.dragon.toolkit.vocon.Grammar;
import com.nuance.dragon.toolkit.vocon.ParamSpecs;
import com.nuance.dragon.toolkit.vocon.VoconConfig;
import com.nuance.dragon.toolkit.vocon.VoconContext;
import com.nuance.dragon.toolkit.vocon.VoconError;
import com.nuance.dragon.toolkit.vocon.VoconParam;
import com.nuance.dragon.toolkit.vocon.VoconRecognizer;
import com.nuance.dragon.toolkit.vocon.VoconResult;
import com.nuance.dragon.toolkit.vocon.VoconWarning;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.ThreadPool;
import com.ubtechinc.alpha.speech.nuance.NuanceServerSettings;
import com.ubtechinc.alpha.speech.nuance.NuanceSettings;
import com.ubtechinc.alpha.speech.recoder.PcmAudioSource;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc : nuance语音识别包装
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/30
 * @modifier:
 * @modify_time:
 */

public final class NuanceRecongnizer implements ISpeechRecognizer {
    private static final String TAG = "Nuance";
    private static final String GRAMMAR_NAME = "ubt_grammar.fcf";
    private PcmAudioSource mPcmAudioSource = new PcmAudioSource(AudioType.PCM_16k);//接收麦克风阵列的source;

    //本地语音识别
    private final VoconRecognizer mVoconRecognizer;
    private BufferingDuplicatorPipe<AudioChunk> mLocalSource;
    private HashMap<VoconParam, Integer> mVoconParams = new HashMap<VoconParam, Integer>();
    private final VoconContext mVoconContext;
    //云端语音识别
    private final CloudRecognizer mCloudRecognizer;
    private final CloudServices mCloudServices;
    private SpeexEncoderPipe mCloudSource;
    private RecogSpec mRecogSpec;

    private Grammar mGrammar;
    private IGrammarListener grammarInitListener;
    private volatile IRecognizerListener mRecognizerListener;

    private volatile EngineType engineType = EngineType.LOCAL;
    private volatile boolean recording;

    private VoconRecognizer.RebuildListener rebuildListener = new VoconRecognizer.RebuildListener() {
        @Override
        public void onComplete(Grammar grammar, List< SkippedWord > skippedWords) {
            LogUtils.i(TAG, "nuance离线语法构建成功");
            if (grammarInitListener != null){
                grammarInitListener.onBuildFinish("", 0);
            }
        }
        @Override
        public void onError(VoconError error) {
            LogUtils.i(TAG, "nuance离线语法构建失败");
        }
    };

    public NuanceRecongnizer(Context context) {
        //建立audiosource
        mLocalSource = new BufferingDuplicatorPipe<>(2);//复制两份
        mLocalSource.connectAudioSource(mPcmAudioSource);
        mCloudSource = new SpeexEncoderPipe();
        mCloudSource.connectAudioSource(mLocalSource);

        //离线
        LogUtils.i(TAG,"nuance本地引擎初始化");
        this.mVoconContext = new VoconContext(GRAMMAR_NAME);
        this.mVoconRecognizer = VoconRecognizer.createVoconRecognizer(
                new FileManager(context, ".jpg", "vocon", "vocon"));
        mVoconRecognizer.enableVerboseAndroidLogging(true);

        LogUtils.i(TAG,"nuance云端引擎初始化");
        final SSLConfig ssl = new SSLConfig(false, null, null);
        List<NMTConfig> nmtConfig = new ArrayList<NMTConfig>(){{add(ssl);}};
        CloudConfig cloudConfig = new CloudConfig("UBTAlpha",
                NuanceServerSettings.HOST, NuanceServerSettings.PORT,
                nmtConfig, NuanceServerSettings.APPID, NuanceServerSettings.APPKEY,
                null, AudioType.PCM_16k, AudioType.PCM_16k);
        mCloudServices = CloudServices.createCloudServices(context, cloudConfig);
        mCloudRecognizer = new CloudRecognizer(mCloudServices);
    }

    @Override
    public void init() {
        //离线语法
        mVoconRecognizer.initialize(new VoconConfig("acmod5_4000_enu_gen_car_f16_v2_0_0.dat",
                "clc_enu_cfg3_v6_0_4.dat"), "speechrecognizer", new VoconRecognizer.InitializeListener() {
            @Override
            public void onLoaded(VoconRecognizer vocon, boolean success) {
                if (!success){
                    LogUtils.i(TAG,"nuance本地引擎初始化失败");
                    return;
                }
                LogUtils.i(TAG,"nuance本地语音引擎初始化成功");

                //nuance离线语法
                mGrammar = Grammar.createGrammar(new ArrayList<VoconContext>(){
                    { add(mVoconContext);}
                }, null);
                mVoconRecognizer.loadGrammar(mGrammar, rebuildListener);
            }
        });

        //离线语音参数
        mVoconParams.put(ParamSpecs.Fx.START_ENABLE, ParamSpecs.Boolean.TRUE);
        mVoconParams.put(ParamSpecs.Fx.MINSPEECH, 60);
        mVoconParams.put(ParamSpecs.Fx.TIMEOUT_LSILENCE, 0);
        mVoconParams.put(ParamSpecs.Fx.TIMEOUT_SPEECH, 8000);
        mVoconParams.put(ParamSpecs.Fx.EVENT_TIMER, 100);
        mVoconParams.put(ParamSpecs.Fx.KNOWN_SPEAKER_CHANGES, ParamSpecs.Boolean.FALSE);
        mVoconParams.put(ParamSpecs.Fx.FARTALK, ParamSpecs.Boolean.TRUE);
        mVoconParams.put(ParamSpecs.Fx.SPEAKER_ADAPTATION, ParamSpecs.SpeakerAdaptMode.AUTOMATIC);
        mVoconParams.put(ParamSpecs.Fx.ENABLEFETCHPCMAUDIO, ParamSpecs.Boolean.FALSE);
        mVoconParams.put(ParamSpecs.Fx.TSILENCE, 300);
        mVoconParams.put(ParamSpecs.Fx.SENSITIVITY, 100);
        Map<VoconParam, Integer> ctxParams = new HashMap<VoconParam, Integer>();
        ctxParams.put(ParamSpecs.Ctx.TSILENCE, 600);
        ctxParams.put(ParamSpecs.Ctx.ACCURACY, 20000);
        ctxParams.put(ParamSpecs.Ctx.TSILENCE_FX, 800);
        ctxParams.put(ParamSpecs.Ctx.MAXNBEST, 5);
        ctxParams.put(ParamSpecs.Ctx.MAXNBEST_SECONDPASS, 1000);
        mVoconContext.setParams(ctxParams);
        //云端语音参数
        Data.Dictionary settings = new Data.Dictionary();
        settings.put("context_tag", NuanceSettings.CONTEXT_TAG);
        settings.put("dictation_language", "eng-USA");
        mRecogSpec = new RecogSpec(NuanceSettings.CMD, settings, "AUDIO_INFO");
        Data.Dictionary requestInfo = new Data.Dictionary();
        requestInfo.put("start", 0);
        requestInfo.put("end", 0);
        requestInfo.put("text", "");
        Data.Dictionary appServerDataDict = new Data.Dictionary();
        requestInfo.put("appserver_data", appServerDataDict);
        mRecogSpec.addParam(new DictionaryParam("REQUEST_INFO", requestInfo));
    }

    @Override
    public int buildGrammar(String grammarStr, IGrammarListener listener) {
        this.grammarInitListener = listener;
        try {
            mGrammar = Grammar.createFromJSON(new JSONObject(grammarStr));
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
        mVoconRecognizer.loadGrammar(mGrammar, rebuildListener);
        return 0;
    }

    public void setEngineType(EngineType mode) {
        if (mode == engineType) return;
        engineType = mode;
    }

    @Override
    public EngineType getCurSpeechMode() {
        return engineType;
    }

    @Override
    public int startListening(final IRecognizerListener listener) {
        this.mRecognizerListener = listener;
        if (isLocalEnable()){
            mVoconRecognizer.startRecognition(mLocalSource, mVoconParams, mGrammar.getContexts(), new SpeechDetectionListener() {
                @Override
                public void onStartOfSpeech() {
                    LogUtils.i(TAG,"nuance 开始录音...");
                    recording = true;
                    if (mRecognizerListener != null){
                        mRecognizerListener.onBegin();
                    }
                }

                @Override
                public void onEndOfSpeech() {
                    recording = false;
                    LogUtils.i(TAG,"nuance 结束录音...");
                    if (mRecognizerListener != null){
                        mRecognizerListener.onEnd();
                    }
                }
            }, new VoconRecognizer.SignalListener() {
                @Override
                public void onUpdate(float v) {
                    LogUtils.w(TAG, "nuance 语音更新信号："+ v);
                }

                @Override
                public void onWarning(VoconWarning voconWarning) {
                    LogUtils.w(TAG, "nuance 语音警告信号："+ voconWarning.getReason());
                }
            }, new VoconRecognizer.ResultListener() {
                @Override
                public void onResult(VoconResult voconResult) {
                    recording = false;
                    //main thread
                    LogUtils.i(TAG, "nuance 本地语音识别结果："+ voconResult.getRawResult().toString());
                    if (mRecognizerListener != null){
                        mRecognizerListener.onResult(voconResult.getRawResult().toString(), false);
                    }
                }

                @Override
                public void onError(VoconError voconError) {
                    //main thread
                    recording = false;
                    LogUtils.i(TAG, "nuance 本地语音识别错误："+voconError.getReason());
                    if (mRecognizerListener != null){
                        mRecognizerListener.onError(voconError.getReasonCode());
                    }
                }
            });
        }else {
            mCloudRecognizer.startRecognition(mRecogSpec, mCloudSource, new CloudRecognizer.Listener() {
                @Override
                public void onResult(CloudRecognitionResult cloudRecognitionResult) {
                    //main thread
                    final String result = cloudRecognitionResult
                            .getDictionary()
                            .toJSON()
                            .toString();
                    LogUtils.i(TAG, "nuance 云端语音识别结果："+ result);
                    if (mRecognizerListener != null){
                        mRecognizerListener.onResult(result, false);
                    }
                }

                @Override
                public void onError(CloudRecognitionError cloudRecognitionError) {
                    //main thread
                    LogUtils.i(TAG, "nuance 云端语音识别错误："+ cloudRecognitionError.toJSON().toString());
                    if (mRecognizerListener != null){
                        mRecognizerListener.onError(cloudRecognitionError.getTransactionError().getErrorCode());
                    }
                }

                @Override
                public void onTransactionIdGenerated(String s) {
                    LogUtils.i(TAG, "nuance 云端语音识别transaction："+ s);
                    if(mRecognizerListener !=null){
                        mRecognizerListener.onBegin();
                    }
                }
            });
        }
        recording = true;
        return 0;
    }

    @Override
    public int writeAudio(final byte[] data, final int start, final int end) {
        ThreadPool.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                mPcmAudioSource.writeAudio(data, end - start);
            }
        });
        return 0;
    }

    @Override
    public void stopListening() {
        if (isLocalEnable()){
            mVoconRecognizer.stopListening();
        }else {
            mCloudRecognizer.cancel();
        }
        recording = false;
    }

    private boolean isLocalEnable() {
        return engineType == EngineType.LOCAL;
    }

    @Override
    public boolean isListening() {
        return recording;
    }

    @Override
    public void cancel() {
        recording = false;
        if (isLocalEnable()){
            mVoconRecognizer.cancelRecognition();
        }else {
            mCloudRecognizer.cancel();
        }
    }

    @Override
    public boolean destroy() {
        recording = false;
        if (mCloudRecognizer != null) {
            mCloudRecognizer.cancel();
        }
        if (mVoconRecognizer != null) {
            mVoconRecognizer.release();
        }
        if (mCloudServices != null){
            mCloudServices.release();
        }
        if (mGrammar != null){
            mGrammar.getContexts().clear();
            mGrammar = null;
        }
        if (mLocalSource != null){
            mLocalSource.disconnectAudioSource();
            mLocalSource = null;
        }
        if (mCloudSource != null){
            mCloudSource.disconnectAudioSource();
            mCloudSource = null;
        }
        return true;
    }
}
