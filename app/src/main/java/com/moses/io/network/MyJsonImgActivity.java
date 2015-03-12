package com.moses.io.network;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.moses.io.R;
import com.moses.io.beams.Images;
import com.moses.io.beams.ImgAlbum;
import com.moses.io.beams.ImgURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 丹 on 2015/1/12.
 */
public class MyJsonImgActivity extends Activity {
    GridView mGridView;
    Button mButton;
    ProgressBar mProgressBar;
    List<ImgURL> mImgList = new ArrayList<>();
    List<String> mNameList = new ArrayList<>();
    MyAdapter adapter;

    String httpURL = "http://192.168.42.169:8080/MyROOT/test";
    HashMap<String, String> parameters = new HashMap<>();
    String reqMethod = "POST";
    HTTPUtil httpUtil = new HTTPUtil(httpURL,parameters,reqMethod);
    Executor executor = new ThreadPoolExecutor(5, 20, 10L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

    HTTPUtil.HttpCallBackInterface callBackInterface =
            new HTTPUtil.HttpCallBackInterface() {
                @Override
                public void onFinish(String s) {
                    Log.e("TAG", "s>>>>>>>>>>>>" + s);
                    Message msg = Message.obtain();
                    msg.obj = s;
                    mHandler.sendMessage(msg);
                }
            };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mGridView.setAdapter(adapter);
            String jsonStr = msg.obj.toString();
            Images images = JSON.parseObject(jsonStr, Images.class);
            List<ImgAlbum> albums = images.getInfo().getPicSet();
            Log.e("TAG", "resultCode>>>>>>>>>>>>"+images.getResultCode() + "resultInfo>>>>>>>>>>>>"+images.getResultInfo() );
            Log.e("TAG", "test information>>>>>>>>>>>>"+images.getInfo().getPicSet().size() );

            for (ImgAlbum album : albums) {
                Log.e("TAG", "album>>>>>>>>>>>>" + album.getAlbumsName());
                List<ImgURL> imgURLs = album.getPicUrlSet();
                for (ImgURL imgUrl : imgURLs) {
                    Log.e("TAG", "imgUrl>>>>>>>>>>>>" + imgUrl.getPicUrl());
                    mImgList.add(imgUrl);
                    mNameList.add(album.getAlbumsName());
                    adapter.setData(mImgList,mNameList);
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_img);
        initView();
    }

    public void initView() {
        mGridView = (GridView) findViewById(R.id.grid_view_json_img);
        mButton = (Button) findViewById(R.id.json_img_button);
        mProgressBar = (ProgressBar) findViewById(R.id.img_pro_bar);
        adapter = new MyAdapter(this);

    }

    public void getImgWithJSON(View v) throws JSONException {
        mButton.setVisibility(View.INVISIBLE);
        mGridView.setEmptyView(mProgressBar);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account", "moses");
        jsonObject.put("password", "0716");
        parameters.put("json", jsonObject.toString());
        Log.e("TAG", "jsonObject>>>>>>>>>>>>" + jsonObject.toString());
        httpUtil.sendRequest(callBackInterface);

    }

    class MyAdapter extends BaseAdapter {
        List<ImgURL> data = new ArrayList<>();
        List<String> nData = new ArrayList<>();
        LayoutInflater inflater;

        public MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<ImgURL> data,List<String> nData) {
            this.data = data;
            this.nData = nData;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.view_json_img_grid_view, null);
                viewHolder = new ViewHolder();
                viewHolder.imgView = (ImageView) convertView.findViewById(R.id.img_view_content);
                viewHolder.imgAlbum = (TextView) convertView.findViewById(R.id.text_view_albums);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            new AsyncTask<String, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(String... params) {
                    Bitmap bitmap = null;
                    try {
                        URL url = new URL(params[0]);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        InputStream is = connection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);

                    viewHolder.imgView.setImageBitmap(bitmap);

                }
            }.executeOnExecutor(executor, data.get(position).getPicUrl());
            viewHolder.imgAlbum.setText(nData.get(position));
            return convertView;
        }

        class ViewHolder {
            ImageView imgView;
            TextView imgAlbum;
        }
    }

}
