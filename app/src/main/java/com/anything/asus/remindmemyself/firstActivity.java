package com.anything.asus.remindmemyself;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class firstActivity extends AppCompatActivity implements View.OnClickListener,
        OnMapReadyCallback, GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener,
        NavigationView.OnNavigationItemSelectedListener {

    //region variable
    public static int interval = 5000, fastinterval = 2000, xxx = 0, countMarker = 0, q_profile_index = -1,
            locBus = 0, k, l, k1, l1, i, count = 0, call = 0, listSize = 0, p_profile_index = -1;
    private DrawerLayout drawer;

    public static long tripnumber = 0, up_trip_number = 0, down_trip_number = 0;

    static long[] pattern = {0, 1000, 1000, 1000, 1000};

    public static feed_class[] feed_array = null;

    public static int feed_count = 0;

    Toolbar toolbar;

    public static Dialog dialog22 = null, dialog23 = null;

    ArrayList<String> time = new ArrayList<String>();

    ProgressDialog progressDialog22;


    public static ArrayList<String> active_top10_mobileNo = new ArrayList<String>();
    public static ArrayList<String> active_top10_time = new ArrayList<String>();
    private GoogleMap mMap = null;
    private ProgressDialog progressDialog;
    public static float zoom = 15f, dis;
    public boolean freeze = true, prechecking = false, destroy = true;
    boolean trip_increment=false,up_trip_increment=false, down_trip_increment=false;
    int height, width;
    public String temp, temp2, customPass, date;
    public long blinking = 0;
    public static PopupWindow popupWindow, popupWindowDogs;
    public static Location centerLocation = null, adminLocation;
    public Button buttonBus, buttonTime, buttonSearch;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private FloatingActionButton fab, fabt;
    private Location lastlocation;
    public profile_class[] p_class = null, q_class = null;
    private Menu menu;
    boolean datebool = false, firstbool = false, activebool = false, schedulebool = false, timebool = false, passbool = false,
            masterbool = false, routebool = false, stoppagebool = false;

    boolean dateek = true, firstek = true, activeek = true, scheduleek = true, timeek = true, passek = true,
            masterek = true, routeek = true, stoppageek = true;

    final HandlerThread handlerThread = new HandlerThread("RequestLocation");
    private SharedPreferences.Editor preEditor, pre1Editor;
    public static SharedPreferences.Editor forceBuseditor, forceTimeeditor;
    private static LatLng currentLatLng = null, aftercameramove, busLocation;
    public static boolean oncealarm = false, flag = true, admin = false,gotoNextActivity=false,
            user = false,removeMarker=true,searchEkbar=false,ekbarCheck=true,
            finalLocation = false, alarmflag = true, Bool_busbtn = false, Bool_timbtn = false;
    public static Marker marker;
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private ImageView img;
    private TextView tv, customtextView;
    private Uri uri;
    private EditText customeditText;
    private LocationManager lm;
    private Ringtone defaultRingtone;
    private static Vibrator myVib;
    private static AudioManager am;
    private AlertDialog.Builder dialog;
    private AlertDialog alert;
    private LinearLayout linearLayout;
    public static String adminselectionBus, adminselectionTime, userselectionBus, userselectionTime, busNumber;
    private FirebaseAuth mAuth;
    public static Context context;

    private  Snackbar snackbar=null;

    private LinearLayout parent_view;

    public LatLng finalBusLocation;

    public static intt[] up = null;
    public static intt[] down = null;
    public static intt2[] up2 = null;
    public static intt2[] down2 = null;

    OnCompleteListener turnoff_listener = null;

    public static DatabaseReference ref = null, dateReference = null, adminCloseReferenceForce = null,
            routeReference = null, scheduleReference = null, firstReference = null,
            stoppageReference = null,
            timeReference = null, passReference = null, masterReference = null, change = null,
            activebusReference = null, forget_enter = null, trip_countRef = null,
            active_for_top10 = null,
            adminref = null, top10 = null, feedReference = null, trip_regain = null,
            busNumberReference,admin_feedback=null;
    DogsDropdownOnItemClickListener dogsDropdownOnItemClickListener = new DogsDropdownOnItemClickListener();
    BusAndTime busAndTime;

    private String appVersionString="tauhidsakib";

    public SharedPreferences pass_version, time_version, route_version, stoppage_version,
            masterPass_version, bus_schedule_version,bus_number_pref;
    public static SharedPreferences trip_s,up_trip_s,down_trip_s;

    public SharedPreferences.Editor pass_version_editor, time_version_editor, route_version_editor,
            stoppage_version_editor, masterPass_version_editor, bus_schedule_version_editor;


    public SharedPreferences schedulePref, busNamePref, busTimePref, date_pref,
            route_pref, time_pref, schedule_pref, password_pref, first_pref,
            master_pref, stoppage_pref, repeatTime_pref, busName_pref;

    public ValueEventListener val_time = null, val_schedule = null, val_pass = null, val_master = null,
            val_trip_regain = null, val_first = null, val_route = null, val_date = null, listenerForce = null,
            listener = null, val_stoppage = null, val_activebus = null, val_top10 = null, val_feed_back = null,
            val_trip = null, val_active_for_top10 = null;


    //endregion

    @SuppressLint("ResourceAsColor")
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        context = this;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        date_pref = getSharedPreferences(getString(R.string.date_pref_file), MODE_PRIVATE);
        date = date_pref.getString(getString(R.string.date_getter), "sakib25");

        forget_enter = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_admin)).child("Location");
        active_for_top10 = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_adminRoster)).child(date);

        top10 = FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_admin_profile));

        trip_increment=true;
        up_trip_increment=true;
        down_trip_increment=true;

        parent_view=findViewById(R.id.parent_view);

        trip_s=getSharedPreferences("trip_pref",MODE_PRIVATE);
        up_trip_s=getSharedPreferences("up_trip_pref",MODE_PRIVATE);
        down_trip_s=getSharedPreferences("down_trip_pref",MODE_PRIVATE);

        GlobalClass.TripEditor= trip_s.edit();
        GlobalClass.upTripEditor=up_trip_s.edit();
        GlobalClass.downTripEditor=down_trip_s.edit();


        fetchData();

        //region db node version
        pass_version = getSharedPreferences(getString(R.string.passVersion_pref_file), MODE_PRIVATE);
        time_version = getSharedPreferences(getString(R.string.timeVersion_pref_file), MODE_PRIVATE);
        route_version = getSharedPreferences(getString(R.string.routeVersion_pref_file), MODE_PRIVATE);
        stoppage_version = getSharedPreferences(getString(R.string.stoppageVersion_pref_file), MODE_PRIVATE);
        masterPass_version = getSharedPreferences(getString(R.string.masterVersion_pref_file), MODE_PRIVATE);
        bus_schedule_version = getSharedPreferences(getString(R.string.scheduleVersion_pref_file), MODE_PRIVATE);
        //endregion

        //region bus, time, search click
        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.busBtn:

                        popupWindowDogs = popupWindowDogs(v);
                        popupWindowDogs.showAsDropDown(v);

                        break;

                    case R.id.timeBtn:

                        // show the list view as dropdown
                        if (buttonBus.getText().equals("BUS"))
                            toastIconInfo("Select the bus name");
                        else {
                            popupWindowDogs = popupWindowDogs(v);
                            popupWindowDogs.showAsDropDown(v);
                        }
                        break;

                    case R.id.searchBtn:
                        if (!isNetworkConnected(firstActivity.this)) {

                            toastIconInfo("Internet connection required");


                            return;
                        }
                        removeMarker=true;
                        searchEkbar=true;
                        searchBus();
                        break;
                }
            }
        };
        //endregion

        // region our view
        tv = findViewById(R.id.mywidget);
        buttonBus = (Button) findViewById(R.id.busBtn);
        buttonTime = (Button) findViewById(R.id.timeBtn);
        buttonSearch = (Button) findViewById(R.id.searchBtn);
        buttonBus.setOnClickListener(handler);
        buttonTime.setOnClickListener(handler);
        buttonSearch.setOnClickListener(handler);
        //endregion

        //region sharedpreferences
        busNamePref = getSharedPreferences(getString(R.string.bus_name_pref_file), MODE_PRIVATE);
        busTimePref = getSharedPreferences(getString(R.string.bus_time_pref_file), MODE_PRIVATE);
        time_pref = getSharedPreferences(getString(R.string.time_pref_file), MODE_PRIVATE);
        schedule_pref = getSharedPreferences(getString(R.string.schedule_pref_file), MODE_PRIVATE);
        password_pref = getSharedPreferences(getString(R.string.pass_pref_file), MODE_PRIVATE);
        first_pref = getSharedPreferences(getString(R.string.first_pref_file), MODE_PRIVATE);
        route_pref = getSharedPreferences(getString(R.string.route_pref_file), MODE_PRIVATE);
        date_pref = getSharedPreferences(getString(R.string.date_pref_file), MODE_PRIVATE);
        master_pref = getSharedPreferences(getString(R.string.master_pref_file), MODE_PRIVATE);
        repeatTime_pref = getSharedPreferences(getString(R.string.repeatTime_pref_file), MODE_PRIVATE);
        schedulePref = getSharedPreferences(getString(R.string.schedule_pref_file), MODE_PRIVATE);
        stoppage_pref = getSharedPreferences(getString(R.string.stoppage_pref_file), MODE_PRIVATE);
        busName_pref = getSharedPreferences(getString(R.string.busName_pref_file), MODE_PRIVATE);
        //endregion

        handlerThread.start();

        //region oncreate declarations
        centerLocation = new Location("");
        adminLocation = new Location("");
        progressDialog = new ProgressDialog(firstActivity.this);
        linearLayout = findViewById(R.id.linear);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        img = (ImageView) findViewById(R.id.iconid);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(firstActivity.this,
                drawer,
                toolbar,
                R.string.nav_open_drawer,
                R.string.nav_close_drawer)
        {
            @Override
            public void onDrawerClosed(View view) {


            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if(snackbar!=null)
                {
                    if(snackbar.isShown())
                        snackbar.dismiss();
                }
            }


        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        fab = (FloatingActionButton) findViewById(R.id.fabloc);
        fabt = (FloatingActionButton) findViewById(R.id.fabtraffic);
        fabt.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.traffic));
        fab.setOnClickListener(this);
        fabt.setOnClickListener(this);


        if (ContextCompat.checkSelfPermission(firstActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(firstActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(firstActivity.this);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        //endregion

        // region admin
        if ((getIntent().getStringExtra("name")).equals("admin")) {

            GlobalClass.admin_own_pic=true;


            if (OTPactivity.trip_increment)
                trip_regain_check();

            final SharedPreferences busNumber = getSharedPreferences("helloBusNumber", MODE_PRIVATE);

            final Handler handler44 = new Handler();
            handler44.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (OTPactivity.OTPvisited) {
                        busNumberReference.child(GlobalClass.BusName)
                                .child(GlobalClass.BusTime).child("BusNumber").
                                setValue(busNumber.getString("BusNumber", "Not provided"));
                    }

                }
            }, 1000);

            GlobalClass.ContactPref = getSharedPreferences(getString(R.string.globalclass_contact_name_pref_file), MODE_PRIVATE);

            String Number = GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter), "");


            trip_countRef = FirebaseDatabase.getInstance().getReference()
                    .child(getString(R.string.Firebase_admin_profile)).child(GlobalClass.BusName)
                    .child(Number);


            feedReference = FirebaseDatabase.getInstance().getReference()
                    .child(getString(R.string.Firebase_admins_feedback))
                    .child(Number);

            fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_bus));
            adminselectionBus = getIntent().getStringExtra("BUSNAME");
            adminselectionTime = getIntent().getStringExtra("TIME");
            tv.setText("Your are in admin mode. " + "              " + " You are in admin mode.");
            tv.setSelected(true);
            this.setTitle(adminselectionBus + " " + adminselectionTime);
            admin = true;
            forget_enter.child(adminselectionBus).child(adminselectionTime).child("active_on").setValue("on");

            menu.findItem(R.id.nav_busReminder).setVisible(false);
            //menu.findItem(R.id.nav_adminProfile2).setVisible(false);
            menu.findItem(R.id.nav_locationAlarm).setVisible(false);
            menu.findItem(R.id.nav_settings).setVisible(false);
            linearLayout.setVisibility(View.GONE);

            feed_array = new feed_class[5000];
            feed_count = 0;

            final Handler handler22 = new Handler();
            handler22.postDelayed(new Runnable() {
                @Override
                public void run() {

                    val_trip = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.getKey().equals("trip")) {
                                    GlobalClass.TripEditor.putLong(getString(R.string.trip_name_getter), Long.parseLong(dataSnapshot1.getValue().toString()));
                                    GlobalClass.TripEditor.apply();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };

                    if (trip_countRef != null)
                        trip_countRef.addValueEventListener(val_trip);


                }
            }, 1000);


        }
        //endregion

        // region user
        if ((getIntent().getStringExtra("name")).equals("user")) {

            if (isNetworkConnected(this))
            {
                fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_bus));
            }
            else if (!isNetworkConnected(this)) {

                fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_location));
            }

            tv.setVisibility(View.GONE);
            img.setVisibility(View.GONE);
            user = true;
            if (isNetworkConnected(this)) {
                if (GlobalClass.first == 0)
                {
                    fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_location));

                }
                else
                {

                    fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_location));

                }

            }

            menu.findItem(R.id.nav_my_review).setVisible(false);

            userselectionBus = getIntent().getStringExtra("BUSNAME");
            userselectionTime = getIntent().getStringExtra("TIME");


            if (GlobalClass.title) {

                buttonBus.setText(userselectionBus);
                buttonTime.setText(userselectionTime);
                if (!isNetworkConnected(this))
                    this.setTitle(userselectionBus + " " + userselectionTime);
                else
                    this.setTitle(userselectionBus + " " + userselectionTime);
            } else {
                if (isNetworkConnected(this))
                    this.setTitle("Lal Bus");
                else
                    this.setTitle("Lal Bus");
                buttonBus.setText("BUS");
                buttonTime.setText("TIME");
            }

            //region user data reception
            ref = FirebaseDatabase.getInstance().getReference();

            menu.findItem(R.id.nav_turnoff).setVisible(false);
            //menu.findItem(R.id.nav_adminProfile2).setVisible(true);
            menu.findItem(R.id.nav_settings).setVisible(false);
            menu.findItem(R.id.nav_editProfile).setVisible(false);

            userDataReception();





            //endregion

        }
        //endregion


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        //region admin data transmission
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                adminref = FirebaseDatabase.getInstance().getReference();
                adminCloseReferenceForce = FirebaseDatabase.getInstance().getReference();
                lastlocation = locationResult.getLastLocation();

                ((GlobalClass) firstActivity.this.getApplication()).lat = lastlocation.getLatitude();
                ((GlobalClass) firstActivity.this.getApplication()).lon = lastlocation.getLongitude();

                ((GlobalClass) firstActivity.this.getApplication()).currentlat = lastlocation.getLatitude();
                ((GlobalClass) firstActivity.this.getApplication()).currentlon = lastlocation.getLongitude();

                currentLatLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());

                if (admin) {

                    if (finalLocation && GlobalClass.adminBackekbar) {

                        GlobalClass.signal = true;
                        GlobalClass.adminBackekbar = false;
                        Intent intent = new Intent(firstActivity.this, AdminBackground.class);
                        intent.putExtra("BusName", adminselectionBus);
                        intent.putExtra("BusTime", adminselectionTime);
                        startService(intent);

                    }


                    listenerForce = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot != null) {
                                if (dataSnapshot.getValue().toString().equals("out")) {

                                    toastIconInfo("You are terminated from admin mode");


                                    terminationProcess();
                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    adminCloseReferenceForce
                            .child(getString(R.string.Firebase_admin)).child("Location")
                            .child(adminselectionBus)
                            .child(adminselectionTime).child("force")
                            .addValueEventListener(listenerForce);


                }
            }
        };
        //endregion

        createLocationRequest();

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


    private void custom_edit_profile_reminder() {
        final Dialog dialog = new Dialog(firstActivity.this);
        dialog.setContentView(R.layout.edit_reminder_custom_layout);

        TextView name = dialog.findViewById(R.id.textNameRem);
        name.setText(GlobalClass.NamePref.
                getString(getString(R.string.profile_name_getter), "Not provided"));

        Button btnYes = dialog.findViewById(R.id.buttonGOT);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                dialog.cancel();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setAttributes(lp);


        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private void feedback_retreive() {
        val_feed_back = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                feed_count = 0;

                for (DataSnapshot id : dataSnapshot.getChildren()) {
                    feed_array[feed_count] = new feed_class();
                    feed_array[feed_count].feed_class_id = id.getKey().toString();

                    for (DataSnapshot vitor : id.getChildren()) {

                        if (vitor.getKey().equals("date")) {
                            feed_array[feed_count
                                    ].feed_class_date = vitor.getValue().toString();
                        }
                        if (vitor.getKey().equals("text")) {
                            feed_array[feed_count].feed_class_text = vitor.getValue().toString();

                        }

                    }
                    feed_count++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        if (feedReference != null)
            feedReference.orderByKey().limitToLast(500).addListenerForSingleValueEvent(val_feed_back);
    }

    private void trip_regain_check() {

        trip_regain = OTPactivity.admin_profile;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                val_trip_regain = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (ds.getKey().equals("trip")) {


                                    if (trip_increment) {
                                        trip_increment=false;
                                        tripnumber = (long) ds.getValue();
                                        GlobalClass.TripEditor.putLong(getString(R.string.trip_name_getter), tripnumber);
                                        GlobalClass.TripEditor.apply();
                                        saveAdminInfo_into_db();
                                    }

                                }
                                if (ds.getKey().equals("up_trip")) {

                                    if (up_trip_increment) {
                                        up_trip_increment=false;
                                        up_trip_number = (long) ds.getValue();

                                        GlobalClass.upTripEditor.putLong(getString(R.string.up_trip_name_getter), up_trip_number);
                                        GlobalClass.upTripEditor.apply();
                                        saveAdminInfo_into_db();

                                    }

                                }

                                if (ds.getKey().equals("down_trip") ) {

                                    if (down_trip_increment) {

                                        down_trip_increment=false;
                                        down_trip_number = (long) ds.getValue();


                                        GlobalClass.downTripEditor.putLong(getString(R.string.down_trip_name_getter), down_trip_number);
                                        GlobalClass.downTripEditor.apply();
                                        saveAdminInfo_into_db();
                                    }

                                }
                            }
                        }

                        else saveAdminInfo_into_db();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                trip_regain.addValueEventListener(val_trip_regain);

            }
        }, 2000);


    }

    private void saveAdminInfo_into_db() {

        if (trip_regain != null && val_trip_regain != null)
            trip_regain.removeEventListener(val_trip_regain);
        //region admin profile setup in db
        SharedPreferences imageURL=getSharedPreferences(getString(R.string.imageURL_pref_file),MODE_PRIVATE);


        OTPactivity.admin_profile.child("name").setValue(GlobalClass.NamePref.
                getString(getString(R.string.profile_name_getter), "Not provided"));
        OTPactivity.admin_profile.child("dept").setValue(GlobalClass.DeptPref.
                getString(getString(R.string.dept_name_getter), "Not provided"));
        OTPactivity.admin_profile.child("email").setValue(GlobalClass.MailPref.
                getString(getString(R.string.mail_name_getter), "Not provided"));
        OTPactivity.admin_profile.child("fbId").setValue(GlobalClass.FbPref.
                getString(getString(R.string.fb_name_getter), "Not provided"));

        SharedPreferences admins_busname=getSharedPreferences("admins_busname",MODE_PRIVATE);
        int count_no=admins_busname.getInt("admins_busname"+GlobalClass.BusName,0);

        if(!imageURL.getString("imageURL","painai").equals("painai"))
        {
            OTPactivity.admin_profile.child("imageURL").
                    setValue(imageURL.getString("imageURL",""));

        }
        else
        {
            OTPactivity.admin_profile.child("imageURL").setValue("Not provided");

        }




        if (count_no==1) {
            OTPactivity.admin_profile.child("down_trip").setValue(0);
            OTPactivity.admin_profile.child("up_trip").setValue(0);
            OTPactivity.admin_profile.child("trip").setValue(0);
        }

//        else
//        {
//            if(GlobalClass.BusTime.matches("(.*)down(.*)"))
//            OTPactivity.admin_profile.child("down_trip").setValue(down_trip_number+1);
//            else if(GlobalClass.BusTime.matches("(.*)up(.*)"))
//            OTPactivity.admin_profile.child("up_trip").setValue(up_trip_number+1);
//            OTPactivity.admin_profile.child("trip").setValue(tripnumber+1);
//        }

        SharedPreferences picStringinfo = getSharedPreferences("picStringinfo", MODE_PRIVATE);
        OTPactivity.admin_profile.child("picString").setValue(picStringinfo.getString("picStringinfo", "painai"));
        OTPactivity.admin_profile.child("feed_contact").setValue(GlobalClass.ContactPref.
                getString(getString(R.string.contact_name_getter), "Not provided"));
        OTPactivity.admin_profile.child("last_accompanied").setValue(date);


        if (OTPactivity.contact_show.getString(getString(R.string.contact_show_name_getter), "zzz").equals("true")) {
            OTPactivity.admin_profile.child("contact").setValue(GlobalClass.ContactPref
                    .getString(getString(R.string.contact_name_getter), "Not provided"));

        } else
            OTPactivity.admin_profile.child("contact").setValue("Not provided");
        //endregion
    }

    public static void savingTripsFinally() {

        if (OTPactivity.admin_history_ref != null && OTPactivity.val_admin_history != null)
            OTPactivity.admin_history_ref.removeEventListener(OTPactivity.val_admin_history);

        if (GlobalClass.up_bool_from_admin_background) {
            GlobalClass.up_bool_from_admin_background = false;



           if(OTPactivity.trip_increment && OTPactivity.up_increment)
           {
               up_trip_number =up_trip_s.getLong("up_trip_name",0)+ 1;
               tripnumber = trip_s.getLong("trip_name",0)+ 1;
           }



        } else if (GlobalClass.down_bool_from_admin_background) {
            GlobalClass.down_bool_from_admin_background = false;


            if(OTPactivity.trip_increment && OTPactivity.down_increment)
            {
                down_trip_number = down_trip_s.getLong("down_trip_name",0)+ 1;
                tripnumber = trip_s.getLong("trip_name",0)+ 1;
            }


        }
        GlobalClass.TripEditor.putLong("trip_name", tripnumber);
        GlobalClass.TripEditor.apply();
        GlobalClass.upTripEditor.putLong("up_trip_name", up_trip_number);
        GlobalClass.upTripEditor.apply();
        GlobalClass.downTripEditor.putLong("down_trip_name", down_trip_number);
        GlobalClass.downTripEditor.apply();

        saveAdminTripAndHistory_into_db();
    }

    private static void saveAdminTripAndHistory_into_db() {



        OTPactivity.admin_profile.child("trip").setValue(tripnumber);
        if( GlobalClass.BusTime.matches("(.*)up(.*)"))
        {
            OTPactivity.admin_profile.child("up_trip").setValue(up_trip_number);

        }
        else if( GlobalClass.BusTime.matches("(.*)down(.*)"))
        {
            OTPactivity.admin_profile.child("down_trip").setValue(down_trip_number);

        }

    }

    private void searchBus() {




        gotoNextActivity=true;
        GlobalClass.title = true;

        toolbar.setTitle(GlobalClass.BoxBusName+" "+GlobalClass.BoxBusTime);

        if (buttonTime.getText().equals("TIME")) {
            toastIconInfo("Select the bus time");
            return;
        }

        ref = FirebaseDatabase.getInstance().getReference();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    String value = dataSnapshot.child("active_on").getValue().toString();

                    if (value.equals("off")) {

                        if(marker!=null)
                        {
                            marker.remove();
                            removeMarker=true;

                        }

                        gotoNextActivity=false;

                        customSnackber();
                        return;

                    } else {
                        GlobalClass.first = 1;
                        if (!GlobalClass.BoxBusName.equals(GlobalClass.PreBusName)) {

                            GlobalClass.PreBusName = GlobalClass.BoxBusName;
                            Bool_busbtn = true;
                        }
                        if (!GlobalClass.BoxBusTime.equals(GlobalClass.preBusTime)) {
                            GlobalClass.preBusTime = GlobalClass.BoxBusTime;
                            Bool_timbtn = true;
                        }

//                        if (Bool_busbtn || Bool_timbtn) {
//                            Bool_timbtn = false;
//                            Bool_busbtn = false;
//                        } else return;

                        if(searchEkbar && gotoNextActivity)
                        {

                            searchEkbar=false;
                            GlobalClass.pp = 1;
                            locBus = 0;
                            destroy = false;

                            if (ref != null && listener != null)
                                ref.removeEventListener(listener);
                            if (activebusReference != null && val_activebus != null)
                                activebusReference.removeEventListener(val_activebus);

                            GlobalClass.userModeFetchData = false;
                            Intent intent = new Intent(firstActivity.this, firstActivity.class);
                            intent.putExtra("name", "user");
                            intent.putExtra("BUSNAME", GlobalClass.BoxBusName);
                            intent.putExtra("TIME", GlobalClass.BoxBusTime);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                searchBus();
            }
        };

        ref.child(getString(R.string.Firebase_admin)).child("Location")
                .child(GlobalClass.BoxBusName)
                .child(GlobalClass.BoxBusTime)
                .addValueEventListener(listener);
    }

    private void customSnackber() {

         snackbar = Snackbar.make(parent_view, "", Snackbar.LENGTH_INDEFINITE);
        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.snackbar_layout, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);
        (custom_view.findViewById(R.id.tv_undo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        snackBarView.addView(custom_view, 0);
        snackbar.show();


    }

    private void changeStatus() {
        GlobalClass.ekbarOff = false;
        change = FirebaseDatabase.getInstance().getReference();
        change.child(getString(R.string.Firebase_admin)).child("Location")
                .child(adminselectionBus)
                .child(adminselectionTime).child("active_on")
                .setValue("off");
        terminationProcess();

    }

    private void createLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(interval);
        locationRequest.setFastestInterval(fastinterval);

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

        SharedPreferences appversion=getSharedPreferences("helloAppVersion",MODE_PRIVATE);
        appVersionString= appversion.getString("AppVersion","tauhidsakib");


        ekbarCheck=true;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                admin_feedback=FirebaseDatabase.getInstance().getReference();


            }
        }, 2000);

        dialog22 = new Dialog(firstActivity.this);
        progressDialog22 = new ProgressDialog(firstActivity.this);

        //region busNameFromSharedPref
        String s;
        s = busName_pref.getString("BusNameKey22", "");


        if(GlobalClass.BusNameKey!=null|| GlobalClass.BusNameKey.size()!=0)
            GlobalClass.BusNameKey.clear();

        StringTokenizer stx = new StringTokenizer(s, "$");
        while (stx.hasMoreElements()) {

            GlobalClass.BusNameKey.add((String) stx.nextElement());
        }
        //endregion

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(firstActivity.this);

        if (admin)
            feedback_retreive();

        busNumberReference = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_adminRoster)).child(date);


        if (GlobalClass.forcedKickedOut)
            terminationProcess();

        //region active_checking

        activebusReference = FirebaseDatabase.getInstance().getReference()
        .child(getString(R.string.Firebase_admin)).child("Location");
        val_activebus = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {

                    for (DataSnapshot bus : dataSnapshot.getChildren()) {

                        if (bus != null) {
                            List<String> buslist = new ArrayList<>();
                            String name = bus.getKey();
                            String time = null;


                            for (DataSnapshot dir : bus.getChildren()) {

                                if (dir != null) {
                                    time = dir.getKey().toString();
                                    for (DataSnapshot inside : dir.getChildren()) {
                                        if (inside != null) {
                                            if (inside.getKey().equals("active_on") && inside.getValue().equals("on")
                                                    && !time.equals("TIME")  ) {

                                                buslist.add(time);
                                                GlobalClass.activetime.put(name + time, "true");


                                            } else if (inside.getKey().equals("active_on") && inside.getValue().equals("off")
                                                    && !time.equals("TIME") )
                                                GlobalClass.activetime.put(name + time, "false");
                                        }

                                    }
                                }

                            }
                            GlobalClass.activebus.put(name, buslist);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        activebusReference.addValueEventListener(val_activebus);
        //endregion

        progressDialog = new ProgressDialog(this);

    }

    private void fetchData() {
        if (isNetworkConnected(this)) {

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
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    fetchData();
                }
            };
            dateReference.addValueEventListener(val_date);
            //endregion

            //region password
            passReference = FirebaseDatabase.getInstance().getReference()
                    .child(getString(R.string.Firebase_password));
            val_pass = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long i = 0, child_count = dataSnapshot.getChildrenCount();

                    for (DataSnapshot busName : dataSnapshot.getChildren()) {

                        ++i;

                        for (DataSnapshot version : busName.getChildren()) {
                            if (!version.getKey().toString().equals(pass_version.getString(busName.getKey().toString() + "pass_version", "sakib"))) {

                                pass_version_editor = pass_version.edit();
                                pass_version_editor.putString(busName.getKey().toString() + "pass_version", version.getKey().toString());
                                pass_version_editor.apply();
                                for (DataSnapshot time : version.getChildren()) {
                                    SharedPreferences.Editor preferencesEditor = password_pref.edit();
                                    preferencesEditor.putString(busName.getKey() + time.getKey(), time.getValue().toString());
                                    preferencesEditor.apply();

                                }
                            } else break;

                        }

                        if (i == child_count) {
                            passbool = true;
                            if (passek) {
                                passek = false;
                                doJob();
                            }
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    fetchData();
                }
            };
            passReference.addValueEventListener(val_pass);
            //endregion

            //region time_schedule
            timeReference = FirebaseDatabase.getInstance().getReference()
                    .child(getString(R.string.Firebase_time));
            val_time = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (GlobalClass.BusNameKey != null || GlobalClass.BusNameKey.size() != 0)
                        GlobalClass.BusNameKey.clear();
                    long i = 0, child_count = dataSnapshot.getChildrenCount();


                    for (DataSnapshot busName : dataSnapshot.getChildren()) {

                        GlobalClass.BusNameKey.add(busName.getKey());

                        ++i;
                        for (DataSnapshot version : busName.getChildren()) {

                            if (!version.getKey().toString().equals(time_version.getString(busName.getKey().toString() + "time_version", "sakib"))) {
                                time_version_editor = time_version.edit();
                                time_version_editor.putString(busName.getKey().toString() + "time_version", version.getKey().toString());
                                time_version_editor.apply();

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
                                timeek = false;


                                doJob();
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    fetchData();
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

                            if (!version.getKey().toString().equals(route_version.getString(Bus.getKey().toString() + "route_version", "sakib"))) {
                                route_version_editor = route_version.edit();
                                route_version_editor.putString(Bus.getKey().toString() + "route_version", version.getKey().toString());
                                route_version_editor.apply();

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
                                routeek = false;
                                doJob();
                            }
                        }

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    fetchData();
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
                            if (!version.getKey().toString().equals(stoppage_version.getString(Bus.getKey().toString() + "stoppage_version", "sakin"))) {
                                stoppage_version_editor = stoppage_version.edit();
                                stoppage_version_editor.putString(Bus.getKey().toString() + "stoppage_version", version.getKey().toString());
                                stoppage_version_editor.apply();

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
                                stoppageek = false;
                                doJob();
                            }
                        }

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    fetchData();
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

                        doJob();

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    fetchData();
                }


            };
            firstReference.addValueEventListener(val_first);
            //endregion

            //region master
            masterReference = FirebaseDatabase.getInstance().getReference()
                    .child(getString(R.string.Firebase_masterAdmin));
            val_master = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String busNameKey = "";

                    for (DataSnapshot version : dataSnapshot.getChildren()) {

                        if (!version.getKey().toString().equals(masterPass_version.getString("masterPass_version", "sakib"))) {
                            masterPass_version_editor = masterPass_version.edit();
                            masterPass_version_editor.putString("masterPass_version", version.getKey().toString());
                            masterPass_version_editor.apply();
                            for (DataSnapshot busName : version.getChildren()) {

                                busNameKey += (busName.getKey() + "$");
                                SharedPreferences.Editor preferencesEditor = master_pref.edit();
                                preferencesEditor.putString(busName.getKey(), busName.getValue().toString());
                                preferencesEditor.apply();

                                SharedPreferences.Editor preferencesEditor2 = busName_pref.edit();
                                preferencesEditor2.putString("BusNameKey22", busNameKey);
                                preferencesEditor2.apply();


                            }

                            masterbool = true;
                            if (masterek) {

                                doJob();
                                masterek = false;
                            }

                        } else {
                            masterbool = true;
                            if (masterek) {
                                doJob();
                                masterek = false;
                            }
                        }


                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    fetchData();
                }
            };
            masterReference.addValueEventListener(val_master);
            //endregion

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
                            if (!version.getKey().toString().equals(bus_schedule_version.getString(busName.getKey().toString() + "bus_schedule_version", "sakib"))) {
                                bus_schedule_version_editor = bus_schedule_version.edit();
                                bus_schedule_version_editor.putString(busName.getKey().toString() + "bus_schedule_version", version.getKey().toString());
                                bus_schedule_version_editor.apply();
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
                                        //up2[k1].bus = (String) time.child("bus").getValue();
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
                                       // down2[l1].bus = (String) time.child("bus").getValue();
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


                                scheduleek = false;
                            }
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    fetchData();
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
                            }
                        }

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    fetchData();
                }

            };
            activebusReference.addValueEventListener(val_activebus);
            //endregion


        }
    }

    private void doJob() {

        if (timebool && stoppagebool && routebool && schedulebool && masterbool
                && firstbool && datebool && passbool && activebool) {
            doWork();

        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // works for walton
        // startLocationUpdates();
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if (mMap != null && GlobalClass.waiting == 1) {
                GlobalClass.waiting = 0;

                fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_location));
                if (GlobalClass.currentlat == -1 && GlobalClass.currentlon == -1) {
                    if (mMap != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.732696, 90.395674), 15f));
                    }
                } else {
                    if (mMap != null) {

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(GlobalClass.currentlat, GlobalClass.currentlon), 15f));

                    }

                }

                return;
            }

            if (GlobalClass.ct == 1 || GlobalClass.ct == 0 &&
                    (GlobalClass.currentlat != -1 && GlobalClass.currentlon != -1)) {
                if (GlobalClass.pp == 1) {

                    busNumberRetreive();

                    progressDialog.setMessage("Searching your bus");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                }

                    GlobalClass.ct = 0;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                                if (GlobalClass.progress == 1)
                                    progressDialog.dismiss();
                                if (GlobalClass.pp == 1) {
                                    progressDialog.dismiss();

                                    fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_location));
                                    ++locBus;
                                    if (mMap != null) {
                                        finalBusLocation = busLocation;
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 15f));

                                    }

                                } else {
                                    if (GlobalClass.currentlon == -1 && GlobalClass.currentlat == -1) {
                                        if (mMap != null) {
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.732696, 90.395674), 15f));


                                        }

                                    } else {
                                        if (mMap != null && busLocation != null) {

                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 15f));

                                        } else if (mMap != null) {
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(GlobalClass.currentlat, GlobalClass.currentlon), 15f));


                                        }

                                    }

                                }



                        }
                    }, 3000);


            }

            if (ContextCompat.checkSelfPermission(firstActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(firstActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                if (location != null) {
                                    lastlocation = location;
                                    LatLng currentLatLng = new LatLng(GlobalClass.currentlat, GlobalClass.currentlon);
                                    markerPlacing(currentLatLng);
                                } else {
                                    LatLng currentLatLng = new LatLng(GlobalClass.currentlat, GlobalClass.currentlon);
                                    if (GlobalClass.ct == 1) {
                                        GlobalClass.ct = 0;
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                if (GlobalClass.currentlat == -1 && GlobalClass.currentlon == -1) {
                                                    if (mMap != null) {

                                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.732696, 90.395674), zoom));

                                                    }
                                                } else {
                                                    if (mMap != null) {
                                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(GlobalClass.currentlat, GlobalClass.currentlon), zoom));

                                                    }
                                                }

                                            }
                                        }, 2000);
                                    }

                                    markerPlacing(currentLatLng);
                                }
                            }
                        });
            }
            startLocationUpdates();
        } else
            alertMethod();

    }

    private void busNumberRetreive() {


        busNumberReference = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_adminRoster)).child(date);

        ValueEventListener val_busNumber;
        val_busNumber = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                try {
                    if(dataSnapshot!=null)
                    {
                        busNumber = dataSnapshot.getValue().toString();

                    }


                } catch (Exception e) {

                    if(progressDialog!=null && progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                        progressDialog.cancel();

                    }
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        if(GlobalClass.BoxBusTime!=null && GlobalClass.BoxBusName!=null)
        busNumberReference.child(GlobalClass.BoxBusName)
                .child(GlobalClass.BoxBusTime).child("BusNumber")
                .addValueEventListener(val_busNumber);
    }

    private void userDataReception() {

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                blinking++;
                if (dataSnapshot != null) {



                    if (userselectionBus.equals("BUS") || userselectionTime.equals("TIME"))
                        return;
                    if (countMarker > 0 && marker != null)
                        marker.remove();

                    if(dataSnapshot.child("active_on").getValue().equals("off") && ekbarCheck)
                    {
                        ekbarCheck=false;
                        customSnackber();

                    }
                    setMarker(dataSnapshot);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        if (!userselectionBus.equals("BUS") && !userselectionTime.equals("TIME")) {
            ref.child(getString(R.string.Firebase_admin)).child("Location")
                    .child(userselectionBus)
                    .child(userselectionTime)
                    .addValueEventListener(listener);
        }
    }

    private void alertMethod() {

        final Context context = firstActivity.this;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.GPS_unavailable);
        dialog.setMessage(R.string.GPS_msg);

        dialog.setPositiveButton("GPS settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);

            }
        });
        alert = dialog.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
    }

    private void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(firstActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(firstActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, handlerThread.getLooper());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveListener(this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mMap.setOnCameraMoveCanceledListener((GoogleMap.OnCameraMoveCanceledListener) context);

            }
        }, 5000);

        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);

        if (GlobalClass.currentlat == -1 && GlobalClass.currentlon == -1) {
            LatLng jigatala = new LatLng(23.732696, 90.395674);
            if (mMap != null) {

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jigatala, 15f));

            }


        } else {

            LatLng jigatala = new LatLng(GlobalClass.currentlat, GlobalClass.currentlon);
            if (mMap != null) {

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jigatala, 15f));

            }
        }

        setUpMap();

    }

    private void setUpMap() {


        if (ContextCompat.checkSelfPermission(firstActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(firstActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        } else {


            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);


            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(firstActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {
                                lastlocation = location;
                                if (GlobalClass.currentlat != -1 && GlobalClass.currentlon != -1) {
                                    currentLatLng = new LatLng(GlobalClass.currentlat, GlobalClass.currentlon);

                                } else
                                    currentLatLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());

                                if (locBus == 0) {
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (GlobalClass.pp == 1) {
                                                GlobalClass.pp = 0;
                                                if (mMap != null) {
                                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 15f));

                                                }

                                            } else {
                                                if (mMap != null) {
                                                    LatLng jigatala = new LatLng(23.766702, 90.378090);

                                                    if (busLocation != null)
                                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 15f));
                                                    else
                                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));


                                                }
                                            }
                                        }
                                    }, 2000);
                                } else {
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (mMap != null && busLocation != null) {

                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 15f));

                                            }

                                        }
                                    }, 2000);
                                }


                            }

                        }
                    });

        }
        if (busLocation != null) {
            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(busLocation, zoom));


            }

        }

    }

    @Override
    public void onCameraMoveCanceled() {

        if (flag) {
            zoom = mMap.getCameraPosition().zoom;
            markerPlacing(mMap.getCameraPosition().target);
        }
    }

    private void markerPlacing(LatLng currentLatLng) {

        if (ActivityCompat.checkSelfPermission(firstActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        if (currentLatLng.latitude == -1 && currentLatLng.longitude == -1) {
            if (mMap != null) {

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.732696, 90.395674), zoom));

            }


        } else {
            if (mMap != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoom));


            }

        }


    }

    private void setMarker(DataSnapshot dataSnapshot) {

        double lat, lng;
        LatLng la;
        countMarker++;
        lat = (double) dataSnapshot.child("latitude").getValue();
        lng = (double) dataSnapshot.child("longitude").getValue();
        busLocation = new LatLng(lat, lng);
        adminLocation.setLatitude(lat);
        adminLocation.setLongitude(lng);

        //region checking bus reminder
        if (centerLocation != null) {
            dis = adminLocation.distanceTo(centerLocation);
            if (dis <= GlobalClass.finalBusRadius) {
                alarmflag = false;
                if (oncealarm) {

                    oncealarm = false;
                    Intent intent1 = new Intent(firstActivity.this, MyReceiver.class);
                    intent1.putExtra("name", "BusReminder");
                    PendingIntent p = PendingIntent.getBroadcast(firstActivity.this, 0, intent1, 0);
                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), p);
                    notification();

                }
            }
        }
        //endregion

        la = new LatLng(lat, lng);
        final LatLng LL = new LatLng(lat, lng);

        String spaceChecking = GlobalClass.BoxBusName + GlobalClass.BoxBusTime;
        int length = spaceChecking.length();
        String snippet = "";
        int x = length / 2;
        for (int i = 0; i < x - 2; i++)
            snippet += " ";

        if(removeMarker && gotoNextActivity)
        {
            MarkerOptions mk = new MarkerOptions();
            mk.draggable(false);
            mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            marker = mMap.addMarker(mk.position(la));

            marker.setSnippet("Bus No : " + busNumber);
            marker.setTitle(GlobalClass.BoxBusName + "  " + GlobalClass.BoxBusTime);
            marker.showInfoWindow();
        }


    }

    private void notification() {
        createNotificationChannel();
        sendNotification("Bus entered in your selected zone");
    }

    private void sendNotification(String msg) {
        Intent intent = new Intent(firstActivity.this, GeofenceTransitionsIntentService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(firstActivity.this, 0, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(firstActivity.this, "1")
                .setSmallIcon(R.drawable.pin)
                .setContentTitle("Bus Notification")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(firstActivity.this);

        notificationManager.notify(3, mBuilder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel one";
            String description = "this is channel one method";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("2", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Intent intent = null;

        switch (id) {
            case R.id.nav_busReminder:

                if (GlobalClass.BoxBusName.equals("BUS")) {
                    toastIconInfo("Select the bus name");
                    return false;
                }
                if (GlobalClass.BoxBusTime.equals("TIME")) {
                    toastIconInfo("Select the bus time");
                    return false;
                }
                oncealarm = true;
                doWork();
                checkNavigation_open_or_not();
                intent = new Intent(firstActivity.this, GeofenceSettings1.class);
                intent.putExtra("purpose", "busReminder");
                startActivity(intent);
                break;
            case R.id.nav_locationAlarm:

                //custom_network_alert();
                doWork();
                checkNavigation_open_or_not();
                intent = new Intent(firstActivity.this, GeofenceSettings1.class);
                intent.putExtra("purpose", "locationAlarm");
                startActivity(intent);
                break;

            case R.id.nav_schedule:
                doWork();
                checkNavigation_open_or_not();
                intent = new Intent(firstActivity.this, RecylerViewActivity.class);
                intent.putExtra("name", "Bus Schedule");
                startActivity(intent);
                break;


            case R.id.nav_route:
                doWork();
                checkNavigation_open_or_not();
                intent = new Intent(firstActivity.this, RecylerViewActivity.class);
                intent.putExtra("name", "Route And Stoppage");
                startActivity(intent);
                break;

            case R.id.nav_settings:
                doWork();
                checkNavigation_open_or_not();

                break;

            case R.id.nav_my_review:
                doWork();

                checkNavigation_open_or_not();
                if (feed_count == 0) {
                    startActivity(new Intent(firstActivity.this, emptyFeedbackLayout.class));
                } else {
                    intent = new Intent(firstActivity.this, RecylerViewActivity.class);
                    intent.putExtra("name", "My Review");
                    startActivity(intent);
                }

                break;

            case R.id.nav_feedback:
                doWork();
                checkNavigation_open_or_not();
                intent = new Intent(firstActivity.this, FeedbackPage.class);
                intent.putExtra("name", "Bus Admin Page");
                startActivity(intent);

                break;

            case R.id.nav_about:
                doWork();
                checkNavigation_open_or_not();
                intent = new Intent(firstActivity.this, About.class);
                intent.putExtra("name", "About Page");
                startActivity(intent);
                break;

            case R.id.nav_editProfile:
                doWork();
                checkNavigation_open_or_not();
                intent = new Intent(firstActivity.this, editProfile.class);
                startActivity(intent);
                break;

            case R.id.nav_turnoff:
                doWork();
                checkNavigation_open_or_not();

                if (GlobalClass.BusTime.matches("(.*)up(.*)")) {

                    GlobalClass.admin_successful_up = getSharedPreferences(getString(R.string.adminsuccessful_up_pref_file), MODE_PRIVATE);



                    if (GlobalClass.admin_successful_up.getString(getString(R.string.admin_successful_up_getter), "sakib")
                            .equals("successful_up_hoise")) {

                        custom_thanks_dialog();
                    } else {
                        normal_turn_off_dialog();
                    }
                } else if (GlobalClass.BusTime.matches("(.*)down(.*)")) {


                    GlobalClass.admin_successful_down = getSharedPreferences(getString(R.string.adminsuccessful_down_pref_file), MODE_PRIVATE);


                    if (GlobalClass.admin_successful_down.getString(getString(R.string.admin_successful_down_getter), "sakib")
                            .equals("successful_down_hoise")) {
                        custom_thanks_dialog();
                    } else {
                        normal_turn_off_dialog();
                    }
                }
                break;
        }
        return true;
    }

    private void searchCurrentActivefortop10() {


        if (active_top10_mobileNo != null) active_top10_mobileNo.clear();
        if (active_top10_time != null) active_top10_time.clear();
        active_top10_mobileNo = new ArrayList<>();
        active_top10_time = new ArrayList<>();


        val_active_for_top10 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long child_time = dataSnapshot.getChildrenCount(), i = 0;

                if (child_time == 0)
                    retrievingtop10();

                else {
                    for (DataSnapshot time : dataSnapshot.getChildren()) {
                        ++i;
                        for (DataSnapshot inner : time.getChildren()) {


                            if (inner.getKey().equals("Status") && inner.getValue().equals("Running")) {
                                active_top10_mobileNo.add(time.child("MobileNo").getValue().toString());

                                active_top10_time.add(time.getKey().toString());
                                break;
                            }
                        }

                        if (i == child_time)
                            srch_profile_accordingTo_mobile_no();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        active_for_top10.child(GlobalClass.BoxBusName).addListenerForSingleValueEvent(val_active_for_top10);

    }

    private void srch_profile_accordingTo_mobile_no() {

        if (active_for_top10 != null && val_active_for_top10 != null)
            active_for_top10.removeEventListener(val_active_for_top10);

        val_top10 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long child_count = dataSnapshot.getChildrenCount(), ct = 0;
                p_class = new profile_class[15];
                p_profile_index = -1;
                for (DataSnapshot unique_contact : dataSnapshot.getChildren()) {

                    ct++;
                    for (String active_top_mobile_no : active_top10_mobileNo) {

                        if (unique_contact.getKey().toString().equals(active_top_mobile_no)) {




                            ++p_profile_index;

                            p_class[p_profile_index] = new profile_class();

                            p_class[p_profile_index].contact_class = unique_contact.child("contact").getValue().toString();

                            p_class[p_profile_index].dept_class = unique_contact.child("dept").getValue().toString();

                            p_class[p_profile_index].down_trip_class = unique_contact.child("down_trip").getValue().toString();

                            p_class[p_profile_index].mail_class = unique_contact.child("email").getValue().toString();

                            p_class[p_profile_index].fbId_class = unique_contact.child("fbId").getValue().toString();

                            p_class[p_profile_index].feed_contact_class = unique_contact.child("feed_contact").getValue().toString();

                            p_class[p_profile_index].imageURL_class = unique_contact.child("imageURL").getValue().toString();

                            p_class[p_profile_index].last_accompanied = unique_contact.child("last_accompanied").getValue().toString();

                            p_class[p_profile_index].name_class = unique_contact.child("name").getValue().toString();

                            p_class[p_profile_index].pic_class = unique_contact.child("picString").getValue().toString();

                            p_class[p_profile_index].trip_class = unique_contact.child("trip").getValue().toString();

                            p_class[p_profile_index].up_trip_class = unique_contact.child("up_trip").getValue().toString();


                        }

                    }
                    if (ct == child_count)
                        retrievingtop10();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        top10.child(GlobalClass.BoxBusName).orderByChild("trip").limitToLast(10).addListenerForSingleValueEvent(val_top10);

    }

    private void normal_turn_off_dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(firstActivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage("Do you want to turn off Admin mode ?");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                terminationProcess();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void custom_thanks_dialog() {

        final Dialog dialog = new Dialog(firstActivity.this);
        dialog.setContentView(R.layout.custom_admin_thanks);


        TextView name = dialog.findViewById(R.id.textName);
        TextView info = dialog.findViewById(R.id.textInfo);
        name.setText(GlobalClass.NamePref.
                getString(getString(R.string.profile_name_getter), "Not provided"));
        info.setText("for accompanying the trip of " + GlobalClass.BusName + " " + GlobalClass.BusTime);
        Button btnYes = dialog.findViewById(R.id.buttonYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalClass.BusTime.matches("(.*)up(.*)")) {
                    GlobalClass.admin_successful_up = getSharedPreferences(getString(R.string.adminsuccessful_up_pref_file), MODE_PRIVATE);
                    SharedPreferences.Editor admin_up = GlobalClass.admin_successful_up.edit();
                    admin_up.putString(getString(R.string.admin_successful_up_getter), "sakib");
                } else if (GlobalClass.BusTime.matches("(.*)down(.*)")) {
                    GlobalClass.admin_successful_down = getSharedPreferences(getString(R.string.adminsuccessful_down_pref_file), MODE_PRIVATE);
                    SharedPreferences.Editor admin_up = GlobalClass.admin_successful_down.edit();
                    admin_up.putString(

                            getString(R.string.admin_successful_down_getter), "sakib");
                }

                dialog.dismiss();
                dialog.cancel();

                terminationProcess();
            }
        });

        Button btnNo = dialog.findViewById(R.id.buttonNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                dialog.cancel();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setAttributes(lp);


        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


    }

    private void retrievingtop10() {



        val_top10 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() == 0)
                    openEmptyActivity();
                else {
                    q_class = new profile_class[15];
                    q_profile_index = -1;
                    for (DataSnapshot unique_contact : dataSnapshot.getChildren()) {

                        boolean x = true;
                        for (String active_phone : active_top10_mobileNo) {
                            if (unique_contact.getKey().toString().equals(active_phone)) {

                                x = false;
                                break;
                            }
                        }
                        if (x) {
                            ++q_profile_index;
                            q_class[q_profile_index] = new profile_class();


                            for (DataSnapshot info : unique_contact.getChildren()) {


                                if (info.getKey().equals("contact"))
                                    q_class[q_profile_index].contact_class = info.getValue().toString();
                                if (info.getKey().equals("dept"))
                                    q_class[q_profile_index].dept_class = info.getValue().toString();
                                if (info.getKey().equals("down_trip"))
                                    q_class[q_profile_index].down_trip_class = info.getValue().toString();
                                if (info.getKey().equals("email"))
                                    q_class[q_profile_index].mail_class = info.getValue().toString();
                                if (info.getKey().equals("fbId"))
                                    q_class[q_profile_index].fbId_class = info.getValue().toString();
                                if (info.getKey().equals("feed_contact"))
                                    q_class[q_profile_index].feed_contact_class = info.getValue().toString();
                                if (info.getKey().equals("imageURL"))
                                    q_class[q_profile_index].imageURL_class = info.getValue().toString();
                                if (info.getKey().equals("last_accompanied"))
                                    q_class[q_profile_index].last_accompanied = info.getValue().toString();
                                if (info.getKey().equals("name"))
                                    q_class[q_profile_index].name_class = info.getValue().toString();
                                if (info.getKey().equals("picString"))
                                    q_class[q_profile_index].pic_class = info.getValue().toString();
                                if (info.getKey().equals("trip"))
                                    q_class[q_profile_index].trip_class = info.getValue().toString();
                                if (info.getKey().equals("up_trip"))
                                    q_class[q_profile_index].up_trip_class = info.getValue().toString();

                            }
                        }
                    }

                    if (p_profile_index == -1 && q_profile_index == -1) {

                        openEmptyActivity();

                    } else {

                        if (val_top10 != null && top10 != null)
                            top10.removeEventListener(val_top10);
                        sortingTop10();
                        progressDialog22.dismiss();
                        progressDialog22.cancel();
                        doWork();
                        checkNavigation_open_or_not();
                        Intent intent = new Intent(firstActivity.this, RecylerViewActivity.class);
                        intent.putExtra("name", "Admin profile");
                        startActivity(intent);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        top10.child(GlobalClass.BoxBusName).orderByChild("trip").limitToLast(10).addListenerForSingleValueEvent(val_top10);

    }

    private void openEmptyActivity() {

        if (val_active_for_top10 != null && active_for_top10 != null)
            active_for_top10.removeEventListener(val_active_for_top10);

        doWork();
        checkNavigation_open_or_not();
        progressDialog22.dismiss();
        progressDialog22.cancel();
        startActivity(new Intent(firstActivity.this, emptyAdmin_layout.class));
    }

    private void sortingTop10() {


        if (val_top10 != null && top10 != null)
            top10.removeEventListener(val_top10);
        GlobalClass.top10list = new HashMap<String, List<firstActivity.profile_class>>();
        List<firstActivity.profile_class> itemsList;
        itemsList = new ArrayList<>();

        if (p_profile_index >= 1) {
            Arrays.sort(p_class, 0, p_profile_index + 1, new CmpTop10());
            for (i = 0; i <= p_profile_index; ++i) {

                itemsList.add(p_class[i]);
            }

        } else if (p_profile_index == 0) {

            itemsList.add(p_class[0]);

        }


        if (q_profile_index >= 1) {
            Arrays.sort(q_class, 0, q_profile_index + 1, new CmpTop10());
            for (i = 0; i <= q_profile_index; ++i) {

                itemsList.add(q_class[i]);
            }
        } else if (q_profile_index == 0) {
            itemsList.add(q_class[0]);


        }

        GlobalClass.top10list.put(GlobalClass.BoxBusName, itemsList);
        listSize = itemsList.size();


    }

    private void terminationProcess() {


        if (!GlobalClass.forcedKickedOut) {
            final ProgressDialog progressDialog = new ProgressDialog(firstActivity.this);
            progressDialog.setMessage("Turning off admin mode...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                preEditor = busTimePref.edit();
                preEditor.putString(getString(R.string.bus_time_getter), "noBusTime");
                preEditor.apply();

                pre1Editor = busNamePref.edit();
                pre1Editor.putString(getString(R.string.bus_name_getter), "noBusName");
                pre1Editor.apply();
            }
        }, 1500);


        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!GlobalClass.forcedKickedOut)
                    progressDialog.dismiss();

                //region termination process

                ref = FirebaseDatabase.getInstance().getReference();

                toastIconInfo("GPS signal transmission is turned off");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                    if (GlobalClass.admin_successful_up.getString(getString(R.string.admin_successful_up_getter), "sakib")
                            .equals("successful_up_hoise") || GlobalClass.admin_successful_down.getString(getString(R.string.admin_successful_down_getter), "sakib")
                            .equals("successful_down_hoise")) {
                        ref.child(getString(R.string.Firebase_adminRoster))
                                .child(date)
                                .child(adminselectionBus)
                                .child(adminselectionTime).child("Status")
                                .setValue("Finished");
                        ref.child(getString(R.string.Firebase_adminRoster))
                                .child(date)
                                .child(adminselectionBus)
                                .child(adminselectionTime).child("Count")
                                .setValue("1");
                    } else {
                        ref.child(getString(R.string.Firebase_adminRoster))
                                .child(date)
                                .child(adminselectionBus)
                                .child(adminselectionTime).child("Status")
                                .setValue("Incomplete");
                    }

                    turnoff_listener = new OnCompleteListener() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {

                                preEditor = busTimePref.edit();
                                preEditor.putString(getString(R.string.bus_time_getter), "noBusTime");
                                preEditor.apply();

                                pre1Editor = busNamePref.edit();
                                pre1Editor.putString(getString(R.string.bus_name_getter), "noBusName");
                                pre1Editor.apply();

                                if (AdminBackground.dbRef != null && AdminBackground.val_trip_rechecking != null)
                                    AdminBackground.dbRef.removeEventListener(AdminBackground.val_trip_rechecking);
                                stopService(new Intent(firstActivity.this, AdminBackground.class));
                                FirebaseAuth.getInstance().signOut();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    finishAndRemoveTask();
                                }
                                System.exit(0);
                            }
                        }
                    };

                    ref.child(getString(R.string.Firebase_admin)).child("Location")
                            .child(adminselectionBus)
                            .child(adminselectionTime).child("active_on")
                            .setValue("off").addOnCompleteListener(turnoff_listener);

                    ref.child(getString(R.string.Firebase_admin)).child("Location")
                            .child(adminselectionBus)
                            .child(adminselectionTime).child("force")
                            .setValue("in");
                    ref.child(getString(R.string.Firebase_admin)).child("Location")
                            .child(adminselectionBus)
                            .child(adminselectionTime).child("latitude").setValue(lastlocation.getLatitude());

                    ref.child(getString(R.string.Firebase_admin)).child("Location")
                            .child(adminselectionBus)
                            .child(adminselectionTime).child("longitude").setValue(lastlocation.getLongitude());


                } else {

                    ref.child(getString(R.string.Firebase_adminRoster)).child(date)
                            .child(adminselectionBus)
                            .child(adminselectionTime).child("Status")
                            .setValue("Finished");

                    turnoff_listener = new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            preEditor = busTimePref.edit();
                            preEditor.putString(getString(R.string.bus_time_getter), "noBusTime");
                            preEditor.apply();

                            pre1Editor = busNamePref.edit();
                            pre1Editor.putString(getString(R.string.bus_name_getter), "noBusName");
                            pre1Editor.apply();

                            stopService(new Intent(firstActivity.this, AdminBackground.class));
                            FirebaseAuth.getInstance().signOut();

                            finishAffinity();
                        }
                    };

                    ref.child(getString(R.string.Firebase_admin)).child("Location")
                            .child(adminselectionBus)
                            .child(adminselectionTime).child("active_on")
                            .setValue("off").addOnCompleteListener(turnoff_listener);

                    ref.child(getString(R.string.Firebase_admin)).child("Location")
                            .child(adminselectionBus)
                            .child(adminselectionTime).child("latitude").setValue(lastlocation.getLatitude());

                    ref.child(getString(R.string.Firebase_admin)).child("Location")
                            .child(adminselectionBus)
                            .child(adminselectionTime).child("longitude").setValue(lastlocation.getLongitude());
                    ref.child(getString(R.string.Firebase_admin)).child("Location")
                            .child(adminselectionBus)
                            .child(adminselectionTime).child("force")
                            .setValue("in");

                }
                //endregion
            }
        }, 2000);
    }

    private void doWork() {
        if (dateReference != null && val_date != null)
            dateReference.removeEventListener(val_date);
        if (masterReference != null && val_master != null)
            masterReference.removeEventListener(val_master);
        if (routeReference != null && val_route != null)
            routeReference.removeEventListener(val_route);
        if (firstReference != null && val_first != null)
            firstReference.removeEventListener(val_first);
        if (passReference != null && val_pass != null)
            passReference.removeEventListener(val_pass);
        if (timeReference != null && val_time != null)
            timeReference.removeEventListener(val_time);
        if (scheduleReference != null && val_schedule != null)
            scheduleReference.removeEventListener(val_schedule);
        if (stoppageReference != null && val_stoppage != null)
            stoppageReference.removeEventListener(val_stoppage);
        if (trip_countRef != null && val_trip != null)
            trip_countRef.removeEventListener(val_trip);

        if(snackbar!=null && snackbar.isShown())
            snackbar.dismiss();


    }

    public PopupWindow popupWindowDogs(View v) {

        // initialize a pop up window type
        popupWindow = new PopupWindow(firstActivity.this);

        // the drop down list is a list view
        final ListView listViewDogs = new ListView(firstActivity.this);


        // set our adapter and pass our pop up window contents
        if (v.getId() == R.id.busBtn) {

            listViewDogs.setAdapter(dogsAdapter(GlobalClass.BusNameKey, "buspop"));


        } else if (v.getId() == R.id.timeBtn) {

            GlobalClass.timetouch = true;
            context = this;
            busAndTime = new BusAndTime(GlobalClass.BoxBusName, context);
            time = busAndTime.getTime();
            listViewDogs.setAdapter(dogsAdapter(time, "timepop"));


        }

        // set the item click listener
        listViewDogs.setOnItemClickListener(new DogsDropdownOnItemClickListener());


        // some other visual settings
        popupWindow.setFocusable(false);
        popupWindow.setWidth((int) (width * .42));
        popupWindow.setHeight((int) (height * .4));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    private ArrayAdapter<String> dogsAdapter(final ArrayList scripts, final String purpose) {

        count = 0;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_popup, scripts) {


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);

                ViewGroup.LayoutParams params = view.getLayoutParams();

                // Set the height of the Item View
                params.height = 80;
                view.setLayoutParams(params);
                // setting the ID and text for every items in the list
                String item = getItem(position);

                // visual settings for the list item
                TextView listItem = new TextView(firstActivity.this);

                listItem.setText(item);


                listItem.setTextSize(20);
                listItem.setPadding(10, 10, 10, 10);

                if (purpose.equals("buspop")) {
                    count++;
                    if ((GlobalClass.activebus.get(item) != null && GlobalClass.activebus.get(item).size() == 0)) {

                        listItem.setTextColor(Color.GRAY);
                    } else {

                        if (isNetworkConnected(firstActivity.this)) {
                            listItem.setTypeface(Typeface.DEFAULT_BOLD);
                            listItem.setTextColor(Color.parseColor("#2F1B74"));

                        } else listItem.setTextColor(Color.GRAY);

                    }
                } else if (purpose.equals("timepop")) {
                    count++;
                    if ((GlobalClass.activetime.get(GlobalClass.BoxBusName + item) != null && GlobalClass.activetime.get(GlobalClass.BoxBusName + item).equals("false"))) {

                        listItem.setTextColor(Color.GRAY);
                    } else {

                        if (isNetworkConnected(firstActivity.this)) {
                            listItem.setTypeface(Typeface.DEFAULT_BOLD);

                            listItem.setTextColor(Color.parseColor("#2F1B74"));
                        } else listItem.setTextColor(Color.GRAY);
                    }

                }


                return listItem;
            }
        };

        return adapter;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 123 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpMap();

                if (GlobalClass.AdminVirgin && admin) {
                    custom_edit_profile_reminder();
                }

            } else {
                finishAffinity();
                System.exit(0);
            }
        }
    }

    @Override
    public void onClick(View v) {

        ++locBus;


        //region fabAdmin
        if (v.getId() == R.id.fabloc && admin) {
            zoom = 15f;

            if (mMap != null)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoom));

        }
        //endregion

        //region fabTraffic
        if (v.getId() == R.id.fabtraffic) {

            if (!isNetworkConnected(firstActivity.this)) {

                toastIconInfo("Internet connection required");
                return;
            }
            if (mMap.isTrafficEnabled()) {
                mMap.setTrafficEnabled(false);

            } else {
                mMap.setTrafficEnabled(true);

            }
        }
        //endregion

        if (v.getId() == R.id.fabloc && user) {

            //region network checking
            if (!isNetworkConnected(firstActivity.this)) {

                if (mMap != null)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoom));
                toastIconInfo("Internet connection required");
                return;
            }

            if (userselectionBus.equals("BUS") || userselectionTime.equals("TIME")) {
                locBus = 0;
                if (GlobalClass.currentlon == -1 && GlobalClass.currentlat == -1) {
                    if (mMap != null)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.732696, 90.395674), 15f));
                } else if (mMap != null)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(GlobalClass.currentlat, GlobalClass.currentlon), 15f));

                toastIconInfo("You haven't searched any bus yet");

                return;
            }
            //endregion
//            if (locBus == 2)
////                locBus++;

            if (locBus % 2 == 1) {


                if (GlobalClass.currentlon == -1 && GlobalClass.currentlat == -1) {
                    if (mMap != null)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.732696, 90.395674), 15f));
                } else {
                    if (mMap != null)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(GlobalClass.currentlat, GlobalClass.currentlon), 15f));
                }

                fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_bus));
                freeze = false;
            } else {

                if(!gotoNextActivity)
                {
                    customSnackber();
                    return;
                }

                else
                {

                    if (mMap != null)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 15f));

                    fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_location));
                    freeze = true;
                }


            }


            //}


        }


    }

    private void sorting(String name) {

        SharedPreferences.Editor repeatTimeEditor = repeatTime_pref.edit();

        temp = "";
        Arrays.sort(up, 0, k, new firstActivity.Cmp());
        for (i = 0; i < k; ++i) {

            System.out.print(" " + up[i].z);
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
        Arrays.sort(down, 0, l, new firstActivity.Cmp());
        for (i = 0; i < l; ++i) {
            String tempo1, tempo2;
            boolean checkFlag = false;
            if (i > 0 && down[i].z.equals(down[i - 1].z)) {
                checkFlag = true;
            }
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

        Arrays.sort(up2, 0, k1, new firstActivity.Cmp2());
        for (i = 0; i < k1; ++i) {
            temp2 += (up2[i].time + "#");
            temp2 += (up2[i].from + "#");
            temp2 += (up2[i].to + "#");
            temp2 += (up2[i].type + "$");
            //temp2 += (up2[i].bus + "$");
        }
        Arrays.sort(down2, 0, l1, new firstActivity.Cmp2());
        for (i = 0; i < l1; ++i) {
            temp2 += (down2[i].time + "#");
            temp2 += (down2[i].from + "#");
            temp2 += (down2[i].to + "#");
            temp2 += (down2[i].type + "$");
            //temp2 += (down2[i].bus + "$");
        }

        SharedPreferences.Editor preferencesEditor = schedule_pref.edit();
        preferencesEditor.putString(name, temp2);
        preferencesEditor.apply();

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

    @Override
    protected void onStop() {


        SharedPreferences busNamePref = getSharedPreferences(getString(R.string.bus_name_pref_file), MODE_PRIVATE);
        SharedPreferences busTimePref = getSharedPreferences(getString(R.string.bus_time_pref_file), MODE_PRIVATE);
        SharedPreferences.Editor preEditor = busTimePref.edit();
        preEditor.putString(getString(R.string.bus_time_getter), GlobalClass.BusTime);
        preEditor.apply();

        SharedPreferences.Editor pre1Editor = busNamePref.edit();
        pre1Editor.putString(getString(R.string.bus_name_getter), GlobalClass.BusName);
        pre1Editor.apply();
        super.onStop();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        ++count;
        if (count % 2 == 0)
            marker.hideInfoWindow();
        else
            marker.showInfoWindow();
        return true;

    }

    @Override
    public void onCameraIdle() {

        if (admin)
            img.setVisibility(View.VISIBLE);
        aftercameramove = mMap.getCameraPosition().target;
        finalLocation = true;
    }

    @Override
    public void onCameraMove() {
        aftercameramove = mMap.getCameraPosition().target;
        zoom = mMap.getCameraPosition().zoom;

        if (admin)
            img.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void custom_user_turn_off() {

        final Dialog dialog = new Dialog(firstActivity.this);
        dialog.setContentView(R.layout.custom_user_turn_off);

        Button btnExit = dialog.findViewById(R.id.buttonExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    if (BusTrackerService.busTrackerService != null)
                        BusTrackerService.busTrackerService.stopSelf();

                    if (TrackerService.trackerService != null)
                        TrackerService.trackerService.stopSelf();

                    finishAndRemoveTask();
                    System.exit(0);

                } else {

                    if (BusTrackerService.busTrackerService != null)
                        BusTrackerService.busTrackerService.stopSelf();

                    if (TrackerService.trackerService != null)
                        TrackerService.trackerService.stopSelf();

                    finishAffinity();
                }
            }
        });

        Button btnCancel = dialog.findViewById(R.id.buttonCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                dialog.cancel();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setAttributes(lp);


        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    public static void custom_network_alert() {

        if (GlobalClass.ekbar_network_alarm) {
            GlobalClass.ekbar_network_alarm = false;
            network_alarm();
        }

        dialog22.setContentView(R.layout.custom_network_alert);

        Button btn = dialog22.findViewById(R.id.gotIt);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myVib != null)
                    myVib.cancel();
                dialog22.cancel();
                dialog22.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog22.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog22.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog22.getWindow().setAttributes(lp);


        dialog22.setCanceledOnTouchOutside(false);
        dialog22.show();

    }

    static void network_alarm() {

        myVib = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        myVib.vibrate(pattern, 0);

        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                //defaultRingtone.stop();
                myVib.cancel();
            }
        };
        handler.postDelayed(r, 10000);
    }

    private void checkNavigation_open_or_not() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
    }

    class CmpTop10 implements Comparator<profile_class> {
        public int compare(profile_class x, profile_class y) {




            int first_trip = Integer.parseInt(x.trip_class);

            int second_trip = Integer.parseInt(y.trip_class);


            if (first_trip == second_trip)
                return 0;
            else if (first_trip > second_trip) return -1;
            else return 1;

        }


    }

    public static class profile_class {
        String name_class, dept_class, contact_class, trip_class, pic_class, fbId_class,
                mail_class, imageURL_class, feed_contact_class, last_accompanied,
                up_trip_class, down_trip_class;
    }

    public static class timeline_class {
        String busName_timeline, busTime_timeline, busFrom_timeline, busTo_timeline;

        public timeline_class(String busName_timeline, String busTime_timeline, String busFrom_timeline, String busTo_timeline) {
            this.busName_timeline = busName_timeline;
            this.busTime_timeline = busTime_timeline;
            this.busFrom_timeline = busFrom_timeline;
            this.busTo_timeline = busTo_timeline;
        }
    }

    public class feed_class {
        String feed_class_text, feed_class_date, feed_class_id;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_adminProfile:
                doWork();
                checkNavigation_open_or_not();

                if (!isNetworkConnected(this)) {
                    toastIconInfo("Internet connection required");
                    return false;
                }

                if (buttonBus.getText().equals("BUS")) {
                    toastIconInfo( "Select the bus name");

                } else {

                    progressDialog22 = new ProgressDialog(firstActivity.this);
                    progressDialog22.setMessage("Loading top admins ...");
                    progressDialog22.setCanceledOnTouchOutside(false);
                    progressDialog22.show();

                    if (admin)
                        GlobalClass.BoxBusName = GlobalClass.BusName;

                    searchCurrentActivefortop10();

                }


        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    public void openCustomTimelineDialog() {

        // custom dialog
        dialog23 = new Dialog(firstActivity.this);
        dialog23.setContentView(R.layout.timeline_custom_layout);

        //dialog23.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


        ImageView cut_cross = dialog23.findViewById(R.id.cut_cross);
        cut_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog23.cancel();
                dialog23.dismiss();
                linearLayout.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                fab.show();
                fabt.show();
            }
        });

        timeline_info_adapter timeline_info_adapter22;
        RecyclerView recyclerView;
        recyclerView = dialog23.findViewById(R.id.CustomRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<timeline_class> itemsList = new ArrayList<>();
        timeline_class a = new timeline_class("Taranga", "12:30 down,",
                "VC Chattar", "Rampura TV center");
        timeline_class b = new timeline_class("Taranga2", "12:30 down2,",
                "Curzon Hall", "Mohammadpur Bus Stand");
        timeline_class c = new timeline_class("Taranga", "12:30 down,",
                "VC Chattar", "Rampura TV center");
        timeline_class d = new timeline_class("Taranga2", "12:30 down2,",
                "Curzon Hall", "Mohammadpur Bus Stand");
        timeline_class e = new timeline_class("Taranga", "12:30 down,",
                "VC Chattar", "Rampura TV center");
        timeline_class f = new timeline_class("Taranga2", "12:30 down2,",
                "Curzon Hall", "Mohammadpur Bus Stand");
        itemsList.add(a);
        itemsList.add(b);
        itemsList.add(c);
        itemsList.add(d);
        itemsList.add(e);
        itemsList.add(f);

        timeline_info_adapter22 = new timeline_info_adapter(this, itemsList);
        recyclerView.setAdapter(timeline_info_adapter22);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog23.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.MATCH_PARENT);
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialog23.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog23.getWindow().setAttributes(lp);

        dialog23.setCanceledOnTouchOutside(false);
        dialog23.setCancelable(false);
        dialog23.show();


    }

    @Override
    public void onBackPressed() {


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(firstActivity.this);
        if (admin) {
            builder.setTitle("Caution!!");
            builder.setMessage("Admin mode requires background running service." +
                    "Minimize the app and don't clear from recent list.");


            builder.setNegativeButton("GOT IT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.show();
        } else {

            custom_user_turn_off();

        }


    }
}
