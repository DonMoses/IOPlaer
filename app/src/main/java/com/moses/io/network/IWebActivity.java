package com.moses.io.network;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.moses.io.R;

import java.util.HashMap;

/**
 * Created by 丹 on 2015/1/9.
 */
public class IWebActivity extends Activity {
    TextView mTextView;
    Button mButton;
    WebView mWebView;
    String servletURL = "http://localhost:8080/MyROOT/test";
    HashMap<String,String> hashMap = new HashMap<>();
    String requestMethod = "GET";
    HTTPUtil httpUtil = new HTTPUtil(servletURL,hashMap,requestMethod);
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mWebView.loadDataWithBaseURL(null,msg.obj.toString(),"text/html","UTF-8",null);
            Log.e("TAG", "handleMessage()>>>>>>>>>>>>>>>>>>>>>"+msg.obj.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web);
        initView();
    }

    public void initView() {
        mTextView = (TextView) findViewById(R.id.my_web_text);
        mButton = (Button) findViewById(R.id.my_web_button);
        mWebView = (WebView) findViewById(R.id.my_web_web_view);
        hashMap.put("account","moses");
        hashMap.put("password","0716");
    }

    public void onClickHttpHost(View v) {

        httpUtil.sendRequest( new HTTPUtil.HttpCallBackInterface() {
                    @Override
                    public void onFinish(String s) {
                        Message msg = Message.obtain();
                        msg.obj = s;
                        mHandler.sendMessage(msg);
                        Log.e("TAG", "onFinish()>>>>>>>>>>>>>>>>>>>>>");
                    }
                });
        Log.e("TAG", "onClickHttpHost>>>>>>>>>>>>>>>>>>>>>");
    }


}
