package com.performance.ubt.sdkTest.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ubt on 2017/7/31.
 */

public abstract class BaseFragement extends Fragment {
    protected View mView;
    protected boolean isViewInitiated; //当前页面是否初始化
    protected boolean isVisibleToUser; //当前页面是否显示
    protected boolean isDataRequested; //是否已经请求了数据
    protected Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mView = inflater.inflate(getLayoutId(), null);
        isViewInitiated = true;
        initView();
        prepareGetData();
        return mView;
    }

    /*初始化页面布局和数据*/
    protected abstract void initView();

    /*布局*/
    public abstract int getLayoutId();

    /*服务器获取数据*/
    protected abstract void getDataFromServer();

    /**
     * 当前页面是否展示
     *
     * @param isVisibleToUser 显示为true， 不显示为false
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareGetData();
    }

    /**
     * 如果只想第一次进入该页面请求数据，return prepareGetData(false)
     * 如果想每次进入该页面就请求数据，return prepareGetData(true)
     *
     * @return
     */
    private boolean prepareGetData() {
        return prepareGetData(false);
    }

    /**
     * 判断是否从服务器器获取数据
     *
     * @param isforceUpdate 强制更新的标记
     * @return
     */
    protected boolean prepareGetData(boolean isforceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataRequested || isforceUpdate)) {
            /*从服务器获取数据*/
            getDataFromServer();
            isDataRequested = true;
            return true;
        }
        return false;
    }
}
