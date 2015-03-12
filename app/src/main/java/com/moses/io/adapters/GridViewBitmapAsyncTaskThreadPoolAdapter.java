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
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 丹 on 2014/12/31.
 */
public class GridViewBitmapAsyncTaskThreadPoolAdapter extends BaseAdapter {
    String[] mList = null;
    LayoutInflater inflater;
    /**
     * 这里是获得一个封装好了的第三方的线程池
     */
    Executor executor = new ThreadPoolExecutor(10, 100, 5L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

    /**
     * @param context ： 获得布局管理器需要的应用上下文
     * @param mList   ： 传入（设置）数据源。 这里可以理解因为girdview网络取图的特殊性，与平常（在适配器外部设置数据源）有所区别。
     */
    public GridViewBitmapAsyncTaskThreadPoolAdapter(Context context, String[] mList) {
        this.mList = mList;
        inflater = LayoutInflater.from(context);
    }

    public void setData() {
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_grid_view_http_bitmap, null);
            imageView = (ImageView) convertView.findViewById(R.id.image_view_for_grid_view_http_bitmap);
            convertView.setTag(imageView);
        } else {
            imageView = (ImageView) convertView.getTag();
        }
        /**
         * 开启异步任务
         *          *** 已线程池方式开启异步任务
         *      ① url = new URL(params[position]);   通过构造函数，数据源已经构造好了，通过position可以指定数据源中对应的数据。
         *                  每构造一个url 就会开启一个线程，然后将线程放入线程池中。
         *      ② BaseAdapter 的特点之一是 getView()方法会根据数据的数量重复执行，所以当上一个线程开启后会有下面这样一个过程：
         *                  url = new URL(params[position+1]); 新的线程被继续添加到线程池中。
         *       如此，直到将与数据源数据量相同数目的线程全部加入线程池中。
         *      ③ 因为在线程池中做任务，所以图片显现的顺序不一定，原则是： 谁先加载好，谁先显现。
         */
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bitmap = null;
                InputStream is = null;
                URL url;
                try {
                    url = new URL(params[position]);     //根据数据源 position 获得数据
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

            /**
             *
             * @param bitmap ： 后台线程获得的Bitmap对象
             *               该对象对应position出现。  因为线程完成的顺序不一定，
             *               所以这个对象获得的顺序不一定。
             *         ***【但是其对应的position是一定的，所以图片出现的位置应该不变，只是出现的时间顺序不同】
             */
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                imageView.setImageBitmap(bitmap);
            }
        }.executeOnExecutor(executor, mList);     //  这里传入参数（  ：我们的数据源）
        return convertView;

    }

}
