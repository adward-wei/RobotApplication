package com.ubt.alpha2.upgrade.impl;

/**
 * Created by ubt on 2017/7/20.
 */

public interface IUpgradeVersionInterface {
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
}
