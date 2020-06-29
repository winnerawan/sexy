package com.browser.proxy.browserproxy.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProxyItem {
//    public static final long UNLOCKED_TIME = 3600000;
    @SerializedName("countryCode")
    @Expose
    private String countryCode = "";
    @SerializedName("countryFlag")
    @Expose
    private int countryFlag;
    @SerializedName("countryName")
    @Expose
    private String countryName = "";

    @SerializedName("proxyHost")
    @Expose
    private String proxyHost = "";
    @SerializedName("proxyPass")
    @Expose
    private String proxyPass = "";
    @SerializedName("proxyPort")
    @Expose
    private int proxyPort = 0;
    @SerializedName("proxyUser")
    @Expose
    private String proxyUser = "";

    private boolean isFree = false;
    private boolean isUnlockededNow = false;
    private long timeToEndUnlock = 0;

    public ProxyItem(Context context, String host, int port, String user, String pass, String countryCode, String countryName) {
        this.proxyHost = host;
        this.proxyPort = port;
        this.proxyUser = user;
        this.proxyPass = pass;
        this.countryCode = countryCode;
        this.countryName = countryName;

//        Resources resources = context.getResources();
        String cc = this.countryCode.toLowerCase();
//        this.countryFlag = resources.getIdentifier("flag_" + cc, "drawable", context.getPackageName());

        Resources resources = context.getResources();
        this.countryFlag = resources.getIdentifier("flag_"+cc, "drawable",
                context.getPackageName());

    }

    public void unlockProxy() {
        this.isUnlockededNow = true;
    }

    public long getTimeToEndUnlock() {
        return this.timeToEndUnlock;
    }

    public void setTimeToEndUnlock(long j) {
        this.timeToEndUnlock = j;
    }

    public boolean isUnlockededNow() {
        return this.isUnlockededNow;
    }

    public void setUnlockededNow(boolean z) {
        this.isUnlockededNow = z;
    }

    public boolean isFree() {
        return this.isFree;
    }

    public void setFree(boolean z) {
        this.isFree = z;
    }

    public String getProxyHost() {
        return this.proxyHost;
    }

    public int getProxyPort() {
        return this.proxyPort;
    }

    public String getProxyUser() {
        return this.proxyUser;
    }

    public String getProxyPass() {
        return this.proxyPass;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getCountryName() {
        if (this.countryCode.equals("") || this.countryCode.equals("0")) {
            return "Disabled";
        }
        if (this.countryName.equals("")) {
            return this.countryCode;
        }
        return this.countryName;
    }

    public int getCountryFlag() {

        Log.e("COUNTRYFLAG", String.valueOf("flag_" + getCountryName().toLowerCase()));
        return this.countryFlag;
    }
}
