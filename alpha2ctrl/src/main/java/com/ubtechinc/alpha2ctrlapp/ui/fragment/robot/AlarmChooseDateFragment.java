package com.ubtechinc.alpha2ctrlapp.ui.fragment.robot;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.DeskClock;
import com.ubtechinc.alpha2ctrlapp.events.DateTimeEvent;
import com.ubtechinc.alpha2ctrlapp.widget.wheelview.OnWheelScrollListener;
import com.ubtechinc.alpha2ctrlapp.widget.wheelview.WheelView;
import com.ubtechinc.alpha2ctrlapp.widget.wheelview.adapter.NumericWheelAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;


public class AlarmChooseDateFragment extends Fragment {
	public static final String TAG = AlarmChooseDateFragment.class.getSimpleName();
	private WheelView mYear, mMonth, mDayOfMonth, mHour, mMin;
	private DeskClock mDC;
	private Context mContext; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDC = this.getArguments().getParcelable(DeskClock.class.getName());
		mContext = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_alarm_choose_date, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView();
	}

	private void initView() {
		final View rootView = this.getView();
		rootView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				rootView.findViewById(R.id.ok).performClick();
			}
			
		});
		rootView.findViewById(R.id.cancel).setOnClickListener(clickListener);
		rootView.findViewById(R.id.ok).setOnClickListener(clickListener);
		mYear = (WheelView) rootView.findViewById(R.id.year);
		mMonth = (WheelView) rootView.findViewById(R.id.month);
		mDayOfMonth = (WheelView) rootView.findViewById(R.id.day);
		mHour = (WheelView) rootView.findViewById(R.id.hour);
		mMin = (WheelView) rootView.findViewById(R.id.minute);

		Calendar c = Calendar.getInstance();
		Context context = this.getActivity();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH);
		String[] dateArray = mDC.getDtstart().split("-");

		NumericWheelAdapter yeadAdapter = new NumericWheelAdapter(context, 2016, curYear);
		yeadAdapter.setLabel(context.getString(R.string.alarm_year));
		mYear.setViewAdapter(yeadAdapter);
		mYear.setCyclic(false);
		mYear.addScrollingListener(scrollListener);
		mYear.setCurrentItem(Integer.parseInt(dateArray[0]) - 2016);

		NumericWheelAdapter monthAdapter = new NumericWheelAdapter(context, 1, 12, "%02d");
		monthAdapter.setLabel(context.getString(R.string.alarm_month));
		mMonth.setViewAdapter(monthAdapter);
		mMonth.setCyclic(true);
		mMonth.addScrollingListener(scrollListener);
		mMonth.setCurrentItem(Integer.parseInt(dateArray[1]) - 1);

		NumericWheelAdapter dayOfMonthAdapter = new NumericWheelAdapter(context, 1, getDay(curYear, curMonth), "%02d");
		dayOfMonthAdapter.setLabel(context.getString(R.string.alarm_day));
		mDayOfMonth.setViewAdapter(dayOfMonthAdapter);
		mDayOfMonth.setCyclic(true);
		mDayOfMonth.setCurrentItem(Integer.parseInt(dateArray[2]) - 1);

		NumericWheelAdapter hourAdapter = new NumericWheelAdapter(context, 0, 23, "%02d");
//		hourAdapter.setLabel("时");
		hourAdapter.setLabel("");
		mHour.setViewAdapter(hourAdapter);
		mHour.setCyclic(true);
		mHour.addScrollingListener(scrollListener);
		mHour.setCurrentItem(mDC.getHour());

		NumericWheelAdapter minAdapter = new NumericWheelAdapter(context, 0, 59, "%02d");
//		minAdapter.setLabel("分");
		minAdapter.setLabel("");
		mMin.setViewAdapter(minAdapter);
		mMin.setCyclic(true);
		mMin.addScrollingListener(scrollListener);
		mMin.setCurrentItem(mDC.getMinutes());
		
		mYear.setVisibleItems(7);// 设置显示行数
		mMonth.setVisibleItems(7);
		mDayOfMonth.setVisibleItems(7);
		mHour.setVisibleItems(7);
		mMin.setVisibleItems(7);
	}

	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ok:
				DateTimeEvent event = new DateTimeEvent(
						mYear.getCurrentItem() + 2016,
						mMonth.getCurrentItem() + 1,
						mDayOfMonth.getCurrentItem() + 1,
						mHour.getCurrentItem(),
						mMin.getCurrentItem() );
				EventBus.getDefault().post(event);
			break;
			}
            getFragmentManager().beginTransaction()
            		.remove(AlarmChooseDateFragment.this)
            		.commit();
		}
	};
	
	private int getDay(int year, int month) {
		int day = 30;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = year % 4 == 0? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	private OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int year = mYear.getCurrentItem() + 2016;// 年
			int month = mMonth.getCurrentItem() + 1;// 月
			NumericWheelAdapter dayOfMonthAdapter 
			= new NumericWheelAdapter(mContext, 1, getDay(year, month), "%02d");
			dayOfMonthAdapter.setLabel(mContext.getString(R.string.alarm_day));
			mDayOfMonth.setViewAdapter(dayOfMonthAdapter);
		}
	};
	
}