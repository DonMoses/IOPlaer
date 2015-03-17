package com.moses.io;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Moses on 2015
 */
public class TestAIDLActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_aidl);

        Button button = (Button) findViewById(R.id.test_aidl_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindAIDLService();
            }
        });

    }

    protected void bindAIDLService() {


    }
}
