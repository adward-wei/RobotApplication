package com.ubtechinc.alpha2ctrlapp.ui.adapter.shop;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionDownLoad;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionFileEntrity;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewDeviceInfo;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.widget.DownloadActionAnimation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @ClassName ActionListAdpter
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 商店动作列表适配器
 * @modifier
 * @modify_time
 */
public class ActionListAdpter extends BaseAdapter {
    private Context context;
    private List<ActionDownLoad> mActionList;
    private LayoutInflater inflater;
    private String TAG = "Alpha2Adapter";
    private MainPageActivity activity;
    public static Map<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    public NewDeviceInfo deviceInfo;
    public boolean isSelcted;
    private long mlLastClickTime = SystemClock.uptimeMillis();
    private DownLoadListener mDownLoadListener;

    public ActionListAdpter(Context context,
                            List<ActionDownLoad> actionInfoList
                            ) {
        this.mActionList = actionInfoList;
        this.context = context;
        this.activity = (MainPageActivity) context;

        inflater = LayoutInflater.from(context);
    }

    public void setDownLoadListener(DownLoadListener downLoadListener) {
        mDownLoadListener = downLoadListener;
    }

    @Override
    public int getCount() {
        return mActionList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mActionList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
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
        if (!TextUtils.isEmpty(mActionList.get(position).getActionLangName())) {
            viewHolder.appName.setText(mActionList.get(position).getActionLangName());
        } else {
            viewHolder.appName.setText(mActionList.get(position).getActionName());
        }
        if (TextUtils.isEmpty(mActionList.get(position).getActionLangDesciber())) {
            if (!TextUtils.isEmpty(mActionList.get(position).getActionDesciber()))
                viewHolder.disc.setText(mActionList.get(position).getActionDesciber());
            else {
                viewHolder.disc.setText(context.getText(R.string.shop_page_no_description));
            }
        } else {
            viewHolder.disc.setText(mActionList.get(position).getActionLangDesciber());
        }

        viewHolder.txt_time.setText(Tools.getActionTime(mActionList.get(position).getActionTime()));

        LoadImage.LoadPicture(activity, viewHolder.appImage, mActionList.get(position).getActionImagePath(), 2);
        switch (mActionList.get(position).getActionType()) {
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
        if (!RobotManagerService.getInstance().isConnectedRobot()) {
            viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_dis_download));
            viewHolder.btn_update.setVisibility(View.GONE);
        } else {
            if (mActionList.get(position).getStatus() == BusinessConstants.APP_STATE_ERROR) {
                viewHolder.btn_update.setVisibility(View.GONE);
            } else {
                viewHolder.btn_update.setVisibility(View.VISIBLE);
            }

            switch (mActionList.get(position).getStatus()) {
                case BusinessConstants.APP_STATE_ERROR:
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.disable_download));
                    break;
                case BusinessConstants.APP_STATE_INIT:
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.download_state));
                    break;
                case BusinessConstants.APP_STATE_DOWNLOADING:
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_loading));
                    break;
                case BusinessConstants.APP_STATE_DOWNLOAD_SUCCESS:
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_installing));
                    break;
                case BusinessConstants.APP_STATE_DOWNLOAD_FAIL:
                    viewHolder.btn_update.clearAnimation();
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_failed));
                    break;
                case BusinessConstants.APP_STATE_INSTALL_SUCCESS:
                    viewHolder.btn_update.clearAnimation();
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.play_playing));
                    break;
                case BusinessConstants.APP_STATE_INSTALL_FAIL:
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
                    if (mActionList.get(position).getStatus() !=  BusinessConstants.APP_STATE_ERROR) {
                        viewHolder.img_type_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.app_dis_download));
                    }
                } else if (mActionList.get(position).getStatus() == BusinessConstants.APP_STATE_ERROR) {
                    ToastUtils.showShortToast( R.string.shop_page_disable_download);
                } else if (mActionList.get(position).getStatus() == BusinessConstants.APP_STATE_INIT || mActionList.get(position).getStatus() == BusinessConstants.APP_STATE_DOWNLOAD_SUCCESS) {
                    ActionFileEntrity bean = new ActionFileEntrity();
                    bean.setActionFilePath(mActionList.get(position).getActionPath());
                    bean.setActionId(mActionList.get(position).getActionId());
                    bean.setActionName(mActionList.get(position).getActionName());
                    bean.setActionType(mActionList.get(position).getActionType());
                    if (TextUtils.isEmpty(mActionList.get(position).getActionOriginalId())) {
                        bean.setActionOriginalId(mActionList.get(position).getActionName());
                    } else {
                        bean.setActionOriginalId(mActionList.get(position).getActionOriginalId());
                    }
                    activity.downLoadAction(bean, mActionList.get(position), null);
                    if(null!= mDownLoadListener){
                        mDownLoadListener.startDownLoad(mActionList.get(position));
                    }
                    viewHolder.btn_update.setImageDrawable(context.getResources().getDrawable(R.drawable.app_loading));
                } else if (mActionList.get(position).getStatus() == BusinessConstants.APP_STATE_INSTALL_SUCCESS) {
                    boolean bTimeOut = Math.abs(SystemClock.uptimeMillis()
                            - mlLastClickTime) > 250 ? true : false;
                    mlLastClickTime = SystemClock.uptimeMillis();
                    if (bTimeOut) {// 防止频繁发送命令
                        if (TextUtils.isEmpty(mActionList.get(position).getActionOriginalId())) {
                            Alpha2Application.getInstance().setShopAction(true);
                            activity.onPlayAction(mActionList.get(position).getActionName(), viewHolder.appName.getText().toString());
                        } else {
                            Alpha2Application.getInstance().setShopAction(true);
                            activity.onPlayAction(mActionList.get(position).getActionOriginalId(), viewHolder.appName.getText().toString());
                        }

                    }

                }


            }
        });
        return convertView;
    }



    public class ViewHolder {
        TextView appName;
        TextView disc;
        ImageView appImage;
        ImageView btn_update;
        TextView txt_time;
        ImageView img_type_logo;
        Animation operatingAnim;
    }




    public void onClear() {
        mActionList.clear();
        this.notifyDataSetChanged();
    }

    public interface DownLoadListener {
        void startDownLoad(ActionDownLoad actionDownLoad);
    }
}
