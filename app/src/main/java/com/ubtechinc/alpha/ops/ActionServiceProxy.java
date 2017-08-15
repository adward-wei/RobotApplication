package com.ubtechinc.alpha.ops;

import android.os.IBinder;
import android.os.RemoteException;

import com.ubtechinc.alpha.affairdispatch.ActionAffairManager;
import com.ubtechinc.alpha.affairdispatch.AffairConstruct;
import com.ubtechinc.alpha.affairdispatch.affair.ActionAffair;
import com.ubtechinc.alpha.affairdispatch.constants.AffairPriority;
import com.ubtechinc.alpha.ops.action.ActionPlayListener;
import com.ubtechinc.alpha.serverlibutil.aidl.ActionInfo;
import com.ubtechinc.alpha.serverlibutil.aidl.IActionListResultListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IActionResultListener;
import com.ubtechinc.alpha.serverlibutil.aidl.IActionService;
import com.ubtechinc.alpha.serverlibutil.interfaces.ActionListResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.StopActonResultListener;

import java.util.List;

/**
 * description: 动作服务的接口类， 主服务内部调用、sdk 调用，都首先通过该类。
 * 把内部调用与sdk调用接口分开的目的：
 * 1. 扩充新的接口时， sdk的接口可以不受影响，避免主服务与sdk的兼容问题
 * 2. 便于做接口权限管控，可以做到有些接口，只供主服务调用，不提供给外部使用。
 * autour: bob.xu
 * date: 2017/6/30 15:56
 * update: 2017/6/30
 * version: a */
public class ActionServiceProxy implements IActionServiceInner{

    private static ActionServiceProxy instance;
    private IActionServiceInner impl;
    private IBinder binder;

    public static final ActionServiceProxy getInstance() {
        if (instance == null) {
            synchronized (ActionServiceProxy.class) {
                instance = new ActionServiceProxy();
            }
        }
        return instance;
    }

    private ActionServiceProxy() {
        impl = new ActionServiceImpl();
        binder = new BinderStub();
    }


    @Override
    public int getActionList(ActionListResultListener onResultListener) {
        return impl.getActionList(onResultListener);
    }

    /**調用流程：playAction() ---> AffairExecute ---> onRealPlayAction()
     * */
    @Override
    public int playAction(String strActionName, ActionPlayListener onListener){
        return playAction(strActionName,AffairPriority.PRIORITY_NORMAL,onListener);
    }

    public int playAction(String strActionName, int priority,ActionPlayListener onListener){
//        ActionAffair.Builder builder = new ActionAffair.Builder();
//        ActionAffair actionAffair = builder.setActionName(strActionName)
//                .setPriority(priority)
//                .setListener(onListener).createActionAffair();
//        AffairConstruct.processAffair(actionAffair);
        return onRealPlayAction(strActionName,onListener);
    }

    @Override
    public void stopAction(StopActonResultListener onListener) {
        impl.stopAction(onListener);
    }

    public int onRealPlayAction(String strActionName, final ActionPlayListener onListener) {
        return impl.playAction(strActionName,onListener);
    }

    public IBinder getBinderStub() {
        return binder;
    }

    public void clearAllPendingActions() {
        ActionAffairManager.getInstance().clearAllAffair();
    }

    /**接收sdk的aild调用*/
    private class BinderStub extends IActionService.Stub {

        @Override
        public int getActionList(final IActionListResultListener onResultListener) throws RemoteException {

            return impl.getActionList(new ActionListResultListener() {

                @Override
                public void onGetActionList(int nOpId, int nErr, List<ActionInfo> onArrAction) {
                    ActionInfo[] infos = null;
                    if (onArrAction != null){
                        infos = new ActionInfo[onArrAction.size()];
                        onArrAction.toArray(infos);
                    }
                    try {
                        onResultListener.onGetActionList(nOpId, nErr,infos);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int playAction(String strActionName, final IActionResultListener onListener) throws RemoteException {

            return impl.playAction(strActionName,new ActionPlayListener() {

                @Override
                public void onActionResult(int nErr) {
                    if (onListener != null) {
                        try {
                            onListener.onPlayActionResult(0,nErr);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }

        @Override
        public void stopAction(final IActionResultListener onListener) throws RemoteException {
            impl.stopAction(new StopActonResultListener() {

                @Override
                public void onStopActionResult(int nErr) {
                    if (onListener != null) {
                        try {
                            onListener.onStopActionResult(nErr);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
