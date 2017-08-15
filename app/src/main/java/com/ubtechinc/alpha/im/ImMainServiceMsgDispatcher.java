package com.ubtechinc.alpha.im;


import com.ubtechinc.alpha.AlphaMessageOuterClass;
import com.ubtechinc.alpha.im.msghandler.BatchDeletDeskClockMsgHandler;
import com.ubtechinc.alpha.im.msghandler.BatchUninstallAppsMsgHandler;
import com.ubtechinc.alpha.im.msghandler.ClickAppButtonMsgHandler;
import com.ubtechinc.alpha.im.msghandler.ConfirmOnlineMsgHandler;
import com.ubtechinc.alpha.im.msghandler.ConnectRobotMsgHandler;
import com.ubtechinc.alpha.im.msghandler.DeleteActionFileMsgHandler;
import com.ubtechinc.alpha.im.msghandler.DeskClockManageHandler;
import com.ubtechinc.alpha.im.msghandler.DisConnectRobotMsgHandler;
import com.ubtechinc.alpha.im.msghandler.DownloadActionMsgHandler;
import com.ubtechinc.alpha.im.msghandler.GetActionlistMsgHandler;
import com.ubtechinc.alpha.im.msghandler.GetActiveDeskClockListHandler;
import com.ubtechinc.alpha.im.msghandler.GetAllAppsMsgHandler;
import com.ubtechinc.alpha.im.msghandler.GetAppButtonMsgHandler;
import com.ubtechinc.alpha.im.msghandler.GetAppConfigMsgHandler;
import com.ubtechinc.alpha.im.msghandler.GetChargePlayMsgHandler;
import com.ubtechinc.alpha.im.msghandler.GetDeskClockHistoryMsgHandler;
import com.ubtechinc.alpha.im.msghandler.GetEmulatingLedMsgHandler;
import com.ubtechinc.alpha.im.msghandler.GetRobotInitStatusMsgHandler;
import com.ubtechinc.alpha.im.msghandler.GetStorageAppListMsgHandler;
import com.ubtechinc.alpha.im.msghandler.GetTTSActionActiveDataMsgHandler;
import com.ubtechinc.alpha.im.msghandler.GetTopAppMsgHandler;
import com.ubtechinc.alpha.im.msghandler.InstallAppMsgHandler;
import com.ubtechinc.alpha.im.msghandler.PlayActionMsgHandler;
import com.ubtechinc.alpha.im.msghandler.QueryPowerDataMsgHandler;
import com.ubtechinc.alpha.im.msghandler.QuerySoftwareVersionMsgHandler;
import com.ubtechinc.alpha.im.msghandler.ReadMotorAngleMsgHandler;
import com.ubtechinc.alpha.im.msghandler.ReplaySpeechRecordMsgHandler;
import com.ubtechinc.alpha.im.msghandler.SaveAppConfigMsgHandler;
import com.ubtechinc.alpha.im.msghandler.SetChargePlayMsgHandler;
import com.ubtechinc.alpha.im.msghandler.SetErrorLogDataMsgHandler;
import com.ubtechinc.alpha.im.msghandler.SetMasterNameMsgHandler;
import com.ubtechinc.alpha.im.msghandler.SetRTCTimeMsgHandler;
import com.ubtechinc.alpha.im.msghandler.SetTTSActionActiveDataMsgHandler;
import com.ubtechinc.alpha.im.msghandler.StartAppMsgHandler;
import com.ubtechinc.alpha.im.msghandler.StopActionMsgHandler;
import com.ubtechinc.alpha.im.msghandler.StopAppMsgHandler;
import com.ubtechinc.alpha.im.msghandler.SynSpeechMsgHandler;
import com.ubtechinc.alpha.im.msghandler.TransferPhotoMsgHandler;
import com.ubtechinc.alpha.im.msghandler.TransferThumbPhotoMsgHandler;
import com.ubtechinc.alpha.im.msghandler.UninstallAppMsgHandler;
import com.ubtechinc.nets.im.annotation.IMMsgRelationVector;
import com.ubtechinc.nets.im.annotation.ImMsgRelation;

/**
 * Created by Administrator on 2017/5/25.
 */

@IMMsgRelationVector({@ImMsgRelation(requestCmdId = IMCmdId.IM_CONNECT_ROBOT_REQUEST,responseCmdId = IMCmdId.IM_CONNECT_ROBOT_RESPONSE,msgHandleClass = ConnectRobotMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_DISCONNECT_ROBOT_REQUEST,responseCmdId = IMCmdId.IM_DISCONNECT_ROBOT_RESPONSE,msgHandleClass = DisConnectRobotMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_DELETE_ACTIONFILE_REQUEST, responseCmdId = IMCmdId.IM_DELETE_ACTIONFILE_RESPONSE, msgHandleClass = DeleteActionFileMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_PLAY_ACTION_REQUEST, responseCmdId = IMCmdId.IM_PLAY_ACTION_RESPONSE, msgHandleClass = PlayActionMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_GET_MOTORANGLE_REQUEST, responseCmdId = IMCmdId.IM_GET_MOTORANGLE_RESPONSE, msgHandleClass = ReadMotorAngleMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_STOP_PLAY_REQUEST, responseCmdId = IMCmdId.IM_STOP_PLAY_RESPONSE, msgHandleClass = StopActionMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_START_APP_REQUEST, responseCmdId = IMCmdId.IM_START_APP_RESPONSE, msgHandleClass = StartAppMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_STOP_APP_REQUEST, responseCmdId = IMCmdId.IM_STOP_APP_RESPONSE, msgHandleClass = StopAppMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_GET_ALLAPPS_REQUEST, responseCmdId = IMCmdId.IM_GET_ALLAPPS_RESPONSE, msgHandleClass = GetAllAppsMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_INSTALL_PACKAGES_REQUEST, responseCmdId = IMCmdId.IM_INSTALL_PACKAGES_RESPONSE, msgHandleClass = InstallAppMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_UNINSTALL_PACKAGES_REQUEST, responseCmdId = IMCmdId.IM_UNINSTALL_PACKAGES_RESPONSE, msgHandleClass = UninstallAppMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_UPDATE_PACKAGES_REQUEST, responseCmdId = IMCmdId.IM_UPDATE_PACKAGES_RESPONSE, msgHandleClass = InstallAppMsgHandler.class),

        @ImMsgRelation(requestCmdId = IMCmdId.IM_GET_APPCONFIG_REQUEST, responseCmdId = IMCmdId.IM_GET_APPCONFIG_RESPONSE, msgHandleClass = GetAppConfigMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_SAVE_APPCONFIG_REQUEST, responseCmdId = IMCmdId.IM_SAVE_APPCONFIG_RESPONSE, msgHandleClass = SaveAppConfigMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_GET_TOP_APP_REQUEST, responseCmdId = IMCmdId.IM_GET_TOP_APP_RESPONSE, msgHandleClass = GetTopAppMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_GET_APP_BUTTONEVENT_REQUEST, responseCmdId = IMCmdId.IM_GET_APP_BUTTONEVENT_RESPONSE, msgHandleClass = GetAppButtonMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_CLICK_APP_BUTTON_REQUEST, responseCmdId = IMCmdId.IM_CLICK_APP_BUTTON_RESPONSE, msgHandleClass = ClickAppButtonMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_DOWNLOAD_ACTIONFILE_REQUEST, responseCmdId = IMCmdId.IM_DOWNLOAD_ACTIONFILE_RESPONSE, msgHandleClass = DownloadActionMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_DESKCLOCK_MANAGE_REQUEST, responseCmdId = IMCmdId.IM_DESKCLOCK_RESPONSE, msgHandleClass = DeskClockManageHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_DESKCLOCK_ACTIVIE_LIST_REQUEST, responseCmdId = IMCmdId.IM_DESKCLOCKLIST_RESPONSE, msgHandleClass = GetActiveDeskClockListHandler.class),

        @ImMsgRelation(requestCmdId = IMCmdId.IM_TRANSFER_PHOTO_REQUEST, responseCmdId = IMCmdId.IM_TRANSFER_PHOTO_RESPONSE, msgHandleClass = TransferPhotoMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_SET_RTC_TIME_REQUEST, responseCmdId = IMCmdId.IM_SET_RTC_TIME_RESPONSE, msgHandleClass = SetRTCTimeMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_SET_CHARGE_AND_PLAY_REQUEST, responseCmdId = IMCmdId.IM_CHARGE_AND_PLAY_RESPONSE, msgHandleClass = SetChargePlayMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_GET_CHARGE_AND_PLAY_REQUEST, responseCmdId = IMCmdId.IM_GET_CHARGE_AND_PLAY_RESPONSE, msgHandleClass = GetChargePlayMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_GET_ALL_THUMBNAIL_REQUEST, responseCmdId = IMCmdId.IM_GET_ALL_THUMBNAIL_RESPONSE, msgHandleClass = TransferThumbPhotoMsgHandler.class),
        @ImMsgRelation(requestCmdId = IMCmdId.IM_GET_NEW_ACTION_LIST_REQUEST, responseCmdId = IMCmdId.IM_GET_NEW_ACTION_LIST_RESPONSE, msgHandleClass = GetActionlistMsgHandler.class),

        @ImMsgRelation(requestCmdId =IMCmdId.IM_TTS_BREATH_ACTION_ON_OFF_REQUEST,responseCmdId =IMCmdId.IM_TTS_BREATH_ACTION_ON_OFF_RESPONSE,msgHandleClass =SetTTSActionActiveDataMsgHandler.class),
        @ImMsgRelation(requestCmdId =IMCmdId.IM_GET_TTS_BREATH_ACTION_ON_OFF_REQUEST,responseCmdId =IMCmdId.IM_GET_TTS_BREATH_ACTION_ON_OFF_RESPONSE,msgHandleClass =GetTTSActionActiveDataMsgHandler.class),
        @ImMsgRelation(requestCmdId =IMCmdId.IM_SYN_SPEECH_REQUEST,responseCmdId =IMCmdId.IM_SYN_SPEECH_RESPONSE,msgHandleClass = SynSpeechMsgHandler.class),
        @ImMsgRelation(requestCmdId =IMCmdId.IM_CLOSE_ROBOT_ERROR_LOG_REQUEST,responseCmdId =IMCmdId.IM_CLOSE_ROBOT_ERROR_LOG_RESPONSE,msgHandleClass =SetErrorLogDataMsgHandler.class),
        @ImMsgRelation(requestCmdId =IMCmdId.IM_QUERY_ROBOT_STORAGE_APP_LIST_REQUEST,responseCmdId =IMCmdId.IM_QUERY_ROBOT_STORAGE_APP_LIST_RESPONSE,msgHandleClass =GetStorageAppListMsgHandler.class),
        @ImMsgRelation(requestCmdId =IMCmdId.IM_UNINSTALL_BATCH_APPS_REQUEST,responseCmdId =IMCmdId.IM_UNINSTALL_BATCH_APPS_RESPONSE,msgHandleClass = BatchUninstallAppsMsgHandler.class),
        @ImMsgRelation(requestCmdId =IMCmdId.IM_RETRY_PLAY_ANSWER_REQUEST,responseCmdId =IMCmdId.IM_RETRY_PLAY_ANSWER_RESPONSE,msgHandleClass = ReplaySpeechRecordMsgHandler.class),
        @ImMsgRelation(requestCmdId =IMCmdId.IM_DELETE_FORMER_CLOCK_REQUEST,responseCmdId = IMCmdId.IM_DELETE_FORMER_CLOCK_RESPONSE,msgHandleClass = BatchDeletDeskClockMsgHandler.class),
        @ImMsgRelation(requestCmdId =IMCmdId.IM_GET_FORMER_CLOCK_REQUEST,responseCmdId = IMCmdId.IM_GET_FORMER_CLOCK_RESPONSE,msgHandleClass = GetDeskClockHistoryMsgHandler.class),
        @ImMsgRelation(requestCmdId =IMCmdId.IM_GET_ROBOT_INIT_STATUS_REQUEST,responseCmdId = IMCmdId.IM_GET_ROBOT_INIT_STATUS_RESPONSE,msgHandleClass = GetRobotInitStatusMsgHandler.class),
        @ImMsgRelation(requestCmdId =IMCmdId.IM_QUERY_ROBOT_POWER_REQUEST,responseCmdId = IMCmdId.IM_QUERY_ROBOT_POWER_RESPONSE,msgHandleClass = QueryPowerDataMsgHandler.class),
        @ImMsgRelation(requestCmdId =IMCmdId.IM_QUERY_HARD_SOFT_WARE_VERSION_REQUEST,responseCmdId = IMCmdId.IM_QUERY_HARD_SOFT_WARE_VERSION_RESPONSE,msgHandleClass = QuerySoftwareVersionMsgHandler.class),
        @ImMsgRelation(requestCmdId =IMCmdId.IM_SET_MASTER_NAME_REQUEST,responseCmdId = IMCmdId.IM_SET_MASTER_NAME_RESPONSE,msgHandleClass = SetMasterNameMsgHandler.class),
        @ImMsgRelation(requestCmdId =IMCmdId.IM_GET_EMULATING_LED_REQUEST,responseCmdId = IMCmdId.IM_GET_EMULATING_LED_RESPONSE,msgHandleClass = GetEmulatingLedMsgHandler.class),
        @ImMsgRelation(requestCmdId =IMCmdId.IM_CONFIRM_ONLINE_REQUEST,responseCmdId = IMCmdId.IM_CONFIRM_ONLINE_RESPONSE,msgHandleClass = ConfirmOnlineMsgHandler.class),

})
public class ImMainServiceMsgDispatcher extends ImMsgDispathcer {
    public ImMainServiceMsgDispatcher() {
        super();
    }

    @Override
    public void dispatchMsg(int cmdId, AlphaMessageOuterClass.AlphaMessage requestMsg, String peer) {
        if (ControlClientManager.getInstance().isLegalPeer(cmdId,peer)) {
            super.dispatchMsg(cmdId, requestMsg, peer);
        }
    }
}
