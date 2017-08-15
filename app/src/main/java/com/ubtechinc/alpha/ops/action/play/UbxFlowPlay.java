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

import android.content.Context;

import com.ubtechinc.alpha.ops.action.Constraints;
import com.ubtechinc.alpha.ops.action.Page;
import com.ubtechinc.alpha.ops.action.UbxFlow;
import com.ubtechinc.alpha.ops.action.utils.UbxFlowHelper;
import com.ubtechinc.alpha.ops.action.utils.UbxParser;

import timber.log.Timber;

/**
 * @desc : 流程播放
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/30
 * @modifier:
 * @modify_time:
 */

public class UbxFlowPlay implements IPlayControl {
    private final Context mContext;
    private UbxFlow mUbxFlow;

    private final String mFile;
    private boolean prepared = false;
    private PlayController mController;
    private final IPlayCompletedCallback callback;

    public UbxFlowPlay(Context cxt, String file, IPlayCompletedCallback callback){
        this.mContext = cxt;
        this.mFile = file;
        this.callback = callback;
    }

    @Override
    public boolean prepare(){
        if (prepared) return true;
        UbxFlow flow = UbxParser.parseUbxFile(mFile);
        prepared =  flow != null;
        if (prepared){
            mUbxFlow = flow;
            mController = new PlayController(mContext, mUbxFlow, mFile, callback);
            mController.prepared();
        }
        return prepared;
    }

    @Override
    public void play(byte[] data, @ILayerResult.DataType int nType) {
        if (!prepared) return;
        Page rootPage = UbxFlowHelper.findRootPage(mUbxFlow);
        if (rootPage == null) {
            Timber.w("can't find root page...");
            if (callback != null) callback.onPlayCompleted(CompletedState.ERROR);
            return;
        }
        final int startBlockId = Constraints.START_BLOCK_ID;
        final int startPortId = Constraints.START_BLOCK_START_PORT_ID;
        PagePlay pagePlay = new PagePlay(rootPage, startBlockId, startPortId);
        pagePlay.setPlayer(mController);
        pagePlay.play(data,nType);
    }

    @Override
    public boolean pause() {
        if (!prepared) return false;
        if (mController.running()) {
            mController.pause();
            return true;
        }
        return false;
    }

    @Override
    public boolean resume() {
        if (!prepared) return false;
        if (mController.isPause()){
            mController.resume();
            return true;
        }
        return false;
    }

    @Override
    public void stop() {
        if (!prepared) return;
        if (mController.isStopped())
            return;
        mController.stop();
    }
}
