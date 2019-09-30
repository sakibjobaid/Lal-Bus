package com.anything.asus.remindmemyself;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class closeReceiver extends BroadcastReceiver {

    public static boolean des=false;
    @Override
    public void onReceive(Context context, Intent intent) {
       des=true;

    }
}
