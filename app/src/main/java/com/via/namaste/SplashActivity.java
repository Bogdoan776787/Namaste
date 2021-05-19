package com.via.namaste;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.congle7997.google_iap.BillingSubs;
import com.congle7997.google_iap.CallBackBilling;
import com.example.api.CheckNewInstall;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SplashActivity extends AppCompatActivity {
    String TAG = "my_SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new CheckNewInstall(this);


        List<String> listSkuFromStore = new ArrayList<>();
        listSkuFromStore.add(Config.REMOVE_ADS_12_MONTHS);
        BillingSubs billingSubs = new BillingSubs(SplashActivity.this, listSkuFromStore);
        billingSubs.checkPurchase(listSkuFromStore, new CallBackBilling() {
            @Override
            public void onPurchase() {
                Config.SHOW_ADS = false;
                handle();
            }

            @Override
            public void onNotPurchase() {
                Config.SHOW_ADS = true;
                handle();
            }

            @Override
            public void onNotLogin() {
                handle();
                Toasty.warning(SplashActivity.this, "Please login Google Play to continue", Toasty.LENGTH_LONG).show();
            }
        });
    }

    private void handle() {
        if (PrefrenceData.getIsFirstTour(getApplicationContext())) {
            Intent i = new Intent(SplashActivity.this, ActivityTour.class);
            startActivity(i);
            finish();
        } else {

            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();

        }
    }
}
