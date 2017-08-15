package com.ubtechinc.alpha.im.msghandler;

import com.google.protobuf.ByteString;
import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.CmSpeechEntity;
import com.ubtechinc.alpha.entity.TranslationBean;
import com.ubtechinc.alpha.utils.TranslationUtil;
import com.ubtechinc.nets.phonerobotcommunite.ProtoBufferDispose;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class SynSpeechMsgHandler implements IMsgHandler{
    @Override
    public void handleMsg(int requestCmdId, int responseCmdId, AlphaMessageOuterClass.AlphaMessage request, String peer) {
        ByteString requestBody = request.getBodyData();
        byte[] bodyBytes = requestBody.toByteArray();
        final long requestSerial = request.getHeader().getSendSerial();

        CmSpeechEntity.CmSpeechEntityRequest  speechEntityRequest =(CmSpeechEntity.CmSpeechEntityRequest) ProtoBufferDispose.unPackData(CmSpeechEntity.CmSpeechEntityRequest.class,bodyBytes);
        TranslationBean translationBean =new TranslationBean();
        translationBean.setContent(speechEntityRequest.getContent());
        translationBean.setActionId(speechEntityRequest.getActionId());
        translationBean.setLanguage(speechEntityRequest.getLanguage());
        translationBean.setNeedTranslate(speechEntityRequest.getNeedTranslate());

        TranslationUtil.performTranslate(translationBean);

    }
}
