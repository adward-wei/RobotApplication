package com.ubtechinc.alpha2ctrlapp.util.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by wzt on 2016/7/6.
 */
public class BTUtil {
    public static final String TAG = "blueToothDeme";
    private static BTUtil sBTUtil;
    private Context mContext;
    private ArrayList<BluetoothDevice> connectedDeviceList;
    private BluetoothA2dp mBluetoothA2dp;

    private BTUtil(Context context) {
        // TODO Auto-generated constructor stub
        mContext = context;
        connectedDeviceList = new ArrayList<BluetoothDevice>();
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
    public boolean createBond(Class<? extends BluetoothDevice> btClass, BluetoothDevice btDevice)
            throws Exception {

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

    public  boolean setPin(Class btClass, BluetoothDevice btDevice,
                          String str) throws Exception {
        try {
            Method removeBondMethod = btClass.getDeclaredMethod("setPin",
                    new Class[]
                            {byte[].class});
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,
                    new Object[]
                            {str.getBytes()});
            Logger.d(TAG, "returnValue设置密码" + returnValue.booleanValue());
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
    public boolean cancelPairingUserInput(Class<?> btClass,
                                          BluetoothDevice device) throws Exception {
        Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
        cancelBondProcess(btClass,device) ;
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        Log.i(TAG,"cancelPairingUserInput****"+returnValue.booleanValue());
        return returnValue.booleanValue();
    }

    // 取消配对
    public boolean cancelBondProcess(Class<?> btClass,
                                     BluetoothDevice device) throws Exception {
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
            for (; i < hideMethod.length; i++)
            {
                Logger.d("method name", hideMethod[i].getName() + ";and the i is:"
                        + i);
            }
            // 取得所有常量
            Field[] allFields = clsShow.getFields();
            for (i = 0; i < allFields.length; i++)
            {
                Logger.d("Field name", allFields[i].getName());
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


    public boolean pair(BluetoothDevice device, String strPsw) {
        boolean result = false;
        try {
                Log.d("mylog", "NOT BOND_BONDED");
                boolean flag1=setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
                boolean flag2=createBond(device.getClass(), device);
                result = true;

            } catch (Exception e) {
                Log.d("mylog", "setPiN failed!");
                e.printStackTrace();
            } //

        return result;
    }

    public boolean unpair(String strAddr) {
        boolean result = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        // some old robots don't have bluetooth.
        if(bluetoothAdapter == null) {
            Log.d(TAG, "bluetoothAdapter == null");
            return false;
        }
        bluetoothAdapter.cancelDiscovery();

        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }



        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);

        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            try {
                Log.d("mylog", "NOT BOND_BONDED");
                boolean flag2=removeBond(device.getClass(), device);
                //boolean flag3=cancelPairingUserInput(device.getClass(), device);
//                remoteDevice = device; // 配对完毕就把这个设备对象传给全局的remoteDevice

                result = true;

            } catch (Exception e) {
                // TODO Auto-generated catch block

                Log.d("mylog", "setPiN failed!");
                e.printStackTrace();
            } //
        } else {
            Log.d("mylog", "HAS BOND_BONDED");
        }
        return result;
    }

    public Set<BluetoothDevice> getBondedDevices() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        // some old robots don't have bluetooth.
        if(bluetoothAdapter == null) {
            Log.d(TAG, "bluetoothAdapter == null");
            return null;
        }
        bluetoothAdapter.cancelDiscovery();

        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        Set<BluetoothDevice> list = null;
        try {
            list = (Set<BluetoothDevice>) bluetoothAdapter.getBondedDevices();
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
            Log.d(TAG, "bluetoothAdapter == null");
            return;
        }
        bluetoothAdapter.startDiscovery();
    }

    public void cancelDiscovery() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        // some old robots don't have bluetooth.
        if(bluetoothAdapter == null) {
            Log.d(TAG, "bluetoothAdapter == null");
            return;
        }
        bluetoothAdapter.cancelDiscovery();
    }





    public boolean connectDevice(String strAddr) {
        Log.d(TAG, "connectDevice");
        boolean isSuccess = true;
        stopDiscovery();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // some old robots don't have bluetooth.
        if(bluetoothAdapter == null) {
            Log.d(TAG, "bluetoothAdapter == null");
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
        Log.d(TAG, "connectDevice isSuccess=" + isSuccess);
        return  isSuccess;
    }

    public boolean disConnectDevice(String strAddr) {
        Log.d(TAG, "disConnectDevice");
        boolean isSuccess = true;
        stopDiscovery();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        // some old robots don't have bluetooth.
        if(bluetoothAdapter == null) {
            Log.d(TAG, "bluetoothAdapter == null");
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
            Log.d(TAG, "bluetoothAdapter == null");
            return;
        }
        if(context == null) {
            Log.d(TAG, "context == null");
        }
        bluetoothAdapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                Log.d(TAG, "onServiceConnected");
                if (BluetoothProfile.A2DP == profile) {
                    mBluetoothA2dp = (BluetoothA2dp) proxy;
                    StringBuffer buffer = new StringBuffer();
                    for(BluetoothDevice device :  mBluetoothA2dp.getConnectedDevices()) {
                        Log.d(TAG, "onServiceConnected device" + device.getName());
                    }
                }
            }

            @Override
            public void onServiceDisconnected(int profile) {
                Log.d(TAG, "onServiceDisconnected");
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


    public boolean pairRobot(String strAddr, String strPsw) {
        boolean result = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // some old robots don't have bluetooth.
        if(bluetoothAdapter == null) {
            Log.d(TAG, "bluetoothAdapter == null");
            return false;
        }

        bluetoothAdapter.cancelDiscovery();

        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);

        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            try {
                Log.d(TAG, "NOT BOND_BONDED");
                boolean flag1=setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
                boolean flag2=createBond(device.getClass(), device);
//                boolean flag3=cancelPairingUserInput(device.getClass(), device);
//                remoteDevice = device; // 配对完毕就把这个设备对象传给全局的remoteDevice

                result = true;

            } catch (Exception e) {
                // TODO Auto-generated catch block

                Log.d(TAG, "setPiN failed!");
                e.printStackTrace();
            } //
        } else {
            Log.d(TAG, "HAS BOND_BONDED");
            try {
                removeBond(device.getClass(), device);
                //BTUtil.createBond(device.getClass(), device);
                boolean flag1= setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
                boolean flag2= createBond(device.getClass(), device);
                //boolean flag3=cancelPairingUserInput(device.getClass(), device);
//                remoteDevice = device; // 如果绑定成功，就直接把这个设备对象传给全局的remoteDevice

                result = true;

            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.d(TAG, "setPiN failed!");
                e.printStackTrace();
            }
        }
        return result;
    }
}
