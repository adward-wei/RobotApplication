package com.ubt.alpha2.upgrade.impl;

/**
 * Created by ubt on 2017/7/21.
 */

public interface IModuleUpgradeTask extends Runnable {
    void startUpgrade();
    void setUpgradeListener(IUpgradeListener upgradeListener);
}
