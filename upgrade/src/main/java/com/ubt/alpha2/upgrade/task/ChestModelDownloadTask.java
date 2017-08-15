package com.ubt.alpha2.upgrade.task;

import com.ubt.alpha2.upgrade.bean.UpgradeModel;
import com.ubt.alpha2.upgrade.serial.SerialConstants;
import com.ubt.alpha2.upgrade.utils.Constants;

/**
 * Created by ubt on 2017/7/20.
 */

public class ChestModelDownloadTask extends EmbeddedModelDownloadTask {
    @Override
    protected int getUpgradeType() {
        return SerialConstants.TYPE_CHEST;
    }

    @Override
    public String getModuleName() {
        return UpgradeModel.MODEL_EMBEDDED_CHEST;
    }
}
