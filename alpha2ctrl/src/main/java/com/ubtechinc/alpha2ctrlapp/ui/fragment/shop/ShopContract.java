package com.ubtechinc.alpha2ctrlapp.ui.fragment.shop;

import com.ubtechinc.alpha2ctrlapp.base.BasePresenter;
import com.ubtechinc.alpha2ctrlapp.base.BaseView;
import com.ubtechinc.alpha2ctrlapp.entity.AppUpdate;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionDownLoad;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;

import java.util.List;

/**
 * @author：tanghongyu
 * @date：4/13/2017 10:22 AM
 * @modifier：tanghongyu
 * @modify_date：4/13/2017 10:22 AM
 * [A brief description]
 * version
 */

public interface ShopContract {

    interface View extends BaseView<Presenter> {
        void showLoadingDialog();
        void dismissLoadingDialog();
        void initAppView();
        void initActionView();
        void setMoreLayVisiable(boolean isVisiable);
        void setMoreAppVisiable(boolean isVisiable);
        void refreshComplete();
        void initAD( String[] mADImageList);
        void notifyActionAdapter(List<ActionDownLoad> mActionList);
        void notifyAppAdapter(List<AppUpdate> mAppMainList);
    }

    interface Presenter extends BasePresenter {
        /**
         * 获取最近的动作表
         */
        void getLastActionList();

        void getFrontPagePic();

        void getAppList();

        void parseActionList(List<ActionInfo> list);

        void pageClick(int position);

        void searchLayClick();

        void refreshGridView();

        int getShopType();

        void onChangeActive(int index);
        void setAppDownload(AppInfo appInfo);
        void setActionDownload(ActionDownLoad appInfo);
        List<AppUpdate> getAppMainList();
        List<ActionDownLoad> getActionList();

        void resume();
        void onDestory();
    }
}
