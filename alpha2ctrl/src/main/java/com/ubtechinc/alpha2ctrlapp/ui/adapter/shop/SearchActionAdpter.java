package com.ubtechinc.alpha2ctrlapp.ui.adapter.shop;

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
import com.ubtechinc.alpha2ctrlapp.base.IDataCallback;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewActionInfo;
import com.ubtechinc.alpha2ctrlapp.service.RobotManagerService;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.shop.SearchFragment;
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

public class SearchActionAdpter extends BaseAdapter implements OnItemClickListener, OnItemLongClickListener, OnPositiveClick, OnNegsitiveClick {
    private Context context;
    private List<NewActionInfo> actionList;
    private LayoutInflater inflater;
    public int clicktemp = -1;
    private long mlLastClickTime = SystemClock.uptimeMillis();
    /**
     * 动作名称
     **/
    private String mPlayActionFileName = "";
    private MainPageActivity activity;

    private CommonDiaglog deleDialog;
    private SearchFragment frag;
    IDataCallback<String> callback;
    public String getmPlayActionFileName() {
        return mPlayActionFileName;
    }

    public void setmPlayActionFileName(String mPlayActionFileName) {
        this.mPlayActionFileName = mPlayActionFileName;
    }

    public SearchActionAdpter(Context context, SearchFragment frag, List<NewActionInfo> actionList, IDataCallback<String> callback) {
        this.context = context;
        this.frag = frag;
        this.actionList = actionList;
        activity = (MainPageActivity) context;
        inflater = LayoutInflater.from(context);
        deleDialog = new CommonDiaglog(activity, false);
        deleDialog.setNegsitiveClick(this);
        deleDialog.setPositiveClick(this);
        this.callback = callback;
        deleDialog.setMessase(context.getString(R.string.local_action_uninstall_action_tips));
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return actionList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return actionList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public void onNotifyDataSetChanged(List<NewActionInfo> actionList) {

        this.actionList = actionList;
        clicktemp = -1;
        this.notifyDataSetChanged();
    }

    public void onClear() {
        this.actionList.clear();
        clicktemp = -1;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.search_item,
                    null, false);
            viewHolder.alphaName = (TextView) convertView
                    .findViewById(R.id.alphaName);
            viewHolder.lvbackground = (LinearLayout) convertView
                    .findViewById(R.id.lvbackground);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (clicktemp == position) {
            viewHolder.alphaName.setTextColor(context.getResources().getColor(R.color.red));
            ;
        } else {
            viewHolder.alphaName.setTextColor(context.getResources().getColor(R.color.black2));

        }

        viewHolder.alphaName.setText(actionList.get(position).getActionName());

        return convertView;
    }

    class ViewHolder {
        TextView alphaName;
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
            }
        }


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

                if (!TextUtils.isEmpty(Alpha2Application.getInstance().getCurrentPlayFileName()) && Alpha2Application.getInstance().getCurrentPlayFileName().equals(actionList.get(clicktemp).getActionName())) {
                    activity.onStopAction();
                    Alpha2Application.getInstance().setCurrentPlayFileName("");
                }
                LoadingDialog.getInstance(context).show();
                callback.onCallback(actionList.get(clicktemp).getActionId());


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

}
