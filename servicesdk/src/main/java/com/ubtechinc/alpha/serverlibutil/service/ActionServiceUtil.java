package com.ubtechinc.alpha.serverlibutil.service;

import android.content.Context;
import android.os.RemoteException;
import android.util.SparseArray;

import com.ubtechinc.alpha.serverlibutil.aidl.ActionInfo;
import com.ubtechinc.alpha.serverlibutil.aidl.IActionListResultListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IActionResultListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IActionService;
import com.ubtechinc.alpha.serverlibutil.interfaces.ActionListResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.ActionResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.StopActonResultListener;
import com.ubtechinc.alpha.sdk.SdkConstants;

import java.util.Arrays;

/**
 * @desc : 动作服务链接助手
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/4/14
 * @modifier:
 * @modify_time:
 */
public final class ActionServiceUtil {
    private IActionService mService;
    private IActionListResultListener.Stub mALRClientImpl;
    private IActionResultListener.Stub mAResultClientImpl;
    private SparseArray<ActionListResultListener> listResultListeners = new SparseArray<>();
    private SparseArray<ActionResultListener> playResultlisteners = new SparseArray<>();
    private StopActonResultListener stopActonResultListener;
    private Context context;
    public ActionServiceUtil(Context context){
        this.mALRClientImpl = new ActionListResultCallbackImpl();
        this.mAResultClientImpl = new ActionResultCallbackImpl();
        this.context = context.getApplicationContext();
        getBinder();
    }

    private void getBinder() {
        if (mService == null) {
            mService =  IActionService.Stub.asInterface(BinderFetchServiceUtil.get(context).getServiceBinder(SdkConstants.ACTION_BINDER_NAME));
        }
    }

    public void clear(){
        listResultListeners.clear();
        playResultlisteners.clear();
        stopActonResultListener = null;
    }

    public int getActionList(ActionListResultListener listResultListener){
        int ret ;
        getBinder();
        try {
            ret = mService.getActionList(mALRClientImpl);
            if (ret > 0) {
                listResultListeners.put(ret, listResultListener);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            ret = SdkConstants.ErrorCode.SERVICE_REMOTE_EXCEPTION;
        }catch (NullPointerException e){
            e.printStackTrace();
            ret = SdkConstants.ErrorCode.SERVICE_UNBINDED;
        }
        return ret;
    }

    public int playAction(String actionName, ActionResultListener listener){
        int ret ;
        getBinder();
        try {
            ret = mService.playAction(actionName, mAResultClientImpl);
            if (ret > 0 ){
                playResultlisteners.put(ret, listener);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            ret = SdkConstants.ErrorCode.SERVICE_REMOTE_EXCEPTION;
        } catch (NullPointerException e){
            e.printStackTrace();;
            ret = SdkConstants.ErrorCode.SERVICE_UNBINDED;
        }
        return ret;
    }

    public int stopAction(StopActonResultListener listener){
        int ret = SdkConstants.ErrorCode.RESULT_SUCCESS;
        this.stopActonResultListener = listener;
        getBinder();
        try {
            mService.stopAction(mAResultClientImpl);
        } catch (RemoteException e) {
            e.printStackTrace();
            ret = SdkConstants.ErrorCode.SERVICE_REMOTE_EXCEPTION;
        } catch (NullPointerException e){
            e.printStackTrace();
            ret = SdkConstants.ErrorCode.SERVICE_UNBINDED;
        }
        return ret;
    }

    private class ActionListResultCallbackImpl extends IActionListResultListener.Stub {

        @Override
        public void onGetActionList(int nOpId, int nErr, ActionInfo[] onArrAction) throws RemoteException {
            ActionListResultListener listener = listResultListeners.get(nOpId);
            if (listener != null){
                listResultListeners.remove(nOpId);
                listener.onGetActionList(nOpId,nErr, Arrays.asList(onArrAction));
            }
        }
    }

    private class ActionResultCallbackImpl extends IActionResultListener.Stub {

        @Override
        public void onPlayActionResult(int nOpId, int nErr) throws RemoteException {
            ActionResultListener listener = playResultlisteners.get(nOpId);
            if (listener != null){
                playResultlisteners.remove(nOpId);
                listener.onPlayActionResult(nOpId, nErr);
            }
        }

        @Override
        public void onStopActionResult(int nErr) throws RemoteException {
            if (stopActonResultListener != null){
                stopActonResultListener.onStopActionResult(nErr);
            }
        }
    }
}
