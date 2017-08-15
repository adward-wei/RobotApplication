package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.ListUtils;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2ActivityIntent;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAuthorizeDataSource;
import com.ubtechinc.alpha2ctrlapp.entity.SearchRelationInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotAndAuthoriserModel;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.alpha2ctrlapp.events.RobotRefreshEvent;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.user.PersonInfoActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.robot.AuthorizeUserAdpeter;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.robot.MyDeviceInfoAdpter;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.widget.DeviceGuidePopView;
import com.ubtechinc.alpha2ctrlapp.widget.RoundImageView;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/*************************
 * @author
 * @date 2016/7/4
 * @Description 我的Alpha设备列表
 * @modify
 * @modify_time
 **************************/
public class MyDeviceActivity extends BaseContactActivity implements View.OnClickListener {
    protected Activity mContext;
    private String TAG = "MyDeviceActivity";
    private boolean isFirst = true;
    private MyDeviceInfoAdpter adpter;
    private Gallery robotView;
    private List<RobotAndAuthoriserModel> mRobotList = new ArrayList<RobotAndAuthoriserModel>();
    private RelativeLayout no_device_tip;
    private GridView authorierView;
    private RelativeLayout btnConnect;
    private TextView deviceName;
    private TextView deviceStatus;
    private AuthorizeUserAdpeter userAdpter;
    private TextView authorizeNum;
    public int currentPosition = -1;
    //当前选中的机器人
    public RobotAndAuthoriserModel currentRobot;
    private TextView noPermisstionTv;
    private TextView deviceBusyTips;
    private LinearLayout authorLay;
    private LinearLayout pointView;
    private ImageView btn_connect_imageView;
    private TextView btn_connect_tv;
    private LinearLayout noPermissionLay;
    private RoundImageView ownerHeader;
    private TextView ownName;
    private DeviceGuidePopView guidePop;
    public static boolean isForeground = false;
    private List<SearchRelationInfo> allList = new ArrayList<SearchRelationInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_device);
        this.mContext = this;
        Intent intent = getIntent();
        isFirst = intent.getBooleanExtra("isFirst", true);
        Alpha2Application.getAlpha2().onlyActivity(this);
        initView();
        if (null != RobotManagerService.getInstance()) {
            RobotManagerService.getInstance().refreshDevices();// 首次都重新刷数据
        }
        EventBus.getDefault().register(this);
    }

    public void initView() {
        this.title = (TextView) findViewById(R.id.authorize_title);
        title.setText(getString(R.string.left_menu_my_robot));
        robotView = (Gallery) findViewById(R.id.lst_robot);
        authorierView = (GridView) findViewById(R.id.confirmlistView);
        deviceName = (TextView) findViewById(R.id.device_name);
        deviceStatus = (TextView) findViewById(R.id.device_stastus);
        mRobotList.clear();
        adpter = new MyDeviceInfoAdpter(mContext, mRobotList, mHandler);
        robotView.setAdapter(adpter);
        robotView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPosition   = position;
                refreshAuthorizeInfo();
                adpter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        robotView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent  = new Intent(mContext,RobotSettingActivity.class);
                intent.putExtra(PreferenceConstants.ALPHA_USER_ID,mRobotList.get(position).getRobotInfo().getEquipmentUserId()+"");
                intent.putExtra(Alpha2ActivityIntent.ALPHAMACADRESS, Tools.getJidByName(mRobotList.get(position).getRobotInfo().getEquipmentUserId()+""));
                intent.putExtra(PreferenceConstants.ROBOT_SERIAL_NO,mRobotList.get(position).getRobotInfo().getEquipmentId());
                intent.putExtra(IntentConstants.DATA_ROBOT_INFO, (Serializable)mRobotList.get(position).getRobotInfo());
                if(mRobotList.get(position).getRobotInfo().getUpUserId() ==0){
                    Constants.isOwner = true;
                }else{
                    Constants.isOwner = false;
                }
                intent.putExtra(IntentConstants.ROBOT_STATE,mRobotList.get(position).getRobotInfo().getStatus());
                 startActivityForResult(intent, NetWorkConstant.REFRESH_AUTHOURIZE);
            }
        });
        authorizeNum = (TextView) findViewById(R.id.authorizeNum);
        no_device_tip = (RelativeLayout) findViewById(R.id.no_device_tip);
        btnConnect = (RelativeLayout) findViewById(R.id.btn_connect);
        noPermisstionTv = (TextView) findViewById(R.id.no_permission);
        deviceBusyTips = (TextView) findViewById(R.id.device_tips);
        btnConnect.setOnClickListener(this);
        authorLay = (LinearLayout) findViewById(R.id.authorLay);
        pointView = (LinearLayout) findViewById(R.id.point);
        btn_connect_imageView = (ImageView) findViewById(R.id.btn_connect_image);
        btn_connect_tv = (TextView) findViewById(R.id.btn_connect_tv);
        noPermissionLay = (LinearLayout) findViewById(R.id.no_permissionLay);
        ownerHeader = (RoundImageView) findViewById(R.id.owner_header);
        ownName = (TextView) findViewById(R.id.owner_name);
        ownerHeader.setOnClickListener(this);
        guidePop = new DeviceGuidePopView(this, false);
        deviceStatus.setVisibility(View.VISIBLE);
        no_device_tip.setVisibility(View.GONE);
        robotView.setVisibility(View.VISIBLE);
        authorLay.setVisibility(View.VISIBLE);
        userAdpter = new AuthorizeUserAdpeter(mContext, allList);
        authorierView.setAdapter(userAdpter);
        authorierView.setOnItemClickListener(userAdpter);

    }





    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }



    public void down(View v) {
        if (isFirst) {
            Intent intent = new Intent(MyDeviceActivity.this, MainPageActivity.class);
            MyDeviceActivity.this.startActivity(intent);
        }
        MyDeviceActivity.this.finish();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.menu_out, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            down(null);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void addBound(View v) {
        Intent intent = new Intent(mContext, BindAlphaActivity.class);
        intent.putExtra("add", true);
        mContext.startActivity(intent);

    }



    public BaseHandler mHandler = new BaseHandler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg == null) {
                Logger.i(TAG, "handleMessage msg is null.");
                return;
            }
            switch (msg.what) {
                case MessageType.ALPHA_LOST_CONNECTED:
                    isCurrentAlpha2MacLostConnection((String) msg.obj);
                    break;
                case NetWorkConstant.RESPONSE_NOT_BIND:
                    Logger.i("nxy", "没有设备");
                    break;

                case NetWorkConstant.REQPONSE_FIND_QUTHORIZE_SUCCESS:
                    List<SearchRelationInfo> userlist = (List<SearchRelationInfo>) msg.obj;
                    refreshAuthoriUserListView(userlist, true);
                    break;
            }
        }
    };

    /**
     * 更新设备授权消息以及设备状态信息信息
     */
    private void refreshAuthorizeInfo() {
        if (mRobotList.size() > 0) {
            if (currentPosition == -1 || currentPosition > mRobotList.size() - 1) {
                currentPosition = mRobotList.size() / 2;
                robotView.setSelection(currentPosition);
            }
            for (int i = 0; i < pointView.getChildCount(); i++) {//先刷新下标点
                if (i == currentPosition) {
                    pointView.getChildAt(currentPosition).setBackgroundResource(R.drawable.sel_point_seleted);
                } else {
                    pointView.getChildAt(i).setBackgroundResource(R.drawable.sel_point);
                }
            }
            currentRobot = mRobotList.get(currentPosition);
            deviceName.setText(TextUtils.isEmpty(currentRobot.getRobotInfo().getUserOtherName()) ? currentRobot.getRobotInfo().getEquipmentId() : currentRobot.getRobotInfo().getUserOtherName());

            refreshButtonState(currentRobot.getRobotInfo().getConnectionState());
            if (isMyRobot(currentRobot.getRobotInfo().getUpUserId())) {
                noPermissionLay.setVisibility(View.GONE);
                authorierView.setVisibility(View.GONE);
                authorizeNum.setVisibility(View.GONE);
//                if ((System.currentTimeMillis() - currentRobot.getRefreshAuthoriserTime() > 3000) || currentRobot.getAuthoriInfo() == null) {
//                    doSearch(currentRobot.getRobotInfo().getEquipmentId()); // 距离上次请求超过3秒或者 获取的授权用户为空，就查找授权用户
//                } else {
//                    refreshAuthoriUserListView(currentRobot.getAuthoriInfo(), false);
//                }

            } else {
                LoadingDialog.dissMiss();
                LoadImage.LoadHeader(MyDeviceActivity.this, 0, ownerHeader, "currentRobot.getRobotInfo()");
                ownName.setText(currentRobot.getRobotInfo().getUpUserName());
                noPermisstionTv.setText(R.string.devices_no_authorize_permission);
                String upUserName = currentRobot.getRobotInfo().getUpUserName();
                if (!StringUtils.isEmpty(upUserName)) {
                    noPermisstionTv.setText(noPermisstionTv.getText().toString().replace("%", upUserName));
                    int index_1 = noPermisstionTv.getText().toString().indexOf(upUserName);
                    int index_2 = index_1 + upUserName.length();
                    SpannableStringBuilder builder = new SpannableStringBuilder(noPermisstionTv.getText().toString());
                    ForegroundColorSpan greenSpan = new ForegroundColorSpan(this.getResources().getColor(R.color.find_psw_tip_color_red));
                    builder.setSpan(greenSpan, index_1, index_2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    noPermisstionTv.setText(builder);
                    noPermissionLay.setVisibility(View.VISIBLE);
                }
                authorierView.setVisibility(View.GONE);
                authorizeNum.setVisibility(View.VISIBLE);
                authorizeNum.setText(mContext.getString(R.string.devices_owner));
            }
        } else {
            LoadingDialog.dissMiss();
        }
    }

    /**
     * 刷新按钮相关状态信息
     *
     * @param connectionState
     */
    private void refreshButtonState(int connectionState) {
        if (null != currentRobot && null != currentRobot.getRobotInfo()) {
            currentRobot.getRobotInfo().setConnectionState(connectionState);
            btn_connect_tv.setTextColor(getResources().getColor(R.color.text_color_t3));
            switch (currentRobot.getRobotInfo().getConnectionState()) {
                case BusinessConstants.ROBOT_STATE_UNAVAILABLE :
                case BusinessConstants.ROBOT_STATE_OFFLINE:
                    btnConnect.setVisibility(View.VISIBLE);
                    deviceStatus.setText(R.string.devices_offline);
                    btnConnect.setBackgroundResource(R.drawable.btn_button_able);
                    btn_connect_tv.setText(R.string.devices_wifi_seting);
                    btn_connect_imageView.setImageResource(R.drawable.btn_connect_wifi_icon);
                    deviceBusyTips.setVisibility(View.GONE);
                    deviceStatus.setTextColor(getResources().getColor(R.color.device_offline));
                    break;
                case BusinessConstants.ROBOT_STATE_ONLINE:
                    btnConnect.setVisibility(View.VISIBLE);
                    btnConnect.setBackgroundResource(R.drawable.btn_button_able);
                    deviceBusyTips.setVisibility(View.GONE);
                    btn_connect_tv.setText(R.string.devices_connect);
                    btn_connect_imageView.setImageResource(R.drawable.btn_connect_icon);
                    deviceStatus.setText(R.string.devices_online_status);
                    deviceStatus.setTextColor(getResources().getColor(R.color.device_online));
                    break;
                case BusinessConstants.ROBOT_STATE_CONNECTED:
                    deviceStatus.setText(R.string.devices_using_status);
                    btn_connect_tv.setText(R.string.devices_dis_connect);
                    btnConnect.setBackgroundResource(R.drawable.whitte_rounder_button);
                    btn_connect_imageView.setImageResource(R.drawable.btn_disconnect_icon);
                    btnConnect.setVisibility(View.VISIBLE);
                    deviceBusyTips.setVisibility(View.GONE);
                    btn_connect_tv.setTextColor(getResources().getColor(R.color.text_color_t6));
                    Constants.deviceName = currentRobot.getRobotInfo().getUserOtherName();
                    deviceStatus.setTextColor(getResources().getColor(R.color.device_busy));
                    break;

                default:
                    break;
            }
        }
    }

    private boolean isMyRobot(int userId) {
        return userId == 0;
    }

    /**
     * 搜索授权用户
     *
     * @param robotId
     */
    private void doSearch(String robotId) {
//        CheckBinedRequest request = new CheckBinedRequest();
//        request.setUpUserId(PreferencesManager.getInstance(mActivity).get(PreferenceConstants.USER_ID));
//        request.setToken(PreferencesManager.getInstance(mActivity).get(PreferenceConstants.TOKEN));
//        request.setEquipmentId(robotId);
//        UserAction action = UserAction.getInstance(mActivity, null);
//        action.setParamerObj(request);
//        searchRobotId = robotId;
//        action.doRequest(NetWorkConstant.REQUEST_FIND_QUTHORIZE, NetWorkConstant.find);

    }

    /**
     * 刷新授权用户列表界面
     *
     * @param list           授权用户列表
     * @param isAfterRequest 是否是网络请求之后刷新数据
     */
    private void refreshAuthoriUserListView(List<SearchRelationInfo> list, boolean isAfterRequest) {
        boolean needRreshCurrent = false;
        RobotInfo robotInfo = null;
        if (null != currentRobot && null != currentRobot.getRobotInfo()) {
            robotInfo = currentRobot.getRobotInfo();
        }
        if (list.size() == 0 && null != robotInfo ) {//没有授权用户的时候
            needRreshCurrent = true;
        } else {
            if (currentRobot != null && null != currentRobot.getRobotInfo()) {
                RobotInfo robotInfoModel1 = currentRobot.getRobotInfo();
                int index = 0;
                if (!isAfterRequest) index++;//如果不是网络请求返回，则跳过第一个授权按钮

                for (; index < list.size(); index++) {
                    Logger.i(TAG, "list index==" + list.get(index).getEquipmentId());
                    Logger.i(TAG, "robotInfoModel1.getEquipmentId()" + robotInfoModel1.getEquipmentId());
                    if (null != robotInfoModel1 && null != list.get(index) && null != list.get(index).getEquipmentId() && list.get(index).getEquipmentId().equals(robotInfoModel1.getEquipmentId())) {
                        needRreshCurrent = true;
                        if (!isAfterRequest)
                            break;
                        // 确保刷新的是当前机器人的状态
                        if (robotInfoModel1 != null && list.get(index).getUserId().equals(robotInfoModel1.getControlUserId() + "")) {
                            SearchRelationInfo info = list.get(index);
                            list.remove(index);
                            list.add(0, info); // 将正在控制的人放到最前面
                        }
                        break;
                    }

                }
            }
        }
        if (needRreshCurrent) {
            showAppGuide();
            authorierView.setVisibility(View.VISIBLE);
            authorizeNum.setVisibility(View.VISIBLE);
            if (isAfterRequest)//在第一个位置插入授权按钮
                list.add(0, new SearchRelationInfo());
            authorizeNum.setText(mContext.getString(R.string.devices_confirmed_user) + "(" + (list.size() - 1) + ")");
            userAdpter.onNotifyDataSetChanged(list);
            currentRobot.setAuthoriInfo(list);//设置授权用户
            if (isAfterRequest) {
                currentRobot.setRefreshAuthoriserTime(System.currentTimeMillis());// 设置本次刷新时间
            }
        }


    }

    /**
     * @return void
     * @Description 更新机器人界面
     */
    private void refreshDeviceView() {
        List<RobotInfo> robotTempList = RobotManagerService.getInstance().getRobotModelList();
        mRobotList.clear();
        if (ListUtils.isEmpty(robotTempList)) {
            ToastUtils.showShortToast( R.string.devices_no_robot);
            RobotManagerService.getInstance().clearConnectCacheData();
            no_device_tip.setVisibility(View.VISIBLE);
            allList.clear();
            userAdpter.onNotifyDataSetChanged(allList);
        } else {
            no_device_tip.setVisibility(View.GONE);
            pointView.removeAllViews();
            for (int i = 0; i < robotTempList.size(); i++) {
                ImageView view = new ImageView(mContext);
                view.setBackgroundResource(R.drawable.sel_point);
                view.setLayoutParams(new LayoutParams(20, 20));
                if (i == 0) {
                    pointView.addView(view);//第一个view不用设置间隔
                } else {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20, 20);
                    lp.setMargins(30, 0, 0, 0);
                    view.setLayoutParams(lp);
                    pointView.addView(view);
                }
                // 设置选中项
                if (currentPosition != -1 && robotTempList.size() > currentPosition) {// 滚动时以滚动目标为选中项
                    robotView.setSelection(currentPosition);
                } else {
                    if (!RobotManagerService.getInstance().isConnectedRobot()) {// 没有滚动时，首选已经连接的作为选中项，否则就设置中间项为选中项
                        robotView.setSelection(robotTempList.size() / 2);
                    } else {
                        if (StringUtils.isEquals(Alpha2Application.getRobotSerialNo(),robotTempList.get(i).getEquipmentId())) {
                            robotView.setSelection(i);
                        }
                    }
                }
                RobotAndAuthoriserModel robotAndAuthoriserModel = new RobotAndAuthoriserModel();
                robotAndAuthoriserModel.setRobotInfo(robotTempList.get(i));
                mRobotList.add(i, robotAndAuthoriserModel);
            }

            refreshAuthorizeInfo();
        }
        adpter.onNotifyDataSetChanged(mRobotList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                if (currentRobot != null) {
                    if (currentRobot.getRobotInfo().getConnectionState() == BusinessConstants.ROBOT_STATE_ONLINE ) {//
                        RobotManagerService.getInstance().connectRobot(currentRobot.getRobotInfo().getEquipmentId(), new IRobotAuthorizeDataSource.IControlRobotCallback() {
                            @Override
                            public void onSuccess() {
                                LoadingDialog.dissMiss();
                                goMainPage();
                                refreshDeviceView();
                            }

                            @Override
                            public void onFail(ThrowableWrapper e) {
                                LoadingDialog.dissMiss();
                                ToastUtils.showShortToast(R.string.bt_connect_fail);
                            }
                        });

                    } else if (currentRobot.getRobotInfo().getConnectionState() == BusinessConstants.ROBOT_STATE_CONNECTED) {
                        RobotManagerService.getInstance().disConnect2Robot(currentRobot.getRobotInfo().getEquipmentId(), new IRobotAuthorizeDataSource.IControlRobotCallback() {
                            @Override
                            public void onSuccess() {
                                LoadingDialog.dissMiss();
                                refreshButtonState(BusinessConstants.ROBOT_STATE_ONLINE);// 断开机器人成功，直接将界面置为在线空闲状态
                            }

                            @Override
                            public void onFail(ThrowableWrapper e) {

                            }
                        });
                    } else if (currentRobot.getRobotInfo().getConnectionState() == BusinessConstants.ROBOT_STATE_OFFLINE || currentRobot.getRobotInfo().getConnectionState() == BusinessConstants.ROBOT_STATE_UNAVAILABLE ) {
                        Intent intent = new Intent(mContext, ConfigureRobotNetworkActivity.class);
                        intent.putExtra("isFromDevice", true);
                        intent.putExtra(Constants.ROBOTSN, currentRobot.getRobotInfo().getEquipmentId());
                        if (currentRobot.getRobotInfo().getMacAddress() != null && !TextUtils.isEmpty(currentRobot.getRobotInfo().getMacAddress())) {
                            intent.putExtra(Constants.ROBOT_MAC, (Serializable) currentRobot.getRobotInfo().getMacAddress());
                        }
                        startActivity(intent);
                    }
                }
                break;
            case R.id.owner_header: {
                Intent intent = new Intent(this, PersonInfoActivity.class);
                intent.putExtra("userId", currentRobot.getRobotInfo().getUpUserId() + "");
                mContext.startActivity(intent);
            }
            break;
            default:
                break;
        }
    }
    /**
     * 连接成功进去主服务
     */
    private void goMainPage() {
        Logger.i("RobotManagerl:跳转");
        Intent intent = new Intent(mContext, MainPageActivity.class);
        startActivity(intent);

    }

    /**
     * 显示引导页面
     */
    private void showAppGuide() {
        if (currentRobot.getRobotInfo().getUpUserId() == 0) {
            if (!SPUtils.get().getBoolean(Constants.VERSION_CODE + Constants.device_invate_guide, false)) {
                guidePop.refresh(true);
                guidePop.show();
                SPUtils.get().put(Constants.VERSION_CODE + Constants.device_invate_guide, true);
            }
        } else {
            if (!SPUtils.get().getBoolean(Constants.VERSION_CODE + Constants.device_add_guide, false)) {
                guidePop.refresh(false);
                guidePop.show();
                SPUtils.get().put(Constants.VERSION_CODE + Constants.device_add_guide, true);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RobotRefreshEvent event) {
        refreshDeviceView();
    }
}
