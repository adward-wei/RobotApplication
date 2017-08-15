package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAlarmDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAlarmReponsitory;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.DeskClock;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.DeskClockList;
import com.ubtechinc.alpha2ctrlapp.events.AlarmCRUDEvent;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.robot.RemindHistroyAdpeter;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/*************************
 * @date 2016/6/22
 * @author
 * @Description 提醒历史记录
 * @modify
 * @modify_time
 **************************/
public class RemindHistroyActivity extends BaseContactActivity implements View.OnClickListener {

    private ListView mListView;
    private BaseAdapter mAlarmListAdapter;
    private List<DeskClock> mDeskClockList = new ArrayList<DeskClock>();

    private int mSelectPosition;
    public boolean isDelete;
    private boolean hasHistroyRemind = false;
    private LinearLayout delelteLay; // 底部删除布局
    private TextView buttonDelete; // 顶部删除的Icon ,点击底部控制底部布局
    private CheckBox checkBoxSelect;
    private ImageView ivSelectedAll;
    private boolean isSelectedAll = false;
    private LinearLayout llSelected;
    private TextView tvDelete;  // 底部删除按钮执行删除事件
    public boolean isEdit = false;
    public HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.remind_histroy_activity);
        setTheme(R.style.ActionSheetStyleiOS7);
        initView();
        EventBus.getDefault().register(this);

    }

    private void initView() {
        delelteLay = (LinearLayout) findViewById(R.id.ll_delete_lay);
        buttonDelete = (TextView) findViewById(R.id.deleteLay);
        checkBoxSelect = (CheckBox) findViewById(R.id.choice_check_box);
        llSelected = (LinearLayout) findViewById(R.id.ll_selected);
        ivSelectedAll = (ImageView) findViewById(R.id.iv_check_box);
        ivSelectedAll.setImageResource(R.drawable.btn_unchoose);
        tvDelete = (TextView) findViewById(R.id.delelte_tv);
        mListView = (ListView) this.findViewById(android.R.id.list);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (isEdit) {
                    if (isSelected.get(position))
                        isSelected.put(position, false);
                    else {
                        isSelected.put(position, true);
                    }
                    mAlarmListAdapter.notifyDataSetChanged();
                }

                if (getSelectedSize() == mDeskClockList.size()) {
                    isSelectedAll = true;
                    ivSelectedAll.setImageResource(R.drawable.ic_press);
                } else if (getSelectedSize() < mDeskClockList.size()) {
                    isSelectedAll = false;
                    ivSelectedAll.setImageResource(R.drawable.btn_unchoose);
                }
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!isEdit) {
                    delelteLay.setVisibility(View.VISIBLE);
                    isEdit = true;
                } else {
                    delelteLay.setVisibility(View.GONE);
                    isEdit = false;
                }

            }
        });
        checkBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    for (int i = 0; i < mDeskClockList.size(); i++) {
                        isSelected.put(i, true);
                    }
                } else {
                    for (int i = 0; i < mDeskClockList.size(); i++) {
                        isSelected.put(i, false);
                    }
                }
                mAlarmListAdapter.notifyDataSetChanged();
            }

        });

        llSelected.setOnClickListener(this);

        tvDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                batchDeleteAlarmList();
            }
        });
//		this.findViewById(R.id.add_alarm).setOnClickListener(
//				new View.OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						Intent intent = new Intent(RemindHistroyActivity.this,
//								AlarmAddActivity.class);
//						startActivity(intent);
//					}
//				});
        this.findViewById(R.id.btn_back).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });
        initListView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_selected:
                isSelectedAll = !isSelectedAll;
                // TODO Auto-generated method stub
                if (isSelectedAll) {
                    ivSelectedAll.setImageResource(R.drawable.ic_press);
                    for (int i = 0; i < mDeskClockList.size(); i++) {
                        isSelected.put(i, true);
                    }
                } else {
                    ivSelectedAll.setImageResource(R.drawable.btn_unchoose);
                    for (int i = 0; i < mDeskClockList.size(); i++) {
                        isSelected.put(i, false);
                    }
                }
                mAlarmListAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void initListView() {

        mAlarmListAdapter = new RemindHistroyAdpeter(this, mDeskClockList, new RemindHistroyAdpeter.ISlideDeleteListener2() {

            @Override
            public void slideDelete(DeskClock clock) {
                // TODO Auto-generated method stub
                deleteAlarm(clock);

            }

        });
        mListView.setAdapter(mAlarmListAdapter);
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

        getAllAlarmList();
        LoadingDialog.getInstance(mContext).show();
    }



    private void refreshListData(List<DeskClock> list) {
        Log.i("alarm", " test ...");
        mDeskClockList.clear();
        mDeskClockList.addAll(list);
        mAlarmListAdapter.notifyDataSetChanged();
        if (hasHistroyRemind && list.size() == 0) {
            mListView.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < mDeskClockList.size(); i++) {
            isSelected.put(i, false);
        }
        if (isEdit && list.size() == 0) {
            RemindHistroyActivity.this.finish();
        }
    }

    private void getAllAlarmList() {


        RobotAlarmReponsitory.getInstance().getAllAlarmList(new IRobotAlarmDataSource.GetAllAlarmListCallback() {
            @Override
            public void onLoadAlarmList(List<DeskClock> alarmList) {
                LoadingDialog.dissMiss();
                refreshListData(alarmList);
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
                    getAllAlarmList();
            }

            @Override
            public void onFail(ThrowableWrapper e) {
                LoadingDialog.dissMiss();
                ToastUtils.showShortToast(
                        R.string.alarm_delete_failed);
            }
        });

    }

    /**
     * 删除所有选择
     */
    public void batchDeleteAlarmList() {
        DeskClockList deskList = new DeskClockList();
        for (int i = 0; i < mDeskClockList.size(); i++) {
            if (isSelected.get(i)) {
                deskList.addToList(mDeskClockList.get(i));
                //list.add(mDeskClockList.get(i));
            }
        }
        if (deskList.getList().size() > 0) {
            LoadingDialog.getInstance(mContext).show();


            RobotAlarmReponsitory.getInstance().batchDeleteAlarm(deskList.getList(), new IRobotAlarmDataSource.ControlAlarmCallback() {
                @Override
                public void onSuccess() {

                        ToastUtils.showShortToast(
                                R.string.alarm_delete_success);
                        getAllAlarmList();

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

    private int getSelectedSize() {
        DeskClockList deskList = new DeskClockList();
        for (int i = 0; i < mDeskClockList.size(); i++) {
            if (isSelected.get(i)) {
                deskList.addToList(mDeskClockList.get(i));
                //list.add(mDeskClockList.get(i));
            }
        }
        return deskList.getList().size();
    }





}
