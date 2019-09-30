package com.anything.asus.remindmemyself;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

public class LocationAlertActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private Window wind;
    public static Ringtone defaultRingtone;
    public static Vibrator myVib;
    public static Uri uri;
    public static AudioManager am;
    public static MediaPlayer player;
    public Activity activity;
    final long[] pattern = {0, 1000, 1000, 1000, 1000};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_alert);

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


        button=(Button)findViewById(R.id.StopAlarm);
        button.setOnClickListener(this);

        //uri = RingtoneManager.getActualDefaultRingtoneUri(LocationAlertActivity.this, RingtoneManager.TYPE_ALARM);
        //defaultRingtone = RingtoneManager.getRingtone(LocationAlertActivity.this, uri);
        myVib = (Vibrator)this.getSystemService(VIBRATOR_SERVICE);
        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        player = MediaPlayer.create(this, notification);


        startAlarm();



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

    @Override
    public void onClick(View v) {

        //defaultRingtone.stop();
        GlobalClass.locAlarm=0;
        player.stop();
        myVib.cancel();
        GlobalClass.ct=1;
        TrackerService.client.removeLocationUpdates(TrackerService.locationCallback);
        ComponentName receiver = new ComponentName(this, closeReceiver.class);
        PackageManager pm = this.getPackageManager();


        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

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

            //defaultRingtone.play();
            //defaultRingtone.setLooping(true);
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
}