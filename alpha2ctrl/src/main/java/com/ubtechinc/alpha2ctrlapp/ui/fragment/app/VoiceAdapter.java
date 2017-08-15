package com.ubtechinc.alpha2ctrlapp.ui.fragment.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;

import java.util.List;


/**
  * TODO: Replace the implementation with code for your data type.
 */
public class VoiceAdapter extends BaseAdapter {

    private List<String> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public VoiceAdapter(Context context, List<String> items) {
        mList = items;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

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
            convertView = mLayoutInflater.inflate(R.layout.fragment_voice_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mContentView= (TextView) convertView.findViewById(R.id.tv_voice_item);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mContentView.setText(mList.get(position));

        return convertView;
    }

    public class ViewHolder {
        public TextView mContentView;


    }
}
