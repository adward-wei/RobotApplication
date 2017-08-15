package com.ubtechinc.alpha2ctrlapp.ui.fragment.robot;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.base.CallbackListener;
import com.ubtechinc.alpha2ctrlapp.data.robot.SortBaseModel;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewActionInfo;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog.OnNegsitiveClick;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.CommonDiaglog.OnPositiveClick;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;

import java.util.List;

/**
 * [动作列表适配器]
 *
 * @author zengdengyi
 * @version 1.0
 * @date 2014-9-27
 **/

public class LocalActionAdpter extends BaseAdapter implements OnItemClickListener, OnItemLongClickListener, OnPositiveClick, OnNegsitiveClick {
    private Context context;
    private List<SortBaseModel> fileList;
    private List<NewActionInfo> actionList;
    private LayoutInflater inflater;
    public int clicktemp = -1;
    private long mlLastClickTime = SystemClock.uptimeMillis();
    ActionRunListener mActionRunListener;
    /**
     * 动作名称
     **/
    private String mPlayActionFileName = "";
    private MainPageActivity activity;

    private CommonDiaglog deleDialog;
    private LocalActionFragment frag;

    public String getmPlayActionFileName() {
        return mPlayActionFileName;
    }
    private  CallbackListener<NewActionInfo> calback;
    public void setmPlayActionFileName(String mPlayActionFileName) {
        this.mPlayActionFileName = mPlayActionFileName;
    }

    public LocalActionAdpter(Context context, List<SortBaseModel> fileList, LocalActionFragment frag, List<NewActionInfo> actionList, CallbackListener<NewActionInfo> calback) {
        this.fileList = fileList;
        this.context = context;
        this.frag = frag;
        this.actionList = actionList;
        activity = (MainPageActivity) context;
        inflater = LayoutInflater.from(context);
        deleDialog = new CommonDiaglog(activity, false);
        deleDialog.setNegsitiveClick(this);
        deleDialog.setPositiveClick(this);
        this.calback = calback;
        deleDialog.setMessase(context.getString(R.string.local_action_uninstall_action_tips));
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return fileList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return fileList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public void onNotifyDataSetChanged(List<SortBaseModel> fileList, List<NewActionInfo> actionList) {
        // TODO Auto-generated method stub
        this.fileList = fileList;
        this.actionList = actionList;
        clicktemp = -1;
        this.notifyDataSetChanged();
    }

    public void onClear() {
        this.fileList.clear();
        clicktemp = -1;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.movement_listview_item,
                    null, false);
            viewHolder.alphaName = (TextView) convertView
                    .findViewById(R.id.alphaName);
            viewHolder.alphaDetaile = (TextView) convertView
                    .findViewById(R.id.alphaDetaile);
            viewHolder.lvbackground = (LinearLayout) convertView
                    .findViewById(R.id.lvbackground);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (clicktemp == position) {
            viewHolder.alphaName.setTextColor(context.getResources().getColor(R.color.red));
//			viewHolder.lvbackground
//					.setBackgroundResource(R.color.sort_list_select);
        } else {
            viewHolder.alphaName.setTextColor(context.getResources().getColor(R.color.black2));
//			viewHolder.lvbackground.setBackgroundResource(Color.TRANSPARENT);
        }
        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.alphaDetaile.setVisibility(View.VISIBLE);
            viewHolder.alphaDetaile.setText(fileList.get(position)
                    .getSortLetters());
        } else {
            viewHolder.alphaDetaile.setVisibility(View.GONE);
        }

        viewHolder.alphaName.setText(fileList.get(position).getName());

        return convertView;
    }

    class ViewHolder {
        TextView alphaName;
        TextView alphaDetaile;
        LinearLayout lvbackground;

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position,
                            long arg3) {
        // TODO Auto-generated method stub
        clicktemp = position;
        notifyDataSetChanged();
        if (RobotManagerService.getInstance().isConnectedRobot()) {

            Logger.i( "onItemClick = " + position);
            boolean bTimeOut = Math.abs(SystemClock.uptimeMillis()
                    - mlLastClickTime) > 250 ? true : false;
            mlLastClickTime = SystemClock.uptimeMillis();
            if (bTimeOut) {// 防止频繁发送命令
                setmPlayActionFileName(actionList.get(position).getActionId());
                activity.onPlayAction(actionList.get(position).getActionId(), actionList.get(position).getActionName());
                Alpha2Application.getInstance().setCurrentPlayFileName(actionList.get(position).getActionName());
                if (null != mActionRunListener) {
                    mActionRunListener.actionRun(actionList.get(position).getActionId(), actionList.get(position).getActionName());
                }

            }
        }


    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return fileList.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = fileList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
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
            if (RobotManagerService.getInstance().isConnectedRobot()) {

                if (!TextUtils.isEmpty(Alpha2Application.getInstance().getCurrentPlayFileName()) && Alpha2Application.getInstance().getCurrentPlayFileName().toString().equals(fileList.get(clicktemp).getName())) {
                    activity.onStopAction();
                    Alpha2Application.getInstance().setCurrentPlayFileName("");
                }
                LoadingDialog.getInstance(context).show();
                LocalActionAdpter.this.calback.callback(actionList.get(clicktemp));


            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        // TODO Auto-generated method stub
        clicktemp = position;
        deleDialog.show();
        return true;
    }

    public int getClicktemp() {
        return clicktemp;
    }

    public void setActionRunListener(ActionRunListener actionRunListener) {
        mActionRunListener = actionRunListener;
    }

    public interface ActionRunListener {
        void actionRun(String actionId, String actionName);
    }
}
