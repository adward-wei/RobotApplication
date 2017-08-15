package com.ubt.alpha2.upgrade.task;

import android.content.Context;

import com.ubt.alpha2.upgrade.UpgradeApplication;
import com.ubt.alpha2.upgrade.impl.IModuleUpgradeTask;
import com.ubt.alpha2.upgrade.impl.IUpgradeListener;
import com.ubt.alpha2.upgrade.install.BaseInstallManager;


/**
 * Created by ubt on 2017/7/21.
 */

abstract public class BaseModuleUpgradeTask implements IModuleUpgradeTask {

    protected BaseInstallManager baseInstallManager;
    protected Context mContext;
    protected IUpgradeListener listener;
    protected String filepath;

    public BaseModuleUpgradeTask(String filepath){
        mContext = UpgradeApplication.getContext();
        this.filepath = filepath;
        baseInstallManager = new BaseInstallManager();
    }

    @Override
    public void setUpgradeListener(IUpgradeListener upgradeListener) {
        this.listener = upgradeListener;
    }

    @Override
    public void run(){
        startUpgrade();
    }

    protected void upgradeSuccess(){
        if(listener != null)
            listener.onUpgradeSuccess();
    }

    protected void upgradeFailed(){
        if(listener != null){
            listener.onUpgradeFailed();
        }
    }

}
