package com.moses.io.asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 丹 on 2014/12/31.
 */
public class ThreadHttpBitmap extends Thread {
    Handler handler;
    String httpURL;
    public ThreadHttpBitmap(String httpURL,Handler handler){
        this.handler = handler;
        this.httpURL = httpURL;
    }

    @Override
    public void run() {
        super.run();
        InputStream is = null;
        try {
            URL url = new URL(httpURL);
            try {
                is = url.openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(is);
        Message msg = Message.obtain();
        msg.obj = bitmap;
        handler.sendMessage(msg);
        Log.v("TAG", "///////////running Thread////////////");
    }
}
