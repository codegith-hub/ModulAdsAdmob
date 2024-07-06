package com.adsmedia.adsmodul;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

public class OpenAds implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    public static String IDOPEN = "";
    public static MyApplication myApplication;
    public static AppOpenAdManager appOpenAdManager;
    public static Activity currentActivity;

    public OpenAds(MyApplication myApplication) {
        OpenAds.myApplication = myApplication;
        OpenAds.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public static void LoadOpenAds(String idOpenAds) {
        IDOPEN = idOpenAds;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
        appOpenAdManager.showAdIfAvailable(currentActivity);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }


    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    public static class AppOpenAdManager {
        private static final String LOG_TAG = "AppOpenAdManager";
        public static AppOpenAd appOpenAd = null;
        private static boolean isLoadingAd = false;
        static boolean isShowingAd = false;
        private static long loadTime = 0;

        public AppOpenAdManager() {
        }

        public static void loadAd(Context context) {
            if (isLoadingAd || isAdAvailable()) {
                return;
            }
            isLoadingAd = true;
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(context, IDOPEN, request, new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    isLoadingAd = false;
                    Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                    super.onAdFailedToLoad(loadAdError);
                }

                @Override
                public void onAdLoaded(@NonNull AppOpenAd ad) {
                    appOpenAd = ad;
                    isLoadingAd = false;
                    loadTime = (new Date()).getTime();
                    Log.d(LOG_TAG, "onAdLoaded.");

                    super.onAdLoaded(appOpenAd);
                }
            });
        }

        private static boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
            long dateDifference = (new Date()).getTime() - loadTime;
            long numMilliSecondsPerHour = 3600000;
            return (dateDifference < (numMilliSecondsPerHour * numHours));
        }

        private static boolean isAdAvailable() {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
        }

        public static void showAdIfAvailable(@NonNull final Activity activity) {
            showAdIfAvailable(activity, new OnShowAdCompleteListener() {
                @Override
                public void onShowAdComplete() {

                }
            });
        }

        public static void showAdIfAvailable(
                @NonNull final Activity activity,
                @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
                if (isShowingAd) {
                    return;
                }

                if (!isAdAvailable()) {
                    onShowAdCompleteListener.onShowAdComplete();
                    loadAd(activity);
                    return;
                }

                appOpenAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                appOpenAd = null;
                                isShowingAd = false;
                                onShowAdCompleteListener.onShowAdComplete();
                                loadAd(activity);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                appOpenAd = null;
                                isShowingAd = false;
                                onShowAdCompleteListener.onShowAdComplete();
                                loadAd(activity);
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                            }
                        });

                isShowingAd = true;
                appOpenAd.show(activity);
        }
    }
}