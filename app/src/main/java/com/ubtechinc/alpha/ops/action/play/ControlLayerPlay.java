/*
 *
 *  *
 *  *  *
 *  *  * Copyright (c) 2008-2017 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *  *
 *  *
 *
 */

package com.ubtechinc.alpha.ops.action.play;

import com.ubtechinc.alpha.ops.action.Block;
import com.ubtechinc.alpha.ops.action.ControlLayer;
import com.ubtechinc.alpha.ops.action.logic.ForLogic;
import com.ubtechinc.alpha.ops.action.logic.ILogic;
import com.ubtechinc.alpha.ops.action.logic.IfLogic;
import com.ubtechinc.alpha.ops.action.logic.LogicControlLayer;
import com.ubtechinc.alpha.ops.action.logic.SwitchLogic;
import com.ubtechinc.alpha.ops.action.motion.MotionControlLayer;
import com.ubtechinc.alpha.ops.action.voice.IVoice;
import com.ubtechinc.alpha.ops.action.voice.VoiceControlLayer;
import com.ubtechinc.alpha.ops.action.voice.VoiceRecognize;
import com.ubtechinc.alpha.ops.action.voice.VoiceTTS;

/**
 * @desc : 控制层播放
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/5
 * @modifier:
 * @modify_time:
 */

class ControlLayerPlay implements IPlayer, ILayerResult {
    private @Block.Type final int mBlockType;
    private final Block mOwnedBlock;
    private final ControlLayer controlLayer;
    private ILayerResult layer;
    private PlayController controller;

    ControlLayerPlay(ControlLayer layer, Block block) {
        this.controlLayer = layer;
        this.mBlockType = block.getType();
        this.mOwnedBlock = block;
    }

    void setController(PlayController playController) {
        this.controller = playController;
    }

    @Override
    public void play(byte[] data, @ILayerResult.DataType int nType) {
        if (this.mBlockType == Block.BLOCK_ACTION){
            MotionControlLayer motionLayer = (MotionControlLayer) controlLayer;
            playMotionLayer(motionLayer, data, nType);
        }else if (this.mBlockType == Block.BLOCK_LOGIC){
            LogicControlLayer logicLayer = (LogicControlLayer) controlLayer;
            playLogicLayer(logicLayer, data, nType);
        }else if (this.mBlockType == Block.BLOCK_VOICE){
            VoiceControlLayer voiceLayer = (VoiceControlLayer) controlLayer;
            playVoiceLayer(voiceLayer, data, nType);
        }
    }

    private void playMotionLayer(MotionControlLayer motionLayer, byte[] data, @ILayerResult.DataType int nType) {
        MotionPlay play = new MotionPlay(controller,mOwnedBlock, motionLayer);
        layer = play;
        controller.playMotion(play, data,nType);
    }

    private void playLogicLayer(LogicControlLayer logicLayer, byte[] data, @ILayerResult.DataType int nType) {
        final @ILogic.Type int logicType = logicLayer.getType();
        if (logicType == ILogic.IF_LOGIC){
            IfLogic logic = (IfLogic) logicLayer.getLogic();
            IfLogicPlay play = new IfLogicPlay(logic ,mOwnedBlock);
            layer = play;
            controller.playIfLogic(play, data, nType);
        }else if (logicType == ILogic.FOR_LOGIC){
            ForLogic logic = (ForLogic) logicLayer.getLogic();
            ForLogicPlay play = new ForLogicPlay(logic, mOwnedBlock);
            layer = play;
            controller.playForLogic(play, data, nType);
        }else {
            SwitchLogic logic = (SwitchLogic) logicLayer.getLogic();
            SwitchLogicPlay play = new SwitchLogicPlay(logic, mOwnedBlock);
            layer = play;
            controller.playSwitchLogic(play, data, nType);
        }
    }

    private void playVoiceLayer(VoiceControlLayer voiceLayer, byte[] data, @ILayerResult.DataType int nType) {
        final @IVoice.Type int voiceType = voiceLayer.getType();
        if (voiceType == IVoice.TYPE_TTS){
            VoiceTTS tts = (VoiceTTS) voiceLayer.getVoice();
            TTSPlay play = new TTSPlay(tts, mOwnedBlock);
            layer = play;
            controller.playVoiceTTS(play, data, nType);
        }else {
            VoiceRecognize recognize = (VoiceRecognize) voiceLayer.getVoice();
            RecognizePlay play = new RecognizePlay(recognize ,mOwnedBlock);
            controller.playVoiceRecognize(play, data, nType);
            layer = play;
        }
    }

    @Override
    public byte[] getOutData() {
        return layer.getOutData();
    }

    @Override
    public @ILayerResult.DataType int getOutDataType() {
        return layer.getOutDataType();
    }

    @Override
    public int getOutPortId() {
        return layer.getOutPortId();
    }
}
