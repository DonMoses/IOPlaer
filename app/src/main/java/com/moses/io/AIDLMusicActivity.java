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
import android.widget.TextView;

import com.moses.aidl.IMyMusicAidl;

/**
 * Created by 丹 on 2014.12.23
 */
public class AIDLMusicActivity extends Activity implements View.OnClickListener {
    TextView mTextView;
    Button mTestAIDLBtn, mAIDLPlayBtn;
    String[] mMedias;
    IMyMusicAidl myAIDLInterface;
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myAIDLInterface = IMyMusicAidl.Stub.asInterface(service);
            Log.e("tag", "11111111111111");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myAIDLInterface = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_music);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mediaFile");
        mMedias = bundle.getStringArray("path");

        intiViews();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
        }
    }

    public void intiViews() {
        mTextView = (TextView) findViewById(R.id.info_text_view);
        mTestAIDLBtn = (Button) findViewById(R.id.aidl_test_button);
        mAIDLPlayBtn = (Button) findViewById(R.id.aidl_player_button);

        mTestAIDLBtn.setOnClickListener(this);
        mAIDLPlayBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aidl_test_button:
                Intent intent = new Intent();
                intent.setPackage("com.moses.aidl");
                intent.setAction("com.moses.aidl.service.mplayerservice");
                bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.aidl_player_button:
                try {
                    Log.e("tag", "playing...............");
                    Log.e("tag", "path......" + mMedias[0]);
                    myAIDLInterface.play("/storage/sdcard0/Download/Amor-yonderboi.mp3");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
