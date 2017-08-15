package com.ubtechinc.alpha2ctrlapp.ui.adapter.robot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewActionInfo;

import java.util.List;

/**
 * @author：ubt
 * @date：2017/3/6 19:43
 * @modifier：ubt
 * @modify_date：2017/3/6 19:43
 * [A brief description]
 * version
 */

public class VoiceActionAdapter extends BaseAdapter {
    private List<NewActionInfo> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int selectedItem = -1;

    public VoiceActionAdapter(Context context, List<NewActionInfo> items) {
        mList = items;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    public void setList(List<NewActionInfo> list) {
        mList.clear();
        this.mList = list;
        notifyDataSetChanged();
    }

    public List<NewActionInfo> getList() {
        return mList;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.item_voice_compund_action, null);
            viewHolder = new ViewHolder();
            viewHolder.mContentView = (TextView) convertView.findViewById(R.id.tv_action_name);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.iv_choose);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mContentView.setText(mList.get(position).getActionName());
        viewHolder.mImageView.setVisibility(View.GONE);
        if (position == selectedItem) {
            viewHolder.mImageView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView mContentView;
        public ImageView mImageView;


    }
}
