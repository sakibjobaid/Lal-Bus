package com.anything.asus.remindmemyself;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class RouteSettings extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener,

        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener {

    //region variable
    int count = 0;
    boolean  flagRoute = true, flagStoppage = false,
            routemode = true, stoppagemode = false;
    private TextView customtextView;
    private EditText customeditText, customDescription;
    private GoogleMap mMap;

    public static LatLng geofenceCentre;
    private ImageView marker;

    public static Context context;

    public static String id;


    private FloatingActionButton fabsave, fabreverse, fabput, fabstoppage;
    public static AlertDialog.Builder dialog;
    public static AlertDialog alert;

    public boolean twotap = false;

    private static final long DOUBLE_CLICK_TIME_DELTA = 500;
    public long lastClickTime;
    static PolylineOptions pop;

    private OnCompleteListener route=null,stoppage=null;



    public Marker[] mkArray = new Marker[200];


    //endregion

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_settings);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        intialize();

        pop = new PolylineOptions();


        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyCxhbMAoJp-6CUVhAgn5SUH0kDg0j9Z1fA");

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocompleteR_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                LatLng search_latlng = place.getLatLng();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(search_latlng, 15f));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });


        setContext(this);


        fabsave = (FloatingActionButton) findViewById(R.id.fabsave);
        fabsave.setOnClickListener(this);
        fabreverse = (FloatingActionButton) findViewById(R.id.fabreverse);
        fabreverse.setOnClickListener(this);
        fabput = (FloatingActionButton) findViewById(R.id.fabput);
        fabput.setOnClickListener(this);
        fabstoppage = (FloatingActionButton) findViewById(R.id.fabStoppage);
        fabstoppage.setOnClickListener(this);
        fabstoppage.setVisibility(View.GONE);

        marker = findViewById(R.id.marker);

    }

    private void intialize() {

        GlobalClass.adminselectedStoppage="";
        GlobalClass.adminselectedRoute="";
        GlobalClass.cRoute.clear();
        GlobalClass.cStoppage.clear();
        GlobalClass.mkcount=0;
        GlobalClass.markerDescription.clear();
        GlobalClass.markerSerial.clear();
        GlobalClass.markerLatlng.clear();
        GlobalClass.markerName.clear();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveListener(this);

        LatLng jigatala = new LatLng(23.732627, 90.395610);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jigatala, 15f));

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {

                mMap.setOnCameraMoveCanceledListener((GoogleMap.OnCameraMoveCanceledListener) context);

            }
        }, 2000);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                id = marker.getId();

                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                    twotap = true;
                    openCustomDialog();
                    lastClickTime = 0;
                } else {

                    twotap = false;
                    MarkerOptions mk = new MarkerOptions();

                    mk.draggable(false);
                    mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    marker.setTitle(GlobalClass.markerName.get(id));
                    marker.setSnippet(GlobalClass.markerDescription.get(id));
                    marker.showInfoWindow();
                }
                lastClickTime = clickTime;


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




    }


    private void toastIconInfo(String purpose) {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(purpose);
        ((CardView) custom_view.findViewById(R.id.parent_view2)).setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));

        toast.setView(custom_view);
        toast.show();
    }

    @SuppressLint("RestrictedApi")

    @Override
    public void onClick(View v) {

        //region save
        if (v == fabsave) {

            if (GlobalClass.cRoute.size() <= 1) {
                toastIconInfo("Empty route cannot be saved");
                return;
            }

            if (routemode) {

                AlertDialog.Builder builder = new AlertDialog.Builder(RouteSettings.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Route Marking completed?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        routemode = false;
                        flagRoute = false;

                        transferRoute();
                        stoppagemode = true;
                        flagStoppage = true;
                        fabput.setVisibility(View.GONE);
                        fabstoppage.setVisibility(View.VISIBLE);
                        fabsave.setImageDrawable(ContextCompat.getDrawable(RouteSettings.this, R.drawable.save_icon));
                        fabstoppage.setVisibility(View.VISIBLE);



                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();

            } else if (stoppagemode) {


                if(GlobalClass.cStoppage.size()==0)
                {
                    toastIconInfo("Empty stoppage cannot be saved");
                    return;

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(RouteSettings.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Your route and stoppage will be saved in the database. Do you want to continue?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        transferStoppage();
                        flagRoute = false;
                        flagStoppage = true;



                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();


            }


        }//endregion

        //region put
        if (v == fabput) {

            pop = new PolylineOptions();

            ++count;
            double lat = geofenceCentre.latitude, lon = geofenceCentre.longitude;
            GlobalClass.cRoute.add(new LatLng(lat, lon));
            pop.addAll(GlobalClass.cRoute).width(7f);
            pop.color(Color.BLUE).geodesic(true);


            if (count >= 2)
                mMap.addPolyline(pop);

        }
        //endregion

        // region stoppage
        if (v == fabstoppage) {

            openCustomDialog();


        }//endregion

        //region reverse
        if (v == fabreverse) {


            if (flagRoute) {
                pop.visible(false);
                pop = new PolylineOptions();


                if (GlobalClass.cRoute.size() > 0)
                    GlobalClass.cRoute.remove(GlobalClass.cRoute.size() - 1);
                if (GlobalClass.cRoute.size() == 0)
                    GlobalClass.adminselectedRoute = "";
                mMap.clear();
                pop.addAll(GlobalClass.cRoute).width(7f);
                pop.color(Color.BLUE).geodesic(true);

                if (count >= 2)
                    mMap.addPolyline(pop);
            }

            if (flagStoppage) {

                LatLng lat = null;
                if (GlobalClass.mkcount > 0) {
                    GlobalClass.mkcount--;
                    GlobalClass.cStoppage.remove(GlobalClass.cStoppage.size() - 1);

                }

                mMap.clear();
                mMap.addPolyline(pop);
                for (int i = 0; i < GlobalClass.mkcount; i++) {
                    MarkerOptions mk = new MarkerOptions();
                    mk.draggable(false);
                    mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    lat = GlobalClass.markerLatlng.get(mkArray[i].getId());
                    double lati = lat.latitude, longi = lat.longitude;
                    String tit = GlobalClass.markerName.get(mkArray[i].getId());
                    String des = GlobalClass.markerDescription.get(mkArray[i].getId());

                    mMap.addMarker(mk.position(new LatLng(lati, longi)).title(tit).snippet(des)).showInfoWindow();

                }
                if(lat!=null)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat,15f));


            }

        }
        //endregion

    }


    private void insertIntoDatabase() {

        final ProgressDialog progressDialog = new ProgressDialog(RouteSettings.this);
        progressDialog.setMessage("Saving your changes...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        route=new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if(task.isSuccessful())
                {
                    if (progressDialog != null)
                        progressDialog.dismiss();

                    finish();
                }
            }
        };

        stoppage= new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful())
                {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    SharedPreferences route_pref= getSharedPreferences(getString(R.string.route_pref_file),MODE_PRIVATE);
                    SharedPreferences.Editor preferencesEditor = route_pref.edit();
                    preferencesEditor.putString(GlobalClass.tempBusName + GlobalClass.routeDir
                            + GlobalClass.routeNumber, GlobalClass.adminselectedRoute);
                    preferencesEditor.apply();

                    finish();
                }

            }
        };

        DatabaseReference refRoute = FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_route))
                .child(GlobalClass.tempBusName).child("1");
        refRoute.child(GlobalClass.routeDir).child(GlobalClass.routeNumber)
                .setValue(GlobalClass.adminselectedRoute).addOnCompleteListener(route);

        DatabaseReference refStoppage = FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_stoppage))
                .child(GlobalClass.tempBusName).child("1");
        refStoppage.child(GlobalClass.routeDir).child(GlobalClass.routeNumber)
                .setValue(GlobalClass.adminselectedStoppage).addOnCompleteListener(stoppage);
    }

    private void transferStoppage() {


        int x = GlobalClass.cStoppage.size();
        for (int i = 0; i < x; i++) {
            GlobalClass.adminselectedStoppage += (GlobalClass.cStoppage.get(i));
        }

        insertIntoDatabase();
    }

    private void transferRoute() {

        for (int i = 0; i < GlobalClass.cRoute.size(); i++) {
            GlobalClass.adminselectedRoute += (String.valueOf(GlobalClass.cRoute.get(i).latitude)
                    + "," + String.valueOf(GlobalClass.cRoute.get(i).longitude) + "$");
        }


    }

    private void openCustomDialog() {

        final Dialog dialog = new Dialog(RouteSettings.this);
        dialog.setContentView(R.layout.stoppage_custom_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        customtextView = (TextView) dialog.findViewById(R.id.emailText);
        customtextView.setText("Stoppage of " + GlobalClass.tempBusName);
        customeditText = (EditText) dialog.findViewById(R.id.mpasswordText);
        customDescription = (EditText) dialog.findViewById(R.id.opasswordText);
        if (twotap) {
            customeditText.setText(GlobalClass.markerName.get(id));
            customDescription.setText(GlobalClass.markerDescription.get(id));
        }

        Button loginButton = (Button) dialog.findViewById(R.id.mlogButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(GlobalClass.mkcount>=190)
                {
                    toastIconInfo("Marker limit exceeded. Can't addd more");
                    return ;
                }


                if (twotap) {
                    if (customeditText.getText().toString().trim().equals("") ||
                            customDescription.getText().toString().trim().equals("")) {
                        toastIconInfo("Fill up all the informations");
                        return;
                    }
                    twotap = false;
                    GlobalClass.markerName.put(id, customeditText.getText().toString());
                    GlobalClass.markerDescription.put(id, customDescription.getText().toString());
                } else {
                    if (customeditText.getText().toString().trim().equals("") ||
                            customDescription.getText().toString().trim().equals("")) {
                        toastIconInfo("Fill up all the informations");
                        return;
                    }
                    MarkerOptions mk = new MarkerOptions();
                    mk.draggable(false);
                    mk.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    mkArray[GlobalClass.mkcount] = mMap.addMarker(mk
                            .position(new LatLng(geofenceCentre.latitude, geofenceCentre.longitude)));
                    mkArray[GlobalClass.mkcount].setTitle(customeditText.getText().toString());
                    mkArray[GlobalClass.mkcount].setSnippet(customDescription.getText().toString());
                    String TitDesc = String.valueOf(geofenceCentre.latitude) + "@"
                            + String.valueOf(geofenceCentre.longitude) + "@"
                            + customeditText.getText().toString().trim() + "@"
                            + customDescription.getText().toString().trim() + "$";
                    GlobalClass.cStoppage.add(TitDesc);
                    GlobalClass.markerName.put(mkArray[GlobalClass.mkcount].getId(), customeditText.getText().toString());
                    GlobalClass.markerDescription.put(mkArray[GlobalClass.mkcount].getId(), customDescription.getText().toString());
                    GlobalClass.markerSerial.put(GlobalClass.mkcount, mkArray[GlobalClass.mkcount].getId());
                    GlobalClass.markerLatlng.put(mkArray[GlobalClass.mkcount].getId(), new LatLng(geofenceCentre.latitude, geofenceCentre.longitude));
                    mkArray[GlobalClass.mkcount].showInfoWindow();
                    GlobalClass.mkcount++;
                }


                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onCameraIdle() {


        geofenceCentre = mMap.getCameraPosition().target;

    }

    @Override
    public void onCameraMove() {

        geofenceCentre = mMap.getCameraPosition().target;


    }

    public void setContext(RouteSettings context) {
        this.context = context;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        SharedPreferences busNamePref = getSharedPreferences(getString(R.string.bus_name_pref_file), MODE_PRIVATE);
        SharedPreferences busTimePref = getSharedPreferences(getString(R.string.bus_time_pref_file), MODE_PRIVATE);
        SharedPreferences.Editor preEditor = busTimePref.edit();
        preEditor.putString(getString(R.string.bus_time_getter), GlobalClass.BusTime);
        preEditor.apply();

        SharedPreferences.Editor pre1Editor = busNamePref.edit();
        pre1Editor.putString(getString(R.string.bus_name_getter), GlobalClass.tempBusName);
        pre1Editor.apply();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

}

