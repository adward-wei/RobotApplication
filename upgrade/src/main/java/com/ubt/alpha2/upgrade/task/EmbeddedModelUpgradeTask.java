package com.ubt.alpha2.upgrade.task;


import com.ubt.alpha2.upgrade.bean.FeedbackInfo;
import com.ubt.alpha2.upgrade.bean.ReportDataBean;
import com.ubt.alpha2.upgrade.bean.UpgradeModuleManager;
import com.ubt.alpha2.upgrade.serial.SerialCmdResult;
import com.ubt.alpha2.upgrade.serial.SerialCommandExecutor;
import com.ubt.alpha2.upgrade.serial.SerialConstants;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.ConditionUtil;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.UpgradeFeedbackConfig;
import com.ubtech.utilcode.utils.ConvertUtils;
import com.ubtechinc.alpha.sdk.sys.SysApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by ubt on 2017/7/21.
 */

abstract public class EmbeddedModelUpgradeTask extends BaseModuleUpgradeTask {

    //获取 嵌入式版本 接口
    protected SysApi mSysApi;

    // 文件读取流
    private InputStream mInputRead;
    private int mReadCount;

    private byte[] mDataSave;
    private static final int PACKET_LEN = 128;
    //与串口交互的任务
    private String mMd5;
    SerialCommandExecutor serialCommandExecutor;

    public EmbeddedModelUpgradeTask(String filepath) {
        super(filepath);
        mSysApi = SysApi.get();
        mSysApi.initializ(mContext);
        mDataSave = new byte[PACKET_LEN];
    }

    @Override
    public void startUpgrade() {
        File file = new File(filepath);
        if(!file.exists())
            return;
        this.mMd5 = ApkUtils.getFileMd5Value(file);
        if(ConditionUtil.checkCondition(true, ConditionUtil.POWER_UPGRADE_THRESHOLD, -1)) {
            LogUtils.d("ConditionUtil.checkCondition OK");
            try {
                mInputRead = new FileInputStream(file);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                return;
            }
            initSerial();
            if(serialCommandExecutor == null) {
                LogUtils.e("serial port open failed");
                return;
            }
            sendUpgradeStart();
            closeSerialPort();
            LogUtils.d("upgrade end");
        }
    }

    private void initSerial(){
        SysApi.get().initializ(mContext).enterUpgradeMode();
        serialCommandExecutor  = new SerialCommandExecutor(mContext,getUpgradeType());
        try {
            serialCommandExecutor.init();
        } catch (IOException e) {
            serialCommandExecutor = null;
            e.printStackTrace();
        }
    }

    private void sendUpgradePage(){
        sendPageData();
    }

    public void sendUpgradeStart(){
        byte[] params = getCommandParam();
        SerialCmdResult result = serialCommandExecutor.executeCommand(getUpgradeStartCommand(),params);
        LogUtils.d("result111: "+result.getCmd()+"  error: "+result.getError());
        if(result.getCmd() == getUpgradeStartCommand() && result.getError() == SerialConstants.ERR_OK){
            UpgradeFeedbackConfig.getInstance().upgdateUpgradeStatus(getModuleName(), FeedbackInfo.DOWNLOAD_INSTALL_FAILED);
            sendUpgradePage();
        }
    }

    public void sendUpgradeEnd(){
        byte[] md5Bytes = ConvertUtils.hexString2Bytes(mMd5);
        sendCommand(getUpgradeEndCommand(), md5Bytes);
    }

    protected  byte[] getCommandParam(){
        File file = new File(filepath);
        long length = file.length();
        byte[] param = new byte[4];
        param[0] = (byte) (length >> 24 & 0xff);
        param[1] = (byte) (length >> 16 & 0xff);
        param[2] = (byte) (length >> 8 & 0xff);
        param[3] = (byte) (length & 0xff);
        return param;
    }

    public void closeSerialPort(){
        if(serialCommandExecutor != null) {
            serialCommandExecutor.clean();
            serialCommandExecutor = null;
            SysApi.get().initializ(mContext).exitUpgradeMode();
        }
    }

    private void sendData(){
        byte[] param = new byte[mReadCount+2];
        Arrays.fill(param, (byte) 0);
        param[0] = (byte) (mReadCount>>8 & 0xff);
        param[1] = (byte) (mReadCount & 0xff);
        System.arraycopy(mDataSave, 0, param, 2, mReadCount);
        sendCommand(getUpgradePageCommand(), param);
    }

    private void sendCommand(byte nCmd, byte[] nParam){
        SerialCmdResult result = serialCommandExecutor.executeCommand(nCmd,nParam);
        LogUtils.d("result.getCmd: "+result.getCmd()+" status: "+result.getError());
        if(result.getCmd() == getUpgradePageCommand()){
            if(result.getError() == SerialConstants.ERR_OK)
                sendPageData();
            else {
                ReportDataBean.getInstance().setUpgradeOk(false);
                upgradeFailed();
                closeSerialPort();
            }
        }else if(result.getCmd() == getUpgradeEndCommand()){
            closeSerialPort();
            ReportDataBean.getInstance().setUpgradeOk(true);
            UpgradeFeedbackConfig.getInstance().upgdateUpgradeStatus(getModuleName(), FeedbackInfo.INSTALL_SUCCESS);
            UpgradeModuleManager.getInstance().setNeededReboot(true);
            upgradeSuccess();
        }
    }


    private void sendPageData(){
        try {
            mReadCount = mInputRead.read(mDataSave, 0, PACKET_LEN);
            if (mReadCount == -1){
                sendUpgradeEnd();
                mInputRead.close();
                return;
            }
            sendData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    abstract protected String getModuleName();

    //获取更新的起始命令
    abstract protected byte getUpgradeStartCommand();
    //获取更新数据的命令
    abstract protected byte getUpgradePageCommand();
    //获取更新结束的命令
    abstract protected byte getUpgradeEndCommand();
    //获取更新类型
    abstract protected @SerialConstants.SerialType int getUpgradeType();
}
