package com.example.asus.remindmemyself;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;

public class firstActivity extends AppCompatActivity implements View.OnClickListener,
        OnMapReadyCallback, GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener ,
        NavigationView.OnNavigationItemSelectedListener{

    //region variable
    private static int interval =5000, fastinterval = 2000;
    public StoppageMarkerPosition sp= new StoppageMarkerPosition();
    private DrawerLayout drawer;
    public static int countMarker=0,locBus=0;
    private GoogleMap mMap=null;
    private ProgressDialog progressDialog;
    private static float zoom=15f;
    public boolean freeze=true,prechecking=false,destroy=true;
    public static boolean oncealarm=false;
    int height,width;
    public static PopupWindow popupWindow,popupWindowDogs;
    public static Location centerLocation=null,adminLocation;
    public  static ValueEventListener listener;
    public Button buttonBus,buttonTime,buttonSearch;
    public String popUpBus [] = {"Anando","Boishakhi","Boshonto","Chittagong Road","Choitaly","Falguni","Hemonto","Ishakha","Kinchit","Khonika","Moitree/null","Srabon","Taranga","Ullash","Wari"};
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private FloatingActionButton fab, fabt;
    private Location lastlocation;
    private Menu menu;
    final HandlerThread handlerThread= new HandlerThread("RequestLocation");;
    private static LatLng currentLatLng = null, aftercameramove,busLocation;
    public static boolean flag = true,admin=false,user=false,finalLocation=false,alarmflag=true,Bool_busbtn=false,Bool_timbtn=false;
    public static Marker marker;
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private static  int count = 0, call = 0,indexNumber=0,k1=0,l1=0;
    private ImageView img;
    private TextView tv,textView;
    public static float dis;
    private Uri uri;
    private LocationManager lm;
    private Ringtone defaultRingtone;
    private  Vibrator myVib;
    private  AudioManager am;
    private AlertDialog.Builder dialog;
    private AlertDialog alert;
    private LinearLayout linearLayout;
    public static String adminselectionBus,adminselectionTime,userselectionBus,userselectionTime;
    private FirebaseAuth mAuth;
    public Context context;
    public static DatabaseReference ref,scheduleReference;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    DogsDropdownOnItemClickListener d= new DogsDropdownOnItemClickListener();
    BusAndTime b;
    String[] time;
    public SharedPreferences schedulePref;
    public String schedulefile="hellojobaid",temp2;
    public ValueEventListener val_schedule=null;
    public static intt2[]up2 = null;
    public static intt2[]down2 = null;

    //endregion




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region oncreate declarations

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Log.d("jobaid", "firstActivity:onCreate");
        tv= findViewById(R.id.mywidget);
        tv.setText("Your are in admin mode. "+"              "+" You are in admin mode.");
        tv.setSelected(true);

        schedulePref = getSharedPreferences(schedulefile, MODE_PRIVATE);

        //region Bus_schedule
        scheduleReference=FirebaseDatabase.getInstance().getReference()
                .child("Bus_Schedule");
        val_schedule= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    up2= new intt2[50];
                    down2= new intt2[50];
                    k1=0; l1=0;

                    for(DataSnapshot inner : snapshot.getChildren()) {

                        if (inner.getKey().matches("(.*)up"))
                        {
                            up2[k1]= new intt2();
                            up2[k1].time= (String) inner.child("time").getValue();
                            up2[k1].from= (String) inner.child("from").getValue();
                            up2[k1].to= (String) inner.child("to").getValue();
                            up2[k1].type= (String) inner.child("type").getValue();

                            Log.d("khushite",up2[k1].time+" "
                                    +up2[k1].from+" "+up2[k1].to+" "+up2[k1].type+" ");
                            k1++;
                        }
                        else if(inner.getKey().matches("(.*)down"))
                        {
                            down2[l1]= new intt2();
                            down2[l1].time= (String) inner.child("time").getValue();
                            down2[l1].from= (String) inner.child("from").getValue();
                            down2[l1].to= (String) inner.child("to").getValue();
                            down2[l1].type= (String) inner.child("type").getValue();
                            l1++;
                        }

                    }
                    Log.d("sakibjob",String.valueOf(snapshot.getKey()));

                    sorting2(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        scheduleReference.addValueEventListener(val_schedule);
        //endregion
        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.busBtn:

                        popupWindowDogs = popupWindowDogs(v);
                        popupWindowDogs.showAsDropDown(v);
                        break;

                    case R.id.timeBtn:

                        // show the list view as dropdown
                        if(buttonBus.getText().equals("BUS"))
                            Toast.makeText(firstActivity.this,"Please select the name of bus ",Toast.LENGTH_LONG).show();
                        else
                        {
                            //popupWindowDogs.dismiss();
                            popupWindowDogs = popupWindowDogs(v);
                            popupWindowDogs.showAsDropDown(v);
                        }
                        break;

                    case R.id.searchBtn:
                         GlobalClass.title=true;
                        if(!isNetworkConnected(firstActivity.this) && buttonTime.getText().equals("TIME"))
                        {
                            Toast.makeText(firstActivity.this,"Network Connection Required",Toast.LENGTH_LONG).show();
                            return ;
                        }
                        if(buttonTime.getText().equals("TIME"))
                        {
                            Toast.makeText(firstActivity.this,"Select the bus time",Toast.LENGTH_LONG).show();
                            return ;
                        }
                        if(!isNetworkConnected(firstActivity.this))
                        {
                            Toast.makeText(firstActivity.this,"Network Connection Required",Toast.LENGTH_LONG).show();
                            return ;
                        }

                         GlobalClass.first=1;
                        if(!GlobalClass.BusName.equals(GlobalClass.PreBusName))
                        {

                            GlobalClass.PreBusName=GlobalClass.BusName;
                            Bool_busbtn=true;
                        }
                        if(!GlobalClass.BusTime.equals(GlobalClass.preBusTime))
                        {
                            Toast.makeText(firstActivity.this,"Same schedule",Toast.LENGTH_LONG).show();
                            GlobalClass.preBusTime=GlobalClass.BusTime;
                            Bool_timbtn=true;
                        }
                        if(Bool_busbtn || Bool_timbtn)
                        {
                            Bool_timbtn=false;Bool_busbtn=false;
                        }
                        else return;

                        GlobalClass.pp=1;
                        locBus=0;
                        destroy=false;
                        if(GlobalClass.BusName.equals("Taranga"))
                        {

//                            ref.removeEventListener(listener);
//                            //progressDialog.setMessage("Searching your bus..");
//                            //progressDialog.show();
//                            userselectionBus=GlobalClass.BusName;
//                            userselectionTime=DogsDropdownOnItemClickListener.BusTime;
//                            Log.d("cheque",userselectionBus+"  " +userselectionTime);
//                            ref = FirebaseDatabase.getInstance().getReference();
//
//                            ref.child("Admin").child("Location")
//                                    .child(userselectionBus)
//                                    .child(userselectionTime)
//                                    .addValueEventListener(listener);
//
//                            listener= new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                    if(countMarker>0)
//                                        marker.remove();
//                                    Log.d("jobaid","firstactivity : asfw");
//                                    Log.d("cheque",GlobalClass.BusName+" ** " +DogsDropdownOnItemClickListener.BusTime);
//
//
//                                    setMarker(dataSnapshot);
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            };
                            Intent intent= new Intent(firstActivity.this,firstActivity.class);
                            intent.putExtra("name","user");
                            intent.putExtra("BUSNAME",GlobalClass.BusName);
                            intent.putExtra("TIME",GlobalClass.BusTime);
                            startActivity(intent);
                            finish();

                        }
                        else
                        {
                            Toast.makeText(firstActivity.this,"Only Taranga Bus Tracking is available now",Toast.LENGTH_LONG).show();
                        }

                        break;
                }
            }
        };

        handlerThread.start();

        // our button
        buttonBus = (Button) findViewById(R.id.busBtn);
        buttonTime=(Button)findViewById(R.id.timeBtn);
        buttonSearch=(Button)findViewById(R.id.searchBtn);
        buttonBus.setOnClickListener(handler);
        buttonTime.setOnClickListener(handler);
        buttonSearch.setOnClickListener(handler);

        centerLocation= new Location("");
        adminLocation = new Location("");
        progressDialog = new ProgressDialog(this);
        linearLayout=findViewById(R.id.linear);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        img = (ImageView) findViewById(R.id.iconid);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        fab = (FloatingActionButton) findViewById(R.id.fabloc);
        fabt = (FloatingActionButton) findViewById(R.id.fabtraffic);
        fabt.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.traffic));
        fab.setOnClickListener(this);
        fabt.setOnClickListener(this);

        //endregion

        if (ContextCompat.checkSelfPermission(firstActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(firstActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
        if((getIntent().getStringExtra("name")).equals("admin"))
        {
            Log.d("locker","admin");
            fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_bus));
            adminselectionBus=getIntent().getStringExtra("BUSNAME");
            adminselectionTime=getIntent().getStringExtra("TIME");
            this.setTitle(adminselectionBus+" "+adminselectionTime);
            admin=true;

        }
        if((getIntent().getStringExtra("name")).equals("user"))
        {
            Log.d("locker","user");

            tv.setVisibility(View.GONE);
            if(isNetworkConnected(this))
            {
                Log.d("pppp","one");
                if(GlobalClass.first==0)
                    fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_location));
                else
                fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_bus));

            }
            else
            {
                Log.d("pppp","two");
                fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_location));
            }
            textView=(TextView)findViewById(R.id.profile_email);

            if(textView!=null)
            textView.setText(GlobalClass.useremail);

            userselectionBus = getIntent().getStringExtra("BUSNAME");
            userselectionTime=getIntent().getStringExtra("TIME");


            if(GlobalClass.title)
            {
                buttonBus.setText(userselectionBus);
                buttonTime.setText(userselectionTime);
                this.setTitle(userselectionBus+" "+userselectionTime);
            }

            else
            {
                this.setTitle("Lal Bus");
                buttonBus.setText("BUS");
                buttonTime.setText("TIME");
            }

            user=true;
            Log.d("jobaid","firstActivity: "+userselectionBus + " " + userselectionTime);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        if(admin)
        {
            menu.findItem(R.id.nav_busReminder).setVisible(false);
            menu.findItem(R.id.nav_settings).setVisible(false);
            linearLayout.setVisibility(View.GONE);
            adminloginToFirebase();
        }
        if(user)
        {
            ref = FirebaseDatabase.getInstance().getReference();
            menu.findItem(R.id.nav_settings).setVisible(false);
            menu.findItem(R.id.nav_turnoff).setVisible(false);

            listener= new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(countMarker>0 && marker!=null)
                        marker.remove();
                    Log.d("jobaid","firstactivity : onDataChange");

                    if(userselectionBus.equals("BUS")||userselectionTime.equals("TIME"))
                       return ;
                    else
                    setMarker(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            ref.child("Admin").child("Location")
                    .child(userselectionBus)
                    .child(userselectionTime)
                    .addValueEventListener(listener);
            Log.d("jobaid","firstActivity:firebaseListener");
            img.setVisibility(View.GONE);
            userloginToFirebase();
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                ref = FirebaseDatabase.getInstance().getReference();
                lastlocation = locationResult.getLastLocation();

                ((GlobalClass)firstActivity.this.getApplication()).lat=lastlocation.getLatitude();
                ((GlobalClass)firstActivity.this.getApplication()).lon=lastlocation.getLongitude();

                ((GlobalClass)firstActivity.this.getApplication()).currentlat=lastlocation.getLatitude();
                ((GlobalClass)firstActivity.this.getApplication()).currentlon=lastlocation.getLongitude();

                currentLatLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
                if(admin)
                {
                    Log.d("jobaid","firstactivity : b4callback");
                    if(finalLocation)
                    {

                        GlobalClass.signal=true;
                        Intent intent= new Intent(firstActivity.this,AdminBackground.class);
                        intent.putExtra("BusName",adminselectionBus);
                        intent.putExtra("BusTime",adminselectionTime);
                        startService(intent);
                        /// location by camera movement
//                        AdminLocation al= new AdminLocation(aftercameramove.latitude,aftercameramove.longitude);
//                        Toast.makeText(firstActivity.this,"GPS signal is transmitting",Toast.LENGTH_SHORT).show();
//                        Log.d("jobaid", "firstactivity : GPS signal is transmitting");
//                        ref.child("Admin").child("Location")
//                                .child(adminselectionBus)
//                                .child(adminselectionTime)
//                                .setValue(al);
                    }
                    else
                    {
                        Log.d("panic ","attack");
                        Toast.makeText(firstActivity.this,"GPS signal is transmitting 2",Toast.LENGTH_LONG).show();
                        AdminLocation al= new AdminLocation(lastlocation.getLatitude(),lastlocation.getLongitude());
                        Log.d("jobaid", "firstactivity : callback2");
                        ref.child("Admin").child("Location")
                                .child(adminselectionBus)
                                .child(adminselectionTime)
                                .setValue(al);
                    }
                }
            }
        };
        createLocationRequest();

    }
    private void sorting2(String name) {
        temp2="";

        Log.d("haider","cholse");
        Arrays.sort(up2,0,k1,new Cmp2());
        Log.d("haider","cholse2");
        for(int i=0; i<k1; ++i) {
            temp2+=(up2[i].time+"#");
            temp2+=(up2[i].from+"#");
            temp2+=(up2[i].to+"#");
            temp2+=(up2[i].type+"$");
        }
        Log.d("haider","cholse3");
        Arrays.sort(down2,0,l1,new Cmp2());
        Log.d("haider","cholse4");
        for(int i=0; i<l1; ++i) {
            temp2+=(down2[i].time+"#");
            temp2+=(down2[i].from+"#");
            temp2+=(down2[i].to+"#");
            temp2+=(down2[i].type+"$");
        }

        Log.d("william",temp2+"@@");
        SharedPreferences.Editor preferencesEditor = schedulePref.edit();
        preferencesEditor.putString(name, temp2);
        preferencesEditor.apply();

    }
    private void createLocationRequest() {
        Log.d("jobaid", "firstactivity: createLocationRequest");

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(interval);
        locationRequest.setFastestInterval(fastinterval);

    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(this);
        Log.d("jobaid", "firstactivity : onStart");
        Log.d("jobaid","abar aise");
    }

    @Override
    protected void onResume() {
        super.onResume();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Log.d("jobaid", "firstactivity : onResume");
        // works for walton
        // startLocationUpdates();
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("jobaid", "firstactivity: on Resume [b4 locationupdate]");

            if(mMap!=null && GlobalClass.waiting==1)
            {
                Log.d("deb","1");
                GlobalClass.waiting=0;
                fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_location));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(GlobalClass.currentlat,GlobalClass.currentlon),15f));

                return;
            }

            if(GlobalClass.ct==1|| GlobalClass.ct==0 &&(GlobalClass.currentlat!=-1 && GlobalClass.currentlon!=-1))
            {
                if(GlobalClass.pp==1)
                {
                    progressDialog.setMessage("Searching your bus");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                }
                GlobalClass.ct=0;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(GlobalClass.progress==1)
                          progressDialog.dismiss();
                        Toast.makeText(firstActivity.this,"biday pitibi",Toast.LENGTH_SHORT).show();
                        if(GlobalClass.pp==1)
                        {
                            progressDialog.dismiss();
                            Log.d("deb","2");
                            fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_location));
                            ++locBus;
                            Log.d("jobaid","hosseeee");
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 15f));

                        }
                        else
                        {
                            Log.d("deb","3");
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(GlobalClass.currentlat,GlobalClass.currentlon),15f));}

                    }
                }, 2000);
            }

            if (ContextCompat.checkSelfPermission(firstActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d("jobaid", "firstactivity: ok but not ok");

                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(firstActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                Log.d("jobaid", "firstactivity: on Resume [onSuccess]");
                                if (location != null) {
                                    Log.d("jobaid", "firstactivity: on Resume [location!=null]");
                                    lastlocation = location;
                                    LatLng currentLatLng = new LatLng(GlobalClass.currentlat, GlobalClass.currentlon);
                                    Log.d("jobaid","firstactivity hose: "+String.valueOf(currentLatLng.longitude));
                                    markerPlacing(currentLatLng);
                                }
                                else
                                {
                                    Log.d("jobaid", "firstactivity: on Resume [location==null]");

                                    LatLng currentLatLng = new LatLng(GlobalClass.currentlat, GlobalClass.currentlon);
                                    if(GlobalClass.ct==1)
                                    {
                                        GlobalClass.ct=0;
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                Log.d("deb","4");

                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(GlobalClass.currentlat,GlobalClass.currentlon),zoom));

                                            }
                                        }, 2000);
                                    }

                                    markerPlacing(currentLatLng);
                                }
                            }
                        });
            }
            startLocationUpdates();
        }
        else
            alertMethod();

    }

    private void alertMethod() {

        Log.d("jobaid", "firstactivity : alertMethod [alertdialog]");
        final Context context = firstActivity.this;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.GPS_unavailable);
        dialog.setMessage(R.string.GPS_msg);

        dialog.setPositiveButton("GPS settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //check = true;
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

        Log.d("jobaid", "firstactivity : startLocationUpdate");
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, handlerThread.getLooper());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d("jobaid", "firstactivity : onMapReady");
        mMap = googleMap;
        if(user)mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        mMap.setPadding(0,150,0,0);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);

        LatLng jigatala = new LatLng(GlobalClass.currentlat, GlobalClass.currentlon);
        //mMap.addMarker(new MarkerOptions().position(jigatala));
        Log.d("deb","5");

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jigatala, 15f));
        setUpMap();

    }

    private void setUpMap() {



        if (ContextCompat.checkSelfPermission(firstActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(firstActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
        else {

                //progressDialog.setMessage("Searching your bus...");
//                progressDialog.setCanceledOnTouchOutside(false);
//                progressDialog.show();

            Log.d("jobaid", "firstactivity : setUpMap [setup success]");
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(firstActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            Log.d("jobaid", "firstactivity : setUpMap [onSuccess]");
                            if (location != null) {
                                Log.d("jobaid", "firstactivity : setUpMap  [deep success]");
                                lastlocation = location;
                                if(GlobalClass.currentlat!=-1 && GlobalClass.currentlon!=-1)
                                {
                                    currentLatLng = new LatLng(GlobalClass.currentlat,GlobalClass.currentlon);

                                }
                                else
                                    currentLatLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());

                                if(locBus==0)
                                {
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            //Toast.makeText(firstActivity.this,currentLatLng.toString()+" sakib",Toast.LENGTH_LONG).show();
                                            if(GlobalClass.pp==1)
                                            {
                                                GlobalClass.pp=0;
                                                Log.d("deb","6");

                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 15f));

                                            }
                                            else
                                            {
                                                Log.d("deb","7");
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));}


                                        }
                                    }, 2000);
                                }
                                else
                                {
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Log.d("deb","8");

                                            // Toast.makeText(firstActivity.this,currentLatLng.toString()+" sakib",Toast.LENGTH_LONG).show();
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));

                                        }
                                    }, 2000);
                                }


                            }

                        }
                    });

        }
        if(busLocation!=null)
        {
            Log.d("sss","one");
            Log.d("deb","9");

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(busLocation,zoom));

        }

    }

    @Override
    public void onCameraMoveCanceled() {

        Log.d("jobaid", "firstactivity : on CameraMoveCanceled [ouside onCamereMoveCanceled]");
        if (flag) {
            Log.d("jobaid", "firstactivity : onCamereMoveCanceled");
            //flag=false;
            zoom= mMap.getCameraPosition().zoom;
            markerPlacing(mMap.getCameraPosition().target);

        }
    }

    private void markerPlacing(LatLng currentLatLng) {

        if (ActivityCompat.checkSelfPermission(firstActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            return;
        }

        if(user)
        {
            Log.d("deb","10");



            Toast.makeText(firstActivity.this,"hoyna ken",Toast.LENGTH_SHORT).show();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,zoom));

        }
        if(admin) {

            Log.d("deb","11");

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoom));
        }


    }

    private void setMarker(DataSnapshot dataSnapshot) {

        double  lat,lng;
        LatLng la;
        countMarker++;
        Log.d("jobaid","firstActivity:setMarker");
        lat= (double) dataSnapshot.child("latitude").getValue();
        lng = (double) dataSnapshot.child("longitude").getValue();
        busLocation= new LatLng(lat,lng);
        adminLocation.setLatitude(lat);
        adminLocation.setLongitude(lng);
        if(centerLocation!=null)
        {
            dis=adminLocation.distanceTo(centerLocation);
            Log.d("jobaid","firstActivity:centerlocation!=null "+String.valueOf(centerLocation.getLatitude())+ ","+String.valueOf(centerLocation.getLongitude()));
            Log.d("jobaid","firstActivity:centerlocation!=null "+String.valueOf(dis));
            if(dis<=GeofenceSettings1.radius )
            {
                Log.d("jobaid","firstActivity:dis<=");
                alarmflag=false;
                if(oncealarm )
                {
                    oncealarm=false;
                    Intent intent1= new Intent(firstActivity.this,MyReceiver.class);
                    intent1.putExtra("name","BusReminder");
                    PendingIntent p= PendingIntent.getBroadcast(firstActivity.this,0,intent1,0);
                    AlarmManager am= (AlarmManager)getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),p);
                    //busGeofenceAlarm();
                    //busGeofenceAlertDialog();
                    notification();
                }

            }
        }

        la = new LatLng(lat, lng);
        final LatLng LL=new LatLng(lat, lng);
        MarkerOptions mk = new MarkerOptions();
        mk.draggable(false);
        mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        marker = mMap.addMarker(mk
                .position(la));
        marker.setTitle(GlobalClass.BusName+"  "+GlobalClass.BusTime);
        marker.showInfoWindow();
        if(locBus%2==0&&user)
        {
            Log.d("jobaid","markerLocBus");

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("jobaid","markerLocBus22");
                    Log.d("deb","12");

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LL, 15f));
                }
            }, 2000);

            freeze=true;
        }


//        for(DataSnapshot ds: dataSnapshot.getChildren())
//        {
//            Log.d("jobaid","analogy");
//            AdminLocation ad= ds.getValue(AdminLocation.class);
//            lat=ad.getLatitude();
//            lng=ad.getLongitude();
//            la= new LatLng(lat,lng);
//            marker.remove();
//            MarkerOptions mk = new MarkerOptions();
//            mk.draggable(false);
//            mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//            marker = mMap.addMarker(mk
//                    .position(la));
//            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(la, zoom));
//
//
//
//        }

//        if(dataSnapshot.getValue()!=null)
//        {
//
////            lat= (Double) dataSnapshot.child("latitude").getValue();
////            lng=(Double) dataSnapshot.child("longitude").getValue();
////            la= new LatLng(lat,lng);
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(la, zoom));
//
//        }

//        else
//        {
//            Log.d("jobaid","value is null");
//        }











//        if (!mMarkers.containsKey(key)) {
//            mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(location)));
//        } else {
//            mMarkers.get(key).setPosition(location);
//        }
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        for (Marker marker : mMarkers.values()) {
//            builder.include(marker.getPosition());
//        }
        //

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
                // Set the intent that will fire when the user taps the notification_avail
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(firstActivity.this);

// notificationId is a unique int for each notification_avail that you must define
        notificationManager.notify(3, mBuilder.build());
    }

    private void createNotificationChannel() {
        Log.d("jobaid", "firstactivity: createNotificationChannel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel one";
            String description = "this is channel one method";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("2", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification_avail behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Log.d("jobaid","firstactivity : onNavigationItemSelectd [navigation outside]");
        int id = item.getItemId();
        Intent intent = null;

        switch (id) {
            case R.id.nav_busReminder:
                oncealarm=true;
                intent = new Intent(firstActivity.this,GeofenceSettings1.class);
                intent.putExtra("purpose","busReminder");
                startActivity(intent);
                break;
            case R.id.nav_locationAlarm:
                intent = new Intent(firstActivity.this,GeofenceSettings1.class);
                intent.putExtra("purpose","locationAlarm");
                startActivity(intent);
                break;
            case R.id.nav_schedule:
                intent = new Intent(firstActivity.this, RecylerViewActivity.class);
                intent.putExtra("name","Bus Schedule");
                startActivity(intent);
                break;

            case R.id.nav_route:
                intent = new Intent(firstActivity.this, RecylerViewActivity.class);
                intent.putExtra("name","Route And Stoppage");
                startActivity(intent);
                break;

            case R.id.nav_settings:
                intent = new Intent(firstActivity.this, SettingsPage.class);
                intent.putExtra("name","Settings Page");
                startActivity(intent);
                break;

            case R.id.nav_busAdmin:
                intent = new Intent(firstActivity.this, AdminsPage.class);
                intent.putExtra("name","Bus Admin Page");
                startActivity(intent);
                break;

            case R.id.nav_feedback:
                intent = new Intent(firstActivity.this, FeedbackPage.class);
                intent.putExtra("name","Bus Admin Page");
                startActivity(intent);
                break;

            case R.id.nav_about:
                intent = new Intent(firstActivity.this, About.class);
                intent.putExtra("name","About Page");
                startActivity(intent);
                break;

            case R.id.nav_turnoff:
                AlertDialog.Builder builder = new AlertDialog.Builder(firstActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to turn off Admin mode ?");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(firstActivity.this,"GPS signal transmission is turned off",Toast.LENGTH_LONG).show();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            stopService(new Intent(firstActivity.this,AdminBackground.class));
                            //AdminBackground.client.removeLocationUpdates(locationCallback);
                            finishAndRemoveTask();
                            System.exit(0);
                        }
                        else {
                            stopService(new Intent(firstActivity.this,AdminBackground.class));
                            //AdminBackground.client.removeLocationUpdates(locationCallback);
                            finishAffinity();
                        }

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void adminloginToFirebase() {
        // Authenticate with Firebase, and request location updates
        String email = "test123@gmail.com";
        String password = "test123";
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(
                email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(firstActivity.this,"successful",Toast.LENGTH_LONG).show();

                    Log.d("jobaid", "firstactivity : adminloginToFirebase [firebase auth success]");
                    createLocationRequest();
                } else {
                    Log.d("jobaid", "firstactivity : adminloginToFirebase [firebase auth failed]");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("jobaid","firstactivity: onFailure "+e.getLocalizedMessage());
            }
        });
    }

    private void userloginToFirebase() {
        String email = "test123@gmail.com";
        String password ="test123" ;
        // Authenticate with Firebase and subscribe to updates
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    subscribeToUpdates();
                    Log.d("jobaid", "firstactivity : userloginToFirebase [firebase auth success]");
                } else {
                    Log.d("jobaid", "firstactivity: userloginToFirebase [firebase auth failed]");
                }
            }
        });
    }

    private void subscribeToUpdates() {
        // Functionality coming next step
        Log.d("jobaid","firstactivity :subscribeToUpdates");
    }

    public PopupWindow popupWindowDogs(View v) {

        Log.d("jobaid","firstactivity : popupWindows");
        // initialize a pop up window type
        popupWindow = new PopupWindow(firstActivity.this);

        // the drop down list is a list view
        final ListView listViewDogs = new ListView(firstActivity.this);


        // set our adapter and pass our pop up window contents
        if(v.getId()==R.id.busBtn)
        {
            Log.d("jobaid","pippip");
            listViewDogs.setAdapter(dogsAdapter(popUpBus));

        }
        else if(v.getId()==R.id.timeBtn)
        {
            Log.d("jobaid",((GlobalClass)firstActivity.this.getApplication()).BusName);
            Log.d("jobaid","pippipff");
            context=this;
            b = new BusAndTime(((GlobalClass)firstActivity.this.getApplication()).BusName,context);
            time = b.getTime();
            listViewDogs.setAdapter(dogsAdapter(time));

        }

        // set the item click listener
        listViewDogs.setOnItemClickListener( new DogsDropdownOnItemClickListener());


        // some other visual settings
        popupWindow.setFocusable(false);
        popupWindow.setWidth((int)(width*.42));
        popupWindow.setHeight((int)(height*.4));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    private ArrayAdapter<String> dogsAdapter(String dogsArray[]) {

        Log.d("jobaid","firstActivity:dogsAdapter");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_popup, dogsArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position,convertView,parent);

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
                listItem.setTextColor(Color.BLACK);

                return listItem;
            }
        };

        return adapter;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d("jobaid", "firstActivity:onRequest");
        if (requestCode == 123 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("cheeking","one");
                setUpMap();

            } else {
                Log.d("cheeking","two");

                finishAffinity();
                System.exit(0);
            }
        }
// else {
//
//            Log.d("cheeking","three");
//
//            finishAffinity();
//            System.exit(0);
//        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.fabloc && admin)
        {
            zoom=15f;
            Log.d("deb","13");

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,zoom));

        }

        if (v.getId() == R.id.fabloc&&user) {

            //region network checking
            if(!isNetworkConnected(firstActivity.this))
            {
                Log.d("sss","two");
                Log.d("deb","14");

                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,zoom));
                Toast.makeText(firstActivity.this,"Network Connection Required",Toast.LENGTH_LONG).show();
                return ;
            }

            if(userselectionBus.equals("BUS")||userselectionTime.equals("TIME"))
            {
                Toast.makeText(this,"You haven't searched any bus yet",Toast.LENGTH_LONG).show();
                return ;
            }
            //endregion
            Log.d("jobaid","jobaid");
            ++locBus;

//            if(locBus==1)
//            {
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        Log.d("sss","three");
//                        if(GlobalClass.first==0)
//                        fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_bus));
//
//                        // Toast.makeText(firstActivity.this,currentLatLng.toString()+" sakib",Toast.LENGTH_LONG).show();
//                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 15f));
//
//                    }
//                }, 2000);
//            }
            if(locBus%2==1)
            {
                Log.d("jobaid","firstactivity:fa bLoc " + locBus);
//                if(locBus==1)
//                {
//
//                    Log.d("deb","15");
//
//                    fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_bus));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,zoom));
//                    return ;}

                Log.d("deb","16");
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(GlobalClass.currentlat,GlobalClass.currentlon),15f));
                fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_bus));
                freeze=false;
            }
            else
            {


                Log.d("jobaid","firstactivity:fabBus " + locBus);
                zoom=15f;
                if(locBus==2)
                {
                    ++locBus;
                    Log.d("pppp","six");
                    Log.d("sss","six");

                    if(GlobalClass.first==0)
                    {
                        fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_location));
                        Log.d("deb","17");

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(busLocation,zoom));
                        ++locBus;
                        return ;

                    }
                    Log.d("deb","18");

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,zoom));
                    fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_bus));

                    return ;
                }

                Log.d("deb","19");

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(busLocation,zoom));
                fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_location));

                freeze=true;

            }


        }
        if (v.getId() == R.id.fabtraffic) {

            if(!isNetworkConnected(firstActivity.this))
            {
                Toast.makeText(firstActivity.this,"Network Connection Required",Toast.LENGTH_LONG).show();
                return ;
            }
            if (mMap.isTrafficEnabled())
            {
                mMap.setTrafficEnabled(false);
                Log.d("jobaid","firstactivity : onclick [disabled]");

            }
            else
            {
                mMap.setTrafficEnabled(true);

                Log.d("jobaid","firstactivity : onclick [enabled]");

            }
        }
    }

    public  boolean isNetworkConnected(Context context) {

        Log.d("jobaid","StartActivity: is NetworkConnected [outside isNetworkConnected]");
        prechecking= false;
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

        if(admin)
            img.setVisibility(View.VISIBLE);
        aftercameramove = mMap.getCameraPosition().target;
        finalLocation=true;
    }

    @Override
    public void onCameraMove() {

        aftercameramove = mMap.getCameraPosition().target;
        zoom= mMap.getCameraPosition().zoom;

        // Log.d("jobaid", "firstActivity:OnCameramove " +String.valueOf(zoom));
        if(admin)
            img.setVisibility(View.VISIBLE);


    }

    @Override
    public void onDestroy() {
        Log.d("jobaid","firstActivity:onDestroy");


        super.onDestroy();

    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder = new AlertDialog.Builder(firstActivity.this);
        if(admin)
        {
            builder.setTitle("Caution!!");
            builder.setMessage("Admin mode requires background running service." +
                    "Minimize the app and don't clear from recent list.");


            builder.setNegativeButton("GOT IT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.show();
        }
        else
        {
            builder.setTitle("Confirmation");
            builder.setMessage("If location alarm or bus reminder is running , you are requested to minimize the app.  Else you may exit.");

            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAndRemoveTask();
                        System.exit(0);
                    }
                    else {
                        finishAffinity();
                    }

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.show();
        }

        //super.onBackPressed();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId())
//        {
//            case R.id.tool_notification:
//                Toast.makeText(this,"Notification Toast",Toast.LENGTH_LONG).show();
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
