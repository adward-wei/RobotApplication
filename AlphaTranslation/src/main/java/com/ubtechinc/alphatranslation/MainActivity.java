package com.ubtechinc.alphatranslation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ubtechinc.alpha.model.StaticValue;
import com.ubtechinc.alpha.sdk.AlphaRobotApi;
import com.ubtechinc.alpha.sdk.motion.MotionRobotApi;
import com.ubtechinc.alpha.sdk.speech.SpeechRobotApi;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechASRListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechTtsListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.SpeechWakeUpListener;
import com.ubtechinc.alphatranslation.GetDataFromWeb.IJsonListener;
import com.umeng.analytics.MobclickAgent;

import java.util.Random;

public class MainActivity extends Activity implements OnClickListener, SpeechASRListener {
	public static final String APP_EXIT = "com.ubtechinc.closeapp";
	private final String strChinesToEnglishTransHttp = "http://117.121.48.3:8887/translate?key=1111&from=zh&to=en&domain=spoken&content=";
	private final String strEnglistToChineseTransHttp = "http://117.121.48.3:8887/translate?key=1111&from=en&to=zh&domain=spoken&content=";
	private static final String TAG = "Translattion";
	private AlphaRobotApi mRobotApi;
	private ExitBroadcast mExitBroadcast;
	//是否是中文转英文
	private boolean mIsZh2En = true;
	private String mPackageName;// = this.getPackageName();

	private Button button1;
	private Button button2;
	private SpeechRobotApi speechApi;

	private MotionRobotApi motionRobotApi;
	private String language;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
		MobclickAgent.setDebugMode(true);
		mPackageName = this.getPackageName();
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		mRobotApi =AlphaRobotApi.get();
		mRobotApi.initializ(this);
		speechApi=SpeechRobotApi.get();
		speechApi.initializ(this);
		speechApi.setSpeechMode(0);
		speechApi.registerWakeUpListener(new SpeechWakeUpListener() {
			@Override
			public void onSuccess() {
				speechApi.startSpeechASR(67,MainActivity.this);
			}
			@Override
			public void onError(int errCode, String errDes) {

			}
		});
		motionRobotApi=MotionRobotApi.get().initializ(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(APP_EXIT);

		filter.addAction(mPackageName);
		filter.addAction(mPackageName + StaticValue.APP_BUTTON_EVENT);
		filter.addAction(mPackageName + StaticValue.APP_BUTOON_EVENT_CLICK);

		mExitBroadcast = new ExitBroadcast();
		MainActivity.this.registerReceiver(mExitBroadcast, filter);


	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.button1:
				try {
					// mRobotApi.speech_setVoiceName("catherine");
                    //mRobotApi.speech_startRecognized("");
					speechApi.switchSpeechCore("cn");
					speechApi.startSpeechASR(67,this);
					mIsZh2En = true;
				}catch (Exception e){
					e.printStackTrace();
				}
				break;
			case R.id.button2:
				mIsZh2En = false;
				try {
					// mRobotApi.speech_setVoiceName("xiaoyan");
					// botApi.speech_startRecognized("");
					speechApi.switchSpeechCore("en");
					Thread.sleep(500);
					speechApi.startSpeechASR(67,this);
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			default:
				break;
		}
	}

	private void performTranslate(final String text) {
		speechApi.stopSpeechASR();
		StringBuilder strBuilder = new StringBuilder();

		if (mIsZh2En) {
			strBuilder.append(strChinesToEnglishTransHttp);
			strBuilder.append(java.net.URLEncoder.encode(text));
		} else {
			strBuilder.append(strEnglistToChineseTransHttp);
			// strBuilder.append(text);
			if (text.length() < 3 || text.equals("Yeah.")) {
				return;
			}
			strBuilder.append(android.net.Uri.encode(text, "US-ASCII"));

		}
		// strBuilder.append(java.net.URLEncoder.encode(text));

		Log.v(TAG, strBuilder.toString());

		// Skip Nuance NLU result
		if(strBuilder.toString().contains("NLU_Result")) {
			return ;
		}
		final String nluResult = strBuilder.toString();
		GetDataFromWeb.getJsonByGet(nluResult, new IJsonListener() {



			@Override
			public void onGetJson(boolean isSuccess, String json,

								  long request_code, String reason) {


				Log.v(TAG, json);
				// mRobotApi.speech_StartTTS(text)
				if(isSuccess) {
					String strTts = JsonParser.parseString(json);
					int number = new Random().nextInt(10);
					String actionName = String.format("ACT%d", number);
					motionRobotApi.playAction(actionName, null);

					String VoiceName;
					if (mIsZh2En) {

						language = "en";
					} else {
						language = "cn";
						VoiceName = "xiaoyan";
						//VoiceName = "catherine";
					}

					speechApi.switchSpeechCore(language);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					speechApi.speechStartTTS(strTts,new SpeechTtsListener(){

						@Override
						public void onEnd() {
							if(language.equals("cn")){
								language="en";
							}else{
								language="cn";
							}

							speechApi.switchSpeechCore(language);
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							speechApi.startSpeechASR(67,MainActivity.this);
						}
					});

				}
			}

		});
	}


	@Override
	public void onDestroy() {
		// mSocketService.releaseConnection();

		if (mRobotApi != null) {
			// mRobotApi.speech_setVoiceName("xiaoyan");
			mRobotApi.stopSpeechAsr();
			mRobotApi = null;
		}

		this.unregisterReceiver(mExitBroadcast);
		super.onDestroy();
	}

	@Override
	public void onBegin() {
		Log.d(TAG,"...onBegin...");

	}

	@Override
	public void onEnd() {
		Log.d(TAG,"...onEnd...");

	}

	@Override
	public void onResult(String text) {
		Log.d(TAG,"...onResule..."+text);
		if(!text.isEmpty()){
			performTranslate(text);
		}
	}

	@Override
	public void onError(int code) {
		Log.d(TAG,"...onError..."+code);
		speechApi.startSpeechASR(67,this);

	}

	public class ExitBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(APP_EXIT)) {
				// mRobotApi.speech_stopRecognized();
				Log.i(TAG, "speech_stopRecognized ");

				if (mRobotApi != null) {
					// mRobotApi.speech_setVoiceName("xiaoyan");
					mRobotApi = null;
				}
				MainActivity.this.finish();
				System.exit(0);
			} else if (intent.getAction().equals(mPackageName)) {

			} else if (intent.getAction().equals(
					mPackageName + StaticValue.APP_BUTTON_EVENT)) {
				//mRobotApi.sendButtonEvent2Server(intent, mPackageName, "utf-8");

			} else if (intent.getAction().equals(
					mPackageName + StaticValue.APP_BUTOON_EVENT_CLICK)) {

			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		MobclickAgent.onPause(this);
		super.onPause();
	}

}
