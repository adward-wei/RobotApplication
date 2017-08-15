package com.ubtechinc.alpha.speech.utils;

import android.os.Environment;

import com.ubtech.utilcode.utils.FileUtils;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.event.ErrorEvent;
import com.ubtechinc.alpha.model.speech.SlotValue;
import com.ubtechinc.alpha.speech.AbstractSpeech;
import com.ubtechinc.alpha.speech.ifytek.JsonParser;
import com.ubtechinc.alpha.speech.ifytek.OfflineSlotProcessor;
import com.ubtechinc.alpha.strategy.IOfflineSlotProcessor;

import java.io.File;
import java.nio.charset.Charset;

/**
 * @desc : 用于处理语音识别结果的工具
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/6/6
 * @modifier:
 * @modify_time:
 */

public class IfytekRecUtil extends IRecUtil{
    public static final int LOCAL_GRAMMAR_SCORE_VALUE = 30;
    private static final String TAG = IfytekRecUtil.class.getSimpleName();
    private static final String IFYTEK_SCORE_CONFIG = "ifytek_score.txt";
    private AbstractSpeech mSpeechContext;
    private IOfflineSlotProcessor defaultSlotProcessor;
    private int mScore = LOCAL_GRAMMAR_SCORE_VALUE;
    public volatile String mPackageName;

    public IfytekRecUtil(AbstractSpeech speechContext) {
        super(speechContext);
        this.mSpeechContext = speechContext;
        mScore = StringUtils.stringToInt(FileUtils.readFile2String(Environment.getExternalStorageDirectory().getPath()+ File.separator+ IFYTEK_SCORE_CONFIG,
                Charset.defaultCharset().name()), LOCAL_GRAMMAR_SCORE_VALUE);
        LogUtils.i(TAG,"当前置信度均值:"+ mScore);
        defaultSlotProcessor = new OfflineSlotProcessor();
    }

    public IfytekRecUtil(AbstractSpeech speechContext, String packageName) {
        this(speechContext);
        this.mPackageName = packageName;
    }

    @Override
    protected boolean filterGrammarResult(String result) {
        int score = JsonParser.getScoreOfGrammar(result);
        LogUtils.I(TAG,"识别分数:%d 返回:%s", score, result);
        //离线语法的置信度需大于IFYTEK_SCORE_CONFIG
        if ((score != -1 && score <= mScore)) {
            LogUtils.i(TAG,"识别置信度低于" + mScore);
            mSpeechContext.notifyErrorEvent(ErrorEvent.ERROR_SPEECH_ERROR);
            restartRecognize();
            return true;
        }

        final boolean isOffline = JsonParser.isOfflineGrammar(result);
        if (isOffline){
            com.ubtechinc.alpha.speech.dispatch.Slot slot = JsonParser.getSlotFromAsrResult(result);
            if (slot != null && SlotValue.isDefaultSlot(SlotValue.valueToType(slot.getName()))){
                LogUtils.i(TAG,"主服务离线语义处理:default slot processor, slotName="+ slot.getName());
                defaultSlotProcessor.processOfflineSlot(slot.getName());
                restartRecognize();
                return true;
            }
            if (JsonParser.getOfflineSpeechWords(result).length() < 2){
                LogUtils.w(TAG,"忽略离线语义单个词...");
                restartRecognize();
                return true;
            }
        }else {
            //有标点符号
            if (JsonParser.getNetSpeechWords(result).length() < 3){
                LogUtils.w(TAG,"忽略网络语义单个词...");
                restartRecognize();
                return true;
            }
        }
        return false;
    }

    @Override
    protected String parseIatResult(String speechResult) {
        return JsonParser.parseIatResult(speechResult);
    }

    private void restartRecognize() {
        HandlerUtils.runUITask(new Runnable() {
            @Override
            public void run() {
                mSpeechContext.startSpeechAsr();
            }
        }, 200);
    }
}
