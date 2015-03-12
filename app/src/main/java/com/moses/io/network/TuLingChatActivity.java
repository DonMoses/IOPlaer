package com.moses.io.network;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.moses.io.R;
import com.moses.io.beams.tuling.TuLingInfo;
import com.moses.io.beams.tuling.TuLingJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 丹 on 2015/1/16.
 */
public class TuLingChatActivity extends Activity implements View.OnClickListener, AbsListView.OnScrollListener {
    String tuLingBaseURL = "http://www.tuling123.com/openapi/api";
    String key = "efa2c6f33c546dddec1fb917c8980a68";
    String info = "";
    String reqMethod = "GET";
    ListView mListView;
    EditText mEditText;
    Button mButton;
    View mDialogView;
    List<TuLingInfo> mInfoList = new ArrayList<>();
    HashMap<String, String> parameters = new HashMap<>();
    TuLingJson tuLingJson;
    HTTPUtil httpUtil = new HTTPUtil(tuLingBaseURL, parameters, reqMethod);
    MyInternetListAdapter adapter;
    InputMethodManager imm;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String infoIN = msg.obj.toString();
            mInfoList.add(getTuLingStr(infoIN, true));
            adapter.setList(mInfoList);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuling_chat);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //隐藏键盘
        initViews();
    }

    //布局
    public void initViews() {
        mListView = (ListView) findViewById(R.id.list_view_chat);
        mEditText = (EditText) findViewById(R.id.edit_text_chat);
        mDialogView = findViewById(R.id.dialog_layout);
        mButton = (Button) findViewById(R.id.button_send_info);
        //mButton 注册监听事件
        mButton.setOnClickListener(this);
        adapter = new MyInternetListAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setOnScrollListener(this);
    }

    //mListView滚动事件监听
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);     //隐藏软键盘
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    //发送按键事件监听
    @Override
    public void onClick(View v) {
        mListView.setSelection(mListView.getCount() - 1);   //显示到最后一行
        info = mEditText.getText().toString();       //每次点击，只要info不为空，则加入到数据源
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);     //隐藏软键盘

        //只要info不为空,根据key和info请求网络
        if (info != null) {
            String theInfo = info;

            //加入换行逻辑
            String reInfo = info.replaceAll(System.getProperty("line.separator"), "");
            //加入空格逻辑
            reInfo = reInfo.replaceAll(" ", "");
            mEditText.setText(""); //清楚文字

            if (!reInfo.equals("")) {
                mInfoList.add(getTuLingStr(theInfo, false));
                adapter.setList(mInfoList);     //更新数据源
                info = reInfo;
                parameters.put("info", info);
                parameters.put("key", key);
                reqForHttp();                   //请求网络
            } else {
                mInfoList.add(getTuLingStr("要输入内容才可以哦，亲...^_^.", true));
                adapter.setList(mInfoList);
            }
        }
    }

    /**
     * 网络请求
     */
    public void reqForHttp() {
        //调用帮助类HTTPUtil
        httpUtil.sendRequest(new HTTPUtil.HttpCallBackInterface() {
            @Override
            public void onFinish(String s) {
                //每次收到服务器返回数据（json）,解析为所需对象
                tuLingJson = JSON.parseObject(s, TuLingJson.class);
                String infoIN = doWithJSONObj(tuLingJson);
                if (infoIN != null) {
                    Message msg = Message.obtain();
                    msg.obj = infoIN;
                    mHandler.sendMessage(msg);
                    Log.e("TAG", "infoIN>>>>>>>>>>>>>>>" + infoIN);
                }
            }
        });
    }

    /**
     * 网络数据处理
     */
    public String doWithJSONObj(TuLingJson tuLingJson) {
//        int code = tuLingJson.getCode();
        String tText;
        tText = tuLingJson.getText();
        return tText;
    }


    /**
     * 数据源和适配器
     */
    class MyInternetListAdapter extends BaseAdapter {
        List<TuLingInfo> mList = new ArrayList<>();
        LayoutInflater inflater;

        public MyInternetListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setList(List<TuLingInfo> mList) {
            this.mList = mList;
            notifyDataSetChanged();
            mListView.setSelection(mListView.getCount() - 1);   //显示到最后一行
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
            TextView chatText;
            TuLingInfo tuLingInfo = mList.get(position);
            boolean isInfoIn = tuLingInfo.getIsInfoIn();
            Log.e("TAG", "isInfoIn>>>>>>>>>>>>>>>" + isInfoIn);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.chat_layout,null);
                chatText = (TextView) convertView.findViewById(R.id.text_view_chat);
                changeVisibleState(
                        convertView.findViewById(R.id.left_chat_icon),
                        convertView.findViewById(R.id.right_chat_icon),
                        isInfoIn,
                        chatText,
                        R.drawable.chat_info_bg_left,
                        R.drawable.chat_info_bg_right);
                convertView.setTag(chatText);
            } else {
                chatText = (TextView) convertView.getTag();
                changeVisibleState(
                        convertView.findViewById(R.id.left_chat_icon),
                        convertView.findViewById(R.id.right_chat_icon),
                        isInfoIn,
                        chatText,
                        R.drawable.chat_info_bg_left,
                        R.drawable.chat_info_bg_right);
            }
            chatText.setText(tuLingInfo.getInfoStr());
            return convertView;
        }
    }

    public TuLingInfo getTuLingStr(String infoStr, boolean isInfoIN) {
        TuLingInfo tuLing = new TuLingInfo();
        tuLing.setInfoStr(infoStr);
        tuLing.setIsInfoIn(isInfoIN);
        return tuLing;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void changeVisibleState(View leftView, View rightView, boolean isLeft, TextView chatText, int bgLeftID, int bgRightID) {
        if (isLeft) {
            leftView.setVisibility(View.VISIBLE);
            rightView.setVisibility(View.INVISIBLE);
            chatText.setBackgroundResource(bgLeftID);
        } else {
            leftView.setVisibility(View.INVISIBLE);
            rightView.setVisibility(View.VISIBLE);
            chatText.setBackgroundResource(bgRightID);
        }
    }
}
