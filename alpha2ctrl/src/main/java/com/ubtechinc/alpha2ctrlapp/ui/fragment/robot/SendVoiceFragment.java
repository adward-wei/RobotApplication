package com.ubtechinc.alpha2ctrlapp.ui.fragment.robot;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.third.engine.AudioParam;
import com.ubtechinc.alpha2ctrlapp.third.engine.AudioPlayer;
import com.ubtechinc.alpha2ctrlapp.third.engine.IWM;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.ConfigureRobotNetworkActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.ConnectNetGuideAcitivty;
import com.ubtechinc.alpha2ctrlapp.ui.activity.robot.MyDeviceActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseFragment;
import com.ubtechinc.alpha2ctrlapp.util.PlayThread;

public class SendVoiceFragment extends BaseFragment implements PlayThread.PlayListener {
	private TextView btn_rechoose;
	private String name, psw, capa;
	private Button btn_reSend, btn_connectFailed, btn_go;
	int nHandler = 0;
	short[] pAudio;
	int[] pNAudio;
	private int frq = /** 19000 **/
	17000;
	private PlayerThread mPlayerThread;
	private int sendTimes = 0;
	private AnimationDrawable anim;
	private ImageView voice1_image;
	private final int STOP_ANIM = 0;
	private final int RESEND = 1;
	private final int WAIT=2;
	private LinearLayout sendFailLay, sendingLay;
	private int time;
	private TextView voice_sends;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init(false, getString(R.string.sonic_send_voice));
		if(activity.btn_ignore!=null)
			activity.btn_ignore.setVisibility(View.VISIBLE);
		initView();
		initControlListener();
	}

	@Override
	public View onCreateFragmentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		return inflater.inflate(R.layout.send_voice, container, false);
	}

	@Override
	public void initView() {
		name = bundle.getString(Constants.WIFI_NAME);
		psw = bundle.getString(Constants.WIFI_PSW);
		capa = bundle.getString(Constants.WIFI_CAPABILITIY);
		sendFailLay = (LinearLayout)mContentView.findViewById(R.id.send_voice_fail_lay);
		sendingLay = (LinearLayout)mContentView.findViewById(R.id.send_voice_ing);
		Log.i("nxy", "name   " + name + "psw   " + psw);
		btn_rechoose = (TextView) getActivity().findViewById(R.id.choose_other);
		btn_reSend = (Button) getActivity().findViewById(R.id.btn_resend);
		btn_connectFailed = (Button) getActivity().findViewById(R.id.btn_failed);
		btn_go = (Button) getActivity().findViewById(R.id.btn_success);
		voice1_image = (ImageView) getActivity().findViewById(R.id.voice1_image);
		IWM.init();
		nHandler = IWM.JniCreate();
		voice_sends = (TextView)mContentView.findViewById(R.id.voice_sends);
		send();
	}
	/**
	 * 倒计时
	 */
 	CountDownTimer countdownTimer = new CountDownTimer(90 * 1000, 1000) {
		public void onTick(long millisUntilFinished) {

			time = (int) (millisUntilFinished / 1000);
			voice_sends.setText( String.valueOf(time)+"s");
			int index_1 = voice_sends.getText().toString().indexOf(String.valueOf(String.valueOf(time)+"s"));
			int index_2 = index_1+String.valueOf(String.valueOf(time)+"s").length();
			SpannableStringBuilder builder = new SpannableStringBuilder(voice_sends.getText().toString());
			ForegroundColorSpan greenSpan = new ForegroundColorSpan(mActivity.getResources().getColor(R.color.find_psw_tip_color_red));
			builder.setSpan(greenSpan, index_1, index_2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			voice_sends.setText(builder);
		}

		public void onFinish() {
			sendFailed();
			if (countdownTimer != null) {
				countdownTimer.cancel();
			}

		}
	};
	@Override
	public void initControlListener() {
		// TODO Auto-generated method stub
		btn_rechoose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated metho
				clearFragment(SendVoiceFragment.class.getName());
				if (bundle == null)
					bundle = new Bundle();
				replaceFragment(OpenAlphaFragment.class.getName(), bundle);
			}
		});

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
				Intent intent = new Intent(getActivity(),
						ConnectNetGuideAcitivty.class);
				intent.putExtra("scanqr", true);
				getActivity().startActivity(intent);
			}
		});
		btn_go.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						MyDeviceActivity.class);
				Alpha2Application.getAlpha2().removeActivity();
			}
		});
	}

	public void send() {
		startWait();
		pAudio = new short[288000];
		pNAudio = new int[1];
		Log.i("nxy", "name = " + name + "  psw = " + psw + " capa = " + capa);
		String[] ss = { "@" + name, "#" + psw, "%" + getCapaType(capa),"*"+ ConfigureRobotNetworkActivity.robotSn };
		sendTimes = 0;
		mPlayerThread = new PlayerThread(ss);
		if (mAudioPlayer == null) {
			initAudioPlayer();
		}
		voice1_image.setBackgroundResource(R.drawable.voice_star_amin);
		anim = (AnimationDrawable) voice1_image.getBackground();
		anim.start();
		mPlayerThread.start();
		inint();

		
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
	
	public Handler uIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOP_ANIM:
				break;
			case RESEND:
				send();
				break;
			case WAIT:
				anim.stop();
				sendFailed();
				break;

			default:
				break;
			}
		}
	};
	private AudioPlayer mAudioPlayer;

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

			uIHandler.sendEmptyMessage(STOP_ANIM);
		}// else if(sendTimes%3==0){
			// Log.i("nxy", " 次完整的发送完成");
		// uIHandler.sendEmptyMessage(RESEND);
		// }
	}

	public void initAudioPlayer() {
		mAudioPlayer = new AudioPlayer(uIHandler);
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
	private void startWait(){
//		if(uIHandler.hasMessages(WAIT)){
//			uIHandler.removeMessages(WAIT);
//		}
//		uIHandler.sendEmptyMessageDelayed(WAIT, 1000*60);
		countdownTimer.start();
	}
	public void startPlay(byte[] data) {
		mAudioPlayer.setDataSource(data);

		// 音频源就绪
		mAudioPlayer.prepare();
		mAudioPlayer.play();
	}
	public void sendFailed(){
		sendFailLay.setVisibility(View.VISIBLE);
		sendingLay.setVisibility(View.GONE);
		voice1_image.setBackgroundResource(R.drawable.voice_sending_failed);
	}
	private void inint(){
		sendFailLay.setVisibility(View.GONE);
		sendingLay.setVisibility(View.VISIBLE);
//		voice1_image.setBackgroundResource(R.drawable.voice_sending);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mPlayerThread != null) {
			mPlayerThread.stopPlay();
		}
		if(anim!=null && anim.isRunning()){
			anim.stop();
		}
		if(countdownTimer!=null)
			countdownTimer.cancel();
	}
}
