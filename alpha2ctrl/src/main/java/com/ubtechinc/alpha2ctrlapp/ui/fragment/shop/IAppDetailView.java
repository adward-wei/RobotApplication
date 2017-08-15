package com.ubtechinc.alpha2ctrlapp.ui.fragment.shop;


import com.ubtechinc.alpha2ctrlapp.entity.AppDetail;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.CommentInfo;

import java.util.List;

/**
 * Created by tanghongyu on 2016/10/16.
 */

public interface IAppDetailView {

    public void refreshDownloadIcon(int status);
    public void downloadComplete();
    public void downloadFail();
    public void downloadPause();
    public void installApkFail(int status);
    public void refreshAppIntroduction(AppDetail appDetail);
    public void lostConnection(String mac);
    public void refreshCommentList(List<CommentInfo> list);
    public void addAppComment(boolean isSuccess);
    public void collectRepeat();
    public void collect(boolean isSuccess);
    public void cancelCollect(boolean isSuccess);
    public void getSharedUrlSuccess();
    public void changeDownLoadStatus(int status);
    public void addOrRemoveFooterView(boolean isAdd);
    public void showShareDialog(AppDetail appDetail);
    public void getAppImageUrls(String stringURL);
    public void downloadButtonClick();
    public void dismissLoadingDialog();
    public void showLoadingDialog();
    public void showLoadingDialog(boolean isCancelable, int timeout);
    public void setCollectButton(boolean isCollect);
    public void setAppDetailList(String appDetailList);
}
