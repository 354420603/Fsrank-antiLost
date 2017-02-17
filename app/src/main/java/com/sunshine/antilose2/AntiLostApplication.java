package com.sunshine.antilose2;

import android.app.Application;

import com.orhanobut.logger.LL;

/**
 * Created by 黄仁兴 on 2017/2/14.
 */

public class AntiLostApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LL.init("huang");
    }
}
