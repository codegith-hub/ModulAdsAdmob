package com.adsmedia.adsmodul;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.google.android.gms.ads.MobileAds;

public class MyApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static OpenAds openAds;
    static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        openAds = new OpenAds(this);
        //MobileAds.initialize(this);
    }
}