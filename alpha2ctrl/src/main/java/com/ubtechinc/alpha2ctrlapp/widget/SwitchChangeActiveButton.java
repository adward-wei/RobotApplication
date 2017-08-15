package com.ubtechinc.alpha2ctrlapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;

public class SwitchChangeActiveButton extends LinearLayout implements OnClickListener{
	
	private TextView btn_app,btn_active;
	private ImageView line1,line2;
	private Integer selectedId;
	private OnSwitchChangedActiveListener changedListener;
	private Context mContext;
	public SwitchChangeActiveButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	

	public SwitchChangeActiveButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.main_send_title, this, true);
		mContext = context;
		btn_app = (TextView) findViewById(R.id.btn_app);
		btn_app.setOnClickListener(this);
		btn_active = (TextView) findViewById(R.id.btn_active);
		btn_active.setOnClickListener(this);
		line1 = (ImageView) findViewById(R.id.app_line);
		line2 = (ImageView) findViewById(R.id.active_line);
		selectedId = R.id.btn_app;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Integer index = 0;
		Integer eventId = v.getId();
		if (eventId == selectedId) {
			// 重复按下选择按钮
			return;
		}else{
			switch (eventId) {
			case R.id.btn_app:
				changeActive(0);
				break;
			case R.id.btn_active:
				changeActive(1);
				break;
			default:
				break;
			}
		}
	}
	public void changeActive(int currentMode){
		
		switch (currentMode) {
		case 0:
			selectedId = R.id.btn_app;
			btn_app.setTextColor(mContext.getResources().getColor(R.color.main_page_active_select));
			btn_active.setTextColor(mContext.getResources().getColor(R.color.main_page_active_unselect));
			btn_app.setBackgroundColor(mContext.getResources().getColor(R.color.white));
			btn_active.setBackgroundColor(mContext.getResources().getColor(R.color.main_page_active_unselect_bg));
			break;
		case 1:
			selectedId = R.id.btn_active;
			btn_app.setTextColor(mContext.getResources().getColor(R.color.main_page_active_unselect));
			btn_active.setTextColor(mContext.getResources().getColor(R.color.main_page_active_select));
			btn_app.setBackgroundColor(mContext.getResources().getColor(R.color.main_page_active_unselect_bg));
			btn_active.setBackgroundColor(mContext.getResources().getColor(R.color.white));
			break;

		default:
			break;
		}

		if(changedListener!=null){
			changedListener.onChangeActive(currentMode);
		}
		
	}
	public interface OnSwitchChangedActiveListener {
		public void onChangeActive(Integer index);
	}

	public OnSwitchChangedActiveListener getChangedListener() {
		return changedListener;
	}

	public void setChangedLanListener(OnSwitchChangedActiveListener changedListener) {
		this.changedListener = changedListener;
	}
}

