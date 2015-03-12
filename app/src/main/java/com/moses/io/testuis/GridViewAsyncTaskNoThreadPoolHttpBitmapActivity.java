package com.moses.io.testuis;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.moses.io.R;
import com.moses.io.adapters.GridViewBitmapAsyncTaskHandlerAdapter;
import com.moses.io.asynctasks.WithInterfaceAsyncTaskHttpBitmap;
import com.moses.io.theinterfaces.GetBitmapByAsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 丹 on 2014/12/31.
 */
public class GridViewAsyncTaskNoThreadPoolHttpBitmapActivity extends Activity implements View.OnClickListener {
    Button getHttpBitmapBtn, cleanHttpBitmapBtn;
    GridView mGridView;
    List<Bitmap> mData = new ArrayList<>();
    GridViewBitmapAsyncTaskHandlerAdapter adapter;
    GetBitmapByAsyncTask[] getBitmapByAsyncTasks;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String i = (String) msg.obj;
            int index = Integer.parseInt(i);
            if (msg.what == 1) {
                mData.add(getBitmapByAsyncTasks[index].getBitmap());
                adapter.setData(mData);
            }
        }
    };

    String[] urls = {
            "http://pic.dofay.com/2014/10/29p01.jpg",
            "http://pic.dofay.com/2014/10/29p02.jpg",
            "http://pic.dofay.com/2014/10/29p03.jpg",
            "http://pic.dofay.com/2014/10/29p04.jpg",
            "http://pic.dofay.com/2014/10/29p05.jpg",
            "http://pic.dofay.com/2014/10/29p06.jpg",
            "http://pic.dofay.com/2014/10/29p07.jpg",
            "http://pic.dofay.com/2014/10/29p08.jpg",
            "http://pic.dofay.com/2014/10/29p09.jpg",
            "http://pic.dofay.com/2014/10/29p10.jpg",
            "http://pic.dofay.com/2014/10/29p11.jpg",
            "http://pic.dofay.com/2014/10/29p12.jpg",

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_http_bitmap);
        initViews();

    }

    public void initViews() {
        getHttpBitmapBtn = (Button) findViewById(R.id.getHttpButton);
        cleanHttpBitmapBtn = (Button) findViewById(R.id.cleanHttpButton);
        getHttpBitmapBtn.setOnClickListener(this);
        cleanHttpBitmapBtn.setOnClickListener(this);
        mGridView = (GridView) findViewById(R.id.grid_view_http_bitmap);
        adapter = new GridViewBitmapAsyncTaskHandlerAdapter(this);
        mGridView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getHttpButton:
                getBitmapByAsyncTasks = new GetBitmapByAsyncTask[urls.length];
                for (int i = 0; i < urls.length; i++) {
                    getBitmapByAsyncTasks[i] = new WithInterfaceAsyncTaskHttpBitmap(mHandler);
                    ((WithInterfaceAsyncTaskHttpBitmap) getBitmapByAsyncTasks[i]).execute(urls[i], String.valueOf(i));
                }
                break;
            case R.id.cleanHttpButton:
                mData.clear();
                adapter.setData(mData);
                break;

            default:
                break;
        }
    }
}
