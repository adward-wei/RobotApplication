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

package com.ubtechinc.alpha.ops;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtech.utilcode.utils.FileUtils;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtech.utilcode.utils.notification.Subscriber;
import com.ubtechinc.alpha.behavior.RobotStandup;
import com.ubtechinc.alpha.behavior.RobotTakeARest;
import com.ubtechinc.alpha.event.SerialStateEvent;
import com.ubtechinc.alpha.ops.action.Action;
import com.ubtechinc.alpha.ops.action.ActionPlayListener;
import com.ubtechinc.alpha.ops.action.play.CompletedState;
import com.ubtechinc.alpha.ops.action.play.ILayerResult;
import com.ubtechinc.alpha.ops.action.play.IPlayCompletedCallback;
import com.ubtechinc.alpha.ops.action.play.UbxFlowPlay;
import com.ubtechinc.alpha.ops.led.SetEyeBlinkOp;
import com.ubtechinc.alpha.provider.ActionInfoVisitor;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;
import com.ubtechinc.alpha.serverlibutil.aidl.ActionInfo;
import com.ubtechinc.alpha.serverlibutil.interfaces.ActionListResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.StopActonResultListener;
import com.ubtechinc.alpha.sdk.SdkConstants;
import com.ubtechinc.alpha.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

/**
 * @desc : 机器人操作控制
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/17
 * @modifier:
 * @modify_time:
 */

public final class RobotOpsManager {
    private static final String TAG = RobotOpsManager.class.getSimpleName();
    private static RobotOpsManager mOpsManager;
    private final Context mContext;
    private volatile SerialCommandReceiver mCmdReceiver;
    private ExecutorService sysOpThread;//系统设置操作线程
    private ExecutorService motorOpThread;//舵机操作线程
    private ExecutorService ledOpThread;//灯控操作线程
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    private volatile UbxFlowPlay mPlay;
    private volatile AtomicBoolean dancing = new AtomicBoolean(false);

    private RobotOpsManager(Context context){
        this.mContext = context.getApplicationContext();
    }

    public static RobotOpsManager get(Context context){
        if (mOpsManager == null){
            synchronized (RobotOpsManager.class){
                if (mOpsManager == null)
                    mOpsManager = new RobotOpsManager(context);
            }
        }
        return mOpsManager;
    }

    public synchronized void init(){
        if (mCmdReceiver != null) return;
        NotificationCenter.defaultCenter().subscriber(SerialStateEvent.class, mSerialStateSubscriber);
        mCmdReceiver = new SerialCommandReceiver(mContext);
        sysOpThread = Executors.newFixedThreadPool(2, new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "sysOpThread");
            }
        });
        motorOpThread = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r,"motorOpThread");
            }
        });
        ledOpThread = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "ledOpThread");
            }
        });
    }

    public synchronized boolean isInit(){
        return mCmdReceiver != null;
    }

    public synchronized void destroy(){
        if (mCmdReceiver == null) return;
        NotificationCenter.defaultCenter().unsubscribe(SerialStateEvent.class, mSerialStateSubscriber);
        sysOpThread.shutdownNow();
        motorOpThread.shutdownNow();
        ledOpThread.shutdownNow();
        mCmdReceiver = null;
    }

    private Subscriber<SerialStateEvent> mSerialStateSubscriber = new Subscriber<SerialStateEvent>() {
        @Override
        public void onEvent(SerialStateEvent event) {
            LogUtils.i( "serial state =" + event.state);
            if (event.state == SerialConstants.SHUTDOWN){
                mCmdReceiver = null;
            }
        }
    };

    //一个串口操作
    public void executeOp(final ISerialCmdOp op, final SerialOpResultListener listener){
        if (mCmdReceiver == null) return;
        if (op.getOpType() == ISerialCmdOp.TYPE_MOTOR){
            motorOpThread.execute(new Runnable() {
                @Override
                public void run() {
                    if (mCmdReceiver == null) return;
                    op.prepare();
                    final OpResult re = op.start(mCmdReceiver);
                    if (listener != null)
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onRecvOpResult(re);
                            }
                        });
                }
            });
            return;
        }

        if (op.getOpType() == ISerialCmdOp.TYPE_LED){
            ledOpThread.execute(new Runnable() {
                @Override
                public void run() {
                    if (mCmdReceiver == null) return;
                    op.prepare();
                    final OpResult re = op.start(mCmdReceiver);
                    if (listener != null)
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onRecvOpResult(re);
                            }
                        });
                }
            });
            return;
        }

        if (op.getOpType() == ISerialCmdOp.TYPE_SYS_SET) {
            sysOpThread.execute(new Runnable() {
                @Override
                public void run() {
                    if (mCmdReceiver == null) return;
                    op.prepare();
                    final OpResult re = op.start(mCmdReceiver);
                    if (listener != null)
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onRecvOpResult(re);
                            }
                        });
                }
            });
            return;
        }
    }

    public void executeOp(final ISerialCmdOp op){
        if (mCmdReceiver == null) return;
        executeOp(op, new SerialOpResultListener() {
            @Override
            public void onRecvOpResult(OpResult result) {
                if (result != null) {
                    Timber.d("opTpe=%d,cmd=%s,errorCode=%d", op.getOpType(), ConvertUtils.byte2HexString(result.cmd), result.errorCode);
                }else {
                    Timber.w("op=%d interrupted..",op.getOpType());
                }
            }
        });
    }

    @Nullable
    public OpResult executeOpSync(final ISerialCmdOp op){
        if (mCmdReceiver == null) return null;
        Callable<OpResult> callable = new Callable() {
            @Override
            public OpResult call() throws Exception {
                if (mCmdReceiver == null) return null;
                op.prepare();
                OpResult result = op.start(mCmdReceiver);
                return result;
            }
        };
        FutureTask<OpResult> task = new FutureTask<OpResult>(callable);
        executeOpInner(op.getOpType(), task);
        try {
            return task.get();
        } catch (InterruptedException e) {
            Timber.w(e.getMessage());
        } catch (ExecutionException e) {
            Timber.w(e.getMessage());
        }
        return null;
    }

    private void executeOpInner(int opType, FutureTask<OpResult> task) {
        if (opType == ISerialCmdOp.TYPE_LED){
            ledOpThread.execute(task);
        }else if (opType == ISerialCmdOp.TYPE_MOTOR){
            motorOpThread.execute(task);
        }else {
            sysOpThread.execute(task);
        }
    }

    //播放动作文件
    public void playAction(final String actionName, final ActionPlayListener listener){
        if (mCmdReceiver == null) return;
        if (dancing.get()){
            if (listener != null)
                listener.onActionResult(SdkConstants.ErrorCode.RESULT_FAIL);
            return;
        }
        dancing.set(true);
        ActionInfoVisitor provider = ActionInfoVisitor.get();
        String actionId = provider.getActionId(actionName);
        if (TextUtils.isEmpty(actionId)){
            actionId = actionName;
        }
        final String actionFile = Constants.ACTION_PATH + File.separator + actionId + ".ubx";

        if (!FileUtils.isFileExists(actionFile)){
            dancing.set(false);
            LogUtils.w(TAG, "动作文件不存在："+ actionFile);
            if (listener != null)
                listener.onActionResult(SdkConstants.ErrorCode.RESULT_FAIL);
            return;
        }
        playActionFileInner(listener, actionFile);
    }

    //播放动作文件
    public void playActionFile(final String actionFile, final ActionPlayListener listener){
        if (mCmdReceiver == null) return;
        if (dancing.get()){
            if (listener != null)
                listener.onActionResult(SdkConstants.ErrorCode.RESULT_FAIL);
            return;
        }
        dancing.set(true);
        if (!new File(actionFile).exists()){
            dancing.set(false);
            if (listener != null)
                listener.onActionResult(SdkConstants.ErrorCode.RESULT_FAIL);
            return;
        }
        playActionFileInner(listener, actionFile);
    }

    private void playActionFileInner(final ActionPlayListener listener, final String actionFile) {
        sysOpThread.execute(new Runnable() {
            @Override
            public void run() {

                mPlay = new UbxFlowPlay(mContext, actionFile, new IPlayCompletedCallback() {

                    @Override
                    public void onPlayCompleted(CompletedState state) {
                        dancing.set(false);
                        executeOp(new SetEyeBlinkOp(true));
                        if (listener != null)
                            listener.onActionResult(state == CompletedState.FINISHED?
                                    SdkConstants.ErrorCode.RESULT_SUCCESS : SdkConstants.ErrorCode.ACTION_INTERRUPTED);
                    }
                });
                if (!mPlay.prepare()){
                    dancing.set(false);
                    if (listener != null)
                        listener.onActionResult(SdkConstants.ErrorCode.RESULT_FAIL);
                    return;
                }
                LogUtils.I("play action = %s", actionFile);
                if (RobotState.get().isInPowerSave()){
                    RobotTakeARest.instance().start(false);
                }
                mPlay.play(null, ILayerResult.TYPE_INT);
            }
        });
    }

    public synchronized void stopAction(StopActonResultListener listener){
        if (mPlay != null) {
            mPlay.stop();
        }
        mPlay = null;
        if (dancing.getAndSet(false)) {
            new RobotStandup(mContext).start();
        }
        if (listener != null)
            listener.onStopActionResult(SdkConstants.ErrorCode.RESULT_SUCCESS);
    }

    public void getActionList(final int id, final ActionListResultListener listener){
        sysOpThread.execute(new Runnable() {
            @Override
            public void run() {
                ActionInfoVisitor provider = ActionInfoVisitor.get();
                List<Action> actions = provider.getAllData();
                final List<ActionInfo> infos = new ArrayList<ActionInfo>(actions.size());
                for (Action action: actions){
                    ActionInfo info = new ActionInfo(action.Id, action.cn_name, action.en_name, action.desc, action.time, action.type);
                    infos.add(info);
                }
                if (listener != null) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onGetActionList(id, SdkConstants.ErrorCode.RESULT_SUCCESS, infos);
                        }
                    });
                }
            }
        });
    }

    public boolean isPlayAction() {
        return dancing.get();
    }
}
