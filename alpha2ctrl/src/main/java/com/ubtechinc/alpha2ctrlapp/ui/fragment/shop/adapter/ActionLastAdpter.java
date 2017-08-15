package com.ubtechinc.alpha2ctrlapp.ui.fragment.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionDownLoad;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionFileEntrity;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewDeviceInfo;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.app.ActionsLibPreViewActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.widget.DownloadActionAnimation;
import com.ubtechinc.alpha2ctrlapp.widget.RefreshListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionLastAdpter extends BaseAdapter implements OnItemClickListener {
    private Context context;
    private List<ActionDownLoad> appInfoList;
    private LayoutInflater inflater;
    public int clicktemp = -1;
    private String TAG = "Alpha2Adapter";
    private MainPageActivity activity;
    public static Map<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    public NewDeviceInfo deviceInfo;
    public boolean isSelcted;
    private boolean mIsActivity;
    private BaseContactActivity baseActivty;
    private RefreshListView listView;
    private long mlLastClickTime = SystemClock.uptimeMillis();
    private DownLoadListener mDownLoadListener;

    public ActionLastAdpter(Context context, List<ActionDownLoad> actionInfoList, boolean isActivity) {
        this.appInfoList = actionInfoList;
        this.context = context;
        this.mIsActivity = isActivity;
        if (!isActivity)
            this.activity = (MainPageActivity) context;
        else
            baseActivty = (BaseContactActivity) context;
        inflater = LayoutInflater.from(context);
    }

    public void setDownLoadListener(DownLoadListener downLoadListener) {
        mDownLoadListener = downLoadListener;
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
            convertView = inflater.inflate(R.layout.layout_action_item_online, null,
                    false);
            viewHolder.appImage = (ImageView) convertView.findViewById(R.id.action_logo);
            viewHolder.appName = (TextView) convertView.findViewById(R.id.txt_action_name);
            viewHolder.disc = (TextView) convertView.findViewById(R.id.txt_disc);
            viewHolder.btn_update = (ImageView) convertView.findViewById(R.id.img_state);
            viewHolder.txt_time = (TextView) convertView.findViewById(R.id.txt_time);
            viewHolder.img_type_logo = (ImageView) convertView.findViewById(R.id.img_type_logo);
            // isSelected.put(position, viewHolder.checkBox.isChecked());

            viewHolder.operatingAnim = DownloadActionAnimation.getDownloadActionAnimation(context);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(appInfoList.get(position).getActionLangName())) {
            viewHolder.appName.setText(appInfoList.get(position).getActionLangName());
        } else {
            viewHolder.appName.setText(appInfoList.get(position).getActionName());
        }
        if (TextUtils.isEmpty(appInfoList.get(position).getActionLangDesciber())) {
            if (!TextUtils.isEmpty(appInfoList.get(position).getActionDesciber()))
                viewHolder.disc.setText(appInfoList.get(position).getActionDesciber());
            else {
                viewHolder.disc.setText(context.getText(R.string.shop_page_no_description));
            }
        } else {
            viewHolder.disc.setText(appInfoList.get(position).getActionLangDesciber());
        }

        viewHolder.txt_time.setText(Tools.getActionTime(appInfoList.get(position).getActionTime()));
        switch (appInfoList.get(position).getActionType()) {
            case 1:
                viewHolder.img_type_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_small_base));
                break;
            case 2:
                viewHolder.img_type_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_small_dance));
                break;
            case 3:
                viewHolder.img_type_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_small_story));
                break;

            default:
                break;
        }
        if (!mIsActivity)
            LoadImage.LoadPicture(activity, viewHolder.appImage, appInfoList.get(position).getActionImagePath(), 2);
        else {
            LoadImage.LoadPicture((Activity) context, viewHolder.appImage, appInfoList.get(position).getActionImagePath(), 2);
        }
        if (!RobotManagerService.getInstance().isConnectedRobot()) {
            viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_dis_download));
            viewHolder.btn_update.setVisibility(View.GONE);
        } else {
            if (appInfoList.get(position).getStatus() == -1) {
                viewHolder.btn_update.setVisibility(View.GONE);
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
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.play_playing));
                    break;
                case 5:
                    viewHolder.btn_update.clearAnimation();
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_failed));
                    break;
                case BusinessConstants.APP_STATE_INSUFFCIENT_SPACE:
                    viewHolder.btn_update.clearAnimation();
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_failed));
                    break;

                default:
                    break;
            }
        }

        viewHolder.btn_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!RobotManagerService.getInstance().isConnectedRobot()) {
                    if (appInfoList.get(position).getStatus() != -1) {
                        viewHolder.img_type_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.app_dis_download));
                    }
                } else if (appInfoList.get(position).getStatus() == -1) {
                    ToastUtils.showShortToast( R.string.shop_page_disable_download);
                } else if (appInfoList.get(position).getStatus() == 0 || appInfoList.get(position).getStatus() == 2) {
                    ActionFileEntrity bean = new ActionFileEntrity();
                    bean.setActionFilePath(appInfoList.get(position).getActionPath());
                    bean.setActionId(appInfoList.get(position).getActionId());
                    bean.setActionName(appInfoList.get(position).getActionName());
                    bean.setActionType(appInfoList.get(position).getActionType());
                    if (TextUtils.isEmpty(appInfoList.get(position).getActionOriginalId())) {
                        bean.setActionOriginalId(appInfoList.get(position).getActionName());
                    } else {
                        bean.setActionOriginalId(appInfoList.get(position).getActionOriginalId());
                    }
                    activity.downLoadAction(bean, appInfoList.get(position), null);
                    if(null!=mDownLoadListener){
                        mDownLoadListener.startDownLoad(appInfoList.get(position));
                    }
//					if(!mIsActivity){
//						mMainPageActivity.currentFragment.sendRequest(bean,
//								MessageType.ALPHA_MSG_ACTIONFILE_DOWNLOAD,
//							 new String[] { AlphaClientApplication.currentAlpha2Mac });
//					}else{
//						baseActivty.sendRequest(bean,
//								MessageType.ALPHA_MSG_ACTIONFILE_DOWNLOAD,
//							 new String[] { AlphaClientApplication.currentAlpha2Mac });
//					}
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_loading));
//					viewHolder.btn_update.startAnimation(viewHolder.operatingAnim);/
                } else if (appInfoList.get(position).getStatus() == 4) {

//					if(viewHolder.isPlay){
//	    				mMainPageActivity.onStopAction();
//	    				viewHolder.isPlay  = false;
//	    				viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_stop));
//	    			}else{

                    boolean bTimeOut = Math.abs(SystemClock.uptimeMillis()
                            - mlLastClickTime) > 250 ? true : false;
                    mlLastClickTime = SystemClock.uptimeMillis();
                    if (bTimeOut) {// 防止频繁发送命令
                        if (TextUtils.isEmpty(appInfoList.get(position).getActionOriginalId())) {
                            Alpha2Application.getInstance().setShopAction(true);
                            activity.onPlayAction(appInfoList.get(position).getActionName(), viewHolder.appName.getText().toString());
                        } else {
                            Alpha2Application.getInstance().setShopAction(true);
                            activity.onPlayAction(appInfoList.get(position).getActionOriginalId(), viewHolder.appName.getText().toString());
                        }

                        viewHolder.isPlay = true;

                    }
//	    				viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.btn_stop_action_play));
//	    			}
                } else if (appInfoList.get(position).getStatus() == BusinessConstants.APP_STATE_INSUFFCIENT_SPACE) {
                    ToastUtils.showShortToast( R.string.news_storage_title);
                }


            }
        });
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position,
                            long arg3) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Logger.i( "dianji ");
        Bundle bundle = new Bundle();
        position = position - 3;
        if (null != appInfoList && appInfoList.size() > 0 && (position > 0 || position == 0) && (appInfoList.size() > position)) {
            bundle.putInt("actionid", appInfoList.get(position).getActionId());
            activity.currentFragment.replaceFragment(ActionsLibPreViewActivity.class.getName(), bundle);
        }
    }

    public class ViewHolder {
        TextView appName;
        TextView disc;
        ImageView appImage;
        ImageView btn_update;
        TextView txt_time;
        ImageView img_type_logo;
        Animation operatingAnim;
        boolean isPlay = false;
    }


    public void onNotifyDataSetChanged(List<ActionDownLoad> infoList) {
        // TODO Auto-generated method stub
        clicktemp = -1;
        appInfoList = infoList;

        this.notifyDataSetChanged();
    }

    public void onNotifyDataSetChanged(List<ActionDownLoad> infoList, RefreshListView lv) {
        // TODO Auto-generated method stub
        clicktemp = -1;
        appInfoList = infoList;
        this.listView = lv;

        this.notifyDataSetChanged();
    }

    public void onClear() {
        appInfoList.clear();
        clicktemp = -1;
        this.notifyDataSetChanged();
    }


    public interface DownLoadListener {
        void startDownLoad(ActionDownLoad actionDownLoad);
    }
}
