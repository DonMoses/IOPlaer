package com.moses.io;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.guagua.moses.servicetest.IMyAidlInterface;

/**
 * Created by Moses on 2015
 */
public class TestAIDLActivity extends Activity {
    private IMyAidlInterface aidlInterface;
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aidlInterface = IMyAidlInterface.Stub.asInterface(service);
            Log.e("TAG", "onServiceConnected>>>>>>>>>>>>");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    while (i < 101) {
                        try {
                            aidlInterface.download(i);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(80);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i++;
                    }
                }
            }).start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_aidl);

        Button button = (Button) findViewById(R.id.test_aidl_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.guagua.moses.servicetest.services.MyService");
                intent.setPackage("com.guagua.moses.servicetest");
                bindService(intent, connection, Context.BIND_AUTO_CREATE);

            }
        });
    }

}
