package com.anything.asus.remindmemyself;


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
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
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

        if (Build.VERSION.SDK_INT <= 22) {
            registerReceiver(locstopReceiver, new IntentFilter(stop));
        }
        requestLocationUpdatesLocation();

        notification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        userLocation = new Location("");
        newCenterLocation = new Location("");

        latitude = intent.getDoubleExtra("Latitude", -1);
        longitude = intent.getDoubleExtra("Longitude", -1);
        radius = intent.getIntExtra("Radius", -1);
        newCenterLocation.setLatitude(latitude);
        newCenterLocation.setLongitude(longitude);


        //return START_STICKY;
        return START_REDELIVER_INTENT;
        //return super.onStartCommand(intent, flags, startId);
    }


    private void requestLocationUpdatesLocation() {


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
                    Location location = locationResult.getLastLocation();
                    if (location != null) {

                        GlobalClass.currentlat = location.getLatitude();
                        GlobalClass.currentlon = location.getLongitude();

                            userLocation.setLatitude(location.getLatitude());
                            userLocation.setLongitude(location.getLongitude());

                        dist = userLocation.distanceTo(newCenterLocation);
                        if (newCenterLocation != null) {

                            if (dist <= radius && boolflag) {

                                stopSelf();

                                boolflag = false;
                                Intent intent = new Intent(TrackerService.this, LocationAlertActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                            if (GlobalClass.selfdestroy) {
                                client.removeLocationUpdates(locationCallback);
                            }
                        } else {
                        }
                    }
                }
            };

            client.requestLocationUpdates(request, locationCallback, null);


        }
    }


    private void notification() {
        createNotificationChannel();
        sendNotification("Tap here to cancel location alarm");

    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel one";
            String description = "this is channel one method";
            int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel channel = new NotificationChannel("2", name, importance);
            channel.setDescription(description);

            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String msg) {



        if (Build.VERSION.SDK_INT > 22) {
            registerReceiver(locstopReceiver, new IntentFilter(stop));
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

    public BroadcastReceiver locstopReceiver = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {
            if (Build.VERSION.SDK_INT > 22) {
                toastIconInfo("Location Alarm is cancelled");
                GlobalClass.selfdestroy = true;
                unregisterReceiver(locstopReceiver);
                stopSelf();
            } else
                onDestroy();
            GlobalClass.locAlarm=0;
            GlobalClass.centreLoc=null;
            GlobalClass.locradius=0;

            toastIconInfo("Location Alarm is cancelled");

        }
    };

    private void toastIconInfo(String purpose) {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View custom_view = inflater.inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(purpose);
        ((CardView) custom_view.findViewById(R.id.parent_view2)).setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));

        toast.setView(custom_view);
        toast.show();
    }


    @Override
    public void onDestroy() {

        if (Build.VERSION.SDK_INT <= 22) {
            GlobalClass.selfdestroy = true;
            if(locstopReceiver!=null)
            //unregisterReceiver(locstopReceiver);
            stopSelf();
        }



        super.onDestroy();
    }
}