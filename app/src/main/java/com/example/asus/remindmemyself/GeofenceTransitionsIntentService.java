package com.example.asus.remindmemyself;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;

import java.util.ArrayList;
import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class GeofenceTransitionsIntentService extends IntentService {
    public static final int GEOFENCE_NOTIFICATION_ID = 0;
    private GeofencingClient mGeofencingClient;
    private GeofencingRequest geofenceRequest;
    public static MediaPlayer md1, md2,md3;
    private Geofence geofence;
    private AlertDialog.Builder dialog;
    private AlertDialog alert;
    public static Uri uri;
    public static int index_no=0;
    public static  Ringtone defaultRingtone;
    public static Vibrator myVib;
    public static AudioManager am;
    public static Thread x;
    public static boolean con=true;


    public GeofenceTransitionsIntentService() {

        super("GeofenceTransitionsIntentService");
        Log.d("jobaid", "Intent:GeofenceTransitionsConstructor");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        if (intent != null) {
//            final String action = intent.getAction();
//            if (ACTION_FOO.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionFoo(param1, param2);
//            } else if (ACTION_BAZ.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionBaz(param1, param2);
//            }
//        }
        // Retrieve the Geofencing intent
        uri = RingtoneManager.getActualDefaultRingtoneUri(GeofenceTransitionsIntentService.this.getApplicationContext(), RingtoneManager.TYPE_ALARM);
        defaultRingtone = RingtoneManager.getRingtone(GeofenceTransitionsIntentService.this, uri);
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        //startAlarm();
//        md1 = MediaPlayer.create(this, R.raw.ringtone);
//        md2 = MediaPlayer.create(this, R.raw.ringtone);
//        md3 = MediaPlayer.create(this, R.raw.ringtone);
//        md1.start();
//        md1.setNextMediaPlayer(md2);
//        md2.setNextMediaPlayer(md3);


        Log.d("jobaid", "Intent:onHandleIntent");
        createNotificationChannel();
        //alertmethod();

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {

            String errorMessage = getError(geofencingEvent.getErrorCode());
            Log.e("jobaid", "Intent:" + errorMessage);
            return;
        }

        // Retrieve GeofenceTrasition
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );
            sendNotification("You entered in your selected zone!!");
        }

       // Intent intent1= new Intent(this,GeofenceSettings1.class);
        Intent intent1= new Intent(this,MyReceiver.class);
        intent1.putExtra("name","LocationAlarm");
        PendingIntent p= PendingIntent.getBroadcast(this,0,intent1,0);
        AlarmManager am= (AlarmManager)getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),p);
        //intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent1.putExtra("IntentService","hello");
        //getApplication().startActivity(intent1);
    }




//    class Mythread extends Thread {
//
//        public void run() {
//            for(int i = 0; i < 10 && con; i++){ //repeat the pattern 5 times
//                myVib.vibrate(pattern, -1);
//                try {
//                    Thread.sleep(4000); //the time, the complete pattern needs
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        Log.d("jobaid", "Intent:createNotificationChannel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel one";
            String description = "this is channel one method";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification_avail behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Create a detail message with Geofences received
    private String getGeofenceTransitionDetails
    (GeofenceTransitionsIntentService geofenceTransitionsIntentService,
     int geoFenceTransition, List<Geofence> triggeringGeofences) {

        Log.d("jobaid", "Intent:getTransitionDetails");

        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            Log.d("jobaid", "geofence ase");
            triggeringGeofencesList.add(geofence.getRequestId());
        }

        String status = null;
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.d("jobaid", "Intent:entering");
            status = "Entering ";
        } else if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.d("jobaid", "Intent:entering");
            status = "Exiting ";
        }

        return status + TextUtils.join(", ", triggeringGeofencesList);
    }

    private void sendNotification(String msg) {

        Log.d("jobaid", "Intent:sendNotification");

        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.pin)
                .setContentTitle("Location Notification")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification_avail
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification_avail that you must define
        notificationManager.notify(2, mBuilder.build());

//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(contentIntent);

        // Add as notification_avail

    }

    // Handle errors
    String getError(int errorCode) {

        Log.d("jobaid", "Intent:getError");

        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }

}
