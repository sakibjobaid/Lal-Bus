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
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PasswordsImage extends AppCompatActivity {


    MenuItem item1,item2;
    int count = 0;
    Bundle b = null;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    String name, temps;
    List<Password_CardView> listitems;
    //public SharedPreferences references, passreference;
    DatabaseReference passRef;
    ValueEventListener passListener;
    Context context;


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
        this.setTitle("Passwords of "+GlobalClass.tempBusName);



        context=PasswordsImage.this;

        passRef= FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_password))
        .child(GlobalClass.tempBusName).child("1");

        Toast.makeText(PasswordsImage.this,"Loading passwords",Toast.LENGTH_SHORT).show();
        doWork();


    }

    private void doWork() {

//        references = getSharedPreferences(getString(R.string.schedule_pref_file), MODE_PRIVATE);
//        temps = references.getString(GlobalClass.tempBusName, "");
//        passreference = getSharedPreferences(getString(R.string.pass_pref_file), MODE_PRIVATE);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);
        listitems = new ArrayList<>();

        passListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long  count=dataSnapshot.getChildrenCount(),i=0;

                for(DataSnapshot time: dataSnapshot.getChildren())
                {
                    i++;
                    Password_CardView abc =
                            new Password_CardView(time.getKey().toString(),time.getValue().toString());
                    listitems.add(abc);

                    if(i==count)
                    {



                        adapter = new PasswordAdapter2(listitems, context);

                        recyclerView.setAdapter(adapter);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        passRef.addListenerForSingleValueEvent(passListener);
//        while (ppx.hasMoreElements()) {
//            String single = (String) ppx.nextElement();
//            StringTokenizer stx = new StringTokenizer(single, "#");
//
//            String tim = (String) stx.nextElement();
//
//            Password_CardView abc =
//                    new Password_CardView(tim, passreference.getString(
//                            GlobalClass.tempBusName + tim
//                            , ""));
//            listitems.add(abc);
//        }
//
//        adapter = new PasswordAdapter2(listitems, this);
//
//        recyclerView.setAdapter(adapter);
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