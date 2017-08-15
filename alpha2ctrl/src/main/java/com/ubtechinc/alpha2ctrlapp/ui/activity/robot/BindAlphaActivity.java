package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.MessageType;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAuthorizeDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAuthorizeReponsitory;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.User;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.widget.RoundImageView;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.alpha2ctrlapp.widget.popWindow.BindGuidePopView;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.List;

/**
 * @ClassName BindAlphaActivity
 * @date 6/7/2017
 * @author tanghongyu
 * @Description 绑定机器人
 * @modifier
 * @modify_time
 */
public class BindAlphaActivity extends BaseContactActivity {
	private String TAG = "BindAlphaActivity";
	private TextView userName, alphaNo, title;
	private RoundImageView userHeader;
	private Dialog errorCodeDialog;
//	private NumInputsView  inputView;
	private String robotId;
	private Button btn_skip,btn_redo;
	private TextView btn_top_right;
	private int time;
	private ContacterReceiver receiver = null;
	private TextView errorMessage;
	private boolean mAdd = false;
	private String boundJid;
	private static final int MSG =0;
	private boolean isLogin  =false;
	private BindGuidePopView guideView ;
	private  String robotMac="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bound_alpha);
		initView();
	}

	private void initView() {
        mAdd = getIntent().getBooleanExtra("add", false);
		userName = (TextView) findViewById(R.id.user_name);
		alphaNo = (TextView) findViewById(R.id.alpha_id);
		robotId = getIntent().getStringExtra(PreferenceConstants.ROBOT_SERIAL_NO);
		userName.setText(SPUtils.get().getString(
				PreferenceConstants.USER_NAME));
		alphaNo.setText(getIntent().getStringExtra(PreferenceConstants.ROBOT_SERIAL_NO));
		this.title = (TextView) findViewById(R.id.authorize_title);
		title.setText(getString(R.string.bind_alpha));
		userHeader = (RoundImageView) findViewById(R.id.user_header);
		LoadImage.LoadHeader(this, 0, userHeader, SPUtils.get().getString(PreferenceConstants.USER_IMAGE));
		LinearLayout btn_back = (LinearLayout)findViewById(R.id.btn_back);
		if(!mAdd){
			btn_back.setVisibility(View.GONE);
		}

		if(!SPUtils.get().getBoolean(Constants.VERSION_CODE+Constants.bind_guide, false)){
			guideView = new BindGuidePopView(this);
			guideView.show();
			SPUtils.get().put(Constants.VERSION_CODE+Constants.bind_guide, true);
		}
	}
	
	
	@Override
	public void onBack(View v){
		if(mAdd){
			this.finish();
		}else{
			
		}
	}
	private class AuthorizeHandler extends BaseHandler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// TODO Auto-generated method stub
			if (msg == null) {
				Logger.i(TAG, "handleMessage msg is null.");
				return;
			}
			switch (msg.what) {
			case MessageType.ALPHA_LOST_CONNECTED:
				Logger.d(TAG, "handleMessage ALPHA_LOST_CONNECTED.");
				
				if(errorCodeDialog!=null && errorCodeDialog.isShowing())
					errorCodeDialog.dismiss();
				isCurrentAlpha2MacLostConnection((String) msg.obj);
				break;
			case NetWorkConstant.RESPONSE_BINDED_SUCCESS:
				Logger.i("nxy", "已经绑定，登录");
//			/	NToast.shortToast(mActivity, R.string.bounded_success);
//				setResult(RESULT_OK);
//				BindAlphaActivity.this.finish();

				break;

			case NetWorkConstant.RESPONSE_SEARCH_SUCCESS:
				List<RobotInfo> list 	 = (List<RobotInfo>)msg.obj;
				boundJid = Tools.getJidByName(list.get(0).getUserId());
				break;


			case MSG:
				break;
			case NetWorkConstant.RESPONSE_UPDATE_FRIEND_SUCCESS:
				LoadingDialog.getInstance(mContext).dismiss();
				ToastUtils.showShortToast( R.string.bind_success);
				alphaConnectNet();
				break;

			}
		}
	}

	
		
	private class ContacterReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			User user = intent.getParcelableExtra(User.userKey);
//			Notice notice = (Notice) intent.getSerializableExtra(IntentConstants.NOTICE_MESSAGE);
			LoadingDialog.dissMiss();

		}
	}
//	private  void addUserReceive(User user) {
//		refreshList();
//	}

	private void errorCodeDialog(String message) {
		if(errorCodeDialog==null){
			errorCodeDialog = new Dialog(this);
			errorCodeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			errorCodeDialog.setContentView(R.layout.error_code_dialog);
			Window dialogWindow = errorCodeDialog.getWindow();
			errorMessage = (TextView)errorCodeDialog.findViewById(R.id.tips_message);
			errorMessage.setText(message);
			btn_skip = (Button)errorCodeDialog.findViewById(R.id.btn_skip);
			btn_redo = (Button)errorCodeDialog.findViewById(R.id.btn_redo);
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.CENTER);
			int screenWidth = this.getWindowManager().getDefaultDisplay()
					.getWidth();
			int screenHeight = this.getWindowManager().getDefaultDisplay()
					.getHeight();
			Resources resources = this.getResources();
			int resourceId = resources.getIdentifier("navigation_bar_height",
					"dimen", "android");
			if (resourceId > 0) {
				screenHeight = screenHeight
						- resources.getDimensionPixelSize(resourceId);// 减去底部操作栏的高度
			}
			// 设置窗口的大小
			Window window = this.getWindow();
			if (screenWidth < screenHeight) {
				lp.width = (int) (screenWidth * (7.0 / 10.0));
				lp.height = (int) (screenHeight * (4.0 / 10.0));
			} else {
				lp.width = (int) (screenWidth * (4.0 / 5.0));
				lp.height = (int) (screenHeight * (3.0 / 4.0));
			}
			dialogWindow.setAttributes(lp);
		
		
			btn_redo.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					errorCodeDialog.dismiss();
				}
			});
			btn_skip.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					errorCodeDialog.dismiss();
					skip(v);
				}
			});
		}
		errorCodeDialog.show();
		
		
	}
    
	public void skip(View v){
		if(!mAdd){
			Intent intent = new Intent(BindAlphaActivity.this, MainPageActivity.class);
			BindAlphaActivity.this.startActivity(intent);
		}
	
		BindAlphaActivity.this.finish();
	}
	private void alphaConnectNet(){
		Intent intent = new Intent(mContext,ConfigureRobotNetworkActivity.class);
		intent.putExtra(Constants.ROBOTSN, robotId);
		intent.putExtra(Constants.ROBOT_MAC,robotMac);
		this.startActivity(intent);
	}

	/**
	 * 跳转到打开提示打开机器网络配置界面
	 * @param v
     */
	public void getCode(View v) {
		PackageManager pkm = getPackageManager();
		boolean has_permission = (PackageManager.PERMISSION_GRANTED == pkm.checkPermission("android.permission.CAMERA", "com.ubtechinc.alpha2ctrlapp"));
		if (!has_permission) {
		    ToastUtils.showShortToast(getResources().getString(R.string.bind_open_camera_pemission));
		}else{
			Intent inte = new Intent();
//			inte.setClass(this, ScanQrActivity.class);
			inte.setClass(this, ZxingCodeActivity.class);
			inte.putExtra("showguide", SPUtils.get().getBoolean(Constants.VERSION_CODE+Constants.scan_guide, false));
			/*startActivityForResult(inte,
					Constat.BindingRequestCode);*/
			startActivity(inte);
			if(mAdd) {
				finish();
			}
		}

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.BindingRequestCode) {
			if (data != null) {

				String info = (String) data.getExtras().get(
						Constants.Result_text);

				if (info.equals(Constants.Result_other_way)) {
//					Intent inte = getIntent();
//					inte.putExtra("mAdd", mAdd);
//					inte.setClass(BindAlphaActivity.this,
//							BoudByNumsActivity.class);
//					BindAlphaActivity.this.startActivity(inte);
				} else {
					 Logger.i("nxy", "sao miao jieguo "+info);
					 if(info.split("robotSeq=").length==2){
						 robotId = info.split("robotSeq=")[1].trim();
					     bindRobot();
					 }else{
						 ToastUtils.showShortToast( R.string.bind_scan_robot_qr);
					 }
					
				}
			}
			if(!SPUtils.get().getBoolean(Constants.VERSION_CODE+Constants.scan_guide, false)){
				SPUtils.get().put(Constants.VERSION_CODE+Constants.scan_guide, true);
			}
		}
	}
	private void bindRobot(){
		LoadingDialog.getInstance(mContext).show();
		RobotAuthorizeReponsitory.getInstance().bindRobot(robotId, Alpha2Application.getAlpha2().getUserId(), new IRobotAuthorizeDataSource.BindRobotCallback() {
			@Override
			public void onSuccess(String macAddress) {

				robotMac = macAddress;
				RobotManagerService.getInstance().refreshDevices();
				LoadingDialog.dissMiss();
				ToastUtils.showShortToast( R.string.bind_success);
				alphaConnectNet();
			}

			@Override
			public void onFail(ThrowableWrapper e) {
				errorCodeDialog(e.getMessage());
			}
		});

	}

	
}
