package com.ubtechinc.alpha.wificonnect.bleperipheral;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

/**
 * Created by ubt on 2017/2/10.
 */

public class UBTGattService {

    private BluetoothGattService mBluetoothGattService;

    //三个特征
    private BluetoothGattCharacteristic mHeartRateMeasurementCharacteristic;
    private BluetoothGattCharacteristic mBodySensorLocationCharacteristic;
    private BluetoothGattCharacteristic mHeartRateControlPoint;

    private static final UUID HEART_RATE_SERVICE_UUID = UUID
            .fromString("0000180D-0000-1000-8000-00805f9b34fb");


    private static final UUID HEART_RATE_MEASUREMENT_UUID = UUID
            .fromString("00002A37-0000-1000-8000-00805f9b34fb");

    /**
     * See <a href="https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.body_sensor_location.xml">
     * Body Sensor Location</a>
     */
    private static final UUID BODY_SENSOR_LOCATION_UUID = UUID
            .fromString("00002A38-0000-1000-8000-00805f9b34fb");

    /**
     * See <a href="https://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_control_point.xml">
     * Heart Rate Control Point</a>
     */
    private static final UUID HEART_RATE_CONTROL_POINT_UUID = UUID
            .fromString("00002A39-0000-1000-8000-00805f9b34fb");



    public UBTGattService(){
        mBluetoothGattService = new BluetoothGattService(HEART_RATE_SERVICE_UUID,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);


        mHeartRateMeasurementCharacteristic =
                new BluetoothGattCharacteristic(HEART_RATE_MEASUREMENT_UUID,
                        BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            /* No permissions */ 0);


        mBodySensorLocationCharacteristic =
                new BluetoothGattCharacteristic(BODY_SENSOR_LOCATION_UUID,
                        BluetoothGattCharacteristic.PROPERTY_READ,
                        BluetoothGattCharacteristic.PERMISSION_READ);

        mHeartRateControlPoint =
                new BluetoothGattCharacteristic(HEART_RATE_CONTROL_POINT_UUID,
                        BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                        BluetoothGattCharacteristic.PERMISSION_WRITE| BluetoothGattCharacteristic.PERMISSION_READ| BluetoothGattCharacteristic.PROPERTY_NOTIFY);

        mBluetoothGattService = new BluetoothGattService(HEART_RATE_SERVICE_UUID,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);

        mBluetoothGattService.addCharacteristic(mHeartRateMeasurementCharacteristic);
        mBluetoothGattService.addCharacteristic(mBodySensorLocationCharacteristic);
        mBluetoothGattService.addCharacteristic(mHeartRateControlPoint);
    }

    public BluetoothGattService getGattService(){
        return mBluetoothGattService;
    }


    public BluetoothGattCharacteristic getHeartWriteCharacteristic(){
        return mHeartRateControlPoint;
    }

    /**
     * Function to communicate to the ServiceFragment that a device wants to write to a
     * characteristic.
     *
     * The ServiceFragment should check that the value being written is valid and
     * return a code appropriately. The ServiceFragment should update the UI to reflect the change.
     * @param characteristic Characteristic to write to
     * @param value Value to write to the characteristic
     * @return {@link android.bluetooth.BluetoothGatt#GATT_SUCCESS} if the read operation
     * was completed successfully. See {@link android.bluetooth.BluetoothGatt} for GATT return codes.
     */
    public int writeCharacteristic(BluetoothGattCharacteristic characteristic, int offset, byte[] value) {
        throw new UnsupportedOperationException("Method writeCharacteristic not overriden");
    };

}
