package com.anything.asus.remindmemyself;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class GeofenceSettings1 extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener,

        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener {

    //region varible
    private TextView instructionTitleView, instructionSubtitleView, radiusDescriptionView;
    private SeekBar radiusBarSeekbar;
    private GoogleMap mMap;
    private Button continue1;
    public static LatLng geofenceCentre;
    private ImageView marker;
    private Geofence geofence;
    private boolean first = true, second = false;
    private int fenceRadius;
    public static Context context;
    private LinearLayout linearLayout;
    private View autocomplete;
    private TextView textView;

    private FloatingActionButton fab2;
    public static AlertDialog alert;
    public static String purpose;

    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static float GEOFENCE_RADIUS = 300.0f; // in meters
    public static int radius,finalradius;


    //endregion
    String TAG ="tauhid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence_settings1);


        autocomplete=(View) findViewById(R.id.autocomplete_fragment);
        textView=(TextView)findViewById(R.id.textinfo);
        textView.setVisibility(View.GONE);
        // Initialize Places.
        Places.initialize(getApplicationContext(),"AIzaSyCxhbMAoJp-6CUVhAgn5SUH0kDg0j9Z1fA");
        //Places.initialize(getApplicationContext(), );
        // Create a new Places client instance.
        //PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                LatLng search_latlng= place.getLatLng();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(search_latlng, 15f));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });

        purpose = getIntent().getStringExtra("purpose");
        if(purpose.equals("busReminder"))
        {
            this.setTitle("Bus Reminder");
            setContext(this);
        }
        else if(purpose.equals("locationAlarm"))
        {
            this.setTitle("Location Alarm");
            setContext(this);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fab2 = (FloatingActionButton) findViewById(R.id.fabloc2);
        fab2.setOnClickListener(this);

        linearLayout= findViewById(R.id.container);
        instructionTitleView = (TextView) findViewById(R.id.instructionTitle);
        instructionSubtitleView = (TextView) findViewById(R.id.instructionSubtitle);
        radiusBarSeekbar = findViewById(R.id.radiusBar);
        continue1 = (Button) findViewById(R.id.continue1);
        radiusDescriptionView = (TextView) findViewById(R.id.radiusDescription);
        marker = findViewById(R.id.marker);



        instructionTitleView.setVisibility(View.VISIBLE);
        instructionSubtitleView.setVisibility(View.VISIBLE);
        radiusBarSeekbar.setVisibility(View.GONE);
        radiusDescriptionView.setVisibility(View.GONE);

        showConfigureLocationStep();

}

    private void showConfigureLocationStep() {

        marker.setVisibility(View.VISIBLE);
        instructionTitleView.setVisibility(View.VISIBLE);
        instructionSubtitleView.setVisibility(View.VISIBLE);
        radiusBarSeekbar.setVisibility(View.GONE);
        radiusDescriptionView.setVisibility(View.GONE);
       // messageText.setVisibility(View.GONE);

        instructionTitleView.setText(getString(R.string.instruction_where_description));
        instructionSubtitleView.setText(getString(R.string.instruction_where_subtitle_description));

        continue1.setOnClickListener(this);

        //showReminderUpdate();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);


        if(GlobalClass.locAlarm==0 && GlobalClass.busAlarm==0)
        {
            LatLng jigatala = new LatLng(GlobalClass.currentlat, GlobalClass.currentlon);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jigatala, 15f));
            return;
        }

        if(GlobalClass.busAlarm==1 && purpose.equals("busReminder"))
        {
            if(GlobalClass.busLoc!=null)
            {
                GlobalClass.marker1 = mMap.addMarker(new MarkerOptions()
                        .position(GlobalClass.busLoc));
                CircleOptions circleOptions = new CircleOptions()
                        .center(GlobalClass.busLoc)
                        .strokeColor(Color.BLUE)
                        .fillColor(Color.argb(100, 150, 150, 150))
                        .radius(GlobalClass.busradius);
                GlobalClass.geoFenceLimits = mMap.addCircle(circleOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GlobalClass.busLoc, 15f));
            }


        }
       else if(GlobalClass.locAlarm==1 && purpose.equals("locationAlarm"))
        {
            if(GlobalClass.centreLoc!=null)
            {
                GlobalClass.marker1 = mMap.addMarker(new MarkerOptions()
                        .position(GlobalClass.centreLoc));
                CircleOptions circleOptions = new CircleOptions()
                        .center(GlobalClass.centreLoc)
                        .strokeColor(Color.BLUE)
                        .fillColor(Color.argb(100, 150, 150, 150))
                        .radius(GlobalClass.locradius);
                GlobalClass.geoFenceLimits = mMap.addCircle(circleOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GlobalClass.centreLoc, 15f));
            }

        }
        if(GlobalClass.busAlarm==0 && purpose.equals("busReminder") && GlobalClass.locAlarm==1)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(GeofenceSettings1.this);
            builder.setTitle("Confirmation");
            builder.setMessage("Your location alarm will be cancelled . Do you want to continue with bus reminder ?");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    TrackerService.trackerService.stopSelf();
                    GlobalClass.centreLoc=null;
                    GlobalClass.locAlarm=0;
                    GlobalClass.locradius=0;
                    GlobalClass.geoFenceLimits.remove();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setCancelable(false);
            builder.show();

            LatLng jigatala = new LatLng(GlobalClass.currentlat, GlobalClass.currentlon);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jigatala, 15f));
        }
        else if(GlobalClass.locAlarm==0 && purpose.equals("locationAlarm") && GlobalClass.busAlarm==1)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(GeofenceSettings1.this);
            builder.setTitle("Confirmation");
            builder.setMessage("Your bus reminder will be cancelled . Do you want to continue with location alarm ?");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BusTrackerService.busTrackerService.stopSelf();
                    GlobalClass.busLoc=null;
                    GlobalClass.busradius=0;
                    GlobalClass.busAlarm=0;
                    GlobalClass.geoFenceLimits.remove();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setCancelable(false);
            builder.show();
            LatLng jigatala = new LatLng(GlobalClass.currentlat, GlobalClass.currentlon);
            //mMap.addMarker(new MarkerOptions().position(jigatala));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jigatala, 15f));

        }




    }

    private void showConfigureRadiusStep() {

        marker.setVisibility(View.GONE);
        instructionTitleView.setVisibility(View.VISIBLE);
        instructionSubtitleView.setVisibility(View.GONE);
        radiusBarSeekbar.setVisibility(View.VISIBLE);
        radiusDescriptionView.setVisibility(View.VISIBLE);
       // messageText.setVisibility(View.GONE);
        instructionTitleView.setText(getString(R.string.instruction_radius_description));
        continue1.setOnClickListener((View.OnClickListener) this);

        radiusBarSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                updateRadiusWithProgress(progress);
                radiusDescriptionView.setText(String.valueOf(getRadius(progress)) + " meters");
                //Toast.makeText(GeofenceSettings1.this,String.valueOf(progress),Toast.LENGTH_LONG).show();

                showReminderUpdate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15f));
        /// without this line change of radius and geofence along with marker will not be shown
        showReminderUpdate();

    }

    private void showReminderUpdate() {

        if(GlobalClass.geoFenceLimits!=null)
            GlobalClass.geoFenceLimits.remove();

        mMap.addMarker(new MarkerOptions().position(geofenceCentre));

        CircleOptions circleOptions = new CircleOptions()
                .center(geofenceCentre)
                .strokeColor(Color.BLUE)
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(fenceRadius);
        GlobalClass.geoFenceLimits = mMap.addCircle(circleOptions);

    }

    private void updateRadiusWithProgress(int progress) {

        radius = getRadius(progress);
        //reminder.radius = (int) radius;
        radiusDescriptionView.setText(getString(R.string.radius_description, String.valueOf(radius)));
    }

    private int getRadius(int progress) {

        if (progress > 10 && progress < 20) progress *= 10;
        else if (progress >= 20 && progress < 30) progress *= 15;
        else if (progress >= 30 && progress < 40) progress *= 20;
        else if (progress >= 40 && progress < 50) progress *= 20;
        else if (progress >= 50 && progress < 60) progress *= 20;
        else if (progress >= 60 && progress < 70) progress *= 25;
        else if (progress >= 60 && progress < 70) progress *= 25;
        else if (progress >= 70 && progress < 80) progress *= 35;
        else if (progress >= 80 && progress < 90) progress *= 40;
        else if (progress >= 90 && progress <= 100) progress *= 50;
        fenceRadius = progress;
        if(purpose.equals("busReminder"))
        GlobalClass.busradius=progress;
        else if(purpose.equals("locationAlarm"))
            GlobalClass.locradius=progress;
        return progress;
    }

    private void client() {

        
        if(purpose.equals("busReminder"))
        {
            GlobalClass.busAlarm=1;
            Intent intent= new Intent(this,BusTrackerService.class);
            intent.putExtra("Latitude",geofenceCentre.latitude);
            intent.putExtra("Longitude",geofenceCentre.longitude);
            intent.putExtra("Bus",GlobalClass.BoxBusName);
            intent.putExtra("Time",GlobalClass.BoxBusTime);
            intent.putExtra("Radius",radius);
            startService(intent);
        }
        else if(purpose.equals("locationAlarm"))
        {
            GlobalClass.locAlarm=1;
            Intent intent= new Intent(this,TrackerService.class);
            intent.putExtra("Latitude",geofenceCentre.latitude);
            intent.putExtra("Longitude",geofenceCentre.longitude);
            intent.putExtra("Radius",radius);
            startService(intent);
        }


    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest() {

        geofence = createGeofence(geofenceCentre, (float) radius);
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }

    // Create a Geofence
    private Geofence createGeofence(LatLng latLng, float radius) {


        firstActivity.centerLocation.setLatitude(latLng.latitude);
        firstActivity.centerLocation.setLongitude(latLng.longitude);
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius + 100)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {

        if (v == fab2) {
            double lat = ((GlobalClass) this.getApplication()).lat;
            double lon = ((GlobalClass) this.getApplication()).lon;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15f));

            }

        }
        if (v == continue1 && first) {

            autocomplete.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            if(GlobalClass.busAlarm==1 && purpose.equals("busReminder"))
            {

                textView.setText("Bus Reminder of "+GlobalClass.BoxBusName+" "+GlobalClass.BoxBusTime);
                AlertDialog.Builder builder = new AlertDialog.Builder(GeofenceSettings1.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to overwrite your previous bus reminder ?");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BusTrackerService.busTrackerService.stopSelf();
                       GlobalClass.geoFenceLimits.remove();
                       GlobalClass.marker1.remove();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }


               else if(GlobalClass.locAlarm==1 && purpose.equals("locationAlarm"))
                {
                    textView.setText("Location Alarm");

                    AlertDialog.Builder builder = new AlertDialog.Builder(GeofenceSettings1.this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to overwrite your previous location alarm ?");

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TrackerService.trackerService.stopSelf();
                            GlobalClass.geoFenceLimits.remove();
                            GlobalClass.marker1.remove();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }

            second = true;
            first = false;
            mMap.getUiSettings().setScrollGesturesEnabled(false);
            fab2.setVisibility(View.GONE);
            geofenceCentre = mMap.getCameraPosition().target;

            if(purpose.equals("busReminder"))
            {
                textView.setText("Bus Reminder of "+GlobalClass.BoxBusName+" "+GlobalClass.BoxBusTime);

                GlobalClass.busLoc=new LatLng(geofenceCentre.latitude,geofenceCentre.longitude);

            }


            else if(purpose.equals("locationAlarm"))
            {

                textView.setText("Location Alarm");

                GlobalClass.centreLoc=new LatLng(geofenceCentre.latitude,geofenceCentre.longitude);

            }

            firstActivity.centerLocation.setLatitude(geofenceCentre.latitude);
            firstActivity.centerLocation.setLongitude(geofenceCentre.longitude);
            showConfigureRadiusStep();
        } else if (v == continue1 && second) {

            GlobalClass.finalBusRadius=radius;
            GlobalClass.ct=1;
            GlobalClass.waiting=1;
            client();
            finish();
        }

    }

    @Override
    public void onCameraIdle() {

        geofenceCentre = mMap.getCameraPosition().target;

    }

    @Override
    public void onCameraMove() {

        geofenceCentre = mMap.getCameraPosition().target;


    }

    public void setContext(GeofenceSettings1 context) {
        this.context = context;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GlobalClass.waiting=1;
    }




    @Override
    protected void onStop() {
        SharedPreferences busNamePref = getSharedPreferences(getString(R.string.bus_name_pref_file), MODE_PRIVATE);
        SharedPreferences busTimePref = getSharedPreferences(getString(R.string.bus_time_pref_file), MODE_PRIVATE);
        SharedPreferences.Editor preEditor = busTimePref.edit();
        preEditor.putString(getString(R.string.bus_time_getter),GlobalClass.BusTime);
        preEditor.apply();

        SharedPreferences.Editor pre1Editor = busNamePref.edit();
        pre1Editor.putString(getString(R.string.bus_name_getter),GlobalClass.BusName);
        pre1Editor.apply();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

