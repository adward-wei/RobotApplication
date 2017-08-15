package com.ubtechinc.alpha2ctrlapp.ui.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ubtech.utilcode.utils.ListUtils;
import com.ubtech.utilcode.utils.TimeUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAuthorizeDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAuthorizeReponsitory;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.user.MyDeviceHistoryAdpter;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;
import com.ubtechinc.alpha2ctrlapp.widget.RefreshListView;
import com.ubtechinc.alpha2ctrlapp.widget.RefreshListView.OnRefreshListener;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HistoryDeviceActivity extends BaseContactActivity {

    protected Activity mContext;
    private String TAG = "MyDeviceActivity";
    private List<RobotInfo> devicelist = new ArrayList<>();
    private MyDeviceHistoryAdpter adpter;
    private RefreshListView listView;
    public BaseContactFragememt currentFragment;
    private boolean isRefresh = false;
    private LinearLayout no_device_tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_device_history);
        this.mContext = this;
        initView();
    }

    public void initView() {
        this.title = (TextView) findViewById(R.id.authorize_title);
        title.setText(getString(R.string.device_history));
        listView = (RefreshListView) findViewById(R.id.lst_robot);
        adpter = new MyDeviceHistoryAdpter(mContext, devicelist, this);
        listView.setAdapter(adpter);
        listView.setOnItemClickListener(adpter);
        /**列表的下拉刷新**/
        listView.setonRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                isRefresh = true;
                checkDevice();
            }
        });
        no_device_tip = (LinearLayout) findViewById(R.id.no_device_tip);
        checkDevice();
    }



    private void refresh(List<RobotInfo> list) {
        devicelist = list;
        if (isRefresh) {
            listView.onRefreshComplete();
            isRefresh = false;
        }
        Collections.sort(devicelist, new CompareList());
        adpter.onNotifyDataSetChanged(devicelist);
    }


    public class CompareList implements Comparator {
        /*
            * int compare(AppEntrityStorageInfo o1, AppEntrityStorageInfo o2) 返回一个基本类型的整型，
            * 返回负数表示：o1 小于o2，
            * 返回0 表示：o1和o2相等，
            * 返回正数表示：o1大于o2。
            */

        @Override
        public int compare(Object obj1, Object obj2) {
            RobotInfo info1 = (RobotInfo) obj1;
            RobotInfo info2 = (RobotInfo) obj2;

            Date date1 = null;
            Date date2 = null;
            long mill1 = 0;
            long mill2 = 0;

                date1 =   TimeUtils.millis2Date(info1.getRelationDate());
                date2 =   TimeUtils.millis2Date(info2.getRelationDate());

            mill1 = TimeUtils.getMillisFromDate(date1);
            mill2 = TimeUtils.getMillisFromDate(date2);
            //按照学生的年龄进行升序排列
            if (mill1 < mill2) {
                return 1;
            }
            if (mill1 == mill2) {
                return 0;
            }
            return -1;
        }
    }

    public void checkDevice() {
        RobotAuthorizeReponsitory.getInstance().loadHistoryRobotList(Alpha2Application.getAlpha2().getUserId(),new IRobotAuthorizeDataSource.LoadRobotListCallback() {
            @Override
            public void onLoadRobotList(List<RobotInfo> robotList) {
                devicelist.addAll(robotList);
                if (ListUtils.isEmpty(devicelist)) {
                    ToastUtils.showShortToast( R.string.devices_no_robot);
                    no_device_tip.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                } else {
                    no_device_tip.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
                refresh(devicelist);
            }

            @Override
            public void onDataNotAvailable(ThrowableWrapper e) {
                ToastUtils.showShortToast(e.getMessage());
            }
        });
    }




}
