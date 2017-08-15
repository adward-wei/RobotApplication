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
import android.widget.ListView;
import android.widget.TextView;

import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.entity.AppInstalledInfo;
import com.ubtechinc.alpha2ctrlapp.entity.AppUpdate;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotApp;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewDeviceInfo;
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

//import com.sina.weibo.sdk.api.CmdObject;

public class AppUpdateAdpter extends BaseAdapter implements OnItemClickListener {
    private Context context;
    private List<AppUpdate> appInfoList;
    private LayoutInflater inflater;
    public int clicktemp = -1;
    private String TAG = "Alpha2Adapter";
    private MainPageActivity activity;
    public static Map<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    public NewDeviceInfo deviceInfo;
    public boolean isSelcted;
    private ListView listView;

    public AppUpdateAdpter(Context context, List<AppUpdate> list) {
        this.appInfoList = list;
        this.context = context;
        this.activity = (MainPageActivity) context;
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
            viewHolder.appImage = (ImageView) convertView
                    .findViewById(R.id.app_image);
            viewHolder.appName = (TextView) convertView
                    .findViewById(R.id.app_name);
            viewHolder.appVer = (TextView) convertView
                    .findViewById(R.id.app_version);
            viewHolder.appSize = (TextView) convertView
                    .findViewById(R.id.app_size);
            viewHolder.appDis = (NumsCountTextView) convertView
                    .findViewById(R.id.app_dis);
            viewHolder.btn_update = (ImageView) convertView
                    .findViewById(R.id.btn_update);

            viewHolder.operatingAnim = DownloadActionAnimation.getDownloadActionAnimation(context);
            // isSelected.put(position, viewHolder.checkBox.isChecked());
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!RobotManagerService.getInstance().isConnectedRobot() || appInfoList.get(position).getAppPath() == null
                || TextUtils.isEmpty(appInfoList.get(position).getAppPath())) {
            viewHolder.btn_update
                    .setImageDrawable(context.getResources().getDrawable(R.drawable.app_dis_download));
            appInfoList.get(position).setStatus(-1);
        } else {
            switch (appInfoList.get(position).getStatus()) {
                case BusinessConstants.APP_STATE_INIT:
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_refresh));
                    viewHolder.btn_update.clearAnimation();
                    break;
                case BusinessConstants.APP_STATE_DOWNLOADING:
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_installing));
//				viewHolder.btn_update.startAnimation(viewHolder.operatingAnim);
                    break;
                case BusinessConstants.APP_STATE_DOWNLOAD_SUCCESS:
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_installing));
//				viewHolder.btn_update.startAnimation(viewHolder.operatingAnim);
                    break;
                case BusinessConstants.APP_STATE_DOWNLOAD_FAIL:
                    viewHolder.btn_update.clearAnimation();
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_failed));
                    break;
                case BusinessConstants.APP_STATE_INSTALL_SUCCESS:
                    viewHolder.btn_update.clearAnimation();
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.btn_app_open));
                    break;
                case BusinessConstants.APP_STATE_INSTALL_FAIL:
                    viewHolder.btn_update.clearAnimation();
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_failed));
                    break;
                case BusinessConstants.APP_STATE_ROBOT_APP_HAS_UPDATE:
                    viewHolder.btn_update.clearAnimation();
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_refresh));
                    break;
                case BusinessConstants.APP_STATE_INSUFFCIENT_SPACE:
                    viewHolder.btn_update.clearAnimation();
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_failed));
                    break;
                default:
                    break;
            }
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

        DecimalFormat df = new DecimalFormat("0.00");
        try {
            double d = Double.valueOf(appInfoList.get(position).getAppSize()) / 1024.0;
            String db = df.format(d);
            viewHolder.appSize.setText(db + "M");
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.appVer.setText(context.getString(R.string.app_detail_version) + appInfoList.get(position).getAppVersion());
        LoadImage.setRounderConner(activity, viewHolder.appImage, appInfoList.get(position).getAppHeadImage(), 1);

        viewHolder.btn_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (RobotManagerService.getInstance().isConnectedRobot()) {


                    if (appInfoList.get(position).getStatus() == 0
                            || appInfoList.get(position).getStatus() == 3
                            || appInfoList.get(position).getStatus() == 5 || appInfoList.get(position).getStatus() == 7) {
                        RobotApp app = new RobotApp();
                        app.setName(appInfoList.get(position).getAppName());
                        app.setPackageName(appInfoList.get(position)
                                .getAppPackage());
                        app.setUrl(appInfoList.get(position).getAppPath());
                        app.setAppKey(appInfoList.get(position).getAppKey());
                        activity.downLoadApp(app, appInfoList.get(position).getAppHeadImage(), appInfoList.get(position).getVersionCode());
                        appInfoList.get(position).setStatus(1);
                        appInfoList.get(position).setStatusTv(context.getResources().getString(R.string.shop_page_downloading));
                        viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_installing));
						 viewHolder.btn_update.startAnimation(viewHolder.operatingAnim);
                    } else if (appInfoList.get(position).getStatus() == -1) {
                        ToastUtils.showShortToast( R.string.shop_page_disable_download);
                    } else if (appInfoList.get(position).getStatus() == 4) {
                        AppInstalledInfo appInfo = new AppInstalledInfo();
                        appInfo.setName(appInfoList.get(position).getAppName());
                        appInfo.setPackageName(appInfoList.get(position)
                                .getAppPackage());
                        appInfo.setAppImagePath(appInfoList.get(position).getAppHeadImage());
                        activity.startApp(appInfo);
                    } else if (appInfoList.get(position).getStatus() == BusinessConstants.APP_STATE_INSUFFCIENT_SPACE) {
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
        clicktemp = position;
        Bundle bundle = new Bundle();
        bundle.putInt("appId", appInfoList.get(position).getAppId());
        activity.currentFragment.replaceFragment(
                AppDetailFragment.class.getName(), bundle);

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
}
