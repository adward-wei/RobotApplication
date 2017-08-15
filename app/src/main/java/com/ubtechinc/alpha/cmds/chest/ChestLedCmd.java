package com.ubtechinc.alpha.cmds.chest;

import com.ubtechinc.alpha.jni.LedControl;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;
import com.ubtechinc.alpha.utils.SysUtils;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/12
 * @modifier:
 * @modify_time:
 */

public class ChestLedCmd extends ChestCmds {
    private final boolean enable;
    public ChestLedCmd(SerialCommandReceiver receiver, boolean enable) {
        super(receiver, null);
        this.enable = enable;
    }

    @Override
    public void execute() {
        if (SysUtils.is5Mic()){
            LedControl.open();
            LedControl.ledSetOn(enable? 3 : 4);
            LedControl.close();
        }
        result = new SerialCmdResult(getCmdId(), SerialConstants.ERR_OK);
    }

    @Override
    public byte getCmdId() {
        return 0;
    }
}
