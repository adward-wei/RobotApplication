package com.ubt.alpha2.upgrade.task;

import com.ubt.alpha2.upgrade.bean.UpgradeModel;
import com.ubt.alpha2.upgrade.serial.SerialConstants;
import com.ubt.alpha2.upgrade.utils.Constants;

/**
 * Created by ubt on 2017/7/21.
 */

public class ChestModuleUpgradeTaskTask extends EmbeddedModelUpgradeTask {

    public ChestModuleUpgradeTaskTask(String filepath) {
        super(filepath);
    }

    @Override
    public String getModuleName(){
        return UpgradeModel.MODEL_EMBEDDED_CHEST;
    }

    @Override
    protected byte getUpgradeStartCommand() {
        return Constants.UpgradeCommand.CHES_CMD_START_UPDATE;
    }

    @Override
    protected byte getUpgradePageCommand() {
        return Constants.UpgradeCommand.CHES_CMD_UPDATE_PAGE;
    }

    @Override
    protected byte getUpgradeEndCommand() {
        return Constants.UpgradeCommand.CHES_CMD_UPDATE_END;
    }

    @Override
    protected int getUpgradeType() {
        return SerialConstants.TYPE_CHEST;
    }
}
