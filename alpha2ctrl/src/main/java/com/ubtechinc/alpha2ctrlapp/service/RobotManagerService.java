package com.ubtechinc.alpha2ctrlapp.service;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.ListUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.data.Injection;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAuthorizeDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAuthorizeReponsitory;
import com.ubtechinc.alpha2ctrlapp.database.RobotInfoProvider;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.alpha2ctrlapp.events.RobotControlEvent;
import com.ubtechinc.alpha2ctrlapp.events.RobotIMStateChangeEvent;
import com.ubtechinc.alpha2ctrlapp.events.RobotRefreshEvent;
import com.ubtechinc.alpha2ctrlapp.events.UpdateRobotInfoEvent;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.im.event.IMStateChange;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;


/**
 * @ClassName RobotManagerService
 * @date 6/13/2017
 * @author tanghongyu
 * @Description 机器人管理类
 * @modifier
 * @modify_time
 */
public class RobotManagerService {
    private static final String TAG = "RobotManagerService";

    private volatile static RobotManagerService sInstance;

    private  RobotInfo connectedRobot;
    private ArrayMap<String, RobotInfo> mRobotStateMap = new ArrayMap<>();


    private RobotManagerService() {

        robotAuthorizeReponsitory = Injection.provideRobotRepository(Alpha2Application.getAlpha2());
        EventBus.getDefault().register(this);
    }


    public static RobotManagerService getInstance() {
        if (sInstance == null) {
            synchronized (RobotManagerService.class) {
                if (sInstance == null) {
                    sInstance = new RobotManagerService();
                }
            }
        }

        return sInstance;
    }




    /**
    /**
     * 重新刷新服务器中的机器人数据
     */
    RobotAuthorizeReponsitory robotAuthorizeReponsitory;

    public void refreshDevices() {
        Logger.i(" refreshDevices ");
        robotAuthorizeReponsitory.loadRobotList(new IRobotAuthorizeDataSource.LoadRobotListCallback() {
            @Override
            public void onLoadRobotList(final List<RobotInfo> robotList) {

                if (ListUtils.isEmpty(robotList)) {

                    EventBus.getDefault().post(new RobotRefreshEvent());
                    mRobotStateMap.clear();

                } else {

                    RobotInfoProvider.get().deleteRobotByParam(ImmutableMap.of("userId", (Object) Alpha2Application.getAlpha2().getUserId()));//每次都是先清空

                    RobotInfoProvider.get().saveOrUpdateByRelationIdAndEquitmentId(robotList);
                    if (mRobotStateMap.isEmpty() || mRobotStateMap.size() != robotList.size()) {//缓存的和获取到的不一致

                        List<String> snList = Lists.newArrayList();
                        for(RobotInfo robotInfo : robotList) {
                            snList.add(robotInfo.getEquipmentId());
                        }
                        RobotAuthorizeReponsitory.getInstance().getRobotIMStatus(snList, new IRobotAuthorizeDataSource.GetRobotIMStateCallback() {
                            @Override
                            public void onLoadRobotIMState(Map<String, String> stateMap) {
                                mRobotStateMap.clear();
                                for (RobotInfo robot : robotList) {
                                    String account = robot.getEquipmentId();
                                    String state = stateMap.get(account);

                                    if(StringUtils.isEmpty(state)) {
                                        robot.setConnectionState(BusinessConstants.ROBOT_STATE_UNAVAILABLE);

                                    }else {
                                        if (StringUtils.isEquals(BusinessConstants.ROBOT_IM_STATE_ONLINE, state)) {
                                            robot.setConnectionState(BusinessConstants.ROBOT_STATE_ONLINE);

                                        } else if (StringUtils.isEquals(BusinessConstants.ROBOT_IM_STATE_OFFLINE, state)) {
                                            robot.setConnectionState(BusinessConstants.ROBOT_STATE_OFFLINE);


                                        } else {
                                            robot.setConnectionState(BusinessConstants.ROBOT_STATE_UNAVAILABLE);
                                        }
                                    }

                                    mRobotStateMap.put(account, robot);
                                }

                                EventBus.getDefault().post(new RobotRefreshEvent());
                            }

                            @Override
                            public void onDataNotAvailable(ThrowableWrapper e) {
                                EventBus.getDefault().post(new RobotRefreshEvent());
                            }
                        });

                    }else {
                        EventBus.getDefault().post(new RobotRefreshEvent());
                    }
                }
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                
                EventBus.getDefault().post(new RobotRefreshEvent());
            }
        });
    }




    private void connectSuccess(RobotInfo robotInfo) {
        Logger.d("connectSuccess");
        connectedRobot = robotInfo;
        Constants.deviceName = connectedRobot.getUserOtherName();
        Alpha2Application.setRobotSerialNo(connectedRobot.getEquipmentId());
        Logger.i(TAG,"connectSuccess");
        connectedRobot.setConnectionState(BusinessConstants.ROBOT_STATE_CONNECTED);
        mRobotStateMap.put(connectedRobot.getEquipmentId(), connectedRobot);
        MainPageActivity.hasConnectedNewRobot = true;
        MainPageActivity.appInfoList.clear();
        EventBus.getDefault().post(new RobotControlEvent(RobotControlEvent.CONNECT_SUCCESS));
    }

    private void connectFail() {

        EventBus.getDefault().post(new RobotControlEvent(RobotControlEvent.CONNECT_FAIL));

    }








    /**
     * 获取机器人连接的序列表
     *
     * @return
     */
    public  String getConnectedSn() {
        if (connectedRobot != null && !TextUtils.isEmpty(connectedRobot.getEquipmentId())) {
            return connectedRobot.getEquipmentId();
        } else {
            return "";
        }


    }

    /**
     * 获取机器人列表，如果数据没有，就从数据库中读取
     *
     * @return
     */
    public List<RobotInfo> getRobotModelList() {
        List<RobotInfo> robotInfoList = Lists.newArrayList();
        if (mRobotStateMap.isEmpty()) {
            robotInfoList.addAll(RobotInfoProvider.get().findRobotListByParam(ImmutableMap.of("userId", (Object) Alpha2Application.getAlpha2().getUserId())));
        }else {
            robotInfoList.addAll(mRobotStateMap.values());
        }

        return robotInfoList;
    }



    /**
     * 清理连接数据
     */
    public void clearConnectCacheData() {

        mRobotStateMap.clear();

        Alpha2Application.setRobotSerialNo("");
        connectedRobot = null;
        Logger.i(TAG, "clearConnectCacheData");
    }

    /**
     * 是否已经连接机器人
     *
     * @return true 没有连接机器人，false 连接机器人
     */
    public boolean isConnectedRobot() {
        return connectedRobot != null;
    }
    public boolean isRobotOwner() {
        return connectedRobot != null && connectedRobot.getUpUserId() == 0;
    }





    public  RobotInfo getConnectedRobot() {
        return connectedRobot;
    }




    public void connectRobot(final String serialNo, @NonNull final IRobotAuthorizeDataSource.IControlRobotCallback controlRobotCallback) {
        final RobotInfo robotInfo = mRobotStateMap.get(serialNo);
        if(robotInfo == null) {
            Logger.d("connectRobot fail robotInfo == null");
            EventBus.getDefault().post(new RobotControlEvent(RobotControlEvent.CONNECT_FAIL));
            return;
        }
        if(connectedRobot != null) {//如果之前有连接，则先断开
            disConnect2Robot(connectedRobot.getEquipmentId(), new IRobotAuthorizeDataSource.IControlRobotCallback() {
                @Override
                public void onSuccess() {
                    connectRobot(serialNo, controlRobotCallback);
                }

                @Override
                public void onFail(ThrowableWrapper e) {
                    Logger.i("connectRobot fail ,disconnect previous robot fail");
                    EventBus.getDefault().post(new RobotControlEvent(RobotControlEvent.CONNECT_FAIL));
                }
            });
        }
        RobotAuthorizeReponsitory.getInstance().connectRobot(serialNo, new IRobotAuthorizeDataSource.IControlRobotCallback() {
            @Override
            public void onSuccess() {
                connectSuccess(robotInfo);
                controlRobotCallback.onSuccess();
            }

            @Override
            public void onFail(ThrowableWrapper e) {
                Logger.d("connect fail reason = ",e.getMessage());
                connectFail();
                controlRobotCallback.onFail(e);
            }
        });
    }

    /**
     * 断开与机器人的连接
     */
    public void disConnect2Robot(String serialNo, @NonNull final IRobotAuthorizeDataSource.IControlRobotCallback controlRobotCallback) {
        RobotAuthorizeReponsitory.getInstance().disConnectRobot(serialNo, new IRobotAuthorizeDataSource.IControlRobotCallback() {
            @Override
            public void onSuccess() {
                Alpha2Application.setRobotSerialNo("");
                connectedRobot = null;
                controlRobotCallback.onSuccess();

            }

            @Override
            public void onFail(ThrowableWrapper e) {

                controlRobotCallback.onFail(e);

                ToastUtils.showShortToast(R.string.bt_disconnect_fail);
            }
        });
    }




    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RobotIMStateChangeEvent event) {
        Logger.d("onEvent RobotIMStateChangeEvent userId = %s  state = %s ",event.getUserId(),event.getState());
        if(mRobotStateMap.get(event.getUserId()) != null) {
            mRobotStateMap.get(event.getUserId()).setConnectionState(event.getState());
        }
        if(event.getState() == 0){
            clearConnectCacheData();
        }
        refreshDevices();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IMStateChange event) {
        Logger.d("onEvent IMStateChange state = ", event.getState());
        if (event.getState() == IMStateChange.STATE_DISCONNECTED || event.getState() == IMStateChange.STATE_FORCE_OFFLINE) {//掉线或被强制挤下线
            clearConnectCacheData();
            LoadingDialog.dissMiss();
        }
        Logger.i(TAG, "广播状态刷新");
        refreshDevices();// 状态更改后，需要重新请求数据
    }

    /*
    * 机器人设置修改名称 头像后刷新UI事件，目前只修改名称和头像，后续有其他属性修改继续添加
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateRobotInfoEvent event) {
        RobotInfo robotInfo = event.getRobotInfo();
        List<RobotInfo> robotInfos = getRobotModelList();
        for (RobotInfo info : robotInfos) {
            if(info.equals(robotInfo)){
                String userOtherName = robotInfo.getUserOtherName();
                if(!TextUtils.isEmpty(userOtherName)){
                    info.setUserOtherName(robotInfo.getUserOtherName());
                }
                String robotUserImage = robotInfo.getUserImage();
                if(!TextUtils.isEmpty(robotUserImage)){
                    info.setUserImage(robotUserImage);
                }
                EventBus.getDefault().post(new RobotRefreshEvent());
                break;
            }
        }
    }
}
