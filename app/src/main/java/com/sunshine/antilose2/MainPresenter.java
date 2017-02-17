package com.sunshine.antilose2;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.orhanobut.logger.LL;
import com.sunshine.antilose2.util.Tools;

import app.mvp.MvpBasePresenter;
import app.mvp.MvpBaseView;

/**
 * Created by 黄仁兴 on 2017/2/14.
 */

public class MainPresenter extends MvpBasePresenter<MainPresenter.MainView> {

    private final MainActivity activity;
    private BluetoothAdapter mBluetoothAdapter;
    private ScanCallback scanCallback;
    private int scanTime = 20000;
    private String deviceName = "FSRK Anti-Loss";

    public interface MainView extends MvpBaseView {
        void showDeviceNotSupport();

        void openBluetooth();

        void showGpsClose();

        void showSearchDevice(BluetoothDevice device, int rssi);
    }

    public MainPresenter(MainActivity a) {
        this.activity = a;
    }

    /*
    * 初始化蓝牙相关的数据
    * */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void initBluetooth() {
        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            getView().showToast("设备不支持蓝牙BLE");
            getView().showDeviceNotSupport();
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            getView().showDeviceNotSupport();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            getView().openBluetooth();
        } else {
            initBle();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void initBle() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Tools.isGpsEnable(activity)) {
                new AlertDialog.Builder(activity)
                        .setTitle("打开GPS")
                        .setMessage("请打开GPS模块")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getView().showDeviceNotSupport();
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getView().showGpsClose();
                    }
                }).show();
            } else {
                startScanDevice(true);
            }
        } else {
            startLeScan(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void startLeScan(boolean b) {
        if (b) {
            LL.e("开始搜索设备");
            mBluetoothAdapter.startLeScan(leScan);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(leScan);
                    LL.e("停止搜索");
                }
            }, scanTime);
        } else {
            mBluetoothAdapter.stopLeScan(leScan);
            LL.e("停止搜索");
        }
    }


    public void startScanDevice(boolean b) {
            /*
      * 系统6.0之后蓝牙搜索（包含6.0）
      * */
        if (scanCallback == null)
            scanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    BluetoothDevice device = result.getDevice();
                    int rssi = result.getRssi();
                    LL.i(device.getName());
                    if (!TextUtils.isEmpty(device.getName())) {
                        if (device.getName().equals(deviceName)) {
                            getView().showSearchDevice(device,rssi);
                        }
                    }
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                    LL.d("搜索失败 " + errorCode);
                }
            };
        if (b) {
            LL.e("开始搜索设备");
            mBluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
                }
            }, scanTime);
        } else
            mBluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
    }


    /*
    * 蓝牙搜索6.0以下
    * */
    private BluetoothAdapter.LeScanCallback leScan = new BluetoothAdapter.LeScanCallback() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            LL.e(device.getName());
            if (!TextUtils.isEmpty(device.getName())) {
                if (device.getName().equals(deviceName)) {
                    getView().showSearchDevice(device, rssi);
                }
            }
        }
    };
}
