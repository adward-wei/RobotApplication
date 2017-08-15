package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.JsonUtils;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.constants.NetWorkConstant;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAuthorizeDataSource;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.WifiBean;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.SingleButtonDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class QrcodeActivity extends BaseActivity implements SingleButtonDialog.SingleButtonOnPositiveClick, SingleButtonDialog.SingleButtonOnNegativeClick, CommonDiaglog.OnPositiveClick, CommonDiaglog.OnNegsitiveClick {
    private TextView btn_go_voice_guide;
    private String name, psw, capa;
    private ImageView codeImage;
    private Bitmap bmpDefaultPic = null;
    private boolean hasCreateCode = false;
    private SingleButtonDialog diglog;

    private Button btn_reSend;
    private LinearLayout sendingLay;
    private int time;
    private TextView cooee_sends;
    private TextView failed_tv;
    private TextView send_result_tv;
    private LinearLayout send_tipsLay;
    private TextView pageSkipTv;
    public static final int SEND_INFO_SUCCESS = 100110;
    private TextView go_to_net_guide;
    private LinearLayout resendLay;
    public CommonDiaglog exsitdialog;
    private boolean isSending = true;
    private boolean hasShowDialog = false;
    private boolean isConnecting = false;
    public static final int GO_TO_CHECK_STATUS = 100111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanqr);
        registerBroadcastReceiver();
        tv_title.setText(R.string.qr_scan);
        btn_ignore.setVisibility(View.VISIBLE);
        initView();
        initControlListener();
        //gq();

    }

    public void initView() {
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString(Constants.WIFI_NAME);
        psw = bundle.getString(Constants.WIFI_PSW);
        capa = bundle.getString(Constants.WIFI_CAPABILITIY);
        Log.i("nxy", "name" + name + "psw" + psw);
        codeImage = (ImageView) findViewById(R.id.image_view);
//		codeImage.setVisibility(View.INVISIBLE);
        btn_go_voice_guide = (TextView) findViewById(R.id.btn_go_guide);
        diglog = new SingleButtonDialog(this);
        diglog.setPositiveClick(this);
        diglog.setNegtiveClick(this);
        btn_reSend = (Button) findViewById(R.id.btn_resend);
        failed_tv = (TextView) findViewById(R.id.failed_tv);
        sendingLay = (LinearLayout) findViewById(R.id.send_cooee_ing);
        cooee_sends = (TextView) findViewById(R.id.cooee_sends);
        send_result_tv = (TextView) findViewById(R.id.send_result_tv);
        send_tipsLay = (LinearLayout) findViewById(R.id.send_tipsLay);
        pageSkipTv = (TextView) findViewById(R.id.page_skip_tips);
        go_to_net_guide = (TextView) findViewById(R.id.go_to_net_guide);
        resendLay = (LinearLayout) findViewById(R.id.resendLay);
        exsitdialog = new CommonDiaglog(this, true);
        exsitdialog.setMessase(mContext.getString(R.string.connect_exist_send_net_info));
        exsitdialog.setNegsitiveClick(this);
        exsitdialog.setPositiveClick(this);
        exsitdialog.setButtonText(mContext.getString(R.string.connect_btn_exist), mContext.getString(R.string.connect_btn_countine_wait));
        exsitdialog.setTitle(mContext.getString(R.string.connect_sending_net_info));
        btn_ignore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QrcodeActivity.this, MyDeviceActivity.class);
                startActivity(intent);
                ((Alpha2Application) getApplication()).removeActivity();

            }
        });
    }

    /**
     * 用于提示的广播
     *
     * @author zengdengyi
     * @date 2015-1-13 下午1:36:59
     */
    private void registerBroadcastReceiver() {
        // 注册 广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentConstants.ACTION_ALPHA_CONNECTED_NET);
        this.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(IntentConstants.ACTION_ALPHA_CONNECTED_NET) && !isConnecting && isSending) {
                sendSuccess();
            }
        }

    };


    private BaseHandler mHanlder = new BaseHandler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_INFO_SUCCESS:
                    Intent intent2 = new Intent(QrcodeActivity.this, MyDeviceActivity.class);
                    mContext.startActivity(intent2);
                    ((Alpha2Application) QrcodeActivity.this.getApplication()).removeActivity();
                    break;
                case NetWorkConstant.RESPONSE_DEVICE_STATUS:
                    String status = (String) msg.obj;
                    doCheckInfo(status);
                    break;
                case GO_TO_CHECK_STATUS:
//                    CheckDeviceStatus task = new CheckDeviceStatus(mActivity, mHanlder, ConfigureRobotNetworkActivity.robotjid);
//                    task.execute();
                    break;
                default:
                    break;
            }

        }
    };

    /**
     * 处理查询在线响应
     *
     * @param status
     */
    private void doCheckInfo(String status) {
        if (status.equals("1") && !isConnecting && isSending) {
            sendSuccess();
        } else if (status.equals("2")) {
            mHanlder.sendEmptyMessageDelayed(GO_TO_CHECK_STATUS, 5000);//  收到不在线的响应继续过5s 请求
        }
    }

    public void initControlListener() {
        // TODO Auto-generated method stub
        btn_go_voice_guide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QrcodeActivity.this, ScanQrVoiceGuideActivity.class);
                QrcodeActivity.this.startActivity(intent);
            }
        });
        go_to_net_guide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QrcodeActivity.this, ConnectNetGuideAcitivty.class);
                QrcodeActivity.this.startActivity(intent);
            }
        });

        btn_reSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                init();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isSending) {
                    exsitdialog.show();
                } else {
                    QrcodeActivity.this.finish();
                }
            }
        });
    }


    @Override

    public void onResume() {
        super.onResume();
        createQRCode();
        if (hasCreateCode) {
            //getCodeImage(EncodeActivity.qRText,hasCreateCode);
        }
    }

    public void gq() {
        WifiBean bean = new WifiBean();
        bean.setName(name);
        bean.setPwd(psw);
        bean.setCap(capa);
        bean.setNum(ConfigureRobotNetworkActivity.robotSn);
        String x = JsonUtils.object2Json(bean).replace(" ", "");
        String xx = x.replace("\r\n", "");
//		if(!getCodeImage(xx, true)){// 先根据要传的参数来查询是否已经保存了上次的二维码的临时文件
//		//	encodeBarcode("TEXT_TYPE", xx); // 没有就去生成
//		}

    }

    private void createQRCode() {
        WifiBean bean = new WifiBean();
        bean.setName(name);
        bean.setPwd(psw);
        bean.setCap(capa);
        bean.setNum(ConfigureRobotNetworkActivity.robotSn);
        String x = JsonUtils.object2Json(bean).replace(" ", "");
        String xx = x.replace("\r\n", "");
        Log.i("nxy", "wifiInfo = " + xx);
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        final String finalWifiInfo = xx;
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(finalWifiInfo, BGAQRCodeUtil.dp2px(mContext, 160), Color.BLACK);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    codeImage.setImageBitmap(bitmap);
                } else {
                    ToastUtils.showShortToast( getString(R.string.encode_code_fail));
                }
            }
        }.execute();
    }

    //	private void encodeBarcode(CharSequence type, CharSequence data) {
//		hasCreateCode = true;
//		IntentIntegrator integrator = new IntentIntegrator(this);
//		List<String> tar = new ArrayList<String>();
//		EncodeActivity.isHistroy =false;
//		EncodeActivity.qRText =data.toString();
//		String pkName = getPackageName();
//		Log.i("nxy", "pack"+pkName);
//		tar.add(pkName);
//		integrator.setTargetApplications(tar);
//		integrator.shareText(data, type);
//	}
//	private boolean getCodeImage(String data,boolean hasCreateCode){
//        String filename=String.valueOf(data.hashCode());
//        File file = new File(Constants.PIC_RECEIVE_TEMP, filename);
//		//得到外部存储卡的路径
//		if(hasCreateCode){
//			 if(file.exists()){
//			    Log.i("nxy", "QrcodeFragement文件存在"+file.getAbsolutePath());
//				bmpDefaultPic = BitmapFactory.decodeFile(file.getAbsolutePath());
//					if(bmpDefaultPic!=null){
//					    Log.i("nxy", "读图成功");
//					    codeImage.setImageBitmap(bmpDefaultPic);
//					    if(diglog!=null&&!hasShowDialog){
//					    	diglog.show();
//					    	hasShowDialog = true;
//					    }
//					    return true;
//					 }
//
//			  }
//		}
//		return false;
//	}
    private void init() {
        btn_reSend.setVisibility(View.GONE);
        sendingLay.setVisibility(View.VISIBLE);
        send_result_tv.setVisibility(View.GONE);
        send_tipsLay.setVisibility(View.VISIBLE);
        pageSkipTv.setVisibility(View.GONE);
        failed_tv.setVisibility(View.GONE);
        btn_go_voice_guide.setVisibility(View.VISIBLE);
        countdownTimer.start();
        resendLay.setVisibility(View.GONE);
        go_to_net_guide.setVisibility(View.GONE);
        isSending = true;
        isConnecting = false;
        mHanlder.sendEmptyMessageDelayed(GO_TO_CHECK_STATUS, 5000);// 隔5s 查询在线状态
    }

    public void sendFailed() {
        isSending = false;
        btn_reSend.setVisibility(View.VISIBLE);
        sendingLay.setVisibility(View.GONE);
        failed_tv.setVisibility(View.VISIBLE);
        failed_tv.setText(R.string.net_connected_failed);
        btn_go_voice_guide.setVisibility(View.GONE);
        send_tipsLay.setVisibility(View.GONE);
        send_result_tv.setVisibility(View.VISIBLE);
//		send_result_tv.setText(R.string.net_connected_failed);
        resendLay.setVisibility(View.VISIBLE);
        pageSkipTv.setVisibility(View.GONE);
        go_to_net_guide.setVisibility(View.VISIBLE);
    }

    /**
     * 机器人上线之后自动连接，如果前面有连接的先断开再连接
     */
    public void sendSuccess() {
        isSending = false;
        send_result_tv.setVisibility(View.VISIBLE);
        sendingLay.setVisibility(View.GONE);
//		send_result_tv.setText(R.string.connect_net_success_tips);
        send_tipsLay.setVisibility(View.GONE);
        pageSkipTv.setVisibility(View.VISIBLE);
        failed_tv.setVisibility(View.VISIBLE);
        failed_tv.setText(R.string.connect_net_success_tips);
        btn_go_voice_guide.setVisibility(View.GONE);
        if (SPUtils.get().getBoolean(PreferenceConstants.OPEN_AUTO_CONNECT, true)) {
            RobotManagerService.getInstance().connectRobot(ConfigureRobotNetworkActivity.robotSn, new IRobotAuthorizeDataSource.IControlRobotCallback() {
                @Override
                public void onSuccess() {
                    LoadingDialog.dissMiss();
                    goMainPage();
                }

                @Override
                public void onFail(ThrowableWrapper e) {
                    LoadingDialog.dissMiss();
                    ToastUtils.showShortToast(R.string.bt_connect_fail);
                }
            });
            if (countdownTimer != null) {
                countdownTimer.cancel();
            }
            isConnecting = true;
        } else {
            mHanlder.sendEmptyMessageDelayed(SEND_INFO_SUCCESS, 2000);
            isConnecting = true;
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
     * 倒计时
     */
    CountDownTimer countdownTimer = new CountDownTimer(90 * 1000, 1000) {
        public void onTick(long millisUntilFinished) {
            time = (int) (millisUntilFinished / 1000);
            cooee_sends.setText(String.valueOf(time) + "s");
            int index_1 = cooee_sends.getText().toString().indexOf(String.valueOf(String.valueOf(time) + "s"));
            int index_2 = index_1 + String.valueOf(String.valueOf(time) + "s").length();
            SpannableStringBuilder builder = new SpannableStringBuilder(cooee_sends.getText().toString());
            ForegroundColorSpan greenSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.find_psw_tip_color_red));
            builder.setSpan(greenSpan, index_1, index_2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            cooee_sends.setText(builder);

        }

        public void onFinish() {
            sendFailed();
            if (countdownTimer != null) {
                countdownTimer.cancel();
            }

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if (isSending) {
                exsitdialog.show();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void OnSingleButtonPositiveClick() {
        // TODO Auto-generated method stub
        init();
    }

    private void unRegisterReceiver() {
        this.unregisterReceiver(mReceiver);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mReceiver != null)
            unRegisterReceiver();
        if (countdownTimer != null)
            countdownTimer.cancel();
    }

    @Override
    public void OnNegsitiveClick() {
        // TODO Auto-generated method stub
        QrcodeActivity.this.finish();
    }

    @Override
    public void OnPositiveClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSingleButtonOnNegativeClick() {
        // TODO Auto-generated method stub
        QrcodeActivity.this.finish();
    }

}
