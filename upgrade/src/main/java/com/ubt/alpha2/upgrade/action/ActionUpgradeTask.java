package com.ubt.alpha2.upgrade.action;

import android.content.Context;
import android.text.TextUtils;

import com.ubt.alpha2.download.DownloadException;
import com.ubt.alpha2.download.DownloadInfo;
import com.ubt.alpha2.download.DownloadManager;
import com.ubt.alpha2.download.DownloadRequest;
import com.ubt.alpha2.download.SimpleCallBack;
import com.ubt.alpha2.download.util.FileUtils;
import com.ubt.alpha2.upgrade.R;
import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.impl.IModuleUpgradeTask;
import com.ubt.alpha2.upgrade.impl.ISyncTask;
import com.ubt.alpha2.upgrade.impl.IUnzipListener;
import com.ubt.alpha2.upgrade.impl.IUpgradeListener;
import com.ubt.alpha2.upgrade.net.HttpCommon;
import com.ubt.alpha2.upgrade.net.HttpManager;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.FileManagerUtils;
import com.ubt.alpha2.upgrade.utils.FilePathUtils;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.MD5FileUtil;
import com.ubt.alpha2.upgrade.utils.UpgradeFeedbackConfig;
import com.ubt.alpha2.upgrade.utils.ZipUtil;
import com.ubtech.utilcode.utils.Utils;
import com.ubtechinc.nets.ResponseListener;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by ubt on 2017/7/26.
 */

public class ActionUpgradeTask implements IModuleUpgradeTask,ISyncTask {
    Context mContext;
    HttpManager httpManager;
    ActionDao actionDao;
    DownloadManager downloadManager;
    List<ActionUpgradeBean.ActionFileBean> addActions;
    ActionReportManager actionReportManager;

    public ActionUpgradeTask(){
        mContext = UpgradeApplication.getContext();
        httpManager = HttpManager.get(mContext);
        Utils.init(mContext);
        httpManager.initAction(mContext,HttpCommon.ACTION_BASE_URL);
        actionDao = new ActionDao(mContext);
        downloadManager = DownloadManager.getInstance();
        downloadManager.init(mContext,false);
        actionReportManager = new ActionReportManager();
    }

    @Override
    public void startUpgrade() {
        new Thread(this).start();
    }

    @Override
    public void setUpgradeListener(IUpgradeListener upgradeListener) {

    }

    @Override
    public void run() {
        reportError();
        getActionUpgradeList(ApkUtils.getProductModel());
    }

    private void getActionUpgradeList(String productType){
        httpManager.doGetAction(HttpCommon.UpgradeAction.URL,HttpCommon.UpgradeAction.getActionListParam(productType),
                new ResponseListener<ActionUpgradeBean>() {
                    @Override
                    public void onError(ThrowableWrapper e) {

                    }

                    @Override
                    public void onSuccess(ActionUpgradeBean actionBean) {
                        LogUtils.d("onSuccess_actionBean: "+actionBean);
                        if(actionBean != null && actionBean.success){
                            dealWithLocalActions(actionBean.data.result);
                        }
                    }
                });
    }

    private void dealWithLocalActions(List<ActionUpgradeBean.ActionFileBean> serverActionFileBeanList){
        LogUtils.d("serverActionFileBeanList: "+serverActionFileBeanList);
        if(serverActionFileBeanList == null || serverActionFileBeanList.isEmpty()){
            return;
        }
        List<ActionUpgradeBean.ActionFileBean> localActionList =actionDao.readActionFromDb();
        LogUtils.d("localActionList: "+localActionList);
        CompareActionFile caf = new CompareActionFile(localActionList,serverActionFileBeanList);
        ActionFileResultInfo actionFileResultInfo = caf.getDifferent();
        List<String> delActionIds = actionFileResultInfo.getDelActionIds();
        int count= delActionIds.size();
        LogUtils.d("delActionIds.size  =  " +count);
        for(int i =0;i<count;i++){
            //删除响应ActionId的本地文件
            delActionFile(delActionIds.get(i));
            //删除数据库对应的动作文件id
            actionDao.deleteActionToDb(delActionIds.get(i));
        }
        //更新本地动作文件列表
        updateActionList(null,delActionIds,false);
        // 下载需要增加的动作文件
        addActions = actionFileResultInfo.getAddActions();
        if(addActions.size() != 0){
            LogUtils.d(" 开始下载：  "+addActions.get(0).actionPackageUrl);
            LogUtils.d(" 开始下载时间：  "+ DateFormatUtils.sDateFormatTest.format(new java.util.Date()) );
            startDownLoad(addActions.get(0).actionPackageUrl);
            waitForResponse();
        }
    }

    @Override
    public void waitForResponse() {

    }

    @Override
    public void notifyForExecute() {

    }

    /**
     * 删除本地动作文件
     */
    private void delActionFile(String ActionId){
        File ubxFile = new File(FilePathUtils.ACTION_PATH+"/"+ActionId+".ubx");
        File fileDir = new File(FilePathUtils.ACTION_PATH+"/"+ActionId);
        if (ubxFile.isFile() && ubxFile.exists()) {
            ubxFile.delete();
        }
        if(fileDir.exists() && fileDir.isDirectory() ){
            FileManagerUtils.deleteFolderFile(FilePathUtils.ACTION_PATH+"/"+ActionId,true);
        }
    }

    /**
     * 更新动作列表 //增加文件 isAdd = false
     */
    void updateActionList(ActionUpgradeBean.ActionFileBean addInfo, List<String> actionIDs, Boolean isAdd){
        if(isAdd){
            ActionFileManage.writeToActionTxt(addInfo.actionId+"##"+addInfo.actionNameCN+"##"+addInfo.actionNameEN+"##"+addInfo.actionType+"\r\n");
        }else {
            if(actionIDs.size() !=0){
                delActionFileList(actionIDs);
            }
        }
    }

    /**
     * 在文件列表中删除某些动作文件ID
     */
    void delActionFileList(List<String> ids) {
        ActionFileManage.fileInit();
        File strFileName = new File(FilePathUtils.ACTION_PATH + "/actionInfo.txt");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(strFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = null;
        String strLine = null;
        try {
            br = new BufferedReader(new InputStreamReader(fis,"gbk"));
            int idsSize = ids.size();
            if(idsSize == 0){
                return;
            }
            while ((strLine = br.readLine()) != null) {
                String[] strs = strLine.split("##");
                int flag = 0;
                for(int i=0;i<idsSize;i++){
                    if(ids.get(i).equals(strs[0])){
                        flag =1;
                        break;
                    }
                }
                if(flag == 0){
                    //写入到新文件
                    ActionFileManage.writeToTempTxt(strLine+"\r\n");
                }
            }
            //删除文件
            if (strFileName.isFile() && strFileName.exists()) {
                strFileName.delete();
            }
            ActionFileManage.renameFile("","");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startDownLoad(String url){
        File folder = new File(FilePathUtils.ACTION_PATH);
        DownloadRequest.Builder builder = new DownloadRequest.Builder();
        builder.setFolder(folder);
        builder.setUri(url);
        builder.setName(FileUtils.getShortFileName(url));
        downloadManager.download(builder.build(),url,new SimpleCallBack(){
            @Override
            public void onCompleted(DownloadInfo downloadInfo) {
                String filepath = new File(downloadInfo.getDir(),downloadInfo.getName()).getAbsolutePath();
                downloadCompleted(filepath);
            }
            @Override
            public void onFailed(DownloadException e) {

            }
        });
    }

    private void downloadCompleted(String filepath){
        LogUtils.d("file信息== "+filepath);
        //md5验证
        File big = new File(filepath);
        try {
            String md5 = MD5FileUtil.getFileMD5String(big).toUpperCase();
            LogUtils.d(" Md5=="+md5 );
            LogUtils.d(" addActions.get(0).getActionMD5()=="+addActions.get(0).actionMD5);
            if(md5.equalsIgnoreCase(addActions.get(0).actionMD5)){
                unzipAction(filepath);
            }else {
                LogUtils.d(" Md5验证失败" );
                //删除压缩文件
                FileManagerUtils.deleteFile(filepath);
                addActions.remove(0);
                if(addActions.size()!=0){
                    LogUtils.d(" Md5验证失败后 动作文件继续下载" );
                    startDownLoad(addActions.get(0).actionPackageUrl);
                }else{
                    LogUtils.d("Md5验证失败后 上传错误信息 " );
                    actionReportManager.addReportWithMd5Error(mContext.getString(R.string.md5_error));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void unzipAction(final String filepath){
        LogUtils.d(" Md5验证成功" );
        ZipUtil zipUtil = new ZipUtil(mContext, new IUnzipListener() {
            @Override
            public void onSuccess(String result) {
                File invalidFile = new File(filepath);
                if (invalidFile.isFile() && invalidFile.exists()) {
                    invalidFile.delete();
                }
                LogUtils.d(" 动作文件解压成功了哦  更新列表：   " +addActions.get(0).toString());
                updateActionList(addActions.get(0),null,true);
                //更新数据库
                actionDao.addActionToDb(addActions.get(0));
                addActions.remove(0);
                if(addActions.size()!=0){
                    LogUtils.d(" 动作文件继续下载" );
                    startDownLoad(addActions.get(0).actionPackageUrl);
                }else {
                    LogUtils.d(" 动作文件下载完成" );
                    LogUtils.d(" 动作文件下载完成时间：  "+ DateFormatUtils.sDateFormatTest.format(new java.util.Date()) );
                }
            }

            @Override
            public void onFailure(String reason) {
                LogUtils.d("动作文件解压失败" );
                addActions.remove(0);
                if(addActions.size()!=0){
                    LogUtils.d("动作文件解压失败后 动作文件继续下载" );
                    startDownLoad(addActions.get(0).actionPackageUrl);
                }else {
                    LogUtils.d("动作文件解压失败后 上传错误信息 " );
                    actionReportManager.addReportWithUnZipError(mContext.getString(R.string.unzip_error));
                }
            }
        });
        try {
            zipUtil.unzip(filepath,FilePathUtils.ACTION_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reportError(){
        reportActionUpgradeError(mContext.getString(R.string.md5_error));
        reportActionUpgradeError(mContext.getString(R.string.unzip_error));
    }

    private void reportActionUpgradeError(final String reason){
        String errorActionIds = UpgradeFeedbackConfig.getInstance().getActionErrorReport(reason);
        if(!TextUtils.isEmpty(errorActionIds)){
            String errorJson = actionReportManager.toJsonStr(errorActionIds,reason);
            httpManager.doPostAction(HttpCommon.UpgradeAction.REPORT_URL,errorJson, new ResponseListener<ActionReportManager.ResponseBean>() {
                @Override
                public void onError(ThrowableWrapper e) {
                    LogUtils.d("report error");
                }

                @Override
                public void onSuccess(ActionReportManager.ResponseBean responseBean) {
                    LogUtils.d("report success： "+responseBean);
                    if(responseBean.success)
                        UpgradeFeedbackConfig.getInstance().saveActionErrorReport(reason,"");
                }
            });
        }
    }
}
