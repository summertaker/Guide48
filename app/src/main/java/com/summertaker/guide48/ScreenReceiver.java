package com.summertaker.guide48;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null) {

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

                //Intent i = new Intent(context, ScreenActivity.class);
                //i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //context.startActivity(i);
            }
        }
    }
}
