package com.anything.asus.remindmemyself;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActiviy extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback {

    //region variable

    private ImageView mAdmin,mUser;
    private LocationManager lm;
    public static Activity st;
    private boolean ajkeiFirst=false;
    public AlertDialog.Builder builder;
    private SharedPreferences phonePref,busNamePref,busTimePref,date_pref;
    private SharedPreferences.Editor preferencesEditor,busNamePrefEditor,busTimeprefEditor;


    public static DatabaseReference admin_list_ref=null,dateReference=null;
    public static ValueEventListener val_admin_list_ref=null,val_date=null;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activiy);

        if(!GlobalClass.AdminVirgin && GlobalClass.ContactPref!=null)
        {
            admin_list_ref= FirebaseDatabase.getInstance().getReference()
                    .child(getString(R.string.Firebase_admin_list))
                    .child(GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter),""));
        }

        if(isNetworkConnected(this))
        {
            //region date
            dateReference = FirebaseDatabase.getInstance().getReference()
                    .child(getString(R.string.Firebase_date));
            val_date = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    SharedPreferences.Editor preferencesEditor = date_pref.edit();
                    preferencesEditor.putString(getString(R.string.date_getter), dataSnapshot.getValue().toString());
                    preferencesEditor.apply();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            dateReference.addValueEventListener(val_date);
            //endregion
        }

        st=this;
        GlobalClass.p=true;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        phonePref = getSharedPreferences(getString(R.string.phone_pref_file), MODE_PRIVATE);
        busNamePref = getSharedPreferences(getString(R.string.bus_name_pref_file), MODE_PRIVATE);
        busTimePref = getSharedPreferences(getString(R.string.bus_time_pref_file), MODE_PRIVATE);
        date_pref = getSharedPreferences(getString(R.string.date_pref_file), MODE_PRIVATE);
        if(phonePref.getString(getString(R.string.phone_number_getter),"default").equals("default"))
            ajkeiFirst=true;

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mAdmin=(ImageView) findViewById(R.id.admin);
        mUser=(ImageView) findViewById(R.id.user);
        mAdmin.setOnClickListener(this);
        mUser.setOnClickListener(this);



        if(!busNamePref.getString(getString(R.string.bus_name_getter),"noBusName").equals("noBusName")
                && !busTimePref.getString(getString(R.string.bus_time_getter),"noBusTime").equals("noBusTime")
                )
        {

            if(isNetworkConnected(this))
            {
                if(dateReference!=null && val_date!=null)
                    dateReference.removeEventListener(val_date);
                gotoNextActivity();
            }

            if(!isNetworkConnected(this))
            {
                networkAlert();
            }
        }
    }

    private void gotoNextActivity() {
        Intent intent2 = new Intent(StartActiviy.this, firstActivity.class);
        GlobalClass.BusName= busNamePref.getString(getString(R.string.bus_name_getter),"noBusName");
        GlobalClass.BusTime= busTimePref.getString(getString(R.string.bus_time_getter),"noBusTime");
        intent2.putExtra("BUSNAME",GlobalClass.BusName);
        intent2.putExtra("TIME",GlobalClass.BusTime );
        intent2.putExtra("name", "admin");

        
        dowork();
        startActivity(intent2);
        finish();
        overridePendingTransition(R.anim.slidein_from_left,R.anim.slidein_from_right);

    }

    private void dowork() {
    }

    private void networkAlert() {
        //region builder
        builder = new AlertDialog.Builder(StartActiviy.this);

        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("No internet !!");
        builder.setMessage("You are already logged in as admin mode." +
                "Internet connection is needed.");
        //endregion

        builder.setNegativeButton("TRY AGAIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (isNetworkConnected(StartActiviy.this)) {

                    gotoNextActivity();

                } else {
                    networkAlert();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
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


    @Override
    public void onClick(View v) {

        Intent intent=null;
        if(v==mAdmin) {
            if (!isNetworkConnected(this)) {

                toastIconInfo("Internet connection required");
                return;
            }

            if (ajkeiFirst) {
                preferencesEditor = phonePref.edit();
                preferencesEditor.putString(getString(R.string.phone_number_getter), "noNumber");
                preferencesEditor.apply();
                busNamePrefEditor = busNamePref.edit();
                busNamePrefEditor.putString(getString(R.string.bus_name_getter), "noBusName");
                busNamePrefEditor.apply();
                busTimeprefEditor = busTimePref.edit();
                busTimeprefEditor.putString(getString(R.string.bus_time_getter), "noBusTime");
                busTimeprefEditor.apply();
            }
            if (!busNamePref.getString(getString(R.string.bus_name_getter), "noBusName").equals("noBusName")
                    && !busTimePref.getString(getString(R.string.bus_time_getter), "noBusTime").equals("noBusTime")
                    ) {
                Intent intent2 = new Intent(StartActiviy.this, firstActivity.class);
                GlobalClass.BusName = busNamePref.getString(getString(R.string.bus_name_getter), "noBusName");
                GlobalClass.BusTime = busTimePref.getString(getString(R.string.bus_time_getter), "noBusTime");
                intent2.putExtra("BUSNAME", GlobalClass.BusName);
                intent2.putExtra("TIME", GlobalClass.BusTime);
                intent2.putExtra("name", "admin");
                startActivity(intent2);
                finish();
                overridePendingTransition(R.anim.slidein_from_left,R.anim.slidein_from_right);

            } else {
                intent = new Intent(StartActiviy.this, AdminOptions.class);
                startActivity(intent);

            }





    }
        if(v==mUser)
        {
                intent= new Intent(StartActiviy.this,firstActivity.class);
                intent.putExtra("name","user");
                intent.putExtra("BUSNAME","BUS");
                intent.putExtra("TIME","TIME");

            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slidein_from_left,R.anim.slidein_from_right);


        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        GoogleMap mMap;
                mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng jigatala = new LatLng(23.738487, 90.373503);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jigatala, 15f));

    }
}