package com.example.asus.remindmemyself;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;

// sakib casdflasjdf
public class Splashscreen extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView img;
    private TextView tv;
    private AlertDialog.Builder dialog;
    private AlertDialog alert;
    private LocationManager lm;
    private boolean flag = false;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Log.d("jobaid", "Splashscreen:Oncreate");
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final Intent intent = new Intent(this, StartActiviy.class);

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    //check if connected!
                    Thread.sleep(2000);
//                    while (!isNetworkConnected(Splashscreen.this)) {
//                        //Wait to connect
//                        Log.d("bikas","just");
//                        //networkAlert(SplashScreen.this);
//                        flag=true;
//                        Thread.sleep(1000);
//                    }
                    //if(flag)
                    Log.d("molla", "one");
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                }
            }
        };
        t.start();


    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("bikas", "onstart");

    }

    @Override
    public void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            GlobalClass.currentlat=location.getLatitude();
                            GlobalClass.currentlon=location.getLongitude();
                            //Toast.makeText(Splashscreen.this, String.valueOf(location), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Log.d("bikas","onResume");
    }
    @Override
    public void onPause()
    {
        super.onPause();
        Log.d("bikas","onPause");
    }
    @Override
    public void onStop()
    {
        super.onStop();
        Log.d("bikas","onStop");

    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("bikas","onDestroy");


    }
    @Override
    public void onRestart() {
        super.onRestart();
        super.onStop();
//        if (!isNetworkConnected(Splashscreen.this)) {
//            networkAlert(Splashscreen.this);
//        }if (!isNetworkConnected(Splashscreen.this)) {
////            networkAlert(Splashscreen.this);
////        }

        Log.d("bikas", "onRestart");


    }

    public static boolean isNetworkConnected(Splashscreen context) {

        Log.d("jobaid","firstactivity: is NetworkConnected [outside isNetworkConnected]");
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

    private void networkAlert(Splashscreen firstActivity) {

        Log.d("jobaid", "firstactivity : networkalert");
        final Context context =Splashscreen.this;
        dialog = new AlertDialog.Builder(context);
        dialog.setMessage("Check your internet connection");
        dialog.setTitle("Network unavailable");
        dialog.setPositiveButton("Network settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // check = true;
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                context.startActivity(intent);
            }
        });
        alert = dialog.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
    }
}
