package com.adsmedia.adsmodul;


import static com.adsmedia.adsmodul.AdsHelper.keyopenApp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.adsmedia.mastermodul.MasterAdsHelper;
import com.adsmedia.mastermodul.RestApi;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

public class MyApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static OpenAds openAds;
    static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        openAds = new OpenAds(this);
        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
        OneSignal.initWithContext(this, keyopenApp);
        OneSignal.getNotifications().requestPermission(false, Continue.none());
    }
}