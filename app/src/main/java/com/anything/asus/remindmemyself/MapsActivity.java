package com.anything.asus.remindmemyself;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
import java.util.StringTokenizer;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener, View.OnClickListener {

    //region variables
    private static LatLng POINT_A = new LatLng(23.757011, 90.3616399);
    private static LatLng POINT_B = new LatLng(23.7272146, 90.4004568);
    private int upDown = 0;
    private String busName, id,finalroute="";
    private Toolbar toolbar;
    private List<LatLng> Route;
    private boolean flag = true;
    public static int count = 0;
    private static GoogleMap mMap;
    public static Marker mk;
    public static  FloatingActionButton fab;
    private TextView tv;
    public Menu main_menu;

    SharedPreferences route_pref, stoppage_pref;
    String  temp = "", temp2 = "";
    //endregion

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        GlobalClass.hiding=0;
        route_pref = getSharedPreferences(getString(R.string.route_pref_file), MODE_PRIVATE);
        stoppage_pref = getSharedPreferences(getString(R.string.stoppage_pref_file), MODE_PRIVATE);


        fab = (FloatingActionButton) findViewById(R.id.fabtrip);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        busName = getIntent().getStringExtra("name");
        this.setTitle(busName.trim() + " Up Trip");
        tv = findViewById(R.id.mywidget);
        tv.setText("Route may change in any unavoidable situation.");
        tv.setSelected(true);

        finalroute=GlobalClass.tempBusName+"upRoute1";

        createRouteUp(busName.trim()+"upRoute1");


        fab.setOnClickListener((View.OnClickListener) this);

        fab.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.down));


        POINT_A = new LatLng(23.757011, 90.3616399);

        POINT_B = new LatLng(23.7272146, 90.4004568);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.



    }



    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener) this);

        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);

        LatLng jigatala = new LatLng(GlobalClass.currentlat, GlobalClass.currentlon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jigatala, 15f));





        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(POINT_A);
                builder.include(POINT_B);
                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);

                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mMap != null) {
                            mMap.clear();

                        }

                        addStoppage(busName,"up");
                        startAnim();


                    }
                }, 1000);


            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                id = marker.getId();

                MarkerOptions mk = new MarkerOptions();
                mk.draggable(false);
                mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                marker.setTitle(GlobalClass.umarkerName.get(id));
                marker.setSnippet(GlobalClass.umarkerDescription.get(id));
                marker.showInfoWindow();

                return true;
            }
        });

    }

    private void createRouteUp(String routePref_name) {

        if (Route == null) {
            Route = new ArrayList<>();
        } else {
            Route.clear();
        }

        //region uproute
            temp = route_pref.getString(routePref_name, "");
            StringTokenizer ppx = new StringTokenizer(temp, "$");


            while (ppx.hasMoreElements()) {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(), ",");
                double lat = Double.parseDouble(stx.nextElement().toString());
                double lon = Double.parseDouble(stx.nextElement().toString());
                Route.add(new LatLng(lat, lon));

            }


            //startAnim();

        //endregion


    }

    private void creatRouteDown(String route_pref_name) {


        if (Route == null) {
            Route = new ArrayList<>();
        } else {
            Route.clear();
        }

        //region down route


            temp = route_pref.getString(route_pref_name, "");
            StringTokenizer ppx = new StringTokenizer(temp, "$");

            while (ppx.hasMoreElements()) {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(), ",");
                double lat = Double.parseDouble(stx.nextElement().toString());
                double lon = Double.parseDouble(stx.nextElement().toString());
                Route.add(new LatLng(lat, lon));

            }



        //endregion


    }

    public static GoogleMap getmMap() {
        return mMap;
    }

    private void addStoppage(String name,String direc) {

        GlobalClass.markerArray=new Marker[200];
            finalroute=(name.trim()+direc+"Route1");
        temp2 = stoppage_pref.getString(finalroute, "");

        StringTokenizer ppy = new StringTokenizer(temp2, "$");
        while (ppy.hasMoreElements()) {

            StringTokenizer stx = new StringTokenizer(ppy.nextElement().toString(), "@");
            double lat = Double.parseDouble(stx.nextElement().toString());
            double lon = Double.parseDouble(stx.nextElement().toString());
            String title = stx.nextElement().toString();
            String desc = stx.nextElement().toString();


            MarkerOptions mk = new MarkerOptions();
            mk.draggable(false);
            mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            GlobalClass.markerArray[GlobalClass.countM] = mMap.addMarker(mk
                    .position(new LatLng(lat,lon)));
            GlobalClass.markerArray[GlobalClass.countM].setTitle(title);
            GlobalClass.markerArray[GlobalClass.countM].setSnippet(desc);


            GlobalClass.umarkerName.put(GlobalClass.markerArray[GlobalClass.countM].getId(), title);
            GlobalClass.umarkerDescription.put(GlobalClass.markerArray[GlobalClass.countM].getId(), desc);
            GlobalClass.umarkerSerial.put(GlobalClass.countM, GlobalClass.markerArray[GlobalClass.countM].getId());
            GlobalClass.umarkerLatlng.put(GlobalClass.markerArray[GlobalClass.countM].getId(), new LatLng(lat, lon));
            mMap.addMarker(mk.position(new LatLng(lat, lon)).title(title).snippet(desc)).showInfoWindow();

            GlobalClass.countM++;
        }
    }

    private void startAnim() {


        MapAnimator.getInstance().animateRoute(mMap, Route);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        ++upDown;
        if (upDown % 2 == 1) {
            main_menu.clear();
            onCreateOptionsMenu(main_menu);
            GlobalClass.down=1;
            GlobalClass.up=0;

            creatRouteDown(busName.trim()+"downRoute1");
           if (MapAnimator.foregroundRouteAnimator.isRunning())
                MapAnimator.foregroundRouteAnimator.cancel();
            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (mMap != null) {
                        mMap.clear();

                    }
                    addStoppage(busName,"down");

                    startAnim();


                }
            }, 2000);
            fab.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.up));
            GlobalClass.up = 0;
            GlobalClass.down = 1;
            this.setTitle(busName.trim() + " Down Trip");
        } else {

            GlobalClass.down=0;
            GlobalClass.up=1;
            main_menu.clear();
            onCreateOptionsMenu(main_menu);
            createRouteUp(busName.trim()+"upRoute1");
            if (MapAnimator.foregroundRouteAnimator.isRunning())
                MapAnimator.foregroundRouteAnimator.cancel();

            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mMap != null) {
                        mMap.clear();

                    }
                    addStoppage(busName,"up");

                    startAnim();
                }
            }, 2000);
            fab.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.down));

            GlobalClass.up = 1;
            GlobalClass.down = 0;
            this.setTitle(busName.trim() + " Up Trip");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        main_menu = menu;
        //getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        int item_count = -1;

        if (GlobalClass.up == 1) {
            //region up menu
                item_count = route_pref.getInt(busName.trim()+"up", -1);
                if (item_count > 1) {
                    for (int i = 1; i <= item_count; i++) {
                        menu.add(Menu.NONE, i, Menu.NONE, "Route " + String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                    }
                    return true;
                }

                else return false;

            //endregion
        }

        else if (GlobalClass.down == 1) {

            //region down menu

                item_count = route_pref.getInt(busName.trim()+"down", -1);
                if (item_count > 1) {

                    for (int i = 1; i <= item_count; i++) {

                        menu.add(Menu.NONE, i, Menu.NONE, "Route " + String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
                else return false;

            //endregion
        }

        return true;

    }

    @Override
    protected void onStop() {

        SharedPreferences busNamePref = getSharedPreferences(getString(R.string.bus_name_pref_file), MODE_PRIVATE);
        SharedPreferences busTimePref = getSharedPreferences(getString(R.string.bus_time_pref_file), MODE_PRIVATE);
        SharedPreferences.Editor preEditor = busTimePref.edit();
        preEditor.putString(getString(R.string.bus_time_getter), GlobalClass.BusTime);
        preEditor.apply();

        SharedPreferences.Editor pre1Editor = busNamePref.edit();
        pre1Editor.putString(getString(R.string.bus_name_getter), GlobalClass.BusName);
        pre1Editor.apply();
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (GlobalClass.up == 1) {

                switch (item.getItemId()) {
                    case 1: {
                        finalroute=busName.trim()+"upRoute1";
                        createRouteUp(finalroute);
                        if (MapAnimator.foregroundRouteAnimator.isRunning())
                            MapAnimator.foregroundRouteAnimator.cancel();
                        doJob();
                        break;
                    }
                    case 2: {
                        finalroute=busName.trim()+"upRoute2";
                        createRouteUp(finalroute);
                        if (MapAnimator.foregroundRouteAnimator.isRunning())
                            MapAnimator.foregroundRouteAnimator.cancel();
                        doJob();
                        break;

                    }
                    case 3: {
                        finalroute=busName.trim()+"upRoute3";
                        createRouteUp(finalroute);
                        if (MapAnimator.foregroundRouteAnimator.isRunning())
                            MapAnimator.foregroundRouteAnimator.cancel();
                        doJob();
                        break;

                    }
                    case 4: {
                        finalroute=busName.trim()+"upRoute4";
                        createRouteUp(finalroute);
                        if (MapAnimator.foregroundRouteAnimator.isRunning())
                            MapAnimator.foregroundRouteAnimator.cancel();
                        doJob();
                        break;

                    }
                    case 5: {
                        finalroute=busName.trim()+"upRoute5";
                        createRouteUp(finalroute);
                        if (MapAnimator.foregroundRouteAnimator.isRunning())
                            MapAnimator.foregroundRouteAnimator.cancel();
                        doJob();
                        break;

                    }
                }

        }

        if(GlobalClass.down==1)
        {
            switch (item.getItemId()) {
                case 1: {
                    finalroute=busName.trim()+"downRoute1";
                    createRouteUp(finalroute);
                    if (MapAnimator.foregroundRouteAnimator.isRunning())
                        MapAnimator.foregroundRouteAnimator.cancel();
                    doJob();
                    break;
                }
                case 2: {
                    finalroute=busName.trim()+"downRoute2";
                    createRouteUp(finalroute);
                    if (MapAnimator.foregroundRouteAnimator.isRunning())
                        MapAnimator.foregroundRouteAnimator.cancel();
                    doJob();
                    break;

                }
                case 3: {
                    finalroute=busName.trim()+"downRoute3";
                    createRouteUp(finalroute);
                    if (MapAnimator.foregroundRouteAnimator.isRunning())
                        MapAnimator.foregroundRouteAnimator.cancel();
                    doJob();
                    break;

                }
                case 4: {
                    finalroute=busName.trim()+"downRoute4";
                    createRouteUp(finalroute);
                    if (MapAnimator.foregroundRouteAnimator.isRunning())
                        MapAnimator.foregroundRouteAnimator.cancel();
                    doJob();
                    break;

                }
                case 5: {
                    finalroute=busName.trim()+"downRoute5";
                    createRouteUp(finalroute);
                    if (MapAnimator.foregroundRouteAnimator.isRunning())
                        MapAnimator.foregroundRouteAnimator.cancel();
                    doJob();
                    break;

                }
            }
        }


        return super.onOptionsItemSelected(item);
    }

    private void doJob() {
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mMap != null) {
                    mMap.clear();

                }
                addStoppage(busName,"up");
                startAnim();
            }
        }, 1000);
    }


}