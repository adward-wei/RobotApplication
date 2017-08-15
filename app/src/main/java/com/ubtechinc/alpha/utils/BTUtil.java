package com.ubtechinc.alpha.utils;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.appmanager.old.RobotBluetooth;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by wzt on 2016/7/6.
 */
public class BTUtil {
    public static final String TAG = "BTUtil";
    private static BTUtil sBTUtil;
    private Context mContext;
    private ArrayList<RobotBluetooth> connectedDeviceList;
    private BluetoothA2dp mBluetoothA2dp;

    private BTUtil(Context context) {
        // TODO Auto-generated constructor stub
        mContext = context;
        connectedDeviceList = new ArrayList<RobotBluetooth>();
        //startListenConnectedDevice(mContext);
        registerConnectedListener(context);
    }

    public static BTUtil getBTUtilInstance(Context context) {
        if(sBTUtil == null) {
            sBTUtil = new BTUtil(context);
        }
        return sBTUtil;
    }
    /**
     * 与设备配对 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    public boolean createBond(Class<? extends BluetoothDevice> btClass, BluetoothDevice btDevice) throws Exception {
        Method createBondMethod = btClass.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    /**
     * 与设备解除配对 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    public boolean removeBond(Class<? extends BluetoothDevice> btClass, BluetoothDevice btDevice)
            throws Exception {
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    public boolean setPin(Class btClass, BluetoothDevice btDevice, String str) throws Exception {
        try {
            Method removeBondMethod = btClass.getDeclaredMethod("setPin",
                    byte[].class);
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice, new Object[]{str.getBytes()});
            LogUtils.e("returnValue设置密码: " + returnValue.booleanValue());
            return returnValue.booleanValue();
        } catch (SecurityException e) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    // 取消用户输入
    public boolean cancelPairingUserInput(Class<?> btClass, BluetoothDevice device) throws Exception {
        Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
        cancelBondProcess(btClass,device) ;
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        LogUtils.i("取消对话框:"+ "cancelPairingUserInput"+returnValue.booleanValue());
        return returnValue.booleanValue();
    }

    // 取消配对
    public boolean cancelBondProcess(Class<?> btClass, BluetoothDevice device) throws Exception {
        Method createBondMethod = btClass.getMethod("cancelBondProcess");
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        return returnValue.booleanValue();
    }

    /**
     *
     * @param clsShow
     */
    public void printAllInform(Class<?> clsShow) {
        try {
            // 取得所有方法
            Method[] hideMethod = clsShow.getMethods();
            int i = 0;
            for (; i < hideMethod.length; i++) {
                LogUtils.e("method name"+ hideMethod[i].getName() + ";and the i is:" + i);
            }
            // 取得所有常量
            Field[] allFields = clsShow.getFields();
            for (i = 0; i < allFields.length; i++)
            {
                LogUtils.e("Field name:"+ allFields[i].getName());
            }
        } catch (SecurityException e) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public boolean pair(String strAddr, String strPsw) {
        boolean result = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        // some old robots don't have bluetooth.
        if(bluetoothAdapter == null) {
            LogUtils.d("bluetoothAdapter == null");
            return false;
        }

        bluetoothAdapter.cancelDiscovery();

        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);

        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            try {
                LogUtils.d("NOT BOND_BONDED");
                boolean flag1=setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
                boolean flag2=createBond(device.getClass(), device);
                result = true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                LogUtils.d("setPiN failed!");
                e.printStackTrace();
            } //
        } else {
            LogUtils.d("HAS BOND_BONDED");
            try {
                removeBond(device.getClass(), device);
                //BTUtil.createBond(device.getClass(), device);
                boolean flag1= setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
                boolean flag2= createBond(device.getClass(), device);
                result = true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                LogUtils.d("setPiN failed!");
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean unpair(String strAddr) {
        boolean result = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        // some old robots don't have bluetooth.
        if(bluetoothAdapter == null) {
            LogUtils.d("bluetoothAdapter == null");
            return false;
        }
        bluetoothAdapter.cancelDiscovery();

        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            try {
                LogUtils.d("NOT BOND_BONDED");
                boolean flag2=removeBond(device.getClass(), device);
                //boolean flag3=cancelPairingUserInput(device.getClass(), device);
//                remoteDevice = device; // 配对完毕就把这个设备对象传给全局的remoteDevice
                result = true;
            } catch (Exception e) {
                LogUtils.d("setPiN failed!");
                e.printStackTrace();
            }
        } else {
            LogUtils.d("HAS BOND_BONDED");
        }
        return result;
    }

    public Set<BluetoothDevice> getBondedDevices() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        // some old robots don't have bluetooth.
        if(bluetoothAdapter == null) {
            LogUtils.d("bluetoothAdapter == null");
            return null;
        }
        bluetoothAdapter.cancelDiscovery();

        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        Set<BluetoothDevice> list = null;
        try {
            list = bluetoothAdapter.getBondedDevices();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void startDiscovery() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        // some old robots don't have bluetooth.
        if(bluetoothAdapter == null) {
            LogUtils.d("bluetoothAdapter == null");
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
            try {
                Thread.sleep(3000);  //延迟3S用于蓝牙开启
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bluetoothAdapter.startDiscovery();
    }

    public void cancelDiscovery() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        // some old robots don't have bluetooth.
        if(bluetoothAdapter == null) {
            LogUtils.d("bluetoothAdapter == null");
            return;
        }
        bluetoothAdapter.cancelDiscovery();
    }

/*
    public ArrayList<RobotBluetooth> getConnectedDevice() {
        return connectedDeviceList;
    }*/

    private void startListenConnectedDevice(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        int a2dp = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
        int headset = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
        int health = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH);

        int flag = -1;
        if (a2dp == BluetoothProfile.STATE_CONNECTED) {
            flag = a2dp;
        } else if (headset == BluetoothProfile.STATE_CONNECTED) {
            flag = headset;
        } else if (health == BluetoothProfile.STATE_CONNECTED) {
            flag = health;
        }

        if (flag != -1) {
            bluetoothAdapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {
                @Override
                public void onServiceDisconnected(int profile) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onServiceConnected(int profile, BluetoothProfile proxy) {
                    // TODO Auto-generated method stub
                    List<BluetoothDevice> mDevices = proxy.getConnectedDevices();
                    connectedDeviceList.clear();
                    if (mDevices != null && mDevices.size() > 0) {
                        RobotBluetooth robotBluetooth = null;
                        for (BluetoothDevice device : mDevices) {
                            LogUtils.i("device name:"+ device.getName());
                            robotBluetooth = new RobotBluetooth();
                            robotBluetooth.setDeviceName(device.getName());
                            robotBluetooth.setDeviceAddress(device.getAddress());
                            connectedDeviceList.add(robotBluetooth);
                        }
                    } else {
                        LogUtils.i("mDevices is null");
                    }
                }
            }, flag);
        }
    }

    public interface BTConnectionStateListener {
        public void onConnectionStateChange();
    }

    public boolean connectDevice(String strAddr) {
        LogUtils.d("connectDevice");
        boolean isSuccess = true;
        stopDiscovery();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // some old robots don't have bluetooth.
        if(bluetoothAdapter == null) {
            LogUtils.d("bluetoothAdapter == null");
            return false;
        }
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
        if(device != null) {
            try {
                isSuccess = (boolean) mBluetoothA2dp.getClass().getMethod("connect", BluetoothDevice.class).invoke(mBluetoothA2dp, device);
            } catch (Exception e) {
                isSuccess = false;
                e.printStackTrace();
            }
        } else {
            isSuccess = false;
        }
        LogUtils.d("connectDevice isSuccess=" + isSuccess);
        return  isSuccess;
    }

    public boolean disConnectDevice(String strAddr) {
        LogUtils.d("disConnectDevice");
        boolean isSuccess = true;
        stopDiscovery();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        // some old robots don't have bluetooth.
        if(bluetoothAdapter == null) {
            LogUtils.d("bluetoothAdapter == null");
            return false;
        }
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
        if(device != null) {
            try {
                isSuccess = (boolean) mBluetoothA2dp.getClass().getMethod("disconnect", BluetoothDevice.class).invoke(mBluetoothA2dp, device);
            } catch (Exception e) {
                isSuccess = false;
                e.printStackTrace();
            }
        } else {
            isSuccess = false;
        }
        return  isSuccess;
    }

    private void stopDiscovery() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isDiscovering())
                bluetoothAdapter.cancelDiscovery();
        }
    }

    public List<BluetoothDevice> getConnectedDevice() {
        List<BluetoothDevice> list = null;
        if(mBluetoothA2dp != null) {
            list = mBluetoothA2dp.getConnectedDevices();
        } else {
        }
        return list;
    }

    private void registerConnectedListener(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if(bluetoothAdapter == null) {
            LogUtils.d("bluetoothAdapter == null");
            return;
        }
        if(context == null) {
            LogUtils.d("context == null");
        }
        bluetoothAdapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                LogUtils.d("onServiceConnected");
                if (BluetoothProfile.A2DP == profile) {
                    mBluetoothA2dp = (BluetoothA2dp) proxy;
                    StringBuffer buffer = new StringBuffer();
                    for(BluetoothDevice device :  mBluetoothA2dp.getConnectedDevices()) {
                        LogUtils.d("onServiceConnected device" + device.getName());
                    }
                }
            }

            @Override
            public void onServiceDisconnected(int profile) {
                LogUtils.d("onServiceDisconnected");
                if (BluetoothProfile.A2DP == profile) {
                    mBluetoothA2dp = null;
                }
            }
        }, BluetoothProfile.A2DP);
    }

    public boolean isBluetoothDiscovering() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.isDiscovering();
    }
    //2016_08_10 Add by liliang for [184] feature
    public void openBluetooth(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.d(TAG,"no bluetooth devices");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Log.d(TAG,"open the bluetooth devices");
            mBluetoothAdapter.enable();
        }
    }

    public String getBluetoothMac() {
        BluetoothAdapter bAdapt = BluetoothAdapter.getDefaultAdapter();
        String btMac="\"No Bluetooth Device!\"";
        if (bAdapt != null) {
         btMac = bAdapt.getAddress();
        } else {
           btMac = "No Bluetooth Device!";
        }
       Log.d(TAG,"Bluetooth MAC:  " + btMac);
        return btMac;
    }
    //2016_08_10 add by liliang for [184] feature ending
}
