package com.ubtechinc.alpha2ctrlapp.ui.fragment.robot;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RecordResultInfo;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.widget.DownloadActionAnimation;

import java.util.List;

/**
 * @author：liuhai
 * @date：2017/4/15 9:32
 * @modifier：liuhai
 * @modify_date：2017/4/15 9:32
 * [A brief description]
 * version
 */

public class TranlateAdapter extends BaseAdapter {
    private List<RecordResultInfo> mAppInfos;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemRecordListener mOnItemRecordListener;
    private int replayPosition = -1;
    private boolean completereplay;


    public TranlateAdapter(Context context, List<RecordResultInfo> appInfos) {
        this.mContext = context;
        mAppInfos = appInfos;
        mLayoutInflater = LayoutInflater.from(context);
    }


    /**
     * 设置List数据并刷新
     *
     * @param appInfos
     */
    public void setAppInfos(List<RecordResultInfo> appInfos) {
        mAppInfos = appInfos;
        this.notifyDataSetChanged();
    }

    public int getReplayPosition() {
        return replayPosition;
    }

    public void setReplayPosition(int replayPosition) {
        this.replayPosition = replayPosition;
    }

    public boolean isCompletereplay() {
        return completereplay;
    }

    public void setCompletereplay(boolean completereplay) {
        this.completereplay = completereplay;
    }

    public List<RecordResultInfo> getAppInfos() {
        return mAppInfos;
    }

    /**
     * 设置回调接口
     *
     * @param onItemRecordListener
     */
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
            convertView = mLayoutInflater.inflate(R.layout.item_tranlate_layout, null);
            holder = new ViewHolder();
            holder.ivPlay = (ImageView) convertView.findViewById(R.id.iv_play);
            holder.tvUserWord = (TextView) convertView.findViewById(R.id.tv_tranlate_user);
            holder.tvRobotWord = (TextView) convertView.findViewById(R.id.tv_tranlate_robot);
            holder.rlPlay = (RelativeLayout) convertView.findViewById(R.id.rl_play);
            holder.operatingAnim = DownloadActionAnimation.getPlayActionAnimation(mContext);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (completereplay && replayPosition == position) {
            holder.ivPlay.setImageDrawable(mContext.getResources().getDrawable(R.drawable.replay_statu));
            holder.ivPlay.startAnimation(holder.operatingAnim);
        } else {
            holder.ivPlay.setImageDrawable(mContext.getResources().getDrawable(R.drawable.record_play));
            holder.ivPlay.clearAnimation();
        }

        holder.tvUserWord.setText(mAppInfos.get(position).getUserMsg());
        holder.tvRobotWord.setText(mAppInfos.get(position).getPushContent());
        holder.rlPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemRecordListener && null != mAppInfos.get(position)) {
                    if (!Tools.isFastClick(5000)) {
                        mOnItemRecordListener.playRecord(mAppInfos.get(position));
                        holder.ivPlay.setImageDrawable(mContext.getResources().getDrawable(R.drawable.replay_statu));
                        startGifLoading(holder.ivPlay);
                    } else {
                        ToastUtils.showShortToast(  mContext.getString(R.string.toast_no_repeat_play));
                    }
                }
            }
        });

        return convertView;
    }

    /**
     * 点击播放按钮启动gif动画
     *
     * @param imageView
     */
    private void startGifLoading(ImageView imageView) {

        Glide.with(mContext)
                .load(R.drawable.ic_tranlate_play)
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
                .into(new GlideDrawableImageViewTarget(imageView, 2));


    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    TranlateAdapter.this.notifyDataSetChanged();
                    break;
            }
        }
    };

    private class ViewHolder {
        private ImageView ivPlay;
        private TextView tvUserWord;
        private TextView tvRobotWord;
        private RelativeLayout rlPlay;
        Animation operatingAnim;
    }

    public interface OnItemRecordListener {
        public void playRecord(RecordResultInfo recordResultInfo);
    }
}
