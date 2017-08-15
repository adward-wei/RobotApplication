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

package com.ubtechinc.alpha.serial;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import com.ubtech.utilcode.utils.ByteBufferList;
import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtech.utilcode.utils.SystemProperty;
import com.ubtech.utilcode.utils.notification.NotificationCenter;
import com.ubtechinc.alpha.cmds.chest.ChestCmds;
import com.ubtechinc.alpha.event.SerialReadBackEvent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

import static com.ubtechinc.alpha.serial.SerialCommandExecutor.DEFAULT_COMMAND_SID;
import static com.ubtechinc.alpha.serial.SerialCommandExecutor.MAX_INDEX;

/**
 * @desc : alpha2串口文件命令操作类
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/18
 * @modifier:
 * @modify_time:
 */
 class Alpha2SerialPortOp implements ISerialPortOpProxy {
    private static final String TAG = "Alpha2SerialPortOp";
    public static final byte DEFAULT_READBACK_SID = 0;
    private static final byte DEFAULT_READBACK_INDEX = 0;
    private static final int INTERVAL_TIME = 10;
    private static final int TASK_WRITE = 1;
    private static final int TASK_READ = 2;

    @IntDef(value = {TASK_WRITE, TASK_READ})
    public @interface Type{}

    private HandlerThread mWorker;
    private SerialPortHandler mHandler;
    private Context mContext;
    private ISerialPortUtil util;
    private ByteBufferList mBBL;//串口数据缓存
    private String mSerialFilePath;
    private @SerialConstants.SerialType int serialType;

    private ArrayList<Semaphore> mSemaphores = new ArrayList<>(MAX_INDEX);
    private ArrayMap<Alpha2SerialProtocol, Semaphore> mRequstMaps = new ArrayMap<>(MAX_INDEX);
    private ArrayMap<Alpha2SerialProtocol, Alpha2SerialProtocol> mAnswerMaps = new ArrayMap<>(MAX_INDEX);



     Alpha2SerialPortOp(Context context, @SerialConstants.SerialType int serialType){
        this.mContext = context;
        this.serialType = serialType;
        String file;
        if (serialType == SerialConstants.TYPE_CHEST) {
            file = SystemProperty.getProperty(ISerialPortUtil.CHEST_SYS_PROPERTY_NAME);
            if (TextUtils.isEmpty(file)) {
                file = ISerialPortUtil.ALPHA2_CHEST_SERIAL_FILE;
            }
        }else {
           file = ISerialPortUtil.ALPHA2_HEADER_SERIAL_FILE;
        }
        this.mSerialFilePath = file;
    }

    private class SerialPortHandler extends Handler {

        public SerialPortHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            @Type int type = msg.what;
            switch (type){
                case TASK_WRITE:
                    handleWriteRequest((Alpha2SerialProtocol)msg.obj);
                    break;
                case TASK_READ:
                    handleReadRequest();
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void init() throws IOException {
        util = new Alpha2SerialPortUtil(mSerialFilePath, ISerialPortUtil.BANDRATE);
        util.open();
        mBBL = new ByteBufferList();
        mWorker = new HandlerThread("SerialPortWorker");
        mWorker.start();
        mHandler = new SerialPortHandler(mWorker.getLooper());
        sendReadMsg();
    }

    @Override
    public void clean() {
        if (mWorker != null) {
            mWorker.quitSafely();
            mWorker = null;
        }
        if (util != null){
            util.close();
            util = null;
        }
        if (mBBL != null){
            mBBL.recycle();
            mBBL = null;
        }
        synchronized (this) {
            mRequstMaps.clear();
            mAnswerMaps.clear();
            for (Semaphore semaphore : mSemaphores) {
                semaphore.release();
            }
            mSemaphores.clear();
        }
    }

    //在cmdThread中执行
    @Override
    public void sendCommand(byte sessionId, byte cmd, byte index, byte[] data, ISerialCommandCallback callback) {
        if (util == null) {
            Timber.e("串口设备未打开...");
            return;
        }

        Timber.d("sendCommand: cmd=%s,index=%d", ConvertUtils.byte2HexString(cmd), index);
        Alpha2SerialProtocol request = new Alpha2SerialProtocol(sessionId, index, cmd, data);
        if (cmd == 0x96){//排除没有应答的命令
            sendWriteMsg(request, 0);
            //透传命令不满足问答模式，默认成功, 只在2mic中存在
            callback.onRcvSerialPortData(sessionId, cmd, index, SerialConstants.ERR_OK, new byte[]{0x00});
            if (null == mHandler.obtainMessage(TASK_READ)) sendReadMsg();
            return;
        }

        Semaphore semaphore = new Semaphore(0);
        synchronized (this){
            mRequstMaps.put(request, semaphore);
            mSemaphores.add(semaphore);
        }

        sendWriteMsg(request, 0);//有必要停顿下，防止信号还未阻塞
        //命令线程等待串口线程将命令成功写入串口
        try {
            Timber.d("sendCommand: enter wait...");
            if (cmd == ChestCmds.CMD_SEND_DATA_TO_MOTOR && serialType == SerialConstants.TYPE_CHEST){
                semaphore.tryAcquire(1, 10, TimeUnit.MILLISECONDS);
            }else {
                semaphore.tryAcquire(1, 2, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            synchronized (this){
                mRequstMaps.remove(request);
                mSemaphores.remove(semaphore);
            }
            Timber.d("sendCommand: interrupted...");
            callback.onRcvSerialPortData(sessionId, cmd, (byte)(index & 0xff), SerialConstants.ERR_UNKNOW, null);
            return;
        }

        //命令线程醒来，检查是否撤销
        Alpha2SerialProtocol answer = null;
        boolean isUndo = false;
        synchronized (this){
            if (mRequstMaps.containsKey(request)){//未撤销
                //串口已有回答
                answer = mAnswerMaps.get(request);
                mRequstMaps.remove(request);
                mSemaphores.remove(semaphore);
                mAnswerMaps.remove(request);
            }else {//撤销了
                isUndo = true;
            }
        }
        Timber.d("receiver answer: cmd=%s,index=%d", ConvertUtils.byte2HexString(cmd), index);

        if (null == mHandler.obtainMessage(TASK_READ)) sendReadMsg();

        if (answer != null) {
            callback.onRcvSerialPortData(sessionId, cmd, (byte)(index & 0xff), SerialConstants.ERR_OK, answer.getParam());
        }else if (isUndo){
            callback.onRcvSerialPortData(sessionId, cmd, (byte)(index & 0xff), SerialConstants.ERR_UNDO, null);
        }else {
            if (cmd == ChestCmds.CMD_SEND_DATA_TO_MOTOR && serialType == SerialConstants.TYPE_CHEST){//硬件不返回结果
                callback.onRcvSerialPortData(sessionId, cmd, (byte)(index & 0xff), SerialConstants.ERR_OK, null);
            }else {
                callback.onRcvSerialPortData(sessionId, cmd, (byte) (index & 0xff), SerialConstants.ERR_IO, null);
            }
        }
    }

    //在辅助线程中执行
    @Override
    public boolean undoCommand(byte sid, byte cmd, byte index) {
        if (util == null)
            return true;
        boolean ret = false;
        synchronized (this) {
            Alpha2SerialProtocol target = findTargetPacket(sid, cmd,index);
            if (target != null) {
                Semaphore semaphore = mRequstMaps.get(target);
                mAnswerMaps.remove(target);
                mSemaphores.remove(semaphore);
                mRequstMaps.remove(target);
                semaphore.release();
                ret = true;
            }
        }
        return ret;
    }

    @Nullable
    private Alpha2SerialProtocol findTargetPacket(byte sid, byte cmd, byte index) {
        Iterator<Alpha2SerialProtocol> iterator = mRequstMaps.keySet().iterator();
        Alpha2SerialProtocol target = null;
        while (iterator.hasNext()){
            target = iterator.next();
            if (target.getId() == sid && target.getCmd() == cmd && target.getIndex() == index)
                break;
        }
        return target;
    }

    private void sendReadMsg() {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(TASK_READ), INTERVAL_TIME);
    }

    //串口线程
    private void handleReadRequest() {
        if (util == null)
            return;
        mHandler.removeMessages(TASK_READ);
        ByteBuffer buffer = ByteBufferList.obtain(ByteBufferList.MIN_ITEM_SIZE);
        int read;
        try {
            read = util.read(buffer);
            if (read > 0){
                mBBL.add(buffer);
                byte[] packetBytes = null;
                while ((packetBytes = Alpha2SerialProtocol.findValidPacket(mBBL)) != null) {
                    Timber.d("read valid packet: " + ConvertUtils.bytes2HexString(packetBytes));
                    try {
                        Alpha2SerialProtocol packet = new Alpha2SerialProtocol(packetBytes);
                        handlePacket(packet);
                    } catch (InvalidPacketException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                    }
                }
            }else {
                ByteBufferList.reclaim(buffer);
            }
            sendReadMsg();
        } catch (IOException e) {
            Log.e(TAG, "IOException:" + e.getMessage());
        }
    }

    //串口线程
    private void handleWriteRequest(Alpha2SerialProtocol request) {
        if (util != null){
            try {
                synchronized (this){
                    if (!mRequstMaps.containsKey(request)) return;
                }
                Timber.d("handle write request...");
                ByteBuffer buffer = request.toByteBuffer();
                util.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
                synchronized (this) {
                    if (mRequstMaps.containsKey(request)) {
                        mAnswerMaps.put(request,null);
                        final Semaphore semaphore = mRequstMaps.get(request);
                        if (semaphore != null)
                            semaphore.release();//通知发送线程
                    }
                }
            }finally {
                sendReadMsg();
            }
        }
    }

    //串口线程
    private void handlePacket(Alpha2SerialProtocol answer) {
        if (answer.getId() == DEFAULT_READBACK_SID && answer.getIndex() == DEFAULT_READBACK_INDEX ){
            //硬件主动发送过来的包
            sendWriteMsg(answer, 0);
            publishReadBackEvent(answer.getCmd(), answer, SerialConstants.ERR_OK);
        }else {
            //软件发出的包,都是小于0x80需应答确认
            //将数据转发给客户端
            final byte cmd = answer.getCmd();
            if (cmd < 0x80 && answer.getId() == DEFAULT_COMMAND_SID) {
                //命令码小于0x80需判断是否接受成功
                synchronized (this) {
                    Alpha2SerialProtocol request = findTargetPacket(answer.getId(), answer.getCmd(), answer.getIndex());
                    byte[] param = answer.getParam();
                    if (param == null){
                        mAnswerMaps.put(request, null);
                        final Semaphore semaphore = mRequstMaps.get(request);
                        if (semaphore != null)
                            semaphore.release();//通知发送线程
                        return;
                    }
                    if (param[0] == 0) {
                        //串口成功接受写入的数据
                        mAnswerMaps.put(request,answer);
                        final Semaphore semaphore = mRequstMaps.get(request);
                        if (semaphore != null)
                            semaphore.release();//通知发送线程
                    } else {
                        //串口写入数据失败，需要重新写入
                        Log.w(TAG, "request"+request.toString() + "\nanswer"+answer.toString());
                        if (request != null && request.tryRetry() && mWorker !=null && mWorker.isAlive()) {
                            sendWriteMsg(request, 1);//等待一点时间
                            return;
                        } else {
                            mAnswerMaps.put(request, null);
                            final Semaphore semaphore = mRequstMaps.get(request);
                            if (semaphore != null)
                                semaphore.release();//通知发送线程
                        }
                    }
                }
            }else {//其它情况，只能算串口不可靠,发送了异常的包,直接忽略掉
                Log.e(TAG, "串口不可靠："+ answer.toString());
            }
        }
    }

    private void publishReadBackEvent(byte cmd,
                                      Alpha2SerialProtocol readbackPacket,
                                      @SerialConstants.ErrorCode int code) {
        SerialReadBackEvent event = new SerialReadBackEvent(serialType);
        event.error = code;
        event.data = readbackPacket != null? readbackPacket.getParam(): null;
        event.cmd = cmd;
        NotificationCenter.defaultCenter().publish(event);
    }

    private void sendWriteMsg(Alpha2SerialProtocol packet, int delayTime) {
        Message msg = mHandler.obtainMessage(TASK_WRITE);
        msg.obj = packet;
        mHandler.sendMessageDelayed(msg, delayTime);
    }
}
