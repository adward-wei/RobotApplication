package com.ubtechinc.alpha2ctrlapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.ubtech.utilcode.utils.StringUtils;
import com.ubtechinc.alpha2ctrlapp.base.Alpha2Application;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.entity.AppInstalledInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionDownLoad;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ActionFileEntrity;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.NewActionInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.RobotInfo;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.ActionDetail;
import com.ubtechinc.alpha2ctrlapp.entity.business.shop.AppInfo;
import com.ubtechinc.alpha2ctrlapp.util.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class DBDao {

    private static final String TAG = "DBDao";
    private static DBDao dao = null;
    private Context context;

    public DBDao() {

    }

    public DBDao(Context context) {
        this.context = context;
    }

    public static DBDao getInstance(Context context) {
        if (dao == null) {
            dao = new DBDao(context);
        }
        return dao;
    }


    /**
     * 添加已安装的数据到的App表
     *
     * @param lists
     */
    public synchronized void insertToAppInstalledList(
            List<RobotAppEntrity> lists) {

        AppInfoProvider.get().saveOrUpdateByPackageNameAndSerialNo(lists);

    }
    public void deleteAllData() {
        ActionInfoProvider.get().deleteAll();
        AppInfoProvider.get().deleteAll();
    }
    /**
     * 更新某个App图像信息
     *
     * @return
     */
    public synchronized void updateAppIcon(String packageName, String name, String imageUrl, String versionCode) {

            RobotAppEntrity entrityInfo = AppInfoProvider.get().findAppByParam(ImmutableMap.of( "packageName",  (Object) StringUtils.nullStringToDefault(packageName), "serialNo", Alpha2Application.getRobotSerialNo()));
            if(entrityInfo == null) {
                entrityInfo = new RobotAppEntrity();
                entrityInfo.setPackageName(packageName);
                entrityInfo.setDownloadState(BusinessConstants.APP_STATE_DOWNLOADING);
                entrityInfo.setName(name);
                entrityInfo.setAppImagePath(imageUrl);
                entrityInfo.setVersionCode(versionCode);
                entrityInfo.setSerialNo(Alpha2Application.getRobotSerialNo());
                AppInfoProvider.get().save(entrityInfo);
            }else {
                entrityInfo.setName(name);
                entrityInfo.setAppImagePath(imageUrl);
                entrityInfo.setVersionCode(versionCode);
                AppInfoProvider.get().updateValuesByParam(entrityInfo, ImmutableMap.of("appKey", (Object) StringUtils.nullStringToDefault(entrityInfo.getAppKey())), "name","versionCode" ,"appImagePath");
            }

    }

    /**
     * 更新App的下载状态
     *
     * @return
     */
    public synchronized void updateAppDownLoadState( RobotAppEntrity info) {
        AppInfoProvider.get().saveOrUpdateByPackageNameAndSerialNo(ImmutableList.of(info));

    }

    /**
     * 更新已安装的App列表图像信息
     *
     * @return
     */
    public synchronized void updateAppListIcon( List<AppInfo> infoList) {
        AppInfoProvider.get().updateAppIconAndName(infoList);

    }

    /**
     * 查询的App列表
     *
     * @return
     */
    public synchronized List<AppInstalledInfo> queryAppList() {
        List<AppInstalledInfo> list = Lists.newArrayList();
        List<RobotAppEntrity> robotAppEntrities = AppInfoProvider.get().findAppListByParam(ImmutableMap.of( "downloadState",  (Object) BusinessConstants.APP_STATE_INSTALL_SUCCESS, "serialNo", Alpha2Application.getRobotSerialNo()));
         for (RobotAppEntrity robotAppEntrity : robotAppEntrities) {
             AppInstalledInfo info = new AppInstalledInfo();
             BeanUtils.copyBean(robotAppEntrity, info);
             list.add(info);
         }


            return list;


    }

    /**
     * 查询的特定的App
     *
     * @param packageName 查询App 的报名
     * @return
     */
    public synchronized AppInstalledInfo queryApk( String packageName) {

        RobotAppEntrity robotAppEntrity = AppInfoProvider.get().findAppByParam(ImmutableMap.of( "packageName",  (Object) StringUtils.nullStringToDefault(packageName), "serialNo", Alpha2Application.getRobotSerialNo()));
        if(robotAppEntrity == null) {
            return null;
        }else{
            AppInstalledInfo info = new AppInstalledInfo();
            BeanUtils.copyBean(robotAppEntrity, info);
            return info;
        }


    }

    /**
     * 查询的特定app的icon
     *
     * @param packageName app 的包名
     * @return
     */
    public String queryApkIcon( String packageName) {
        RobotAppEntrity robotAppEntrity = AppInfoProvider.get().findAppByParam(ImmutableMap.of( "packageName",  (Object)  StringUtils.nullStringToDefault(packageName), "serialNo", Alpha2Application.getRobotSerialNo()));
        if(robotAppEntrity == null) {
            return "";
        }else{
            return robotAppEntrity.getAppImagePath();
        }


    }

    public void deleteApp(String packageName) {
        AppInfoProvider.get().deleteByParam(ImmutableMap.of( "packageName",  (Object)  StringUtils.nullStringToDefault(packageName), "serialNo", Alpha2Application.getRobotSerialNo()));


    }

    /**
     * 查询的App 状态列表
     *
     * @param packageName app包名
     * @return
     */
    public int queryAppStatus(String packageName, int versionCode) {
        RobotAppEntrity entrityInfo = AppInfoProvider.get().findAppByParam(ImmutableMap.of( "packageName",  (Object)  StringUtils.nullStringToDefault(packageName), "serialNo", Alpha2Application.getRobotSerialNo()));
        if(entrityInfo == null) {
            return BusinessConstants.APP_STATE_INIT;
        }else {
            //有可能机器人下载时，传过来的版本号是空的
            int dbVersion = StringUtils.isEmpty(entrityInfo.getVersionCode()) ? 0 : Integer.valueOf(entrityInfo.getVersionCode());

            if (dbVersion < versionCode) {
                return BusinessConstants.APP_STATE_ROBOT_APP_HAS_UPDATE;
            } else if (dbVersion == versionCode) {
                return entrityInfo.getDownloadState();
            } else {
//					return 6;
                return BusinessConstants.APP_STATE_INSTALL_SUCCESS;
            }
        }


    }


    /**
     * 添加已安装的数据到的动作表
     *
     */
    public synchronized void insertActionList(       List<NewActionInfo> actionInfoList) {
        ActionInfoProvider.get().saveOrUpdateByActionIdAndSerialNo(Alpha2Application.getRobotSerialNo(), actionInfoList);

    }

    /**
     * 更新动作表的下载状态
     *
     * @return
     */
    public synchronized long updateActionDownloadState(ActionFileEntrity info, ActionDownLoad downLoadObj, ActionDetail detailObj) {
        long row = -1;
        Cursor cursor = null;
        String actionLanName = "";
        String actionLanDesciber = "";
        String lan = Constants.SYSTEM_LAN;
        if (downLoadObj != null) {
            if (!TextUtils.isEmpty(downLoadObj.getActionLangDesciber())) {
                actionLanDesciber = downLoadObj.getActionLangDesciber();

            }
            if (!TextUtils.isEmpty(downLoadObj.getActionLangName())) {
                actionLanName = downLoadObj.getActionLangName();

            }
        } else if (detailObj != null) {
            if (!TextUtils.isEmpty(detailObj.getActionLangDesciber())) {
                actionLanDesciber = detailObj.getActionLangDesciber();

            }
            if (!TextUtils.isEmpty(detailObj.getActionLangName())) {
                actionLanName = detailObj.getActionLangName();

            }
        }
        try {

            ActionEntrityInfo actionEntrityInfo = ActionInfoProvider.get().findActionByParam(ImmutableMap.of( "actionOriginalId",  (Object)  StringUtils.nullStringToDefault(info.getActionOriginalId()), "serialNo", Alpha2Application.getRobotSerialNo()));
            if (actionEntrityInfo != null) {// 更新
                ContentValues values = new ContentValues();
                values.put("downloadState", info.getDownloadState());
                if (info.getDownloadState() == 4) { // 在完全安装成功的时候才更新获取相关的信息
                    values.put("actionId", info.getActionId());
                    values.put("actionType", info.getActionType());
                    values.put("actionFilePath", info.getActionFilePath());
                    values.put("actionName", info.getActionName());
                    values.put("actionOriginalId", info.getActionOriginalId());
                    values.put("lan", lan);
                    if (downLoadObj != null || detailObj != null) {
                        values.put("actionLanName", actionLanName);
                        values.put("actionLanDesciber", actionLanDesciber);
                    }
                }

                ActionInfoProvider.get().updateByParam(values,ImmutableMap.of( "actionOriginalId",  (Object) StringUtils.nullStringToDefault(info.getActionOriginalId()), "serialNo", Alpha2Application.getRobotSerialNo()) );
            } else  {// 插入
                actionEntrityInfo = new ActionEntrityInfo();
                BeanUtils.copyBean(info, actionEntrityInfo);
                actionEntrityInfo.setSerialNo(Alpha2Application.getRobotSerialNo());
                actionEntrityInfo.setLanguage(lan);

                if (downLoadObj != null || detailObj != null) {
                    actionEntrityInfo.setActionLanName(actionLanName);
                    actionEntrityInfo.setActionLanDesciber(actionLanDesciber);

                }
                ActionInfoProvider.get().save(actionEntrityInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return row;
    }

    /**
     * 查询的某个动作的状态
     *
     * @param actionOriginalID 动作的id(新动作是id，老动作是名称)
     * @param actionFilePath   动作的下载URL
     * @return int
     */
    public int queryActionStatus( String actionOriginalID, String actionFilePath) {
        if(!TextUtils.isEmpty(actionFilePath)) {
            ActionEntrityInfo actionEntrityInfo = ActionInfoProvider.get().findActionByParam( ImmutableMap.of( "actionOriginalId",  (Object) StringUtils.nullStringToDefault(actionOriginalID), "serialNo", Alpha2Application.getRobotSerialNo()));
            if(actionEntrityInfo == null) {
                return BusinessConstants.APP_STATE_INIT;
            }else {
               return actionEntrityInfo.getDownloadState();
            }

        }else {
            return BusinessConstants.APP_STATE_ERROR;
        }



    }

    /**
     * 查询的动作列表
     *
     * @return
     */
    public List<String> queryActionList() {



        List<String> list = new ArrayList<String>();
        List<ActionEntrityInfo> actionEntrityInfos = ActionInfoProvider.get().findActionListByParam( ImmutableMap.of( "downloadState",  (Object)BusinessConstants.APP_STATE_INSTALL_SUCCESS, "serialNo", Alpha2Application.getRobotSerialNo()));
        for(ActionEntrityInfo actionEntrityInfo : actionEntrityInfos) {
            list.add(actionEntrityInfo.getActionOriginalId());
        }


        return list;

    }

    /**
     * 根据动作类型查询已安装的动作表
     *
     * @param type 动作类型
     * @return
     */
    public synchronized List<NewActionInfo> queryActionListByType(int type) {
        List<NewActionInfo> list = Lists.newArrayList();
        List<ActionEntrityInfo> actionEntrityInfos = ActionInfoProvider.get().findActionListByParam( ImmutableMap.of( "downloadState",  (Object)BusinessConstants.APP_STATE_INSTALL_SUCCESS, "serialNo", Alpha2Application.getRobotSerialNo(), "actionType",  type));
        for(ActionEntrityInfo actionEntrityInfo : actionEntrityInfos) {
            NewActionInfo info = new NewActionInfo();
            info.setActionId(actionEntrityInfo.getActionOriginalId());
            if (!TextUtils.isEmpty(actionEntrityInfo.getActionLanName())) {
                info.setActionName(actionEntrityInfo.getActionLanName());
            } else {
                info.setActionName(actionEntrityInfo.getActionName());
            }
            info.setActionType(actionEntrityInfo.getActionType());
            list.add(info);
        }


        return list;

    }

    /**
     * 根据动作名称进行模糊查询
     *
     * @param name 关键字
     * @param type 动作类型1基本，2音乐舞蹈 ，3寓言故事
     * @return
     */
    public List<NewActionInfo> queryActionListByName( String name, int type) {
        List<ActionEntrityInfo> actionEntrityInfos = ActionInfoProvider.get().findActionListByParam( ImmutableMap.of("actionLanName", (Object) StringUtils.nullStringToDefault(name), "downloadState",  BusinessConstants.APP_STATE_INSTALL_SUCCESS, "serialNo", Alpha2Application.getRobotSerialNo(), "actionType",  type));
        List<NewActionInfo> list = new ArrayList<NewActionInfo>();
        for(ActionEntrityInfo actionEntrityInfo : actionEntrityInfos) {
            NewActionInfo info = new NewActionInfo();
            info.setActionId(actionEntrityInfo.getActionOriginalId());
            info.setActionName(actionEntrityInfo.getActionLanName());
            info.setActionType(actionEntrityInfo.getActionType());
            list.add(info);
        }
        return list;



    }

    public void deleteAction( String actionOriginalID) {
        ActionInfoProvider.get().deleteActionListByParam(Alpha2Application.getRobotSerialNo(),   ImmutableMap.of("actionOriginalId", (Object)StringUtils.nullStringToDefault(actionOriginalID) ));
    }

    /**
     * 添加用户已有的机器人数据列表
     *
     * @param lists
     */
    public synchronized void insertToRobotList(
            List<RobotInfo> lists) {
        RobotInfoProvider.get().saveOrUpdateByRelationIdAndEquitmentId(lists);
    }
    public synchronized void deleteAllRobot() {
        RobotInfoProvider.get().deleteAll();
    }


    /**
     * 根据用户名称查询机器人列表
     *
     * @param userId 用户ID
     * @return
     */
    public List<RobotInfo> getRobotList(String userId) {

       return RobotInfoProvider.get().findRobotListByParam(ImmutableMap.of("userId", (Object) StringUtils.nullStringToDefault(userId) ));
    }


}
