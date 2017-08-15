package com.ubtechinc.alpha.wificonnect.bleperipheral;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.util.Log;

import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.robotinfo.RobotState;
import com.ubtechinc.alpha.wificonnect.IBluetoothDataInteract;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by ubt on 2017/2/9.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class UBTBluetoothService {

    private final String TAG = "UBTBluetoothService";

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeAdvertiser mAdvertiser;

    private AdvertiseData mAdvData;
    private AdvertiseData mAdvScanResponse;
    private AdvertiseSettings mAdvSettings;

    private BluetoothGattServer mBlueGattServer;
    private UBTGattService mGattService;

    private HashSet<BluetoothDevice> devices;

    private Context mContext;

    private byte[] resultByte = null;

    private String mSearialNum;

    private IBluetoothDataInteract dataInteract;

    private boolean isAlreadyBroadcast = false;

    private String backString = null;
    private final BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, final int status, int newState) {
            super.onConnectionStateChange(device, status, newState);

            Log.i(TAG, "onConnectionStateChange status="+status);
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    devices.add(device);
                    Log.v(TAG, "Connected to device: " + device.getAddress() + "devices number :" + devices.size());
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    devices.remove(device);
                    resultByte = null;
                    Log.v(TAG, "Disconnected from device" + "devices number :" + devices.size());
                }
            }
            else
            {
                // There are too many gatt errors (some of them not even in the documentation) so we just
                // show the error to the user.
                resultByte = null;
                Log.e(TAG, "Error when connecting: " + status);
            }
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset,
                                                BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            Log.i(TAG, "Device tried to read characteristic: " + characteristic.getUuid());
            Log.i(TAG, "read Value: " + Arrays.toString(characteristic.getValue())+"  offset="+offset);

            String wiwiListString = "";

            byte b[] = wiwiListString.getBytes();//String转换为byte[]

            //这里是读请求，可以把WIFI列表返回给手机
            if (offset != 0) {

                mBlueGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_INVALID_OFFSET, offset,
                        b);
                return;
            }
            mBlueGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS,
                    offset, b);
        }

        @Override
        public void onNotificationSent(BluetoothDevice device, int status) {
            super.onNotificationSent(device, status);
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId,
                                                 BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded,
                                                 int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite,
                    responseNeeded, offset, value);

            Log.d(TAG, "onCharacteristicWriteRequest  valueSIZE="+value.length);
            //收到信息手机发送过来的数据
            resultByte = BLEDataUtil.decode(value, resultByte);//作为全局变量..
            if(BLEDataUtil.isEnd(value)) {
                String result = new String(resultByte);
                resultByte = null;
                if (dataInteract != null){
                    dataInteract.receverData(result);
                }

            }

            Log.i(TAG, "Characteristic Write request: " + Arrays.toString(value)+"  offset="+offset);

            if (responseNeeded) {
                mBlueGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS  , 0, null);

                if(BLEDataUtil.isEnd(value)) {
                    if (dataInteract == null){
                        return;
                    }
                    backString = dataInteract.responseData();
                    byte[][] packets = BLEDataUtil.encode(backString);
                    LogUtils.i( "to Clinet :" + backString);
                    for(int i=0; i<packets.length; i++) {
                        characteristic.setValue(packets[i]);
//                        UBTLog.d(TAG, Arrays.toString(packets[i]));
                        mBlueGattServer.notifyCharacteristicChanged(device, characteristic, false);
                        SystemClock.sleep(10);
                    }
                    backString = null;
                }

            }
            else {
                Log.i("responseNeeded", "responseNeeded noneed111");
            }
        }



        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId,
                                             BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded,
                                             int offset,
                                             byte[] value) {
            Log.i(TAG, "Descriptor Write Request " + descriptor.getUuid() + " " + Arrays.toString(value));
            super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded,
                    offset, value);
            if(responseNeeded) {
                Log.i("responseNeeded", "responseNeeded 222");
                String s = "hellow world";
                byte b[] = s.getBytes();//String转换为byte[]

                mBlueGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0,null);
            }
            else {
                Log.i("responseNeeded", "responseNeeded noneed222");
            }

        }


        //.特征被读取。当回复响应成功后，客户端会读取然后触发本方法
        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
            Log.e(TAG, String.format("onDescriptorReadRequest：device name = %s, address = %s", device.getName(), device.getAddress()));
            Log.e(TAG, String.format("onDescriptorReadRequest：requestId = %s", requestId));
            mBlueGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null);
        }

    };

    AdvertiseCallback mAdvCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.i(TAG,"Advertising onStartSuccess: ");
        }

        @Override
        public void onStartFailure(int errorCode) {
            Log.i(TAG,"Advertising onStartFailure: " + errorCode);
            super.onStartFailure(errorCode);
            Log.i(TAG,"onStartFailure errorCode" + errorCode);
            if (errorCode == ADVERTISE_FAILED_DATA_TOO_LARGE) {
                Log.i(TAG,"Failed to start advertising as the advertise data to be broadcasted is larger than 31 bytes.");
            } else if (errorCode == ADVERTISE_FAILED_TOO_MANY_ADVERTISERS) {
                Log.i(TAG,"Failed to start advertising because no advertising get is available.");
            } else if (errorCode == ADVERTISE_FAILED_ALREADY_STARTED) {
                Log.i(TAG,"Failed to start advertising as the advertising is already started");
            } else if (errorCode == ADVERTISE_FAILED_INTERNAL_ERROR) {
                Log.i(TAG,"Operation failed due to an internal error");
            } else if (errorCode == ADVERTISE_FAILED_FEATURE_UNSUPPORTED) {
                Log.i(TAG,"This feature is not supported on this platform");
            }
        }
    };


    private UBTBluetoothService(Context context){
        mContext = context;
        mBluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        devices = new HashSet<BluetoothDevice>();

    }

    private static UBTBluetoothService service;

    public static UBTBluetoothService getBluetoothIntance(Context context){
        if (service == null){
            service = new UBTBluetoothService(context);
        }
        return service;
    }


    private void initBluetoothServer() {
        LogUtils.d("initBluetoothServer()" );
        mBlueGattServer = mBluetoothManager.openGattServer(mContext, mGattServerCallback);
        if(mBlueGattServer != null) {
            mGattService = new UBTGattService();
            mBlueGattServer.addService(mGattService.getGattService());
            initAdvertiser();
        }else{
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    initBluetoothServer();
                }
            },2000);
        }
    }



    private void initAdvertiser() {
        LogUtils.d("initAdvertiser()");

        mAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        mAdvSettings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .setConnectable(true)
                .build();


        ParcelUuid pUuid = new ParcelUuid(UUID.fromString(getaUUID()));

        try {
            mSearialNum = new String(RobotState.get().getSid().getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        LogUtils.i( "serialNum : " + mSearialNum);

        LogUtils.i( "serialByte[]" + Arrays.toString(mSearialNum.getBytes()));
        mAdvData = new AdvertiseData.Builder()
                .addServiceData(pUuid, mSearialNum.getBytes())
                .build();

        mAdvScanResponse = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .build();

        if (mAdvertiser != null){
            mAdvertiser.startAdvertising(mAdvSettings, mAdvData, mAdvScanResponse, mAdvCallback);
        }else{
        }
    }


    private String getaUUID(){
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        }
        catch (Exception ignored) {

        }
        UUID uuid = UUID.nameUUIDFromBytes(serial.getBytes()); //92e274db-80cc-35da-b71a-8e393a389401
        LogUtils.i("uuid :" + uuid.toString());
        return uuid.toString();
    }


    public void setBluetoothInteract(IBluetoothDataInteract dataInteract){
        this.dataInteract = dataInteract;
    }
    /**
     * 广播设备
     */
    public void beginBoradCastReciver(){
        Log.d(TAG, "beginBoradCastReciver");
        if (!isAlreadyBroadcast) {
            isAlreadyBroadcast = true;
            initBluetoothServer();
        }
    }

    public void sendBleData(String data){
        sendBleData(data, false);
    }

    public void sendBleData(String data, boolean isFinished){
        BluetoothGattCharacteristic characteristic = mGattService.getHeartWriteCharacteristic();
        byte[][] packets = BLEDataUtil.encode(data);
        for (BluetoothDevice device: devices){
            Log.d(TAG, "mac address" + device.getAddress());
            for(int i=0; i<packets.length; i++) {
                characteristic.setValue(packets[i]);
                mBlueGattServer.notifyCharacteristicChanged(device, characteristic, false);
                SystemClock.sleep(50);
            }
        }
        LogUtils.i( "sendBLEData String" + data);
        if (isFinished){
            disableRobotBluetooth();
        }
    }

    public void enableRobotBluetooth(){
        LogUtils.d("enable robot bluetooth");
        if (mBluetoothAdapter != null){
            if (mBluetoothAdapter.isEnabled()){
                beginBoradCastReciver();
            }else {
                mBluetoothAdapter.enable();
            }
        }
    }

    public void disableRobotBluetooth(){
        LogUtils.d("disable robot bluetooth");
        isAlreadyBroadcast = false;
        mBluetoothAdapter.disable();
    }

    public int getConnectedDevicesNum(){
      if (devices != null){
          return devices.size();
      }else{
          return 0;
      }
    }

}
