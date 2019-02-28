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
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
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
    private static int countMarker=0,locBus=0;
    private GoogleMap mMap;
    private static float zoom=15f;
    public boolean freeze=true,secondflag=false;
    public static boolean oncealarm=false;
    int height,width;
    public static PopupWindow popupWindow;
    private ProgressDialog progressDialog;
    //final long[] pattern = {0, 1000, 1000};
    public static Location centerLocation=null,adminLocation;
    public PopupWindow popupWindowDogs;
    private static ValueEventListener listener;
    public Button buttonBus,buttonTime,buttonSearch;
    public String popUpBus [] = {"Anando","Boishakhi","Boshonto","Chittagong Road","Choitaly","Falguni","Hemonto","Ishakha","Kinchit","Khonika","Moitree/null","Srabon","Taranga","Ullash","Wari"};
    //public String popUpTime [] = {"7:00 am","7:30 am","8:00 am","7:00 am","7:30 am","8:00 am"};
    private boolean locationUpdateState = false, permit = false, locationsettings = false, check = false;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private FloatingActionButton fab, fabt;
    private Location lastlocation;
    private Menu menu;
    final HandlerThread handlerThread= new HandlerThread("RequestLocation");;
    private static LatLng currentLatLng = null, aftercameramove,busLocation;
    public static boolean flag = true,admin=false,user=false,finalLocation=false,alarmflag=true,srch=false;
    private Marker marker;
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private static  int count = 0, call = 0,indexNumber=0;
    private ImageView img;
    private TextView tv;
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
    private static DatabaseReference ref;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    DogsDropdownOnItemClickListener d= new DogsDropdownOnItemClickListener();



    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //region oncreate declarations
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Log.d("statetracking", "onCreate");

        tv= findViewById(R.id.mywidget);
        tv.setText("Your are in admin mode. "+"              "+" You are in admin mode.");
        tv.setSelected(true);

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.busBtn:
                        buttonBus.setText(userselectionBus);
                        popupWindowDogs = popupWindowDogs(v);
                        popupWindowDogs.showAsDropDown(v);
                        break;
                    case R.id.timeBtn:
                        // show the list view as dropdown
                        buttonTime.setText(userselectionTime);
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
                        locBus=0;
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
                            intent.putExtra("TIME",DogsDropdownOnItemClickListener.BusTime);
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
        Log.d("jobaid", "firstactivity:onCreate");
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

        fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_location));
        fabt.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.traffic));


        fab.setOnClickListener(this);
        fabt.setOnClickListener(this);

        //endregion

        if((getIntent().getStringExtra("name")).equals("admin"))
        {

            adminselectionBus=getIntent().getStringExtra("BUSNAME");
            adminselectionTime=getIntent().getStringExtra("TIME");
            this.setTitle(adminselectionBus+" "+adminselectionTime);
            admin=true;
        }
        if((getIntent().getStringExtra("name")).equals("user"))
        {
            tv.setVisibility(View.GONE);
            userselectionBus = getIntent().getStringExtra("BUSNAME");
            userselectionTime=getIntent().getStringExtra("TIME");
            this.setTitle(userselectionBus+" "+userselectionTime);

            buttonBus.setText(userselectionBus);
            buttonTime.setText(userselectionTime);
            user=true;
            Log.d("parbo",userselectionBus + " " + userselectionTime);
        }

        if(admin)
        {

            linearLayout.setVisibility(View.GONE);
            adminloginToFirebase();
        }
        if(user)
        {
            ref = FirebaseDatabase.getInstance().getReference();

            listener= new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(countMarker>0 && marker!=null)
                        marker.remove();
                    Log.d("jobaid","firstactivity : asfw");
                    //Log.d("cheque",GlobalClass.BusName+" ** " +DogsDropdownOnItemClickListener.BusTime);


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
            Log.d("him","them");
            img.setVisibility(View.GONE);
            userloginToFirebase();
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        if(admin)
        {
            menu.findItem(R.id.nav_busReminder).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);



        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                ref = FirebaseDatabase.getInstance().getReference();

                lastlocation = locationResult.getLastLocation();

                ((GlobalClass)firstActivity.this.getApplication()).lat=lastlocation.getLatitude();
                ((GlobalClass)firstActivity.this.getApplication()).lon=lastlocation.getLongitude();

                currentLatLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
                if(admin)
                {
                    Log.d("jobaid","firstactivity : b4callback");
                    if(finalLocation)
                    {
                        /// location by camera movement
                        AdminLocation al= new AdminLocation(aftercameramove.latitude,aftercameramove.longitude);
                        Toast.makeText(firstActivity.this,"GPS signal is transmitting",Toast.LENGTH_SHORT).show();
                        Log.d("jobaid", "firstactivity : callback2");
                        ref.child("Admin").child("Location")
                                .child(adminselectionBus)
                                .child(adminselectionTime)
                                .setValue(al);
                    }
                    else
                    {
                        Toast.makeText(firstActivity.this,"GPS signal is transmitting",Toast.LENGTH_LONG).show();
                        AdminLocation al= new AdminLocation(lastlocation.getLatitude(),lastlocation.getLongitude());
                        Log.d("jobaid", "firstactivity : callback2");
                        ref.child("Admin").child("Location")
                                .child(adminselectionBus)
                                .child(adminselectionTime)
                                .setValue(al);
                    }
                }
//                if(user && srch)
//                {
//
//                    listener= new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            if(countMarker>0)
//                                marker.remove();
//                            Log.d("jobaid","firstactivity : asfw");
//                            Log.d("cheque",String.valueOf(call)+"  "+ GlobalClass.BusName+" ** " +DogsDropdownOnItemClickListener.BusTime);
//
//
//                            setMarker(dataSnapshot);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    };
//                    //Log.d("cheque",userselectionBus+" " +userselectionTime);
//                    //progressDialog.dismiss();
//                    Log.d("jobaid","firstactivity : inside user");
//                    ref.child("Admin").child("Location")
//                            .child(userselectionBus)
//                            .child(userselectionTime)
//                            .addValueEventListener(listener);
//                }

            }
        };

        createLocationRequest();

    }

    private void createLocationRequest() {
        Log.d("jobaid", "firstactivity: createLocationRequest");

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(interval);
        locationRequest.setFastestInterval(fastinterval);
//        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
//        {
//            startLocationUpdates();
//        }
//        else
//        {
//            Log.d("jobaid","alertdialog");
//            final Context context= firstActivity.this;
//            String msg="Please enable your GPS/Location Service";
//            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//            dialog.setMessage(msg);
//
//            dialog.setPositiveButton("GPS settings", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    check=true;
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    context.startActivity(intent);
//                }
//            });
//            alert = dialog.create();
//            alert.show();
//        }

//         builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//
//         client = LocationServices.getSettingsClient(this);
//         task = client.checkLocationSettings(builder.build());
//
//        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
//            @Override
//            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//
//                 Log.d("jobaid","task success");
//                locationUpdateState = true;
//                locationsettings=true;
//                startLocationUpdates();
//            }
//        });
//
//
//        task.addOnFailureListener(this, new OnFailureListener() {
////            @Override
////            public void onFailure(@NonNull Exception e) {
////
////                Log.d("jobaid","failure");
////
////                if (e instanceof ResolvableApiException) {
////                    // Location settings are not satisfied, but this can be fixed
////                    // by showing the user a dialog.
////                    Log.d("jobaid","resolvable failure");
////
////
////                    try {
////                        // Show the dialog by calling startResolutionForResult(),
////                        // and check the result in onActivityResult().
////                        Log.d("jobaid","try failure");
////
////                        ResolvableApiException resolvable = (ResolvableApiException) e;
////                        resolvable.startResolutionForResult(firstActivity.this,
////                                2);
////                    } catch (IntentSender.SendIntentException sendEx) {
////                        Log.d("jobaid","catched failure");
////
////                        // Ignore the error.
////                    }
////                }
////            }
////        });
//@Override
//public void onFailure(@NonNull Exception e) {
//
//    locationsettings=false;
//
//    int statusCode = ((ApiException) e).getStatusCode();
//    final Context context= firstActivity.this;
//    switch (statusCode) {
//        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//           Log.w("jobaid", "Location settings not satisfied, attempting resolution intent");
////            try {
////                ResolvableApiException resolvable = (ResolvableApiException) e;
////                resolvable.startResolutionForResult(firstActivity.this,2);
////            } catch (IntentSender.SendIntentException sendIntentException) {
////                Log.e("jobaid", "Unable to start resolution intent");
////            }
//            String msg="Please enable your GPS/Location Service";
//            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//            dialog.setMessage(msg);
//
//            dialog.setPositiveButton("GPS settings", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    check=true;
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    context.startActivity(intent);
//                }
//            });
//            AlertDialog alert = dialog.create();
//            alert.show();
//
////            dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////
////                }
////            });
//            break;
//        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//            Log.w("jobaid", "Location settings not satisfied and can't be changed");
//            break;
//    }
//}
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("jobaid", "firstactivity : onStart");

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

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                Log.d("jobaid", "firstactivity: on Resume [onSuccess]");
                                if (location != null) {
                                    Log.d("jobaid", "firstactivity: on Resume [location!=null]");
                                    lastlocation = location;
                                    LatLng currentLatLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }

        Log.d("jobaid", "firstactivity : startLocationUpdate");
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, handlerThread.getLooper());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Log.d("jobaid", "firstactivity : onMapReady");
        mMap = googleMap;
        if(user)mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        mMap.setPadding(0,150,0,0);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        setUpMap();

    }

    private void setUpMap() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
        else {
            Log.d("jobaid", "firstactivity : setUpMap [setup success]");
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            Log.d("jobaid", "firstactivity : setUpMap [onSuccess]");
                            if (location != null) {
                                Log.d("jobaid", "firstactivity : setUpMap  [deep success]");
                                lastlocation = location;
                                currentLatLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());

                                if(locBus==0)
                                {
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            //Toast.makeText(firstActivity.this,currentLatLng.toString()+" sakib",Toast.LENGTH_LONG).show();
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));

                                        }
                                    }, 2000);
                                }
                                else
                                {
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                           // Toast.makeText(firstActivity.this,currentLatLng.toString()+" sakib",Toast.LENGTH_LONG).show();
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));

                                        }
                                    }, 1000);
                                }


                            }

                        }
                    });

        }
        if(busLocation!=null)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(busLocation,zoom));

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            return;
        }

        if(user)
        {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,zoom));

        }
        if(admin)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoom));


    }

    private void setMarker(DataSnapshot dataSnapshot) {

        double  lat,lng;
        LatLng la;
        countMarker++;
        Log.d("mi","six");
        lat= (double) dataSnapshot.child("latitude").getValue();
        lng = (double) dataSnapshot.child("longitude").getValue();
        busLocation= new LatLng(lat,lng);
        adminLocation.setLatitude(lat);
        adminLocation.setLongitude(lng);
        if(centerLocation!=null)
        {
            dis=adminLocation.distanceTo(centerLocation);
            Log.d("like",String.valueOf(centerLocation.getLatitude())+ ","+String.valueOf(centerLocation.getLongitude()));

            if(dis<=GeofenceSettings1.radius )
            {
                Log.d("liker","hose2");
                alarmflag=false;
                if(oncealarm )
                {
                    oncealarm=false;
                    Intent intent1= new Intent(this,MyReceiver.class);
                    intent1.putExtra("name","LocationAlarm");
                    PendingIntent p= PendingIntent.getBroadcast(this,0,intent1,0);
                    AlarmManager am= (AlarmManager)getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),p);
                    //busGeofenceAlarm();
                    //busGeofenceAlertDialog();
                    notification();
                }


            }
        }

        la = new LatLng(lat, lng);

        MarkerOptions mk = new MarkerOptions();
        mk.draggable(false);
        mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        marker = mMap.addMarker(mk
                .position(la));
        if(locBus%2==0&&user)
        {
            Log.d("vejal","fao");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(la, zoom));
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
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.pin)
                .setContentTitle("Bus Notification")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
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
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private   void busGeofenceAlertDialog()  {

        Log.d("jobaid", "firstactivity : alertdialog");
        dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Bus Notification");
        dialog.setIcon(R.drawable.bus_enter);
        dialog.setMessage(R.string.Bus_msg);



        dialog.setPositiveButton("Stop Alarm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                defaultRingtone.stop();
                myVib.cancel();

            }
        });
        Log.d("vox","rashidul");
        alert =dialog.create();
        alert.show();
//        alert.show();
//        if (!firstActivity.this.isFinishing()){
//            alert.show();
//        }
//        else                     alert.show();

        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
    }

    private void busGeofenceAlarm()  {

        uri = RingtoneManager.getActualDefaultRingtoneUri(firstActivity.this.getApplicationContext(), RingtoneManager.TYPE_ALARM);
        defaultRingtone = RingtoneManager.getRingtone(firstActivity.this, uri);
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        if(am.getRingerMode()== AudioManager.RINGER_MODE_NORMAL)
        {
            defaultRingtone.play();
            //myVib.vibrate(pattern,0);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(myVib.hasVibrator())
                    {
                        alert.dismiss();
                        alert.cancel();
                        Log.d("oops","oin");
                        myVib.cancel();
                        defaultRingtone.stop();
                    }

                }
            },20000);
        }
        else if(am.getRingerMode()==AudioManager.RINGER_MODE_VIBRATE || am.getRingerMode()==AudioManager.RINGER_MODE_SILENT)
        {

            Log.d("oops","oops");
            defaultRingtone.play();
            //myVib.vibrate(pattern,0);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                      if(myVib.hasVibrator())
                      {
                          Log.d("oops","oin");

                          alert.dismiss();
                          alert.cancel();
                          myVib.cancel();
                      }

                }
            }, 20000);

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

                intent = new Intent(this,GeofenceSettings1.class);
                intent.putExtra("purpose","busReminder");
                startActivity(intent);
                break;
            case R.id.nav_locationAlarm:
                intent = new Intent(this,GeofenceSettings1.class);
                intent.putExtra("purpose","locationAlarm");
                startActivity(intent);
                break;
            case R.id.nav_schedule:
                intent = new Intent(this, RecylerViewActivity.class);
                intent.putExtra("name","Bus Schedule");
                startActivity(intent);
                break;

            case R.id.nav_route:
                intent = new Intent(this, RecylerViewActivity.class);
                intent.putExtra("name","Route And Stoppage");
                startActivity(intent);
                break;
//            case R.id.nav_logout:
//                AlertDialog.Builder  builder= new AlertDialog.Builder(home_page.this);
//                builder.setIcon(R.drawable.logout);
//                builder.setMessage("Do you want to logout?");
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finishAffinity();
//                        System.exit(0);
//                    }
//                });
//                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                AlertDialog alert= builder.create();
//
//                alert.show();
//
//
//                break;

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
                    Toast.makeText(firstActivity.this,"Not successful",Toast.LENGTH_LONG).show();
                    Log.d("jobaid", "firstactivity : adminloginToFirebase [firebase auth failed]");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("mistake",e.getLocalizedMessage());
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
         popupWindow = new PopupWindow(this);

        // the drop down list is a list view
        final ListView listViewDogs = new ListView(this);


        // set our adapter and pass our pop up window contents
        if(v.getId()==R.id.busBtn)
        {
            listViewDogs.setAdapter(dogsAdapter(popUpBus));

        }
        else if(v.getId()==R.id.timeBtn)
        {
            Log.d("jobaid",((GlobalClass)firstActivity.this.getApplication()).BusName);
            BusAndTime b = new BusAndTime(((GlobalClass)firstActivity.this.getApplication()).BusName);
            listViewDogs.setAdapter(dogsAdapter(b.getTime()));

        }

        // set the item click listener
        listViewDogs.setOnItemClickListener( new DogsDropdownOnItemClickListener());


        // some other visual settings
        popupWindow.setFocusable(false);
        popupWindow.setWidth((int)(width*.45));
        popupWindow.setHeight((int)(height*.4));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    private ArrayAdapter<String> dogsAdapter(String dogsArray[]) {

        Log.d("jobaid","dogsAdapter");
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

        Log.d("jobaid", "onRequest");
        if (requestCode == 123 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpMap();

            } else {

                finishAffinity();
                System.exit(0);
            }
        } else {

            finishAffinity();
            System.exit(0);
        }
    }

//    private void alertMethod() {
//
//        Log.d("jobaid", "firstactivity : alertMethod [alertdialog]");
//        final Context context = firstActivity.this;
//        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//        dialog.setTitle(R.string.GPS_unavailable);
//        dialog.setMessage(R.string.GPS_msg);
//
//        dialog.setPositiveButton("GPS settings", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                check = true;
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                context.startActivity(intent);
//
//            }
//        });
//        alert = dialog.create();
//        alert.show();
//        alert.setCanceledOnTouchOutside(false);
//        alert.setCancelable(false);
//    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.fabloc && admin)
        {
            zoom=15f;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,zoom));

        }

        if (v.getId() == R.id.fabloc&&user) {
            ++locBus;

                 if(locBus==1)
                 {
                     final Handler handler = new Handler();
                     handler.postDelayed(new Runnable() {
                         @Override
                         public void run() {

                             // Toast.makeText(firstActivity.this,currentLatLng.toString()+" sakib",Toast.LENGTH_LONG).show();
                             mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 15f));

                         }
                     }, 1000);
                 }
                if(locBus%2==1)
                {
                    Log.d("hayre","amar " + locBus);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,zoom));
                    fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_bus));
                    markerPlacing(currentLatLng);
                    freeze=false;
                }
                else
                {

                    Log.d("hayre","kopal " + locBus);
                    zoom=15f;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(busLocation,zoom));
                    fab.setImageDrawable(ContextCompat.getDrawable(firstActivity.this, R.drawable.my_location));

                    freeze=true;

                }


        }
        if (v.getId() == R.id.fabtraffic) {


//            for(int i=0;i<sp.TarangaLat.length;i++)
//            {
//                MarkerOptions mk = new MarkerOptions();
//                mk.draggable(false);
//                mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
//                marker = mMap.addMarker(mk
//                        .position(new LatLng(sp.TarangaLat[i],sp.TarangaLon[i]))
//                        .title(sp.TarangaStopName[i]));
//
//                marker.showInfoWindow();
//            }


            //marker.setSnippet("hello");
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
//        if(v.getId()==R.id.fabnavigation)
//        {
//
//
//            drawer.openDrawer(Gravity.LEFT);
//        }
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
        //currentLatLng=mMap.getCameraPosition().target;
        if(admin)
            img.setVisibility(View.VISIBLE);
        //Log.d("jobaid", "idle");

        aftercameramove = mMap.getCameraPosition().target;
        finalLocation=true;
    }

    @Override
    public void onCameraMove() {

        aftercameramove = mMap.getCameraPosition().target;
        zoom= mMap.getCameraPosition().zoom;

        //Log.d("jobaid", "move " +String.valueOf(zoom));
        if(admin)
            img.setVisibility(View.VISIBLE);
        /// to remove previous marker due to continuos calling of the markerplacing

        //marker.remove();

    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("jobaid", "firstactivity  : onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("statetracking", "firstactivity : onDestroy");

    }
}