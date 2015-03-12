package com.moses.io.beams;

import android.content.Context;
import android.content.Intent;

/**
 * Created by 丹 on 2014/12/30.
 */
public class MyActivityIntents {
    private Class<?> toActivity;
    private String intentName;

    public MyActivityIntents( Class<?> toActivity,String intentName) {
        this.toActivity = toActivity;
        this.intentName = intentName;
    }

    public void intentStart(Context context){
        context.startActivity(new Intent(context,toActivity));
    }

    public String getIntentName() {
        return intentName;
    }
}
