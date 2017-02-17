package com.sunshine.antilose2.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.sunshine.antilose2.MainActivity;
import com.sunshine.antilose2.R;

import app.mvp.MvpActivity;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by 黄仁兴 on 2017/2/15.
 */

public class WelcomeActivity extends MvpActivity<WelcomePresenter.MainView, WelcomePresenter>
        implements WelcomePresenter.MainView {
    private int REQUEST_ENABLE_BT = 10;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleViewGone();
        setContentView(R.layout.activity_welcome);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "设备不支持蓝牙BLE", Toast.LENGTH_SHORT).show();
            finish();
        }
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        } else {
            initPermission();
        }
    }


    /*
      * 检测应用是否给予了权限
      * */
    @TargetApi(Build.VERSION_CODES.M)
    private void initPermission() {
        if (Build.VERSION.SDK_INT >= M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // 给出一个提示，告诉用户为什么需要这个权限
                    Toast.makeText(this, "缺少权限应用无法正常运行，请在设置中开启权限", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    // 用户没有拒绝，直接申请权限
                    String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION};
                    ActivityCompat.requestPermissions(this, permissions, 10);
                }
            } else {
                startActivityToMainActivity();
            }
        } else {
            startActivityToMainActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (Integer integer : grantResults) {
            if (integer != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "你还没有开启相应权限，请在设置中开启", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        startActivityToMainActivity();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startActivityToMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getContext(), MainActivity.class));
                finish();
            }
        }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
        } else {
            initPermission();
        }
    }

    @Override
    protected WelcomePresenter createPresenter() {
        return new WelcomePresenter();
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg, Toast.LENGTH_SHORT);
    }
}
