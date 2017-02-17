package com.sunshine.antilose2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.orhanobut.logger.LL;
import com.sunshine.antilose2.bean.BluetoothDeviceBean;
import com.sunshine.antilose2.ble.BluetoothLeService;
import com.sunshine.antilose2.fragment.AboutFragment;
import com.sunshine.antilose2.fragment.CameraFragment;
import com.sunshine.antilose2.fragment.HomeFragment;
import com.sunshine.antilose2.fragment.LocationFragment;
import com.sunshine.antilose2.listener.OnItemClickListener;
import com.sunshine.antilose2.util.Tools;
import com.sunshine.antilose2.view.ListViewDecoration;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.mvp.MvpActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends MvpActivity<MainPresenter.MainView, MainPresenter>
        implements MainPresenter.MainView, View.OnClickListener {

    @Bind(R.id.container)
    FrameLayout container;
    @Bind(R.id.tab_home)
    RadioButton tabHome;
    @Bind(R.id.tab_location)
    RadioButton tabLocation;
    @Bind(R.id.tab_camera)
    RadioButton tabCamera;
    @Bind(R.id.tab_about)
    RadioButton tabAbout;
    @Bind(R.id.SlidingPane)
    SlidingPaneLayout mSlidingPane;
    @Bind(R.id.recycler_view)
    SwipeMenuRecyclerView mSwipeMenuRecyclerView;

    private Context mContext;

    private Fragment[] fragments = {HomeFragment.getHomeFragment(), LocationFragment.getLocationFragment(),
            CameraFragment.getCameraFragment(), AboutFragment.getAboutFragment()};
    private Fragment lastFragment;
    private int REQUEST_ENABLE_BT = 10;
    private List<BluetoothDeviceBean> mDevices;
    private com.sunshine.antilose2.adapter.MenuAdapter mMenuAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleViewGone();
        setContentView(R.layout.app_bar_main1);
        mContext = this;
        ButterKnife.bind(this);
        initSlide();
        initFragment();
        initLeft();
    }

    private void initLeft() {
//        mStrings = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            mStrings.add(new BluetoothDeviceBean("我是第" + i + "个", "Df-ff-00", String.valueOf(0)));
//        }
        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mSwipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mSwipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        mSwipeMenuRecyclerView.addItemDecoration(new ListViewDecoration(getContext()));// 添加分割线。
        // 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
        // 设置菜单创建器。
        mSwipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);
        mMenuAdapter = new com.sunshine.antilose2.adapter.MenuAdapter(null);
        mMenuAdapter.setOnItemClickListener(onItemClickListener);
        mSwipeMenuRecyclerView.setAdapter(mMenuAdapter);
    }

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.item_height);

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
//            {
//                SwipeMenuItem addItem = new SwipeMenuItem(mContext)
//                        .setBackgroundDrawable(R.drawable.selector_green)// 点击的背景。
//                        .setImage(R.mipmap.ic_action_add) // 图标。
//                        .setWidth(width) // 宽度。
//                        .setHeight(height); // 高度。
//                swipeLeftMenu.addMenuItem(addItem); // 添加一个按钮到左侧菜单。
//
//                SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
//                        .setBackgroundDrawable(R.drawable.selector_red)
//                        .setImage(R.mipmap.ic_action_close)
//                        .setWidth(width)
//                        .setHeight(height);
//
//                swipeLeftMenu.addMenuItem(closeItem); // 添加一个按钮到左侧菜单。
//            }

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。

                SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_green)
                        .setText("断开")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。

                SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_purple)
                        .setImage(R.mipmap.ic_action_close)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。

            }
        }
    };

    private String lastAddress;
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            for (BluetoothDeviceBean deviceBean : mDevices) {
                deviceBean.setState(false);
            }
            BluetoothDeviceBean deviceBean = mDevices.get(position);
            deviceBean.setState(true);
            mMenuAdapter.setData(mDevices);
            mSlidingPane.closePane();
            String address = deviceBean.getAddress();
            if (mBluetoothLeService != null) {
                if (!TextUtils.isEmpty(lastAddress) && !TextUtils.equals(address, lastAddress)) {
                    mBluetoothLeService.disconnect();
                    LL.e("断开连接");
                }
                if (!TextUtils.equals(address, lastAddress)) {
                    displayBlutoothDeviceData(deviceBean);
                    boolean connect = mBluetoothLeService.connect(address);
                    LL.e("连接设备" + address + connect);
                }
            }
            lastAddress = address;
        }
    };

    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView#RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                if (menuPosition == 0) {// 删除按钮被点击。
                    mDevices.remove(adapterPosition);
                    mMenuAdapter.notifyItemRemoved(adapterPosition);
                    disconnectDevice();
                } else if (menuPosition == 1) {// 断开按钮被点击。
                    disconnectDevice();
                } else if (menuPosition == 2) {// 关闭按钮被点击。
                    showToast("我是关闭");
                }
            }
            closeable.smoothCloseMenu();
        }
    };

    /*
    *设备断开连接
    * */
    private void disconnectDevice() {
        mBluetoothLeService.disconnect();
        lastAddress = null;
        HomeFragment.getHomeFragment().showDeviceData(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
        } else {
            presenter.initBle();
        }
    }

    private void displayBlutoothDeviceData(BluetoothDeviceBean deviceBean) {
        HomeFragment.getHomeFragment().showDeviceData(deviceBean);
        if (Build.VERSION.SDK_INT >= 23) {
            presenter.startScanDevice(false);
        } else {
            presenter.startLeScan(false);
        }
    }

    /*
    * 初始化Fragment
    * */
    private void initFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragments[0], "home");
        fragmentTransaction.add(R.id.container, fragments[1], "location");
        fragmentTransaction.hide(fragments[1]);
        fragmentTransaction.add(R.id.container, fragments[2], "camera");
        fragmentTransaction.hide(fragments[2]);
        fragmentTransaction.add(R.id.container, fragments[3], "about");
        fragmentTransaction.hide(fragments[3]);
        fragmentTransaction.commit();
        tabHome.setOnClickListener(this);
        tabLocation.setOnClickListener(this);
        tabCamera.setOnClickListener(this);
        tabAbout.setOnClickListener(this);
        lastFragment = fragments[0];
    }

    /*
    * 初始化侧滑菜单
    * */
    private void initSlide() {
        //设置滑动视差 可选
        mSlidingPane.setParallaxDistance(200);
        //菜单滑动的颜色渐变设置 可选
        mSlidingPane.setCoveredFadeColor(getResources().getColor(R.color.colorAccent));
        //主视图滑动的颜色渐变设置 可选
        mSlidingPane.setSliderFadeColor(0);
        //滑动监听 可选
        mSlidingPane.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("mylog", "slide --- " + slideOffset);

                // slideOffset这个参数 是跟随滑动0-1变化的 通过这个数值变化我们可以做出一些不一样的滑动效果
//                ll_menu.setScaleY(slideOffset / 2 + 0.5F);
//                ll_menu.setScaleX(slideOffset/ 2 +  0.5F);
//                ll_main.setScaleY(1 - slideOffset / 5);
//                - setParallaxDistance(int parallaxBy) 设置滑动视差
//                        - setCoveredFadeColor(int color) 导航菜单视图的滑动颜色渐变
//                        - setSliderFadeColor(int color) 主视图的滑动颜色渐变
//                        - setPanelSlideListener(SlidingPaneLayout.PanelSlideListener listener) 滑动监听
//                        - openPane() 打开导航菜单
//                        - closePane() 关闭导航菜单

            }

            @Override
            public void onPanelOpened(View panel) {
                Log.i("mylog", "slide --- open");
            }

            @Override
            public void onPanelClosed(View panel) {
                Log.i("mylog", "slide --- close");
            }
        });
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg, Toast.LENGTH_SHORT);
    }

    @OnClick(R.id.btn_search)
    public void startSearchDevice() {
        if (mDevices != null) {
            mDevices.clear();
        }
        presenter.initBluetooth();
    }

    /*
    * 底部菜单tab的点击事件
    * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_home:
                switchFragment(fragments[0]);
                break;
            case R.id.tab_location:
                switchFragment(fragments[1]);
                break;
            case R.id.tab_camera:
                switchFragment(fragments[2]);
                break;
            case R.id.tab_about:
                switchFragment(fragments[3]);
                break;
        }
    }

    /*
    * 切换fragment
    * */
    private void switchFragment(Fragment fragment) {
        if (fragment != lastFragment) {
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            beginTransaction.hide(lastFragment).show(fragment).commit();
            lastFragment = fragment;
        }

    }

    private int count = 0;

    @Override
    public void onBackPressed() {
        count++;
        if (count == 2)
            super.onBackPressed();
        else {
            showToast("再次点击退出 应用");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    count = 0;
                }
            }, 3000);
        }
    }

    @Override
    public void showDeviceNotSupport() {
        finish();
    }

    /*
    * 打开蓝牙
    * */
    @Override
    public void openBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    @Override
    public void showGpsClose() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, 1);
    }

    /*
    * 处理搜索到的设备
    * */
    @Override
    public void showSearchDevice(BluetoothDevice device, int rssi) {
        if (mDevices == null) {
            mDevices = new ArrayList<>();
        }
        for (BluetoothDeviceBean bean : mDevices) {
            if (TextUtils.equals(device.getAddress(), bean.getAddress()))
                return;
        }
        mDevices.add(new BluetoothDeviceBean(device.getName(), device.getAddress(), String.valueOf(rssi)));
        mMenuAdapter.setData(mDevices);
    }

    public static BluetoothLeService getBluetoothLeService() {
        return mBluetoothLeService;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private static BluetoothLeService mBluetoothLeService;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                LL.e("Unable to initialize Bluetooth");
            }
//            mBluetoothLeService.connect(macAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    private void displayData(byte[] data) {
        String result = Tools.printHex(data);
        LL.e(result);
    }

    private IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
