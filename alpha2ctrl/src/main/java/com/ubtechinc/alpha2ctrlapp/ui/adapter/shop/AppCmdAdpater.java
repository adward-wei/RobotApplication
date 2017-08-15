package com.ubtechinc.alpha2ctrlapp.ui.adapter.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.base.ImageGalleryAdpter;

import java.util.List;

/**
 * @author：ubt
 * @date：2017/2/16 19:49
 * @modifier：ubt
 * @modify_date：2017/2/16 19:49
 * [A brief description]
 * version
 */

public class AppCmdAdpater extends BaseAdapter {
    private Context context;
    private List<String> cmdInfoList;
    private LayoutInflater inflater;
    public int clicktemp = -1;
    private String TAG = "Alpha2Adapter";
    private MainPageActivity activity;
    public boolean isSelcted;
    private String[] urls;
    private ImageGalleryAdpter galleryAdper;

    public AppCmdAdpater(Context context, List<String> cmdInfoList) {
        this.cmdInfoList = cmdInfoList;
        this.context = context;
        this.activity = (MainPageActivity) context;
//		getUrls(commentInfoList);
        inflater = LayoutInflater.from(context);
    }

    public void setCmdList(List<String> cmdList) {
        this.cmdInfoList.clear();
        this.cmdInfoList.addAll(cmdList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return cmdInfoList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return cmdInfoList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        AppCmdAdpater.ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            viewHolder = new AppCmdAdpater.ViewHolder();
            convertView = inflater.inflate(R.layout.layout_app_detail_cmd, null, false);

            viewHolder.content = (TextView) convertView
                    .findViewById(R.id.tv_cmd_content);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AppCmdAdpater.ViewHolder) convertView.getTag();
        }
        viewHolder.content.setText("\"" + cmdInfoList.get(position) + "\"");
        return convertView;
    }


    public class ViewHolder {
        TextView content;
    }


}
