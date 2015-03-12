package com.moses.io.services;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.moses.io.R;

/**
 * Created by 丹 on 2014.12.22
 */
public class MusicUI extends Activity implements View.OnClickListener {

    String[] mMedias;
    String path;
    ListView mListView;
    MediaListAdapter mAdapter;
    SeekBar mSeekBar;
    TextView mCurrentTime;
    TextView mTotalTime;
    Button mPlayBtn, mPrevBtn, mNextBtn;
    int index;

    MusicService.PlayControl mPlayControl;
    //------------------------------broadcast-----update UI----------
//    BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals("com.moses.io.services.musicplayerbroadcast")) {
//                int pos = intent.getIntExtra("duration", 0); //总时间
//                int maxPos = intent.getIntExtra("position", 0);// 当前时间
//                mSeekBar.setMax(maxPos);
//                mSeekBar.setProgress(pos);
//
//                int min = (pos / 1000) / 60;
//                int sec = (pos / 1000) % 60;
//
//                int maxMin = (maxPos / 1000) / 60;
//                int maxSec = (maxPos / 1000) % 60;
//
//                if (maxSec < 10) {
//                    mTotalTime.setText("" + maxMin + ":0" + maxSec);
//                } else {
//                    mTotalTime.setText("" + maxMin + ":" + maxSec);
//                }
//
//                if (sec < 10) {
//                    mCurrentTime.setText("" + min + ":0" + sec);
//                } else {
//                    mCurrentTime.setText("" + min + ":" + sec);
//                }
//            }
//        }
//    };
    //------------------------------handler-----update UI----------
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos = msg.arg1; //mMediaPlayer.getCurrentPosition()当前时间
            int maxPos = msg.arg2;//mMediaPlayer.getDuration(); 总时间

            mSeekBar.setMax(maxPos);
            mSeekBar.setProgress(pos);

            int min = (pos / 1000) / 60;
            int sec = (pos / 1000) % 60;

            int maxMin = (maxPos / 1000) / 60;
            int maxSec = (maxPos / 1000) % 60;

            if (maxSec < 10) {
                mTotalTime.setText("" + maxMin + ":0"+ maxSec);
            } else {
                mTotalTime.setText("" + maxMin + ":"+ maxSec);
            }

            if (sec < 10) {
                mCurrentTime.setText("" + min + ":0" + sec);
            } else {
                mCurrentTime.setText("" + min + ":" + sec);
            }


        }
    };

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayControl = (MusicService.PlayControl) service;
            mPlayControl.musicHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayControl = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music_ui);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mediaFile");
        mMedias = bundle.getStringArray("path");

        mListView = (ListView) findViewById(R.id.ui_list_view);
        mAdapter = new MediaListAdapter(this);
        mListView.setAdapter(mAdapter);
        mAdapter.setList(mMedias);

        mSeekBar = (SeekBar) findViewById(R.id.ui_seek_bar);
        mCurrentTime = (TextView) findViewById(R.id.ui_current_time);
        mTotalTime = (TextView) findViewById(R.id.ui_total_time);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int position;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                position = progress;
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mPlayControl.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayControl.seekPlay(position);
            }
        });

        Intent intent1 = new Intent(MusicUI.this, MusicService.class);
        startService(intent1);
        bindService(intent1, mServiceConnection, BIND_AUTO_CREATE);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                path = mMedias[position];
                mPlayControl.play(path);
            }
        });

        mPlayBtn = (Button) findViewById(R.id.music_play_btn);
        mPrevBtn = (Button) findViewById(R.id.music_prev_btn);
        mNextBtn = (Button) findViewById(R.id.music_next_btn);

        mPlayBtn.setOnClickListener(this);
        mPrevBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        IntentFilter filter = new IntentFilter("com.moses.io.services.musicplayerbroadcast");
//        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    class MediaListAdapter extends BaseAdapter {
        String[] list = new String[mMedias.length];
        LayoutInflater mInflater;
        Context context;

        public MediaListAdapter(Context context) {
            this.context = context;
            mInflater = LayoutInflater.from(context);
        }

        public void setList(String[] list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public Object getItem(int position) {
            return list[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.view_file_list, null);
                viewHolder = new ViewHolder();
                viewHolder.img = (ImageView) convertView.findViewById(R.id.fileIcon);
                viewHolder.name = (TextView) convertView.findViewById(R.id.fileName);
                viewHolder.duration = (TextView) convertView.findViewById(R.id.fileCount);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.img.setImageResource(R.drawable.icon_music);
            viewHolder.name.setText(mMedias[position]);
            /**
             * 这里暂且没有设置路径和音乐文件时长
             */
            return convertView;
        }

        class ViewHolder {
            ImageView img;
            TextView name;
            TextView duration;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.music_play_btn:
                mPlayControl.play(path);
                break;
            case R.id.music_prev_btn:
                indexUp();
                mPlayControl.play(mMedias[index]);
                break;
            case R.id.music_next_btn:
                indexDown();
                mPlayControl.play(mMedias[index]);
                break;
            default:
                break;
        }
    }

    public void indexUp() {
        if (index < mMedias.length - 1) {
            index = index + 1;
        } else index = 0;

    }

    public void indexDown() {
        if (index > 0) {
            index = index - 1;
        } else index = mMedias.length - 1;
    }

}
