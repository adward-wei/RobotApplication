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
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.ops.action.Block;
import com.ubtechinc.alpha.ops.action.CancelableTask;
import com.ubtechinc.alpha.ops.action.ControlLayer;
import com.ubtechinc.alpha.ops.action.Page;
import com.ubtechinc.alpha.ops.action.Port;
import com.ubtechinc.alpha.ops.action.UbxFlow;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;

import timber.log.Timber;

/**
 * @desc : 播放控制类
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/5
 * @modifier:
 * @modify_time:
 */

final class PlayController {
    private static final int INIT = -1;
    private static final int PREPARE = 0;
    private static final int STOP = 1;
    private static final int PAUSE = 2;
    private static final int RUNNING = 3;
    private static final int INTERRUPT = 4;
    @IntDef(value = {INIT, PREPARE, STOP, PAUSE, RUNNING, INTERRUPT})
    private  @interface State{}

    private final UbxFlow mUbxFlow;
    private final String mUbxFile;
    private IPlayCompletedCallback callback;

    private Context mContext;
    private Semaphore pause_semaphore = new Semaphore(0);
    private ScheduledExecutorService mBlockPlayer;
    private SparseArray<CancelableTask> mBlockTasks = new SparseArray<>();
    private volatile  @State
    int state = INIT;

    private Semaphore wait_semaphore = new Semaphore(0);
    private ExecutorService mLayerPlayer;

    PlayController(Context mContext, UbxFlow flow, String file, IPlayCompletedCallback callback) {
        this.mContext = mContext;
        this.mUbxFlow = flow;
        this.mUbxFile = file;
        this.callback = callback;
    }

    UbxFlow getFlow() {
        return mUbxFlow;
    }

    Context getContext() {
        return mContext;
    }

    String getUbxFile() {
        return mUbxFile;
    }

    synchronized void prepared() {
        if (state == INIT){
            mBlockPlayer = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    return new Thread(r,"BlockPlayer");
                }
            });
            mLayerPlayer = Executors.newSingleThreadExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    Thread thread = new Thread(r, "LayerPlayer");
                    thread.setPriority(Thread.MAX_PRIORITY);
                    return thread;
                }
            });
            state = PREPARE;
        }
    }

    synchronized boolean isStopped() {
        return state == STOP || state == INTERRUPT;
    }

    synchronized void stop() {
        if (isStopped()) return;
        if (isPause()) {
            pause_semaphore.release(1);
        }
        state = INTERRUPT;
    }

    synchronized boolean running() {
        return state == RUNNING;
    }

    synchronized void pause() {
        if (running()){
            state = PAUSE;
        }
    }

    synchronized boolean isPause() {
        return state == PAUSE;
    }

    synchronized void resume() {
        if (isPause()){
            state = RUNNING;
            pause_semaphore.release(1);
        }
    }

    synchronized boolean isInterrupted() {
        return state == INTERRUPT;
    }

    /////////////////块播放逻辑////////////////////
    void playBlock(final Page page, final Block block, final Port port) {
        if (isStopped() || mBlockPlayer == null) return;
        final CancelableTask task = new CancelableTask() {
            @Override
            public void run() {
                Timber.d("block task enter...");
                if (isCanceled()){
                    Timber.d("block task be canceled..");
                    if (mBlockTasks.size() == 0){
                        state = STOP;
                        Timber.d("ubx play completed...11");
                        finished(CompletedState.FINISHED);
                    }
                    return;
                }
                while(isPause()){
                    Timber.d("block task be paused..");
                    try {
                        pause_semaphore.acquire(1);
                    } catch (InterruptedException e) {
                        Timber.d(e.getMessage());
                    }
                }
                Timber.d("block task begin...");
                if (!isCanceled() && !isStopped()) {
                    handleBlockPlay(page, block, port);
                }
                synchronized (PlayController.this) {
                    mBlockTasks.remove(makeKey(page.getId(), block.getId()));
                }
                Timber.d("block task end...");
                if (isInterrupted()){
                    Timber.d("ubx play completed...22");
                    finished(CompletedState.INTERRUPT);
                }else if (mBlockTasks.size() == 0){
                    state = STOP;
                    Timber.d("ubx play completed...33");
                    finished(CompletedState.FINISHED);
                }

            }
        };
        synchronized (this){
            mBlockTasks.put(makeKey(page.getId(),block.getId()), task);
            if (state == PREPARE){
                state = RUNNING;
            }
        }
        mBlockPlayer.execute(task);
    }

    private void finished(CompletedState state) {
        if (callback != null) {
            callback.onPlayCompleted(state);
            callback = null;
        }
        if (mBlockTasks != null)
            mBlockTasks.clear();
        mBlockTasks = null;
        if (mLayerPlayer != null)
            mLayerPlayer.shutdown();
        mLayerPlayer = null;
        if (mBlockPlayer != null)
            mBlockPlayer.shutdown();
        mBlockPlayer = null;
    }

    private static int makeKey(int pageId , int blockId) {
        return pageId << 16 | (blockId & 0xffff);
    }

    private void handleBlockPlay(Page page, Block block, Port port) {
        ControlLayer layer = null;
        if (port.getType() == Port.TYPE_GENERAL || port.getType() == Port.TYPE_START){
            layer = block.getLayer();
        }else if (port.getType() == Port.TYPE_CANCEL){
            if (block.getType() == Block.BLOCK_FLOWCHART){
                cancelBlockTasksByPageId(block.getLinkToId());
            }else if (block.getType() == Block.BLOCK_ACTION|| block.getType() == Block.BLOCK_VOICE){
                cancelBlockTasksByPageIdBlockId(page.getId(), block.getId());
            }else if (block.getType() == Block.BLOCK_LOGIC){
                cancelBlockTasksByPageId(block.getLinkToId());
            }
        }else if (port.getType() == Port.TYPE_STOP){
            LogUtils.i("line data error....");
        }
        if(layer == null) return;
        ControlLayerPlay layerPlay = new ControlLayerPlay(layer, block);
        layerPlay.setController(this);

        layerPlay.play(port.getData(), port.getDataType());
        try {
            wait_semaphore.acquire(1);
        } catch (InterruptedException e) {
            Timber.d(e.getMessage());
        }
        PagePlay play = new PagePlay(page, block.getId(), layerPlay.getOutPortId());
        play.setPlayer(this);
        play.play(layerPlay.getOutData(), layerPlay.getOutDataType());
    }

    private void cancelBlockTasksByPageIdBlockId(int pageId, int blockId) {
        synchronized (this){
            for (int i = 0; i < mBlockTasks.size(); i++){
                int key = mBlockTasks.keyAt(i);
                if (key == makeKey(pageId, blockId)) {
                    mBlockTasks.get(key).cancel();
                    mBlockTasks.remove(key);
                    return;
                }
            }
        }
    }

    private void cancelBlockTasksByPageId(int pageId) {
        synchronized (this){
            for (int i = 0; i < mBlockTasks.size(); i++){
                int key = mBlockTasks.keyAt(i);
                if (((key >> 16) & 0xffff) == pageId) {
                    mBlockTasks.get(key).cancel();
                    mBlockTasks.remove(key);
                }
            }
        }
    }

    /////////////////层播放逻辑////////////////////
    void playIfLogic(final IfLogicPlay logic, final byte[] data, final int nType) {
        mLayerPlayer.execute(new Runnable() {
            @Override
            public void run() {
                Timber.d("IfLogic layer task enter...");
                logic.play(data, nType);
                wait_semaphore.release(1);
                Timber.d("IfLogic layer task exit...");
            }
        });
    }

    void playSwitchLogic(final SwitchLogicPlay logic, final byte[] data, final int nType) {
        mLayerPlayer.execute(new Runnable() {
            @Override
            public void run() {
                Timber.d("SwitchLogic layer task enter...");
                logic.play(data, nType);
                wait_semaphore.release(1);
                Timber.d("SwitchLogic layer task exit...");
            }
        });
    }

    void playForLogic(final ForLogicPlay logic, final byte[] data, final int nType) {
        mLayerPlayer.execute(new Runnable() {
            @Override
            public void run() {
                Timber.d("ForLogic layer task enter...");
                logic.play(data,nType);
                wait_semaphore.release(1);
                Timber.d("ForLogic layer task exit...");
            }
        });
    }

    void playVoiceRecognize(final RecognizePlay recognize, final byte[] data, final int nType) {
        mLayerPlayer.execute(new Runnable() {
            @Override
            public void run() {
                Timber.d("Recognize layer task enter...");
                recognize.play(data, nType);
                wait_semaphore.release(1);
                Timber.d("Recognize layer task exit...");
            }
        });
    }

    void playVoiceTTS(final TTSPlay tts, final byte[] data, final int nType) {
        mLayerPlayer.execute(new Runnable() {
            @Override
            public void run() {
                Timber.d("TTS layer task enter...");
                tts.play(data, nType);
                wait_semaphore.release(1);
                Timber.d("TTS layer task exit...");
            }
        });
    }

    void playMotion(final MotionPlay play, final byte[] data, final int nType) {
        mLayerPlayer.execute(new Runnable() {
            @Override
            public void run() {
                Timber.d("Motion layer task enter...");
                play.play(data,nType);
                wait_semaphore.release(1);
                Timber.d("Motion layer task exit...");
            }
        });
    }
}
