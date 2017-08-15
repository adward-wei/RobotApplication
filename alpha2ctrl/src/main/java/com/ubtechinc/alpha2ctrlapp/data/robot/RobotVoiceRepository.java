package com.ubtechinc.alpha2ctrlapp.data.robot;

import android.support.annotation.NonNull;

import com.ubtechinc.alpha.CmSpeechEntity;
import com.ubtechinc.alpha.ReplaySpeechRecord;
import com.ubtechinc.alpha.im.IMCmdId;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.data.ErrorParser;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ReplaySpeechRcord;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.TranslationBean;
import com.ubtechinc.alpha2ctrlapp.entity.net.GetSpeechRecordModule;
import com.ubtechinc.alpha2ctrlapp.entity.request.GetTranlateRequest;
import com.ubtechinc.alpha2ctrlapp.im.Phone2RobotMsgMgr;
import com.ubtechinc.alpha2ctrlapp.util.BeanUtils;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.im.IMErrorUtil;
import com.ubtechinc.nets.phonerobotcommunite.ICallback;

/**
 * @author：tanghongyu
 * @date：6/17/2017 2:35 PM
 * @modifier：tanghongyu
 * @modify_date：6/17/2017 2:35 PM
 * [A brief description]
 * version
 */

public class RobotVoiceRepository {

    private static RobotVoiceRepository INSTANCE = null;


    // Prevent direct instantiation.
    private RobotVoiceRepository() {

    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link RobotActionRepository} instance
     */
    public static RobotVoiceRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RobotVoiceRepository();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public void startVoiceCompound(TranslationBean translationBean, final @NonNull IRobotVoiceDataSource.StartVoiceCompoundCallback callback) {

        CmSpeechEntity.CmSpeechEntityRequest.Builder builder = CmSpeechEntity.CmSpeechEntityRequest.newBuilder();
        BeanUtils.copyBeanToProto(translationBean, builder);
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_SYN_SPEECH_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmSpeechEntity.CmSpeechEntityResponse>() {
            @Override
            public void onSuccess(CmSpeechEntity.CmSpeechEntityResponse data) {
                if(data.getIsSuccess() ) {
                    callback.onSuccess();
                }else {
                    callback.onFail(IMErrorUtil.handleException(data.getReturnCode()));
                }
            }

            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }
        });
    }

    public void replayTTSContent(ReplaySpeechRcord replaySpeechRcord, @NonNull final IRobotVoiceDataSource.ReplayTTSContentCallback callback ) {

        ReplaySpeechRecord.ReplaySpeechRecordRequest.Builder builder = ReplaySpeechRecord.ReplaySpeechRecordRequest.newBuilder();
        BeanUtils.copyBeanToProto(replaySpeechRcord, builder);
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_RETRY_PLAY_ANSWER_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<ReplaySpeechRecord.ReplaySpeechRecordResponse>() {
            @Override
            public void onSuccess(ReplaySpeechRecord.ReplaySpeechRecordResponse data) {
                if(data.getIsSuccess() ) {
                    callback.onSuccess();
                }else {
                    callback.onFail(IMErrorUtil.handleException(data.getReturnCode()));
                }
            }

            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }
        });

    }

    public void getSpeechRecord(GetTranlateRequest data, @NonNull final IRobotVoiceDataSource.GetSpeechRecordCallback callback) {
        GetSpeechRecordModule.Request request = new GetSpeechRecordModule().new Request();
        BeanUtils.copyBean(data, request);
        HttpProxy.get().doGet(request, new ResponseListener<GetSpeechRecordModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onSuccess(GetSpeechRecordModule.Response response) {

                if(ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onLoadSpeechRecords(response.getData().getResult().getRecords());
                }else {
                    callback.onDataNotAvailable(ErrorParser.get().getThrowableWrapper());
                }
            }
        });

    }
}
