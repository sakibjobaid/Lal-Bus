package com.anything.asus.remindmemyself;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    String name,temps;
    List<Schedule_CardView> listitems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.setTitle("Notifications");

        recyclerView= (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);
        listitems= new ArrayList<>();
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
}
