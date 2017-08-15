package com.ubtechinc.alpha2ctrlapp.ui.adapter.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ubtech.utilcode.utils.TimeUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewDeviceInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.alpha2ctrlapp.ui.activity.user.HistoryDeviceActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.user.ScanDeviceEncodeActivity;
import com.ubtechinc.alpha2ctrlapp.widget.RoundImageView;

import java.util.Date;
import java.util.List;

public class MyDeviceHistoryAdpter extends BaseAdapter implements OnItemClickListener {
    private Context context;
    private List<RobotInfo> mDeviceInfoList;
    private LayoutInflater inflater;
    public int clicktemp = -1;
    private String TAG = "Alpha2Adapter";
    //	private MainPageActivity activity;
    public NewDeviceInfo deviceInfo;
    public boolean isSelcted;
    private String to;
    private HistoryDeviceActivity mfrDeviceFragement;

    public MyDeviceHistoryAdpter(Context context, List<RobotInfo> mDeviceInfoList, HistoryDeviceActivity frag) {
        this.mDeviceInfoList = mDeviceInfoList;
        this.context = context;
//		this.activity = (MainPageActivity)context;
        inflater = LayoutInflater.from(context);
        this.mfrDeviceFragement = frag;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDeviceInfoList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mDeviceInfoList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        final ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.device_history_item, null, false);
            viewHolder.alphaNo = (TextView) convertView.findViewById(R.id.alphaNo);
            viewHolder.ivLogo = (RoundImageView) convertView.findViewById(R.id.ivLogo);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String time = "";

            Date date =   TimeUtils.millis2Date(mDeviceInfoList.get(position).getRelationDate());
            time = TimeUtils.dateFormatToString(date, TimeUtils.DEFAULT_DATE_FORMAT);


        viewHolder.time.setText(time);
        viewHolder.alphaNo.setText(mDeviceInfoList.get(position).getEquipmentId());
//		LoadImage.LoadGrayHeader(mfrDeviceFragement.getActivity(),viewHolder.ivLogo, mDeviceInfoList.get(position).getUserImage());	
        return convertView;
    }

    public class ViewHolder {
        TextView alphaNo;
        RoundImageView ivLogo;
        TextView time;


    }

    public void setConnectedDevice(NewDeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public NewDeviceInfo getConnectedDevice() {
        return this.deviceInfo;
    }


    public void onNotifyDataSetChanged(List<RobotInfo> InfoList) {
        // TODO Auto-generated method stub
        this.mDeviceInfoList = InfoList;
        clicktemp = -1;
        this.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        Bundle bundle = new Bundle();
        position--;
        Intent intent = new Intent(context, ScanDeviceEncodeActivity.class);
        intent.putExtra("ALPHA_No", mDeviceInfoList.get(position).getEquipmentId());
        context.startActivity(intent);
    }


}