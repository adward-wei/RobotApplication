package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmBatchDeleteDeskClock;
import com.ubtechinc.alpha.DeskClockOuterClass;
import com.ubtechinc.alpha.provider.AlarmInfoVisitor;
import com.ubtechinc.nets.im.service.RobotPhoneCommuniteProxy;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

import java.util.List;

/**
 * Created by Administrator on 2017/6/6 0006.
 */

public class BatchDeletDeskClockMsgHandler implements  IMsgHandler{
    private static final String TAG ="BatchDeletDeskClockMsgHandler";

    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {
        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        final long requestSerial = request.getHeader().getSendSerial();
        LogUtils.i(TAG,"enter DeleteDeskClockMsgHandler , reqeustSerial:"+requestSerial);
        CmBatchDeleteDeskClock.CmBatchDeleteDeskRequest deskClockListRequest = (CmBatchDeleteDeskClock.CmBatchDeleteDeskRequest) ProtoBufferDispose.unPackData(CmBatchDeleteDeskClock.CmBatchDeleteDeskRequest.class,bodyBytes);
        List<DeskClockOuterClass.DeskClock> deskClockRequests= deskClockListRequest.getClockListList();



        if(deskClockRequests != null) {
            LogUtils.i(TAG,"deskClockRequests.size="+deskClockRequests.size());
            for (DeskClockOuterClass.DeskClock deskClock : deskClockRequests) {
                LogUtils.i(TAG,"clock id:"+deskClock.getClockID());
                AlarmInfoVisitor.get().deleteById(deskClock.getClockID());
            }
            CmBatchDeleteDeskClock.CmBatchDeleteDeskResponse.Builder builder = CmBatchDeleteDeskClock.CmBatchDeleteDeskResponse.newBuilder();
            builder.setIsSuccess(true);
            RobotPhoneCommuniteProxy.getInstance().sendResponseMessage(responseCmdId, "1", requestSerial, builder.build(), peer, null);
        }else{
            LogUtils.i(TAG,"deskClockRequests is null!!!");
        }
    }
}
