package com.example.asus.remindmemyself;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminBackground extends Service {

    private static boolean boolflag = true;
    private static String adminBus = null, adminTime = null;

    public static LocationCallback locationCallback;
    public static NotificationManager notificationManager;
    public DatabaseReference reference;
    public static FusedLocationProviderClient client;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        client = LocationServices.getFusedLocationProviderClient(this);
        boolflag = true;
        Log.d("jobaid", "AdminBackground:onCreate");

        if(GlobalClass.signal)
        {
            requestLocationUpdatesLocation();
            notification();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        adminBus = intent.getStringExtra("BusName");
        adminTime = intent.getStringExtra("BusTime");
        return START_REDELIVER_INTENT;
        //return super.onStartCommand(intent, flags, startId);
    }


    private void requestLocationUpdatesLocation() {

        Log.d("jobaid", "AdminBackground:requestLocationUpdates");

        LocationRequest request = new LocationRequest();
        request.setInterval(5000);
        request.setFastestInterval(2000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        int permission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    reference = FirebaseDatabase.getInstance().getReference();
                    Location location = locationResult.getLastLocation();
                    if (location != null) {


                        AdminLocation al = new AdminLocation(location.getLatitude(), location.getLongitude());
                        Toast toast= Toast.makeText(AdminBackground.this, "OK", Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 100);
                        toast.show();
                        Log.d("jobaid", "AdminBackground : callback2");
                        reference.child("Admin").child("Location")
                                .child(adminBus)
                                .child(adminTime)
                                .setValue(al);

                        if (GlobalClass.selfdestroy) {
                            stopSelf();
                            client.removeLocationUpdates(locationCallback);
                        }
                    }
                    else
                    {
                        Toast.makeText(AdminBackground.this,"Location is null.Check network connection",Toast.LENGTH_LONG).show();
                    }

                }
            };

            Toast.makeText(AdminBackground.this, "eshe gese asljdfjasoijfaioji", Toast.LENGTH_SHORT).show();
            Log.d("jobaid", "eshe gese");
            client.requestLocationUpdates(request, locationCallback, null);


        }
    }


    private void notification() {
        createNotificationChannel();
        Log.d("jobaid","AdminBackground: notification");
        if(GlobalClass.signal)
        sendNotification("Admin mode is active");

    }

    private void createNotificationChannel() {

        Log.d("jobaid", "AdminBackground: createNotificationChannel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel one";
            String description = "this is channel one method";
            int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel channel = new NotificationChannel("2", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification_avail behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String msg) {


        Log.d("jobaid", "AdminBackground:sendNotification");
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification_avail

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "2")
                .setContentTitle(getString(R.string.app_name))
                .setContentText(msg)
                .setOngoing(true)
                .setChannelId("2")
                .setColorized(true)
                .setColor(Color.rgb(255, 255, 255))
                .setLights(Color.rgb(255, 255, 255), 1000, 1000)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.my_bus);
        startForeground(2, builder.build());

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


    public BroadcastReceiver stopReceiver = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("jobaid", "AdminBackground:onReceive");


        }
    };
}