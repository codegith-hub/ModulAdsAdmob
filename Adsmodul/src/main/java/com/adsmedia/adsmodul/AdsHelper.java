package com.adsmedia.adsmodul;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.app.Activity;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.BannerListener;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class AdsHelper {
    public static String Notification = "f4020d13-3918-4c6a-899b-9585f30cdb84";
    public static ConsentInformation consentInformation;
    public static ConsentDebugSettings debugSettings;
    public static ConsentRequestParameters params;
    private static final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
    public static void gdpr (Activity activity, Boolean childDirected){
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
                                    initializeAds(activity);
                                }

                            }
                    );

                },
                (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                    // Consent gathering failed.
                });
        if (consentInformation.canRequestAds()) {
            initializeAds(activity);
        }
    }
    public static void initializeAds(Activity activity){
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

           }
    public static void debugMode(Boolean debug){
        StartAppSDK.setTestAdsEnabled(debug);
    }
    public static AdView bannerAdmob;

    public static void showBanner(Activity activity, RelativeLayout layout, String admobId, String metaId){
        AdRequest request = new AdRequest.Builder()
                .build();
        bannerAdmob = new AdView(activity);
        bannerAdmob.setAdUnitId(admobId);
        layout.addView(bannerAdmob);
        AdSize adSize = getAdSize(activity);
        bannerAdmob.setAdSize(adSize);
        bannerAdmob.loadAd(request);
        bannerAdmob.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                        Banner startAppBanner = new Banner(activity, new BannerListener() {
                            @Override
                            public void onReceiveAd(View view) {
                            }

                            @Override
                            public void onFailedToReceiveAd(View view) {
                                layout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onImpression(View view) {
                            }

                            @Override
                            public void onClick(View view) {
                            }
                        });
                        RelativeLayout.LayoutParams bannerParameters =
                                new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                        bannerParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        layout.addView(startAppBanner, bannerParameters);

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
    public static void loadInterstitial(Activity activity, String admobId, String metaId){
        AdRequest request = new AdRequest.Builder()
                .build();
        InterstitialAd.load(activity, admobId, request,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        interstitialAdmob = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        interstitialAdmob = null;
                    }
                });
    }
    public static int count = 0;
    public static void showInterstitial(Activity activity, String admobId, String metaId, int interval){
       if (count>=interval){
           if (interstitialAdmob != null) {
               interstitialAdmob.show(activity);
           } else {
               StartAppAd.showAd(activity);
           }
           loadInterstitial(activity, admobId, metaId);
           count=0;
       } else {
           count++;
       }
    }
    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
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
}
