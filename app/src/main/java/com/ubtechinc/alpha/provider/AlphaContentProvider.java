package com.ubtechinc.alpha.provider;

import com.ubtechinc.alpha.provider.contracts.ActionInfoContract;
import com.ubtechinc.alpha.provider.contracts.AlarmInfoContract;
import com.ubtechinc.alpha.provider.contracts.AppInfoContract;
import com.ubtechinc.alpha.provider.contracts.PhotoInfoContract;
import com.ubtechinc.framework.provider.ContractContentProviderBase;
import com.ubtechinc.framework.provider.DatabaseContract;

import java.util.List;

/**
 * @desc : 主服务数据库:包含：动作，闹钟，图片，bnf,appinfo关系表
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public final class AlphaContentProvider extends ContractContentProviderBase {
    public static final String PROVIDER_NAME = "com.ubtechinc.alpha.coreservices";

    @Override
    protected String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    protected String getDatabaseName() {
        return EntityManagerHelper.DB_ACCOUNT;
    }

    @Override
    protected int getDatabaseVersion() {
        return EntityManagerHelper.DB_VERSION;
    }

    @Override
    protected void addDatabaseContracts(List<DatabaseContract> databaseContracts) {
        databaseContracts.add(new ActionInfoContract());
        databaseContracts.add(new AlarmInfoContract());
        databaseContracts.add(new PhotoInfoContract());
        databaseContracts.add(new AppInfoContract());
    }

}
