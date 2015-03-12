package com.moses.io.network;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moses.io.R;
import com.moses.io.beams.inernetlist.InternetMerchant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 丹 on 2015/1/15.
 */
public class PagerListActivity extends Activity {
    ListView mListView;
    ProgressBar mProBar;
    HashMap<String, String> parameters = new HashMap<>();
    String httpURL = "http://192.168.42.10:8080/MyROOT/test";
    String requestMethod = "GET";
    int pageCount = 0;
    int itemCount = 0;
    MyInternetListAdapter adapter;
    HTTPUtil httpUtil = new HTTPUtil(httpURL, parameters, requestMethod);
    MyHttpBitmapUtil bitmapUtil = new MyHttpBitmapUtil();
    HTTPUtil.HttpCallBackInterface httpCallBackInterface = new HTTPUtil.HttpCallBackInterface() {
        @Override
        public void onFinish(String s) {
        
        }
    };
    MyHttpBitmapUtil.HttpBitmapCallBackListener bitmapCallBackListener = new MyHttpBitmapUtil.HttpBitmapCallBackListener() {
        @Override
        public void onFinish(Bitmap bitmap) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_list);
        initViews();
    }

    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };

    public void initViews() {
        mListView = (ListView) findViewById(R.id.internet_list_view);
        mProBar = (ProgressBar) findViewById(R.id.internet_progressbar);
        adapter = new MyInternetListAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(mProBar);
        mListView.setOnScrollListener(onScrollListener);
        parameters.put("pageCount", String.valueOf(pageCount));
        parameters.put("itemCount", String.valueOf(itemCount));


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
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
                viewHolder = new ViewHolder();
                viewHolder.merchantName = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            String pirURL = mList.get(position).getPicUrl();

            return convertView;
        }

        class ViewHolder {
            TextView merchantName;
        }
    }
}
