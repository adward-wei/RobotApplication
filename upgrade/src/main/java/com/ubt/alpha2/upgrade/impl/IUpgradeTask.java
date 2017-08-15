package com.ubt.alpha2.upgrade.impl;

import com.ubt.alpha2.upgrade.bean.ServerVersionBean;
import com.ubt.alpha2.upgrade.exception.UpgradeException;

/**
 * Created by ubt on 2017/6/27.
 */
public interface IUpgradeTask extends IGetLocalModuleInfo,Runnable {
    /**
     * @author: slive
     * @description: initial before upgrade
     * @return:
     */
    void initUpgrade();

    /**
     * @author: slive
     * @description: get the Access Token from server
     * @return:
     */
    void getAccessToken();

    /**
     * @author: slive
     * @description: get the moduleId list from server with moduleName
     * @return:
     */
    void getModuleIdList();

    /**
     * @author: slive
     * @description: get the download version list from server with moduleId and local version
     * @return:
     */
    void getVersionInfoList();

    /**
     * @author: slive
     * @description: download the module
     * @return:
     * @param moduleInfo
     */
    void downloadModule(ServerVersionBean.ModuleInfo moduleInfo);

    /**
     * @author: slive
     * @description: upgrade the download module
     * @return:
     */
    void upgradeModule();

    /**
     * @author: slive
     * @description: upload the feedback of uploadInfo
     * @return:
     */
    void uploadUpgradeFeedback();

    /**
     * @author: slive
     * @description: interrupt the upgrade thread
     * @return:
     */
    void waitForResponse();

    /**
     * @author: slive
     * @description: notify the  upgrade thread
     * @return:
     */
    void notifyForExecute();

    /**
     * @author: slive
     * @description:  monitor the upgrade task whether completed
     * @return:
     */
    void setTaskFinishListener(ITaskCompleteListener listener);

    /**
     * @author: slive
     * @description:  cause UpgradeException
     * @return:
     */
    void upgradeCauseException() throws UpgradeException;


}
