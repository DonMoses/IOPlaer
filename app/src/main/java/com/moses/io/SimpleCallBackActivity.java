package com.moses.io;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by 丹 on 2015/1/6.
 */
public class SimpleCallBackActivity extends Activity {
    TextView mTextView;
    MyCallBackInterface myCallBackInterface;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_callback);
        initView();
        testCallBackThread();
    }

    public void initView() {
        mTextView = (TextView) findViewById(R.id.callback_text);
        myCallBackInterface = new MyCallBackInterface() {
            @Override
            public void onFinish(final int i) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText(String.valueOf(i));
                    }
                });
                Log.e("Tag", "i>>>>>>>>>>>>>>>>>>>>>>>" + i);
            }
        };
    }

    public void testCallBackThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 1024;
                while (i > 0) {
                    myCallBackInterface.onFinish(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i--;
                }
            }
        }).start();
    }

    public interface MyCallBackInterface {
        void onFinish(int i);
    }
}