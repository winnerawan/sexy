package com.browser.proxy.browserproxy.Browser;

import android.os.Handler;
import android.os.Message;
import com.browser.proxy.browserproxy.View.NinjaWebView;

public class NinjaClickHandler extends Handler {
    private NinjaWebView webView;

    public NinjaClickHandler(NinjaWebView webView) {
        super();
        this.webView = webView;
    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);
        webView.getBrowserController().onLongPress(message.getData().getString("url"));
    }
}
