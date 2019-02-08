package com.example.asus.remindmemyself;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.HandlerThread;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class firstActivity extends FragmentActivity implements View.OnClickListener,
        OnMapReadyCallback, GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener ,
        NavigationView.OnNavigationItemSelectedListener{

    //region variable
    private static int interval =5000, fastinterval = 2000;
    public StoppageMarkerPosition sp= new StoppageMarkerPosition();
    private DrawerLayout drawer;
    private int countMarker=0;
    private GoogleMap mMap;
    int height,width;
    public PopupWindow popupWindowDogs;
    public Button buttonBus,buttonTime;
    public String popUpBus [] = {"Taranga","Choitali","Ullash","Boishakhi","Choitali","Ullash","Boishakhi"};
    public String popUpTime [] = {"7:00 am","7:30 am","8:00 am","7:00 am","7:30 am","8:00 am"};
    private boolean locationUpdateState = false, permit = false, locationsettings = false, check = false;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Location lastlocation;
    final HandlerThread handlerThread= new HandlerThread("RequestLocation");;
    private LatLng currentLatLng = null, aftercameramove;
    private boolean flag = true,admin=false,user=false,finalLocation=false;
    private Marker marker;
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private int count = 0, call = 0;
    private ImageView img;
    private LocationManager lm;
    private AlertDialog.Builder dialog;
    private AlertDialog alert;



    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region oncreate declarations
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        img = (ImageView) findViewById(R.id.iconid);
        Log.d("jobaid", "onCreate");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabloc);
        FloatingActionButton fabt = (FloatingActionButton) findViewById(R.id.fabtraffic);
        FloatingActionButton fabn=(FloatingActionButton)findViewById(R.id.fabnavigation);
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

       if(!isNetworkConnected(this))
       {
           networkAlert(this);
       }


           fab.setOnClickListener(this);
           fabt.setOnClickListener(this);
           fabn.setOnClickListener(this);




        //endregion

    if((getIntent().getStringExtra("name")).equals("admin"))
        admin=true;
    if((getIntent().getStringExtra("name")).equals("user"))
        user=true;


        if(admin)
        {
            Log.d("jobaid","admintrue");
            adminloginToFirebase();

        }
        if(user)
        {
            userloginToFirebase();

        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        // initialize pop up window
//        popupWindowDogs = popupWindowDogs();

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.busBtn:
                        // show the list view as dropdown
                        popupWindowDogs = popupWindowDogs(v);
                        popupWindowDogs.showAsDropDown(v, -5, 0);
                        break;
                    case R.id.timeBtn:
                        // show the list view as dropdown
                        popupWindowDogs = popupWindowDogs(v);
                        popupWindowDogs.showAsDropDown(v, -5, 0);
                        break;
                }
            }
        };

        // our button
        buttonBus = (Button) findViewById(R.id.busBtn);
        buttonTime=(Button)findViewById(R.id.timeBtn);
        buttonBus.setOnClickListener(handler);
        buttonTime.setOnClickListener(handler);


        handlerThread.start();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AdminLocation");

                lastlocation = locationResult.getLastLocation();

                //Toast.makeText(firstActivity.this,lastlocation.toString(),Toast.LENGTH_LONG).show();
//                if(finalLocation)
//                currentLatLng = new LatLng(aftercameramove.latitude, aftercameramove.longitude);
//                else
                    currentLatLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());

                Log.d("jobaid", "callback");
                if(admin)
                {
                    Log.d("jobaid","b4callback");
                    if(finalLocation)
                    {
                        /// location by camere movement
                        //AdminLocation al= new AdminLocation(aftercameramove.latitude,aftercameramove.longitude);
                        AdminLocation al= new AdminLocation(lastlocation.getLatitude(), lastlocation.getLongitude());
                        Log.d("jobaid", "callback2");
                        ref.setValue(al);
                    }
                    else
                    {
                        AdminLocation al= new AdminLocation(lastlocation.getLatitude(),lastlocation.getLongitude());
                        Log.d("jobaid", "callback2");
                        ref.setValue(al);
                    }

                    //ref.child("latitude").setValue(lastlocation.getLatitude());
                    //ref.child("longitude").setValue(lastlocation.getLongitude());

                }
                if(user)
                {

                    Log.d("jobaid","inside user");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(countMarker>0)
                                marker.remove();
                            setMarker(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
//                if(flag)
//                {
//                    flag=false;
//                    markerPlacing(currentLatLng);
//
//
//                }

                //marker.remove();
            }
        };

        createLocationRequest();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Log.d("sakib","navigation");
        int id = item.getItemId();
        Intent intent = null;

        switch (id) {
            case R.id.nav_busGeofence:
                intent = new Intent(this,GeofenceSettings1.class);
                startActivity(intent);
                break;
//            case R.id.nav_settings:
//                intent = new Intent(this, ProfileActivity.class);
//                startActivity(intent);
//                break;
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

    private void setMarker(DataSnapshot dataSnapshot) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once
//        String key = dataSnapshot.getKey();
       // HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

        Log.d("jobaid","inside setMarker");

        double  lat,lng;
        LatLng la;

        countMarker++;
        lat= (double) dataSnapshot.child("latitude").getValue();
        lng = (double) dataSnapshot.child("longitude").getValue();
        la = new LatLng(lat, lng);
        //marker.remove();
        MarkerOptions mk = new MarkerOptions();
        mk.draggable(false);
        mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        marker = mMap.addMarker(mk
                .position(la));

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
//            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(la, 15f));
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
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(la, 15f));
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

    private void adminloginToFirebase() {
        // Authenticate with Firebase, and request location updates
        String email = "test123@gmail.com";
        String password = "test123";
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("jobaid", "firebase auth success");
                    createLocationRequest();
                } else {
                    Log.d("jobaid", "firebase auth failed");
                }
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
                    Log.d("jobaid", "firebase auth success");
                } else {
                    Log.d("jobaid", "firebase auth failed");
                }
            }
        });
    }

    private void subscribeToUpdates() {
        // Functionality coming next step
    }

    private void networkAlert(firstActivity firstActivity) {

        Log.d("jobaid", "networkalert");
        final Context context = firstActivity.this;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(R.string.network_msg);
        dialog.setTitle(R.string.network_unavailable);
        dialog.setPositiveButton("Network settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                check = true;
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                context.startActivity(intent);
            }
        });
        alert = dialog.create();
        alert.show();

    }

    public static boolean isNetworkConnected(Context context) {
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
        return result;
    }

    public PopupWindow popupWindowDogs(View v) {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(this);

        // the drop down list is a list view
        ListView listViewDogs = new ListView(this);

        // set our adapter and pass our pop up window contents
        if(v.getId()==R.id.busBtn)
        {
            listViewDogs.setAdapter(dogsAdapter(popUpBus));
            listViewDogs.setDivider(null);
        }
        else if(v.getId()==R.id.timeBtn)
        {
            listViewDogs.setAdapter(dogsAdapter(popUpTime));
            listViewDogs.setDivider(null);
        }

        // set the item click listener
        listViewDogs.setOnItemClickListener( new DogsDropdownOnItemClickListener());

        // some other visual settings
        popupWindow.setFocusable(false);
        popupWindow.setWidth((int)(width*.45));
        popupWindow.setHeight((int)(height*.4));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //popupWindow.setBackgroundDrawable();

        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    /*
     * adapter where the list values will be set
     */
    private ArrayAdapter<String> dogsAdapter(String dogsArray[]) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dogsArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // setting the ID and text for every items in the list
                String item = getItem(position);
                //String[] itemArr = item.split("::");
                //String text = itemArr[0];
                //String id = itemArr[1];

                // visual settings for the list item
                TextView listItem = new TextView(firstActivity.this);

                listItem.setText(item);
                //listItem.setTag(id);
                listItem.setHeight(150);
                listItem.setMinHeight(150);
                listItem.setTextSize(20);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.BLACK);

                return listItem;
            }
        };

        return adapter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("jobaid", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Log.d("jobaid", "onResume");

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("jobaid", "b4 locationupdate");
            startLocationUpdates();
        } else
            alertMethod();


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("jobaid", "onPause");

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            alert.dismiss();
            alert.cancel();
        }
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                Log.d("jobaid", "onSuccess");
                                if (location != null) {
                                    Log.d("jobaid", "setup deep success");
                                    lastlocation = location;
                                    LatLng currentLatLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
                                    markerPlacing(currentLatLng);
                                }

                            }
                        });
            }
        }
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

    }

    private void createLocationRequest() {
        Log.d("jobaid", "createlocation");

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

    private void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }

        Log.d("jobaid", "startLocationUpdate");
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, handlerThread.getLooper());
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d("jobaid", "onMapReady");
        mMap = googleMap;
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
            Log.d("jobaid", "setup success");
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            Log.d("jobaid", "onSuccess");
                            if (location != null) {
                                Log.d("jobaid", "setup deep success");
                                lastlocation = location;
                                LatLng currentLatLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
                                markerPlacing(currentLatLng);
                            }

                        }
                    });

        }


    }

    private void alertMethod() {

        Log.d("jobaid", "alertdialog");
        final Context context = firstActivity.this;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.GPS_unavailable);
        dialog.setMessage(R.string.GPS_msg);

        dialog.setPositiveButton("GPS settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                check = true;
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alert = dialog.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.fabloc) {
            markerPlacing(currentLatLng);
        }
        if (v.getId() == R.id.fabtraffic) {


            for(int i=0;i<sp.TarangaLat.length;i++)
            {
                MarkerOptions mk = new MarkerOptions();
                mk.draggable(false);
                mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                marker = mMap.addMarker(mk
                        .position(new LatLng(sp.TarangaLat[i],sp.TarangaLon[i]))
                        .title(sp.TarangaStopName[i]));

                marker.showInfoWindow();
            }


            //marker.setSnippet("hello");
            if (mMap.isTrafficEnabled())
            {
                mMap.setTrafficEnabled(false);
                Log.d("para","disabled");

            }
            else
            {
                mMap.setTrafficEnabled(true);
                Log.d("para","enabled");

            }
        }
        if(v.getId()==R.id.fabnavigation)
        {


            drawer.openDrawer(Gravity.LEFT);
        }
    }

    private void markerPlacing(LatLng currentLatLng) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


//        MarkerOptions mk = new MarkerOptions();
//        mk.draggable(false);
//        mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
//        //mk.getIcon()
//
//
//         marker = mMap.addMarker(mk
//                .position(currentLatLng)
//                .title("jobaid"));
//
//        marker.showInfoWindow();

        //marker.setSnippet("hello");
        Log.d("jobaid", "markerPlacing");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));


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
        img.setVisibility(View.VISIBLE);
        Log.d("jobaid", "idle");

        aftercameramove = mMap.getCameraPosition().target;
        finalLocation=true;
    }

    @Override
    public void onCameraMove() {

        Log.d("jobaid", "move");
        img.setVisibility(View.VISIBLE);
        /// to remove previous marker due to continuos calling of the markerplacing

        //marker.remove();

    }

    @Override
    public void onCameraMoveCanceled() {

        Log.d("jobaid", "ouside sb");
        if (flag) {
            Log.d("jobaid", "sb");
            //flag=false;
            markerPlacing(mMap.getCameraPosition().target);
        }
    }



}
