package com.moses.io.testuis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.moses.io.R;
import com.moses.io.adapters.GridViewBitmapAsyncTaskAdapter;

/**
 * Created by 丹 on 2014/12/31.
 */
public class GridViewAsyncTaskHttpBitmapActivity extends Activity implements View.OnClickListener {
    Button getHttpBitmapBtn, cleanHttpBitmapBtn;
    GridView mGridView;
    GridViewBitmapAsyncTaskAdapter adapter;

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
        adapter = new GridViewBitmapAsyncTaskAdapter(this,urls);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getHttpButton:
                mGridView.setAdapter(adapter);   //
                adapter.setData();
                break;
            case R.id.cleanHttpButton:
                mGridView.setAdapter(null);      //因为给适配器直接传入了数据(图片地址)，所以这里使用setAdapter(null)清空数据
                break;
            default:
                break;
        }
    }


}
