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

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.BaseHandler;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ScollViewPager extends LinearLayout implements OnPageChangeListener {


    private List<ImageView> imageViewList = new ArrayList<ImageView>();
    private Context mContext;
    private ImageView[] img_logo_index;
    private String[] drwableList;
    private ViewPager viewPage;
    private OnViewPageSelected mOnViewPageSelected;

    public ScollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_page, this, true);
        viewPage = (ViewPager) findViewById(R.id.commonViewPager);


    }

    public void setOnViewPageSelected(OnViewPageSelected onViewPageSelected) {
        mOnViewPageSelected = onViewPageSelected;
    }

    public void change() {
        if (!isTouch)
            viewPage.setCurrentItem(viewPage.getCurrentItem() + 1);
    }
    public void initView() {
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
        if (imageViewList.size() > 0)
            imageViewList.clear();
        ImageView iv;
        for (int i = 0; i < drwableList.length; i++) {
            iv = new ImageView(mContext);
            iv.setScaleType(ScaleType.FIT_XY);
            ImageLoader.getInstance(mContext).displayImage(drwableList[i], iv, 3);
            imageViewList.add(iv);
            final int k = i;
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnViewPageSelected) {
                        mOnViewPageSelected.onPageClick(k);
                    }
                }
            });
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
//				container.removeView(imageViewList.get(position % imageViewList.size()));
        }

        /**
         * 创建一个view
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.removeView(imageViewList.get(position % imageViewList.size()));
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
        if (handler.hasMessages(-1)) {
            handler.removeMessages(-1);
        }
        handler.sendEmptyMessageDelayed(-1, 2000);
    }

    public void setImageIndex(int index) {
        for (int i = 0; i < img_logo_index.length; i++) {
            if (index != i) {
                img_logo_index[i].setVisibility(View.VISIBLE);
                img_logo_index[i].setBackgroundResource(R.drawable.sel_point);
            } else {
                img_logo_index[i].setBackgroundResource(R.drawable.sel_point_seleted);
            }
        }
    }

    public ImageView[] getImg_logo_index() {
        return img_logo_index;
    }

    public void setImg_logo_index(ImageView[] img_logo_index) {
        this.img_logo_index = img_logo_index;
    }

    public String[] getDrwableList() {
        return drwableList;
    }

    public void setDrwableList(String[] drwableList) {
        this.drwableList = drwableList;
    }

    public boolean isTouch() {
        return isTouch;
    }

    public void setTouch(boolean isTouch) {
        this.isTouch = isTouch;
    }

    public BaseHandler getHandler() {
        return handler;
    }

    public void setHandler(BaseHandler handler) {
        this.handler = handler;
    }
    private boolean isTouch = false;
    private BaseHandler handler;

    public interface OnViewPageSelected {
        void onPageClick(int position);
        void onPageSelected(int position);
    }

}
