package com.moses.io.network;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.moses.io.R;
import com.moses.io.beams.inernetlist.InternetList;
import com.moses.io.beams.inernetlist.InternetMerchant;
//import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 丹 on 2015/1/14.
 */
public class InternetListActivity extends Activity {
    ListView mListView;
    String httpURL = "http://192.168.42.10:8080/MyROOT/test";
    HashMap<String, String> parameters = new HashMap<>();
    String requestMethod = "POST";
    List<InternetMerchant> mMerchants = new ArrayList<>();
    MyInternetListAdapter myInternetListAdapter;
    HTTPUtil httpUtil = new HTTPUtil(httpURL,parameters,requestMethod);
    MyHttpBitmapUtil bitmapUtil = new MyHttpBitmapUtil();
    HTTPUtil.HttpCallBackInterface httpCallBackInterface = new HTTPUtil.HttpCallBackInterface() {
        @Override
        public void onFinish(String s) {
            Message msg = Message.obtain();
            msg.obj = s;
            msg.what = 1;
            mHandler.sendMessage(msg);
        }
    };

    InternetList internetList;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String internetJsonStr = msg.obj.toString();
                    getJSONObj(internetJsonStr);
                    break;
                case 1024:
                    mMerchants = internetList.getInfo().getMerchantKey();
                    myInternetListAdapter.setList(mMerchants);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_list);
        try {
            initViews();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initViews() throws JSONException {
        mListView = (ListView) findViewById(R.id.internet_list_view);
        mListView.setEmptyView(findViewById(R.id.internet_progressbar));
        myInternetListAdapter = new MyInternetListAdapter(this);
        mListView.setAdapter(myInternetListAdapter);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account", "moses");
        jsonObject.put("password", "0716");
        parameters.put("json", jsonObject.toString());
        Log.e("TAG", "jsonObject>>>>>>>>>>>>" + jsonObject.toString());
        gerInternetList();
    }

    public void gerInternetList() {
        httpUtil.sendRequest(httpCallBackInterface);
    }

    class MyInternetListAdapter extends BaseAdapter {
        List<InternetMerchant> mList = new ArrayList<>();
        LayoutInflater inflater;

        public MyInternetListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setList(List<InternetMerchant> mList) {
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
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.view_internet_list, null);
                viewHolder = new ViewHolder();
                viewHolder.merchantImg = (ImageView) convertView.findViewById(R.id.internet_list_img);
                viewHolder.merchantType_1 = (ImageView) convertView.findViewById(R.id.internet_type_1);
                viewHolder.merchantType_2 = (ImageView) convertView.findViewById(R.id.internet_type_2);
                viewHolder.merchantType_3 = (ImageView) convertView.findViewById(R.id.internet_type_3);
                viewHolder.merchantName = (TextView) convertView.findViewById(R.id.internet_list_name);
                viewHolder.merchantCoupon = (TextView) convertView.findViewById(R.id.internet_list_coupon);
                viewHolder.merchantLocation = (TextView) convertView.findViewById(R.id.internet_list_location);
                viewHolder.merchantDistance = (TextView) convertView.findViewById(R.id.internet_list_distance);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //显示文字信息
            viewHolder.merchantName.setText(mList.get(position).getName());
            viewHolder.merchantCoupon.setText(mList.get(position).getCoupon());
            try {
                viewHolder.merchantLocation.setText(new String(mList.get(position).getLocation().getBytes(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            viewHolder.merchantDistance.setText(mList.get(position).getDistance());
            //这里判断显示的TYPE
            String couponType = mList.get(position).getCouponType();
            String cardType = mList.get(position).getCardType();
            String groupType = mList.get(position).getGroupType();
            if (couponType.equals("YES")) {
                viewHolder.merchantType_3.setVisibility(View.VISIBLE);
            }
            if (cardType.equals("YES")) {
                viewHolder.merchantType_1.setVisibility(View.VISIBLE);
            }
            if (groupType.equals("YES")) {
                viewHolder.merchantType_2.setVisibility(View.VISIBLE);
            }

            //这里是异步操作 AsyncTask 取图片
            String pirURL = mList.get(position).getPicUrl();
            /**
             * Picasso方式轻松实现取图片 + 二级缓存处理
             */
//            Picasso.with(InternetListActivity.this).load(pirURL).into(viewHolder.merchantImg);
            /**
             *   自定义工具类，封装类似Picasso功能，LruCache
             */
            Bitmap bbMap = bitmapUtil.getBitmap(pirURL, new MyHttpBitmapUtil.HttpBitmapCallBackListener() {
                @Override
                public void onFinish(Bitmap bitmap) {
                    viewHolder.merchantImg.setImageBitmap(bitmap);
                }
            });
            if (bbMap != null) {
                viewHolder.merchantImg.setImageBitmap(bbMap);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView merchantImg;
            ImageView merchantType_1;
            ImageView merchantType_2;
            ImageView merchantType_3;
            TextView merchantName;
            TextView merchantCoupon;
            TextView merchantLocation;
            TextView merchantDistance;
        }
    }

    public void getJSONObj(String jsonURL) {
        new AsyncTask<String, Void, InternetList>() {
            @Override
            protected InternetList doInBackground(String... params) {
                InternetList internetList1 = null;
                StringBuilder builder;
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    builder = new StringBuilder();
                    InputStream is = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    String objStr = builder.toString();
                    internetList1 = JSON.parseObject(objStr, InternetList.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return internetList1;
            }

            @Override
            protected void onPostExecute(InternetList listOBJ) {
                super.onPostExecute(listOBJ);
                internetList = listOBJ;
                Message msg = Message.obtain();
                msg.obj = msg;
                msg.what = 1024;
                mHandler.sendMessage(msg);
            }
        }.execute(jsonURL);
    }

}
