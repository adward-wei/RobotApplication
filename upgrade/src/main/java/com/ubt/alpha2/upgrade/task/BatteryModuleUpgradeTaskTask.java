package com.ubt.alpha2.upgrade.task;

import com.ubt.alpha2.upgrade.bean.UpgradeModel;
import com.ubt.alpha2.upgrade.serial.SerialConstants;
import com.ubt.alpha2.upgrade.utils.Constants;

/**
 * Created by ubt on 2017/7/21.
 */

public class BatteryModuleUpgradeTaskTask extends EmbeddedModelUpgradeTask {

    public BatteryModuleUpgradeTaskTask(String filepath) {
        super(filepath);
    }

    public String getModuleName(){
        return UpgradeModel.MODEL_EMBEDDED_BATTERY;
    }

    @Override
    protected byte getUpgradeStartCommand() {
        return Constants.UpgradeCommand.CHEST_BATTERY_UPDATE_START;
    }

    @Override
    protected byte getUpgradePageCommand() {
        return Constants.UpgradeCommand.CHEST_BATTERY_UPDATE_PAGE;
    }

    @Override
    protected byte getUpgradeEndCommand() {
        return Constants.UpgradeCommand.CHEST_BATTERY_UPDATE_END;
    }

    @Override
    protected int getUpgradeType() {
        return SerialConstants.TYPE_CHEST;
    }

}
