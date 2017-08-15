package com.ubtechinc.alpha.cmds.header;

import com.ubtechinc.alpha.jni.LedControl;
import com.ubtechinc.alpha.serial.SerialCmdResult;
import com.ubtechinc.alpha.serial.SerialCommandReceiver;
import com.ubtechinc.alpha.serial.SerialConstants;
import com.ubtechinc.alpha.utils.SysUtils;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/11
 * @modifier:
 * @modify_time:
 */

public class StopHeadHorseCmd extends HeadCmds {
    public StopHeadHorseCmd(SerialCommandReceiver receiver) {
        super(receiver, null);
    }

    @Override
    public void execute() {
        if (SysUtils.is5Mic()){
            LedControl.open();
            LedControl.ledSetHead(0,0,0,0,0,0,0,4);
            LedControl.close();
        }
        result = new SerialCmdResult(getCmdId(), SerialConstants.ERR_OK);
    }

    @Override
    public byte getCmdId() {
        return 0;
    }
}
