package com.moses.io.network;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.moses.io.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 丹 on 2015/1/7.
 */
public class MyWebActivity extends Activity {
    TextView mTextView;
    Button mButton;
    WebView mWebView;
    String servletURL = "http://localhost:8080/MyROOT/test";

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
    }

    public void onClickHttpHost(View v) {
        mButton.setVisibility(View.INVISIBLE);
//        requestHttpByGet(servletURL+"?account=moses&password=0716");
        requestHttpByPost(servletURL, "moses", "0716");

    }

    /**
     * HttpURLConnection 方式 GET 请求
     *
     */
    public void requestHttpByGet(String httpURL) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                HttpURLConnection connection;
                StringBuilder stringBuilder = null;
                try {
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    InputStream in = connection.getInputStream();
                    stringBuilder = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return stringBuilder != null ? stringBuilder.toString() : null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mWebView.loadDataWithBaseURL(null,s,"text/html","UTF-8",null);
//                mWebView.loadData(s,"text/html","utf-8");
                Log.v("TAG",s);
            }
        }.execute(httpURL);
    }

    /**
     * HttpClient 方式 POST 请求
     *
     */
    public void requestHttpByPost(String ss, String account, String password) {
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                //post参数
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(params[0]);
                List<NameValuePair> parameters = new ArrayList<>();
                parameters.add(new BasicNameValuePair("account", params[1]));
                parameters.add(new BasicNameValuePair("password", params[2]));
                UrlEncodedFormEntity entity = null;
                try {
                    entity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                httpPost.setEntity(entity);
                StringBuilder stringBuilder = null;
                try {
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        InputStream is = httpEntity.getContent();
                        stringBuilder = new StringBuilder();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //接收响应数据
                return stringBuilder != null ? stringBuilder.toString() : null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mTextView.setText(s);
            }
        }.execute(ss, account, password);
    }

}
