package com.anything.asus.remindmemyself;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class Splashscreen extends AppCompatActivity implements OnMapReadyCallback {

    //region variable

    private LocationManager lm;
    private static boolean flag = false;
    public String temp, temp2;
    public static int k, l, k1, l1;
    public static intt[] up = null;
    public static intt[] down = null;
    public static intt2[] up2 = null;
    public static intt2[] down2 = null;
    private FirebaseAuth mAuth;
    ProgressBar pb;
    long timestampFromFirebase;

    boolean overallek = true;
    boolean datebool = false, firstbool = false, activebool = false, schedulebool = false, timebool = false,
            routebool = false, stoppagebool = false, admin_radius_aage_bool = false,
            admin_pore_bool = false, admin_aage_bool = false, admin_radius_pore_bool = false,
            bool_force_aage=false,bool_force_pore=false;

    boolean dateek = true, firstek = true, activeek = true, scheduleek = true, timeek = true,
            routeek = true, stoppageek = true, admin_radius_ek_aage = true, admin_aage_ek = true,
            admin_pore_ek = true, admin_radius_ek_pore = true,ekbar_force_aage=true,ekbar_force_pore=true;

    static int progress_cnt = 30;
    GoogleMap mMap;


    private FusedLocationProviderClient fusedLocationClient;

    DatabaseReference timeReference = null, scheduleReference = null,
            firstReference = null, routeReference = null,
            dateReference = null, setDate = null,
            stoppageReference = null, activebusReference = null,
            admin_radius_aage = null, admin_aage = null, admin_pore = null,
            receivefromTimeStamp = null, timestamp = null,
            admin_radius_pore = null, force_aage_ref=null,force_pore_ref=null;

    public SharedPreferences time_pref, schedule_pref, first_pref, adminHoise_pref, route_pref,
            date_pref, repeatTime_pref, stoppage_pref, busName_pref,force_aage,force_pore;

    public static int i = 0, sleep;
    public AlertDialog.Builder builder;
    ValueEventListener val_time = null, val_schedule = null, val_timestamp = null, val_first = null,
            val_route = null, val_date = null, val_stoppage = null, val_activebus = null,
            val_admin_aage_radius = null, val_admin_pore_radius = null, val_admin_aage = null,
            val_admin_pore = null,val_admin_force_aage=null, val_admin_force_pore=null;

    OnCompleteListener firstListener = null, secondListener = null;





//endregion

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        //region admin_profile sharedpreference and editor
        GlobalClass.ContactPref = getSharedPreferences(getString(R.string.globalclass_contact_name_pref_file), MODE_PRIVATE);
        GlobalClass.ContactEditor = GlobalClass.ContactPref.edit();
        GlobalClass.NamePref = getSharedPreferences(getString(R.string.globalclass_profile_name_pref_file), MODE_PRIVATE);
        GlobalClass.NameEditor = GlobalClass.NamePref.edit();
        GlobalClass.DeptPref = getSharedPreferences(getString(R.string.globalclass_dept_name_pref_file), MODE_PRIVATE);
        GlobalClass.DeptEditor = GlobalClass.DeptPref.edit();
        GlobalClass.MailPref = getSharedPreferences(getString(R.string.globalclass_mail_name_pref_file), MODE_PRIVATE);
        GlobalClass.MailEditor = GlobalClass.MailPref.edit();
        GlobalClass.FbPref = getSharedPreferences(getString(R.string.globalclass_fb_name_pref_file), MODE_PRIVATE);
        GlobalClass.FbEditor = GlobalClass.FbPref.edit();
        GlobalClass.TripPref = getSharedPreferences(getString(R.string.globalclass_trip_name_pref_file), MODE_PRIVATE);
        GlobalClass.TripEditor = GlobalClass.TripPref.edit();
        GlobalClass.upTripPref = getSharedPreferences(getString(R.string.globalclass_upTrip_name_pref_file), MODE_PRIVATE);
        GlobalClass.upTripEditor = GlobalClass.upTripPref.edit();
        GlobalClass.downTripPref = getSharedPreferences(getString(R.string.globalclass_downTrip_name_pref_file), MODE_PRIVATE);
        GlobalClass.downTripEditor = GlobalClass.downTripPref.edit();
        GlobalClass.PicStringPref = getSharedPreferences(getString(R.string.globalclass_picString_name_pref_file), MODE_PRIVATE);
        GlobalClass.PicStringEditor = GlobalClass.PicStringPref.edit();
        //endregion

        //region preferences

        force_aage=getSharedPreferences("helloForceAage",MODE_PRIVATE);
        force_pore=getSharedPreferences("helloForcePore",MODE_PRIVATE);
        time_pref = getSharedPreferences(getString(R.string.time_pref_file), MODE_PRIVATE);
        schedule_pref = getSharedPreferences(getString(R.string.schedule_pref_file), MODE_PRIVATE);
        first_pref = getSharedPreferences(getString(R.string.first_pref_file), MODE_PRIVATE);
        route_pref = getSharedPreferences(getString(R.string.route_pref_file), MODE_PRIVATE);
        date_pref = getSharedPreferences(getString(R.string.date_pref_file), MODE_PRIVATE);
        repeatTime_pref = getSharedPreferences(getString(R.string.repeatTime_pref_file), MODE_PRIVATE);
        stoppage_pref = getSharedPreferences(getString(R.string.stoppage_pref_file), MODE_PRIVATE);
        busName_pref = getSharedPreferences(getString(R.string.busName_pref_file), MODE_PRIVATE);
        adminHoise_pref = getSharedPreferences(getString(R.string.adminHoise_pref_file), MODE_PRIVATE);

        GlobalClass.admin_aage_pref = getSharedPreferences(getString(R.string.admin_aage_pref_file), MODE_PRIVATE);
        GlobalClass.admin_radius_aage_pref = getSharedPreferences(getString(R.string.admin_radius_aage_pref_file), MODE_PRIVATE);
        GlobalClass.admin_radius_pore_pref = getSharedPreferences(getString(R.string.admin_radius_pore_pref_file), MODE_PRIVATE);
        GlobalClass.admin_pore_pref = getSharedPreferences(getString(R.string.admin_pore_pref_file), MODE_PRIVATE);

        //endregion

        //region Setting default value in sharedpreference
        SharedPreferences.Editor preferencesEditor = date_pref.edit();
        preferencesEditor.putString(getString(R.string.date_getter), "Not provided");
        preferencesEditor.apply();
        preferencesEditor = route_pref.edit();
        preferencesEditor.putString(getString(R.string.route_getter), "Not provided");
        preferencesEditor.apply();
        preferencesEditor = first_pref.edit();
        preferencesEditor.putString("First", "Not provided");
        preferencesEditor.apply();
        preferencesEditor = time_pref.edit();
        preferencesEditor.putString(getString(R.string.time_getter), "Not provided");
        preferencesEditor.apply();
        preferencesEditor = schedule_pref.edit();
        preferencesEditor.putString(getString(R.string.schedule_getter), "Not provided");
        preferencesEditor.apply();
        preferencesEditor = stoppage_pref.edit();
        preferencesEditor.putString(getString(R.string.stoppage_getter), "Not provided");
        preferencesEditor.apply();
        preferencesEditor = busName_pref.edit();
        preferencesEditor.putString(getString(R.string.bus_name_getter), "Not provided");
        preferencesEditor.apply();
        //endregion


        mAuth = FirebaseAuth.getInstance();

        if (GlobalClass.BusNameKey != null || GlobalClass.BusNameKey.size() != 0)
            GlobalClass.BusNameKey.clear();

        if (adminHoise_pref.getString(getString(R.string.adminHoise_getter), "Not provided").equals("Yes"))
            GlobalClass.adminHoise = true;

        //region first time,no network
        if (!first_pref.getString(getString(R.string.first_login_getter), "").equals("2523")
                && !isNetworkConnected(Splashscreen.this)) {

            GlobalClass.AdminVirgin = true;
            GlobalClass.UserVirgin=true;
            sleep = 20000;
            networkAlert();
        }//endregion

        //region no network
        else if (!isNetworkConnected(Splashscreen.this)) {
            sleep = 4000;

            networkAlert();
        }//endregion


        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String formattedDate = dateFormat.format(Calendar.getInstance().getTime());
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        pb = findViewById(R.id.pbBar);


        if(isNetworkConnected(this))
        updateCheking();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void updateCheking() {


        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        final String newVersion= firebaseRemoteConfig.getString("app_version");

        HashMap<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put("app_version", "3.0");


        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        firebaseRemoteConfig.fetch() // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            firebaseRemoteConfig.activate();
                            String currentVersion=getAppVersion(Splashscreen.this);

                            SharedPreferences appversion=getSharedPreferences("helloAppVersion",MODE_PRIVATE);
                            SharedPreferences.Editor ed22= appversion.edit();
                            ed22.putString("AppVersion",currentVersion);
                            ed22.apply();



                            if(!newVersion.equals(currentVersion) &&
                                    (!newVersion.equals("") && newVersion.length()!=0) && newVersion!=null )
                            {

                                onUpdateNeeded("https://play.google.com/store/apps/details?id=com.anything.asus.remindmemyself");
                            }

                            else
                            {

                                continueToNext();
                            }
                        }
                    }
                });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void continueToNext() {

        //region timestamp setup version
        timestamp = FirebaseDatabase.getInstance().getReference();

        firstListener = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {

                    callDateFromFirebase();
                }
                else
                {
                    toastIconInfo("Error in data loading. Restart again.");
                }
            }
        };

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {

                timestamp.child("TIMESTAMP").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(firstListener);

            }
        }, 3000);
        //endregion


        if (isNetworkConnected(this)) {
            if (Build.VERSION.SDK_INT > 23)
                pb.setProgress(progress_cnt, true);
            else
                pb.setProgress(progress_cnt);

        }

    }

    private String getAppVersion(Context context) {

        String result = "";

        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");


        } catch (PackageManager.NameNotFoundException e) {
        }

        return result;
    }

    private void callDateFromFirebase() {


        receivefromTimeStamp = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_timestamp));
        val_timestamp = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                timestampFromFirebase = (long) dataSnapshot.getValue();

                GlobalClass.clock = timestampFromFirebase - System.currentTimeMillis();
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(timestampFromFirebase);

                GlobalClass.timeStampDate = android.text.format.DateFormat.format("dd-MM-yyyy", cal).toString();

                GlobalClass.timeStampTime = android.text.format.DateFormat.format("HH:mm", cal).toString();

                setDateToFirebase();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        receivefromTimeStamp.addValueEventListener(val_timestamp);
    }

    private void setDateToFirebase() {


        setDate = FirebaseDatabase.getInstance().getReference();
        secondListener = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if (task.isSuccessful()) {


                    //region first time,no network
                    if (!first_pref.getString(getString(R.string.first_login_getter), "").equals("2523")
                            && !isNetworkConnected(Splashscreen.this)) {

                        GlobalClass.AdminVirgin = true;
                        sleep = 20000;
                        networkAlert();
                    }//endregion

                    //region first time,network

                    else if (!first_pref.getString(getString(R.string.first_login_getter), "").equals("2523")
                            && isNetworkConnected(Splashscreen.this)) {


                        sleep = 20000;
                        GlobalClass.AdminVirgin = true;
                        GlobalClass.UserVirgin=true;
                        final Timer t = new Timer();
                        TimerTask tt = new TimerTask() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void run() {
                                if (Build.VERSION.SDK_INT > 23)
                                    pb.setProgress(progress_cnt, true);
                                else
                                    pb.setProgress(progress_cnt);
                                if (progress_cnt == 100)
                                    t.cancel();
                            }

                        };
                        t.schedule(tt, 0, 150);


                        final Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                Allfetch();

                            }
                        }, 3000);

                    }//endregion

                    //region no network
                    else if (!isNetworkConnected(Splashscreen.this)) {
                        sleep = 4000;
                        networkAlert();
                    }//endregion

                    //region network
                    else if (isNetworkConnected(Splashscreen.this)) {

                        sleep = 20000;

                        final Timer t = new Timer();
                        TimerTask tt = new TimerTask() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void run() {
                                if (Build.VERSION.SDK_INT > 23)
                                    pb.setProgress(progress_cnt, true);
                                else
                                    pb.setProgress(progress_cnt);
                                if (progress_cnt == 100)
                                    t.cancel();
                            }

                        };
                        t.schedule(tt, 0, 150);

                        final Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Allfetch();

                            }
                        }, 3000);

                    }//endregion

                }
            }
        };
        setDate.child(getString(R.string.Firebase_date)).setValue(GlobalClass.timeStampDate).addOnCompleteListener(secondListener);
    }

    private void Allfetch() {

        //region date
        dateReference = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_date));
        val_date = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                SharedPreferences.Editor preferencesEditor = date_pref.edit();
                preferencesEditor.putString(getString(R.string.date_getter), dataSnapshot.getValue().toString());
                preferencesEditor.apply();
                datebool = true;
                if (dateek) {
                    dateek = false;
                    doJob();
                    progress_cnt += 10;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Allfetch();
            }
        };
        dateReference.addValueEventListener(val_date);
        //endregion

//        //region password
//        passReference = FirebaseDatabase.getInstance().getReference()
//                .child(getString(R.string.Firebase_password));
//        val_pass = new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                long i = 0, child_count = dataSnapshot.getChildrenCount();
//
//                for (DataSnapshot busName : dataSnapshot.getChildren()) {
//
//                    ++i;
//
//                    for (DataSnapshot version : busName.getChildren()) {
//                        if (!version.getKey().toString().equals(pass_version.getString(busName.getKey().toString() + "pass_version", "sakib"))
//                                || version.getKey().toString().equals(pass_version.getString(busName.getKey().toString() + "pass_version", "sakib"))) {
//
//                            pass_version_editor = pass_version.edit();
//                            pass_version_editor.putString(busName.getKey().toString() + "pass_version", version.getKey().toString());
//                            pass_version_editor.apply();
//                            for (DataSnapshot time : version.getChildren()) {
//                                SharedPreferences.Editor preferencesEditor = password_pref.edit();
//                                preferencesEditor.putString(busName.getKey() + time.getKey(), time.getValue().toString());
//                                preferencesEditor.apply();
//
//                                password_string += (time.getKey().toString() + "#" + time.getValue().toString() + "$");
//
//                            }
//                            password_string_editor.putString(busName.getKey(), password_string);
//                            password_string_editor.apply();
//                        } else break;
//
//                    }
//
//                    if (i == child_count) {
//
//                        passbool = true;
//                        if (passek) {
//                            progress_cnt += 15;
//                            passek = false;
//                            doJob();
//                        }
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Allfetch();
//            }
//        };
//        passReference.addValueEventListener(val_pass);
//        //endregion

        //region time_schedule
        timeReference = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_time));
        val_time = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long i = 0, child_count = dataSnapshot.getChildrenCount();

                if(GlobalClass.BusNameKey!=null || GlobalClass.BusNameKey.size()!=0)
                    GlobalClass.BusNameKey.clear();


                for (DataSnapshot busName : dataSnapshot.getChildren()) {

                    ++i;
                    GlobalClass.BusNameKey.add(busName.getKey());
                    for (DataSnapshot version : busName.getChildren()) {

                        if (!version.getKey().toString().equals("1")
                                || version.getKey().toString().equals("1")) {

                            up = new intt[50];
                            down = new intt[50];
                            k = 0;
                            l = 0;

                            for (DataSnapshot time : version.getChildren()) {

                                char lastChar = time.getKey().charAt(time.getKey().length() - 1);

                                if (time.getKey().matches("(.*)up") && lastChar == 'p') {
                                    up[k] = new intt();
                                    StringTokenizer pp = new StringTokenizer(time.getKey(), " up");
                                    up[k++].z = (String) pp.nextElement();

                                } else if (time.getKey().matches("(.*)up2") && lastChar == '2') {
                                    up[k] = new intt();

                                    StringTokenizer pp = new StringTokenizer(time.getKey(), " up");
                                    up[k++].z = (String) pp.nextElement();

                                } else if (time.getKey().matches("(.*)down") && lastChar == 'n') {
                                    String tempo;
                                    down[l] = new intt();
                                    StringTokenizer pp = new StringTokenizer(time.getKey(), " down");
                                    tempo = (String) pp.nextElement();


                                    StringTokenizer stx = new StringTokenizer(tempo, ":");
                                    int a = Integer.parseInt((String) stx.nextElement());
                                    int b = Integer.parseInt((String) stx.nextElement());
                                    if (a != 12)
                                        a += 12;
                                    if (b == 0)
                                        down[l++].z = String.valueOf(a) + ":00";
                                    else
                                        down[l++].z = String.valueOf(a) + ":" + String.valueOf(b);

                                } else if (time.getKey().matches("(.*)down2") && lastChar == '2') {
                                    String tempo = "";
                                    down[l] = new intt();
                                    StringTokenizer pp = new StringTokenizer(time.getKey(), " down");
                                    tempo = (String) pp.nextElement();



                                    StringTokenizer stx = new StringTokenizer(tempo, ":");
                                    int a = Integer.parseInt((String) stx.nextElement());
                                    int b = Integer.parseInt((String) stx.nextElement());
                                    if (a != 12)
                                        a += 12;
                                    if (b == 0)
                                        down[l++].z = String.valueOf(a) + ":00";
                                    else
                                        down[l++].z = String.valueOf(a) + ":" + String.valueOf(b);

                                }


                            }

                            sorting(busName.getKey());
                        } else break;

                    }


                    if (i == child_count) {
                        timebool = true;

                        if (timeek) {
                            progress_cnt += 10;
                            timeek = false;

                            doJob();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Allfetch();
            }
        };
        timeReference.addValueEventListener(val_time);
        //endregion

        //region route
        routeReference = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_route));

        val_route = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long i = 0, child_count = dataSnapshot.getChildrenCount();


                for (DataSnapshot Bus : dataSnapshot.getChildren()) {
                    ++i;
                    for (DataSnapshot version : Bus.getChildren()) {

                        if (!version.getKey().toString().equals("1")
                                || version.getKey().toString().equals("1")) {



                            for (DataSnapshot dir : version.getChildren()) {

                                if (dir.getKey().equals("down")) {


                                    SharedPreferences.Editor preferencesEditor = route_pref.edit();
                                    preferencesEditor.putInt(Bus.getKey().toString() + "down", (int) dir.getChildrenCount());
                                    preferencesEditor.apply();
                                    for (DataSnapshot route : dir.getChildren()) {
                                        preferencesEditor.putString(Bus.getKey().toString() + "down" + route.getKey().toString(), route.getValue().toString());
                                        preferencesEditor.apply();

                                    }
                                } else if (dir.getKey().equals("up")) {
                                    SharedPreferences.Editor preferencesEditor = route_pref.edit();
                                    preferencesEditor.putInt(Bus.getKey().toString() + "up", (int) dir.getChildrenCount());
                                    preferencesEditor.apply();
                                    for (DataSnapshot route : dir.getChildren()) {
                                        preferencesEditor.putString(Bus.getKey().toString() + "up" + route.getKey().toString(), route.getValue().toString());
                                        preferencesEditor.apply();

                                    }
                                }
                            }


                        } else break;


                    }


                    if (i == child_count) {
                        routebool = true;

                        if (routeek) {
                            progress_cnt += 10;
                            routeek = false;

                            doJob();
                        }
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Allfetch();
            }
        };

        routeReference.addValueEventListener(val_route);
        //endregion

        //region stoppage
        stoppageReference = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_stoppage));

        val_stoppage = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long i = 0, child_count = dataSnapshot.getChildrenCount();
                for (DataSnapshot Bus : dataSnapshot.getChildren()) {

                    ++i;
                    for (DataSnapshot version : Bus.getChildren()) {
                        if (!version.getKey().toString().equals("1")
                                || version.getKey().toString().equals("1")) {



                            for (DataSnapshot direction : version.getChildren()) {


                                if (direction.getKey().equals("down")) {


                                    SharedPreferences.Editor preferencesEditor = stoppage_pref.edit();
                                    preferencesEditor.putInt(Bus.getKey().toString() + "down", (int) direction.getChildrenCount());
                                    preferencesEditor.apply();
                                    for (DataSnapshot route : direction.getChildren()) {

                                        preferencesEditor.putString(Bus.getKey().toString() + "down" + route.getKey().toString(), route.getValue().toString());
                                        preferencesEditor.apply();


                                    }
                                } else if (direction.getKey().equals("up")) {
                                    SharedPreferences.Editor preferencesEditor = stoppage_pref.edit();
                                    preferencesEditor.putInt(Bus.getKey().toString() + "up", (int) direction.getChildrenCount());
                                    preferencesEditor.apply();
                                    for (DataSnapshot route : direction.getChildren()) {


                                        preferencesEditor.putString(Bus.getKey().toString() + "up" + route.getKey().toString(), route.getValue().toString());
                                        preferencesEditor.apply();

                                    }
                                }
                            }

                        } else break;

                    }


                    if (i == child_count) {

                        stoppagebool = true;
                        if (stoppageek) {
                            progress_cnt += 10;
                            stoppageek = false;
                            doJob();
                        }
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Allfetch();
            }
        };

        stoppageReference.addValueEventListener(val_stoppage);
        //endregion

        //region first_login
        firstReference = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_firstLogin));
        val_first = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                SharedPreferences.Editor preferencesEditor = first_pref.edit();
                preferencesEditor.putString(getString(R.string.first_login_getter), dataSnapshot.getValue().toString());
                preferencesEditor.apply();
                preferencesEditor = first_pref.edit();
                preferencesEditor.putString("First", "First");
                preferencesEditor.apply();
                firstbool = true;

                if (firstek) {
                    firstek = false;
                    progress_cnt += 0;

                    doJob();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Allfetch();
            }


        };
        firstReference.addValueEventListener(val_first);
        //endregion

//        //region master
//        masterReference = FirebaseDatabase.getInstance().getReference()
//                .child(getString(R.string.Firebase_masterAdmin));
//        val_master = new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                String busNameKey = "";
//                String masterPassCentralAdmin = "";
//
//                for (DataSnapshot version : dataSnapshot.getChildren()) {
//
//                    if (!version.getKey().toString().equals(masterPass_version.getString("masterPass_version", "sakib"))
//                            || version.getKey().toString().equals(masterPass_version.getString("masterPass_version", "sakib"))) {
//                        masterPass_version_editor = masterPass_version.edit();
//                        masterPass_version_editor.putString("masterPass_version", version.getKey().toString());
//                        masterPass_version_editor.apply();
//                        for (DataSnapshot busName : version.getChildren()) {
//
//                            busNameKey += (busName.getKey() + "$");
//                            SharedPreferences.Editor preferencesEditor = master_pref.edit();
//                            preferencesEditor.putString(busName.getKey(), busName.getValue().toString());
//                            preferencesEditor.apply();
//
//
//                            SharedPreferences.Editor preferencesEditor2 = busName_pref.edit();
//                            preferencesEditor2.putString("BusNameKey22", busNameKey);
//                            preferencesEditor2.apply();
//
//                            masterPassCentralAdmin+=(busName.getKey().toString().trim()+"#"
//                                    +busName.getValue().toString().trim()+"$");
//
//                            SharedPreferences.Editor preferencesEditor3 = master_pass_central_admin.edit();
//                            preferencesEditor3.putString("MasterPassCentralAdmin", masterPassCentralAdmin);
//                            preferencesEditor3.apply();
//
//
//                        }
//
//
//                        masterbool = true;
//                        if (masterek) {
//                            progress_cnt += 5;
//
//                            doJob();
//                            masterek = false;
//                        }
//
//                    } else {
//                        masterbool = true;
//                        if (masterek) {
//                            progress_cnt += 5;
//                            doJob();
//                            masterek = false;
//                        }
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Allfetch();
//            }
//        };
//        masterReference.addValueEventListener(val_master);
//        //endregion

        //region Bus_schedule
        scheduleReference = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_busSchedule));
        val_schedule = new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long i = 0, child_count = dataSnapshot.getChildrenCount();

                for (DataSnapshot busName : dataSnapshot.getChildren()) {
                    ++i;
                    for (DataSnapshot version : busName.getChildren()) {
                        if (!version.getKey().toString().equals("1")
                                || version.getKey().toString().equals("1")) {

                            up2 = new intt2[50];
                            down2 = new intt2[50];
                            k1 = 0;
                            l1 = 0;

                            for (DataSnapshot time : version.getChildren()) {

                                if (time.getKey().matches("(.*)up")) {
                                    up2[k1] = new intt2();
                                    up2[k1].time = (String) time.child("time").getValue();
                                    up2[k1].from = (String) time.child("from").getValue();
                                    up2[k1].to = (String) time.child("to").getValue();
                                    up2[k1].type = (String) time.child("type").getValue();
//                                                up2[k1].bus = (String) time.child("bus").getValue();
                                    k1++;
                                } else if (time.getKey().matches("(.*)up2")) {
                                    up2[k1] = new intt2();
                                    up2[k1].time = (String) time.child("time").getValue();
                                    up2[k1].from = (String) time.child("from").getValue();
                                    up2[k1].to = (String) time.child("to").getValue();
                                    up2[k1].type = (String) time.child("type").getValue();
                                    //up2[k1].bus = (String) time.child("bus").getValue();
                                    k1++;
                                } else if (time.getKey().matches("(.*)down")) {
                                    down2[l1] = new intt2();
                                    down2[l1].time = (String) time.child("time").getValue();
                                    down2[l1].from = (String) time.child("from").getValue();
                                    down2[l1].to = (String) time.child("to").getValue();
                                    down2[l1].type = (String) time.child("type").getValue();
                                    //down2[l1].bus = (String) time.child("bus").getValue();
                                    l1++;
                                } else if (time.getKey().matches("(.*)down2")) {
                                    down2[l1] = new intt2();
                                    down2[l1].time = (String) time.child("time").getValue();
                                    down2[l1].from = (String) time.child("from").getValue();
                                    down2[l1].to = (String) time.child("to").getValue();
                                    down2[l1].type = (String) time.child("type").getValue();
                                    //down2[l1].bus = (String) time.child("bus").getValue();
                                    l1++;
                                }
                            }
                        } else break;

                        sorting2(busName.getKey());
                    }
                    if (i == child_count) {


                        schedulebool = true;
                        if (scheduleek) {

                            doJob();
                            progress_cnt += 5;


                            scheduleek = false;
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Allfetch();
            }
        };
        scheduleReference.addValueEventListener(val_schedule);
        //endregion

        //region active_checking
        activebusReference = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_admin)).child("Location");
        val_activebus = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long i = 0, child_count = dataSnapshot.getChildrenCount();


                for (DataSnapshot bus : dataSnapshot.getChildren()) {
                    ++i;

                    List<String> buslist = new ArrayList<>();
                    String name = bus.getKey();
                    String time = null;


                    for (DataSnapshot dir : bus.getChildren()) {


                        time = dir.getKey().toString();
                        for (DataSnapshot inside : dir.getChildren()) {

                            if (inside.getKey().equals("active_on") && inside.getValue().equals("on")
                                    && !time.equals("TIME")) {
                                buslist.add(time);
                                GlobalClass.activetime.put(name + time, "true");
                            } else if (inside.getKey().equals("active_on") && inside.getValue().equals("off")
                                    && !time.equals("TIME"))
                                GlobalClass.activetime.put(name + time, "false");


                        }


                    }
                    GlobalClass.activebus.put(name, buslist);
                    if (i == child_count) {


                        activebool = true;
                        if (activeek) {
                            activeek = false;

                            doJob();
                            progress_cnt += 5;
                        }
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Allfetch();
            }

        };
        activebusReference.addValueEventListener(val_activebus);
        //endregion

        //region admin_radius_aage
        admin_radius_aage = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_admin_radius_aage));
        val_admin_aage_radius = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot != null) {
                    SharedPreferences.Editor preferencesEditor = GlobalClass.admin_radius_aage_pref.edit();
                    preferencesEditor.putLong(getString(R.string.admin_radius_aage_getter), Long.valueOf(dataSnapshot.getValue().toString()));
                    preferencesEditor.apply();
                    admin_radius_aage_bool = true;

                    if (admin_radius_ek_aage) {
                        admin_radius_ek_aage = false;

                        doJob();
                        progress_cnt += 0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Allfetch();
            }
        };
        admin_radius_aage.addValueEventListener(val_admin_aage_radius);
        //endregion

        //region admin_radius_pore
        admin_radius_pore = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_admin_radius_pore));
        val_admin_pore_radius = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot != null) {
                    SharedPreferences.Editor preferencesEditor = GlobalClass.admin_radius_pore_pref.edit();
                    preferencesEditor.putLong(getString(R.string.admin_radius_pore_getter), Long.valueOf(dataSnapshot.getValue().toString()));
                    preferencesEditor.apply();

                    admin_radius_pore_bool = true;
                    if (admin_radius_ek_pore) {
                        admin_radius_ek_pore = false;

                        doJob();
                        progress_cnt += 0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Allfetch();
            }
        };
        admin_radius_pore.addValueEventListener(val_admin_pore_radius);
        //endregion

        //region admin_force_aage
        force_aage_ref = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_admin_force_out_aage));
        val_admin_force_aage = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot != null) {
                    SharedPreferences.Editor preferencesEditor = force_aage.edit();
                    preferencesEditor.putLong("forceAageGetter", Long.valueOf(dataSnapshot.getValue().toString()));
                    preferencesEditor.apply();
                    bool_force_aage = true;

                    if (ekbar_force_aage) {
                        ekbar_force_aage = false;

                        doJob();
                        progress_cnt += 0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Allfetch();
            }
        };
        force_aage_ref.addValueEventListener(val_admin_force_aage);
        //endregion

        //region admin_force_pore
        force_pore_ref = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_admin_force_out_pore));
        val_admin_force_pore = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot != null) {
                    SharedPreferences.Editor preferencesEditor = force_pore.edit();
                    preferencesEditor.putLong("forcePoreGetter", Long.valueOf(dataSnapshot.getValue().toString()));
                    preferencesEditor.apply();

                    bool_force_pore = true;
                    if (ekbar_force_pore) {
                        ekbar_force_pore = false;

                        doJob();
                        progress_cnt += 0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Allfetch();
            }
        };
        force_pore_ref.addValueEventListener(val_admin_force_pore);
        //endregion

        //region admin_aage
        admin_aage = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_aage));
        val_admin_aage = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot != null) {
                    SharedPreferences.Editor preferencesEditor = GlobalClass.admin_aage_pref.edit();
                    preferencesEditor.putLong(getString(R.string.admin_aage_getter), Long.valueOf(dataSnapshot.getValue().toString()));
                    preferencesEditor.apply();


                    admin_aage_bool = true;

                    if (admin_aage_ek) {
                        admin_aage_ek = false;

                        doJob();
                        progress_cnt += 0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Allfetch();
            }
        };
        admin_aage.addValueEventListener(val_admin_aage);
        //endregion

        //region admin_pore
        admin_pore = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_pore));
        val_admin_pore = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot != null) {
                    SharedPreferences.Editor preferencesEditor = GlobalClass.admin_pore_pref.edit();
                    preferencesEditor.putLong(getString(R.string.admin_pore_getter), Long.valueOf(dataSnapshot.getValue().toString()));
                    preferencesEditor.apply();

                    admin_pore_bool = true;
                    if (admin_pore_ek) {
                        admin_pore_ek = false;

                        doJob();
                        progress_cnt += 0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Allfetch();
            }
        };
        admin_pore.addValueEventListener(val_admin_pore);
        //endregion

    }

    private void doJob() {

        if (timebool && stoppagebool && routebool && schedulebool
                && firstbool && datebool  && activebool && admin_radius_aage_bool
                && admin_aage_bool && admin_pore_bool && admin_radius_pore_bool
        && bool_force_aage && bool_force_pore ) {
            if (overallek) {
                overallek = false;
                doWork();

                Intent intent = new Intent(Splashscreen.this, StartActiviy.class);
                startActivity(intent);
                finish();
            }

        }
    }

    private void doWork() {
        if (dateReference != null && val_date != null)
            dateReference.removeEventListener(val_date);

        if (routeReference != null && val_route != null)
            routeReference.removeEventListener(val_route);
        if (firstReference != null && val_first != null)
            firstReference.removeEventListener(val_first);

        if (timeReference != null && val_time != null)
            timeReference.removeEventListener(val_time);
        if (scheduleReference != null && val_schedule != null)
            scheduleReference.removeEventListener(val_schedule);
        if (stoppageReference != null && val_stoppage != null)
            stoppageReference.removeEventListener(val_stoppage);
        if (activebusReference != null && val_activebus != null)
            activebusReference.removeEventListener(val_activebus);
        if (admin_radius_aage != null && val_admin_aage_radius != null)
            admin_radius_aage.removeEventListener(val_admin_aage_radius);
        if (admin_radius_pore != null && val_admin_pore_radius != null)
            admin_radius_pore.removeEventListener(val_admin_pore_radius);
        if (admin_aage != null && val_admin_aage != null)
            admin_aage.removeEventListener(val_admin_aage);
        if (admin_pore != null && val_admin_pore != null)
            admin_pore.removeEventListener(val_admin_pore);
        if (val_timestamp != null && receivefromTimeStamp != null)
            receivefromTimeStamp.removeEventListener(val_timestamp);
        if(val_admin_force_aage!=null && force_aage_ref!=null)
            force_aage_ref.removeEventListener(val_admin_force_aage);
        if(val_admin_force_pore!=null && force_pore_ref!=null)
            force_pore_ref.removeEventListener(val_admin_force_pore);



    }

    private void networkAlert() {

        if (sleep == 4000) {
            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {

                    doWork();

                    startActivity(new Intent(Splashscreen.this, StartActiviy.class));
                    finish();

                }
            }, sleep);

            String s;
            s = busName_pref.getString("BusNameKey22", "");


            StringTokenizer stx = new StringTokenizer(s, "$");
            while (stx.hasMoreElements()) {

                GlobalClass.BusNameKey.add((String) stx.nextElement());
            }



            toastIconInfo("You are in offline mode");
        } else {
            //region builder
            builder = new AlertDialog.Builder(Splashscreen.this);

            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle("No internet !!");
            builder.setMessage("Launching the app for the first time requires data synchronization." +
                    "Internet connection is needed.");
            //endregion

            builder.setNegativeButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (isNetworkConnected(Splashscreen.this)) {

                        final Timer t = new Timer();
                        TimerTask tt = new TimerTask() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void run() {

                                if (Build.VERSION.SDK_INT > 23)
                                    pb.setProgress(progress_cnt, true);
                                else
                                    pb.setProgress(progress_cnt);
                                if (progress_cnt == 100)
                                    t.cancel();
                            }

                        };
                        t.schedule(tt, 0, 150);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Allfetch();

                            }
                        }, 3000);


                    } else {
                        networkAlert();
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }


    }

    private void toastIconInfo(String purpose) {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(purpose);
        ((CardView) custom_view.findViewById(R.id.parent_view2)).setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));

        toast.setView(custom_view);
        toast.show();
    }


    public void onUpdateNeeded(final String updateUrl) {


        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please update your app to new version .")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).create();
        dialog.show();
        dialog.setCancelable(false);
    }

    private void redirectStore(String updateUrl) {

        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void sorting(String name) {

        SharedPreferences.Editor repeatTimeEditor = repeatTime_pref.edit();

        temp = "";
        Arrays.sort(up, 0, k, new Cmp());
        for (i = 0; i < k; ++i) {
            if (i > 0 && up[i].z.equals(up[i - 1].z)) {
                temp += (up[i].z + " up2");
                repeatTimeEditor.putString(name + up[i].z + " up2", "1");
                repeatTimeEditor.apply();
                temp += "$";

            } else {
                temp += (up[i].z + " up");
                repeatTimeEditor.putString(name + up[i].z + " up", "1");
                repeatTimeEditor.apply();
                temp += "$";

            }


        }
        Arrays.sort(down, 0, l, new Cmp());
        for (i = 0; i < l; ++i) {
            String tempo1, tempo2;

            boolean checkFlag = false;
            if (i > 0 && down[i].z.equals(down[i - 1].z)) {
                checkFlag = true;
            }
            System.out.print(" " + down[i].z);
            StringTokenizer stx = new StringTokenizer(down[i].z, ":");
            int a = Integer.parseInt((String) stx.nextElement());
            int b = Integer.parseInt((String) stx.nextElement());
            if (a > 12) {
                a -= 12;
                tempo1 = String.valueOf(a);

            } else tempo1 = String.valueOf(a);
            if (b == 0)
                tempo2 = ":00";
            else if (b < 10)
                tempo2 = ":0" + b;
            else
                tempo2 = ":" + String.valueOf(b);
            if (checkFlag) {
                temp += (tempo1 + tempo2 + " down2");
                repeatTimeEditor.putString(name + tempo1 + tempo2 + " down2", "1");
                repeatTimeEditor.apply();
                temp += "$";
            } else {
                temp += (tempo1 + tempo2 + " down");
                repeatTimeEditor.putString(name + tempo1 + tempo2 + " down", "1");
                repeatTimeEditor.apply();
                temp += "$";
            }

        }

        SharedPreferences.Editor preferencesEditor = time_pref.edit();
        preferencesEditor.putString(name, temp);
        preferencesEditor.apply();

    }

    private void sorting2(String name) {
        temp2 = "";
        Arrays.sort(up2, 0, k1, new Cmp2());
        for (i = 0; i < k1; ++i) {
            temp2 += (up2[i].time + "#");
            temp2 += (up2[i].from + "#");
            temp2 += (up2[i].to + "#");
            temp2 += (up2[i].type + "$");
            //temp2 += (up2[i].bus + "$");
        }
        Arrays.sort(down2, 0, l1, new Cmp2());
        for (i = 0; i < l1; ++i) {
            temp2 += (down2[i].time + "#");
            temp2 += (down2[i].from + "#");
            temp2 += (down2[i].to + "#");
            temp2 += (down2[i].type + "$");
        }

        SharedPreferences.Editor preferencesEditor = schedule_pref.edit();
        preferencesEditor.putString(name, temp2);
        preferencesEditor.apply();

    }

    class Cmp implements Comparator<intt> {
        public int compare(intt x, intt y) {

            String strx = x.z, stry = y.z;
            StringTokenizer stx = new StringTokenizer(strx, ":");
            int a = Integer.parseInt((String) stx.nextElement());

            int b = Integer.parseInt((String) stx.nextElement());
            StringTokenizer sty = new StringTokenizer(stry, ":");
            int c = Integer.parseInt((String) sty.nextElement());

            int d = Integer.parseInt((String) sty.nextElement());

            if (a == c) {
                if (b == d) return 0;
                if (b < d) return -1;
                else return 1;
            } else if (a < c) return -1;
            else return 1;

        }
    }

    class Cmp2 implements Comparator<intt2> {
        public int compare(intt2 x, intt2 y) {

            String strx = x.time, stry = y.time;
            int a = 0, b = 0, c = 0, d = 0;
            if (x.time.matches("(.*)down")) {
                StringTokenizer ppx = new StringTokenizer(strx, " down");
                String tempox = (String) ppx.nextElement();
                StringTokenizer stx = new StringTokenizer(tempox, ":");
                a = Integer.parseInt((String) stx.nextElement());
                b = Integer.parseInt((String) stx.nextElement());
                if (a != 12)
                    a += 12;
                StringTokenizer ppy = new StringTokenizer(stry, " down");
                String tempoy = (String) ppy.nextElement();
                StringTokenizer sty = new StringTokenizer(tempoy, ":");
                c = Integer.parseInt((String) sty.nextElement());
                if (c != 12)
                    c += 12;
                d = Integer.parseInt((String) sty.nextElement());

            } else if (x.time.matches("(.*)down2")) {
                StringTokenizer ppx = new StringTokenizer(strx, " down");
                String tempox = (String) ppx.nextElement();
                StringTokenizer stx = new StringTokenizer(tempox, ":");
                a = Integer.parseInt((String) stx.nextElement());
                b = Integer.parseInt((String) stx.nextElement());
                if (a != 12)
                    a += 12;
                StringTokenizer ppy = new StringTokenizer(stry, " down");
                String tempoy = (String) ppy.nextElement();
                StringTokenizer sty = new StringTokenizer(tempoy, ":");
                c = Integer.parseInt((String) sty.nextElement());
                if (c != 12)
                    c += 12;
                d = Integer.parseInt((String) sty.nextElement());

            } else if (x.time.matches("(.*)up")) {
                StringTokenizer ppx = new StringTokenizer(strx, " up");
                String tempox = (String) ppx.nextElement();

                StringTokenizer stx = new StringTokenizer(tempox, ":");
                a = Integer.parseInt((String) stx.nextElement());

                b = Integer.parseInt((String) stx.nextElement());

                StringTokenizer ppy = new StringTokenizer(stry, " up");
                String tempoy = (String) ppy.nextElement();

                StringTokenizer sty = new StringTokenizer(tempoy, ":");
                c = Integer.parseInt((String) sty.nextElement());

                d = Integer.parseInt((String) sty.nextElement());


            } else if (x.time.matches("(.*)up2")) {
                StringTokenizer ppx = new StringTokenizer(strx, " up");
                String tempox = (String) ppx.nextElement();
                StringTokenizer stx = new StringTokenizer(tempox, ":");

                a = Integer.parseInt((String) stx.nextElement());

                b = Integer.parseInt((String) stx.nextElement());

                StringTokenizer ppy = new StringTokenizer(stry, " up");
                String tempoy = (String) ppy.nextElement();

                StringTokenizer sty = new StringTokenizer(tempoy, ":");
                c = Integer.parseInt((String) sty.nextElement());

                d = Integer.parseInt((String) sty.nextElement());


            }

            if (a == c) {
                if (b == d) return 0;
                if (b < d) return -1;
                else return 1;
            } else if (a < c) return -1;
            else return 1;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser user) {

        if (user == null) {
            mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else
                        updateUI(null);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            GlobalClass.currentlat = location.getLatitude();
                            GlobalClass.currentlon = location.getLongitude();
                        }
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    public static boolean isNetworkConnected(Splashscreen context) {

        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = true;
                    }
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        result = true;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        result = true;
                    }
                }
            }
        }
        if (result) flag = true;
        return result;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng jigatala = new LatLng(23.738487, 90.373503);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jigatala, 15f));

    }
}

class intt {
    public String z;
}

class intt2 {
    public String time, from, to, type;
}

