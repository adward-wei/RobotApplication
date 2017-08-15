package com.ubtechinc.alpha2ctrlapp.data.robot;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.CmBatchDeleteDeskClock;
import com.ubtechinc.alpha.CmGetDeskClockList;
import com.ubtechinc.alpha.CmManageDeskClock;
import com.ubtechinc.alpha.DeskClockOuterClass;
import com.ubtechinc.alpha.im.IMCmdId;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.DeskClock;
import com.ubtechinc.alpha2ctrlapp.im.Phone2RobotMsgMgr;
import com.ubtechinc.alpha2ctrlapp.util.BeanUtils;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.im.IMErrorUtil;
import com.ubtechinc.nets.phonerobotcommunite.ICallback;

import java.util.List;

/**
 * @ClassName RobotAlarmReponsitory
 * @date 6/16/2017
 * @author tanghongyu
 * @Description 机器人闹钟数据类
 * @modifier
 * @modify_time
 */
public class RobotAlarmReponsitory {

    private static RobotAlarmReponsitory INSTANCE = null;
    private static final String TAG = "RobotAlarmReponsitory";

    // Prevent direct instantiation.
    private RobotAlarmReponsitory() {

    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link RobotActionRepository} instance
     */
    public static RobotAlarmReponsitory getInstance() {
        Logger.t(TAG);
        if (INSTANCE == null) {
            INSTANCE = new RobotAlarmReponsitory();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    public void getValidAlarmList(@NonNull final IRobotAlarmDataSource.GetValidAlarmListCallback callback) {

        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_DESKCLOCK_ACTIVIE_LIST_REQUEST, "1", null, Alpha2Application.getRobotSerialNo(), new ICallback< CmGetDeskClockList.CmGetDeskClockListResponse>() {

            @Override
            public void onSuccess( final CmGetDeskClockList.CmGetDeskClockListResponse data) {
               final List<DeskClock> alarmList = Lists.newArrayList();
                List<DeskClockOuterClass.DeskClock> robotAlarmList = data.getClockListList();
                for(DeskClockOuterClass.DeskClock alarm : robotAlarmList) {
                    DeskClock clock = new DeskClock();
                    BeanUtils.copyBeanFromProto(alarm, clock);
                    alarmList.add(clock);
                    Logger.d( "getValidAlarmList = %s" , clock.toString());
                }
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onLoadValidAlarmList(alarmList);
                    }
                });

            }

            @Override
            public void onError(final ThrowableWrapper e)

            {

                Logger.w( "getAllAlarmList onError = %s" , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                @Override
                public void run() {
                    callback.onDataNotAvailable(e);
                }
            });

            }
        });

    }

    public void getAllAlarmList(@NonNull final IRobotAlarmDataSource.GetAllAlarmListCallback callback) {

        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_GET_FORMER_CLOCK_REQUEST, "1", null, Alpha2Application.getRobotSerialNo(), new ICallback<CmGetDeskClockList.CmGetDeskClockListResponse>() {

            @Override
            public void onSuccess(final CmGetDeskClockList.CmGetDeskClockListResponse data) {
                final List<DeskClock> alarmList = Lists.newArrayList();
                List<DeskClockOuterClass.DeskClock> robotAlarmList = data.getClockListList();
                for(DeskClockOuterClass.DeskClock alarm : robotAlarmList) {
                    DeskClock clock = new DeskClock();
                    BeanUtils.copyBeanFromProto(alarm, clock);
                    alarmList.add(clock);
                    Logger.d( "getValidAlarmList = %s" , clock.toString());
                }
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onLoadAlarmList(alarmList);
                    }
                });

            }

            @Override
            public void onError(final ThrowableWrapper e) {
                Logger.w( "getAllAlarmList onError = %s" , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataNotAvailable(e);
                    }
                });

            }
        });

    }

    public void batchDeleteAlarm(List<DeskClock> deskClocks, @NonNull final IRobotAlarmDataSource.ControlAlarmCallback callback) {

        CmBatchDeleteDeskClock.CmBatchDeleteDeskRequest.Builder reuest =  CmBatchDeleteDeskClock.CmBatchDeleteDeskRequest.newBuilder();
        List<DeskClockOuterClass.DeskClock> deskClockList = Lists.newArrayList();
        for(DeskClock deskClock : deskClocks) {
            DeskClockOuterClass.DeskClock.Builder builder = DeskClockOuterClass.DeskClock.newBuilder();
            BeanUtils.copyBeanToProto(deskClock, builder);
            deskClockList.add(builder.build());
            Logger.d( "batchDeleteAlarm = %s" , deskClock.toString());
        }
        reuest.addAllClockList(deskClockList);
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_DELETE_FORMER_CLOCK_REQUEST, "1", reuest.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmBatchDeleteDeskClock.CmBatchDeleteDeskResponse>() {

            @Override
            public void onSuccess(final CmBatchDeleteDeskClock.CmBatchDeleteDeskResponse data) {
                Logger.d( "batchDeleteAlarm result = %b" , data.getIsSuccess());
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

                Logger.w( "batchDeleteAlarm onError = %s" , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });
            }
        });
    }

    public void deleteAlarm(DeskClock clock, @NonNull final IRobotAlarmDataSource.ControlAlarmCallback callback) {
        CmManageDeskClock.CmManageDeskClockRequest.Builder builder =  CmManageDeskClock.CmManageDeskClockRequest.newBuilder();
        DeskClockOuterClass.DeskClock.Builder deskClock =DeskClockOuterClass.DeskClock.newBuilder();
        BeanUtils.copyBeanToProto(clock, deskClock);
        builder.setDeskClock(deskClock);
        Logger.d( "deleteAlarm clock = %s" , clock.toString());
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_DESKCLOCK_MANAGE_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmManageDeskClock.CmManageDeskClockResponse>() {

            @Override
            public void onSuccess(final CmManageDeskClock.CmManageDeskClockResponse data) {
                Logger.d("deleteAlarm result = %b" , data.getIsSuccess());
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

                Logger.w( "deleteAlarm onError = %s " , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });

            }
        });
    }

    public void addAlarm(DeskClock clock, @NonNull final IRobotAlarmDataSource.ControlAlarmCallback callback) {
        CmManageDeskClock.CmManageDeskClockRequest.Builder builder =  CmManageDeskClock.CmManageDeskClockRequest.newBuilder();
        DeskClockOuterClass.DeskClock.Builder deskClock =DeskClockOuterClass.DeskClock.newBuilder();
        BeanUtils.copyBeanToProto(clock, deskClock);
        builder.setDeskClock(deskClock);

        Logger.d( "addAlarm clock = %s" , clock.toString());
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_DESKCLOCK_MANAGE_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmManageDeskClock.CmManageDeskClockResponse>() {

            @Override
            public void onSuccess(final CmManageDeskClock.CmManageDeskClockResponse data) {
                Logger.d("addAlarm result = %s" , data.getIsSuccess());

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
            public void onError(final ThrowableWrapper e)
            {
                Logger.w( "addAlarm onError = %s" , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });

            }
        });
    }

    public void updateAlarm(DeskClock clock, @NonNull final IRobotAlarmDataSource.ControlAlarmCallback callback) {
        CmManageDeskClock.CmManageDeskClockRequest.Builder builder =  CmManageDeskClock.CmManageDeskClockRequest.newBuilder();
        DeskClockOuterClass.DeskClock.Builder deskClock =DeskClockOuterClass.DeskClock.newBuilder();
        BeanUtils.copyBeanToProto(clock, deskClock);
        builder.setDeskClock(deskClock);
        Logger.d( "updateAlarm clock =  %s" , clock.toString());
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_DESKCLOCK_MANAGE_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmManageDeskClock.CmManageDeskClockResponse>() {

            @Override
            public void onSuccess(final CmManageDeskClock.CmManageDeskClockResponse data) {

                Logger.d( "updateAlarm result = %s" , data.getIsSuccess());
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
                Logger.w(TAG, "updateAlarm onError = %s" , e.toString());
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
