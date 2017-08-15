package com.ubtechinc.alpha2ctrlapp.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class PicScanView extends LinearLayout implements OnPageChangeListener {

	private List<ImageView> imageViewList = new ArrayList<ImageView>();
	private Context mContext;
	private String[] drwableList;
	private ViewPager viewPage;
	private TextView currentNo, totalNo;
	private float beforeX;
	private boolean isCanScroll = true;
	private boolean isFirst = false;

	public PicScanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		LayoutInflater.from(context).inflate(R.layout.pic_scan_layout, this,
				true);
		viewPage = (ViewPager) findViewById(R.id.picviewpager);
		currentNo = (TextView) findViewById(R.id.current_pic_num);
		totalNo = (TextView) findViewById(R.id.pic_count);

	}

	public void change() {
		viewPage.setCurrentItem(viewPage.getCurrentItem() + 1);
	}

	public void initView() {
		isFirst = true;
		prepareData();
		ViewPagerAdapter adapter = new ViewPagerAdapter();
		viewPage.setAdapter(adapter);
		viewPage.setOnPageChangeListener(this);
		isFirst  = true;
		/**
		 * 2147483647 / 2 = 1073741820 - 1
		 */
		int n = Integer.MAX_VALUE / 2 % imageViewList.size();
		int itemPosition = Integer.MAX_VALUE / 2 - n;

		viewPage.setCurrentItem(itemPosition);
	}

	private void prepareData() {
		if (imageViewList.size() > 0)
			imageViewList.clear();
		ImageView iv;
		for (int i = 0; i < drwableList.length; i++) {
			iv = new ImageView(mContext);
			iv.setScaleType(ScaleType.FIT_XY);
			ImageLoader.getInstance(mContext).displayImage(drwableList[i], iv,
					3);
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
		// 把当前选中的position对应的点置为enabled状态
		if(position % imageViewList.size()==0 ){
			setScrollble(false);
			
		}else{
			setScrollble(true);
//			isFirst = false;
		}
		currentNo.setText(position % imageViewList.size() + 1 + "");
		totalNo.setText(imageViewList.size() + "");
	}

	public String[] getDrwableList() {
		return drwableList;
	}

	public void setDrwableList(String[] drwableList) {
		this.drwableList = drwableList;
	}

	

	public boolean isScrollble() {

		return isCanScroll;

	}

	/**
	 * 
	 * 设置 是否可以滑动
	 * 
	 * @param isCanScroll
	 */

	public void setScrollble(boolean isCanScroll) {

		this.isCanScroll = isCanScroll;

	}

}
