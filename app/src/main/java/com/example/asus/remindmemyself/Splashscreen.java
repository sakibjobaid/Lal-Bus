package com.example.asus.remindmemyself;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.StringTokenizer;


// sakib casdflasjdf
public class Splashscreen extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView img;
    private TextView tv;
    private AlertDialog.Builder dialog;
    private AlertDialog alert;
    private LocationManager lm;
    private static boolean flag = false;
    public String temp,temp2;
    public static int k,l,k1,l1;
    public static intt[]up = null;
    public static intt[]down = null;
    public static intt2[]up2 = null;
    public static intt2[]down2 = null;

    private FusedLocationProviderClient fusedLocationClient;
    DatabaseReference firebaseDatabase,scheduleReference,passreference,
            firstreference,freshReference,routeReference;
    public  SharedPreferences mPreferences,schedulePref,passwordpref,first_pref
            ,freshness_pref,route_pref;
    public static String sharedPrefFile =  "hellosakib",schedulefile="hellojobaid"
            ,passfile="hellopassword",first_file="hellofirst",
            freshness_file="hellofreshness",route_file="helloroute",freshString;
    public static int i=0,sleep=3000;
    public AlertDialog.Builder builder;
    public AlertDialog alertDialog;
    ValueEventListener val=null,val_schedule=null,val_pass=null,
            val_first=null,val_freshness=null,val_route=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        schedulePref = getSharedPreferences(schedulefile, MODE_PRIVATE);
        passwordpref = getSharedPreferences(passfile, MODE_PRIVATE);
        first_pref = getSharedPreferences(first_file, MODE_PRIVATE);
        freshness_pref=getSharedPreferences(freshness_file,MODE_PRIVATE);
        route_pref=getSharedPreferences(route_file,MODE_PRIVATE);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String formattedDate=dateFormat.format(Calendar.getInstance().getTime());
        Log.d("bikas", "Splashscreen:Oncreate");
        Log.d("shala",  Calendar.getInstance().getTime().toString());
        Log.d("shala",  formattedDate.toString());
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        //region route
        routeReference= FirebaseDatabase.getInstance().getReference()
                .child("Route");

        val_route= new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {

                    Log.d("dekhi",snapshot.getKey());

                    for(DataSnapshot bus: snapshot.getChildren()) {

                        if (bus.getKey().equals("Down"))
                        {
                            SharedPreferences.Editor preferencesEditor = route_pref.edit();
                            preferencesEditor.putInt(snapshot.getKey().toString()+"Down", (int) bus.getChildrenCount());
                            preferencesEditor.apply();
                               for(DataSnapshot route: bus.getChildren())
                               {
                                   Log.d("dekhidown",  route.getValue().toString());
                                   preferencesEditor.putString(snapshot.getKey().toString()+"Down"+route.getKey().toString(),route.getValue().toString());
                                   preferencesEditor.apply();

                               }
                        }

                        else if (bus.getKey().equals("Up"))
                        {
                            SharedPreferences.Editor preferencesEditor = route_pref.edit();
                            preferencesEditor.putInt(snapshot.getKey().toString()+"Up", (int) bus.getChildrenCount());
                            preferencesEditor.apply();
                            for(DataSnapshot route: bus.getChildren())
                            {
                                Log.d("dekhiup",  route.getValue().toString());
                                preferencesEditor.putString(snapshot.getKey().toString()+"Up"+route.getKey().toString(),route.getValue().toString());
                                preferencesEditor.apply();

                            }
                        }


                    }

                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        routeReference.addValueEventListener(val_route);
        //endregion

        //region freshness
        freshReference= FirebaseDatabase.getInstance().getReference().child("freshness");
        val_freshness= new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                freshString=dataSnapshot.getValue().toString();
                SharedPreferences.Editor preferencesEditor = first_pref.edit();
                preferencesEditor.putString("fresh_data",dataSnapshot.getValue().toString());
                preferencesEditor.apply();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        freshReference.addValueEventListener(val_freshness);
        //endregion

         //region first_login
        firstreference= FirebaseDatabase.getInstance().getReference()
                .child("first_login");
        val_first= new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                SharedPreferences.Editor preferencesEditor = first_pref.edit();
                preferencesEditor.putString("first_sync",dataSnapshot.getValue().toString());
                preferencesEditor.apply();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        firstreference.addValueEventListener(val_first);
        //endregion

        if(!first_pref.getString("first_sync","").equals("2522")
                &&!isNetworkConnected(Splashscreen.this))
        {
            networkAlert();
        }

        else if(!first_pref.getString("first_sync","").equals("2522")
                && isNetworkConnected(Splashscreen.this))
        {

//            Thread t = new Thread() {
//                            @Override
//            public void run() {
//                try {
//                    Thread.currentThread().sleep(3000);
//
//                } catch (Exception e) {
//
//                }
//            }
//             };
//            t.start();
            ProgressDialog progressDialog=new ProgressDialog(Splashscreen.this);
            progressDialog.setMessage("Fetching data...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            //region password
            passreference= FirebaseDatabase.getInstance().getReference()
                    .child("Passwords");
            val_pass= new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("passki",snapshot.getValue().toString());
                        SharedPreferences.Editor preferencesEditor = passwordpref.edit();
                        preferencesEditor.putString(snapshot.getKey(),snapshot.getValue().toString());
                        preferencesEditor.apply();

                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            };
            passreference.addValueEventListener(val_pass);
            //endregion

            //region time_schedule
            firebaseDatabase= FirebaseDatabase.getInstance().getReference()
                    .child("Admin").child("Location");
            val= new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        up= new intt[50];
                        down= new intt[50];
                        k=0; l=0;

                        Log.d("toadsf",String.valueOf(snapshot.getKey()));
                        Log.d("toadsf",String.valueOf(snapshot.getChildrenCount()));
                        for(DataSnapshot inner : snapshot.getChildren()) {

                            if (inner.getKey().matches("(.*)up"))
                            {
                                up[k]= new intt();
                                StringTokenizer pp = new StringTokenizer(inner.getKey()," up");
                                up[k++].z= (String) pp.nextElement();
                            }
                            else if(inner.getKey().matches("(.*)down"))
                            {
                                String tempo;
                                down[l]=new intt();
                                StringTokenizer pp = new StringTokenizer(inner.getKey()," down");
                                tempo =(String) pp.nextElement();

                                StringTokenizer stx = new StringTokenizer(tempo,":");
                                int a=Integer.parseInt((String) stx.nextElement());
                                int b=Integer.parseInt((String) stx.nextElement());
                                if(a!=12)
                                    a+=12;
                                if(b==0)
                                    down[l++].z=String.valueOf(a)+":00";
                                else
                                    down[l++].z=String.valueOf(a)+":"+String.valueOf(b);

                            }
                            Log.d("toadsf",String.valueOf(inner.getKey()));

                        }

                        sorting(snapshot.getKey());
                    }
//                if(GlobalClass.listTaranga.size()!=0)
//                    GlobalClass.listTaranga.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Log.d("toot",snapshot.getKey());
//                    GlobalClass.listTaranga.add(snapshot.getKey().toString());
//                    Log.d("toad",String.valueOf(GlobalClass.listTaranga.size()));
//                }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            };
            firebaseDatabase.addValueEventListener(val);
            //endregion

            //region Bus_schedule
            scheduleReference=FirebaseDatabase.getInstance().getReference()
                    .child("Bus_Schedule");
            val_schedule= new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        up2= new intt2[50];
                        down2= new intt2[50];
                        k1=0; l1=0;

                        for(DataSnapshot inner : snapshot.getChildren()) {

                            if (inner.getKey().matches("(.*)up"))
                            {
                                up2[k1]= new intt2();
                                up2[k1].time= (String) inner.child("time").getValue();
                                up2[k1].from= (String) inner.child("from").getValue();
                                up2[k1].to= (String) inner.child("to").getValue();
                                up2[k1].type= (String) inner.child("type").getValue();

                                Log.d("khushite",up2[k1].time+" "
                                        +up2[k1].from+" "+up2[k1].to+" "+up2[k1].type+" ");
                                k1++;
                            }
                            else if(inner.getKey().matches("(.*)down"))
                            {
                                down2[l1]= new intt2();
                                down2[l1].time= (String) inner.child("time").getValue();
                                down2[l1].from= (String) inner.child("from").getValue();
                                down2[l1].to= (String) inner.child("to").getValue();
                                down2[l1].type= (String) inner.child("type").getValue();
                                l1++;
                            }

                        }
                        Log.d("sakibjob",String.valueOf(snapshot.getKey()));

                        sorting2(snapshot.getKey());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            scheduleReference.addValueEventListener(val_schedule);
            //endregion
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splashscreen.this,StartActiviy.class));
                    finish();
                }
            }, sleep);

            progressDialog.dismiss();

        }

        else if(first_pref.getString("first_sync","").equals("2522")
                && isNetworkConnected(Splashscreen.this))
        {
            if(!freshness_pref.getString("fresh_data","").equals("1"))
            {
                //region password
                passreference= FirebaseDatabase.getInstance().getReference()
                        .child("Passwords");
                val_pass= new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.d("passki",snapshot.getValue().toString());
                            SharedPreferences.Editor preferencesEditor = passwordpref.edit();
                            preferencesEditor.putString(snapshot.getKey(),snapshot.getValue().toString());
                            preferencesEditor.apply();

                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                };
                passreference.addValueEventListener(val_pass);
                //endregion

                //region time_schedule
                firebaseDatabase= FirebaseDatabase.getInstance().getReference()
                        .child("Admin").child("Location");
                val= new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            up= new intt[50];
                            down= new intt[50];
                            k=0; l=0;

                            Log.d("toadsf",String.valueOf(snapshot.getKey()));
                            Log.d("toadsf",String.valueOf(snapshot.getChildrenCount()));
                            for(DataSnapshot inner : snapshot.getChildren()) {

                                if (inner.getKey().matches("(.*)up"))
                                {
                                    up[k]= new intt();
                                    StringTokenizer pp = new StringTokenizer(inner.getKey()," up");
                                    up[k++].z= (String) pp.nextElement();
                                }
                                else if(inner.getKey().matches("(.*)down"))
                                {
                                    String tempo;
                                    down[l]=new intt();
                                    StringTokenizer pp = new StringTokenizer(inner.getKey()," down");
                                    tempo =(String) pp.nextElement();

                                    StringTokenizer stx = new StringTokenizer(tempo,":");
                                    int a=Integer.parseInt((String) stx.nextElement());
                                    int b=Integer.parseInt((String) stx.nextElement());
                                    if(a!=12)
                                        a+=12;
                                    if(b==0)
                                        down[l++].z=String.valueOf(a)+":00";
                                    else
                                        down[l++].z=String.valueOf(a)+":"+String.valueOf(b);

                                }
                                Log.d("toadsf",String.valueOf(inner.getKey()));

                            }

                            sorting(snapshot.getKey());
                        }
//                if(GlobalClass.listTaranga.size()!=0)
//                    GlobalClass.listTaranga.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Log.d("toot",snapshot.getKey());
//                    GlobalClass.listTaranga.add(snapshot.getKey().toString());
//                    Log.d("toad",String.valueOf(GlobalClass.listTaranga.size()));
//                }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                };
                firebaseDatabase.addValueEventListener(val);
                //endregion

                //region Bus_schedule
                scheduleReference=FirebaseDatabase.getInstance().getReference()
                        .child("Bus_Schedule");
                val_schedule= new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            up2= new intt2[50];
                            down2= new intt2[50];
                            k1=0; l1=0;

                            for(DataSnapshot inner : snapshot.getChildren()) {

                                if (inner.getKey().matches("(.*)up"))
                                {
                                    up2[k1]= new intt2();
                                    up2[k1].time= (String) inner.child("time").getValue();
                                    up2[k1].from= (String) inner.child("from").getValue();
                                    up2[k1].to= (String) inner.child("to").getValue();
                                    up2[k1].type= (String) inner.child("type").getValue();

                                    Log.d("khushite",up2[k1].time+" "
                                            +up2[k1].from+" "+up2[k1].to+" "+up2[k1].type+" ");
                                    k1++;
                                }
                                else if(inner.getKey().matches("(.*)down"))
                                {
                                    down2[l1]= new intt2();
                                    down2[l1].time= (String) inner.child("time").getValue();
                                    down2[l1].from= (String) inner.child("from").getValue();
                                    down2[l1].to= (String) inner.child("to").getValue();
                                    down2[l1].type= (String) inner.child("type").getValue();
                                    l1++;
                                }

                            }
                            Log.d("sakibjob",String.valueOf(snapshot.getKey()));

                            sorting2(snapshot.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                scheduleReference.addValueEventListener(val_schedule);
                //endregion

            }
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splashscreen.this,StartActiviy.class));
                    finish();
                }
            }, sleep);
        }

        else
        {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splashscreen.this,StartActiviy.class));
                    finish();
                }
            }, sleep);
        }

//        while (!isNetworkConnected(Splashscreen.this)) {
//            //Wait to connect
//            Log.d("ammu","just");
////            if(flag)
////                networkAlert();
//            flag=true;
//            //Thread.currentThread().sleep(1000);
//        }


//        //region timer
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//
//        final Intent intent = new Intent(this, StartActiviy.class);
//
//        Thread t = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    //check if connected!
//                    //Thread.currentThread().sleep(3000);
////                    if(!isNetworkConnected(Splashscreen.this))
////                    {
////
////                    }
//
//                    while (!isNetworkConnected(Splashscreen.this)) {
//                        //Wait to connect
//                        Log.d("ammu","just");
//                        if(!flag)
//                        networkAlert();
//                        flag=true;
//                        //Thread.currentThread().sleep(1000);
//                    }
//                    if(flag)
//                    {
//                        Log.d("molla", "one");
//                        startActivity(intent);
//                        finish();
//                    }
//
//
//                } catch (Exception e) {
//                }
//            }
//        };
//        t.start();
//        //endregion
    }

    private void sorting(String name) {
        temp="";
        Log.d("asche","hoise");
        Arrays.sort(up,0,k,new Cmp());
        for(i=0; i<k; ++i) {
            System.out.print(" "+up[i].z);
            temp+=(up[i].z+" up$");
        }
        Arrays.sort(down,0,l,new Cmp());
        for(i=0; i<l; ++i) {
            String tempo1,tempo2;
            System.out.print(" "+down[i].z);
            StringTokenizer stx = new StringTokenizer(down[i].z,":");
            int a=Integer.parseInt((String) stx.nextElement());
            int b=Integer.parseInt((String) stx.nextElement());
            if(a>12)
            {a-=12;tempo1=String.valueOf(a);}
            else tempo1=String.valueOf(a);
            if(b==0)
                tempo2=":00";
            else
                tempo2=":"+String.valueOf(b);
            temp+=(tempo1+tempo2+" down$");
        }
        Log.d("asche",temp);


        Log.d("hey there",temp);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(name, temp);
        preferencesEditor.apply();

    }

    private void sorting2(String name) {
        temp2="";

        Log.d("haider","cholse");
        Arrays.sort(up2,0,k1,new Cmp2());
        Log.d("haider","cholse2");
        for(i=0; i<k1; ++i) {
            temp2+=(up2[i].time+"#");
            temp2+=(up2[i].from+"#");
            temp2+=(up2[i].to+"#");
            temp2+=(up2[i].type+"$");
        }
        Log.d("haider","cholse3");
        Arrays.sort(down2,0,l1,new Cmp2());
        Log.d("haider","cholse4");
        for(i=0; i<l1; ++i) {
            temp2+=(down2[i].time+"#");
            temp2+=(down2[i].from+"#");
            temp2+=(down2[i].to+"#");
            temp2+=(down2[i].type+"$");
        }

        Log.d("william",temp2+"@@");
        SharedPreferences.Editor preferencesEditor = schedulePref.edit();
        preferencesEditor.putString(name, temp2);
        preferencesEditor.apply();

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
        //firebaseDatabase.removeEventListener(val);
        //firebaseDatabase.removeEventListener(val_schedule);
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

        Log.d("jobaid","Splashascreen: is NetworkConnected [outside isNetworkConnected]");
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
        if(result) flag=true;
        return result;
    }

    private void networkAlert() {

        Log.d("hoynaken", "firstactivity : networkalert");
        builder = new AlertDialog.Builder(Splashscreen.this);

        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("No internet !!");
        builder.setMessage("Launching the app for the first time requires data synchronization." +
                "Internet connection is needed.");


        builder.setNegativeButton("TRY AGAIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(isNetworkConnected(Splashscreen.this))
                {

                    dialog.dismiss();
                    ProgressDialog progressDialog=new ProgressDialog(Splashscreen.this);
                    progressDialog.setMessage("Fetching data...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                        //region password
                        passreference= FirebaseDatabase.getInstance().getReference()
                                .child("Passwords");
                        val_pass= new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Log.d("passki",snapshot.getValue().toString());
                                    SharedPreferences.Editor preferencesEditor = passwordpref.edit();
                                    preferencesEditor.putString(snapshot.getKey(),snapshot.getValue().toString());
                                    preferencesEditor.apply();

                                }



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        };
                        passreference.addValueEventListener(val_pass);
                        //endregion

                        //region time_schedule
                        firebaseDatabase= FirebaseDatabase.getInstance().getReference()
                                .child("Admin").child("Location");
                        val= new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    up= new intt[50];
                                    down= new intt[50];
                                    k=0; l=0;

                                    Log.d("toadsf",String.valueOf(snapshot.getKey()));
                                    Log.d("toadsf",String.valueOf(snapshot.getChildrenCount()));
                                    for(DataSnapshot inner : snapshot.getChildren()) {

                                        if (inner.getKey().matches("(.*)up"))
                                        {
                                            up[k]= new intt();
                                            StringTokenizer pp = new StringTokenizer(inner.getKey()," up");
                                            up[k++].z= (String) pp.nextElement();
                                        }
                                        else if(inner.getKey().matches("(.*)down"))
                                        {
                                            String tempo;
                                            down[l]=new intt();
                                            StringTokenizer pp = new StringTokenizer(inner.getKey()," down");
                                            tempo =(String) pp.nextElement();

                                            StringTokenizer stx = new StringTokenizer(tempo,":");
                                            int a=Integer.parseInt((String) stx.nextElement());
                                            int b=Integer.parseInt((String) stx.nextElement());
                                            if(a!=12)
                                                a+=12;
                                            if(b==0)
                                                down[l++].z=String.valueOf(a)+":00";
                                            else
                                                down[l++].z=String.valueOf(a)+":"+String.valueOf(b);

                                        }
                                        Log.d("toadsf",String.valueOf(inner.getKey()));

                                    }

                                    sorting(snapshot.getKey());
                                }
//                if(GlobalClass.listTaranga.size()!=0)
//                    GlobalClass.listTaranga.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Log.d("toot",snapshot.getKey());
//                    GlobalClass.listTaranga.add(snapshot.getKey().toString());
//                    Log.d("toad",String.valueOf(GlobalClass.listTaranga.size()));
//                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        };
                        firebaseDatabase.addValueEventListener(val);
                        //endregion

                        //region Bus_schedule
                        scheduleReference=FirebaseDatabase.getInstance().getReference()
                                .child("Bus_Schedule");
                        val_schedule= new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    up2= new intt2[50];
                                    down2= new intt2[50];
                                    k1=0; l1=0;

                                    for(DataSnapshot inner : snapshot.getChildren()) {

                                        if (inner.getKey().matches("(.*)up"))
                                        {
                                            up2[k1]= new intt2();
                                            up2[k1].time= (String) inner.child("time").getValue();
                                            up2[k1].from= (String) inner.child("from").getValue();
                                            up2[k1].to= (String) inner.child("to").getValue();
                                            up2[k1].type= (String) inner.child("type").getValue();

                                            Log.d("khushite",up2[k1].time+" "
                                                    +up2[k1].from+" "+up2[k1].to+" "+up2[k1].type+" ");
                                            k1++;
                                        }
                                        else if(inner.getKey().matches("(.*)down"))
                                        {
                                            down2[l1]= new intt2();
                                            down2[l1].time= (String) inner.child("time").getValue();
                                            down2[l1].from= (String) inner.child("from").getValue();
                                            down2[l1].to= (String) inner.child("to").getValue();
                                            down2[l1].type= (String) inner.child("type").getValue();
                                            l1++;
                                        }

                                    }
                                    Log.d("sakibjob",String.valueOf(snapshot.getKey()));

                                    sorting2(snapshot.getKey());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        scheduleReference.addValueEventListener(val_schedule);
                        //endregion


                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("thisss","is");
                    progressDialog.dismiss();
                    startActivity(new Intent(Splashscreen.this,StartActiviy.class));
                     finish();
                }

                else
                {
                    networkAlert();
                }
            }
        });

        AlertDialog dialog=builder.create();
        dialog.setCancelable(false);
        //builder.setCancelable(false);
        //builder.show();
        dialog.show();

//        final Context context =Splashscreen.this;
//        dialog = new AlertDialog.Builder(context);
//        dialog.setMessage("Check your internet connection");
//        dialog.setTitle("Network unavailable");
//        dialog.setPositiveButton("Network settings", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                // check = true;
//                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
//                context.startActivity(intent);
//            }
//        });
//        alert = dialog.create();
//        alert.show();
//        alert.setCanceledOnTouchOutside(false);
//        alert.setCancelable(false);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
    }
}

class intt {
    public String z;
}

class intt2 {
    public String time,from,to,type;
}

class Cmp implements Comparator<intt>
{
    public int compare(intt x,intt y) {

        String strx = x.z,stry=y.z;

        StringTokenizer stx = new StringTokenizer(strx,":");
        int a=Integer.parseInt((String) stx.nextElement());
        int b=Integer.parseInt((String) stx.nextElement());

        StringTokenizer sty = new StringTokenizer(stry,":");
        int c=Integer.parseInt((String) sty.nextElement());
        int d=Integer.parseInt((String) sty.nextElement());



        if(a==c)
        {
            if(b==d)return 0;
            if(b<d)return -1;
            else return 1;
        }
        else if(a<c) return -1;
        else return 1;

    }
}

class Cmp2 implements Comparator<intt2>
{
    public int compare(intt2 x,intt2 y) {

        Log.d("williams","sdfa");
        String strx = x.time,stry=y.time;
        int a=0,b=0,c=0,d=0;
        if(x.time.matches("(.*)down"))
        {
            StringTokenizer ppx = new StringTokenizer(strx," down");
            String tempox=(String) ppx.nextElement();
            StringTokenizer stx = new StringTokenizer(tempox,":");
            a=Integer.parseInt((String) stx.nextElement());
            b=Integer.parseInt((String) stx.nextElement());
            if(a!=12)
                a+=12;
            StringTokenizer ppy= new StringTokenizer(stry," down");
            String tempoy=(String) ppy.nextElement();
            StringTokenizer sty = new StringTokenizer(tempoy,":");
            c=Integer.parseInt((String) sty.nextElement());
            if(c!=12)
                c+=12;
            d=Integer.parseInt((String) sty.nextElement());

        }

        else if(x.time.matches("(.*)up"))
        {
            StringTokenizer ppx = new StringTokenizer(strx," up");
            String tempox=(String) ppx.nextElement();
            StringTokenizer stx = new StringTokenizer(tempox,":");
            a=Integer.parseInt((String) stx.nextElement());
            b=Integer.parseInt((String) stx.nextElement());
            StringTokenizer ppy= new StringTokenizer(stry," up");
            String tempoy=(String) ppy.nextElement();
            StringTokenizer sty = new StringTokenizer(tempoy,":");
            c=Integer.parseInt((String) sty.nextElement());
            d=Integer.parseInt((String) sty.nextElement());

        }

        if(a==c)
        {
            if(b==d)return 0;
            if(b<d)return -1;
            else return 1;
        }
        else if(a<c) return -1;
        else return 1;

    }
}