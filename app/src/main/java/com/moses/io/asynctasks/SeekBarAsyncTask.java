package com.moses.io.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * Created by 丹 on 2014/12/30.
 */
public class SeekBarAsyncTask extends AsyncTask<String, Integer, String> {
    private static final String DONE_STR = "Done";
    private static final String LOADING_STR = "Loading";
    private Button button;
    private Context context;
    private SeekBar seekBar;
    int progress = 0;
    public SeekBarAsyncTask(Context context,SeekBar seekBar,Button button){
        this.context = context;
        this.seekBar = seekBar;
        this.button = button;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        button.setText(LOADING_STR);
        Toast.makeText(context, LOADING_STR, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        int count = 0;
        while (count < 100) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(count);
            count++;
        }
        return DONE_STR;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        button.setText(DONE_STR);
        Toast.makeText(context,DONE_STR,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progress = values[0];
        seekBar.setProgress(progress);
    }
}