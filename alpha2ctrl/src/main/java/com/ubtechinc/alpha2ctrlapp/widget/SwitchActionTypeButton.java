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

public class SwitchActionTypeButton extends LinearLayout implements OnClickListener{
	
	private TextView btn_nursery,btn_story,btn_dance,btn_education,btn_sport;
	private ImageView nurseryLine,storyLine,danceLine,educationLine,sportLine;
	private Integer selectedId;
	private OnSwitchChangedTypeListener changedListener;
	private Context mContext;
	public SwitchActionTypeButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	

	public SwitchActionTypeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.switch_action_type_layout, this, true);
		mContext = context;
		btn_nursery = (TextView) findViewById(R.id.btn_nursery);
		btn_nursery.setOnClickListener(this);
		nurseryLine = (ImageView) findViewById(R.id.nursery_line);
		
		btn_story = (TextView) findViewById(R.id.btn_story);
		btn_story.setOnClickListener(this);
		
		storyLine = (ImageView) findViewById(R.id.story_line);
		
		btn_dance = (TextView) findViewById(R.id.btn_dance);
		btn_dance.setOnClickListener(this);
		danceLine = (ImageView) findViewById(R.id.dance_line);
		
		btn_education = (TextView) findViewById(R.id.btn_education);
		btn_education.setOnClickListener(this);
		educationLine = (ImageView) findViewById(R.id.education_line);
		
		btn_sport = (TextView) findViewById(R.id.btn_sport);
		btn_sport.setOnClickListener(this);
		sportLine = (ImageView) findViewById(R.id.sport_line);
		
		selectedId = R.id.btn_nursery;
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
			case R.id.btn_nursery:
				changeActive(0);
				break;
			case R.id.btn_story:
				changeActive(1);
				break;
			case R.id.btn_dance:
				changeActive(2);
				break;
			case R.id.btn_education:
				changeActive(3);
				break;
			case R.id.btn_sport:
				changeActive(4);
				break;
		
			default:
				break;
			}
		}
	}
	public void changeActive(int currentMode){
		
		switch (currentMode) {
		case 0:
			selectedId = R.id.btn_nursery;
			btn_nursery.setTextColor(mContext.getResources().getColor(R.color.text_color_t5));
			nurseryLine.setBackgroundColor(mContext.getResources().getColor(R.color.text_color_t5));
			
			btn_story.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			storyLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			
			btn_dance.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			danceLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			
			btn_education.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			educationLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			
			btn_sport.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			sportLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));

			break;
		case 1:
			selectedId = R.id.btn_story;
			btn_nursery.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			nurseryLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			
			btn_story.setTextColor(mContext.getResources().getColor(R.color.text_color_t5));
			storyLine.setBackgroundColor(mContext.getResources().getColor(R.color.text_color_t5));
			
			btn_dance.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			danceLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			
			btn_education.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			educationLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			
			btn_sport.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			sportLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			break;
		case 2:
			selectedId = R.id.btn_dance;
			btn_nursery.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			nurseryLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			
			btn_story.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			storyLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			
			btn_dance.setTextColor(mContext.getResources().getColor(R.color.text_color_t5));
			danceLine.setBackgroundColor(mContext.getResources().getColor(R.color.text_color_t5));
			
			btn_education.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			educationLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			
			btn_sport.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			sportLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			break;
		case 3:
			selectedId = R.id.btn_education;
			btn_nursery.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			nurseryLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			
			btn_story.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			storyLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			
			btn_dance.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			danceLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			
			btn_education.setTextColor(mContext.getResources().getColor(R.color.text_color_t5));
			educationLine.setBackgroundColor(mContext.getResources().getColor(R.color.text_color_t5));
			
			btn_sport.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			sportLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			break;
		case 4:
			selectedId = R.id.btn_story;
			btn_nursery.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			nurseryLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			
			btn_story.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			storyLine.setBackgroundColor(mContext.getResources().getColor(R.color.text_color_t1));
			
			btn_dance.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			danceLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			
			btn_education.setTextColor(mContext.getResources().getColor(R.color.text_color_t1));
			educationLine.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_background));
			
			btn_sport.setTextColor(mContext.getResources().getColor(R.color.text_color_t5));
			sportLine.setBackgroundColor(mContext.getResources().getColor(R.color.text_color_t5));
			break;
		default:
			break;
		}

		if(changedListener!=null){
			changedListener.onChangeActive(currentMode);
		}
		
	}
	public interface OnSwitchChangedTypeListener {
		public void onChangeActive(Integer index);
	}

	public OnSwitchChangedTypeListener getChangedTypeListener() {
		return changedListener;
	}

	public void setChangedTypeListener(OnSwitchChangedTypeListener changedListener) {
		this.changedListener = changedListener;
	}
}

