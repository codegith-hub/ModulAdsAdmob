package com.sampleads.newkonsep;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.adsmedia.adsmodul.AdsHelper;

public class MainActivity extends AppCompatActivity {
    String SelectAds = "ADMOB";
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AdsHelper.initializeAds(this,BuildConfig.APPLICATION_ID,"8025677", SelectAds);
        if (BuildConfig.DEBUG){
            AdsHelper.debugMode(true);
        }
        AdsHelper.loadInterstitial(this,
                "ca-app-pub-3940256099942544/1033173712x",SelectAds);
        AdsHelper.showBanner(this, findViewById(R.id.layAds),
                "ca-app-pub-3940256099942544/9214589741x",SelectAds);
        AdsHelper.loadReward(this,"ca-app-pub-3940256099942544/5224354917x", SelectAds);
        this.findViewById(R.id.tbShow).setOnClickListener(v -> {
            AdsHelper.showInterstitial(MainActivity.this,
                    "ca-app-pub-3940256099942544/1033173712x",0,SelectAds);
        });

        this.findViewById(R.id.tbReward).setOnClickListener(v -> {
            AdsHelper.showReward(MainActivity.this,"ca-app-pub-3940256099942544/5224354917x",SelectAds);
        });

    }

    public void onResume() {
        if (AdsHelper.unlockreward){
            Toast.makeText(this, "Add Coins!",
                    Toast.LENGTH_LONG).show();
            AdsHelper.unlockreward = false;
        }
        super.onResume();
    }
}
