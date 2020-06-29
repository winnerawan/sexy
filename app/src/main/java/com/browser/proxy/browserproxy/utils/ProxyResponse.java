package com.browser.proxy.browserproxy.utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProxyResponse {

    @SerializedName("proxies")
    private List<ProxyItem> proxyItems;

    public List<ProxyItem> getProxyItems() {
        return proxyItems;
    }

    public void setProxyItems(List<ProxyItem> proxyItems) {
        this.proxyItems = proxyItems;
    }
}
