package com.ubtechinc.alpha2ctrlapp.data.robot;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.CmAlphaInitParam;
import com.ubtechinc.alpha.CmQueryPowerData;
import com.ubtechinc.alpha.CmQuerySoftwareVersion;
import com.ubtechinc.alpha.CmSetMaterName;
import com.ubtechinc.alpha.CmSetRTCTimeData;
import com.ubtechinc.alpha.im.IMCmdId;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.AlphaParam;
import com.ubtechinc.alpha2ctrlapp.im.Phone2RobotMsgMgr;
import com.ubtechinc.alpha2ctrlapp.util.BeanUtils;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.phonerobotcommunite.ICallback;

/**
 * @ClassName RobotInitRepository
 * @date 6/14/2017
 * @author tanghongyu
 * @Description 机器人初始化数据仓库
 * @modifier
 * @modify_time
 */
public class RobotInitRepository {

    private static RobotInitRepository INSTANCE = null;
    boolean mCacheIsDirty = false;
    private static final String TAG = "RobotInitRepository";
    // Prevent direct instantiation.
    private RobotInitRepository() {

    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link RobotInitRepository} instance
     */
    public static RobotInitRepository getInstance() {
        Logger.t(TAG);
        if (INSTANCE == null) {
            INSTANCE = new RobotInitRepository();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    public void getRobotInitParam( final @NonNull IRobotInitDataSource.GetInitDataCallback callback) {
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_GET_ROBOT_INIT_STATUS_REQUEST, "1", null, Alpha2Application.getRobotSerialNo(), new ICallback<CmAlphaInitParam.CmAlphaInitParamResponse>() {
            @Override
            public void onSuccess(CmAlphaInitParam.CmAlphaInitParamResponse data) {

                CmQuerySoftwareVersion.CmQuerySoftwareVersionResponse softwareVersion = data.getSoftwareVersion();
                Logger.d("getRobotInitParam = ", softwareVersion);
               final AlphaParam alphaParam = new AlphaParam();
                alphaParam.setMasterName(data.getMasterName());
                alphaParam.setServiceLanguage(data.getServiceLanguage());
                alphaParam.setSDAvailableVolume(data.getMemory().getAvailableExternalSize());
                alphaParam.setSDTotalVolume(data.getMemory().getTotalExternalSize());

                BeanUtils.copyBeanFromProto(softwareVersion, alphaParam);


                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onLoadInitData(alphaParam);
                    }
                });
            }

            @Override
            public void onError(final ThrowableWrapper e) {
                Logger.w("getRobotInitParam  onError = %s ", e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataNotAvailable(e);
                    }
                });


            }
        });
    }

    public void setRTCTime(CmSetRTCTimeData.CmSetRTCTimeDataRequest request,  final @NonNull IRobotInitDataSource.SetInitDataCallback callback) {
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_SET_RTC_TIME_REQUEST, "1", request, Alpha2Application.getRobotSerialNo(), new ICallback<CmSetRTCTimeData.CmSetRTCTimeDataResponse>() {

            @Override
            public void onSuccess(CmSetRTCTimeData.CmSetRTCTimeDataResponse data) {

                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess();
                    }
                });

            }

            @Override
            public void onError(final ThrowableWrapper e)
            {
                Logger.w("setRTCTime  onError = %s ", e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail  (e);
                    }
                });

            }
        });
    }


    public void getPower(@NonNull final IRobotInitDataSource.GetPowerDataCallback callback) {
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_QUERY_ROBOT_POWER_REQUEST, "1", null, Alpha2Application.getRobotSerialNo(), new ICallback<CmQueryPowerData.CmQueryPowerDataResponse>() {

            @Override
            public void onSuccess(final CmQueryPowerData.CmQueryPowerDataResponse data) {


                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onLoadPowerData(data.getPowerValue());
                    }
                });


            }

            @Override
            public void onError(final ThrowableWrapper e) {
                Logger.w("getPower  onError = %s ", e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataNotAvailable(e);
                    }
                });

            }
        });
    }

    public void setServiceLanguage(String language, @NonNull final IRobotInitDataSource.SetInitDataCallback callback) {

    }


    public void setMasterName(String masterName, @NonNull final IRobotInitDataSource.SetInitDataCallback callback) {


        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_SET_MASTER_NAME_REQUEST, "1", CmSetMaterName.CmSetMasterNameRequest.newBuilder().setMasterName(masterName).build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmSetMaterName.CmSetMasterNameResponse>() {
            @Override
            public void onSuccess(final CmSetMaterName.CmSetMasterNameResponse data) {
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        if(data.getIsSuccess()) {
                            callback.onSuccess();
                        }else {
                            callback.onFail(new ThrowableWrapper("", data.getResultCode()));
                        }
                    }
                });


            }

            @Override
            public void onError(final ThrowableWrapper e)
            {
                Logger.w("setMasterName  onError = %s ", e.toString());
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
