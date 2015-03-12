package com.moses.io.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.moses.io.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 丹 on 2014/12/31.
 */
public class GridViewBitmapAsyncTaskHandlerAdapter extends BaseAdapter {
    List<Bitmap> mList = new ArrayList<>();
    LayoutInflater inflater;

    public GridViewBitmapAsyncTaskHandlerAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Bitmap> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.view_grid_view_http_bitmap,null);
            imageView = (ImageView) convertView.findViewById(R.id.image_view_for_grid_view_http_bitmap);
            convertView.setTag(imageView);
        }else {
            imageView = (ImageView) convertView.getTag();
        }

        imageView.setImageBitmap(mList.get(position));
        return convertView;

    }




}
