package com.moses.io.testuis;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.moses.io.R;
import com.moses.io.adapters.GridViewBitmapAsyncTaskHandlerAdapter;
import com.moses.io.asynctasks.ThreadHttpBitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 丹 on 2014/12/31.
 */
public class GridViewThreadHttpBitmapActivity extends Activity implements View.OnClickListener {
    Button getHttpBitmapBtn, cleanHttpBitmapBtn;
    GridView mGridView;
    List<Bitmap> mData = new ArrayList<>();
    GridViewBitmapAsyncTaskHandlerAdapter adapter;
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


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
            mData.add(bitmap);
            adapter.setData(mData);
            Log.v("TAG", "////////////bitmap/////////////////" + bitmap);
        }
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
                for (String url : urls) {
                    new ThreadHttpBitmap(url, mHandler).start();
                    Log.v("TAG", "//////////////////////////////////////");
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
