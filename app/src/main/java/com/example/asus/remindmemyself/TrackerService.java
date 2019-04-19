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

public class TrackerService extends Service {

    public static TrackerService trackerService;
    private static boolean boolflag = true;
    private static final String TAG = TrackerService.class.getSimpleName();
    private static Location newCenterLocation = null, userLocation = null;
    private static double latitude, longitude, radius, dist;
    public static LocationCallback locationCallback;
    public static NotificationManager notificationManager;

    public static FusedLocationProviderClient client;
    String stop = "stop";


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        trackerService=this;
        client = LocationServices.getFusedLocationProviderClient(this);
        boolflag = true;
        Log.d("jobaid", "TrackerService:onCreate");

        if (Build.VERSION.SDK_INT <= 22) {
            registerReceiver(stopReceiver, new IntentFilter(stop));
        }
        requestLocationUpdatesLocation();

        notification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        userLocation = new Location("");
        newCenterLocation = new Location("");

        Log.d("jobaid", "TrackerService: onstartCommand");
        latitude = intent.getDoubleExtra("Latitude", -1);
        longitude = intent.getDoubleExtra("Longitude", -1);
        radius = intent.getIntExtra("Radius", -1);
        Log.d("jobaid", "TrackerService:OnStartCommand " + String.valueOf(latitude));
        Log.d("jobaid", "TrackerService:OnStartCommand " + String.valueOf(longitude));
        newCenterLocation.setLatitude(latitude);
        newCenterLocation.setLongitude(longitude);


        //return START_STICKY;
        return START_REDELIVER_INTENT;
        //return super.onStartCommand(intent, flags, startId);
    }


    private void requestLocationUpdatesLocation() {

        Log.d("jobaid", "TrackerService:requestLocationUpdates");

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
                    //DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    Location location = locationResult.getLastLocation();
                    if (location != null) {

                        GlobalClass.currentlat = location.getLatitude();
                        GlobalClass.currentlon = location.getLongitude();

                        userLocation.setLatitude(location.getLatitude());
                        userLocation.setLongitude(location.getLongitude());


                        Log.d("jobaid", "TrackerService: pp " + location.getLatitude() + "  " + location.getLongitude());

                        //Toast.makeText(TrackerService.this,"outside",Toast.LENGTH_SHORT).show();
                        dist = userLocation.distanceTo(newCenterLocation);
                        if (newCenterLocation != null) {
                            Log.d("jobaid", "TrackerService: " + String.valueOf(dist));
                            Log.d("jobaid", "TrackerService: " + String.valueOf(radius));

                            Toast.makeText(TrackerService.this, "just not null", Toast.LENGTH_SHORT).show();

                            Log.d("jobaid", "TrackerService:not null pp " + location.getLatitude() + "  " + location.getLongitude() + " inside");

                            if (dist <= radius && boolflag) {

                                stopSelf();
                                Toast.makeText(TrackerService.this, "inside", Toast.LENGTH_SHORT).show();

                                boolflag = false;
                                Log.d("jobaid", "TrackerService: " + location.getLatitude() + "  " + location.getLongitude() + " deep " + "inside");
                                Intent intent = new Intent(TrackerService.this, LocationAlertActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                            if (GlobalClass.selfdestroy) {
                                client.removeLocationUpdates(locationCallback);
                            }
                        } else {
                            Toast.makeText(TrackerService.this, "newlocation null", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            };

            Toast.makeText(TrackerService.this, "eshe gese asljdfjasoijfaioji", Toast.LENGTH_SHORT).show();
            Log.d("jobaid", "eshe gese");
            client.requestLocationUpdates(request, locationCallback, null);


        }
    }


    private void notification() {
        createNotificationChannel();
        sendNotification("Tap here to cancel location alarm");

    }

    private void createNotificationChannel() {

        Log.d("jobaid", "TrackerService: createNotificationChannel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel one";
            String description = "this is channel one method";
            int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel channel = new NotificationChannel("3", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification_avail behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String msg) {


        Log.d("jobaid", "TrackerService:sendNotification");

        if (Build.VERSION.SDK_INT > 22) {
            registerReceiver(stopReceiver, new IntentFilter(stop));
        }
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
            Log.d("jobaid", "TrackerService:onReceive");
            if (Build.VERSION.SDK_INT > 22) {
                Log.d("qwer","22 greater loc");
                Toast.makeText(TrackerService.this, "Location Alarm is cancelled", Toast.LENGTH_LONG).show();
                GlobalClass.selfdestroy = true;
                unregisterReceiver(stopReceiver);
                stopSelf();
            } else
                onDestroy();
            GlobalClass.locAlarm=0;
            GlobalClass.centreLoc=null;
            GlobalClass.locradius=0;
            Log.d("qwer","22 lesser loc");

            Toast.makeText(TrackerService.this, "Location Alarm is cancelled", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public void onDestroy() {

        Log.d("jobaid","Trackerservice: onDestroy");
        if (Build.VERSION.SDK_INT <= 22) {
            GlobalClass.selfdestroy = true;
            if(stopReceiver!=null)
            //unregisterReceiver(stopReceiver);
            stopSelf();
        }



        super.onDestroy();
    }
}