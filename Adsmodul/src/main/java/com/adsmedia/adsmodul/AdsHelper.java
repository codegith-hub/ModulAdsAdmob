package com.adsmedia.adsmodul;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adsmedia.mastermodul.MasterAdsHelper;
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerAd;
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerAdLoadListener;
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerRequest;
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerSize;
import com.bytedance.sdk.openadsdk.api.init.PAGConfig;
import com.bytedance.sdk.openadsdk.api.init.PAGSdk;
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAd;
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAdLoadListener;
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialRequest;
import com.bytedance.sdk.openadsdk.api.reward.PAGRewardItem;
import com.bytedance.sdk.openadsdk.api.reward.PAGRewardedAd;
import com.bytedance.sdk.openadsdk.api.reward.PAGRewardedAdInteractionListener;
import com.bytedance.sdk.openadsdk.api.reward.PAGRewardedAdLoadListener;
import com.bytedance.sdk.openadsdk.api.reward.PAGRewardedRequest;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.RewardedInterstitialAd;
import com.facebook.ads.RewardedInterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


public class AdsHelper {
    public static ConsentInformation consentInformation;
    public static ConsentRequestParameters params;
    public static boolean directData = false;

    public static void gdpr(Activity activity, Boolean childDirected, String gameAppId, String selectAds) {
        if (selectAds.equals("ADMOB")) {
            params = new ConsentRequestParameters
                    .Builder()
                    .setTagForUnderAgeOfConsent(childDirected)
                    .build();
            consentInformation = UserMessagingPlatform.getConsentInformation(activity);
            consentInformation.requestConsentInfoUpdate(
                    activity,
                    params,
                    (ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
                        UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                                activity,
                                (ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                                    if (loadAndShowError != null) {

                                    }
                                    // Consent has been gathered.
                                    if (consentInformation.canRequestAds()) {
                                        initializeAds(activity, gameAppId, selectAds);
                                    }

                                }
                        );

                    },
                    (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                        // Consent gathering failed.
                    });
            if (consentInformation.canRequestAds()) {
                initializeAds(activity, gameAppId, selectAds);
            }
        }
    }

    public static void initializeAds(Activity activity, String gameAppId, String selectAds) {
        new Thread(
                () ->
                        // Initialize the Google Mobile Ads SDK on a background thread.
                        MobileAds.initialize(
                                activity,
                                initializationStatus -> {
                                    Map<String, AdapterStatus> statusMap =
                                            initializationStatus.getAdapterStatusMap();
                                    for (String adapterClass : statusMap.keySet()) {
                                        AdapterStatus status = statusMap.get(adapterClass);
                                        Log.d(
                                                "MyApp",
                                                String.format(
                                                        "Adapter name: %s, Description: %s, Latency: %d",
                                                        adapterClass, status.getDescription(), status.getLatency()));
                                    }

                                }))
                .start();
        MasterAdsHelper.initializeAds(activity, gameAppId);

    }

    public static void initializeAds(Activity activity, String keypos, String gameAppId, String selectAds) {
        switch (selectAds) {
            case "ADMOB":
                new Thread(
                        () ->
                                // Initialize the Google Mobile Ads SDK on a background thread.
                                MobileAds.initialize(
                                        activity,
                                        initializationStatus -> {
                                            Map<String, AdapterStatus> statusMap =
                                                    initializationStatus.getAdapterStatusMap();
                                            for (String adapterClass : statusMap.keySet()) {
                                                AdapterStatus status = statusMap.get(adapterClass);
                                                Log.d(
                                                        "MyApp",
                                                        String.format(
                                                                "Adapter name: %s, Description: %s, Latency: %d",
                                                                adapterClass, status.getDescription(), status.getLatency()));
                                            }

                                        }))
                        .start();
                break;
            case "FACEBOOK":
                if (!AudienceNetworkAds.isInitialized(activity)) {
                    AudienceNetworkAds
                            .buildInitSettings(activity)
                            .withInitListener(new MetaInitialize())
                            .initialize();
                }
                break;
            case "UNITY":
                IUnityAdsInitializationListener listener = new IUnityAdsInitializationListener() {
                    @Override
                    public void onInitializationComplete() {

                    }

                    @Override
                    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {

                    }
                };
                UnityAds.initialize(activity, gameAppId, listener);
                break;
            case "PANGLE":
                PAGConfig pAGInitConfig = buildNewConfig(activity, gameAppId);
                PAGSdk.init(activity, pAGInitConfig, new PAGSdk.PAGInitCallback() {
                    @Override
                    public void success() {
                    }

                    @Override
                    public void fail(int code, String msg) {
                    }
                });
                break;
        }
        MasterAdsHelper.initializeAds(activity, keypos);

    }

    public static void debugMode(Boolean debug) {
        MasterAdsHelper.debugMode(debug);
        AdSettings.setTestMode(true);
    }

    public static AdView bannerAdmob;
    public static com.facebook.ads.AdView bannerFan;
    public static BannerView unityBanner;
    public static PAGBannerAd bannerAdPangle;

    public static void showBanner(Activity activity, RelativeLayout layout, String bannerID, String selectAds) {
        switch (selectAds) {
            case "ADMOB":
                AdRequest request = new AdRequest.Builder()
                        .build();
                bannerAdmob = new AdView(activity);
                bannerAdmob.setAdUnitId(bannerID);
                layout.addView(bannerAdmob);
                directData = true;
                AdSize adSize = getAdSize(activity);
                bannerAdmob.setAdSize(adSize);
                bannerAdmob.loadAd(request);
                bannerAdmob.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {

                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        if (bannerAdmob != null) {
                            bannerAdmob.destroy();
                        }
                        MasterAdsHelper.showBanner(activity, layout);
                    }

                    @Override
                    public void onAdOpened() {
                    }

                    @Override
                    public void onAdClicked() {
                    }

                    @Override
                    public void onAdClosed() {
                    }
                });
                break;
            case "FACEBOOK":
                bannerFan = new com.facebook.ads.AdView(activity, bannerID,
                        com.facebook.ads.AdSize.BANNER_HEIGHT_50);
                layout.addView(bannerFan);
                directData = true;
                com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {
                        if (bannerFan != null) {
                            bannerFan.destroy();
                        }
                        MasterAdsHelper.showBanner(activity, layout);
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {

                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }
                };
                bannerFan.loadAd(bannerFan.buildLoadAdConfig().withAdListener(adListener).build());
                break;
            case "UNITY":
                unityBanner = new BannerView(activity, bannerID, new UnityBannerSize(320, 50));
                unityBanner.load();
                directData = true;
                layout.addView(unityBanner);
                unityBanner.setListener(new BannerView.Listener() {
                    @Override
                    public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
                        super.onBannerFailedToLoad(bannerAdView, errorInfo);
                        if (unityBanner != null) {
                            unityBanner.destroy();
                        }
                        MasterAdsHelper.showBanner(activity, layout);
                    }
                });
                break;
            case "PANGLE":
                PAGBannerSize bannerSize = PAGBannerSize.BANNER_W_320_H_50;
                PAGBannerRequest bannerRequest = new PAGBannerRequest(bannerSize);
                directData = true;
                PAGBannerAd.loadAd(bannerID, bannerRequest, new PAGBannerAdLoadListener() {
                    @Override
                    public void onError(int code, String message) {
                        MasterAdsHelper.showBanner(activity, layout);
                    }

                    @Override
                    public void onAdLoaded(PAGBannerAd bannerAd) {
                        if (bannerAd == null) {
                            return;
                        }
                        bannerAdPangle = bannerAd;
                        if (bannerAdPangle != null) {
                            layout.addView(bannerAdPangle.getBannerView());
                        }

                    }
                });
                break;
        }

    }

    private static AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public static InterstitialAd interstitialAdmob;
    public static com.facebook.ads.InterstitialAd interstitialFAN;
    public static PAGInterstitialAd interstitialAdPangle;

    public static void loadInterstitial(Activity activity, String admobId, String selectAds) {
        switch (selectAds) {
            case "ADMOB":
                AdRequest request = new AdRequest.Builder()
                        .build();
                directData = true;
                InterstitialAd.load(activity, admobId, request,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                interstitialAdmob = interstitialAd;
                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.i(TAG, loadAdError.getMessage());
                                interstitialAdmob = null;
                            }
                        });
                break;
            case "FACEBOOK":
                interstitialFAN = new com.facebook.ads.InterstitialAd(activity, admobId);
                interstitialFAN.loadAd();
                break;
            case "UNITY":
                IUnityAdsLoadListener listener = new IUnityAdsLoadListener() {
                    @Override
                    public void onUnityAdsAdLoaded(String placementId) {

                    }

                    @Override
                    public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {

                    }
                };
                try {
                    UnityAds.load(admobId, listener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "PANGLE":
                try {
                    PAGInterstitialRequest request2 = new PAGInterstitialRequest();
                    PAGInterstitialAd.loadAd(admobId,
                            request2,
                            new PAGInterstitialAdLoadListener() {
                                @Override
                                public void onError(int code, String message) {
                                }

                                @Override
                                public void onAdLoaded(PAGInterstitialAd pagInterstitialAd) {
                                    interstitialAdPangle = pagInterstitialAd;
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        MasterAdsHelper.loadInterstitial(activity);
    }

    private static RewardedInterstitialAd rewardedInterstitialAd;
    private static RewardedAd mRewardedAd;
    public static boolean unlockreward = false;
    public static PAGRewardedRequest request;
    public static PAGRewardedAd rewardedAd;

    public static void loadReward(Activity activity, String rewardId, String selectAds) {
        directData = true;
        MasterAdsHelper.loadReward(activity);
        switch (selectAds) {
            case "ADMOB":
                AdRequest adRequest2 = new AdRequest.Builder()
                        .build();
                RewardedAd.load(activity, rewardId,
                        adRequest2, new RewardedAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                mRewardedAd = null;
                            }

                            @Override
                            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {

                                mRewardedAd = rewardedAd;

                            }
                        });

                break;
            case "FACEBOOK":
                rewardedInterstitialAd = new RewardedInterstitialAd(activity, rewardId);
                RewardedInterstitialAdListener rewardedInterstitialAdListener =
                        new RewardedInterstitialAdListener() {
                            @Override
                            public void onError(Ad ad, AdError error) {

                            }

                            @Override
                            public void onAdLoaded(Ad ad) {

                            }

                            @Override
                            public void onAdClicked(Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {

                            }

                            @Override
                            public void onRewardedInterstitialCompleted() {
                                unlockreward = true;
                            }

                            @Override
                            public void onRewardedInterstitialClosed() {
                            }
                        };
                rewardedInterstitialAd.loadAd(
                        rewardedInterstitialAd.buildLoadAdConfig()
                                .withAdListener(rewardedInterstitialAdListener)
                                .build());
                break;
            case "UNITY":
                IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
                    @Override
                    public void onUnityAdsAdLoaded(String placementId) {

                    }

                    @Override
                    public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {

                    }
                };
                try {
                    UnityAds.load(rewardId, loadListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "PANGLE":
                request = new PAGRewardedRequest();
                PAGRewardedAd.loadAd(rewardId,
                        request,
                        new PAGRewardedAdLoadListener() {
                            @Override
                            public void onError(int code, String message) {
                            }

                            @Override
                            public void onAdLoaded(PAGRewardedAd rewardedAd) {
                            }
                        });
                break;
        }

    }

    public static int count = 0;
    public static int countInterstitial = 0;

    public static void showInterstitial(Activity activity, String admobId, int interval, String selectAds) {
        if (count >= interval) {
            switch (selectAds) {
                case "ADMOB":
                    if (countInterstitial >= 1) {
                        MasterAdsHelper.showInterstitial(activity);
                        countInterstitial = 0;
                    } else {
                        if (interstitialAdmob != null) {
                            interstitialAdmob.show(activity);
                        }
                        countInterstitial++;
                    }
                    loadInterstitial(activity, admobId, selectAds);
                    break;
                case "FACEBOOK":
                    if (countInterstitial >= 1) {
                        MasterAdsHelper.showInterstitial(activity);
                        countInterstitial = 0;
                    } else {
                        if (interstitialFAN == null || !interstitialFAN.isAdLoaded()) {

                        } else {
                            interstitialFAN.show();
                        }
                        countInterstitial++;
                    }
                    loadInterstitial(activity, admobId, selectAds);

                    break;
                case "UNITY":
                    if (countInterstitial >= 1) {
                        MasterAdsHelper.showInterstitial(activity);
                        countInterstitial = 0;
                    } else {
                        IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
                            @Override
                            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                                MasterAdsHelper.showInterstitial(activity);
                            }

                            @Override
                            public void onUnityAdsShowStart(String placementId) {

                            }

                            @Override
                            public void onUnityAdsShowClick(String placementId) {

                            }

                            @Override
                            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {

                            }
                        };
                        UnityAds.show(activity, admobId, new UnityAdsShowOptions(), showListener);
                        countInterstitial++;
                    }
                    break;
                case "PANGLE":
                    if (countInterstitial >= 1) {
                        MasterAdsHelper.showInterstitial(activity);
                        countInterstitial = 0;
                    } else {
                        if (interstitialAdPangle != null) {
                            interstitialAdPangle.show(activity);
                            interstitialAdPangle = null;
                        }
                        countInterstitial++;
                    }
                    break;

            }
            count = 0;
        } else {
            count++;
        }
    }

    public static int counterReward = 0;

    public static void showReward(Activity activity, String admobId, String selectAds) {
        switch (selectAds) {
            case "ADMOB":
                if (counterReward >= 1) {
                    MasterAdsHelper.showReward(activity);
                    unlockreward = true;
                    counterReward = 0;
                } else {
                    if (mRewardedAd != null) {
                        Activity activityContext = activity;
                        mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                unlockreward = true;
                            }
                        });
                    }
                    counterReward++;
                }
                loadReward(activity, admobId, selectAds);

                break;
            case "FACEBOOK":
                if (counterReward >= 1) {
                    MasterAdsHelper.showReward(activity);
                    unlockreward = true;
                    counterReward = 0;
                } else {
                    if (rewardedInterstitialAd == null || !rewardedInterstitialAd.isAdLoaded()) {
                    } else {
                        rewardedInterstitialAd.show(
                                rewardedInterstitialAd.buildShowAdConfig()
                                        .withAppOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                        .build());
                    }
                    counterReward++;
                }
                loadReward(activity, admobId, selectAds);
                break;
            case "UNITY":
                if (counterReward >= 1) {
                    MasterAdsHelper.showReward(activity);
                    unlockreward = true;
                    counterReward = 0;
                } else {
                    IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
                        @Override
                        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                        }

                        @Override
                        public void onUnityAdsShowStart(String placementId) {

                        }

                        @Override
                        public void onUnityAdsShowClick(String placementId) {

                        }

                        @Override
                        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                            unlockreward = true;
                        }
                    };
                    UnityAds.show(activity, admobId, new UnityAdsShowOptions(), showListener);
                    counterReward++;
                }
                loadReward(activity, admobId, selectAds);
                break;
            case "PANGLE":
                if (counterReward >= 1) {
                    MasterAdsHelper.showReward(activity);
                    unlockreward = true;
                    counterReward = 0;
                } else {
                    if (rewardedAd != null) {
                        rewardedAd.setAdInteractionListener(new PAGRewardedAdInteractionListener() {

                            @Override
                            public void onAdShowed() {

                            }

                            @Override
                            public void onAdClicked() {

                            }

                            @Override
                            public void onAdDismissed() {

                            }

                            @Override
                            public void onUserEarnedReward(PAGRewardItem item) {
                                unlockreward = true;
                            }

                            @Override
                            public void onUserEarnedRewardFail(int errorCode, String errorMsg) {

                            }
                        });
                        rewardedAd.show(activity);
                    }

                    counterReward++;
                }
                loadReward(activity, admobId, selectAds);
                break;
        }
    }

    public static final String md5(final String s) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            //Logger.logStackTrace(TAG,e);
        }
        return "";
    }

    public static void onDestroy(String selectAds) {
        if (selectAds.equals("ADMOB")) {
            if (bannerAdmob != null) {
                bannerAdmob.destroy();
            }
        } else if (selectAds.equals("FACEBOOK")) {
            if (bannerFan != null) {
                bannerFan.destroy();
            }
        }
    }

    private static PAGConfig buildNewConfig(Context context, String appID) {
        return new PAGConfig.Builder()
                .appId(appID)
                .debugLog(true)
                .build();
    }
}
