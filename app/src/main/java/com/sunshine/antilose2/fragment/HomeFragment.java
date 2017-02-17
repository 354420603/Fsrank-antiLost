package com.sunshine.antilose2.fragment;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunshine.antilose2.MainActivity;
import com.sunshine.antilose2.R;
import com.sunshine.antilose2.bean.BluetoothDeviceBean;

import app.mvp.MvpFragment;
import app.util.TimeUtilsHRX;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.sunshine.antilose2.R.id.ib_seek;


public class HomeFragment extends MvpFragment<HomeFragmentPresenter.HomeFragmentView, HomeFragmentPresenter> implements HomeFragmentPresenter.HomeFragmentView {
    private static HomeFragment homeFragment;
    @Bind(R.id.ib_editor)
    ImageButton ibEditor;
    @Bind(ib_seek)
    ImageButton ibSeek;
    @Bind(R.id.ib_bell)
    ImageButton ibBell;
    @Bind(R.id.tv_rssi)
    TextView tvRssi;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_state)
    TextView tvState;
    @Bind(R.id.image_search)
    ImageView imageSearch;
    @Bind(R.id.iv_head_icon)
    ImageView ivHeadIcon;
    @Bind(R.id.iv_icon_tag)
    CircleImageView ivIconTag;
    @Bind(R.id.tv_device_name)
    EditText tvDeviceName;
    @Bind(R.id.tv_address_name)
    TextView tvAddressName;


    public static HomeFragment getHomeFragment() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }

    @Override
    protected HomeFragmentPresenter createPresenter() {
        return new HomeFragmentPresenter();
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg, Toast.LENGTH_SHORT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String carrier = android.os.Build.MANUFACTURER;
        tvDeviceName.setText(carrier);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private int time = 0;
    private CountDownTimer countDownTimer = new CountDownTimer(1000000000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tvTime.setText(TimeUtilsHRX.toTimeStr(++time));
        }

        @Override
        public void onFinish() {

        }
    };
    private String lastAddress;

    public void showDeviceData(BluetoothDeviceBean deviceBean) {
        if (deviceBean == null) {
            String carrier = android.os.Build.MANUFACTURER;
            tvDeviceName.setText(carrier);
            tvRssi.setText("0");
            tvState.setText("否");
            tvAddressName.setText("adress");
            return;
        }
        if (!TextUtils.isEmpty(lastAddress) && !TextUtils.equals(deviceBean.getAddress(), lastAddress)) {
            countDownTimer.cancel();
            time = 0;
        }
        tvDeviceName.setText(deviceBean.getName());
        tvAddressName.setText(deviceBean.getAddress());
        tvRssi.setText(deviceBean.getRssi());
        tvState.setText("是");
        countDownTimer.start();
        lastAddress = deviceBean.getAddress();
    }

    @OnClick(R.id.ib_seek)
    public void onSeekClick() {
        sendData();
    }

    private AnimationDrawable ad;

    /*
    * 向蓝牙设备发送消息
    * */
    public void sendData() {
        final BluetoothGattCharacteristic alter = MainActivity.getBluetoothLeService().getAlterBluetoothGattCharacteristic();
        final BluetoothGatt gatt = MainActivity.getBluetoothLeService().getGatt();
        if (ad == null && alter != null) {
            ibSeek.setImageResource(R.drawable.alter_animation);
            ad = (AnimationDrawable) ibSeek.getDrawable();
            ad.start();
            byte[] s = new byte[]{2};
            alter.setValue(s);
            gatt.writeCharacteristic(alter);
            gatt.writeCharacteristic(alter);
        } else {
            assert ad != null;
            ad.stop();
            ad = null;
            ibSeek.setImageResource(R.drawable.seek_button_bg);
            byte[] s = new byte[]{0};
            alter.setValue(s);
            gatt.writeCharacteristic(alter);
        }
    }
}