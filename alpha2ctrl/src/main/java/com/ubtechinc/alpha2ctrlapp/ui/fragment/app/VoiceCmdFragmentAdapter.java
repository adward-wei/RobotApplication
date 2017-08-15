package com.ubtechinc.alpha2ctrlapp.ui.fragment.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author：ubt
 * @date：2017/2/18 15:42
 * @modifier：ubt
 * @modify_date：2017/2/18 15:42
 * [A brief description]
 * version
 */

public class VoiceCmdFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> list_fragment;                         //fragment列表
    private List<String> list_Title;                              //tab名的列表


    public VoiceCmdFragmentAdapter(FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title) {
        super(fm);
        this.list_fragment = list_fragment;
        this.list_Title = list_Title;
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_Title.size();
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {

        return list_Title.get(position % list_Title.size());
    }
}