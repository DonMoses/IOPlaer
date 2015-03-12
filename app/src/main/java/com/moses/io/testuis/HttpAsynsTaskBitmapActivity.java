package com.moses.io.testuis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.moses.io.R;
import com.moses.io.asynctasks.SimpleHttpAsyncTaskBitmap;

/**
 * Created by 丹 on 2014/12/30.
 */
public class HttpAsynsTaskBitmapActivity extends Activity implements View.OnClickListener{
    private static final String MZ_1 = "http://pic.dofay.com/2014/10/29p01.jpg";
    ImageView mImageViewA;
    Button mButtonA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_asynstask_bitmap);
        initViews();

    }
    public void initViews(){
        mImageViewA = (ImageView) findViewById(R.id.http_img_asynstask_imageview);
        mButtonA = (Button) findViewById(R.id.http_img_asynstask_button);
        mButtonA.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.http_img_asynstask_button:
                new SimpleHttpAsyncTaskBitmap(mImageViewA).execute(MZ_1);
                break;

            default:
                break;
        }
    }

}
