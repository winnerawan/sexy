package com.browser.proxy.browserproxy;

import android.app.Application;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig mCalligraphyConfig = new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/google_sans.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();

        CalligraphyConfig.initDefault(mCalligraphyConfig);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
