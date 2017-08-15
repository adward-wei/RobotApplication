package com.ubtechinc.alpha2ctrlapp.data.robot;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.AlDeleteActionFile;
import com.ubtechinc.alpha.AlPlayActionCommand;
import com.ubtechinc.alpha.AlStopPlayAction;
import com.ubtechinc.alpha.CmDownloadActionFile;
import com.ubtechinc.alpha.CmGetActionList;
import com.ubtechinc.alpha.CmNewActionInfo;
import com.ubtechinc.alpha.CmReadMotorAngle;
import com.ubtechinc.alpha.im.IMCmdId;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionFileEntrity;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewActionInfo;
import com.ubtechinc.alpha2ctrlapp.im.Phone2RobotMsgMgr;
import com.ubtechinc.alpha2ctrlapp.util.BeanUtils;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.im.IMErrorUtil;
import com.ubtechinc.nets.phonerobotcommunite.ICallback;

import java.util.List;


/**
 * @ClassName RobotActionRepository
 * @date 6/14/2017
 * @author tanghongyu
 * @Description 机器人动作数据仓库
 * @modifier
 * @modify_time
 */
public class RobotActionRepository {

    private static RobotActionRepository INSTANCE = null;
    private static final String TAG = "RobotActionRepository";

    // Prevent direct instantiation.
    private RobotActionRepository() {

    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link RobotActionRepository} instance
     */
    public static RobotActionRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RobotActionRepository();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    public void deleteActionById(final String actionId, final @NonNull IRobotActionDataSource.ControlActionCallback callback) {
        AlDeleteActionFile.AlDeleteActionFileRequest.Builder builder =  AlDeleteActionFile.AlDeleteActionFileRequest.newBuilder();
        builder.setFileName(actionId);
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_DELETE_ACTIONFILE_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<AlDeleteActionFile.AlDeleteActionFileResponse>() {
            @Override
            public void onSuccess(final AlDeleteActionFile.AlDeleteActionFileResponse data) {
                Logger.t(TAG).d( "deleteActionById actionId = %s, result = %s", actionId, data.getIsSuccess());
                if(data.getIsSuccess()) {

                    HandlerUtils.runUITask(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess();
                        }
                    });

                }else {

                    HandlerUtils.runUITask(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail(IMErrorUtil.handleException(data.getResultCode()));
                        }
                    });

                }
            }

            @Override
            public void onError(final ThrowableWrapper e) {

                Logger.t(TAG).w("deleteActionById onError ", e.getMessage());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });
            }
        });

    }


    public void playAction(final String actionName, final @NonNull IRobotActionDataSource.ControlActionCallback callback) {
        AlPlayActionCommand.AlPlayActionCommandRequest.Builder builder = AlPlayActionCommand.AlPlayActionCommandRequest.newBuilder();
        builder.setActionName(actionName);
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_PLAY_ACTION_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback< AlPlayActionCommand.AlPlayActionCommandResponse>() {
            @Override
            public void onSuccess( final AlPlayActionCommand.AlPlayActionCommandResponse data) {


                Logger.t(TAG).d("playAction actionName = %s, result = %s", actionName, data.getIsSuccess());
                if(data.getIsSuccess()) {
                    HandlerUtils.runUITask(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess();
                        }
                    });
                }else {
                    HandlerUtils.runUITask(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail(IMErrorUtil.handleException(data.getResultCode()));
                        }
                    });
                }
            }

            @Override
            public void onError(final ThrowableWrapper e) {

                Logger.t(TAG).w( "playAction onError ", e.getMessage());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });
            }
        });

    }

    public void stopPlayAction(final @NonNull IRobotActionDataSource.ControlActionCallback callback) {
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_STOP_PLAY_REQUEST, "1", null, Alpha2Application.getRobotSerialNo(), new ICallback<AlStopPlayAction.AlStopPlayActionResponse>() {
            @Override
            public void onSuccess(final AlStopPlayAction.AlStopPlayActionResponse data) {

                Logger.t(TAG).d( "stopPlayAction, result = %s", data.getIsSuccess());
                if(data.getIsSuccess()) {

                    HandlerUtils.runUITask(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess();
                        }
                    });

                }else {
                    HandlerUtils.runUITask(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail(IMErrorUtil.handleException(data.getResultCode()));
                        }
                    });

                }
            }

            @Override
            public void onError(final ThrowableWrapper e) {
                Logger.t(TAG).w( "stopPlayAction onError ", e.getMessage());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });
            }
        });
    }

    public void getActionList(String languageType, int actionType,final  @NonNull IRobotActionDataSource.GetActionListCallback callback) {
        CmGetActionList.CmGetActionListRequest.Builder builder =  CmGetActionList.CmGetActionListRequest.newBuilder();
        builder.setLanguageType(languageType);
        builder.setActionType(actionType);
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_GET_NEW_ACTION_LIST_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmGetActionList.CmGetActionListResponse>() {

            @Override
            public void onSuccess(final CmGetActionList.CmGetActionListResponse data) {
                final List<NewActionInfo> actionInfoList = Lists.newArrayList();
                for ( CmNewActionInfo.CmNewActionInfoResponse response : data.getActionListList()) {
                    NewActionInfo actionInfo = new NewActionInfo();
                    BeanUtils.copyBeanFromProto(response, actionInfo);
                    actionInfo.setDownloadState(BusinessConstants.APP_STATE_INSTALL_SUCCESS);
                    actionInfo.setRobotSerialNo(Alpha2Application.getRobotSerialNo());
                    actionInfoList.add(actionInfo);

                }
                Logger.t(TAG).d( "getAction = ", actionInfoList.size());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onLoadActionList(actionInfoList);
                    }
                });

            }

            @Override
            public void onError(final ThrowableWrapper e) {
                Logger.t(TAG).w( "getActionList onError ", e.getMessage());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataNotAvailable(e);
                    }
                });

            }
        } );
    }

    public void downloadAction(final ActionFileEntrity actionFileEntrity, final  @NonNull IRobotActionDataSource.ControlActionCallback callback) {

        CmDownloadActionFile.CmDownloadActionFileRequest.Builder builder = CmDownloadActionFile.CmDownloadActionFileRequest.newBuilder();
        BeanUtils.copyBeanToProto(actionFileEntrity, builder);
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_DOWNLOAD_ACTIONFILE_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmDownloadActionFile.CmDownloadActionFileResponse>() {

            @Override
            public void onSuccess(final CmDownloadActionFile.CmDownloadActionFileResponse data) {
                Logger.t(TAG).d( "downloadAction action = %s  result = %s ", actionFileEntrity.getActionName(), data.getIsSuccess());
                if (data.getIsSuccess()) {

                    HandlerUtils.runUITask(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess();
                        }
                    });

                }else {

                    HandlerUtils.runUITask(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail(IMErrorUtil.handleException(data.getResultCode()));
                        }
                    });

                }
            }

            @Override
            public void onError(final ThrowableWrapper e)

            {
                Logger.t(TAG).w("getActionList onError ", e.getMessage());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });

            }
        });
    }


    public void readMotorAngleAction(int MotorId,final @NonNull IRobotActionDataSource.ControlActionCallback callback) {
        CmReadMotorAngle.CmReadMotorAngleRequest.Builder builder = CmReadMotorAngle.CmReadMotorAngleRequest.newBuilder();
        builder.setMotorID(MotorId);
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_GET_MOTORANGLE_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmReadMotorAngle.CmReadMotorAngleResponse>() {
            @Override
            public void onSuccess(final CmReadMotorAngle.CmReadMotorAngleResponse data) {
                Logger.t(TAG).d( "readMotorAngleAction, result = %s", data.getAngle());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess();
                    }
                });
            }

            @Override
            public void onError(final ThrowableWrapper e) {
                Logger.t(TAG).w( "stopPlayAction onError ", e.getMessage());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });
            }
        });
    }

}
