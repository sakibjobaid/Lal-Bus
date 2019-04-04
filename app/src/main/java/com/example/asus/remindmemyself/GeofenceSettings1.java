package com.example.asus.remindmemyself;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GeofenceSettings1 extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener,

        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener {

    //region varible
    private TextView instructionTitleView, instructionSubtitleView, radiusDescriptionView;
    private SeekBar radiusBarSeekbar;
    private EditText messageText;
    private GoogleMap mMap;
    private Button continue1;
    public static LatLng geofenceCentre;
    private ImageView marker;
    private Circle geoFenceLimits;
    private GeofencingClient mGeofencingClient;
    private GeofencingRequest geofenceRequest;
    private Geofence geofence;
    private PendingIntent geoFencePendingIntent;
    private boolean first = true, second = false;
    private int fenceRadius;
    public static Context context;
    private LinearLayout linearLayout;

    private FloatingActionButton fab2;
    public static AlertDialog.Builder dialog;
    public static AlertDialog alert;
    public static String purpose;

    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static float GEOFENCE_RADIUS = 300.0f; // in meters
    public static int radius;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence_settings1);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        messageText = (EditText) findViewById(R.id.message);
        marker = findViewById(R.id.marker);


        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Log.d("jobaid", "Settings:oncreate");
        instructionTitleView.setVisibility(View.VISIBLE);
        instructionSubtitleView.setVisibility(View.VISIBLE);
        radiusBarSeekbar.setVisibility(View.GONE);
        radiusDescriptionView.setVisibility(View.GONE);
        messageText.setVisibility(View.GONE);

//        if (name != null && name.equals("hello")) {
//        try {
//            linearLayout.setVisibility(View.GONE);
//            alertmethod();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }
//        else
    showConfigureLocationStep();

}

    private void showConfigureLocationStep() {

        Log.d("jobaid", "Settings:showConfigureLocationStep");

        marker.setVisibility(View.VISIBLE);
        instructionTitleView.setVisibility(View.VISIBLE);
        instructionSubtitleView.setVisibility(View.VISIBLE);
        radiusBarSeekbar.setVisibility(View.GONE);
        radiusDescriptionView.setVisibility(View.GONE);
        messageText.setVisibility(View.GONE);

        instructionTitleView.setText(getString(R.string.instruction_where_description));
        instructionSubtitleView.setText(getString(R.string.instruction_where_subtitle_description));

        continue1.setOnClickListener(this);

        //showReminderUpdate();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        Log.d("jobaid", "Settings:onMapReady");
        // Add a marker in Sydney and move the camera
        LatLng jigatala = new LatLng(GlobalClass.currentlat, GlobalClass.currentlon);
        //mMap.addMarker(new MarkerOptions().position(jigatala));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jigatala, 15f));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);


    }

    private void showConfigureRadiusStep() {
        Log.d("jobaid", "Settings:showConfigureRadiusStep");

        marker.setVisibility(View.GONE);
        instructionTitleView.setVisibility(View.VISIBLE);
        instructionSubtitleView.setVisibility(View.GONE);
        radiusBarSeekbar.setVisibility(View.VISIBLE);
        radiusDescriptionView.setVisibility(View.VISIBLE);
        messageText.setVisibility(View.GONE);
        instructionTitleView.setText(getString(R.string.instruction_radius_description));
        continue1.setOnClickListener((View.OnClickListener) this);
        Log.d("jobaid", "here ");

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

        Log.d("jobaid", "Settings:showReminderInMap");

//            val vectorToBitmapt = vectorToBitmap(context.resources, R.drawable.ic_twotone_location_on_48px)
//            val marker = map.addMarker(MarkerOptions().position(latLng).icon(vectorToBitmap))
//            marker.tag = reminder.id
        if (geoFenceLimits != null)
            geoFenceLimits.remove();
        mMap.addMarker(new MarkerOptions().position(geofenceCentre));
        CircleOptions circleOptions = new CircleOptions()
                .center(geofenceCentre)
                .strokeColor(Color.BLUE)
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(fenceRadius);
        geoFenceLimits = mMap.addCircle(circleOptions);
//            if (reminder.radius != null) {
//                val radius = reminder.radius as Double
//                map.addCircle(CircleOptions()
//                        .center(reminder.latLng)
//                        .radius(radius)
//                        .strokeColor(ContextCompat.getColor(context, R.color.colorAccent))
//                        .fillColor(ContextCompat.getColor(context, R.color.colorReminderFill)))
//            }

    }

    private void updateRadiusWithProgress(int progress) {

        Log.d("jobaid", "Settings:updateRadiusWithProgress");


        radius = getRadius(progress);
        //reminder.radius = (int) radius;
        radiusDescriptionView.setText(getString(R.string.radius_description, String.valueOf(radius)));
    }

    private int getRadius(int progress) {
        Log.d("jobaid", "Settings:getRadius");
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
        return progress;
    }

    private void client() {

        Log.d("jobaid", "Settings:client");
        //region apatoto bondho
        //        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mGeofencingClient.addGeofences(createGeofenceRequest(),
//                    createGeofencePendingIntent())
//                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            // Geofences added
//                            // ...
//                            Log.d("jobaid", "Geofence success");
//                        }
//                    })
//                    .addOnFailureListener(this, new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.d("jobaid", "Geofence failure");
//
//                            // Failed to add geofences
//                            // ...
//                        }
//                    });
//        } else {
//            Log.d("jobaid", "mara");
//        }
        //endregion
        if(purpose.equals("busReminder"))
        {
            Intent intent= new Intent(this,BusTrackerService.class);
            intent.putExtra("Latitude",geofenceCentre.latitude);
            intent.putExtra("Longitude",geofenceCentre.longitude);
            intent.putExtra("Radius",radius);
            startService(intent);
        }
        else if(purpose.equals("locationAlarm"))
        {
            Intent intent= new Intent(this,TrackerService.class);
            intent.putExtra("Latitude",geofenceCentre.latitude);
            intent.putExtra("Longitude",geofenceCentre.longitude);
            intent.putExtra("Radius",radius);
            startService(intent);
        }


    }

    private void alertmethod() throws InterruptedException {


        Log.d("jobaid", "settings:alertdialog");
        //Context context = getApplicationContext();

        dialog = new AlertDialog.Builder(this);
        Log.d("jobaid", "Setting:fox");

        dialog.setTitle(R.string.Bus_Geofence);
        dialog.setIcon(R.drawable.bus_enter);
        dialog.setMessage( R.string.Bus_msg);


        dialog.setPositiveButton("Stop Alarm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //check = true;
                Toast.makeText(GeofenceSettings1.this,"abal",Toast.LENGTH_LONG).show();
                GeofenceTransitionsIntentService.defaultRingtone.stop();
                GeofenceTransitionsIntentService.myVib.cancel();
                Log.d("oops","oin6");

                //if (GeofenceTransitionsIntentService.md1.isPlaying())GeofenceTransitionsIntentService.md1.stop();
                //if (GeofenceTransitionsIntentService.md2.isPlaying())GeofenceTransitionsIntentService.md2.stop();
                finish();
//                ActivityCompat.finishAffinity(GeofenceSettings1.this);
//                Intent intent = new Intent(GeofenceSettings1.this, firstActivity.class);
//                if (firstActivity.admin) intent.putExtra("name", "admin");
//                else if (firstActivity.user) intent.putExtra("name", "user");
//                startActivity(intent);
            }
        });
        alert = dialog.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);

    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest() {
        Log.d("jobaid", "Settings:createGeofenceRequest");
        Log.d("same", String.valueOf(geofenceCentre.latitude) + " , " + String.valueOf(geofenceCentre.longitude));

        geofence = createGeofence(geofenceCentre, (float) radius);
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }

    // Create a Geofence
    private Geofence createGeofence(LatLng latLng, float radius) {
        Log.d("jobaid", "Settings:createGeofence");

        Log.d("there", String.valueOf(latLng.latitude) + " , " + String.valueOf(latLng.longitude));

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

    private PendingIntent createGeofencePendingIntent() {
        Log.d("jobaid", "Settings:createGeofencePendingIntent");
        if (geoFencePendingIntent != null)
            return geoFencePendingIntent;
        Log.d("jobaid", "Settings:dhur");

        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);

        PendingIntent p = PendingIntent.getService(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("jobaid", "Settings:muri khao");


        return p;


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

            Log.d("jobaid", "Settings:onclick:click one");
            second = true;
            first = false;
            mMap.getUiSettings().setScrollGesturesEnabled(false);
            fab2.setVisibility(View.GONE);
            geofenceCentre = mMap.getCameraPosition().target;
            firstActivity.centerLocation.setLatitude(geofenceCentre.latitude);
            firstActivity.centerLocation.setLongitude(geofenceCentre.longitude);
            showConfigureRadiusStep();
        } else if (v == continue1 && second) {

            Log.d("jobaid", "Settings:onclick:click two "+String.valueOf(radius));

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

    //    private void addReminder(Reminder reminder ) {
//
//        Log.d("jobaid","addRemainder")
//        getRepository().add(reminder,
//                success = {
//                        setResult(Activity.RESULT_OK)
//                        finish()
//                },
//                failure = {
//                        Snackbar.make(main, it, Snackbar.LENGTH_LONG).show()
//                })
//    }

//    private void showConfigureMessageStep() {
//
//        Log.d("jobaid","showConfigureMessageStep");
//
//        marker.setVisibility(View.GONE);
//        instructionTitleView.setVisibility(View.VISIBLE);
//        instructionSubtitleView.setVisibility(View.GONE);
//        radiusBarSeekbar.setVisibility(View.GONE);
//        radiusDescriptionView.setVisibility(View.GONE);
//        messageText.setVisibility(View.VISIBLE);
//        instructionTitleView.setText(getString(R.string.instruction_where_description));
//
//        instructionTitleView.setText(getString(R.string.instruction_message_description));
//        continue1.setOnClickListener(this);
//        next.setOnClickListener {
//            hideKeyboard(this, message)
//
//            ///message.text is equivalent to message.getText()
//            reminder.message = message.text.toString()
//
//            if (reminder.message.isNullOrEmpty()) {
//                message.error = getString(R.string.error_required)
//            } else {
//                addReminder(reminder)
//            }
//        }
//        message.requestFocusWithKeyboard()
//
//        showReminderUpdate()
//
//    }

}

