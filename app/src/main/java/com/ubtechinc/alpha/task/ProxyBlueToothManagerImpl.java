//package com.ubtechinc.alpha.task;
//
//import android.bluetooth.BluetoothDevice;
//
//import com.ubtech.utilcode.utils.LogUtils;
//import com.ubtechinc.alpha.appmanager.old.RobotBluetooth;
//import com.ubtechinc.alpha.appmanager.old.RobotBluetoothList;
//import com.ubtechinc.alpha.entity.ManageRobotBluetooth;
//import com.ubtechinc.alpha.event.BluetoothManageEvent;
//import com.ubtechinc.alpha.utils.BTUtil;
//import com.ubtechinc.framework.notification.NotificationCenter;
//import com.ubtechinc.framework.notification.Subscriber;
//import com.ubtechinc.nets.utils.JsonUtil;
//
//import java.util.List;
//import java.util.Set;
//
//import static com.ubtechinc.framework.util.R.mContext;
//
///**
// * Created by Administrator on 2017/6/6 0006.
// */
//
//public class ProxyBlueToothManagerImpl extends AbstractProxyService{
//
//    protected Subscriber<BluetoothManageEvent> bluetoothManageEventSubscriber =new Subscriber<BluetoothManageEvent>(){
//        @Override
//        public void onEvent(BluetoothManageEvent bluetoothManageEvent) {
//            ManageRobotBluetooth manageRobotBluetooth=new ManageRobotBluetooth();
//            manageRobotBluetooth.setContent(bluetoothManageEvent.content);
//            manageRobotBluetooth.setCode(bluetoothManageEvent.code);
//            manageRobotBluetooth.setOrderCmd(bluetoothManageEvent.orderCmd);
//            manageRobotBluetooth.setSendByClient(bluetoothManageEvent.isSendByClient);
//            handleManageBT(manageRobotBluetooth);
//        }
//    };
//    @Override
//    public void registerEvent() {
//        NotificationCenter.defaultCenter().subscriber(BluetoothManageEvent.class,bluetoothManageEventSubscriber);
//    }
//
//    @Override
//    public void unregisterEvent() {
//        NotificationCenter.defaultCenter().unsubscribe(BluetoothManageEvent.class,bluetoothManageEventSubscriber);
//    }
//
//    private void handleManageBT(ManageRobotBluetooth manageRobotBluetooth) {
//        if(manageRobotBluetooth != null && manageRobotBluetooth.isSendByClient()) {
//            ManageRobotBluetooth manageRB = null;
//            BTUtil sBTUtil = BTUtil.getBTUtilInstance(mContext);
//            String info = null;
//            RobotBluetooth robotBluetooth = null;
//            boolean isSuccess = false;
//            int code = 0;
//            switch (manageRobotBluetooth.getOrderCmd()) {
//                case 1:
//                    LogUtils.d("command 1");
//                    if(!sBTUtil.isBluetoothDiscovering()) {
//                        sBTUtil.startDiscovery();
//                    }
//                    manageRB = new ManageRobotBluetooth();
//                    manageRB.setOrderCmd(1);
//                    manageRB.setSendByClient(false);
//                    manageRB.setCode(3);
//                    manageRB.setContent("");
////                    sendEventbusMessage(SocketCmdId.ALPHA_MSG_MANAGE_BLUETOOTH, manageRB);
//                    handleManageBT(manageRB);
//                    break;
//                case 2:
//                    //sBTUtil.startDiscovery();
//                    LogUtils.d("command 2");
//                    Set<BluetoothDevice> bondedList = sBTUtil.getBondedDevices();
//                    RobotBluetoothList robotBluetoothBondedList = null;
//                    if(bondedList != null) {
//                        robotBluetoothBondedList = new RobotBluetoothList();
//                        for(BluetoothDevice item : bondedList) {
//                            robotBluetooth = new RobotBluetooth();
//                            robotBluetooth.setDeviceName(item.getName());
//                            robotBluetooth.setDeviceAddress(item.getAddress());
//                            robotBluetooth.setBondState(item.getBondState());
//                            robotBluetoothBondedList.getList().add(robotBluetooth);
//                        }
//                    }
//                    manageRB = new ManageRobotBluetooth();
//                    manageRB.setOrderCmd(2);
//                    manageRB.setSendByClient(false);
//                    manageRB.setCode(1);
//                    if(robotBluetoothBondedList != null) {
////                        String jsonString = JsonUtils.getInstance().getJson(robotBluetoothBondedList);
//                        String jsonString =JsonUtil.object2Json(robotBluetoothBondedList);
//                        manageRB.setContent(jsonString);
//                    } else {
//                        manageRB.setContent("");
//                    }
////                    sendEventbusMessage(SocketCmdId.ALPHA_MSG_MANAGE_BLUETOOTH, manageRB);
//                    handleManageBT(manageRB);
//                    break;
//                case 3:
//                    LogUtils.d("command 3");
//                    sBTUtil.cancelDiscovery();
//                    manageRB = new ManageRobotBluetooth();
//                    manageRB.setOrderCmd(3);
//                    manageRB.setSendByClient(false);
//                    manageRB.setCode(1);
//                    manageRB.setContent("");
////                    sendEventbusMessage(SocketCmdId.ALPHA_MSG_MANAGE_BLUETOOTH, manageRB);
//                    handleManageBT(manageRB);
//                    break;
//                case 4:
//                    LogUtils.d("command 4");
//                    info = manageRobotBluetooth.getContent();
////                    robotBluetooth = (RobotBluetooth) JsonUtils.getInstance().jsonToBean(info, RobotBluetooth.class);
//                    robotBluetooth =JsonUtil.getObject(info,RobotBluetooth.class);
//                    isSuccess = sBTUtil.pair(robotBluetooth.getDeviceAddress(), "000000");
//                    code = 0;
//                    if(isSuccess) {
//                        code = 1;
//                        manageRB = new ManageRobotBluetooth();
//                        manageRB.setOrderCmd(4);
//                        manageRB.setSendByClient(false);
//                        manageRB.setCode(3);
//                        manageRB.setContent("");
////                        sendEventbusMessage(SocketCmdId.ALPHA_MSG_MANAGE_BLUETOOTH, manageRB);
//                        handleManageBT(manageRB);
//                    } else {
//                        manageRB = new ManageRobotBluetooth();
//                        manageRB.setOrderCmd(4);
//                        manageRB.setSendByClient(false);
//                        manageRB.setCode(code);
//                        manageRB.setContent("");
////                        sendEventbusMessage(SocketCmdId.ALPHA_MSG_MANAGE_BLUETOOTH, manageRB);
//                        handleManageBT(manageRB);
//                    }
//                    break;
//                case 5:
//                    LogUtils.d("command 5");
//                    info = manageRobotBluetooth.getContent();
//                    robotBluetooth = JsonUtil.getObject(info,RobotBluetooth.class);;
//                    isSuccess = sBTUtil.unpair(robotBluetooth.getDeviceAddress());
//                    code = 0;
//                    if(isSuccess) {
//                        code = 1;
//                        manageRB = new ManageRobotBluetooth();
//                        manageRB.setOrderCmd(5);
//                        manageRB.setSendByClient(false);
//                        manageRB.setCode(3);
//                        manageRB.setContent("");
//                    } else {
//                        manageRB = new ManageRobotBluetooth();
//                        manageRB.setOrderCmd(5);
//                        manageRB.setSendByClient(false);
//                        manageRB.setCode(code);
//                        manageRB.setContent("");
//                    }
////                    sendEventbusMessage(SocketCmdId.ALPHA_MSG_MANAGE_BLUETOOTH, manageRB);
//                    handleManageBT(manageRB);
//                    break;
//                case 6:
//                    LogUtils.d("command 6");
//                    List<BluetoothDevice> list = sBTUtil.getConnectedDevice();
//                    RobotBluetoothList robotBluetoothList = new RobotBluetoothList();
//                    if(list != null) {
//                        for (BluetoothDevice item : list) {
//                            robotBluetooth = new RobotBluetooth();
//                            robotBluetooth.setDeviceName(item.getName());
//                            robotBluetooth.setDeviceAddress(item.getAddress());
//                            robotBluetooth.setBondState(item.getBondState());
//                            robotBluetoothList.getList().add(robotBluetooth);
//                        }
//                    }
//                    String jsonString =JsonUtil.object2Json(robotBluetoothList);
////                    String jsonString = JsonUtils.getInstance().getJson(robotBluetoothList);
//                    manageRB = new ManageRobotBluetooth();
//                    manageRB.setOrderCmd(6);
//                    manageRB.setSendByClient(false);
//                    manageRB.setCode(1);
//                    manageRB.setContent(jsonString);
////                    sendEventbusMessage(SocketCmdId.ALPHA_MSG_MANAGE_BLUETOOTH, manageRB);
//                    handleManageBT(manageRB);
//                    break;
//            }
//        }
//    }
//}
