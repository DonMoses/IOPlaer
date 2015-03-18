package com.moses.io.gate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.moses.io.MainActivity;
import com.moses.io.R;
import com.moses.io.SimpleCallBackActivity;
import com.moses.io.TestAIDLActivity;
import com.moses.io.beams.MyActivityIntents;
import com.moses.io.network.IWebActivity;
import com.moses.io.network.InternetListActivity;
import com.moses.io.network.MyJsonActivity;
import com.moses.io.network.MyJsonImgActivity;
import com.moses.io.network.MyWebActivity;
import com.moses.io.network.SimpleWebViewActivity;
import com.moses.io.network.TuLingChatActivity;
import com.moses.io.testlocation.LocationActivity;
import com.moses.io.testmedia.PhotoActivity;
import com.moses.io.testuis.GridViewAsyncTaskNoThreadPoolHttpBitmapActivity;
import com.moses.io.testuis.GridViewAsyncTaskHttpBitmapActivity;
import com.moses.io.testuis.GridViewAsyncTaskThreadPoolHttpBitmapActivity;
import com.moses.io.testuis.GridViewThreadHttpBitmapActivity;
import com.moses.io.testuis.HttpAsynsTaskBitmapActivity;
import com.moses.io.testuis.MyAsyncTaskActivity;

import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexFile;

/**
 * Created by 丹 on 2014.12.30
 */
public class MyActivity extends Activity {
    ListView mListView;
    List<MyActivityIntents> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_for_intents);
        mListView = (ListView) findViewById(R.id.all_for_list_view);
        ActivityAdapter adapter = new ActivityAdapter(this);
        mData = getData();
        mListView.setAdapter(adapter);
        adapter.setData(mData);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mData.get(position).intentStart(MyActivity.this);
            }
        });

    }


    class ActivityAdapter extends BaseAdapter {
        List<MyActivityIntents> mList = new ArrayList<>();
        LayoutInflater inflater;

        public ActivityAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<MyActivityIntents> mList) {
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
            TextView textView;
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
                textView = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(textView);
            } else {
                textView = (TextView) convertView.getTag();
            }
            textView.setText(mList.get(position).getIntentName());
            return convertView;
        }
    }

    public List<MyActivityIntents> getData() {
        mData.add(new MyActivityIntents(MainActivity.class, "Pager播放器"));
        mData.add(new MyActivityIntents(MyAsyncTaskActivity.class, "AsyncTask异步任务"));
        mData.add(new MyActivityIntents(HttpAsynsTaskBitmapActivity.class, "AsyncTask异步任务网络取图片"));
        mData.add(new MyActivityIntents(GridViewThreadHttpBitmapActivity.class, "Tread+Handler方式GridView网络取图片"));
        mData.add(new MyActivityIntents(GridViewAsyncTaskNoThreadPoolHttpBitmapActivity.class, "AsyncTask(单线程模式)+Handler方式GridView网络取图片"));
        mData.add(new MyActivityIntents(GridViewAsyncTaskHttpBitmapActivity.class, "AsyncTask(单线程模式)方式GridView网络取图片"));
        mData.add(new MyActivityIntents(GridViewAsyncTaskThreadPoolHttpBitmapActivity.class, "AsyncTask(线程池模式)方式GridView网络取图片"));
        mData.add(new MyActivityIntents(SimpleWebViewActivity.class, "WebView入门"));
        mData.add(new MyActivityIntents(SimpleCallBackActivity.class, "简单回调机制实现UI更新"));
        mData.add(new MyActivityIntents(MyWebActivity.class, "简单Servlet实现http访问"));
        mData.add(new MyActivityIntents(IWebActivity.class, "简单封装http|通过线程"));
        mData.add(new MyActivityIntents(MyJsonActivity.class, "简单封装http|JSON字符串传递和解析"));
        mData.add(new MyActivityIntents(MyJsonImgActivity.class, "简单封装http|JSON图像传递（基于字符串）和解析"));
        mData.add(new MyActivityIntents(InternetListActivity.class, "简单封装http|internet显示ListView列表"));
        mData.add(new MyActivityIntents(TuLingChatActivity.class, "http|JSON|图灵智能应答"));
        mData.add(new MyActivityIntents(PhotoActivity.class, "多媒体之拍照"));
        mData.add(new MyActivityIntents(LocationActivity.class, "地理位置信息"));
        mData.add(new MyActivityIntents(TestAIDLActivity.class, "AIDL测试"));

        return mData;
    }


}
