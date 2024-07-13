package com.sampleads.newkonsep;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.adsmedia.adsmodul.AdsHelper;
import com.adsmedia.adsmodul.OpenAds;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AdsHelper.initializeAds(this,BuildConfig.APPLICATION_ID);
        if (BuildConfig.DEBUG){
            AdsHelper.debugMode(true);
        }
        OpenAds.LoadOpenAds("ca-app-pub-3940256099942544/9257395921");
        OpenAds.AppOpenAdManager.showAdIfAvailable(SplashActivity.this, new OpenAds.OnShowAdCompleteListener() {
            @Override
            public void onShowAdComplete() {
                startActivity(true);
            }
        });



    }

    private void startActivity(boolean useTime){
        if (useTime){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                }
            },1000*3);
        }else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

    }

}