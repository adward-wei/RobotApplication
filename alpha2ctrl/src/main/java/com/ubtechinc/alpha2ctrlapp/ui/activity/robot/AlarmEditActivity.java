package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.constants.SocketCmdId;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAlarmDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAlarmReponsitory;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.DeskClock;
import com.ubtechinc.alpha2ctrlapp.events.AlarmCRUDEvent;
import com.ubtechinc.alpha2ctrlapp.events.AlarmSelRepeatEvent;
import com.ubtechinc.alpha2ctrlapp.events.DateTimeEvent;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.robot.AlarmChooseDateFragment;
import com.ubtechinc.alpha2ctrlapp.util.AlarmUtil;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;


public class AlarmEditActivity extends BaseContactActivity {
	private TextView mAlarmTime, mRepeat;
	private EditText mAlarmMsg;
	private DeskClock mDeskClock;
	private View maskView;
	private Calendar onCreateCalendar, onRestartCalendar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_alarm_add);
		mDeskClock = this.getIntent().getExtras()
				.getParcelable(DeskClock.class.getName());
		initView();
		initTitlelbar();
		EventBus.getDefault().register(this);
		onCreateCalendar = Calendar.getInstance();
	}

	private void initTitlelbar() {
		findViewById(R.id.btn_back).setOnClickListener(titlebarClickListener);
		findViewById(R.id.add_alarm).setOnClickListener(titlebarClickListener);
	}

	private View.OnClickListener titlebarClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back: {
				finish();
			}
				break;
			case R.id.add_alarm: {
				String msg = mAlarmMsg.getText().toString();
				if (TextUtils.isEmpty(msg)) {
					Toast.makeText(AlarmEditActivity.this,
							R.string.alarm_null_msg, Toast.LENGTH_LONG).show();
					return;
				}
				mDeskClock.setMessage(msg);
				mDeskClock.setType(2);
				EventBus.getDefault().post(new AlarmCRUDEvent(mDeskClock));

				RobotAlarmReponsitory.getInstance().updateAlarm(mDeskClock, new IRobotAlarmDataSource.ControlAlarmCallback() {
					@Override
					public void onSuccess() {

							ToastUtils.showShortToast(
									R.string.alarm_update_success);
							AlarmEditActivity.this.finish();

					}

					@Override
					public void onFail(ThrowableWrapper e) {
						ToastUtils.showShortToast(
								R.string.alarm_update_failed);
					}
				});
				LoadingDialog.getInstance(mContext).show();
			}
				break;
			}
		}
	};

	private void initView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.alarm_detail);
		maskView = findViewById(R.id.dialog_mask);
		mRepeat = (TextView) findViewById(R.id.alarm_repeat);
		mRepeat.setText(AlarmUtil.getRepeatTypeStr(this, mDeskClock));
		mRepeat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AlarmEditActivity.this,
						AlarmRepeatActivity.class);
				Bundle extras = new Bundle();
				extras.putParcelable(DeskClock.class.getName(), mDeskClock);
				intent.putExtras(extras);
				startActivity(intent);
			}
		});
		mAlarmTime = (TextView) findViewById(R.id.alarm_time);
		mAlarmTime.setText(AlarmUtil.getAlarmDateTimeStr(mDeskClock));
		mAlarmTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlarmChooseDateFragment fragment = new AlarmChooseDateFragment();
				Bundle args = new Bundle();
				if (onRestartCalendar != null && onCreateCalendar.compareTo(onRestartCalendar) != 0) {
					DeskClock dc = new DeskClock();
					dc.setDtstart(String.format("%04d-%02d-%02d",
							onRestartCalendar.get(Calendar.YEAR),
							onRestartCalendar.get(Calendar.MONTH) + 1,
							onRestartCalendar.get(Calendar.DAY_OF_MONTH)));
					dc.setHour(onRestartCalendar.get(Calendar.HOUR_OF_DAY));
					dc.setMinutes(onRestartCalendar.get(Calendar.MINUTE));
					args.putParcelable(DeskClock.class.getName(), dc);
				} else {
					args.putParcelable(DeskClock.class.getName(), mDeskClock);
				}
				fragment.setArguments(args);
				maskView.setVisibility(View.VISIBLE);
				
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
				ft.add(R.id.dialog_mask, fragment);
				ft.commit();
			}
		});
		mAlarmMsg = (EditText) findViewById(R.id.alarm_content);
		mAlarmMsg.setText(mDeskClock.getMessage());
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(AlarmSelRepeatEvent event) {
		mDeskClock.setDaysofweek(event.deskClock.getDaysofweek());
		mRepeat.setText(AlarmUtil.getRepeatTypeStr(this, mDeskClock));
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(DateTimeEvent event) {
		AlarmUtil.fillDeskClock(mDeskClock, event);
		mAlarmTime.setText(AlarmUtil.getAlarmDateTimeStr(mDeskClock));
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			FragmentManager fm = this.getFragmentManager();
			Fragment fragment = fm.findFragmentById(R.id.dialog_mask);
			if (fragment != null) {
				fm.beginTransaction().remove(fragment).commit();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}



	@Override
	protected void onRestart() {
		super.onRestart();
		onRestartCalendar = Calendar.getInstance();
	}
}
