package com.moses.io.asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 丹 on 2014/12/30.
 */
public class SimpleHttpAsyncTaskBitmap extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    Bitmap bitmap;

    public SimpleHttpAsyncTaskBitmap(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
        InputStream is = null;
        HttpURLConnection connection = null;
        URL url;
        try {
            url = new URL(params[0]);
//            connection = (HttpURLConnection) url.openConnection();
//            is = connection.getInputStream();
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
//            if (connection != null) {
//                connection.disconnect();
//            }
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
    }

}
