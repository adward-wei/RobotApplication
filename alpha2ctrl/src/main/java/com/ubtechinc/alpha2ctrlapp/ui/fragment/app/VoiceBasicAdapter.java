package com.ubtechinc.alpha2ctrlapp.ui.fragment.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.app.BasicCmdModel;

import java.util.List;


/**
 * TODO: Replace the implementation with code for your data type.
 */
public class VoiceBasicAdapter extends BaseAdapter {

    private List<BasicCmdModel> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public VoiceBasicAdapter(Context context, List<BasicCmdModel> items) {
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
            convertView = mLayoutInflater.inflate(R.layout.fragment_item_voice_basic, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.iv_left_type);
            viewHolder.mContentView = (TextView) convertView.findViewById(R.id.tv_basic_content);
            viewHolder.mTitleView = (TextView) convertView.findViewById(R.id.tv_basic_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mContentView.setText(mList.get(position).getContent());
        viewHolder.mTitleView.setText(mList.get(position).getTitle());
        viewHolder.mImageView.setImageResource(mList.get(position).getIvId());

        return convertView;
    }

    public class ViewHolder {
        public ImageView mImageView;
        public TextView mContentView;
        public TextView mTitleView;

    }
}
