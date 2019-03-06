package com.example.asus.remindmemyself;

import android.app.KeyguardManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    final long[] pattern = {0, 1000, 1000, 1000, 1000};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_alert);

        if (Build.VERSION.SDK_INT >= 27) {
            Log.d("hellohello","greater than 27");
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);
        } else {
            Log.d("hellohello", ";lesser");

            wind = this.getWindow();
            wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }


        button=(Button)findViewById(R.id.StopAlarm);
        button.setOnClickListener(this);

        uri = RingtoneManager.getActualDefaultRingtoneUri(LocationAlertActivity.this, RingtoneManager.TYPE_ALARM);
        defaultRingtone = RingtoneManager.getRingtone(LocationAlertActivity.this, uri);
        myVib = (Vibrator)this.getSystemService(VIBRATOR_SERVICE);
        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        startAlarm();



    }



    @Override
    public void onClick(View v) {

        Log.d("bangladesh","la");
        defaultRingtone.stop();
        myVib.cancel();
        finish();

    }

    private void startAlarm() {

        if(am.getRingerMode()==AudioManager.RINGER_MODE_NORMAL)
        {
            Handler handler=new Handler();
            Runnable r=new Runnable() {
                public void run() {
                    defaultRingtone.stop();
                    myVib.cancel();
                    finish();
                }
            };
            handler.postDelayed(r, 180000);
            defaultRingtone.play();
            defaultRingtone.setLooping(true);
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
