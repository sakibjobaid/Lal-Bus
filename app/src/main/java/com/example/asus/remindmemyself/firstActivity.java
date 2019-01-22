package com.example.asus.remindmemyself;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class firstActivity extends FragmentActivity implements View.OnClickListener,OnMapReadyCallback ,GoogleMap.OnCameraMoveCanceledListener,GoogleMap.OnMarkerClickListener,GoogleMap.OnCameraIdleListener,GoogleMap.OnCameraMoveListener{

    //region variable
    private GoogleMap mMap;
    private boolean locationUpdateState = false, permit = false;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private LatLng currentLatLng,lcd=null;
    private boolean flag= true;
    private Marker marker;
    private int count=0;
    private ImageView img;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        img= (ImageView) findViewById(R.id.iconid);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabloc);
        FloatingActionButton fabt = (FloatingActionButton) findViewById(R.id.fabtraffic);

       fab.setOnClickListener(this);
       fabt.setOnClickListener(this);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                markerPlacing(currentLatLng);
//            }
//        });


//        fabt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mMap.isTrafficEnabled())
//                mMap.setTrafficEnabled(false);
//                else
//                    mMap.setTrafficEnabled(true);
//            }
//        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                lastlocation = locationResult.getLastLocation();
                currentLatLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
                //marker.remove();
                //markerPlacing(currentLatLng);
            }
        };

        createLocationRequest();



    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.fabloc)
        {
            markerPlacing(currentLatLng);
        }
        if(v.getId()==R.id.fabtraffic)
        {
            if(mMap.isTrafficEnabled())
                mMap.setTrafficEnabled(false);
                else
                    mMap.setTrafficEnabled(true);
        }
    }

    private void createLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {


                locationUpdateState = true;
                startLocationUpdates();
            }
        });


        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.

                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(firstActivity.this,
                                2);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

    }

    private void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 124 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);

        setUpMap();

//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void setUpMap() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 124);
        } else permit = true;
        if (permit) {
            //mMap.setMyLocationEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {
                                lastlocation = location;
                                LatLng currentLatLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
                                markerPlacing(currentLatLng);
                            }

                        }
                    });
            {

            }

        }
    }

    private void markerPlacing(LatLng currentLatLng) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        //mMap.setMyLocationEnabled(true);



        MarkerOptions mk = new MarkerOptions();
        mk.draggable(false);
        mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        //mk.getIcon()


         marker = mMap.addMarker(mk
                .position(currentLatLng)
                .title("sakib"));

        marker.showInfoWindow();

        //marker.setSnippet("hello");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));



    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        ++count;
        if(count%2==1)
        marker.hideInfoWindow();
        else
            marker.showInfoWindow();
        return true;
    }

    @Override
    public void onCameraIdle() {
        //currentLatLng=mMap.getCameraPosition().target;
        img.setVisibility(View.VISIBLE);
//        if(flag)
//        {
//            Log.d("sima","love");
//            flag=false;
//            markerPlacing(currentLatLng);
//        }
        //
        Log.d("sima","idle");
    }

    @Override
    public void onCameraMove() {

        Log.d("sima","move");
        img.setVisibility(View.VISIBLE);
        marker.remove();

    }

    @Override
    public void onCameraMoveCanceled() {

        if(flag)
        {
            Log.d("sima","love");
            flag=false;
            lcd=mMap.getCameraPosition().target;
            markerPlacing(mMap.getCameraPosition().target);
        }
    }


}
