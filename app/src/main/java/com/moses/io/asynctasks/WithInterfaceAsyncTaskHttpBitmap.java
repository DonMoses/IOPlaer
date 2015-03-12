package com.moses.io.asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.moses.io.theinterfaces.GetBitmapByAsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by 丹 on 2014/12/31.
 */
public class WithInterfaceAsyncTaskHttpBitmap extends AsyncTask<String, Void, Bitmap> implements GetBitmapByAsyncTask {
    private static final int ASYNC_TASK_DONE = 1;
    Bitmap bitmap  = null;
    Handler mHandler;
    String number = "";
    public WithInterfaceAsyncTaskHttpBitmap(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public Bitmap getBitmap() {
        Log.v("TAG", "////////////////getBitmap//////////////////////" + bitmap);
        return bitmap;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        number = params[1];

        InputStream is = null;
        URL url;
        Bitmap bitmap = null;
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
        this.bitmap = bitmap;
        Message msg = Message.obtain();
        msg.obj = number;
        msg.what = ASYNC_TASK_DONE;
        mHandler.sendMessage(msg);
        Log.v("TAG", "////////////////bitmap//////////////////////" + bitmap);
    }

}
