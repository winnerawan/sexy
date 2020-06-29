package com.browser.proxy.browserproxy.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.browser.proxy.browserproxy.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(this::gotoBrowserActivity, 1000);
    }

    private void gotoBrowserActivity(){
        startActivity(new Intent(this, BrowserActivity.class));
        finish();
    }

}
