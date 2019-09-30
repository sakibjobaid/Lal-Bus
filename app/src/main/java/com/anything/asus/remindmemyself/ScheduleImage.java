package com.anything.asus.remindmemyself;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ScheduleImage extends AppCompatActivity implements
        OnMapReadyCallback {

    int count=0;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    String name,temps;
    List<Schedule_CardView> listitems;
    public SharedPreferences references;
    private static Context context;
    public static GoogleMap mMap;
    public static FloatingActionButton fab;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context=ScheduleImage.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_image);
         name= getIntent().getStringExtra("name");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        this.setTitle(name);


        references = getSharedPreferences(getString(R.string.schedule_pref_file), MODE_PRIVATE);
        temps=references.getString(name,"");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView= (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);
        listitems= new ArrayList<>();
        StringTokenizer ppx = new StringTokenizer(temps,"$");

        while(ppx.hasMoreElements())
        {
            String single= (String) ppx.nextElement();

            StringTokenizer stx = new StringTokenizer(single,"#");
            Schedule_CardView abc= new Schedule_CardView(
                    (String)stx.nextElement(),(String)stx.nextElement(),
                    (String)stx.nextElement(),(String)stx.nextElement());
            listitems.add(abc);
        }

        adapter= new ScheduleAdapter2(listitems,this);

        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    protected void onResume() {
        super.onResume();
    }


    @SuppressLint("RestrictedApi")
    public  static  void openCustomDialog() {

        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_map_in_schedule);






        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);



        MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);
        MapsInitializer.initialize(context);

        fab=(FloatingActionButton)dialog.findViewById(R.id.fablocmap);
        fab.setVisibility(View.VISIBLE);

        mMapView = (MapView) dialog.findViewById(R.id.mapView);
        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();// needed to get the map to display immediately


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap=googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.getUiSettings().setTiltGesturesEnabled(false);
                mMap.getUiSettings().setMapToolbarEnabled(false);

                LatLng posisiabsen = new LatLng(23.739368, 90.375367); ////your lat lng
                googleMap.addMarker(new MarkerOptions().position(posisiabsen).title("Your title"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisiabsen,15f));
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.getUiSettings().setCompassEnabled(false);
                googleMap.getUiSettings().setRotateGesturesEnabled(false);
                //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 0, null);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng jigatala = new LatLng(GlobalClass.currentlat, GlobalClass.currentlon);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(jigatala, 15f));
            }
        });


        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng jigatala = new LatLng(GlobalClass.currentlat, GlobalClass.currentlon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jigatala, 15f));

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);

    }
}