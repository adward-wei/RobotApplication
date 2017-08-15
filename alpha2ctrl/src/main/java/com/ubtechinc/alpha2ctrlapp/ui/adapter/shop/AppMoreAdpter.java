package com.ubtechinc.alpha2ctrlapp.ui.adapter.shop;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.entity.AppUpdate;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewDeviceInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotApp;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.widget.DownloadActionAnimation;
import com.ubtechinc.alpha2ctrlapp.widget.NumsCountTextView;
import com.ubtechinc.alpha2ctrlapp.widget.RefreshListView;

import java.text.DecimalFormat;
import java.util.List;


public class AppMoreAdpter extends AppListInfoAdpter {
    private String TAG = "AppMoreAdpter";
    public NewDeviceInfo deviceInfo;
    public boolean isSelcted;
    private RefreshListView listView;

    public AppMoreAdpter(Context context, List<AppUpdate> list, RefreshListView lv, DownLoadListener downLoadListener) {
        super(context, list, downLoadListener);
        this.listView = lv;
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

        if (appInfoList.get(position).getStatus() == -1) {
            viewHolder.btn_update.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btn_update.setVisibility(View.VISIBLE);
        }
        switch (appInfoList.get(position).getStatus()) {
            case -1:
                viewHolder.btn_update.clearAnimation();
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.disable_download));
                break;
            case 0:
                viewHolder.btn_update.clearAnimation();
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.download_state));
                break;
            case 1:
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_installing));
                viewHolder.btn_update.startAnimation(viewHolder.operatingAnim);
                break;
            case 2:
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_installing));
                viewHolder.btn_update.startAnimation(viewHolder.operatingAnim);
                break;
            case 3:
                viewHolder.btn_update.clearAnimation();
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_failed));
                break;
            case 4:
                viewHolder.btn_update.clearAnimation();
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.btn_app_open));
                break;
            case 5:
                viewHolder.btn_update.clearAnimation();
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_failed));
                break;
            case 6:
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_failed));
                break;
            case 7:
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_refresh));
                break;
            case BusinessConstants.APP_STATE_INSUFFCIENT_SPACE:
                viewHolder.btn_update.clearAnimation();
                viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_failed));
                break;
            default:
                break;
        }

        // viewHolder.btn_update.setText(appInfoList.get(position).getStatusTv());
        if (TextUtils.isEmpty(appInfoList.get(position).getAppLanguageName())) {
            viewHolder.appName.setText(appInfoList.get(position).getAppName());
        } else {
            viewHolder.appName.setText(appInfoList.get(position).getAppLanguageName());
        }
        if (TextUtils.isEmpty(appInfoList.get(position).getAppLanguageDesciber())) {
            if (!TextUtils.isEmpty(appInfoList.get(position).getAppResume()))
                viewHolder.appDis.setText(appInfoList.get(position).getAppResume());
            else
                viewHolder.appDis.setText(context.getText(R.string.shop_page_no_description));
        } else {
            viewHolder.appDis.setText(appInfoList.get(position).getAppLanguageDesciber());
        }
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            double d = (Double.valueOf(appInfoList.get(position).getAppSize()) / 1024.0);
            String db = df.format(d);
            viewHolder.appSize.setText(context.getString(R.string.app_detail_size) + db + "M");
        } catch (Exception e) {
            viewHolder.appSize.setText("");
            e.printStackTrace();
        }

        viewHolder.appVer.setText(context.getString(R.string.app_detail_version) + appInfoList.get(position).getAppVersionName());
        LoadImage.setRounderConner(activity, viewHolder.appImage, appInfoList.get(position).getAppIcon(), 1);

        viewHolder.btn_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!RobotManagerService.getInstance().isConnectedRobot()) {
                  //  activity.connnect_robot_tips.setTextColor(context.getResources().getColor(R.color.red));
                    if (appInfoList.get(position).getStatus() != -1) {
                        if (appInfoList.get(position).getPackageName().equals(BusinessConstants.PACKAGENAME_SHOP_ACTION) || appInfoList.get(position).getPackageName().equals(BusinessConstants.PACKAGENAME_SHOP_ALARM)) {
                            return;
                        } else {
                            //  viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_dis_download));
                            appInfoList.get(position).setStatus(-1);
                        }
                    }
                } else {

                    if (appInfoList.get(position).getStatus() == 0
                            || appInfoList.get(position).getStatus() == 3
                            || appInfoList.get(position).getStatus() == 5 || appInfoList.get(position).getStatus() == 7) {
                        RobotApp app = new RobotApp();
                        app.setName(appInfoList.get(position).getAppName());
                        app.setPackageName(appInfoList.get(position).getPackageName());
                        app.setUrl(appInfoList.get(position).getAppPath());
                        app.setAppKey(appInfoList.get(position).getAppKey());
                        activity.downLoadApp(app, appInfoList.get(position).getAppIcon(), Integer.parseInt(appInfoList.get(position).getAppVersion()));
                        //appInfoList.get(position).setStatus(1);

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
                        //	viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_loading));
                        notifyDataSetChanged();
                        if (null != mDownLoadListener) {
                            mDownLoadListener.startDownLoad(appInfoList.get(position));
                        }
                        // viewHolder.btn_update.startAnimation(viewHolder.operatingAnim);
                    } else if (appInfoList.get(position).getStatus() == -1) {
                        ToastUtils.showShortToast( R.string.shop_page_disable_download);
                    } else if (appInfoList.get(position).getStatus() == 4) {
//                        RobotApp appInfo = new RobotApp();
//                        appInfo.setName(appInfoList.get(position).getAppName());
//                        appInfo.setPackageName(appInfoList.get(position).getPackageName());
//                        AppInstalledInfo newInfo = new AppInstalledInfo();
//                        newInfo.setApkInfo(appInfo);
//                        newInfo.setAppImagePath(appInfoList.get(position).getAppHeadImage());
//                        activity.startApp(newInfo);
                    } else if (appInfoList.get(position).getStatus() == 6) {
                        ToastUtils.showShortToast( R.string.shop_page_disable_download);
                    } else if (appInfoList.get(position).getStatus() == BusinessConstants.APP_STATE_INSUFFCIENT_SPACE) {
                        ToastUtils.showShortToast( R.string.news_storage_title);
                    }


                }

            }
        });
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
        clicktemp = position;
        Bundle bundle = new Bundle();
        if (listView != null)
            position--;
        skipToAppDetail(false, appInfoList.get(position));
    }


    public void onNotifyDataSetChanged(List<AppUpdate> infoList, RefreshListView lv) {
        // TODO Auto-generated method stub
        clicktemp = -1;
        appInfoList = infoList;
        this.listView = lv;
        this.notifyDataSetChanged();
    }



}
