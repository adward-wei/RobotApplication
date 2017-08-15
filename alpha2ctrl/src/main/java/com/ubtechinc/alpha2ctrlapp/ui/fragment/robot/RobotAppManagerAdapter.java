package com.ubtechinc.alpha2ctrlapp.ui.fragment.robot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubtech.utilcode.utils.StringUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.AppPackageSimpleInfo;
import com.ubtechinc.alpha2ctrlapp.util.DataCleanManager;
import com.ubtechinc.alpha2ctrlapp.util.Tools;

import java.util.HashMap;
import java.util.List;

/**
 * @author：liuhai
 * @date：2017/4/13 11:17
 * @modifier：ubt
 * @modify_date：2017/4/13 11:17
 * [A brief description]
 * version
 */

public class RobotAppManagerAdapter extends BaseAdapter {

    private List<AppPackageSimpleInfo> mAppInfos;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private boolean isManager;//是否处于编辑状态
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelectedMap;

    public RobotAppManagerAdapter(Context context, List<AppPackageSimpleInfo> appInfos) {
        this.mContext = context;
        mAppInfos = appInfos;
        isSelectedMap = new HashMap<>();
        mLayoutInflater = LayoutInflater.from(context);
    }


    // 初始化isSelected的数据
    public void initData() {
        getIsSelectedMap().clear();
        for (int i = 0; i < mAppInfos.size(); i++) {
            getIsSelectedMap().put(i, false);
        }
    }


    public void setAppInfos(List<AppPackageSimpleInfo> appInfos) {
        mAppInfos = appInfos;
        initData();
        this.notifyDataSetChanged();
    }

    public List<AppPackageSimpleInfo> getAppInfos() {
        return mAppInfos;
    }

    /**
     * 设置为编辑状态
     *
     * @param manager true 表示正在编辑
     */
    public void setManager(boolean manager) {
        isManager = manager;
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mAppInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppInfos != null ? mAppInfos.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.robot_app_manager_item, null);
            holder = new ViewHolder();
            holder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_selected);
            holder.tvAppName = (TextView) convertView.findViewById(R.id.tv_app_name);
            holder.ivAppPath = (ImageView) convertView.findViewById(R.id.iv_app_img);
            holder.tvAppSize = (TextView) convertView.findViewById(R.id.tv_app_size);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (isManager) {
            holder.ivSelected.setVisibility(View.VISIBLE);
        } else {
            holder.ivSelected.setVisibility(View.GONE);
        }

        // 根据isSelected来设置checkbox的选中状况
        if (getIsSelectedMap().get(position)) {
            holder.ivSelected.setImageResource(R.drawable.ic_press);
        } else {
            holder.ivSelected.setImageResource(R.drawable.ic_unpress);
        }
        String packageName = StringUtils.nullStringToDefault(mAppInfos.get(position).getPackageName());

        if (StringUtils.isEquals(Constants.app_smartcamera_packageName, packageName)&& Tools.isCh(mContext)) {
            holder.tvAppName.setText(Constants.app_smartcamera_name_ch);
        } else if (StringUtils.isEquals(Constants.app_smartcamera_packageName, packageName) && !Tools.isCh(mContext)) {
            holder.tvAppName.setText(Constants.app_smartcamera_name_en);
        } else if (StringUtils.isEquals(BusinessConstants.PACKAGENAME_VIDEO_SUPERVISION, packageName) && Tools.isCh(mContext)) {
            holder.tvAppName.setText(Constants.app_video_name_ch);
        } else if (StringUtils.isEquals(BusinessConstants.PACKAGENAME_VIDEO_SUPERVISION, packageName) && !Tools.isCh(mContext)) {
            holder.tvAppName.setText(Constants.app_video_name_en);
        } else if (StringUtils.isEquals(BusinessConstants.PACKAGENAME_ALPHA_TRANSLATION, packageName) && Tools.isCh(mContext)) {
            holder.tvAppName.setText(Constants.app_alphatranslation_name_ch);
        } else if (StringUtils.isEquals(BusinessConstants.PACKAGENAME_ALPHA_TRANSLATION, packageName) && !Tools.isCh(mContext)) {
            holder.tvAppName.setText(Constants.app_alphatranslation_name_en);
        } else {
            holder.tvAppName.setText(mAppInfos.get(position).getName());
        }
        holder.tvAppSize.setText(DataCleanManager.getFormatSize(mAppInfos.get(position).getAppSize()));
        holder.ivAppPath.setImageBitmap(Bytes2Bimap(mAppInfos.get(position).getIcon()));
        return convertView;
    }

    private class ViewHolder {
        private ImageView ivSelected;
        private ImageView ivAppPath;
        private TextView tvAppName;
        private TextView tvAppSize;

    }


    public void setSelectedItem(int position) {
        //根据当前是否选择设置
        if (isSelectedMap.get(position)) {
            isSelectedMap.put(position, false);
            setIsSelectedMap(isSelectedMap);
        } else {
            isSelectedMap.put(position, true);
            setIsSelectedMap(isSelectedMap);
        }
        notifyDataSetChanged();
    }


    public static HashMap<Integer, Boolean> getIsSelectedMap() {
        return isSelectedMap;
    }

    public static void setIsSelectedMap(HashMap<Integer, Boolean> isSelectedMap) {
        RobotAppManagerAdapter.isSelectedMap = isSelectedMap;
    }

    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

}
