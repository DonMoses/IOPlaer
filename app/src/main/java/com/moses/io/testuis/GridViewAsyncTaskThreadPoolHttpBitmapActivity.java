package com.moses.io.testuis;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.moses.io.R;
import com.moses.io.adapters.GridViewBitmapAsyncTaskAdapter;
import com.moses.io.adapters.GridViewBitmapAsyncTaskThreadPoolAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by 丹 on 2014/12/31.
 */
public class GridViewAsyncTaskThreadPoolHttpBitmapActivity extends Activity implements View.OnClickListener {
    Button getHttpBitmapBtn, cleanHttpBitmapBtn;
    GridView mGridView;
    ImageView mImageView;
    View theGroupView;
    GridViewBitmapAsyncTaskThreadPoolAdapter adapter;

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
        theGroupView = findViewById(R.id.view_group_grid_view);
        getHttpBitmapBtn = (Button) findViewById(R.id.getHttpButton);
        cleanHttpBitmapBtn = (Button) findViewById(R.id.cleanHttpButton);
        mImageView = (ImageView) findViewById(R.id.large_image_view);
        getHttpBitmapBtn.setOnClickListener(this);
        cleanHttpBitmapBtn.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        mGridView = (GridView) findViewById(R.id.grid_view_http_bitmap);
        /**
         * 点击显示大图
         */
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                theGroupView.setVisibility(View.INVISIBLE);
                new AsyncTask<String, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(String... params) {
                        Bitmap bitmap = null;
                        InputStream is = null;
                        URL url;
                        try {
                            url = new URL(params[0]);
                            is = url.openStream();
                            bitmap = BitmapFactory.decodeStream(is);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (is != null) {
                                try {
                                    is.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return bitmap;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute(bitmap);
                        mImageView.setImageBitmap(bitmap);
                    }
                }.execute(urls[position]);
                mImageView.setVisibility(View.VISIBLE);
            }
        });
        adapter = new GridViewBitmapAsyncTaskThreadPoolAdapter(this, urls);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getHttpButton:
                mGridView.setAdapter(adapter);
                adapter.setData();     //
                break;
            case R.id.cleanHttpButton:
                mGridView.setAdapter(null);      //因为给适配器直接传入了数据(图片地址)，所以这里使用setAdapter(null)清空数据
                mImageView.setImageBitmap(null);
                break;
            case R.id.large_image_view:
                theGroupView.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

}
