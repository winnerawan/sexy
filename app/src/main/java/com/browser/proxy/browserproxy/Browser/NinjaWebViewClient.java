package com.browser.proxy.browserproxy.Browser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.MailTo;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import androidx.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.*;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.browser.proxy.browserproxy.R;
import com.browser.proxy.browserproxy.Unit.BrowserUnit;
import com.browser.proxy.browserproxy.Unit.IntentUnit;
import com.browser.proxy.browserproxy.View.NinjaWebView;

import java.io.ByteArrayInputStream;

public class NinjaWebViewClient extends WebViewClient {
    private NinjaWebView ninjaWebView;
    private Context context;

    private AdBlock adBlock;

    private boolean white;

    private String currentProxyHost = "";
    private String currentProxyPass = "";
    private int currentProxyPort = 0;
    private String currentProxyUser = "";


    public void updateWhite(boolean white) {
        this.white = white;
    }

    private boolean enable;
    public void enableAdBlock(boolean enable) {
        this.enable = enable;
    }

    public NinjaWebViewClient(NinjaWebView ninjaWebView) {
        super();
        this.ninjaWebView = ninjaWebView;
        this.context = ninjaWebView.getContext();
        this.adBlock = ninjaWebView.getAdBlock();
        this.white = false;
        this.enable = true;

        Log.w("NINJAWEBVIEW", "INIT");
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        if (view.getTitle() == null || view.getTitle().isEmpty()) {
            ninjaWebView.update(context.getString(R.string.album_untitled), url);
        } else {
            ninjaWebView.update(view.getTitle(), url);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        if (!ninjaWebView.getSettings().getLoadsImagesAutomatically()) {
            ninjaWebView.getSettings().setLoadsImagesAutomatically(true);
        }

        if (view.getTitle() == null || view.getTitle().isEmpty()) {
            ninjaWebView.update(context.getString(R.string.album_untitled), url);
        } else {
            ninjaWebView.update(view.getTitle(), url);
        }

        if (ninjaWebView.isForeground()) {
            ninjaWebView.invalidate();
        } else {
            ninjaWebView.postInvalidate();
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(BrowserUnit.URL_SCHEME_MAIL_TO)) {
            Intent intent = IntentUnit.getEmailIntent(MailTo.parse(url));
            context.startActivity(intent);
            view.reload();
            return true;
        } else if (url.startsWith(BrowserUnit.URL_SCHEME_INTENT)) {
            Intent intent;
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                context.startActivity(intent);
                return true;
            } catch (Exception e) {} // When intent fail will crash
        }

        white = adBlock.isWhite(url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

    @Deprecated
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (enable && !white && adBlock.isAd(url)) {
            return new WebResourceResponse(
                    BrowserUnit.MIME_TYPE_TEXT_PLAIN,
                    BrowserUnit.URL_ENCODING,
                    new ByteArrayInputStream("".getBytes())
            );
        }

        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (enable && !white && adBlock.isAd(request.getUrl().toString())) {
                return new WebResourceResponse(
                        BrowserUnit.MIME_TYPE_TEXT_PLAIN,
                        BrowserUnit.URL_ENCODING,
                        new ByteArrayInputStream("".getBytes())
                );
            }
        }

        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public void onFormResubmission(WebView view, @NonNull final Message dontResend, final Message resend) {
        Context holder = IntentUnit.getContext();
        if (holder == null || !(holder instanceof Activity)) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(holder);
        builder.setCancelable(false);
        builder.setTitle(R.string.dialog_title_resubmission);
        builder.setMessage(R.string.dialog_content_resubmission);
        builder.setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resend.sendToTarget();
            }
        });
        builder.setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dontResend.sendToTarget();
            }
        });

        builder.create().show();
    }

    @Override
    public void onReceivedSslError(WebView view, @NonNull final SslErrorHandler handler, SslError error) {
        Context holder = IntentUnit.getContext();
        if (holder == null || !(holder instanceof Activity)) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(holder);
        builder.setCancelable(false);
        builder.setTitle(R.string.dialog_title_warning);
        builder.setMessage(R.string.dialog_content_ssl_error);
        builder.setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });
        builder.setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        if (error.getPrimaryError() == SslError.SSL_UNTRUSTED) {
            dialog.show();
        } else {
            handler.proceed();
        }
    }

//    @Override
//    public void onReceivedHttpAuthRequest(WebView view, @NonNull final HttpAuthHandler handler, String host, String realm) {
//        Context holder = IntentUnit.getContext();
//        if (holder == null || !(holder instanceof Activity)) {
//            return;
//        }
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(holder);
//        builder.setCancelable(false);
//        builder.setTitle(R.string.dialog_title_sign_in);
//
//        LinearLayout signInLayout = (LinearLayout) LayoutInflater.from(holder).inflate(R.layout.dialog_sign_in, null, false);
//        final EditText userEdit = (EditText) signInLayout.findViewById(R.id.dialog_sign_in_username);
//        final EditText passEdit = (EditText) signInLayout.findViewById(R.id.dialog_sign_in_password);
//        passEdit.setTypeface(Typeface.DEFAULT);
//        passEdit.setTransformationMethod(new PasswordTransformationMethod());
//        builder.setView(signInLayout);
//
//        builder.setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String user = userEdit.getText().toString().trim();
//                String pass = passEdit.getText().toString().trim();
//                handler.proceed(user, pass);
//            }
//        });
//
//        builder.setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                handler.cancel();
//            }
//        });
//
//        builder.create().show();
//    }

    @Override
    public void onReceivedHttpAuthRequest(WebView webView, final HttpAuthHandler httpAuthHandler, String str, String str2) {
        Context context2 = IntentUnit.getContext();
        if ((context2 instanceof Activity)) {
            if (this.currentProxyHost.equals("") || !str.equals(this.currentProxyHost)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context2);
                builder.setCancelable(false);
                builder.setTitle(R.string.dialog_title_sign_in);
                LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context2).inflate(R.layout.dialog_sign_in, (ViewGroup) null, false);
                final EditText editText = (EditText) linearLayout.findViewById(R.id.dialog_sign_in_username);
                final EditText editText2 = (EditText) linearLayout.findViewById(R.id.dialog_sign_in_password);
                editText2.setTypeface(Typeface.DEFAULT);
                editText2.setTransformationMethod(new PasswordTransformationMethod());
                builder.setView(linearLayout);
                builder.setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        httpAuthHandler.proceed(editText.getText().toString().trim(), editText2.getText().toString().trim());
                    }
                });
                builder.setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        httpAuthHandler.cancel();
                    }
                });
                builder.create().show();
                return;
            }
            httpAuthHandler.proceed(this.currentProxyUser, this.currentProxyPass);
        }
    }

    public void setProxy(String str, int i, String str2, String str3) {
        this.currentProxyHost = str;
        this.currentProxyPort = i;
        this.currentProxyUser = str2;
        this.currentProxyPass = str3;
        Log.w("NINJAWEBVIEW", "SET PROXY");

    }
}
