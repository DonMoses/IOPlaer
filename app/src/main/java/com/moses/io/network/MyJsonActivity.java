package com.moses.io.network;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moses.io.R;

import com.alibaba.fastjson.JSON;
import com.moses.io.beams.City;
import com.moses.io.beams.CityJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 丹 on 2015/1/10.
 */
public class MyJsonActivity extends Activity {
    Button mButton;
    TextView mTextView;
    WebView mWebView;
    ListView mListView;
    ProgressBar mProgressBar;
    List<City> cities;
    CityInfoAdapter adapter;
    String httpURL = "http://192.168.42.182:8080/MyROOT/test";
    String requestMethod = "POST";
    HashMap<String, String> parameters = new HashMap<>();
    HTTPUtil httpUtil = new HTTPUtil(httpURL,parameters,requestMethod);
    HTTPUtil.HttpCallBackInterface callBackInterface = new HTTPUtil.HttpCallBackInterface() {
        @Override
        public void onFinish(String s) {
            Message msg = Message.obtain();
            msg.obj = s;
            mHandler.sendMessage(msg);
        }
    };

//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            String str = msg.obj.toString();
//            JSONObject jsonObject;
//            String code = null;
//            try {
//                jsonObject  = new JSONObject(str);
//                code = jsonObject.getString("code");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            mTextView.setText("your code is:"+" "+code);
//        }
//    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mListView.setAdapter(adapter);
            String str = msg.obj.toString();
            Log.e("TAG", "str>>>>>>>>>>>>" + str);
            CityJSON cityJSON = JSON.parseObject(str, CityJSON.class);
            cities = cityJSON.getInfo();
            adapter.setmList(cities);
            mTextView.setText("ResultCode:" + "  " + cityJSON.getResultCode() + "          " + "ResultInfo:" + "  " + cityJSON.getResultInfo());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web);
        initView();

    }

    public void initView() {
        mButton = (Button) findViewById(R.id.my_web_button);
        mTextView = (TextView) findViewById(R.id.my_web_text);
        mWebView = (WebView) findViewById(R.id.my_web_web_view);
        mListView = (ListView) findViewById(R.id.my_web_list_view);
        mProgressBar = (ProgressBar) findViewById(R.id.pro_bar);
        mListView.setEmptyView(mProgressBar);
        adapter = new CityInfoAdapter(this);

    }

    public void onClickHttpHost(View v) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account", "moses");
        jsonObject.put("password", "0716");
        parameters.put("json", jsonObject.toString());
        Log.e("TAG", "jsonObject>>>>>>>>>>>>" + jsonObject.toString());
        httpUtil.sendRequest(callBackInterface);
    }

    class CityInfoAdapter extends BaseAdapter {
        List<City> mList = new ArrayList<>();
        LayoutInflater inflater;

        public CityInfoAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setmList(List<City> mList) {
            this.mList = mList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CityInfoViews cityInfoViews;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_view_city_info, null);
                cityInfoViews = new CityInfoViews();
                cityInfoViews.codeTextView = (TextView) convertView.findViewById(R.id.city_code_text_view);
                cityInfoViews.keyTextView = (TextView) convertView.findViewById(R.id.city_key_text_view);
                cityInfoViews.nearestTextView = (TextView) convertView.findViewById(R.id.city_nearest_text_view);
                cityInfoViews.valueTextView = (TextView) convertView.findViewById(R.id.city_value_text_view);
                convertView.setTag(cityInfoViews);
            } else {
                cityInfoViews = (CityInfoViews) convertView.getTag();
            }

            cityInfoViews.codeTextView.setText(mList.get(position).getCode());
            cityInfoViews.keyTextView.setText(mList.get(position).getKey());
            cityInfoViews.nearestTextView.setText(mList.get(position).getNearest());
            cityInfoViews.valueTextView.setText(mList.get(position).getValue());

            return convertView;
        }

        class CityInfoViews {
            TextView codeTextView;
            TextView keyTextView;
            TextView valueTextView;
            TextView nearestTextView;
        }
    }
}
