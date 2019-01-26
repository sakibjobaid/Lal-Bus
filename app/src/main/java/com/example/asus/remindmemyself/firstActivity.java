package com.example.asus.remindmemyself;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;

public class firstActivity extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener {

    private static int interval =0, fastinterval = 0;
    //region variable
    private GoogleMap mMap;
    private boolean locationUpdateState = false, permit = false, locationsettings = false, check = false;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private LatLng currentLatLng = null, aftercameramove;
    private boolean flag = true;
    private Marker marker;
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
        Log.d("sima", "onCreate");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabloc);
        FloatingActionButton fabt = (FloatingActionButton) findViewById(R.id.fabtraffic);

        fab.setOnClickListener(this);
        fabt.setOnClickListener(this);
        //endregion


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                lastlocation = locationResult.getLastLocation();
                currentLatLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
                Log.d("sima", "callback");
                if(flag)
                {
                    flag=false;
                    markerPlacing(currentLatLng);

                }

                //marker.remove();

            }
        };

        createLocationRequest();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("sima", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Log.d("sima", "onResume");

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("sima", "b4 locationupdate");
            startLocationUpdates();
        } else
            alertMethod();


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("sima", "onPause");

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

                                Log.d("sima", "onSuccess");
                                if (location != null) {
                                    Log.d("sima", "setup deep success");
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
        Log.d("sima", "createlocation");

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
//            Log.d("sima","alertdialog");
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
//                 Log.d("sima","task success");
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
////                Log.d("sima","failure");
////
////                if (e instanceof ResolvableApiException) {
////                    // Location settings are not satisfied, but this can be fixed
////                    // by showing the user a dialog.
////                    Log.d("sima","resolvable failure");
////
////
////                    try {
////                        // Show the dialog by calling startResolutionForResult(),
////                        // and check the result in onActivityResult().
////                        Log.d("sima","try failure");
////
////                        ResolvableApiException resolvable = (ResolvableApiException) e;
////                        resolvable.startResolutionForResult(firstActivity.this,
////                                2);
////                    } catch (IntentSender.SendIntentException sendEx) {
////                        Log.d("sima","catched failure");
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
//           Log.w("sima", "Location settings not satisfied, attempting resolution intent");
////            try {
////                ResolvableApiException resolvable = (ResolvableApiException) e;
////                resolvable.startResolutionForResult(firstActivity.this,2);
////            } catch (IntentSender.SendIntentException sendIntentException) {
////                Log.e("sima", "Unable to start resolution intent");
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
//            Log.w("sima", "Location settings not satisfied and can't be changed");
//            break;
//    }
//}
//        });

    }

    private void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }

        Log.d("sima", "startLocationUpdate");
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d("sima", "onRequest");
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

        Log.d("sima", "onMapReady");
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        setUpMap();

    }

    private void setUpMap() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
        else {
            Log.d("sima", "setup success");
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            Log.d("sima", "onSuccess");
                            if (location != null) {
                                Log.d("sima", "setup deep success");
                                lastlocation = location;
                                LatLng currentLatLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
                                markerPlacing(currentLatLng);
                            }

                        }
                    });

        }


    }

    private void alertMethod() {

        Log.d("sima", "alertdialog");
        final Context context = firstActivity.this;
        String msg = "Please enable your GPS/Location Service and High Accuracy as location method";
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(msg);

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

            if (mMap.isTrafficEnabled())
                mMap.setTrafficEnabled(false);
            else
                mMap.setTrafficEnabled(true);
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
//                .title("sakib"));
//
//        marker.showInfoWindow();

        //marker.setSnippet("hello");
        Log.d("sima", "markerPlacing");
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
        Log.d("sima", "idle");
        aftercameramove = mMap.getCameraPosition().target;
    }

    @Override
    public void onCameraMove() {

        Log.d("sima", "move");
        img.setVisibility(View.VISIBLE);
        /// to remove previous marker due to continuos calling of the markerplacing

        //marker.remove();

    }

    @Override
    public void onCameraMoveCanceled() {

        Log.d("sima", "ouside sb");
        if (flag) {
            Log.d("sima", "sb");
            //flag=false;
            markerPlacing(mMap.getCameraPosition().target);
        }
    }


}
