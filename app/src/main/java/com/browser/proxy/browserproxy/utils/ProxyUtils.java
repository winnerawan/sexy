package com.browser.proxy.browserproxy.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Proxy;
import android.net.ProxyInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.util.Log;
import android.webkit.WebView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ProxyUtils {
    private static final String LOG_TAG = "ProxyUtils";
    private static final String NON_PROXY_HOSTS = "localhost|127.0.0.1|googleads.g.doubleclick.net|csi.gstatic.com|imasdk.googleapis.com";

    public static void logggg(String str, String str2, Context context) {
        Log.i(str, str2);
    }

    public static boolean setProxy(WebView webView, String str, int i) {
        Log.i("PROXY", "SET PROXY FROM PROXY UTILS");
        int i2 = Build.VERSION.SDK_INT;
        if (i2 < 16) {
            return setProxyICS(webView, str, i);
        }
        if (i2 < 19) {
            return setProxyJB(webView, str, i);
        }
        if (i2 < 21) {
//            return setProxyKK(webView, str, i, "android.app.Application");
        }
        boolean proxyLollipop = setProxyMM(webView.getContext(), str, i);
        if (!proxyLollipop) {
            logggg(LOG_TAG, "proxy lolipopEX", webView.getContext());
            setProxyLollipop_ex(webView.getContext(), str, i);
        }
        return proxyLollipop;
    }

    public static boolean clearProxy(WebView webView) {
        int i = Build.VERSION.SDK_INT;
        if (i < 16) {
            return setProxyICS(webView, (String) null, 0);
        }
        if (i < 19) {
            return setProxyJB(webView, (String) null, 0);
        }
        if (i < 21) {
//            return setProxyKK(webView, (String) null, 0, "android.app.Application");
        }
        return setProxyLollipop(webView.getContext(), (String) null, 0);
    }

    private static boolean setProxyICS(WebView webView, String str, int i) {
        try {
            Class.forName("android.webkit.JWebCoreJavaBridge").getDeclaredMethod("updateProxy", new Class[]{Class.forName("android.net.ProxyProperties")}).invoke(getFieldValueSafely(Class.forName("android.webkit.BrowserFrame").getDeclaredField("sJavaBridge"), getFieldValueSafely(Class.forName("android.webkit.WebViewCore").getDeclaredField("mBrowserFrame"), getFieldValueSafely(Class.forName("android.webkit.WebView").getDeclaredField("mWebViewCore"), webView))), new Object[]{Class.forName("android.net.ProxyProperties").getConstructor(new Class[]{String.class, Integer.TYPE, String.class}).newInstance(new Object[]{str, Integer.valueOf(i), null})});
            logggg(LOG_TAG, "Setting proxy with 4.0 API successful!", webView.getContext());
            return true;
        } catch (Exception e) {
            logggg(LOG_TAG, "failed to setup HTTP proxy: " + e, webView.getContext());
            return false;
        }
    }

    private static boolean setProxyJB(WebView webView, String str, int i) {
        try {
            Object fieldValueSafely = getFieldValueSafely(Class.forName("android.webkit.BrowserFrame").getDeclaredField("sJavaBridge"), getFieldValueSafely(Class.forName("android.webkit.WebViewCore").getDeclaredField("mBrowserFrame"), getFieldValueSafely(Class.forName("android.webkit.WebViewClassic").getDeclaredField("mWebViewCore"), Class.forName("android.webkit.WebViewClassic").getDeclaredMethod("fromWebView", new Class[]{Class.forName("android.webkit.WebView")}).invoke((Object) null, new Object[]{webView}))));
            Constructor<?> constructor = Class.forName("android.net.ProxyProperties").getConstructor(new Class[]{String.class, Integer.TYPE, String.class});
            Class.forName("android.webkit.JWebCoreJavaBridge").getDeclaredMethod("updateProxy", new Class[]{Class.forName("android.net.ProxyProperties")}).invoke(fieldValueSafely, new Object[]{constructor.newInstance(new Object[]{str, Integer.valueOf(i), null})});
            logggg(LOG_TAG, "Setting proxy with 4.1 - 4.3 API successful!", webView.getContext());
            return true;
        } catch (Exception e) {
            logggg(LOG_TAG, "Setting proxy with >= 4.1 API failed with error: " + e.getMessage(), webView.getContext());
            return false;
        }
    }


    private static boolean setProxyLollipop_ex(Context context, String str, int i) {
        if (str == null) {
            System.clearProperty("http.proxyHost");
            System.clearProperty("http.proxyPort");
            System.clearProperty("https.proxyHost");
            System.clearProperty("https.proxyPort");
        } else {
            System.setProperty("http.proxyHost", str);
            System.setProperty("http.proxyPort", i + "");
            System.setProperty("https.proxyHost", str);
            System.setProperty("https.proxyPort", i + "");
            System.setProperty("http.nonProxyHosts", NON_PROXY_HOSTS);
            System.setProperty("https.nonProxyHosts", NON_PROXY_HOSTS);
        }
        try {
            Context applicationContext = context.getApplicationContext();
            Field field = Class.forName("android.app.Application").getField("mLoadedApk");
            field.setAccessible(true);
            Object obj = field.get(applicationContext);
            Field declaredField = Class.forName("android.app.LoadedApk").getDeclaredField("mReceivers");
            declaredField.setAccessible(true);
            boolean z = false;
            for (Object next : ((Map) ((Map) declaredField.get(obj)).get(applicationContext)).keySet()) {
                Method declaredMethod = next.getClass().getDeclaredMethod("onReceive", new Class[]{Context.class, Intent.class});
                try {
                    declaredMethod.invoke(next, new Object[]{applicationContext, new Intent("android.intent.action.PROXY_CHANGE")});
                } catch (Exception unused) {
                }
                z = true;
            }
            return z;
        } catch (Exception e) {
            e.printStackTrace();
            logggg(LOG_TAG, e.getMessage(), context);
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("all")
    private static boolean setProxyLollipop(Context context, String str, int i) {
        Method declaredMethod;
        Context context2 = context;
        String str2 = str;
        int i2 = i;
        if (str2 == null) {
            System.clearProperty("http.proxyHost");
            System.clearProperty("http.proxyPort");
            System.clearProperty("https.proxyHost");
            System.clearProperty("https.proxyPort");
        } else {
            System.setProperty("http.proxyHost", str2);
            System.setProperty("http.proxyPort", i2 + "");
            System.setProperty("https.proxyHost", str2);
            System.setProperty("https.proxyPort", i2 + "");
            System.setProperty("http.nonProxyHosts", NON_PROXY_HOSTS);
            System.setProperty("https.nonProxyHosts", NON_PROXY_HOSTS);
        }
        try {
            Context applicationContext = context.getApplicationContext();
            Field declaredField = Class.forName("android.app.Application").getDeclaredField("mLoadedApk");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(applicationContext);
            Field declaredField2 = Class.forName("android.app.LoadedApk").getDeclaredField("mReceivers");
            declaredField2.setAccessible(true);
            boolean z = false;
            ArrayMap receivers = (ArrayMap) declaredField.get(declaredField2);
            for (Object keySet : receivers.values()) {
                for (Object next : ((ArrayMap) keySet).keySet()) {
                    Class<?> cls = next.getClass();
                    if (cls.getName().contains("ProxyChangeListener") && (declaredMethod = cls.getDeclaredMethod("onReceive", new Class[]{Context.class, Intent.class})) != null) {
                        Intent intent = new Intent("android.intent.action.PROXY_CHANGE");
                        Class<?> cls2 = Class.forName("android.net.ProxyInfo");
                        Method method = cls2.getMethod("buildDirectProxy", new Class[]{String.class, Integer.TYPE});
                        if (method != null) {
                            intent.putExtra("proxy", (Parcelable) method.invoke(cls2, new Object[]{str2, Integer.valueOf(i)}));
                            declaredMethod.invoke(next, new Object[]{context2, intent});
                            z = true;
                        }
                    }
                }
            }
            logggg(LOG_TAG, "Lollipop successfully", context2);
            return z;
        } catch (Exception e) {
            e.printStackTrace();
            logggg(LOG_TAG, e.getMessage(), context2);
            return false;
        }
    }

    private static Object getFieldValueSafely(Field field, Object obj) throws IllegalArgumentException, IllegalAccessException {
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        Object obj2 = field.get(obj);
        field.setAccessible(isAccessible);
        return obj2;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("all")
    private static boolean setProxyMM(Context ctx, String host, int port) {
        Log.w(TAG, "try to setProxyMM");
        Context appContext = ctx.getApplicationContext();

        try {
            Class applictionCls = appContext.getClass();
            Field loadedApkField = applictionCls.getField("mLoadedApk");
            loadedApkField.setAccessible(true);
            Object loadedApk = loadedApkField.get(appContext);
            Class loadedApkCls = Class.forName("android.app.LoadedApk");
            Field receiversField = loadedApkCls.getDeclaredField("mReceivers");
            receiversField.setAccessible(true);
            ArrayMap receivers = (ArrayMap) receiversField.get(loadedApk);
            for (Object receiverMap : receivers.values()) {
                for (Object rec : ((ArrayMap) receiverMap).keySet()) {
                    Class clazz = rec.getClass();
                    if (clazz.getName().contains("ProxyChangeListener")) {
                        Method onReceiveMethod = clazz.getDeclaredMethod("onReceive", Context.class, Intent.class);
                        Intent intent = new Intent(Proxy.PROXY_CHANGE_ACTION);
                        Bundle extras = new Bundle();
                        List<String> exclusionsList = new ArrayList<>(1);
                        ProxyInfo proxyInfo = ProxyInfo.buildDirectProxy(host, port, exclusionsList);
                        extras.putParcelable("android.intent.extra.PROXY_INFO", proxyInfo);
                        intent.putExtras(extras);

                        onReceiveMethod.invoke(rec, appContext, intent);
                    }
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "setProxyKKPlus - exception : {}", e);
            return false;
        }
        return true;
    }
}
