package com.adsmedia.adsmodul;

import android.content.Context;
import android.util.Log;
import com.facebook.ads.AudienceNetworkAds;

public class MetaInitialize
        implements AudienceNetworkAds.InitListener {

    static void initialize(Context context) {
        if (!AudienceNetworkAds.isInitialized(context)) {
            AudienceNetworkAds
                    .buildInitSettings(context)
                    .withInitListener(new MetaInitialize())
                    .initialize();
        }
    }

    @Override
    public void onInitialized(AudienceNetworkAds.InitResult result) {
        Log.d(AudienceNetworkAds.TAG, result.getMessage());
    }
}