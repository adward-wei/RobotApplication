package com.ubtechinc.alpha2ctrlapp.data.robot;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.JsonUtils;
import com.ubtech.utilcode.utils.thread.HandlerUtils;
import com.ubtechinc.alpha.CmAppEntrityInfoOuterClass;
import com.ubtechinc.alpha.CmAppEntrityList;
import com.ubtechinc.alpha.CmAppUninstallInfo;
import com.ubtechinc.alpha.CmAppUninstallList;
import com.ubtechinc.alpha.CmAppUninstallRobotInfo;
import com.ubtechinc.alpha.CmBaseResponseOuterClass;
import com.ubtechinc.alpha.CmInstallApp;
import com.ubtechinc.alpha.CmRobotMemory;
import com.ubtechinc.alpha.CmStartApp;
import com.ubtechinc.alpha.CmStopApp;
import com.ubtechinc.alpha.CmTransferAppData;
import com.ubtechinc.alpha.CmUninstallApp;
import com.ubtechinc.alpha.CmrAppButtonEventDataOuterClass;
import com.ubtechinc.alpha.CmrAppConfigData;
import com.ubtechinc.alpha.im.IMCmdId;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotApp;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.AppPackageSimpleInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ButtonConfig;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.StorageResponse;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.UnIntallApp;
import com.ubtechinc.alpha2ctrlapp.im.Phone2RobotMsgMgr;
import com.ubtechinc.alpha2ctrlapp.util.BeanUtils;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.im.IMErrorUtil;
import com.ubtechinc.nets.phonerobotcommunite.ICallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanghongyu
 * @ClassName RobotAppRepository
 * @date 6/14/2017
 * @Description 机器人App数据仓库
 * @modifier
 * @modify_time
 */
public class RobotAppRepository {

    private static RobotAppRepository INSTANCE = null;
    private static final String TAG = "RobotAppRepository";
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private RobotAppRepository() {

    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link RobotAppRepository} instance
     */
    public static RobotAppRepository getInstance() {
        Logger.t(TAG);
        if (INSTANCE == null) {
            INSTANCE = new RobotAppRepository();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    public void getAllInstalledApp(final @NonNull IRobotAppDataSource.GetInstalledAppCallback callback) {

        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_GET_ALLAPPS_REQUEST, "1", null, Alpha2Application.getRobotSerialNo(), new ICallback<CmAppEntrityList.CmAppEntrityListResponse>() {

            @Override
            public void onSuccess(CmAppEntrityList.CmAppEntrityListResponse data) {
                List<CmAppEntrityInfoOuterClass.CmAppEntrityInfo> appEntrityInfos = data.getAppListList();
                final List<RobotApp> appEntrityInfoList = Lists.newArrayList();
                for (CmAppEntrityInfoOuterClass.CmAppEntrityInfo appEntrityInfo : appEntrityInfos) {

                    RobotApp app = new RobotApp();
                    BeanUtils.copyBeanFromProto(appEntrityInfo, app);
                    Logger.t(TAG).d("getAllInstalledApp = " +  app);
                    appEntrityInfoList.add(app);
                }

                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onLoadAppList(appEntrityInfoList);
                    }
                });

            }

            @Override
            public void onError(final ThrowableWrapper e) {
                Logger.t(TAG).w( "deleteAlarm onError = %s " , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataNotAvailable(e);
                    }
                });

            }
        });
    }

    public void getCurrentRunningApp(final @NonNull IRobotAppDataSource.GetRunningAppCallback callback) {

        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_GET_TOP_APP_REQUEST, "1", null, Alpha2Application.getRobotSerialNo(), new ICallback<CmAppEntrityInfoOuterClass.CmAppEntrityInfo>() {


            @Override
            public void onSuccess(CmAppEntrityInfoOuterClass.CmAppEntrityInfo data) {

                final RobotApp app = new RobotApp();
                BeanUtils.copyBeanFromProto(data, app);
                Logger.t(TAG).d("getCurrentRunningApp = %s ", app.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onLoadApp(app);
                    }
                });
            }

            @Override
            public void onError(final ThrowableWrapper e) {

                Logger.t(TAG).w( "getCurrentRunningApp onError = %s " , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataNotAvailable(e);
                    }
                });
            }
        });
    }

    public void startApp(final String name, final String packageName, @NonNull final IRobotAppDataSource.ControlAppCallback callback) {
        CmStartApp.CmStartAppRequest.Builder builder = CmStartApp.CmStartAppRequest.newBuilder();
        builder.setName(name);
        builder.setPackageName(packageName);
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_START_APP_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmStartApp.CmStartAppResponse>() {


            @Override
            public void onSuccess(final CmStartApp.CmStartAppResponse data) {
                Logger.t(TAG).d( "startApp packageName = %s, name = %s, result = %b " ,packageName, name,data.getIsSuccess());
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
                Logger.t(TAG).w( "startApp onError = %s " , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });
            }
        });
    }

    public void stopApp(String name, String packageName, @NonNull final IRobotAppDataSource.ControlAppCallback callback) {
        CmStopApp.CmStopAppRequest.Builder builder = CmStopApp.CmStopAppRequest.newBuilder();
        builder.setPackageName(packageName);
        builder.setName(name);
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_STOP_APP_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmStopApp.CmStopAppResponse>() {


            @Override
            public void onSuccess(final CmStopApp.CmStopAppResponse data) {
                Logger.t(TAG).d( "updateApp result = %b " ,data.getIsSuccess());
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
                Logger.t(TAG).w( "stopApp onError = %s " , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });

            }
        });
    }

    public void installApp(RobotApp info, @NonNull final IRobotAppDataSource.ControlAppCallback callback) {
        CmInstallApp.CmInstallAppRequest.Builder builder = CmInstallApp.CmInstallAppRequest.newBuilder();
        BeanUtils.copyBeanToProto(info, builder);
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_INSTALL_PACKAGES_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmInstallApp.CmInstallAppResponse>() {


            @Override
            public void onSuccess(final CmInstallApp.CmInstallAppResponse data) {
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
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });
            }
        });
    }

    public void uninstallApp(String packageName,  String appKey, final @NonNull IRobotAppDataSource.ControlAppCallback callback) {
        CmUninstallApp.CmUninstallAppRequest.Builder builder = CmUninstallApp.CmUninstallAppRequest.newBuilder();
        builder.setPackageName(packageName);
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_UNINSTALL_PACKAGES_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmUninstallApp.CmUninstallAppResponse>() {


            @Override
            public void onSuccess(final CmUninstallApp.CmUninstallAppResponse data) {
                Logger.t(TAG).d( "updateApp result = %b " ,data.getIsSuccess());
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
                Logger.t(TAG).w( "uninstallApp onError = %s " , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });
            }
        });
    }

    public void updateApp(RobotApp info, final IRobotAppDataSource.ControlAppCallback callback) {
        CmInstallApp.CmInstallAppRequest.Builder builder = CmInstallApp.CmInstallAppRequest.newBuilder();
        BeanUtils.copyBeanToProto(info, builder);
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_UPDATE_PACKAGES_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmInstallApp.CmInstallAppResponse>() {


            @Override
            public void onSuccess(final CmInstallApp.CmInstallAppResponse data) {
                Logger.t(TAG).d( "updateApp result = %b " ,data.getIsSuccess());
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
                Logger.t(TAG).w( "updateApp onError = %s " , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });
            }
        });
    }

    public void getThirdAppConfig(int cmdId, String packageName, byte[] data, final @NonNull ICallback<CmrAppConfigData.CmrAppConfigDataResponse> callback) {

        CmTransferAppData.CmTransferAppDataRequest.Builder builder = CmTransferAppData.CmTransferAppDataRequest.newBuilder();
        builder.setPackageName(packageName);
        builder.setCmdID(cmdId);
        builder.setDatas(ByteString.copyFrom(data));
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_GET_APPCONFIG_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmrAppConfigData.CmrAppConfigDataResponse>() {


            @Override
            public void onSuccess(final CmrAppConfigData.CmrAppConfigDataResponse data) {

                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(data);
                    }
                });

            }

            @Override
            public void onError(final ThrowableWrapper e) {
                Logger.t(TAG).w( "getThirdAppConfig onError = %s " , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(e);
                    }
                });

            }
        });

    }

    public void saveThirdAppConfig(int cmdId, String packageName, byte[] data, @NonNull final IRobotAppDataSource.ControlAppCallback callback) {

        CmTransferAppData.CmTransferAppDataRequest.Builder builder = CmTransferAppData.CmTransferAppDataRequest.newBuilder();
        builder.setPackageName(packageName);
        builder.setCmdID(cmdId);
        builder.setDatas(ByteString.copyFrom(data));

        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_SAVE_APPCONFIG_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmBaseResponseOuterClass.CmBaseResponse>() {


            @Override
            public void onSuccess(final CmBaseResponseOuterClass.CmBaseResponse data) {
                Logger.t(TAG).d( "saveThirdAppConfig result = %d " , data.getResult());

                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (data.getResult() == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onFail(null);
                        }
                    }
                });

            }

            @Override
            public void onError(final ThrowableWrapper e) {
                Logger.t(TAG).w( "saveThirdAppConfig onError = %s " , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });
            }
        });

    }

    public void getAppButton(int cmdId, String packageName, byte[] data, @NonNull final IRobotAppDataSource.GetAppButtonConfigCallback callback) {
        CmTransferAppData.CmTransferAppDataRequest.Builder builder = CmTransferAppData.CmTransferAppDataRequest.newBuilder();
        builder.setPackageName(packageName);
        builder.setCmdID(cmdId);
        builder.setDatas(ByteString.copyFrom(data));
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_CLICK_APP_BUTTON_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmrAppButtonEventDataOuterClass.CmrAppButtonEventData>() {


            @Override
            public void onSuccess(CmrAppButtonEventDataOuterClass.CmrAppButtonEventData data) {
                String json = new String(data.getDatas().toByteArray());
                final ButtonConfig event = JsonUtils.jsonToBean(json,
                        ButtonConfig.class);
                Logger.t(TAG).d( "getAppButton ButtonConfig = %s " ,json);

                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onLoadButtonConfig(event);
                    }
                });
            }

            @Override
            public void onError(final ThrowableWrapper e)

            {   Logger.t(TAG).w( "getAppButton onError = %s " , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataNotAvailable(e);
                    }
                });
            }
        });
    }

    public void clickAppButton(int cmdId, String packageName, byte[] data, @NonNull final IRobotAppDataSource.ControlAppCallback callback) {
        CmTransferAppData.CmTransferAppDataRequest.Builder builder = CmTransferAppData.CmTransferAppDataRequest.newBuilder();
        builder.setPackageName(packageName);
        builder.setCmdID(cmdId);
        builder.setDatas(ByteString.copyFrom(data));
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_CLICK_APP_BUTTON_REQUEST, "1", builder.build(), Alpha2Application.getRobotSerialNo(), new ICallback<CmBaseResponseOuterClass.CmBaseResponse>() {


            @Override
            public void onSuccess(final CmBaseResponseOuterClass.CmBaseResponse data) {


                Logger.t(TAG).d( "clickAppButton result = %d " , data.getResult());

                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (data.getResult() == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onFail(IMErrorUtil.handleException(data.getResult()));
                        }
                    }
                });

            }

            @Override
            public void onError(final ThrowableWrapper e) {

                Logger.t(TAG).w( "clickAppButton onError = %s " , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e);
                    }
                });

            }
        });

    }

    public void getRobotStorage(@NonNull final IRobotAppDataSource.GetRobotStorageCallback callback) {
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_QUERY_ROBOT_STORAGE_APP_LIST_REQUEST, "1", null, Alpha2Application.getRobotSerialNo(), new ICallback<CmRobotMemory.CmRobotMemoryResponse>() {


            @Override
            public void onSuccess(CmRobotMemory.CmRobotMemoryResponse data) {

                final StorageResponse response = new StorageResponse();
                BeanUtils.copyBeanFromProto(data, response);

                List<AppPackageSimpleInfo> appList = new ArrayList<>();
                for (CmAppUninstallRobotInfo.CmAppUninstallRobotInfoResponse cmAppUninstallRobotInfoResponse : data.getAppListList()) {
                    AppPackageSimpleInfo appPackageSimpleInfo = new AppPackageSimpleInfo();
                    BeanUtils.copyBeanFromProto(cmAppUninstallRobotInfoResponse, appPackageSimpleInfo);
                    appList.add(appPackageSimpleInfo);
                    Logger.t(TAG).d( "getRobotStorage AppPackageSimpleInfo = %s " , appPackageSimpleInfo.toString());
                }
                response.setAppList(appList);


                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onLoadRobotStorage(response);
                    }
                });

            }

            @Override
            public void onError(final ThrowableWrapper e) {

                Logger.t(TAG).w( "getRobotStorage onError = %s " , e.toString());
                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataNotAvailable(e);
                    }
                });
            }
        });
    }

    public void batchUninstall(List<AppPackageSimpleInfo> appList, final @NonNull IRobotAppDataSource.BatchUninstallAppCallback callback) {
        CmAppUninstallList.CmAppUninstallListRequest.Builder builder = CmAppUninstallList.CmAppUninstallListRequest.newBuilder();
        for (AppPackageSimpleInfo app : appList) {

            CmAppUninstallInfo.CmAppUninstallInfoRequest.Builder appRequest = CmAppUninstallInfo.CmAppUninstallInfoRequest.newBuilder();
            BeanUtils.copyBeanToProto(app, appRequest);
            builder.addAppList(appRequest.build());
        }
        Phone2RobotMsgMgr.getInstance().sendDataToRobot(IMCmdId.IM_UNINSTALL_BATCH_APPS_REQUEST, "1", null, Alpha2Application.getRobotSerialNo(), new ICallback<CmAppUninstallList.CmAppUninstallListResponse>() {
            @Override
            public void onSuccess(CmAppUninstallList.CmAppUninstallListResponse data) {

                List<CmAppUninstallInfo.CmAppUninstallInfoResponse> appUninstallInfoResponseList = data.getAppListList();
                final List<UnIntallApp> unIntallApps = Lists.newArrayList();
                for (CmAppUninstallInfo.CmAppUninstallInfoResponse response : appUninstallInfoResponseList) {
                    UnIntallApp app = new UnIntallApp();
                    BeanUtils.copyBeanFromProto(response, app);
                    unIntallApps.add(app);
                    Logger.t(TAG).d( "batchUninstall UnIntallApp = %s " , app.toString());
                }


                HandlerUtils.runUITask(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(unIntallApps);
                    }
                });
            }

            @Override
            public void onError(final ThrowableWrapper e) {

                Logger.t(TAG).w( "batchUninstall onError = %s " , e.toString());
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
