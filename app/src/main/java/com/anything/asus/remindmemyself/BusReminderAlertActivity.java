package com.anything.asus.remindmemyself;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class BusReminderAlertActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private Window wind;
    public static Ringtone defaultRingtone;
    public static Vibrator myVib;
    public static Uri uri;
    public static MediaPlayer player;

    public static AudioManager am;
    final long[] pattern = {0, 1000, 1000, 1000, 1000};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_reminder_alert);

        if (Build.VERSION.SDK_INT >= 27) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);
        } else {

            wind = this.getWindow();
            wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

       // Toast.makeText(this,"ashese",Toast.LENGTH_LONG).show();
        button=(Button)findViewById(R.id.StopAlarm);
        button.setOnClickListener(this);

//        uri = RingtoneManager.getActualDefaultRingtoneUri(BusReminderAlertActivity.this, RingtoneManager.TYPE_ALARM);
//        defaultRingtone = RingtoneManager.getRingtone(BusReminderAlertActivity.this, uri);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        player = MediaPlayer.create(this, notification);
        myVib = (Vibrator)this.getSystemService(VIBRATOR_SERVICE);
        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        startAlarm();



    }

    @Override
    public void onClick(View v) {

        BusTrackerService.busTrackerService.stopSelf();
        GlobalClass.busAlarm=0;
        GlobalClass.finalBusRadius=0;
        player.stop();
        myVib.cancel();
        finish();

    }

    private void startAlarm() {

        if(am.getRingerMode()==AudioManager.RINGER_MODE_NORMAL)
        {

            Handler handler=new Handler();
            Runnable r=new Runnable() {
                public void run() {
                    //defaultRingtone.stop();
                    player.stop();
                    myVib.cancel();
                    finish();
                }
            };
            handler.postDelayed(r, 180000);
            player.start();
            player.setLooping(true);
            myVib.vibrate(pattern,0);

        }
        else if(am.getRingerMode()==AudioManager.RINGER_MODE_VIBRATE || am.getRingerMode()==AudioManager.RINGER_MODE_SILENT)
        {

            Handler handler=new Handler();
            Runnable r=new Runnable() {
                public void run() {
                    myVib.cancel();
                    finish();
                }
            };
            handler.postDelayed(r, 180000);
            myVib.vibrate(pattern,0);

        }
    }

    @Override
    protected void onStop() {

        SharedPreferences busNamePref = getSharedPreferences(getString(R.string.bus_name_pref_file), MODE_PRIVATE);
        SharedPreferences busTimePref = getSharedPreferences(getString(R.string.bus_time_pref_file), MODE_PRIVATE);
        SharedPreferences.Editor preEditor = busTimePref.edit();
        preEditor.putString(getString(R.string.bus_time_getter),GlobalClass.BusTime);
        preEditor.apply();

        SharedPreferences.Editor pre1Editor = busNamePref.edit();
        pre1Editor.putString(getString(R.string.bus_name_getter),GlobalClass.BusName);
        pre1Editor.apply();
        super.onStop();
    }
}