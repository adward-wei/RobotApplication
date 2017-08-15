package com.ubtechinc.alpha2ctrlapp.ui.adapter.robot;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.DeskClock;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.robot.ActionSheet;
import com.ubtechinc.alpha2ctrlapp.util.AlarmUtil;

import java.util.List;

public class AlarmListAdapter extends BaseSwipeAdapter {
	private LayoutInflater mInflater;
	private List<DeskClock> mList;
	private Activity mContext;
	private ISlideDeleteListener2 deleteListener;
	public AlarmListAdapter(Activity context, List<DeskClock> list, ISlideDeleteListener2 deleteListener) {
		this.mList = list;
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.deleteListener = deleteListener;
	}

	static class ViewHolder {
		TextView date;
		TextView time;
		TextView msg;
		TextView repeat;
		TextView delete;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public DeskClock getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void fillValues(int position, View convertView) {
		DeskClock dc = this.getItem(position);
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.msg.setText(dc.getMessage());
		holder.date.setText(AlarmUtil.getDeskClockDateStr(dc));
		holder.time.setText(AlarmUtil.getDeskClockTimeStr(dc));
		holder.delete.setOnClickListener(clickListener);
		holder.repeat.setText(AlarmUtil.getRepeatTypeStr(mContext, dc));
	}

	@Override
	public View generateView(int position, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		View convertView = mInflater
				.inflate(R.layout.alarm_list, parent, false);
		holder.date = (TextView) convertView.findViewById(R.id.date);
		holder.time = (TextView) convertView.findViewById(R.id.time);
		holder.repeat = (TextView) convertView
				.findViewById(R.id.alarm_time_repeat);
		holder.msg = (TextView) convertView.findViewById(R.id.alarm_remark);
		holder.delete = (TextView) convertView.findViewById(R.id.delete);
		holder.delete.setTag(position);
		convertView.setTag(holder);
		return convertView;
	}

	@Override
	public int getSwipeLayoutResourceId(int position) {
		return R.id.swipe;
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int pos = (Integer) v.getTag();
		
			deleteListener.slideDelete(mList.get(pos));
		}

	};

	private ActionSheet.ActionSheetListener listener = new ActionSheet.ActionSheetListener() {

		@Override
		public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

		}

		@Override
		public void onOtherButtonClick(ActionSheet actionSheet, int index) {
			// mList.remove(location);
		}

	};

	public interface ISlideDeleteListener2 {
		public void slideDelete(DeskClock clock);
	}

}
