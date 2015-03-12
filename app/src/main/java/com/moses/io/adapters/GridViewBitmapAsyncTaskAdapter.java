package com.moses.io.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.moses.io.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by 丹 on 2014/12/31.
 */
public class GridViewBitmapAsyncTaskAdapter extends BaseAdapter {
    String[] mList = null;
    LayoutInflater inflater;

    public GridViewBitmapAsyncTaskAdapter(Context context, String[] mList){
        this.mList = mList;
        inflater = LayoutInflater.from(context);
    }

    public void setData(){
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.length;
    }

    @Override
    public Object getItem(int position) {
        return mList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.view_grid_view_http_bitmap,null);
            imageView = (ImageView) convertView.findViewById(R.id.image_view_for_grid_view_http_bitmap);
            convertView.setTag(imageView);
        }else {
            imageView = (ImageView) convertView.getTag();
        }
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bitmap = null;
                InputStream is = null;
                URL url;
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
                imageView.setImageBitmap(bitmap);
            }
        }.execute(mList[position]);
        return convertView;

    }

}
