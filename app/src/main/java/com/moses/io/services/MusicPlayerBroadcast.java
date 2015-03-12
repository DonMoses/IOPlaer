package com.moses.io.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.moses.io.MainActivity;

/**
 * Created by 丹 on 2014/12/24.
 */
public class MusicPlayerBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.e("Tag", "...................out.....");
        //调用系统广播， 需要在manifest注册相应权限 user-permission， 这里需要注册android.permission.RECEIVE_BOOT_COMPLETED
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//            Log.e("Tag", "...............in.........");
//            Intent iintent = new Intent(context, MainActivity.class);
            //从外部启动，需要flag：  Intent.FLAG_ACTIVITY_NEW_TASK
            //这里在开机时，从系统启动到指定活动，所以需要为intent 设置flag。
//            iintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(iintent);
            Toast.makeText(context," 瓜 ^_^ 老婆 ^_^ ",Toast.LENGTH_SHORT).show();

        }
    }
}

