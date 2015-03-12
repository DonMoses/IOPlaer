package com.moses.io.network;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 丹 on 2015/1/14.
 */
public class CopyInternetListActivity extends Activity {
    ListView mListView;
    String httpURL = "http://192.168.42.10:8080/MyROOT/test";
    HashMap<String, String> parameters = new HashMap<>();
    String requestMethod = "POST";
    List<InternetMerchant> mMerchants = new ArrayList<>();
    MyInternetListAdapter myInternetListAdapter;
    HTTPUtil httpUtil = new HTTPUtil(httpURL,parameters,requestMethod);
    HTTPUtil.HttpCallBackInterface httpCallBackInterface = new HTTPUtil.HttpCallBackInterface() {
        @Override
        public void onFinish(String s) {
            Message msg = Message.obtain();
            msg.obj = s;
            mHandler.sendMessage(msg);
        }
    };
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage( Message msg) {
            super.handleMessage(msg);
            String internetJsonStr = msg.obj.toString();
            Log.e("TAG", "internetJsonStr>>>>>>>>>>>>>>>" + internetJsonStr);
            InternetList internetList = JSON.parseObject(internetJsonStr, InternetList.class);
            mMerchants = internetList.getInfo().getMerchantKey();
            myInternetListAdapter.setList(mMerchants);
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
            viewHolder.merchantLocation.setText(new String(mList.get(position).getLocation().getBytes(), 18, mList.get(position).getLocation().length()));
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
            Executor executor = new ThreadPoolExecutor(15, 100, 10L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
            new AsyncTask<String, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... params) {
                    Bitmap bitmap = null;
                    try {
                        URL url = new URL(params[0]);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        InputStream is = connection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    viewHolder.merchantImg.setImageBitmap(bitmap);
                }
            }.executeOnExecutor(executor, pirURL);
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

}
