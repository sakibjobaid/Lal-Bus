package com.example.asus.remindmemyself;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BusTrackerService extends Service {

    private boolean boolflag=true,alarmflag=true,oncealarm=false;
    private static final String TAG = TrackerService.class.getSimpleName();
    private static Location newCenterLocation=null,userLocation=null;
    private static double latitude,longitude,radius,dist;

    private LocationCallback locationCallback;
    public  static ValueEventListener listener;


    FusedLocationProviderClient client;


    @Override
    public IBinder onBind(Intent intent) {

        return null;}



    @Override
    public void onCreate() {
        super.onCreate();
        client= LocationServices.getFusedLocationProviderClient(this);
        Log.d("jobaid","BusTrackerService:onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        userLocation= new Location("");
        newCenterLocation = new Location("");


        Log.d("jobaid","BusTrackerService:onStartCommand");

        latitude= intent.getDoubleExtra("Latitude",-1);
        longitude=intent.getDoubleExtra("Longitude",-1);
        radius=intent.getIntExtra("Radius",-1);

        Log.d("jobaid","BusTrackerService "+String.valueOf(latitude));
        Log.d("jobaid","BusTrackerService "+String.valueOf(longitude));
        Log.d("jobaid",GlobalClass.BusName);
        Log.d("jobaid",GlobalClass.BusTime);
        newCenterLocation.setLatitude(latitude);
        newCenterLocation.setLongitude(longitude);

        dataRequest();
        notification();

        return super.onStartCommand(intent, flags, startId);
    }

    private void dataRequest() {

        Log.d("jobaid","datarequest");

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


            listener= new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    Log.d("jobaid","BusTrackerService : onDataChange");
                    Toast.makeText(BusTrackerService.this,"data changing",Toast.LENGTH_SHORT).show();

                    TrackLocation(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            ref.child("Admin").child("Location")
                    .child(GlobalClass.BusName)
                    .child(GlobalClass.BusTime)
                    .addValueEventListener(listener);
            Log.d("jobaid","BusTrackerService:Listener");

    }

    private void TrackLocation(DataSnapshot dataSnapshot) {

        double  lat,lng,dis;
        LatLng la;
        firstActivity.countMarker++;
        Log.d("jobaid","BusTrackerService:setMarker");
        lat= (double) dataSnapshot.child("latitude").getValue();
        lng = (double) dataSnapshot.child("longitude").getValue();

        userLocation.setLatitude(lat);
        userLocation.setLongitude(lng);
        if(newCenterLocation!=null)
        {
            dis=userLocation.distanceTo(newCenterLocation);
            Log.d("jobaid","BusTrackerService: newCenter!=null "+String.valueOf(dis));
            if(dis<=GeofenceSettings1.radius )
            {
                stopSelf();
                Log.d("jobaid","BusTrackerService:dis");
                alarmflag=false;
                if(oncealarm )
                {
                    oncealarm=false;
                    Intent intent1= new Intent(this,MyReceiver.class);
                    intent1.putExtra("name","BusReminder");
                    PendingIntent p= PendingIntent.getBroadcast(this,0,intent1,0);
                    AlarmManager am= (AlarmManager)getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),p);
                    //notification_avail();
                }

            }
        }
    }

    private void notification() {
        createNotificationChannel();
        sendNotification("Tap here to cancel bus reminder");

    }

    private void sendNotification(String msg) {


        Log.d("jobaid","BusTrackerService:sendNotification");
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification_avail

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"123")
                .setContentTitle(getString(R.string.app_name))
                .setContentText(msg)
                .setOngoing(true)
                .setChannelId("2")
                .setColorized(true)
                .setColor(Color.rgb(255, 255, 255))
                .setLights(Color.rgb(255, 255, 255),1000,1000)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.my_bus);
        startForeground(1, builder.build());

//        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
//                .setSmallIcon(R.drawable.pin)
//                .setContentTitle("Bus Notification")
//                .setContentText(msg)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                // Set the intent that will fire when the user taps the notification_avail
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(false);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//// notificationId is a unique int for each notification_avail that you must define
//        notificationManager.notify(3, mBuilder.build());

    }

    private void createNotificationChannel() {

        Log.d("jobaid", "BusTrackerService: createNotificationChannel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel one";
            String description = "this is channel one method";
            int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel channel = new NotificationChannel("2", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification_avail behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("jobaid","BusTrackerService:onReceive");

            GlobalClass.selfdestroy=true;
            if (Build.VERSION.SDK_INT >22)
            {
                unregisterReceiver(stopReceiver);
                stopSelf();
            }
            else
                onDestroy();
            Toast.makeText(BusTrackerService.this,"Bus Reminder is cancelled",Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public void onDestroy()
    {
        Log.d("jobaid","BusTrackerService:onDestroy");
        GlobalClass.selfdestroy=true;
        if(Build.VERSION.SDK_INT <=22)
        {
            //unregisterReceiver(stopReceiver);
            stopSelf();
        }

        super.onDestroy();
    }





}