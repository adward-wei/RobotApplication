package com.ubtechinc.alpha2ctrlapp.ui.adapter.shop;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.ListUtils;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.IntentConstants;
import com.ubtechinc.alpha2ctrlapp.entity.AppUpdate;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewDeviceInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotApp;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.shop.AppDetailFragment;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.widget.DownloadActionAnimation;
import com.ubtechinc.alpha2ctrlapp.widget.NumsCountTextView;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AppListInfoAdpter extends BaseAdapter implements
        OnItemClickListener {
    protected Context context;
    protected List<AppUpdate> appInfoList;
    protected LayoutInflater inflater;
    public int clicktemp = -1;
    private String TAG = "Alpha2Adapter";
    protected MainPageActivity activity;
    public static Map<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    public NewDeviceInfo deviceInfo;
    public boolean isSelcted;
    protected DownLoadListener mDownLoadListener;

    public AppListInfoAdpter(Context context, List<AppUpdate> mDeviceInfoList,  DownLoadListener downLoadListener) {
        this.appInfoList = mDeviceInfoList;
        this.context = context;
        this.activity = (MainPageActivity) context;
        mDownLoadListener = downLoadListener;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return appInfoList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return appInfoList.get(arg0);
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
            convertView = inflater.inflate(R.layout.app_update_item, null,
                    false);
            viewHolder.appImage = (ImageView) convertView.findViewById(R.id.app_image);
            viewHolder.appName = (TextView) convertView.findViewById(R.id.app_name);
            viewHolder.appVer = (TextView) convertView.findViewById(R.id.app_version);
            viewHolder.appSize = (TextView) convertView.findViewById(R.id.app_size);
            viewHolder.appDis = (NumsCountTextView) convertView.findViewById(R.id.app_dis);
            viewHolder.btn_update = (ImageView) convertView.findViewById(R.id.btn_update);

            // isSelected.put(position, viewHolder.checkBox.isChecked());
            viewHolder.operatingAnim = DownloadActionAnimation.getDownloadActionAnimation(context);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final AppUpdate appUpdate = appInfoList.get(position);
//		if(appUpdate.getStatus()== BusinessConstants.APP_STATE_ERROR || (ListUtils.isEmpty(appUpdate.getLinkedAppList()) && StringUtils.isEquals(appUpdate.getSdkType(), BusinessConstants.SHOP_SDK_TYPE_IOS))){
//			//如果不能下载或者主程序是IOS且没有子程序的情况下，不显示下载
//			viewHolder.btn_update.setVisibility(View.GONE);
//		}else{
//			viewHolder.btn_update.setVisibility(View.VISIBLE);
//		}

        switch (appUpdate.getStatus()) {
            case BusinessConstants.APP_STATE_ERROR:
                viewHolder.btn_update.clearAnimation();
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.disable_download));
                break;
            case BusinessConstants.APP_STATE_INIT:
                viewHolder.btn_update.clearAnimation();
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.download_state));
                break;
            case BusinessConstants.APP_STATE_DOWNLOADING:
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_installing));
                viewHolder.btn_update.startAnimation(viewHolder.operatingAnim);
                break;
            case BusinessConstants.APP_STATE_DOWNLOAD_SUCCESS:
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_installing));
                viewHolder.btn_update.startAnimation(viewHolder.operatingAnim);
                break;
            case BusinessConstants.APP_STATE_DOWNLOAD_FAIL:
                viewHolder.btn_update.clearAnimation();
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_failed));
                break;
            case BusinessConstants.APP_STATE_INSTALL_SUCCESS://机器人端解压APK成功（表示）
                viewHolder.btn_update.clearAnimation();
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.btn_app_open));
                break;
            case BusinessConstants.APP_STATE_INSTALL_FAIL:
                viewHolder.btn_update.clearAnimation();
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_failed));
                break;
            case BusinessConstants.APP_STATE_CAN_NOT_DOWNLOAD:
                viewHolder.btn_update.clearAnimation();
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_failed));
                break;
            case BusinessConstants.APP_STATE_ROBOT_APP_HAS_UPDATE:
                viewHolder.btn_update.clearAnimation();
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_refresh));
                break;
            case BusinessConstants.APP_STATE_INSUFFCIENT_SPACE:
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_failed));
                break;
            default:
                break;
        }
        if (appUpdate.getPackageName().equals(BusinessConstants.PACKAGENAME_SHOP_ACTION) || appUpdate.getPackageName().equals(BusinessConstants.PACKAGENAME_SHOP_ALARM)) {
            viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.btn_app_open));
            appInfoList.get(position).setStatus(BusinessConstants.APP_STATE_INSTALL_SUCCESS);
        }

        // viewHolder.btn_update.setText(appInfoList.get(position).getStatusTv());
        if (TextUtils.isEmpty(appUpdate.getAppLanguageName())) {
            viewHolder.appName.setText(appUpdate.getAppName());
        } else {
            viewHolder.appName.setText(appUpdate.getAppLanguageName());
        }
//		if(TextUtils.isEmpty(appUpdate.getAppLanguageDesciber())){
//			if(!TextUtils.isEmpty(appUpdate.getAppResume()))
//				viewHolder.appDis.setText(appUpdate.getAppResume());
//			else
//				viewHolder.appDis.setText(context.getText(R.string.shop_page_no_description));
//		}else{
//			viewHolder.appDis.setText(appUpdate.getAppLanguageDesciber());
//		}
        if (!StringUtils.isEmpty(appUpdate.getAppDesc())) {
            viewHolder.appDis.setText(appUpdate.getAppDesc());
        } else {
            viewHolder.appDis.setText(context.getText(R.string.shop_page_no_description));
        }

        DecimalFormat df = new DecimalFormat("0.00");
        try {
            double d = Double.valueOf(appUpdate.getAppSize()) / 1024.0;
            String db = df.format(d);
            viewHolder.appSize.setText(context.getString(R.string.app_detail_size) + db + "M");
        } catch (Exception e) {
            viewHolder.appSize.setText("");
            e.printStackTrace();
        }

        viewHolder.appVer.setText(context.getString(R.string.app_detail_version) + appUpdate.getAppVersionName());
        LoadImage.setRounderConner(activity, viewHolder.appImage, appUpdate.getAppIcon(), 1);
        viewHolder.btn_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final int status = appUpdate.getStatus();
                if (!RobotManagerService.getInstance().isConnectedRobot()) {
                    // mMainPageActivity.connnect_robot_tips.setTextColor(context.getResources().getColor(R.color.red));
                    if (status != BusinessConstants.APP_STATE_ERROR) {
                        if (appInfoList.get(position).getPackageName().equals(BusinessConstants.PACKAGENAME_SHOP_ACTION) || appInfoList.get(position).getPackageName().equals(BusinessConstants.PACKAGENAME_SHOP_ALARM)) {
                            return;
                        } else {
                            //  viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_dis_download));
                            appInfoList.get(position).setStatus(-1);
                        }
                    }

                } else {

                    if (status == BusinessConstants.APP_STATE_INIT
                            || status == BusinessConstants.APP_STATE_DOWNLOAD_FAIL
                            || status == BusinessConstants.APP_STATE_INSTALL_FAIL
                            || status == BusinessConstants.APP_STATE_ROBOT_APP_HAS_UPDATE) {

                        RobotApp app = new RobotApp();
                        app.setName(appInfoList.get(position).getAppName());
                        app.setPackageName(appInfoList.get(position).getPackageName());
                        app.setUrl(appInfoList.get(position).getAppPath());
                        app.setAppKey(appInfoList.get(position).getAppKey());
                        activity.downLoadApp(app, appInfoList.get(position).getAppIcon(), Integer.parseInt(appInfoList.get(position).getAppVersion()));
                        //    appInfoList.get(position).setStatus(1);

                        if (appInfoList.get(position).getPackageName().equals(BusinessConstants.PACKAGENAME_CH_CHAT)) {
                            for (int j = 0; j < appInfoList.size(); j++) {
                                if (appInfoList.get(j).getPackageName().equals(BusinessConstants.PACKAGENAME_CH_CHAT)) {
                                    appInfoList.get(j).setStatus(1);
                                }
                            }
                        } else {
                            appInfoList.get(position).setStatus(1);
                        }

                        appInfoList.get(position).setStatusTv(context.getResources().getString(R.string.shop_page_downloading));
                        //  viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_refresh));
                        //  skipToAppDetail(true, appUpdate);
                        notifyDataSetChanged();
                        viewHolder.btn_update.startAnimation(viewHolder.operatingAnim);
                        if (mDownLoadListener != null) {
                            mDownLoadListener.startDownLoad(appInfoList.get(position));
                        }
                    } else if (status == BusinessConstants.APP_STATE_ERROR) {
                        ToastUtils.showShortToast( R.string.shop_page_disable_download);
                    } else if (status == BusinessConstants.APP_STATE_INSTALL_SUCCESS) {

                    } else if (status == BusinessConstants.APP_STATE_CAN_NOT_DOWNLOAD) {
                        ToastUtils.showShortToast( R.string.shop_page_disable_download);
                    } else if (status == BusinessConstants.APP_STATE_INSUFFCIENT_SPACE) {
                        ToastUtils.showShortToast( R.string.news_storage_title);
                    }

                }

            }
        });

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position,
                            long arg3) {
        try {
            position = position - 3;
            if ((!ListUtils.isEmpty(appInfoList)) && null != appInfoList.get(position)) {
                AppUpdate appUpdate = appInfoList.get(position);
                boolean isDownload = false;
                if (appUpdate.getStatus() == 1) {
                    isDownload = true;
                }
                skipToAppDetail(isDownload, appInfoList.get(position));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    protected void skipToAppDetail(boolean isDownload, AppUpdate appUpdate) {
        Bundle bundle = new Bundle();
        Logger.i( "appId" + appUpdate.getAppId());
        bundle.putInt(IntentConstants.DATA_APP_ID, appUpdate.getAppId());
        bundle.putBoolean(IntentConstants.DATA_IS_DOWNLOAD, isDownload);
        activity.currentFragment.replaceFragment(AppDetailFragment.class.getName(), bundle);
    }

    public class ViewHolder {
        TextView appName;
        ImageView appImage;
        ImageView btn_update;
        NumsCountTextView appDis;
        TextView appSize;
        TextView appVer;
        Animation operatingAnim;
    }


    public void setConnectedDevice(NewDeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public NewDeviceInfo getConnectedDevice() {
        return this.deviceInfo;
    }

    public void onNotifyDataSetChanged(List<AppUpdate> infoList) {
        // TODO Auto-generated method stub
        clicktemp = -1;
        appInfoList = infoList;
        this.notifyDataSetChanged();
    }

    public void onClear() {
        appInfoList.clear();
        clicktemp = -1;
        this.notifyDataSetChanged();
    }

    public interface DownLoadListener {
        void startDownLoad(AppInfo appInfo);
    }
}
