package com.ubtechinc.alpha2ctrlapp.ui.fragment.robot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.broadcom.cooee.Cooee;
import com.ubtech.utilcode.utils.bt.ByteHexHelper;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.third.engine.AudioParam;
import com.ubtechinc.alpha2ctrlapp.third.engine.AudioPlayer;
import com.ubtechinc.alpha2ctrlapp.third.engine.IWM;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.ConfigureRobotNetworkActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.ConnectNetGuideAcitivty;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.MyDeviceActivity;
import com.ubtechinc.alpha2ctrlapp.util.PlayThread;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog.OnNegsitiveClick;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog.OnPositiveClick;

public class SendCooeeFragment extends BaseActivity implements PlayThread.PlayListener,OnPositiveClick, OnNegsitiveClick {
	private String name, psw, capa;
	private Button btn_reSend, btn_connectFailed;
	private boolean haSendCooee = false;
	private Thread mThread = null;
	private int  mLocalIp;
	private LinearLayout  sendingLay;
	private ImageView cooee_image;
	private int time;
	private TextView cooee_sends;
	int nHandler = 0;
	short[] pAudio;
	int[] pNAudio;
	private int frq = /** 19000 **/17000;
	private PlayerThread mPlayerThread;
	private int sendTimes = 0;
	private AudioPlayer mAudioPlayer;
	private RelativeLayout  sendImageLay;
	private ImageView disConnectImage;
	private TextView failed_tv;
	private TextView send_result_tv;
	private LinearLayout send_tipsLay;
	private TextView  pageSkipTv;
	public static final int SEND_INFO_SUCCESS =100110;
	private boolean isSending = true;
	public CommonDiaglog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_cooee);
		initView();
		registerBroadcastReceiver();
		
	}
	

	
	public void initView() {
		tv_title.setText(R.string.net_connect_title);
		btn_ignore.setVisibility(View.GONE);
		Bundle bundle = getIntent().getExtras();
		name = bundle.getString(Constants.WIFI_NAME);
		psw = bundle.getString(Constants.WIFI_PSW);
		capa = bundle.getString(Constants.WIFI_CAPABILITIY);
		mLocalIp= bundle.getInt("ip");
		Log.i("nxy", "name   " + name + "psw   " + psw);
		sendImageLay = (RelativeLayout)findViewById(R.id.send_imageLay);
		disConnectImage = (ImageView)findViewById(R.id.disConnectImage);
		btn_reSend = (Button)findViewById(R.id.btn_resend);
		btn_connectFailed = (Button)findViewById(R.id.btn_failed);
		sendingLay = (LinearLayout)findViewById(R.id.send_cooee_ing);
		cooee_image= (ImageView)findViewById(R.id.cooee_image);
		cooee_sends = (TextView)findViewById(R.id.cooee_sends);
		failed_tv = (TextView)findViewById(R.id.failed_tv);
		send_result_tv = (TextView)findViewById(R.id.send_result_tv);
		send_tipsLay = (LinearLayout)findViewById(R.id.send_tipsLay);
		pageSkipTv = (TextView)findViewById(R.id.page_skip_tips);
		dialog = new CommonDiaglog(this, true);
		dialog.setMessase(mContext.getString(R.string.connect_exist_send_net_info));
		dialog.setNegsitiveClick(this);
		dialog.setPositiveClick(this);
		dialog.setButtonText(mContext.getString(R.string.connect_btn_exist), mContext.getString(R.string.connect_btn_countine_wait));
		dialog.setTitle(mContext.getString(R.string.connect_sending_net_info));
		IWM.init();
		nHandler = IWM.JniCreate();
		initControlListener() ;
		send();
	}

	public void initControlListener() {

		btn_reSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				send();
			}
		});

		btn_connectFailed.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,ConnectNetGuideAcitivty.class);
				intent.putExtra("scanqr", true);
				mContext.startActivity(intent);
			}
		});
	}
	private Handler mHanlder = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch (msg.what) {
//			case SEND_FAILED:
//				sendFailed();
//				break;
//			case STOP_ANIM:
//				break;
//			case RESEND:
//				send();
//				break;
//			case WAIT:
//				anim.stop();
//				sendFailed();
//				break;
			case SEND_INFO_SUCCESS:
				Intent intent2 = new Intent(SendCooeeFragment.this, MyDeviceActivity.class);
				mContext.startActivity(intent2);
				((Alpha2Application)SendCooeeFragment.this.getApplication()).removeActivity();
				break;
			default:
				break;
			}
			
		}
	};

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(action.equals(IntentConstants.ACTION_ALPHA_CONNECTED_NET)){
//				Intent intent2 = new Intent(ConfigureRobotNetworkActivity.this, MyDeviceActivity.class);
//				mActivity.startActivity(intent2);
//				((Alpha2Application)ConfigureRobotNetworkActivity.this.getApplication()).removeActivity();
				sendSuccess();
				mHanlder.sendEmptyMessageDelayed(SEND_INFO_SUCCESS, 1000);
			}

		}

	};
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

	private void unRegisterReceiver() {
		this.unregisterReceiver(mReceiver);
	}
	/**
	 * 只需要传一个参数就可以,不需要太长
	 * @param strSecure
	 * @return
	 */
	public String getCapaType(String strSecure){
		String Type;
		
		if (strSecure.contains("WEP")){
			Type = "WEP";
		}else if (strSecure.contains("WPA2")){
			Type = "WPA2";
		}else if (strSecure.contains("WPA")){
			Type = "WPA";
		}else{
			Type = "none";
		}
		return Type;
	}
	/**
	 * 倒计时
	 */
 	CountDownTimer countdownTimer = new CountDownTimer(90 * 1000, 1000) {
		public void onTick(long millisUntilFinished) {
			isSending = true;
			time = (int) (millisUntilFinished / 1000);
			cooee_sends.setText( String.valueOf(time)+"s");
			int index_1 = cooee_sends.getText().toString().indexOf(String.valueOf(String.valueOf(time)+"s"));
			int index_2 = index_1+String.valueOf(String.valueOf(time)+"s").length();
			SpannableStringBuilder builder = new SpannableStringBuilder(cooee_sends.getText().toString());
			ForegroundColorSpan greenSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.find_psw_tip_color_red));
			builder.setSpan(greenSpan, index_1, index_2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			cooee_sends.setText(builder);
		}

		public void onFinish() {
			isSending = false;
			sendFailed();
			if (countdownTimer != null) {
				countdownTimer.cancel();
			}
			 haSendCooee = false;
	         if(mThread!=null)  
	        	 mThread = null;

		}
	};
	
	
	private void send(){
		//zdy
		byte len = (byte) ConfigureRobotNetworkActivity.robotSn.length();
		final String pswrobotsn = psw+ ConfigureRobotNetworkActivity.robotSn+ ByteHexHelper.byteToHexString(len);
		
		 if (!haSendCooee) {
			 sendVoice();//发送声波
			 haSendCooee = true;
            /* set packet interval */
            Cooee.SetPacketInterval(20); /* 10ms */

            if (mThread == null) {
                mThread = new Thread() {
                    public void run() {
                        while (haSendCooee) {
                            //Cooee.send(ssid, password, mLocalIp, "0123456789abcdef");
                            Cooee.send(name, pswrobotsn, mLocalIp);
//                        	Logger.i("sending", "sending cooee");
                            //Cooee.send(ssid, password);
                        }
                    }
                };
            }

            mThread.start();
            init();
        } else {
       	 haSendCooee = false;
            mThread = null;
        }
	}
	public void sendVoice() {
		startWait();
		pAudio = new short[288000];
		pNAudio = new int[1];

		String name1="@"+name;
		String psw1= "#" + psw;
		String capa1="%" + getCapaType(capa);
		String sn = "*"+ ConfigureRobotNetworkActivity.robotSn ;
		String newName =name1+ ByteHexHelper.byteToHexString(CalcCheck(name1.getBytes(), name1.getBytes().length));
		String newPsw = psw1+ ByteHexHelper.byteToHexString(CalcCheck(psw1.getBytes(), psw1.getBytes().length));
		String newCapa= capa1+ ByteHexHelper.byteToHexString(CalcCheck(capa1.getBytes(), capa1.getBytes().length));
		String newSn= sn+ ByteHexHelper.byteToHexString(CalcCheck(sn.getBytes(), sn.getBytes().length));
		Log.i("nxy", "name = " + newName + "  psw = " + newPsw + " capa = " + newCapa);
		String[] ss = { newName, newPsw, newCapa,newSn};
		sendTimes = 0;
		mPlayerThread = new PlayerThread(ss);
		if (mAudioPlayer == null) {
			initAudioPlayer();
		}
		mPlayerThread.start();
	}
	/**
	 * 校验正确性
	 * @param Bytes
	 * @param len
	 * @return
	 */
	private byte CalcCheck(byte[] Bytes,int len)
	{
		byte i, result = 0;
		for (result = Bytes[0], i = 1; i < len ; i++)
		{
			result ^= Bytes[i];
		}
		
		return result;
	}

	public void sendFailed(){
		btn_reSend.setVisibility(View.VISIBLE);
		btn_connectFailed.setVisibility(View.VISIBLE);
		sendingLay.setVisibility(View.GONE);
		System.gc();
		cooee_image.clearAnimation();
		sendImageLay.setVisibility(View.GONE);
		disConnectImage.setImageResource(R.drawable.lianjieshibai);
		disConnectImage.setVisibility(View.VISIBLE);
		failed_tv.setVisibility(View.VISIBLE);
	}
	public void sendSuccess(){
		disConnectImage.setImageResource(R.drawable.connect_net_success);
		disConnectImage.setVisibility(View.VISIBLE);
		send_result_tv.setText(R.string.connect_net_success_tips);
		send_tipsLay.setVisibility(View.VISIBLE);
		pageSkipTv.setVisibility(View.GONE);
	}
	private void init(){
		btn_reSend.setVisibility(View.GONE);
		btn_connectFailed.setVisibility(View.GONE);
		sendingLay.setVisibility(View.VISIBLE);
		send_result_tv.setText(R.string.net_connecting);
		send_tipsLay.setVisibility(View.VISIBLE);
		pageSkipTv.setVisibility(View.GONE);
		cooee_image.setImageDrawable(getResources().getDrawable(R.drawable.sending_icon));
		Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.turn_around_anim);
		operatingAnim.setInterpolator(new LinearInterpolator());
		cooee_image.startAnimation(operatingAnim);
		sendImageLay.setVisibility(View.VISIBLE);
		disConnectImage.setVisibility(View.GONE);
		failed_tv.setVisibility(View.GONE);
//		cooee_image.setBackground(getResources().getDrawable(R.drawable.lainjie));
	}
	private void startWait(){
//		if(mHanlder.hasMessages(SEND_FAILED)){
//			mHanlder.removeMessages(SEND_FAILED);
//		}
//		mHanlder.sendEmptyMessageDelayed(SEND_FAILED, 1000*60);
		countdownTimer.start();
	}
	public void startPlay(byte[] data) {
		mAudioPlayer.setDataSource(data);

		// 音频源就绪
		mAudioPlayer.prepare();
		mAudioPlayer.play();
	}
	public class PlayerThread extends Thread {
		private boolean isLooper;
		private int index;
		private boolean isPlaying;
		private boolean isEnd;

		public boolean isPlaying() {
			return isPlaying;
		}

		public void setPlaying(boolean isPlaying) {
			this.isPlaying = isPlaying;
		}

		// private PlayThread play;
		private String[] s;

		public PlayerThread(String[] s) {
			this.s = s;
		}

		public void stopPlay() {
			this.isLooper = true;
			if (mPlayerThread.isPlaying()) {

			}
			if (mAudioPlayer != null) {
				mAudioPlayer.release();
			}
			this.interrupt();
		}

		public void run() {
			while (!isLooper) {
				if (!isEnd) {
					if (mPlayerThread.isPlaying()) {
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						mPlayerThread.setPlaying(true);
						IWM.GenAudio(s[index], s[index++].length(), pAudio,
								pNAudio, frq, 500);
						// play = new PlayThread(pAudio, pNAudio[0], 100,
						// nHandler, SendVoiceFragment.this);
						startPlay(decodeData(pAudio, pNAudio[0]));
						if (index == 4)
							index = 0;
						sendTimes++;
						if (sendTimes == 12) {
							isEnd = true;
						}
					}

				}
			}
		}
	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		mPlayerThread.setPlaying(false);

		if (sendTimes == 12) {
			Log.i("nxy", "3次完整的发送完成");

//			mHanlder.sendEmptyMessage(STOP_ANIM);
		}// else if(sendTimes%3==0){
			// Log.i("nxy", " 次完整的发送完成");
		// uIHandler.sendEmptyMessage(RESEND);
		// }
	}

	public void initAudioPlayer() {
		mAudioPlayer = new AudioPlayer(mHanlder);
		mAudioPlayer.setListener(this);
		// 获取音频参数
		AudioParam audioParam = getAudioParam();
		mAudioPlayer.setAudioParam(audioParam);
	}

	/*
	 * 获得PCM音频数据参数
	 */
	public AudioParam getAudioParam() {
		AudioParam audioParam = new AudioParam();
		audioParam.mFrequency = 48000;
		audioParam.mChannel = AudioFormat.CHANNEL_CONFIGURATION_MONO;
		audioParam.mSampBit = AudioFormat.ENCODING_PCM_16BIT;

		return audioParam;
	}

	public byte[] decodeData(short[] b, int lens) {
		byte[] buffer = new byte[lens * 2];
		int i, j;
		for (i = 0, j = 0; i < lens; i++, j += 2) {

			buffer[j + 0] = (byte) (b[i] >> 0);
			buffer[j + 1] = (byte) (b[i] >> 8);
		}
		return buffer;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mReceiver!=null)
			unRegisterReceiver();
		cooee_image.clearAnimation();
		haSendCooee = false;
        mThread = null;
        if (mPlayerThread != null) {
			mPlayerThread.stopPlay();
		}
        if(countdownTimer!=null)
        	countdownTimer.cancel();
	}
	
	@Override
	public void onBack(View v){
		if(isSending){
			dialog.show();
			
		}else{
			SendCooeeFragment.this.finish();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			if(isSending){
				dialog.show();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void OnNegsitiveClick() {
		// TODO Auto-generated method stub
		SendCooeeFragment.this.finish();
	}

	@Override
	public void OnPositiveClick() {
	
	}
}
