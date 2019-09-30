package com.anything.asus.remindmemyself;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminBackground extends Service {

    //region variables
    private static boolean boolflag = true,prechecking = false,ekbar=true;
    private static String adminBus = null, adminTime = null;
    Location location;
    long admin_radius_aage,admin_radius_pore;
    public static LocationCallback locationCallback;
    public static NotificationManager notificationManager;
    public DatabaseReference reference,ref,adminCloseReference;
    ValueEventListener listener=null;
    public static FusedLocationProviderClient client;
    public static SharedPreferences date_pref,force_aage_pref,force_pore_pref;
    public static String date;
    public long time_difference_aage=0,time_difference_pore=0;
    public static DatabaseReference dbRef=null;
    public static ValueEventListener val_trip_rechecking=null;
    boolean first_time;
    // endregion

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {

        super.onCreate();
        client = LocationServices.getFusedLocationProviderClient(this);
        boolflag = true;
        admin_radius_aage= GlobalClass.admin_radius_aage_pref.getLong(getString(R.string.admin_radius_aage_getter),-1);
        admin_radius_pore= GlobalClass.admin_radius_pore_pref.getLong(getString(R.string.admin_radius_pore_getter),-1);


        force_aage_pref=getSharedPreferences("helloForceAage",MODE_PRIVATE);
        force_pore_pref=getSharedPreferences("helloForcePore",MODE_PRIVATE);

        time_difference_aage=force_aage_pref.getLong("forceAageGetter",1800000);
        time_difference_pore=force_pore_pref.getLong("forcePoreGetter",1800000);


        first_time=true;
        GlobalClass.ekbartime=true;

        adminCloseReference = FirebaseDatabase.getInstance().getReference();
        date_pref = getSharedPreferences(getString(R.string.date_pref_file), MODE_PRIVATE);
        date = date_pref.getString(getString(R.string.date_getter), "sakib25");

        dbRef=FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_adminRoster))
                .child(date).child(GlobalClass.BusName).child(GlobalClass.BusTime);

        GlobalClass.admin_successful_up= getSharedPreferences("helloadminsuccessful_up",MODE_PRIVATE);
        SharedPreferences.Editor admin_up=GlobalClass.admin_successful_up.edit();
        admin_up.putString("admin_successfull_up","no_way");
        admin_up.apply();



        GlobalClass.admin_successful_down= getSharedPreferences(getString(R.string.adminsuccessful_down_pref_file),MODE_PRIVATE);
        SharedPreferences.Editor admin_down=GlobalClass.admin_successful_down.edit();
        admin_down.putString("admin_successfull_down","no_way");
        admin_down.apply();

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
                     location = locationResult.getLastLocation();

                    if (!isNetworkConnected(AdminBackground.this)) {

                        firstActivity.custom_network_alert();
                        return;
                    }
                     Location TSC_center=new Location("");
                     TSC_center.setLatitude(23.732571);
                     TSC_center.setLongitude(90.395545);


                     String dir=" " ;
                     if(GlobalClass.BusTime.matches("(.*)up(.*)"))
                         dir="up";
                     else if(GlobalClass.BusTime.matches("(.*)down(.*)"))
                         dir="down";


                    if(location!=null)
                     {

                         if(TSC_center.distanceTo(location)<=admin_radius_aage && dir.equals("up"))
                         {
                             if(GlobalClass.ekbartime)
                             {
                                 GlobalClass.timestamp_time=System.currentTimeMillis();
                                 GlobalClass.ekbartime=false;

                             }


                             if(System.currentTimeMillis()-GlobalClass.timestamp_time>time_difference_aage)
                             {
                                 reference.child(getString(R.string.Firebase_admin)).child("Location")
                                         .child(adminBus)
                                         .child(adminTime).child("force").setValue("out");
                             }

                             if(first_time)
                             {
                                 first_time=false;
                                 GlobalClass.up_bool_from_admin_background=true;
                                 SharedPreferences.Editor admin_up=GlobalClass.admin_successful_up.edit();
                                 admin_up.putString("admin_successfull_up","successful_up_hoise");
                                 admin_up.apply();

                             }
                             if(GlobalClass.history_ekbar_update)
                             {
                                 GlobalClass.history_ekbar_update=false;
                                 admin_history_setup_in_db();

                             }
                             if(ekbar)
                             {
                                 final Handler handler = new Handler();
                                 handler.postDelayed(new Runnable() {
                                     @Override
                                     public void run() {

                                         ekbar=false;
                                         RepeatChecking_Trip_increment();
                                     }
                                 }, 2000);


                             }

                         }
                         else if(TSC_center.distanceTo(location)>=admin_radius_pore && dir.equals("down"))
                         {

                             if(GlobalClass.ekbartime)
                             {
                                 GlobalClass.timestamp_time=System.currentTimeMillis();
                                 GlobalClass.ekbartime=false;

                             }


                             if(System.currentTimeMillis()-GlobalClass.timestamp_time>time_difference_pore)
                             {
                                 reference.child(getString(R.string.Firebase_admin)).child("Location")
                                         .child(adminBus)
                                         .child(adminTime).child("force").setValue("out");
                             }

                             if(first_time)
                             {
                                 first_time=false;
                                 GlobalClass.down_bool_from_admin_background=true;
                                 SharedPreferences.Editor admin_down=GlobalClass.admin_successful_down.edit();
                                 admin_down.putString("admin_successfull_down","successful_down_hoise");
                                 admin_down.apply();

                             }


                             if(GlobalClass.history_ekbar_update)
                             {
                                 GlobalClass.history_ekbar_update=false;
                                 admin_history_setup_in_db();

                             }
                             if(ekbar)
                             {
                                 final Handler handler = new Handler();
                                 handler.postDelayed(new Runnable() {
                                     @Override
                                     public void run() {

                                         ekbar=false;
                                         RepeatChecking_Trip_increment();
                                     }
                                 }, 2000);


                             }

                         }
                     }

                    if (location != null) {


                        listener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.getValue()!=null)
                                {
                                    if(dataSnapshot.getValue().toString().equals("out"))
                                    {
                                        GlobalClass.forcedKickedOut=true;
                                        GlobalClass.cholbe=false;
                                        if(GlobalClass.ekbarOff)
                                            changestatus();
                                    }
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        adminCloseReference.child(getString(R.string.Firebase_admin)).child("Location")
                                .child(adminBus)
                                .child(adminTime).child("force")
                                .addValueEventListener(listener);

                        if(GlobalClass.first_on_status)
                        {

                            reference.child(getString(R.string.Firebase_admin)).child("Location")
                                    .child(adminBus)
                                    .child(adminTime).child("active_on").setValue("on");
                            GlobalClass.first_on_status=false;
                        }

                        if(GlobalClass.first_on_force)
                        {

                            reference.child(getString(R.string.Firebase_admin)).child("Location")
                                    .child(adminBus)
                                    .child(adminTime).child("force").setValue("in");
                            GlobalClass.first_on_force=false;
                        }

                        if(GlobalClass.cholbe)
                        {
                            reference.child(getString(R.string.Firebase_admin)).child("Location")
                                    .child(adminBus)
                                    .child(adminTime).child("latitude").setValue(location.getLatitude());
                            reference.child(getString(R.string.Firebase_admin)).child("Location")
                                    .child(adminBus)
                                    .child(adminTime).child("longitude").setValue(location.getLongitude());



                            if (GlobalClass.selfdestroy) {

                                stopSelf();
                            }
                        }

                    }
                    else
                    {
                    }

                }
            };


            client.requestLocationUpdates(request, locationCallback, null);


        }
    }

    private void admin_history_setup_in_db() {

        OTPactivity.val_admin_history= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long count=dataSnapshot.getChildrenCount();
                count++;
                OTPactivity.admin_history_ref.child(String.valueOf(count))
                        .child("date").setValue(date);
                OTPactivity.admin_history_ref.child(String.valueOf(count))
                        .child("bus").setValue(GlobalClass.BusName);
                OTPactivity.admin_history_ref.child(String.valueOf(count))
                        .child("time").setValue(GlobalClass.BusTime);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        if(OTPactivity.val_admin_history!=null && OTPactivity.admin_history_ref!=null)
        OTPactivity.admin_history_ref.addListenerForSingleValueEvent(OTPactivity.val_admin_history);
    }

    public static  void RepeatChecking_Trip_increment() {

         val_trip_rechecking= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.child("Count").exists() &&
                        dataSnapshot.child("MobileNo").getValue()
                                .equals(GlobalClass.ContactPref.getString("" +
                                        "contact_name","sakib")))
                {


                    return;

                }
                 else
                {
                    firstActivity.savingTripsFinally();

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        dbRef.addListenerForSingleValueEvent(val_trip_rechecking);

    }

    private void changestatus() {

        GlobalClass.ekbarOff=false;
        reference.child(getString(R.string.Firebase_admin)).child("Location")
                .child(adminBus)
                .child(adminTime).child("active_on").setValue("off");
        reference.child(getString(R.string.Firebase_admin)).child("Location")
                .child(adminBus)
                .child(adminTime).child("force").setValue("in");

        if(listener!=null && reference!=null)
            reference.removeEventListener(listener);
        this.stopSelf();
        //stopService()

    }

    private void notification() {
        createNotificationChannel();
        if(GlobalClass.signal)
        sendNotification("Admin mode is active");

    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel one";
            String description = "this is channel one method";
            int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel channel = new NotificationChannel("3", name, importance);
            channel.setDescription(description);

            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String msg) {


        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification_avail

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "3")
                .setContentTitle(getString(R.string.app_name))
                .setContentText(msg)
                .setOngoing(true)
                .setChannelId("3")
                .setColorized(true)
                .setColor(Color.rgb(255, 255, 255))
                .setLights(Color.rgb(255, 255, 255), 1000, 1000)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.my_bus);
        startForeground(3, builder.build());

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

    public boolean isNetworkConnected(Context context) {

        prechecking = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        prechecking = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        prechecking = true;
                    }
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        prechecking = true;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        prechecking = true;
                    }
                }
            }
        }
        return prechecking;
    }

    //region receiver
    public BroadcastReceiver stopReceiver = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {

            Intent intent1 = new Intent(AdminBackground.this, firstActivity.class);
            intent1.putExtra("BUSNAME", GlobalClass.BusName);
            intent1.putExtra("TIME", GlobalClass.BusTime);
            intent1.putExtra("name", "admin");
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent1);



        }
    };
    //endregion



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {


    }
}