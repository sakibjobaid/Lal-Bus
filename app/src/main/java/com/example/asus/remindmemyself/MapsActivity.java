package com.example.asus.remindmemyself;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener, View.OnClickListener {

    private static LatLng POINT_A = new LatLng(23.757011,90.3616399);

    private static  LatLng POINT_B = new LatLng(23.7272146,90.4004568);

    private int  upDown=0;
    private String  busName;
    private Toolbar toolbar;
    private List<LatLng> Route;
    private boolean flag=true;
    public static int count=0;
    private static GoogleMap mMap;
    public static Marker mk;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fab= (FloatingActionButton) findViewById(R.id.fabtrip);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        busName = getIntent().getStringExtra("name");
        this.setTitle(busName+ " Up Trip");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Log.d("jobaid","MapsActivity:onCreate");
        fab.setOnClickListener((View.OnClickListener) this);

        fab.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.down));



        POINT_A = new LatLng(23.757011,90.3616399);

        POINT_B = new LatLng(23.7272146,90.4004568);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        createRouteUp();
    }

    private void createRouteUp() {

        Log.d("jobaid","MapsActivity:createRoute");

        if (Route == null) {
            Log.d("jobaid","if");
            Route = new ArrayList<>();
        } else {
            Route.clear();
        }

        Route.add(new LatLng(23.757011,90.3616399));
        Route.add(new LatLng(23.7559504,90.3627772));
        Route.add(new LatLng(23.7553612,90.363614));
        Route.add(new LatLng(23.7544185,90.3646869));
        Route.add(new LatLng(23.7535544,90.3656525));
        Route.add(new LatLng(23.7527884,90.3665966));
        Route.add(new LatLng(23.7521992,90.3672833));
        Route.add(new LatLng(23.7514136,90.3678626));
        Route.add(new LatLng(23.7505494,90.3683991));
        Route.add(new LatLng(23.7505494,90.3683991));
        Route.add(new LatLng(23.7489782,90.369472));
        Route.add(new LatLng(23.7482122,90.3700299));
        Route.add(new LatLng(23.7474855,90.3705234));
        Route.add(new LatLng(23.7457375,90.3717036));
        Route.add(new LatLng(23.7446375,90.3723902));
        Route.add(new LatLng(23.7435966,90.3731412));
        Route.add(new LatLng(23.7425359,90.3738064));
        Route.add(new LatLng(23.741652,90.3742356));
        Route.add(new LatLng(23.7407878,90.3747076));
        Route.add(new LatLng(23.7398646,90.3751582));
        Route.add(new LatLng(23.7389415,90.3756732));
        Route.add(new LatLng(23.7383915,90.3760165));
        Route.add(new LatLng(23.73847,90.3770894));
        Route.add(new LatLng(23.7386075,90.3784198));
        Route.add(new LatLng(23.738804,90.3796429));
        Route.add(new LatLng(23.73902,90.3807372));
        Route.add(new LatLng(23.7392361,90.3817028));
        Route.add(new LatLng(23.7393539,90.382604));
        Route.add(new LatLng(23.7394914,90.3831834));
        Route.add(new LatLng(23.74067,90.3829045));
        Route.add(new LatLng(23.7407092,90.3831405));
        Route.add(new LatLng(23.7396879,90.3833336));
        Route.add(new LatLng(23.7384308,90.3835696));
        Route.add(new LatLng(23.7372522,90.3838271));
        Route.add(new LatLng(23.736388,90.3839988));
        Route.add(new LatLng(23.7347576,90.384385));
        Route.add(new LatLng(23.7336773,90.3846854));
        Route.add(new LatLng(23.7324594,90.3850288));
        Route.add(new LatLng(23.732479,90.3861231));
        Route.add(new LatLng(23.7326362,90.3871316));
        Route.add(new LatLng(23.7327933,90.3883976));
        Route.add(new LatLng(23.7329701,90.3897495));
        Route.add(new LatLng(23.7331862,90.3911657));
        Route.add(new LatLng(23.7331665,90.3926892));
        Route.add(new LatLng(23.733088,90.3937191));
        Route.add(new LatLng(23.7328719,90.3943629));
        Route.add(new LatLng(23.7323415,90.3949637));
        Route.add(new LatLng(23.7315951,90.3952426));
        Route.add(new LatLng(23.7307111,90.3953499));
        Route.add(new LatLng(23.7298076,90.3954143));
        Route.add(new LatLng(23.7292968,90.3954572));
        Route.add(new LatLng(23.7288843,90.3960151));
        Route.add(new LatLng(23.7281771,90.3966803));
        Route.add(new LatLng(23.7276075,90.3972596));
        Route.add(new LatLng(23.7272539,90.397603));
        Route.add(new LatLng(23.72747,90.3983325));
        Route.add(new LatLng(23.727686,90.3990621));
        Route.add(new LatLng(23.7279218,90.4000491));
        Route.add(new LatLng(23.7278039,90.4003925));
        Route.add(new LatLng(23.7272146,90.4004568));

    }

    public static GoogleMap getmMap() {
        return mMap;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        Log.d("jobaid","MapsActivity:onMapReady");
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener) this);
        LatLng sydney = new LatLng(23.742461, 90.373904);
        mk =mMap.addMarker(new MarkerOptions().position(sydney)
                .title("Zigatala")
                .snippet("Ecstasy")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));


        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                Log.d("jobaid","ashse");
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(POINT_A);
                builder.include(POINT_B);
                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);

                mMap.moveCamera(cu);
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.738287, 90.372810), 15f));

                mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null);

                startAnim();
            }
        });
    }

    private void startAnim(){

        Log.d("jobaid","MapsActivity:startAnim");

        if(mMap != null) {
            MapAnimator.getInstance().animateRoute(mMap, Route);
        }

        else {
            Toast.makeText(getApplicationContext(), "Map not ready", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("jobaid","fowl");
        count++;
        if(count%2==0)
        {mk.hideInfoWindow();Log.d("jobaid","hide "+String.valueOf(count));}
        else
        {
            mk.showInfoWindow();
            Log.d("jobaid","show "+String.valueOf(count));

        }


        return true;
    }

    @Override
    public void onClick(View v) {
        ++upDown;
        if(upDown%2==1)
        {
            creatRouteDown();
            startAnim();
            fab.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.up));

            this.setTitle(busName+ " Down Trip");
        }
        else
        {

            createRouteUp();
            startAnim();
            fab.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.down));

            this.setTitle(busName+ " Up Trip");
        }
    }

    private void creatRouteDown() {

        Log.d("jobaid","MapsActivity:createRoute");

        if (Route == null) {
            Log.d("jobaid","if");
            Route = new ArrayList<>();
        } else {
            Route.clear();
        }

        Route.add(new LatLng(23.7272146,90.4004568));
        Route.add(new LatLng(23.7278039,90.4003925));
        Route.add(new LatLng(23.7279218,90.4000491));
        Route.add(new LatLng(23.727686,90.3990621));
        Route.add(new LatLng(23.72747,90.3983325));
        Route.add(new LatLng(23.7272539,90.397603));
        Route.add(new LatLng(23.7276075,90.3972596));
        Route.add(new LatLng(23.7281771,90.3966803));
        Route.add(new LatLng(23.7288843,90.3960151));
        Route.add(new LatLng(23.7292968,90.3954572));
        Route.add(new LatLng(23.7298076,90.3954143));
        Route.add(new LatLng(23.7307111,90.3953499));
        Route.add(new LatLng(23.7315951,90.3952426));
        Route.add(new LatLng(23.7323415,90.3949637));
        Route.add(new LatLng(23.7328719,90.3943629));
        Route.add(new LatLng(23.733088,90.3937191));
        Route.add(new LatLng(23.7331665,90.3926892));
        Route.add(new LatLng(23.7331862,90.3911657));
        Route.add(new LatLng(23.7329701,90.3897495));
        Route.add(new LatLng(23.7327933,90.3883976));
        Route.add(new LatLng(23.7326362,90.3871316));
        Route.add(new LatLng(23.732479,90.3861231));
        Route.add(new LatLng(23.7324594,90.3850288));
        Route.add(new LatLng(23.7336773,90.3846854));
        Route.add(new LatLng(23.7347576,90.384385));
        Route.add(new LatLng(23.736388,90.3839988));
        Route.add(new LatLng(23.7372522,90.3838271));
        Route.add(new LatLng(23.7384308,90.3835696));
        Route.add(new LatLng(23.7396879,90.3833336));
        Route.add(new LatLng(23.7407092,90.3831405));
        Route.add(new LatLng(23.74067,90.3829045));
        Route.add(new LatLng(23.7394914,90.3831834));
        Route.add(new LatLng(23.7393539,90.382604));
        Route.add(new LatLng(23.7392361,90.3817028));
        Route.add(new LatLng(23.73902,90.3807372));
        Route.add(new LatLng(23.738804,90.3796429));
        Route.add(new LatLng(23.7386075,90.3784198));
        Route.add(new LatLng(23.73847,90.3770894));
        Route.add(new LatLng(23.7383915,90.3760165));
        Route.add(new LatLng(23.7389415,90.3756732));
        Route.add(new LatLng(23.7398646,90.3751582));
        Route.add(new LatLng(23.7407878,90.3747076));
        Route.add(new LatLng(23.741652,90.3742356));
        Route.add(new LatLng(23.7425359,90.3738064));
        Route.add(new LatLng(23.7435966,90.3731412));
        Route.add(new LatLng(23.7446375,90.3723902));
        Route.add(new LatLng(23.7457375,90.3717036));
        Route.add(new LatLng(23.7474855,90.3705234));
        Route.add(new LatLng(23.7482122,90.3700299));
        Route.add(new LatLng(23.7489782,90.369472));
        Route.add(new LatLng(23.7505494,90.3683991));
        Route.add(new LatLng(23.7505494,90.3683991));
        Route.add(new LatLng(23.7514136,90.3678626));
        Route.add(new LatLng(23.7521992,90.3672833));
        Route.add(new LatLng(23.7527884,90.3665966));
        Route.add(new LatLng(23.7535544,90.3656525));
        Route.add(new LatLng(23.7544185,90.3646869));
        Route.add(new LatLng(23.7553612,90.363614));
        Route.add(new LatLng(23.7559504,90.3627772));
        Route.add(new LatLng(23.757011,90.3616399));

    }
}
