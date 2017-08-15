package com.ubtechinc.alpha2ctrlapp.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;

import java.util.ArrayList;
import java.util.List;

public class GuiderPager extends LinearLayout implements OnPageChangeListener {

	
	private List<ImageView> imageViewList = new ArrayList <ImageView>();
	private Context mContext;
	private ImageView[] img_logo_index;
	private Drawable [] drwableList ;
	private ViewPager viewPage;
	private boolean isTouch = false;
	private TextView guideTip1,guideTip2;
	private RelativeLayout goLay;
	private float beforeX;
	private int temp = 0;
	public GuiderPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		LayoutInflater.from(context).inflate(R.layout.guide_view_page, this, true);
		viewPage = (ViewPager)findViewById(R.id.commonViewPager);

		
	}
	
   	public void change(){
   		if(!isTouch)
   			viewPage.setCurrentItem(viewPage.getCurrentItem() + 1);
   	}
	public void initView(RelativeLayout goLay,TextView tips1,TextView tips2) {
		this.goLay = goLay;
		this.guideTip1 = tips1;
		this.guideTip2 = tips2;
    	prepareData();
    	ViewPagerAdapter adapter = new ViewPagerAdapter();
    	viewPage.setAdapter(adapter);
    	viewPage.setOnPageChangeListener(this);
    	/**
    	 * 2147483647 / 2 = 1073741820 - 1 
    	 */
    	int n = Integer.MAX_VALUE / 2 % imageViewList.size();
    	int itemPosition = Integer.MAX_VALUE / 2 - n;
    	
    	viewPage.setCurrentItem(itemPosition);
	}
	
	 private void prepareData() {
		 	if(imageViewList.size()>0)
		 		imageViewList.clear();
	    	ImageView iv;
	    	for (int i = 0; i < drwableList.length; i++) {
				iv = new ImageView(mContext);
				iv.setScaleType(ScaleType.FIT_XY);
				iv.setImageDrawable(drwableList[i]);
				imageViewList.add(iv);
			}
	    }
	    
	    
	    class ViewPagerAdapter extends PagerAdapter {

			@Override
			public int getCount() {
				return Integer.MAX_VALUE;
			}

			/**
			 * 判断出去的view是否等于进来的view 如果为true直接复用
			 */
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			/**
			 * 销毁预加载以外的view对象, 会把需要销毁的对象的索引位置传进来就是position
			 */
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView(imageViewList.get(position % imageViewList.size()));
			}

			/**
			 * 创建一个view
			 */
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(imageViewList.get(position % imageViewList.size()));
				return imageViewList.get(position % imageViewList.size());
			}
	    }

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int position) {
			setImageIndex(position % imageViewList.size());// 把当前选中的position对应的点置为enabled状态
			isTouch = true;
		}
		// -----禁止左滑-------左滑：上一次坐标 > 当前坐标

		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {

				switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:// 按下如果‘仅’作为‘上次坐标’，不妥，因为可能存在左滑，motionValue大于0的情况（来回滑，只要停止坐标在按下坐标的右边，左滑仍然能滑过去）
					beforeX = ev.getX();
					break;
				case MotionEvent.ACTION_MOVE:
					if(temp==3){
						return true;// 最后一页的时候禁止左滑
					}else{
						float motionValue = ev.getX() - beforeX;
						if (motionValue > 0 && temp ==0) {// 禁止左滑
							return true;
						}else if(motionValue < 0 && temp ==3){
							return true;
						}
						beforeX = ev.getX();// 手指移动时，再把当前的坐标作为下一次的‘上次坐标’，解决上述问题
					}

					break;
				default:
					break;
				}
				return super.dispatchTouchEvent(ev);
		}
		public void setImageIndex(int index) {
			for (int i = 0; i < img_logo_index.length; i++) {
				if (index != i) {
					img_logo_index[i].setVisibility(View.VISIBLE);
					img_logo_index[i].setBackgroundResource(R.drawable.guide_pont_unselect);
				} else {
					img_logo_index[i].setBackgroundResource(R.drawable.guide_pont_select);
				}
			}
			ChangeOtherView(index);
		}
		public void  ChangeOtherView(int index){
			temp = index;
			switch (index) {
			case 0:
				guideTip1.setText(R.string.guide_page1_tips1);
				guideTip2.setText(R.string.guide_page1_tips2);
				break;
			case 1:
				guideTip1.setText(R.string.guide_page2_tips1);
				guideTip2.setText(R.string.guide_page2_tips2);
				break;
			case 2:
				guideTip1.setText(R.string.guide_page3_tips1);
				guideTip2.setText(R.string.guide_page3_tips2);
				break;
			case 3:
				guideTip1.setText(R.string.guide_page4_tips1);
				guideTip2.setText(R.string.guide_page4_tips2);
				break;


			default:
				break;
			}
			if(index!=3){
				goLay.setVisibility(View.GONE);
			}else {
				goLay.setVisibility(View.VISIBLE);
			}
		}
		public ImageView[] getImg_logo_index() {
			return img_logo_index;
		}
		public void setImg_logo_index(ImageView[] img_logo_index) {
			this.img_logo_index = img_logo_index;
		}
		public Drawable[] getDrwableList() {
			return drwableList;
		}
		public void setDrwableList(Drawable[] drwableList) {
			this.drwableList = drwableList;
		}


		public boolean isTouch() {
			return isTouch;
		}

		public void setTouch(boolean isTouch) {
			this.isTouch = isTouch;
		}
		

}
