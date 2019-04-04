package com.example.asus.remindmemyself;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        String in= intent.getStringExtra("name");
        if(in.equals("LocationAlarm"))
        {
            Intent intent1= new Intent(context,LocationAlertActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
        if(in.equals("BusReminder"))
        {
            Intent intent1 = new Intent(context,BusReminderAlertActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);

        }

   }

}
