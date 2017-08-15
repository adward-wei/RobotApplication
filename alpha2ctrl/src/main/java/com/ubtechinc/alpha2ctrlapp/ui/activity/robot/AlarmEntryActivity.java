package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ubtech.utilcode.utils.ListUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAlarmDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAlarmReponsitory;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.DeskClock;
import com.ubtechinc.alpha2ctrlapp.events.AlarmCRUDEvent;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.robot.AlarmListAdapter;
import com.ubtechinc.alpha2ctrlapp.widget.RefreshListView;
import com.ubtechinc.alpha2ctrlapp.widget.RefreshListView.OnRefreshListener;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AlarmEntryActivity
 * @date 6/27/2017
 * @author tanghongyu
 * @Description 闹钟页
 *
 * @modifier
 * @modify_time
 */
public class AlarmEntryActivity extends BaseContactActivity {

	private RefreshListView mListView;
	private BaseAdapter mAlarmListAdapter;
	private List<DeskClock> mDeskClockList = new ArrayList<DeskClock>();

	private int mSelectPosition;
	public boolean isDelete;
	private TextView tips1,tips2;
	private  TextView remindHistroyTv;
	private boolean hasHistroyRemind  =false;
	private boolean isRefresh= false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_alarm_entry);
		setTheme(R.style.ActionSheetStyleiOS7);
		initView();
		EventBus.getDefault().register(this);

	}

	private void initView() {
		mListView = (RefreshListView) this.findViewById(android.R.id.list);
		this.findViewById(R.id.add_alarm).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(AlarmEntryActivity.this,
								AlarmAddActivity.class);
						startActivity(intent);
					}
				});
		/**列表的下拉刷新**/
		mListView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				isRefresh = true; 
				getHistroyALarmList();
			}
		});
		this.findViewById(R.id.btn_back).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mListView != null)
					position--;
				mSelectPosition = position;
				Intent intent = new Intent(AlarmEntryActivity.this,
						AlarmEditActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(DeskClock.class.getName(),
						mDeskClockList.get(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		remindHistroyTv = (TextView)findViewById(R.id.remind_histroy_tv);
		remindHistroyTv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AlarmEntryActivity.this,RemindHistroyActivity.class);
				startActivity(intent);
			}
		});
		View emptyView = this.findViewById(android.R.id.empty);
		mListView.setEmptyView(emptyView);
		tips1= (TextView)findViewById(R.id.tips1);
		tips2 = (TextView)findViewById(R.id.tips2);
		String str1 = getString(R.string.alarm_add_prompt1_tips);
		int index_1 = tips1.getText().toString().indexOf(str1);
		int index_2 = index_1+str1.length();
		SpannableStringBuilder builder1 = new SpannableStringBuilder(tips1.getText().toString());
		ForegroundColorSpan greenSpan1 = new ForegroundColorSpan(this.getResources().getColor(R.color.find_psw_tip_color_red));
		builder1.setSpan(greenSpan1, index_1, index_2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tips1.setText(builder1);
		String str2="+";
		int index_12 = tips2.getText().toString().indexOf(str2);
		int index_22 = index_12+str2.length();
		SpannableStringBuilder builder12 = new SpannableStringBuilder(tips2.getText().toString());
		ForegroundColorSpan greenSpan12 = new ForegroundColorSpan(this.getResources().getColor(R.color.find_psw_tip_color_red));
		builder12.setSpan(greenSpan12, index_12, index_22,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tips2.setText(builder12);
		initListView();
	}

	private void initListView() {
	
		mAlarmListAdapter = new AlarmListAdapter(this, mDeskClockList,new AlarmListAdapter.ISlideDeleteListener2(){

			@Override
			public void slideDelete(DeskClock clock) {
				// TODO Auto-generated method stub
				deleteAlarm(clock);
				
			}
			
		});
		mListView.setAdapter(mAlarmListAdapter);
		mListView.getEmptyView().setVisibility(View.GONE);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(AlarmCRUDEvent event) {
		switch (event.deskClock.getType()) {
		case 0: // add
			mDeskClockList.add(event.deskClock);
			mAlarmListAdapter.notifyDataSetChanged();
			break;
		case 2: // update & replace
			mDeskClockList.set(mSelectPosition, event.deskClock);
			mAlarmListAdapter.notifyDataSetChanged();
			break;
		}
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();

		getHistroyALarmList();
		LoadingDialog.getInstance(mContext).show();
	}


	private void refreshListData(List<DeskClock> list) {
		Log.i("alarm", " test ...");
		mDeskClockList.clear();
		mDeskClockList.addAll(list);
		mAlarmListAdapter.notifyDataSetChanged();
		if(hasHistroyRemind&& list.size()==0){
			mListView.setVisibility(View.GONE);
		}else{
			mListView.setVisibility(View.VISIBLE);
		}
		if(isRefresh){
			mListView.onRefreshComplete();
			isRefresh = false;
		}
	}

	private void getValidAlarmList() {

		RobotAlarmReponsitory.getInstance().getValidAlarmList(new IRobotAlarmDataSource.GetValidAlarmListCallback() {
			@Override
			public void onLoadValidAlarmList(List<DeskClock> alarmList) {
				LoadingDialog.dissMiss();
				refreshListData(alarmList);
			}

			@Override
			public void onDataNotAvailable(ThrowableWrapper e) {

			}
		});

	}
	private void getHistroyALarmList() {

		RobotAlarmReponsitory.getInstance().getAllAlarmList(new IRobotAlarmDataSource.GetAllAlarmListCallback() {
			@Override
			public void onLoadAlarmList(List<DeskClock> alarmList) {
				if(ListUtils.isEmpty(alarmList)){

					remindHistroyTv.setVisibility(View.GONE);
					hasHistroyRemind =false;
				}else{
					remindHistroyTv.setVisibility(View.VISIBLE);
					hasHistroyRemind = true;
				}
				getValidAlarmList();
			}

			@Override
			public void onDataNotAvailable(ThrowableWrapper e) {

			}
		});

	}



	public void deleteAlarm(DeskClock clock) {
		isDelete = true;
		LoadingDialog.getInstance(mContext).show();
		clock.setType((byte) 1);
		RobotAlarmReponsitory.getInstance().deleteAlarm(clock, new IRobotAlarmDataSource.ControlAlarmCallback() {
			@Override
			public void onSuccess() {

				ToastUtils.showShortToast(
						R.string.alarm_delete_success);
				getValidAlarmList();

			}

			@Override
			public void onFail(ThrowableWrapper e) {
				LoadingDialog.dissMiss();
				ToastUtils.showShortToast(
						R.string.alarm_delete_failed);
			}
		});
	}


}
