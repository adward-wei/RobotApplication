package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.DeskClock;
import com.ubtechinc.alpha2ctrlapp.events.AlarmSelRepeatEvent;
import com.ubtechinc.alpha2ctrlapp.util.AlarmUtil;
import com.ubtechinc.alpha2ctrlapp.widget.CheckedRelativeLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;


public class AlarmRepeatActivity extends Activity {

	private CheckedRelativeLayout once, day, week, month;
	private int repeatTypeViewId;
	private CheckedRelativeLayout[] dayOfWeekList = new CheckedRelativeLayout[7];
	private ViewGroup dayOfWeekContainer; 
	private DeskClock deskClock;
	private TextView monthPromptDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_alarm_repeat);
		deskClock = this.getIntent().getExtras().getParcelable(DeskClock.class.getName());
		initView();
		initTitlelbar();
	
		switch (AlarmUtil.getDeskClockRepeatType(deskClock)) {
		case everyday:
			exclusive(R.id.repeat_everyday);
			break;
		case everymonth:
			exclusive(R.id.repeat_everymonth);
			break;
		case everyweek:
			exclusive(R.id.repeat_everyweek);
			break;
		case once:
			exclusive(R.id.repeat_once);
			break;
		}
	}
	
	private void initTitlelbar() {
		findViewById(R.id.btn_back).setOnClickListener(titlebarClickListener);
		findViewById(R.id.finish).setOnClickListener(titlebarClickListener);
	}
	
	private View.OnClickListener titlebarClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back: {
				finish();
			}
			break;
			case R.id.finish: {
				switch (repeatTypeViewId) {
				case R.id.repeat_once:
					deskClock.setDaysofweek(0);
				break;
				case R.id.repeat_everyday:
					deskClock.setDaysofweek(127);
				break;
				case R.id.repeat_everyweek:
					int week = 0;
					for (int i = 0; i < dayOfWeekList.length; ++i) {
						week |= ((dayOfWeekList[i].isChecked() ? 1 : 0) << i);
					}
					deskClock.setDaysofweek(week);;
				break;
				case R.id.repeat_everymonth:
					deskClock.setDaysofweek(128);
				break;
				}
				EventBus.getDefault().post(new AlarmSelRepeatEvent(deskClock));
				finish();
			}
			break;
			}
		}
	};
	private void initView() {
		once = (CheckedRelativeLayout) findViewById(R.id.repeat_once);
		once.setOnClickListener(repeatClickListener);
		day = (CheckedRelativeLayout) findViewById(R.id.repeat_everyday);
		day.setOnClickListener(repeatClickListener);
		week = (CheckedRelativeLayout) findViewById(R.id.repeat_everyweek);
		week.setOnClickListener(repeatClickListener);
		month = (CheckedRelativeLayout) findViewById(R.id.repeat_everymonth);
		month.setOnClickListener(repeatClickListener);
		dayOfWeekContainer = (ViewGroup) findViewById(R.id.days_container);
		monthPromptDate = (TextView) findViewById(R.id.month_prompt_date);

		dayOfWeekList[0] = (CheckedRelativeLayout) this.findViewById(R.id.monday);
		dayOfWeekList[1] = (CheckedRelativeLayout) this.findViewById(R.id.tuesday);
		dayOfWeekList[2] = (CheckedRelativeLayout) this.findViewById(R.id.wednesday);
		dayOfWeekList[3] = (CheckedRelativeLayout) this.findViewById(R.id.thursday);
		dayOfWeekList[4] = (CheckedRelativeLayout) this.findViewById(R.id.friday);
		dayOfWeekList[5] = (CheckedRelativeLayout) this.findViewById(R.id.saturday);
		dayOfWeekList[6] = (CheckedRelativeLayout) this.findViewById(R.id.sunday);
		switch (AlarmUtil.getDeskClockRepeatType(deskClock)) {
		case everyweek:
			int dayOfWeek = deskClock.getDaysofweek();
			for (int i = 0; i < 7; ++i) {			
				if ((dayOfWeek & (1 << i)) == (1 << i)) { 
					dayOfWeekList[i].setChecked(true);
				} 
			}
			break;
		default:
			break;		
		}
	}

	private View.OnClickListener repeatClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			exclusive(v.getId());
		}
	};
	
	public void exclusive(int controlID) {
		switch (controlID) {
		case R.id.repeat_everyweek:
			int dayOfWeek = deskClock.getDaysofweek();
			if (dayOfWeek != 0) {
				for (int i = 0; i < 7; ++i) {			
					if ((dayOfWeek & (1 << i)) == (1 << i)) { 
						dayOfWeekList[i].setChecked(true);
					} 
				}
			} else {
				Calendar calendar = Calendar.getInstance();
				String[] dateArray = deskClock.getDtstart().split("-");
				calendar.clear();
				calendar.set(Integer.parseInt(dateArray[0]),  //year
						Integer.parseInt(dateArray[1]),  //month
						Integer.parseInt(dateArray[2])); //day
				int foo = calendar.get(Calendar.DAY_OF_WEEK);
				switch (foo) {
				case Calendar.SUNDAY:
					dayOfWeekList[6].setChecked(true);
					break;
				default:
					dayOfWeekList[foo-2].setChecked(true);
					break;
				}
			}
			
		break;
		case R.id.repeat_everymonth:
			monthPromptDate.setText(AlarmUtil.getDeskClockDateStr(deskClock));
		break;
		}
		repeatTypeViewId = controlID;
		once.setChecked(controlID == R.id.repeat_once);
		day.setChecked(controlID == R.id.repeat_everyday);
		week.setChecked(controlID == R.id.repeat_everyweek);
		month.setChecked(controlID == R.id.repeat_everymonth);
		dayOfWeekContainer.setVisibility(controlID == R.id.repeat_everyweek
				? View.VISIBLE : View.GONE);
		monthPromptDate.setVisibility(controlID == R.id.repeat_everymonth
				? View.VISIBLE : View.INVISIBLE);
	}	

}