package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.JsonUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.base.CallbackListener;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.BluetoothDevice;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ManageRobotBluetooth;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.robot.BluetoothDeviceAdapter;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;

import java.util.ArrayList;

/*************************
* @date 2016/7/4
* @author 唐宏宇
* @Description 蓝牙设备管理页
* @modify
* @modify_time
**************************/
public class ConnectBluetoothActivity extends BaseContactActivity implements
        View.OnClickListener, CallbackListener<Bundle> {

    ListView connection_records_lv, search_result_lv;
    ImageView connection_state_iv, control_iv;
    TextView control_tv,connection_state_tv, btn_top_right,connection_device_tv;
    LinearLayout connection_layer,control_layer,history_layer, search_result_layer;
    BluetoothDeviceAdapter historyConnectionAdapter, searchResultAdapter;
    ProgressBar search_pb;
    //初始状态
    private final int CONNECTION_STATE_DISCONNECTED = 0;
    //已连接
    private final int CONNECTION_STATE_CONNECTED = 1;
    //搜索中
    private final int CONNECTION_STATE_SEARCHING = 2;
    //搜索结果
    private final int CONNECTION_STATE_SEARCH_RESULT = 3;
    //搜索停止
    private final int CONNECTION_STATE_STOP = 4;
    ArrayList<BluetoothDevice> mHistoryConnections,mSearchResults;
    private boolean mIsConnected;
    private BluetoothDevice mCurrentBluetoothDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_bluetooth);
        initView();
        initData();
    }

    private void initView() {
        title= (TextView)findViewById(R.id.authorize_title);
        title.setText(R.string.bt_name);

        connection_records_lv = (ListView) findViewById(R.id.connection_records_lv);
        search_result_lv= (ListView) findViewById(R.id.search_result_lv);
        connection_state_iv = (ImageView) findViewById(R.id.connection_state_iv);
        control_iv = (ImageView) findViewById(R.id.control_iv);
        control_tv = (TextView) findViewById(R.id.control_tv);
        connection_state_tv = (TextView) findViewById(R.id.connection_state_tv);
        connection_layer = (LinearLayout) findViewById(R.id.connection_layer);
        control_layer = (LinearLayout) findViewById(R.id.control_layer);
        history_layer = (LinearLayout) findViewById(R.id.history_layer);
        search_pb = (ProgressBar) findViewById(R.id.search_pb);
        search_result_layer = (LinearLayout) findViewById(R.id.search_result_layer);
        control_layer.setOnClickListener(this);
        connection_device_tv= (TextView) findViewById(R.id.connection_device_tv);
        btn_top_right = (TextView) findViewById(R.id.btn_top_right);
        btn_top_right.setText(R.string.bt_refresh);

        btn_top_right.setOnClickListener(this);
    }

    private void initData() {
        //查询本地历史连接记录

        mHistoryConnections = new ArrayList<>();
        mSearchResults = new ArrayList<>();

        Intent intent = getIntent();
        if(intent != null) {
            mIsConnected =  intent.getBooleanExtra(IntentConstants.ROBOT_BLUETOOTH_CONNECTION_STATE, false);
            mCurrentBluetoothDevice = (BluetoothDevice) intent.getSerializableExtra(IntentConstants.ROBOT_BLUETOOTH_DEVICE);
        }


        historyConnectionAdapter = new BluetoothDeviceAdapter(mApplication, mHistoryConnections, this);
        searchResultAdapter = new BluetoothDeviceAdapter(mApplication, mSearchResults, this);
        connection_records_lv.setAdapter(historyConnectionAdapter);
        search_result_lv.setAdapter(searchResultAdapter);
        mHandler = new MyHandler();
        refreshUIByConnectionState((mIsConnected && mCurrentBluetoothDevice != null) ? CONNECTION_STATE_CONNECTED : CONNECTION_STATE_DISCONNECTED);
        getMatchedDeviceList();
    }

    /**
     * @Description 根据蓝牙设备连接状态刷新UI
     * @param state 蓝牙设备连接
     * @return
     * @throws
     */
    private void refreshUIByConnectionState(int state) {
        switch (state) {
            case CONNECTION_STATE_DISCONNECTED://未连接状态
                connection_state_iv.setImageResource(R.drawable.warning);
                connection_state_tv.setText(R.string.bt_none);
                control_layer.setBackgroundResource(R.drawable.btn_button_able);
                control_iv.setImageResource(R.drawable.search_signal);
                control_tv.setText(R.string.bt_button_searching);
                control_tv.setTextColor(getResources().getColor(R.color.text_color_t3));
                control_layer.setVisibility(View.VISIBLE);
                search_result_layer.setVisibility(View.GONE);
                btn_top_right.setVisibility(View.GONE);
                connection_device_tv.setText("");
                break;
            case CONNECTION_STATE_CONNECTED ://已连接

                connection_state_iv.setImageResource(R.drawable.bluetooth);
                connection_state_tv.setText(R.string.bt_ing);
                control_layer.setBackgroundResource(R.drawable.white_rounder_button);
                control_iv.setImageResource(R.drawable.btn_disconnect_icon);
                control_tv.setText(R.string.devices_dis_connect);
                control_tv.setTextColor(getResources().getColor(R.color.text_color_t6));
                control_layer.setVisibility(View.VISIBLE);
                search_result_layer.setVisibility(View.GONE);
                connection_layer.setVisibility(View.VISIBLE);
                btn_top_right.setVisibility(View.GONE);
                connection_device_tv.setText(mCurrentBluetoothDevice.getDeviceName());
                break;
            case CONNECTION_STATE_SEARCHING ://搜索中
                connection_state_iv.setImageResource(R.drawable.bluetooth_search);
                connection_state_tv.setText(R.string.bt_searching);
                control_layer.setVisibility(View.GONE);
                search_result_layer.setVisibility(View.GONE);
                search_pb.setVisibility(View.VISIBLE);
                btn_top_right.setVisibility(View.GONE);
                break;
            case CONNECTION_STATE_SEARCH_RESULT ://搜索到设备
                connection_layer.setVisibility(View.GONE);
                control_layer.setVisibility(View.GONE);
                search_result_layer.setVisibility(View.VISIBLE);
                btn_top_right.setVisibility(View.VISIBLE);
                break;
            case CONNECTION_STATE_STOP ://搜索结束
                search_pb.setVisibility(View.GONE);
                btn_top_right.setVisibility(View.VISIBLE);
                break;


        }
    }


    /**
     * @Description 连接或断开蓝牙
     * @param isConnect 是否连接 device 连接或断开的设备
     * @return
     */
    private final int MANAGE_TIME_OUT_LIMIT = 20000;//连接或断开的超时时间
    private final int MSG_MANAGE_TIME_OUT_WHAT = 99;
    private void connectOrDisconnect(boolean isConnect, BluetoothDevice device){
        LoadingDialog.getInstance(this).show();
        setCanConnect(false);//正在连接或断
        // 开时不允许点击
        mHandler.sendEmptyMessageDelayed(
                MSG_MANAGE_TIME_OUT_WHAT,
                MANAGE_TIME_OUT_LIMIT);
        ManageRobotBluetooth manage = new ManageRobotBluetooth();
        manage.setSendByClient(true);
        manage.setContent(JsonUtils.object2Json(device));
        int commandId = isConnect ? BusinessConstants.ROBOT_BLUETOOTH_CMD_CONNECT_DEVICE : BusinessConstants.ROBOT_BLUETOOTH_CMD_DISCONNECT_DEVICE;
        manage.setOrderCmd(commandId);
//        sendRequest(manage, SocketCmdId.ALPHA_MSG_MANAGE_BLUETOOTH
//               );
    }
    /**
     * @Description 设置是否能连接，防止连接过程中点击连接
     * @param
     * @return
     */
    private void setCanConnect(boolean isCanConnect) {
        historyConnectionAdapter.setCanConnect(isCanConnect);
        searchResultAdapter.setCanConnect(isCanConnect);
        historyConnectionAdapter.notifyDataSetChanged();
        searchResultAdapter.notifyDataSetChanged();
    }

    /**
     * @Description 开始扫描蓝牙设备
     * @param
     * @return
     */
    private void startOrStopScanBluetoothDevice(boolean isStart){

        LoadingDialog.getInstance(mContext).show();
//        ManageRobotBluetooth manage = new ManageRobotBluetooth();
//        manage.setSendByClient(true);
//        manage.setOrderCmd(isStart ? BusinessConstants.ROBOT_BLUETOOTH_CMD_SCAN : BusinessConstants.ROBOT_BLUETOOTH_CMD_BREAK_OFF_SCAN);
//        sendRequest(manage, SocketCmdId.ALPHA_MSG_MANAGE_BLUETOOTH);
    }
    private void getMatchedDeviceList(){

        LoadingDialog.getInstance(mContext).show();
//        ManageRobotBluetooth manage = new ManageRobotBluetooth();
//        manage.setSendByClient(true);
//        manage.setOrderCmd(BusinessConstants.ROBOT_BLUETOOTH_CMD_GET_MATCHED_DEVICE_LIST );
//        sendRequest(manage, SocketCmdId.ALPHA_MSG_MANAGE_BLUETOOTH);
    }
    int mConnectPosition = -1;
    @Override
    public void callback(Bundle bundle) {
        this.mCurrentBluetoothDevice = (BluetoothDevice) bundle.getSerializable("device");
        mConnectPosition = bundle.getInt("position");


        showPromptDialog(true);
    }

    private class MyHandler extends BaseHandler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg == null) {
                Logger.i("handleMessage msg is null.");

                return;
            }

           if(msg.what == MSG_MANAGE_TIME_OUT_WHAT)   {
               //管理蓝牙请求超时
               LoadingDialog.getInstance(mContext).dismiss();
               setCanConnect(true);
               return;
           }


        }
    }

    private void showPromptDialog(boolean isConnect) {
       final CommonDiaglog exsitdialog = new CommonDiaglog(this, true);


        if(isConnect) {
            exsitdialog.setTitle(getString(R.string.bt_connect_warning_title));
            String buffer = getString(R.string.bluetooth_want_to_connect_the_device);
            String newStr = buffer.replace("XX", mCurrentBluetoothDevice.getDeviceName());
            exsitdialog.setMessase(newStr);
            exsitdialog.setNegsitiveClick(new CommonDiaglog.OnNegsitiveClick() {
                @Override
                public void OnNegsitiveClick() {
                    exsitdialog.dismiss();
                }
            });
            exsitdialog.setPositiveClick(new CommonDiaglog.OnPositiveClick() {
                @Override
                public void OnPositiveClick() {
                    connectOrDisconnect(true, mCurrentBluetoothDevice);
                }
            });
        }else {
            exsitdialog.setTitle(getString(R.string.bt_disconnect_warning_title));
            exsitdialog.setMessase(mContext
                    .getString(R.string.bt_warning_describe));
            exsitdialog.setNegsitiveClick(new CommonDiaglog.OnNegsitiveClick() {
                @Override
                public void OnNegsitiveClick() {
                    exsitdialog.dismiss();
                }
            });
            exsitdialog.setPositiveClick(new CommonDiaglog.OnPositiveClick() {
                @Override
                public void OnPositiveClick() {
                    connectOrDisconnect(false, mCurrentBluetoothDevice);
                }
            });
        }
        exsitdialog.setButtonText(
                mContext.getString(R.string.common_btn_cancel),
                mContext.getString(R.string.common_btn_confirm));
        exsitdialog.show();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.control_layer :
                if(mIsConnected) {//已连上，点击时是断开
                    showPromptDialog(false);
                }else{//未连上，点击时是搜索
                    startOrStopScanBluetoothDevice(true);
                    refreshUIByConnectionState(this.CONNECTION_STATE_SEARCHING);
                }

                break;
            case R.id.btn_top_right :
                mSearchResults.clear();
                searchResultAdapter.notifyDataSetChanged();
                startOrStopScanBluetoothDevice(true);
                btn_top_right.setEnabled(false);
                break;

        }
    }

    @Override
    public void onBack(View v) {
        Intent intent = new Intent();
        intent.putExtra("isConnected", mIsConnected);
        intent.putExtra("device", mCurrentBluetoothDevice);
        setResult(RESULT_OK, intent);
        super.onBack(v);

    }

    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent event) {

        if(KeyCode == event.KEYCODE_BACK){
            onBack(null);
            return false;
        }

        return super.onKeyDown(KeyCode, event);
    }



}
