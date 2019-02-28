package com.example.asus.remindmemyself;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class BusReminderAlertActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    public static Ringtone defaultRingtone;
    public static Vibrator myVib;
    public static Uri uri;
    public static AudioManager am;
    final long[] pattern = {0, 1000, 1000, 1000, 1000};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_alert);

        button=(Button)findViewById(R.id.StopAlarm);
        button.setOnClickListener(this);

        uri = RingtoneManager.getActualDefaultRingtoneUri(BusReminderAlertActivity.this, RingtoneManager.TYPE_ALARM);
        defaultRingtone = RingtoneManager.getRingtone(BusReminderAlertActivity.this, uri);
        myVib = (Vibrator)this.getSystemService(VIBRATOR_SERVICE);
        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        startAlarm();



    }

    @Override
    public void onClick(View v) {

        Log.d("bangladesh","las");
        defaultRingtone.stop();
        myVib.cancel();
        finish();

    }

    private void startAlarm() {

        if(am.getRingerMode()==AudioManager.RINGER_MODE_NORMAL)
        {
            defaultRingtone.play();
            myVib.vibrate(pattern,0);

        }
        else if(am.getRingerMode()==AudioManager.RINGER_MODE_VIBRATE || am.getRingerMode()==AudioManager.RINGER_MODE_SILENT)
        {
            myVib.vibrate(pattern,0);

        }
    }


}
