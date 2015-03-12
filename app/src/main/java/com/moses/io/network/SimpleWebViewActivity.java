package com.moses.io.network;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.moses.io.R;

/**
 * Created by 丹 on 2015/1/4.
 */
public class SimpleWebViewActivity extends Activity {
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_webview);

        mWebView = (WebView) findViewById(R.id.simple_webview);
        String httpURL = "http://localhost:8080/MyROOT/test";
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(httpURL);

    }
}
