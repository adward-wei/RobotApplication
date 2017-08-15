package com.ubtechinc.alpha2ctrlapp.ui.fragment.shop.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.orhanobut.logger.Logger;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.CallbackListener;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.entity.AppInstalledInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotApp;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.robot.AppFragMent;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog.OnNegsitiveClick;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog.OnPositiveClick;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;


/**
 * @author nixiaoyan
 * @ClassName AppInfoAdapter
 * @date 2016/6/25
 * @Description
 * @modifier 唐宏宇
 * @modify_time 2016/7/25
 */
public class AppInfoAdapter extends BaseAdapter implements OnItemClickListener, OnPositiveClick, OnNegsitiveClick, OnItemLongClickListener {
    private Context context;
    private List<AppInstalledInfo> appList = Lists.newArrayList();
    private LayoutInflater inflater;
    public int clicktemp = -1;
    private AppFragMent mfragement;
    private CommonDiaglog deleDialog;
    private MainPageActivity activity;
    CallbackListener callbackListener;
    private static final String TAG = "AppInfoAdapter";

    public AppInfoAdapter(Context context,
                          AppFragMent fragement, CallbackListener callbackListener) {
        this.context = context;
        this.mfragement = fragement;
        inflater = LayoutInflater.from(context);
        this.activity = (MainPageActivity) this.mfragement.getActivity();
        deleDialog = new CommonDiaglog(this.mfragement.getActivity(), true);
        deleDialog.setNegsitiveClick(this);
        deleDialog.setPositiveClick(this);
        deleDialog.setMessase(context.getString(R.string.app_uninstall_tips));
        this.callbackListener = callbackListener;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return appList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    public void onNotifyDataSetChanged() {
        this.appList.clear();

        this.appList = MainPageActivity.dao.queryAppList();
        clicktemp = -1;
        this.notifyDataSetChanged();
    }

    public List<AppInstalledInfo> getFileList() {
        return appList;
    }

    public void onClear() {
        this.appList.clear();
        clicktemp = -1;
        this.notifyDataSetChanged();
    }

    public void removeEntrity(String packageName) {
        boolean ret = false;
        for (int i = 0; i < appList.size(); i++) {
            if (appList.get(i).getPackageName().equals(
                    packageName)) {
                ret = true;
                appList.remove(i);
                break;
            }
        }
        if (ret) {
            this.notifyDataSetChanged();
        }
    }

    public void setClicktemp(int clicktemp) {
        this.clicktemp = clicktemp;
    }

    public void checkUpdate(List<AppInfo> appInfos) {
        this.appList.clear();
        MainPageActivity.dao.updateAppListIcon(appInfos);
        this.appList = MainPageActivity.dao.queryAppList();
        Logger.t(TAG).i("DBao->list" + appList.size());

        if (activity.currentAppInfo != null && activity.currentAppInfo != null)
            activity.changeCurrentApp();
        this.notifyDataSetChanged();
        Logger.t(TAG).i("AppFragment-> checkUpdate==========");
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        Bitmap bitmap = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.app_list_item, null, false);
            viewHolder.appName = (TextView) convertView
                    .findViewById(R.id.app_name);
            viewHolder.app_logo = (ImageView) convertView
                    .findViewById(R.id.app_logo);
            viewHolder.app_delete_flag = (ImageView) convertView.findViewById(R.id.app_delete_flag);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!mfragement.mMainPageActivity.hasEdit) {//编辑模式
            if (position == 0 || position == 1 || position == 2 || appList.get(position).isSystemApp()
                    || appList.get(position).getPackageName().equals(BusinessConstants.PACKAGENAME_CH_CHAT)
                    || appList.get(position).getPackageName().equals(BusinessConstants.PACKAGENAME_EN_CHAT)

                    ) {//系统应用，动作表，闹钟，相册不可以删除
                viewHolder.app_delete_flag.setVisibility(View.GONE);
            } else {
                viewHolder.app_delete_flag.setVisibility(View.VISIBLE);
            }


        } else {
            if (clicktemp == position) {
                viewHolder.app_logo
                        .setBackgroundResource(R.drawable.app_selected);
            } else {
                viewHolder.app_logo
                        .setBackgroundResource(R.drawable.app_unselected);
            }
            viewHolder.app_delete_flag.setVisibility(View.GONE);
        }

        viewHolder.appName.setText(appList.get(position).getName());


        LoadImage.setRounderConner(mfragement.getActivity(), viewHolder.app_logo, appList.get(position).getAppImagePath(), 1);
        Logger.t(TAG).d("packageName==" + appList.get(position).getPackageName() + " imagePath==" + appList.get(position).getAppImagePath());

        if (position == 0) {
            viewHolder.app_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.move_selected));
            viewHolder.appName.setText(context.getString(R.string.shop_page_action_list));
        }
        if (position == 1) {
            viewHolder.appName.setText(context.getString(R.string.alarm_title));
            viewHolder.app_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.alarm_icon));
        }
        if (position == 2) {
            viewHolder.appName.setText(context.getString(R.string.image_galley));
            viewHolder.app_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.album_icon));
        }


        return convertView;
    }

    class ViewHolder {
        TextView appName;
        TextView packgeName;
        Button btn_uninstalled;
        ImageView app_logo;
        ImageView app_delete_flag;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position,
                            long arg3) {
        // TODO Auto-generated method stub

        clicktemp = position;
        notifyDataSetChanged();
        if (!mfragement.mMainPageActivity.hasEdit && position != 0 && position != 1 && position != 2 && !appList.get(position).isSystemApp()
                && !appList.get(position).getPackageName().equals(BusinessConstants.PACKAGENAME_CH_CHAT)
                && !appList.get(position).getPackageName().equals(BusinessConstants.PACKAGENAME_EN_CHAT)) {
            if (deleDialog == null) {
                deleDialog = new CommonDiaglog(mfragement.getActivity(), true);
            }
            deleDialog.setImageViewVisble();
            if (appList.get(position).getAppImagePath() != null) {
                deleDialog.setImageView(appList.get(position).getAppImagePath());
            } else {
                deleDialog.setImageView(context.getResources().getDrawable(R.drawable.no_app));
            }
            deleDialog.show();

        } else {

            mfragement.startApp(appList.get(position));
            HashMap<String, String> map = new HashMap<String, String>();
            if (position == 0) {
                map.put("appName", context.getString(R.string.shop_page_action_list));
            } else if (position == 1) {
                map.put("appName", context.getString(R.string.alarm_title));
            } else if (position == 2) {
                map.put("appName", context.getString(R.string.image_galley));
            } else if (position == 3) {
                map.put("appName", context.getString(R.string.voice_compound_title));
            } else {
                map.put("appName", appList.get(position).getName());
            }
            MobclickAgent.onEvent(Alpha2Application.getAlpha2(), Constants.YOUMENT_MY_APP_START_TIMES, map);

        }

    }

    @Override
    public void OnNegsitiveClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void OnPositiveClick() {
        // TODO Auto-generated method stub
        if (clicktemp != -1) {
            LoadingDialog.getInstance(context).show();
            RobotApp app = appList.get(clicktemp);
            callbackListener.callback(app);

        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {

        Logger.t(TAG).i( position + "chang an  ");
        if (mfragement.mMainPageActivity.hasEdit) {//是否是编辑状态
            mfragement.mMainPageActivity.hasEdit = false;
            notifyDataSetChanged();
            mfragement.mMainPageActivity.btn_finishEdit.setVisibility(View.VISIBLE);
            mfragement.mMainPageActivity.bottomLay.setVisibility(View.GONE);
        }

        return true;

    }
}