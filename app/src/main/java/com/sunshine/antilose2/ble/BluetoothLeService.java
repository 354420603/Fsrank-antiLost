package com.sunshine.antilose2.ble;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.orhanobut.logger.LL;
import com.sunshine.antilose2.util.Tools;

import java.util.List;
import java.util.UUID;

@SuppressLint("NewApi")
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    public BluetoothGatt mBluetoothGatt;
    public int mConnectionState = STATE_DISCONNECTED;

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";//
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";//
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";//
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";//
    public final static String ACTION_DATA_BATTERY = "com.sunshine.bletooth.loss";//
    public final static String ACTION_DATA_ALTER = "com.sunshine.bletooth.loss.alter";//
    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    public int newRssi;
    private String mAddress;
    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        /**
         * ����״̬
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            String intentAction;
            System.out.println("=======status:" + status);
            if (newState == BluetoothProfile.STATE_CONNECTED) {// ���ӳɹ�
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
//				MyApplication.mConnectionState = true;
                mBluetoothGatt.discoverServices();
                broadcastUpdate(intentAction);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {// �Ͽ�����
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
//				MyApplication.mConnectionState = false;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        /**
         * ���ַ���
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            gat = gatt;
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService batteryService = gatt.getService(UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb"));
                if (batteryService != null) {
                    BluetoothGattCharacteristic characteristic = batteryService.getCharacteristic(UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb"));
                }
                BluetoothGattService alertService = gatt.getService(UUID.fromString("00001802-0000-1000-8000-00805f9b34fb"));
                if (alertService != null) {
                    alterBluetoothGattCharacteristic = alertService.getCharacteristic(UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb"));
                }

                BluetoothGattService charService = gatt.getService(UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb"));
                if (charService != null) {
                    BluetoothGattCharacteristic characteristic = charService.getCharacteristic(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
                    int charaProp = characteristic.getProperties();
                    if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                        mBluetoothGatt.setCharacteristicNotification(characteristic, true);
                        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
                        if (descriptor != null) {
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            mBluetoothGatt.writeDescriptor(descriptor);
                            LL.e("==========订阅了特征值");
                        }
                    }
                }
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                LL.e("onServicesDiscovered received: " + status);
            }
        }

        /**
         *
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if ((characteristic.getUuid().toString()).equals(SampleGattAttributes.BATTERY_LEVEL_READ)) {
                    broadcastUpdate(ACTION_DATA_BATTERY, characteristic);
                }
                if ((characteristic.getUuid().toString()).equals(SampleGattAttributes.TX_POWER_LEVEL_READ)) {
                    broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                }
            }
        }

        /**
         *
         */
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor descriptor, int status) {

            System.out.println("onDescriptorWriteonDescriptorWrite = " + status + ", descriptor =" + descriptor.getUuid().toString());
        }

        /**
         *
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            LL.e("我收到数据了:   " + Tools.printHex(characteristic.getValue()));
            if ((characteristic.getUuid().toString()).equals(SampleGattAttributes.BATTERY_LEVEL_READ)) {
                broadcastUpdate(ACTION_DATA_BATTERY, characteristic);
                if (characteristic.getValue() != null) {
                    System.out.println(characteristic.getStringValue(0));
                }
                System.out.println("111111111111onCharacteristicChanged222-----");
            }
            if ((characteristic.getUuid().toString()).equals(SampleGattAttributes.TX_POWER_LEVEL_READ)) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                if (characteristic.getValue() != null) {
                    System.out.println(characteristic.getStringValue(0));
                }
            }
            if ((characteristic.getUuid().toString()).equals(SampleGattAttributes.LOSS_LEVEL_READ)) {
                byte[] value = characteristic.getValue();
                if (value[0] == 2) {
                    System.out.println("" + value[0]);
                    broadcastUpdate(ACTION_DATA_ALTER);
                }
            }

        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, final int rssi, int status) {
            newRssi = rssi;
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            System.out.println("--------write success----- status:" + status);

        }

    };
    private BluetoothGattCharacteristic alterBluetoothGattCharacteristic;
    private BluetoothGatt gat;

    /**
     * @param action
     */
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        intent.putExtra("add", mBluetoothDeviceAddress);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                LL.e("Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                LL.e("Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            System.out.println("Received heart rate: %d" + heartRate);
            LL.e(String.format("Received heart rate: %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(
                        data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));

                System.out.println("ppp" + stringBuilder.toString());
                intent.putExtra(EXTRA_DATA, data);
            }
        }
        sendBroadcast(intent);
    }

    public BluetoothGattCharacteristic getAlterBluetoothGattCharacteristic() {
        return alterBluetoothGattCharacteristic;
    }

    public BluetoothGatt getGatt() {
        return gat;
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();
    private String mBluetoothDeviceName;

    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                LL.i("Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            LL.e("Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            LL.i("BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device. Try to reconnect. )
        if (mBluetoothDeviceAddress != null
                && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter
                .getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        mBluetoothGatt = device.connectGatt(this, true, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mBluetoothDeviceName = device.getName();
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.disconnect();
        mBluetoothGatt = null;
    }

    public void wirteCharacteristic(BluetoothGattCharacteristic characteristic) {

        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        mBluetoothGatt.writeCharacteristic(characteristic);

    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    public void setCharacteristicNotification(
            BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID
                .fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
        if (descriptor != null) {
            System.out.println("write descriptor");
            descriptor
                    .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) {
            return null;
        }
        return mBluetoothGatt.getServices();
    }

    public boolean getRssiVal() {
        if (mBluetoothGatt == null) {
            return false;
        }
        return mBluetoothGatt.readRemoteRssi();
    }
}
