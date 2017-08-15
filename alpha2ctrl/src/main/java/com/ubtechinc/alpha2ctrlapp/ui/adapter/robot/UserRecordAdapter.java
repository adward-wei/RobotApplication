package com.ubtechinc.alpha2ctrlapp.ui.adapter.robot;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.TimeUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RecordResultInfo;
import com.ubtechinc.alpha2ctrlapp.util.Tools;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author：liuhai
 * @date：2017/4/15 9:32
 * @modifier：liuhai
 * @modify_date：2017/4/15 9:32
 * [A brief description]
 * version
 */

public class UserRecordAdapter extends BaseAdapter {

    private static final String TAG = "UserRecordAdapter";
    private static final int LabelId_dance = 8;
    private static final int LabelId_action = 9;
    private static final int LabelId_music = 12;
    private static final int LabelId_story = 13;
    private List<RecordResultInfo> mAppInfos;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    OnItemRecordListener mOnItemRecordListener;


    public UserRecordAdapter(Context context, List<RecordResultInfo> appInfos) {
        this.mContext = context;
        mAppInfos = appInfos;
        mLayoutInflater = LayoutInflater.from(context);
    }


    public void setAppInfos(List<RecordResultInfo> appInfos) {
        mAppInfos = appInfos;
        this.notifyDataSetChanged();
    }

    public List<RecordResultInfo> getAppInfos() {
        return mAppInfos;
    }


    public void setOnItemRecordListener(OnItemRecordListener onItemRecordListener) {
        this.mOnItemRecordListener = onItemRecordListener;
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_use_record_layout, null);
            holder = new ViewHolder();
            holder.tvUserWord = (TextView) convertView.findViewById(R.id.tv_user_word);
            holder.tvRobotWord = (TextView) convertView.findViewById(R.id.tv_robot_word);
            holder.relativeDate = (RelativeLayout) convertView.findViewById(R.id.rl_date);
            holder.tvDateYear = (TextView) convertView.findViewById(R.id.tv_date_year);
            holder.tvDateMonth = (TextView) convertView.findViewById(R.id.tv_date_month);
            holder.tvDateDay = (TextView) convertView.findViewById(R.id.tv_date_day);
            holder.llLinkLayout = (RelativeLayout) convertView.findViewById(R.id.ll_bottom_link);
            holder.tvLink = (TextView) convertView.findViewById(R.id.tv_link);
            holder.ivRelpay = (ImageView) convertView.findViewById(R.id.iv_replay);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RecordResultInfo recordResultInfo = mAppInfos.get(position);
        //2017-06-29 16:47:08
        String originTime = recordResultInfo.getCreateTime();
        String date = TimeUtils.getTime(TimeUtils.string2Millis(recordResultInfo.getCreateTime(), TimeUtils.DEFAULT_DATE_FORMAT.toPattern()), TimeUtils.DATE_FORMAT_DATE);
        /**
         * 当前日期是昨天还是之前日期
         */
        int todayType = daysBetween(TimeUtils.string2Millis(recordResultInfo.getCreateTime(), TimeUtils.DEFAULT_DATE_FORMAT.toPattern()), System.currentTimeMillis());
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Logger.d("adpater", "currentYear===" + currentYear);
        Logger.d("adpater", "day===" + todayType);
        Logger.d("adpater", TimeUtils.getTime(TimeUtils.string2Millis(recordResultInfo.getCreateTime(), TimeUtils.DEFAULT_DATE_FORMAT.toPattern())));
        if (position == getPositionForSection(date)) {
            holder.relativeDate.setVisibility(View.VISIBLE);
            if (todayType == 1) {
                holder.relativeDate.setVisibility(View.GONE);
            } else if (todayType == 2) {
                holder.tvDateYear.setVisibility(View.GONE);
                holder.tvDateMonth.setVisibility(View.GONE);
                holder.tvDateDay.setText(mContext.getString(R.string.time_yesterday));
            } else if (todayType == 3) {
                holder.tvDateYear.setVisibility(View.VISIBLE);
                holder.tvDateMonth.setVisibility(View.GONE);

                Calendar calendar = null;


                    calendar =  TimeUtils.str2Calendar(recordResultInfo.getCreateTime(), TimeUtils.DEFAULT_DATE_FORMAT.toPattern());


                ;
                int day = calendar.get(calendar.DAY_OF_MONTH);
                String dayStr = "";
                if (day < 10) {
                    dayStr = "0" + day;
                } else {
                    dayStr = day + "";
                }
                String monthStr = calendar.get(Calendar.MONTH) + 1 + "月";
                String day_month_tr = dayStr + monthStr;
                SpannableString monthstyled = new SpannableString(day_month_tr);
                monthstyled.setSpan(new TextAppearanceSpan(mContext, R.style.day_month_style), dayStr.length(), day_month_tr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                monthstyled.setSpan(new TextAppearanceSpan(mContext, R.style.day_style), 0, dayStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                holder.tvDateDay.setText(monthstyled);
                /**
                 * 如果是今年则不显示年
                 */
                if (currentYear != calendar.get(Calendar.YEAR)) {
                    String year = calendar.get(Calendar.YEAR) + "年";
                    SpannableString styledText = new SpannableString(year);
                    styledText.setSpan(new TextAppearanceSpan(mContext, R.style.style1), year.length() - 1, year.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.tvDateYear.setText(styledText);
                } else {
                    holder.tvDateYear.setVisibility(View.GONE);
                }
            }
        } else {
            holder.relativeDate.setVisibility(View.GONE);
        }
        holder.tvUserWord.setText(mAppInfos.get(position).getUserMsg());
        if (recordResultInfo.getLabelId() == LabelId_dance) {//跳舞
            holder.llLinkLayout.setVisibility(View.VISIBLE);
            holder.ivRelpay.setImageResource(R.drawable.ic_replay_dance);
            holder.tvLink.setText(recordResultInfo.getPushContent());
            holder.tvRobotWord.setText(mAppInfos.get(position).getRobotMsg());
        } else if (recordResultInfo.getLabelId() == LabelId_action) {//动作
            holder.llLinkLayout.setVisibility(View.VISIBLE);
            holder.ivRelpay.setImageResource(R.drawable.ic_replay_action);
            holder.tvLink.setText(recordResultInfo.getPushContent());
            holder.tvRobotWord.setText(mAppInfos.get(position).getRobotMsg());
        } else if (recordResultInfo.getLabelId() == LabelId_music) {//音乐
            holder.llLinkLayout.setVisibility(View.VISIBLE);
            holder.ivRelpay.setImageResource(R.drawable.ic_sing);
            holder.tvLink.setText(recordResultInfo.getPushContent());
            holder.tvRobotWord.setText(mAppInfos.get(position).getRobotMsg());
        } else if (recordResultInfo.getLabelId() == LabelId_story) {//故事
            holder.llLinkLayout.setVisibility(View.VISIBLE);
            holder.ivRelpay.setImageResource(R.drawable.ic_replay_story);
            holder.tvLink.setText(recordResultInfo.getPushContent());
            holder.tvRobotWord.setText(mAppInfos.get(position).getRobotMsg());
        } else {
            holder.llLinkLayout.setVisibility(View.GONE);
            holder.tvRobotWord.setText(mAppInfos.get(position).getPushContent());
        }

        holder.ivRelpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemRecordListener && null != mAppInfos.get(position)) {
                    if (!Tools.isFastClick(5000)) {
                        mOnItemRecordListener.playRecord(mAppInfos.get(position));
                        startGifLoading(mAppInfos.get(position).getLabelId(), holder.ivRelpay);
                    } else {
                        ToastUtils.showShortToast(mContext.getString(R.string.toast_no_repeat_play), 1000);
                    }
                }
            }
        });

        return convertView;
    }


    /**
     * 点击播放按钮启动gif动画
     *
     * @param labelId
     * @param imageView
     */
    private void startGifLoading(int labelId, ImageView imageView) {
        int drawableId = R.drawable.ic_sing_play;
        switch (labelId) {
            case LabelId_dance:
                drawableId = R.drawable.ic_dance_play;
                break;
            case LabelId_action:
                drawableId = R.drawable.ic_action_play;
                break;
            case LabelId_music:
                drawableId = R.drawable.ic_sing_play;
                break;

            case LabelId_story:
                drawableId = R.drawable.ic_story_play;
                break;
        }

        Glide.with(mContext)
                .load(drawableId)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<Integer, GlideDrawable>() {

                    @Override
                    public boolean onException(Exception arg0, Integer arg1,
                                               Target<GlideDrawable> arg2, boolean arg3) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource,
                                                   Integer model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        // 计算动画时长
                        GifDrawable drawable = (GifDrawable) resource;
                        GifDecoder decoder = drawable.getDecoder();

                        int duration = 0;
                        for (int i = 0; i < drawable.getFrameCount(); i++) {
                            duration += decoder.getDelay(i);
                        }
                        //发送延时消息，通知动画结束
                        mHandler.sendEmptyMessageDelayed(1,
                                duration);
                        return false;
                    }
                }) //仅仅加载一次gif动画
                .into(new GlideDrawableImageViewTarget(imageView, 1));


    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    UserRecordAdapter.this.notifyDataSetChanged();
                    break;
            }
        }
    };


    private class ViewHolder {
        private RelativeLayout relativeDate;
        private RelativeLayout llLinkLayout;
        private TextView tvDateMonth;
        private TextView tvDateYear;
        private TextView tvDateDay;
        private TextView tvUserWord;
        private TextView tvRobotWord;
        private TextView tvLink;
        private ImageView ivRelpay;


    }

    /**
     * 获取catalog首次出现位置
     */
    public int getPositionForSection(String catalog) {

        for (int i = 0; i < getCount(); i++) {

            String sortStr = TimeUtils.getTime(TimeUtils.string2Millis(mAppInfos.get(i).getCreateTime(), TimeUtils.DEFAULT_DATE_FORMAT.toPattern()), TimeUtils.DATE_FORMAT_DATE);
            if (catalog.equals(sortStr)) {
                return i;
            }
        }
        return -1;
    }

    public interface OnItemRecordListener {
         void playRecord(RecordResultInfo recordResultInfo);
    }

    public   int daysBetween(long timetemp, long currenttime) {

        Date early = new Date(timetemp);
        Date late = new Date(currenttime);
        java.util.Calendar calst = java.util.Calendar.getInstance();
        java.util.Calendar caled = java.util.Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        //设置时间为0时
        calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calst.set(java.util.Calendar.MINUTE, 0);
        calst.set(java.util.Calendar.SECOND, 0);
        caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
        caled.set(java.util.Calendar.MINUTE, 0);
        caled.set(java.util.Calendar.SECOND, 0);
        //得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
                .getTime().getTime() / 1000)) / 3600 / 24;
        if(days==0){
            return 1;
        }else if(days==1){
            return 2;
        }else{
            return 3;
        }

    }

}
