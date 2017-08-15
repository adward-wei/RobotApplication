package com.ubtechinc.alpha2ctrlapp.data.robot;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.CmGetChargeAndPlayData;
import com.ubtechinc.alpha.CmGetTTSActionActiveData;
import com.ubtechinc.alpha.CmSetChargeAndPlayData;
import com.ubtechinc.alpha.CmSetErrorLogData;
import com.ubtechinc.alpha.CmSetTTSActionActiveData;
import com.ubtechinc.alpha.im.IMCmdId;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.data.ErrorParser;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.GetLogByMobileEntrity;
import com.ubtechinc.alpha2ctrlapp.entity.net.UpdateRobotInfoModule;
import com.ubtechinc.alpha2ctrlapp.im.Phone2RobotMsgMgr;
import com.ubtechinc.alpha2ctrlapp.util.BeanUtils;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.HttpProxy;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.im.IMErrorUtil;
import com.ubtechinc.nets.phonerobotcommunite.ICallback;

/**
 * @author：tanghongyu
 * @date：6/16/2017 4:55 PM
 * @modifier：tanghongyu
 * @modify_date：6/16/2017 4:55 PM
 * [A brief description]
 * version
 */

public class RobotConfigRepository {

    private static RobotConfigRepository INSTANCE = null;
    private static final String TAG = "RobotConfigRepository";

    // Prevent direct instantiation.
    private RobotConfigRepository() {

    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link RobotActionRepository} instance
     */
    public static RobotConfigRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RobotConfigRepository();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * 边冲边玩操作
     *
     * @param isOpen true 表示打开边冲边玩，false 表示关闭边冲边玩
     */
    public void setChargeWhilePlaying(final boolean isOpen, @NonNull final IRobotConfigDataSource.SetChargeWhilePlayingCallback callback) {

        CmSetChargeAndPlayData.CmSetChargeAndPlayDataRequest.Builder builder = CmSetChargeAndPlayData.CmSetChargeAndPlayDataRequest.newBuilder();
        builder.setIsOpen(isOpen);

        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_SET_CHARGE_AND_PLAY_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmSetChargeAndPlayData.CmSetChargeAndPlayDataResponse>() {
            @Override
            public void onSuccess(final CmSetChargeAndPlayData.CmSetChargeAndPlayDataResponse data) {
                Logger.t(TAG).d("setChargeWhilePlaying isOpen = %s, result = %b", isOpen, data.getIsSuccess());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (data.getIsSuccess()) {
                            callback.onSuccess();
                        } else {
                            callback.onFail(IMErrorUtil.handleException(data.getResultCode()));
                        }

                    }
                });

            }

            @Override
            public void onError(final ThrowableWrapper e) {
                Logger.t(TAG).w("setChargeWhilePlaying onError %s ", e.getMessage());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });

            }
        });
    }

    public void getChargeWhilePlayingState(@NonNull final IRobotConfigDataSource.GetChargeWhilePlayingStateCallback callback) {

        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_GET_CHARGE_AND_PLAY_REQUEST, "1", null, Alpha2Application.getRobotSerialNo(), new ICallback<CmGetChargeAndPlayData.CmGetChargeAndPlayDataResponse>() {
            @Override
            public void onSuccess(final CmGetChargeAndPlayData.CmGetChargeAndPlayDataResponse data) {

                Logger.t(TAG).d("getChargeWhilePlayingState state = %b", data.getIsOpen());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {

                        callback.onLoadChargeWhilePlayingState(data.getIsOpen());

                    }
                });

            }

            @Override
            public void onError(final ThrowableWrapper e) {
                Logger.t(TAG).w("getChargeWhilePlayingState onError %s ", e.getMessage());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {

                        callback.onDataNotAvailable(e);

                    }
                });

            }
        });
    }

    public void setTTSWhileActive(final boolean isOpen, @NonNull final IRobotConfigDataSource.SetTTSActionStateCallback callback) {

        CmSetTTSActionActiveData.CmSetTTSActionActiveDataRequest.Builder builder = CmSetTTSActionActiveData.CmSetTTSActionActiveDataRequest.newBuilder();
        builder.setIsOpen(isOpen);
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_TTS_BREATH_ACTION_ON_OFF_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmSetTTSActionActiveData.CmSetTTSActionActiveDataResponse>() {
            @Override
            public void onSuccess(final CmSetTTSActionActiveData.CmSetTTSActionActiveDataResponse data) {

                Logger.t(TAG).d("setTTSWhileActive isOpen = %s, result = %b", isOpen, data.getIsSuccess());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {

                        if (data.getIsSuccess()) {
                            callback.onSuccess();
                        } else {
                            callback.onFail(IMErrorUtil.handleException(data.getResultCode()));
                        }

                    }
                });

            }

            @Override
            public void onError(final ThrowableWrapper e) {
                Logger.t(TAG).w("setTTSWhileActive onError %s ", e.getMessage());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {

                        callback.onFail(e);

                    }
                });

            }
        });
    }

    public void getTTSWhileActiveState(@NonNull final IRobotConfigDataSource.GetTTSActionStateCallback callback) {


        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_GET_TTS_BREATH_ACTION_ON_OFF_REQUEST, "1", null, Alpha2Application.getRobotSerialNo(), new ICallback<CmGetTTSActionActiveData.CmGetTTSActionActiveDataResponse>() {
            @Override
            public void onSuccess(final CmGetTTSActionActiveData.CmGetTTSActionActiveDataResponse data) {
                Logger.t(TAG).d("getTTSWhileActiveState state = %b", data.getIsOpen());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onLoadTTSActionState(data.getIsOpen());

                    }
                });


            }

            @Override
            public void onError(final ThrowableWrapper e) {
                Logger.t(TAG).w("getTTSWhileActiveState onError %s ", e.getMessage());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {

                        callback.onDataNotAvailable(e);

                    }
                });

            }
        });
    }

    public void startUploadLog(@NonNull final IRobotConfigDataSource.StartUploadLogCallback callback) {

        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_CLOSE_ROBOT_ERROR_LOG_REQUEST, "1", null, Alpha2Application.getRobotSerialNo(), new ICallback<CmSetErrorLogData.CmSetErrorLogDataResponse>() {
            @Override
            public void onSuccess(final CmSetErrorLogData.CmSetErrorLogDataResponse data) {


                final GetLogByMobileEntrity getLogByMobileEntrity1 = new GetLogByMobileEntrity();
                BeanUtils.copyBeanFromProto(data, getLogByMobileEntrity1);
                Logger.t(TAG).d("startUploadLog state = %b", data.getIsSuccess());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (data.getIsSuccess()) {
                            callback.onSuccess(getLogByMobileEntrity1);
                        } else {
                            callback.onFail(IMErrorUtil.handleException(data.getResultCode()));
                        }

                    }
                });

            }

            @Override
            public void onError(final ThrowableWrapper e) {
                Logger.t(TAG).w("startUploadLog onError %s ", e.getMessage());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {

                        callback.onFail(e);
                    }
                });


            }
        });
    }

    public void updateInfo(final String userId, final String userOtherName, final String userImage, @NonNull final IRobotConfigDataSource.UpdateInfoCallback callback) {
        UpdateRobotInfoModule.Request request = new UpdateRobotInfoModule().new Request();
        request.setUserName(userId);
        request.setUserOtherName(userOtherName);
        request.setUserImage(userImage);
        HttpProxy.get().doPost(request, new ResponseListener<UpdateRobotInfoModule.Response>() {
            @Override
            public void onError(ThrowableWrapper e) {
                callback.onFail(e);
            }

            @Override
            public void onSuccess(UpdateRobotInfoModule.Response response) {

                if (ErrorParser.get().isSuccess(response.getResultCode())) {
                    callback.onSuccess(userId, userOtherName, userImage);
                } else {
                    callback.onFail(ErrorParser.get().getThrowableWrapper());
                }
            }
        });

    }
}
