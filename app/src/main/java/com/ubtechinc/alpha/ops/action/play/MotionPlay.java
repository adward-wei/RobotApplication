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

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.animation.LinearInterpolator;

import com.ubtech.utilcode.utils.FileUtils;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.ops.RobotOpsManager;
import com.ubtechinc.alpha.ops.action.Block;
import com.ubtechinc.alpha.ops.action.Port;
import com.ubtechinc.alpha.ops.action.motion.Frame;
import com.ubtechinc.alpha.ops.action.motion.IMotionLayer;
import com.ubtechinc.alpha.ops.action.motion.LedFrame;
import com.ubtechinc.alpha.ops.action.motion.MotionControlLayer;
import com.ubtechinc.alpha.ops.action.motion.MotionGroup;
import com.ubtechinc.alpha.ops.action.motion.MotionGroupLayer;
import com.ubtechinc.alpha.ops.action.motion.MotorFrame;
import com.ubtechinc.alpha.ops.action.motion.MusicFrame;
import com.ubtechinc.alpha.ops.action.utils.BlockHelper;
import com.ubtechinc.alpha.ops.led.SetEarOp;
import com.ubtechinc.alpha.ops.led.SetEyeOp;
import com.ubtechinc.alpha.ops.led.SetMouthOp;
import com.ubtechinc.alpha.ops.motor.SendMotorDataOp;
import com.ubtechinc.alpha.utils.SysUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import timber.log.Timber;

/**
 * @desc : 动作层播放
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/5/8
 * @modifier:
 * @modify_time:
 */

public class MotionPlay implements IPlayer,ILayerResult {
    private final PlayController controller;
    private final Block mOwnedBlock;
    private final int totalTime;
    private final int timeScale;
    private AtomicInteger consumeTime = new AtomicInteger(0);
    private volatile int motorFrameGroupConsumeTime = 0;
    private volatile int ledFrameGroupConsumeTime = 0;
    private volatile int musicFrameGroupConsumeTime = 0;
    private final ArrayList<MotionGroup> motorGroups = new ArrayList<>();
    private final ArrayList<MotionGroup> earGroups = new ArrayList<>();
    private final ArrayList<MotionGroup> eyeGroups = new ArrayList<>();
    private final ArrayList<MotionGroup> mouthGroups = new ArrayList<>();
    private final ArrayList<MotionGroup> musicGroups = new ArrayList<>();
    private ExecutorService motorThread ;
    private ExecutorService ledThread ;
    private ExecutorService musicThread;
    private MusicPlayThread musicPlayThread;
    private byte[] outData;
    private @ILayerResult.DataType int dataType;

    MotionPlay(PlayController controller, Block ownedBlock, MotionControlLayer motionLayer) {
        this.controller = controller;
        this.mOwnedBlock = ownedBlock;
        this.totalTime = motionLayer.getTimeLayer().getTime();
        this.timeScale = Math.max(20, motionLayer.getTimeLayer().getTimeScale());
        try {
            for (int index = 0; index < motionLayer.getLayers().size(); index++){
                MotionGroupLayer groupLayer = motionLayer.getLayers().get(index);
                if (groupLayer.getType() == IMotionLayer.TYPE_MOTOR){
                    for (int j = 0 ; j < groupLayer.getGroups().size(); j++) {
                        motorGroups.add(groupLayer.getGroups().get(j).clone());
                    }
                }else if (groupLayer.getType() == IMotionLayer.TYPE_MUSIC){
                    for (int j = 0; j < groupLayer.getGroups().size(); j++){
                        musicGroups.add(groupLayer.getGroups().get(j).clone());
                    }
                }else if (groupLayer.getType() == IMotionLayer.TYPE_EAR){
                    for (int j = 0; j < groupLayer.getGroups().size(); j++){
                        earGroups.add(groupLayer.getGroups().get(j).clone());
                    }
                }else if (groupLayer.getType() == IMotionLayer.TYPE_EYE){
                    for (int j = 0; j < groupLayer.getGroups().size(); j++){
                        eyeGroups.add(groupLayer.getGroups().get(j).clone());
                    }
                }else if (groupLayer.getType() == IMotionLayer.TYPE_MOUTH){
                    for (int j = 0; j < groupLayer.getGroups().size(); j++){
                        mouthGroups.add(groupLayer.getGroups().get(j).clone());
                    }
                }
            }
        }catch (CloneNotSupportedException e){
            Timber.w(e.getMessage());
        }
        sortAllGroups();
        motorThread = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r,"MotorPlay");
            }
        });
        ledThread = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r,"LedPlay");
            }
        });
        musicThread = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r,"MusicPlay");
            }
        });
    }

    @Override
    public byte[] getOutData() {
        return outData;
    }

    @Override
    @ILayerResult.DataType public int getOutDataType() {
        return dataType;
    }

    @Override
    public int getOutPortId() {
        return BlockHelper.findPortIdByPortType(mOwnedBlock, Port.TYPE_STOP);
    }

    @Override
    public void play(byte[] data, @ILayerResult.DataType int nType) {
        this.outData = data==null? new byte[0]:data;
        this.dataType = nType;
        final int scale = timeScale;
        LogUtils.W("totalTime = %s, scale = %s", totalTime, scale);
        refreshFrame(scale);
    }

    private void refreshFrame(final int scale) {
        final ValueAnimator animator = ValueAnimator.ofInt(totalTime, 0);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(totalTime * scale);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (controller.isPause()){
                    animator.pause();
                }else if (controller.isStopped()){
                    animator.cancel();
                }else {
                    consumeTime.set(Math.round(totalTime * animation.getAnimatedFraction()));
                    updateFrame();
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) { stop(); }
            @Override
            public void onAnimationCancel(Animator animation) { stop(); }
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        HandlerUtils.runUITask(new Runnable() {
            @Override
            public void run() {
                animator.start();
            }
        });
        try {
            synchronized (this) {
                this.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        synchronized (this) {
            MotionPlay.this.notifyAll();
        }
        if (musicPlayThread != null)
            musicPlayThread.stopPlay();
        motorThread.shutdown();
        ledThread.shutdown();
        musicThread.shutdownNow();
    }

    private void updateFrame() {
        if (motorGroups.size() > 0) {
            updateMotorGroups(motorGroups);
        }
        if (earGroups.size() > 0) {
            updateLedGroups(earGroups);
        }
        if (eyeGroups.size() > 0){
            updateLedGroups(eyeGroups);
        }
        if (mouthGroups.size() > 0){
            updateLedGroups(motorGroups);
        }
        if (musicGroups.size() > 0) {
            updateMusicGroups(musicGroups);
        }
    }

    private void updateMotorGroups(final ArrayList<MotionGroup> motorGroups) {
        motorThread.execute(new Runnable() {
            @Override
            public void run() {
                if (motorGroups.size() <= 0) return;
                MotionGroup group = motorGroups.get(0);
                if (consumeTime.get() < (group.getStartTime() + motorFrameGroupConsumeTime)) return;
                ArrayList<Frame> frames = group.getFrames();
                if (frames.size() <= 0){
                    motorGroups.remove(0);
                    motorFrameGroupConsumeTime = 0;
                    return;
                }
                sortFrames(frames);
                MotorFrame frame = (MotorFrame) frames.remove(0);
                motorFrameGroupConsumeTime += (frame.getRunTime() +  frame.getHaltTime());
                if (consumeTime.get() < group.getStartTime() + motorFrameGroupConsumeTime){
                    byte[] motorAngles = new byte[20];
                    byte[] temp = new byte[4];
                    final byte[] dest = frame.getMotorData();
                    for (int j = 0; j < motorAngles.length; j++) {
                        System.arraycopy(dest, j * 8 + 4, temp, 0, 4);
                        motorAngles[j] = temp[0];
                    }
                    if (SysUtils.is2Mic()) {
                        motorAngles[19] = 0;
                        motorAngles[18] = 0;
                    }
                    short sContinueTime = (short) (frame.getRunTime() * timeScale);
                    RobotOpsManager.get(controller.getContext()).executeOpSync(new SendMotorDataOp(motorAngles, sContinueTime));
                }

                if (frames.size() == 0) {
                    motorGroups.remove(0);
                    motorFrameGroupConsumeTime = 0;
                }
            }
        });
    }

    private void updateLedGroups(final ArrayList<MotionGroup> ledGroups) {
        ledThread.execute(new Runnable() {
            @Override
            public void run() {
                if (ledGroups.size() <= 0 ) return;
                MotionGroup group = ledGroups.get(0);
                if (consumeTime.get() < group.getStartTime() + ledFrameGroupConsumeTime) return;
                ArrayList<Frame> frames = group.getFrames();
                if (frames.size() <= 0){
                    ledGroups.remove(0);
                    ledFrameGroupConsumeTime = 0;
                    return;
                }
                sortFrames(frames);
                LedFrame frame = (LedFrame) frames.remove(0);
                ledFrameGroupConsumeTime += (frame.getRunTime() + frame.getHaltTime());
                if (consumeTime.get() < group.getStartTime() + ledFrameGroupConsumeTime){
                    int type = frame.getType();
                    if (type == IMotionLayer.TYPE_EAR) {
                        RobotOpsManager.get(controller.getContext()).executeOp(new SetEarOp(
                                frame.getLeftLed(), frame.getRightLed(),
                                frame.getBright(),
                                (short) (frame.getOnTime() * timeScale ),
                                (short) (frame.getOffTime() * timeScale),
                                (short) (frame.getRunTime() * timeScale)
                        ));
                    }else if (type == IMotionLayer.TYPE_EYE){
                        RobotOpsManager.get(controller.getContext()).executeOp(new SetEyeOp(
                                frame.getLeftLed(), frame.getRightLed(),
                                frame.getBright(), frame.getColor(),
                                (short) (frame.getOnTime() * timeScale ),
                                (short) (frame.getOffTime() * timeScale),
                                (short) (frame.getRunTime() * timeScale)
                        ));
                    }else if (type == IMotionLayer.TYPE_MOUTH){
                        RobotOpsManager.get(controller.getContext()).executeOp(new SetMouthOp(
                                frame.getBright(),
                                (short)(frame.getOnTime() * timeScale),
                                (short)(frame.getOffTime() * timeScale),
                                (short)(frame.getRunTime() * timeScale),
                                frame.getMode()));
                    }
                }
                if (frames.size() == 0){
                    ledGroups.remove(0);
                    ledFrameGroupConsumeTime = 0;
                }
            }
        });
    }

    private void updateMusicGroups(final ArrayList<MotionGroup> musicGroups) {
        musicThread.execute(new Runnable() {
            @Override
            public void run() {
                if (musicGroups.size() <= 0 ) return;
                MotionGroup group = musicGroups.get(0);
                if (consumeTime.get() < group.getStartTime() + musicFrameGroupConsumeTime) return;
                ArrayList<Frame> frames = group.getFrames();
                if (frames.size() <= 0){
                    musicGroups.remove(0);
                    musicFrameGroupConsumeTime = 0;
                    return;
                }
                sortFrames(frames);
                MusicFrame frame = (MusicFrame) frames.remove(0);
                musicFrameGroupConsumeTime += frame.getHaltTime() + frame.getRunTime();
                if (consumeTime.get() < group.getStartTime() + musicFrameGroupConsumeTime) {

                    String musicFile = controller.getUbxFile();
                    musicFile = musicFile.substring(0, musicFile.length() - 4) + File.separator;
                    if (TextUtils.isEmpty(frame.getMusicFileName())) {
                        List<File> files = FileUtils.listFilesInDirWithFilter(musicFile, ".mp3", false);
                        if (files.size() > 0) {
                            musicFile = files.get(0).getAbsolutePath();
                        }
                    } else {
                        musicFile += frame.getMusicFileName();
                    }
                    Timber.d("musicfile: " + musicFile);
                    if (musicPlayThread != null) {
                        musicPlayThread.stopPlay();
                    }
                    musicPlayThread = new MusicPlayThread(controller, timeScale, frame.getRunTime() * timeScale, musicFile);
                    musicPlayThread.start();
                }
                if (frames.size() == 0){
                    musicGroups.remove(0);
                    musicFrameGroupConsumeTime = 0;
                }
            }
        });
    }

    private void sortAllGroups() {
        sortGroups(motorGroups);
        sortGroups(earGroups);
        sortGroups(eyeGroups);
        sortGroups(mouthGroups);
        sortGroups(musicGroups);
    }

    private static void sortGroups(ArrayList<MotionGroup> groups){
        Collections.sort(groups, new Comparator<MotionGroup>() {
            @Override
            public int compare(MotionGroup lhs, MotionGroup rhs) {
                return lhs.getStartTime() - rhs.getStartTime();
            }
        });
    }

    private static void sortFrames(ArrayList<Frame> frames){
        Collections.sort(frames, new Comparator<Frame>() {
            @Override
            public int compare(Frame lhs, Frame rhs) {
                return lhs.getId() - rhs.getId();
            }
        });
    }

    /**
     * 音乐播放线程类
     */
    private static class MusicPlayThread extends Thread{
        private final PlayController controller;
        /**持续播放的时间*/
        private final int totalTime;
        private final int scale;
        /**当前播放时间*/
        private int mCurrentTime;
        /**音乐的位置*/
        private final String musicPath;
        /**播放音乐控件*/
        private final MediaPlayer mediaPlayer;
        private volatile boolean stopped = false;
        private boolean playing = false;

        MusicPlayThread(PlayController controller, int scale, int totalTime, String filePath) {
            this.totalTime = totalTime;
            this.musicPath = filePath;
            this.mCurrentTime = 0;
            this.scale = scale;
            this.controller = controller;
            mediaPlayer = new MediaPlayer();
        }

        @Override
        public void run() {
            if (totalTime == 0 || TextUtils.isEmpty(musicPath)) return;
            mediaPlayer.reset();
            try {
                FileInputStream fis = new FileInputStream(musicPath);
                mediaPlayer.setDataSource(fis.getFD());
                mediaPlayer.prepare();
                mediaPlayer.setLooping(false);
            } catch (Exception e) {
                Timber.w("music play:"+e.getMessage());
                return;
            }
            while(!controller.isStopped() && (mCurrentTime <= totalTime) && !stopped) {
                if (controller.isPause()){
                    playing = false;
                    mediaPlayer.pause();
                }else if (controller.running()){
                    if (!playing) {
                        mediaPlayer.start();
                        playing = true;
                    }
                }
                try {
                    Thread.sleep(scale);
                    mCurrentTime += scale;
                } catch (InterruptedException e) {
                    Timber.w(e.getMessage());
                    break;
                }
            }
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        void stopPlay(){
            this.stopped = true;
        }
    }
}
