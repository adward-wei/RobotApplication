package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.JsonUtils;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAuthorizeDataSource;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.WifiBluthoothMode;
import com.ubtechinc.alpha2ctrlapp.events.RobotControlEvent;
import com.ubtechinc.alpha2ctrlapp.events.RobotRefreshEvent;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;
import com.ubtechinc.alpha2ctrlapp.util.bluetooth.BluetoothChatService;
import com.ubtechinc.alpha2ctrlapp.util.bluetooth.BluetoothManager;
import com.ubtechinc.alpha2ctrlapp.util.grant.PermissionsManager;
import com.ubtechinc.alpha2ctrlapp.util.grant.PermissionsResultAction;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.utils.WifiControl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;


public class InputWifiActivity extends BaseActivity implements OnClickListener , TextView.OnEditorActionListener {

	private ImageView btn_scanWifi;
	private TextView btn_send;
    private EditText  nameEd, pswEd ;
    private CheckBox  btn_showPsw;
    private int currentModel = 0;
    private WifiManager wifiManager;
    private int  mLocalIp;
    private boolean needPsw =true;
	private static final String TAG="RobotManagerl";
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothChatService mblueChatService;
	private WifiBluthoothMode wifiSendmode =null ;
	private boolean isSendWifi = false;// 是否正在进行蓝牙操作
	private static final int MSG_TO_QRCODE=1010;// 发完数据开始10s计时，没收到数据就开始二维码
	private static final int MSG_CHECK_BLUETHOOTH=1011;// 启动蓝牙命令开始，如果没有开启蓝牙就默认进入二维码
	private static final int REQUEST_START_BLUETOOTH=1001;// 启动蓝牙
	private static final int GO_CONTROL_ALPHA=1002;// 连接机器人
	private boolean hasSendQrCode;
	private TextView errorInfoTv;
	private  static final int CHECK_DIALOG = 1003;
	private boolean isWaiting =false;
	private boolean isOnFront =false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_wifi);
		initView();
		initControlListener();
		registReceive();
		EventBus.getDefault().register(this);
	}

	public void initView() {
		setTitle(R.string.set_wifi_title);
		wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		btn_scanWifi = (ImageView) findViewById(R.id.btn_scanwifi);
		btn_send = (TextView) findViewById(R.id.btn_open_or_send);
		btn_showPsw = (CheckBox)findViewById(R.id.btn_checkbox);
		nameEd = (EditText) findViewById(R.id.input_name);
		pswEd = (EditText) findViewById(R.id.input_psw);
		nameEd.setText(SPUtils.get().getString(Constants.WIFI_NAME, ""));
		pswEd.setText(SPUtils.get().getString(Constants.WIFI_PSW, ""));
		String path=Environment.getExternalStorageDirectory().toString();
		File file = new File(path,"core_image.png");
		mblueChatService = new BluetoothChatService(this, mHandler);
		if(file.exists()){
			file.delete();
		}
		refreshMode(0);
		WifiManager wifiManager = (WifiManager)  mContext.getSystemService(Context.WIFI_SERVICE);
	    WifiInfo info = wifiManager.getConnectionInfo();
	    mLocalIp = info.getIpAddress();
	    if(!TextUtils.isEmpty(pswEd.getText())){
			if (!TextUtils.isEmpty(nameEd.getText())) {
				btn_send.setBackgroundResource(R.drawable.btn_button_able);
			}else{
				btn_send.setBackgroundResource(R.drawable.button_disable);
			}
		}else{
			btn_send.setBackgroundResource(R.drawable.button_disable);
		}
		errorInfoTv = (TextView)findViewById(R.id.error_wifi_tips);
	}

	public void initControlListener() {
		// TODO Auto-generated method stub

		btn_scanWifi.setOnClickListener(this);
		btn_send.setOnClickListener(this);

		btn_showPsw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(btn_showPsw.isChecked())
					pswEd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				else{
					pswEd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});
		nameEd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				// TODO Auto-generated method stub	
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(!TextUtils.isEmpty(pswEd.getText())){
					if (!TextUtils.isEmpty(nameEd.getText())) {
						btn_send.setBackgroundResource(R.drawable.btn_button_able);
					}else{
						btn_send.setBackgroundResource(R.drawable.button_disable);
					}
				}else{
					btn_send.setBackgroundResource(R.drawable.button_disable);
				}
			}
		});
		nameEd.setOnEditorActionListener(this);
		pswEd.setOnEditorActionListener(this);
		pswEd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub	
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				if(!TextUtils.isEmpty(pswEd.getText().toString())||!needPsw){
						if (!TextUtils.isEmpty(nameEd.getText())) {
							btn_send.setBackgroundResource(R.drawable.btn_button_able);

						}else{
							btn_send.setBackgroundResource(R.drawable.button_disable);
						}

				}else{
					btn_send.setBackgroundResource(R.drawable.button_disable);
				}
			}
		});
		btn_ignore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InputWifiActivity.this, MyDeviceActivity.class);
				startActivity(intent);
				((Alpha2Application)getApplication()).removeActivity();

			}
		});
		pswEd.setOnEditorActionListener(this);

		nameEd.setOnEditorActionListener(this);

	}
	@Override
	public void onBack(View v){
		this.finish();

	}
	@Override
	public void onPause(){
		super.onPause();
		isOnFront = false;
	}
	@Override
	public void onResume(){
		super.onResume();
		isWaiting =false;
		isOnFront = true;
		LoadingDialog.dissMiss();
		needPsw = true;
		if(!TextUtils.isEmpty(Constants.ssID)){
			nameEd.setText(Constants.ssID);
			if (!Constants.WIFI_CAPA.contains("WPA")
						&&!Constants.WIFI_CAPA.contains("wpa")
						&&!Constants.WIFI_CAPA.contains("WEP")
						&&!Constants.WIFI_CAPA.contains("wep")
						&&!Constants.WIFI_CAPA.contains("EAP")
						&&!Constants.WIFI_CAPA.contains("EAP")){
				needPsw =false;  // 公共WiFi
				btn_send.setBackgroundResource(R.drawable.btn_button_able);
			}else{
				needPsw =true;
			}
		}else{
			if(!TextUtils.isEmpty(nameEd.getText().toString())){
				String capabilities;
				WifiControl controls =  WifiControl.get(this);
				List<ScanResult> scanlist = controls.getScanResultForList();
				for (ScanResult r : scanlist) {
					if (r.SSID.equals(nameEd.getText().toString())) {
						capabilities = r.capabilities;
						if(!capabilities.contains("WPA")
								&&!capabilities.contains("wpa")
								&&!capabilities.contains("WEP")
								&&!capabilities.contains("wep")
								&&!capabilities.contains("EAP")
								&&!capabilities.contains("EAP")){
							needPsw = false;
							btn_send.setBackgroundResource(R.drawable.btn_button_able);
						}else{
							needPsw = true;
						}
					}
				}
			}else{
				btn_send.setBackgroundResource(R.drawable.button_disable);
			}
		}
		if(!needPsw){
			pswEd.setEnabled(false);
			btn_showPsw.setVisibility(View.GONE);
			needPsw = false;
			pswEd.setText("");
			pswEd.setHint(R.string.wifi_free_wifi);
		}else{
			needPsw = true;
			pswEd.setEnabled(true);
			btn_showPsw.setVisibility(View.VISIBLE);
			if(!nameEd.getText().toString().equals(SPUtils.get().getString(Constants.WIFI_NAME, ""))){
				if(pswEd.getText().toString().equals(SPUtils.get().getString(Constants.WIFI_PSW, ""))){
					pswEd.setText("");
				}
			}
		}
		mHandler.removeMessages(CHECK_DIALOG);


	}
	private void refreshMode(int type){
		if(type == currentModel)
			return;
		currentModel = type;
		if(type == 0){
			nameEd.setEnabled(true);
			pswEd.setEnabled(true);
			btn_send.setText(R.string.common_btn_next);
			if (!wifiManager.isWifiEnabled()) {
		        wifiManager.setWifiEnabled(true);
		    }
		}
	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_scanwifi:{
			Intent intent = new Intent(this, WifiActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.btn_open_or_send:
			hasSendQrCode = false;
			goQrConnect(0,false);
			break;
		case R.id.choose_other:
			Intent intent = new Intent(InputWifiActivity.this,ConnectNetGuideAcitivty.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	/**
	 * 发送数据
	 * @param type 0表示wifi ,1表示热点
	 * @param isQrCode true 用二维码方式，false 是蓝牙
     */
	private void goQrConnect(int type,boolean isQrCode){
        String capa ="";
		if(TextUtils.isEmpty(nameEd.getText().toString())){
			ToastUtils.showShortToast( R.string.wifi_name_isempty);

			return;
		}

		if(nameEd.getText().toString().length()>32){
			ToastUtils.showShortToast( R.string.wifi_name_too_long);
			return;
		}

		if(pswEd.getText().toString().length()>32){
			ToastUtils.showShortToast( R.string.wifi_psw_too_long);
			return;
		}
		errorInfoTv.setVisibility(View.INVISIBLE);
		Bundle	bundle = new Bundle();
		if(currentModel == 0){
			WifiInfo info = wifiManager.getConnectionInfo();
			Logger.i("nx", "getBSSID"+info.getBSSID());
			String capabilities = "";
			WifiControl controls = WifiControl.get(this);
			List<ScanResult> scanlist = controls.getScanResultForList();
			for (ScanResult r : scanlist) {
				if (r.SSID.equals(nameEd.getText().toString())) {
					capabilities = r.capabilities;
					if(!capabilities.contains("WPA")
							&&!capabilities.contains("wpa")
							&&!capabilities.contains("WEP")
							&&!capabilities.contains("wep")
							&&!capabilities.contains("EAP")
							&&!capabilities.contains("EAP")){
						pswEd.setEnabled(false);
						pswEd.setText("");
						btn_showPsw.setVisibility(View.GONE);
						needPsw = false;
					}else{
						needPsw = true;
						pswEd.setEnabled(true);
						btn_showPsw.setVisibility(View.VISIBLE);
					}
				}
			}
			if(needPsw &&pswEd.getText().toString().length()<8){
				ToastUtils.showShortToast( R.string.wifi_psw_inccrect);
				return;
			}
				bundle.putString(Constants.WIFI_NAME, nameEd.getText().toString());
				if(needPsw)
					bundle.putString(Constants.WIFI_PSW, pswEd.getText().toString());
				else{
					bundle.putString(Constants.WIFI_PSW, "no_pass_word");
				}
				bundle.putString(Constants.WIFI_CAPABILITIY, capabilities);
			capa= capabilities;
				bundle.putInt("ip", mLocalIp);

		}else{
			if(TextUtils.isEmpty(pswEd.getText().toString())){
				ToastUtils.showShortToast( R.string.wifi_psw_isempty);
				return;
			}
			if(pswEd.getText().toString().length()<8){
				ToastUtils.showShortToast( R.string.wifi_psw_inccrect);
				return;
			}
			bundle.putString(Constants.WIFI_NAME, nameEd.getText().toString());
			bundle.putString(Constants.WIFI_PSW, pswEd.getText().toString());
			bundle.putString(Constants.WIFI_CAPABILITIY, "[WPA-PSK-TKIP+CCMP][ESS]");
			bundle.putInt("ip", mLocalIp);
			capa="[WPA-PSK-TKIP+CCMP][ESS]";

		}
		SPUtils.get().put(Constants.WIFI_NAME, nameEd.getText().toString());
		SPUtils.get().put(Constants.WIFI_PSW, pswEd.getText().toString());

		// 取到mac地址就直接用蓝牙连接
		if(!StringUtils.isEmpty(ConfigureRobotNetworkActivity.robotMac)){
			if(!isQrCode){
				wifiSendmode = new WifiBluthoothMode();
				wifiSendmode.setName(nameEd.getText().toString());
				if (needPsw)
					wifiSendmode.setPwd(pswEd.getText().toString());
				else {
					wifiSendmode.setPwd("no_pass_word");
				}
				wifiSendmode.setCap(capa);
				wifiSendmode.setNum(ConfigureRobotNetworkActivity.robotSn);
				Logger.i(TAG,"点击按钮");
				connectBluetoothDevice();
				hasSendQrCode = false;
			}else if(!hasSendQrCode){// 避免接收到异常广播，重复弹框
				Intent intent  = new Intent(this, QrcodeActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				LoadingDialog.dissMiss();
				mHandler.removeMessages(MSG_TO_QRCODE);//移除计时
				hasSendQrCode = true;
			}
		}else{// 没有获取到Mac地址

			Intent intent  = new Intent(this, QrcodeActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			hasSendQrCode =true;
			LoadingDialog.dissMiss();
			mHandler.removeMessages(MSG_TO_QRCODE);//移除计时
		}



	}
	// wifi热点开关
    public boolean setWifiApEnabled(boolean enabled) {
        if (enabled) { // disable WiFi in any case
            //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
            wifiManager.setWifiEnabled(false);
        }
        try {
            //热点的配置类
            WifiConfiguration apConfig = new WifiConfiguration();
            //配置热点的名称(可以在名字后面加点随机数什么的)
            apConfig.SSID = nameEd.getText().toString();
            //配置热点的密码
            apConfig.preSharedKey=pswEd.getText().toString();
            apConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            apConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                //通过反射调用设置热点
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            //返回热点打开状态
            return (Boolean) method.invoke(wifiManager, apConfig, enabled);
        } catch (Exception e) {
            return false;
        }
    }
	private void  registReceive(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);  //发现新设备
		filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);  //绑定状态改变
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);  //开始扫描
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);  //结束扫描
		filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);  //连接状态改变
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);  //蓝牙开关状态改变
		filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
		registerReceiver(receiver, filter);
	}
	private BroadcastReceiver receiver = new BroadcastReceiver()  {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String strPsw = "000000";
			 if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
				Log.i(TAG, "开始扫描");
			} else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
				Log.i(TAG, "停止扫描");
			} else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				//蓝牙开关状态改变
				int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
				switch (state) {
					case BluetoothAdapter.STATE_OFF:
						Log.i(TAG, "蓝牙关闭");
						mblueChatService=null;
						break;
					case BluetoothAdapter.STATE_ON:
						Log.i(TAG, "蓝牙打开");
						connectBluetoothDevice();
						break;

				}
			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				switch (device.getBondState()) {
					case BluetoothDevice.BOND_BONDING:
						Log.d(TAG, "正在配对......");
						break;
					case BluetoothDevice.BOND_BONDED:
						Log.d(TAG, "完成配对");
						BluetoothManager.getInstance(context, device).connect(device);//连接设备
						mblueChatService.connect(device,false);
						break;
					case BluetoothDevice.BOND_NONE:
						Log.d(TAG, "取消配对");
					default:
						break;
				}
			}
		}
	};


	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(RobotControlEvent event) {
		switch (event.getAction()) {
			case RobotControlEvent.CONNECT_SUCCESS :
				((Alpha2Application)InputWifiActivity.this.getApplication()).removeActivity();
				break;
		}

	}

	/**
		 * The Handler that gets information back from the BluetoothChatService
		 */
		private BaseHandler mHandler = new BaseHandler() {
			@Override
			public void handleMessage(Message msg) {
					switch (msg.what) {
					case BluetoothChatService.MESSAGE_STATE_CHANGE:
						switch (msg.arg1) {
							case BluetoothChatService.STATE_CONNECTED:
								Logger.i(TAG,"连接上");
								if(wifiSendmode!=null && !TextUtils.isEmpty(wifiSendmode.getName()))
									sendWifiMessage(wifiSendmode);
								else{
									if(isSendWifi) goQrConnect(0,false);
								}
								break;
							case BluetoothChatService.STATE_CONNECTING:
								break;
							case BluetoothChatService.STATE_LISTEN:
							case BluetoothChatService.STATE_NONE:
								break;
						}
						break;
					case BluetoothChatService.MESSAGE_WRITE:
						byte[] writeBuf = (byte[]) msg.obj;
						// construct a string from the buffer
						String writeMessage = new String(writeBuf);
						Logger.i(TAG,"Me:  " + writeMessage);
						if(writeMessage.equals("600")){
							mblueChatService.stop(); // 发送完之后关闭蓝牙传输
							Logger.i("control","发送600");
						}
						break;
					case BluetoothChatService.MESSAGE_READ:
						byte[] readBuf = (byte[]) msg.obj;
						// construct a string from the valid bytes in the buffer
						String readMessage = new String(readBuf, 0, msg.arg1);
						if(readMessage.equals("400")){// 机器人端回复400表示已经上线
							isSendWifi = false;
							byte[] send = "600".getBytes();
							mblueChatService.write(send); // 发送600 ，通知机器人关掉蓝牙
							Logger.i("control","收到400");
							sendEmptyMessageDelayed(GO_CONTROL_ALPHA,2*1000);//延时两秒以便花名册的更新
							isWaiting= true;
							if(LoadingDialog.mDia!=null){
								if(!LoadingDialog.mDia.isShowing()){
									LoadingDialog.getInstance(mContext).show();
								}
							}else{
								LoadingDialog.getInstance(mContext).show();
							}
						}else if(readMessage.equals("800")){// 表示wifi 和密码发送错误
							isSendWifi = false;
							mblueChatService.stop();
							removeMessages(MSG_TO_QRCODE);
							removeMessages(MSG_CHECK_BLUETHOOTH);
							LoadingDialog.dissMiss();
							errorInfoTv.setVisibility(View.VISIBLE);
						}
						Logger.i(TAG,"device read" + ":  " + readMessage);

						break;
					case GO_CONTROL_ALPHA:

						RobotManagerService.getInstance().disConnect2Robot(ConfigureRobotNetworkActivity.robotSn, new IRobotAuthorizeDataSource.IControlRobotCallback() {
							@Override
							public void onSuccess() {

							}

							@Override
							public void onFail(ThrowableWrapper e) {

							}
						});

						isWaiting= false;
						 break;
					case BluetoothChatService.MESSAGE_DEVICE_NAME:
						// save the connected device's name
						String name = msg.getData().getString(BluetoothChatService.DEVICE_NAME);
//						ToastUtils.showShortToast("Connected to " + name);
						break;
					case BluetoothChatService.MESSAGE_TOAST:
						if(isSendWifi){
							goQrConnect(0,true);// 连接失败进入二维码方式
							ToastUtils.showShortToast( msg.getData().getString(BluetoothChatService.TOAST));
						}
						break;
					case MSG_TO_QRCODE:
						Logger.i(TAG,"计时");
						if(isSendWifi)
							goQrConnect(0,true);//进去二维码流程
						break;
					case  MSG_CHECK_BLUETHOOTH:
						if(bluetoothAdapter!=null && !bluetoothAdapter.isEnabled()){
							if(!hasSendQrCode){
								goQrConnect(0,true);
							}
						}
						break;
					case CHECK_DIALOG:

						if(hasMessages(CHECK_DIALOG)){
							removeMessages(CHECK_DIALOG);
						}
						if(isSendWifi||(!isSendWifi&&isWaiting)){
							if(isOnFront){
								if(LoadingDialog.mDia!=null){
									if(!LoadingDialog.mDia.isShowing()){
										LoadingDialog.getInstance(mContext).show();
									}
								}else{
									LoadingDialog.getInstance(mContext).show();
								}
							}
							sendEmptyMessageDelayed(CHECK_DIALOG,100);
						}
							break;
				}
			}
		};

	/**
	 * 连接蓝牙设备
	 */
	private void connectBluetoothDevice(){
		isSendWifi = true;
		Log.i(TAG, "连接设备");
		if(mblueChatService==null)
			mblueChatService= new BluetoothChatService(mContext,mHandler);
		if(mblueChatService.getState()==BluetoothChatService.STATE_NONE){
			mayRequestLocation();
			bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if(bluetoothAdapter.isEnabled()){
//				bluetoothAdapter.startDiscovery();
				// Get the BluetoothDevice object
				BluetoothDevice device = bluetoothAdapter.getRemoteDevice(ConfigureRobotNetworkActivity.robotMac);
				// Attempt to connect to the device
				mblueChatService.connect(device, false);
				LoadingDialog.getInstance(mContext).show();
			}else {
				if(bluetoothAdapter!=null){// 有蓝牙
					Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					if (Build.VERSION.SDK_INT >= 23) {
						startActivityForResult(intent,REQUEST_START_BLUETOOTH); // 5.0 需要处理蓝牙打开结果
					}else {
//						bluetoothAdapter.enable();// 强制打开蓝牙不弹框
						startActivity(intent); // 弹框
					}
					mHandler.sendEmptyMessageDelayed(MSG_CHECK_BLUETHOOTH,3*1000);
					LoadingDialog.getInstance(mContext).show();

				}else{
					goQrConnect(0,true);
				}

			}
		}else{
			if(mblueChatService.getState()==BluetoothChatService.STATE_CONNECTED){
				if(wifiSendmode!=null && !TextUtils.isEmpty(wifiSendmode.getName())){
					sendWifiMessage(wifiSendmode);
					LoadingDialog.getInstance(mContext).show();
				}
				else{
					goQrConnect(0,false);
				}
			}else{
//				BluetoothDevice device = bluetoothAdapter.getRemoteDevice("22:22:98:5A:73:65");
				BluetoothDevice device = bluetoothAdapter.getRemoteDevice(ConfigureRobotNetworkActivity.robotMac);
				mblueChatService.connect(device, false);
				LoadingDialog.getInstance(mContext).show();
			}
		}

	}

	/**
	 * 发送wifi 数据
	 * @param messageMode
     */
	private void sendWifiMessage(WifiBluthoothMode messageMode) {

		String message= JsonUtils.object2Json(messageMode);
		// Check that we're actually connected before trying anything
		if (mblueChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mblueChatService.write(send);
			if(mHandler.hasMessages(MSG_TO_QRCODE)){
				mHandler.removeMessages(MSG_TO_QRCODE);
			}
			Logger.i(TAG,"发送数据"+message);
			mHandler.sendEmptyMessageDelayed(MSG_TO_QRCODE,30*1000);
			mHandler.sendEmptyMessageDelayed(CHECK_DIALOG,100);
			isWaiting= false;
		}
	}


	/**
	 * 6.0以上的请求蓝牙权限
	 */
	private void mayRequestLocation(){
		PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
				new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionsResultAction() {

					@Override
					public void onGranted() {
						// 有权限
					}

					@Override
					public void onDenied(String permission) {
						Logger.w(TAG, "onDenied: Read ACCESS_COARSE_LOCATION");
						goQrConnect(0,true); //权限拒绝
					}
				}
		);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case 0:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Permission Granted
				} else {
					// Permission Denied
					goQrConnect(0,true);
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode== REQUEST_START_BLUETOOTH){
			if(resultCode == RESULT_CANCELED){
				hasSendQrCode = false;
				goQrConnect(0,true);
			}
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
		if(mblueChatService!=null){
			mblueChatService.stop();
			mblueChatService=null;
		}
     EventBus.getDefault().unregister(this);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		switch(actionId){

			case EditorInfo.IME_NULL:
				break;
			case EditorInfo.IME_ACTION_NEXT:
				this.pswEd.requestFocus();
				break;
			case EditorInfo.IME_ACTION_DONE:
				btn_send.callOnClick();
				break;
		}
		//Toast.makeText(this, v.getText()+"--" + actionId, Toast.LENGTH_LONG).show();
		return true;


	}




}
