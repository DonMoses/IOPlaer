package com.moses.io.testuis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.moses.io.R;
import com.moses.io.asynctasks.SeekBarAsyncTask;

/**
 * Created by 丹 on 2014/12/30.
 */
public class MyAsyncTaskActivity extends Activity implements View.OnClickListener {
    Button mButton;
    SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_asynctast);
        initViews();
    }

    public void initViews() {
        mButton = (Button) findViewById(R.id.async_task_button);
        mSeekBar = (SeekBar) findViewById(R.id.async_task_seekBar);
        mButton.setOnClickListener(this);
        mSeekBar.setMax(100);
    }

    @Override
    public void onClick(View v) {
        //there's only mButton, so it's no need to use switch(){};
        new SeekBarAsyncTask(MyAsyncTaskActivity.this,mSeekBar,mButton).execute("");
    }


}
