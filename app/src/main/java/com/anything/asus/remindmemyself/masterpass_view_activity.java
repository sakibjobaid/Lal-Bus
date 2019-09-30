package com.anything.asus.remindmemyself;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class masterpass_view_activity extends AppCompatActivity {



    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter adapter;
    protected String name, temps;
    protected List<master_password_cardview> listitems;
    protected DatabaseReference masterReference;
    protected ValueEventListener masterVal;
    protected Context context;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        this.setTitle("Master Admin Passwords");
        context=masterpass_view_activity.this;

        masterReference= FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_masterAdmin))
                .child("1");


        Toast.makeText(masterpass_view_activity.this,"Loading master passwords",Toast.LENGTH_SHORT).show();



        doWork();


    }

    private void doWork() {



        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);
        listitems = new ArrayList<>();

        masterVal= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long count=dataSnapshot.getChildrenCount(),x=0;

                for(DataSnapshot busName: dataSnapshot.getChildren())
                {
                    x++;
                    master_password_cardview abc = new master_password_cardview(
                            busName.getKey().toString(),busName.getValue().toString());
                    listitems.add(abc);

                    if(count==x)
                    {


                        adapter = new MasterPasswordAdapter(listitems, context);

                        recyclerView.setAdapter(adapter);
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        masterReference.addListenerForSingleValueEvent(masterVal);
//        StringTokenizer ppx = new StringTokenizer(temps, "$");
//        while (ppx.hasMoreElements()) {
//            String single = (String) ppx.nextElement();
//            StringTokenizer stx = new StringTokenizer(single, "#");
//
//            String busName = (String) stx.nextElement();
//            String password= (String) stx.nextElement();
//
//
//        }


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
        preEditor.putString(getString(R.string.bus_time_getter), GlobalClass.BusTime);
        preEditor.apply();

        SharedPreferences.Editor pre1Editor = busNamePref.edit();
        pre1Editor.putString(getString(R.string.bus_name_getter), GlobalClass.tempBusName);
        pre1Editor.apply();

        GlobalClass.edit=true;
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



}