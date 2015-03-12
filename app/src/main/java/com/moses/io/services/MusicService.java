package com.moses.io.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.io.IOException;

/**
 * Created by 丹 on 2014/12/22
 */
public class MusicService extends Service {
    MediaPlayer mMediaPlayer = new MediaPlayer();
    MusicBinder mMusicBinder;
    Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicBinder = new MusicBinder();

        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
//                mHandler.sendEmptyMessage(PLAY_DONE);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {


                        //------------------handler ----刷新界面
                        Message msg = Message.obtain();
                        msg.arg2 = mMediaPlayer.getDuration();
                        msg.arg1 = mMediaPlayer.getCurrentPosition();
                        mHandler.sendMessageDelayed(msg, 1000);

                        //------------------广播  ------刷新界面
//                        Intent intent = new Intent("com.moses.io.services.musicplayerbroadcast");
//                        intent.putExtra("position", mMediaPlayer.getDuration());
//                        intent.putExtra("duration", mMediaPlayer.getCurrentPosition());
//                        MusicService.this.sendBroadcast(intent);
//
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }

                }
            }
        }).start();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMusicBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    //interface
    public interface PlayControl {
        public void musicHandler(Handler handler);

        public void play(String path);

        public void pause();

        public void seekPlay(int position);

    }

    //a class extends Binder and implements the interface up
    public class MusicBinder extends Binder implements PlayControl {
        public void musicHandler(Handler handler) {
            mHandler = handler;
        }

        public void play(String path) {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
                try {

                    mMediaPlayer.setDataSource(path);
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMediaPlayer.start();
            } else if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            } else {
                mMediaPlayer.reset();
                try {
                    mMediaPlayer.setDataSource(path);
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMediaPlayer.start();
            }

        }

        public void pause() {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }

        public void seekPlay(int position) {
            mMediaPlayer.seekTo(position);
            mMediaPlayer.start();
        }


    }

}
