package com.example.asus.remindmemyself;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class RecylerViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ScheduleAdapter scheduleAdapter;
    List<Schedule> scheduleList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyler_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String title= getIntent().getStringExtra("name");
        switch (title)
        {
            case "Bus Schedule":  this.setTitle("Bus Schedule"); break;
            case "Route And Stoppage":  this.setTitle("Route And Stoppage"); break;
        }

        if(title.equals("Bus Schedule"))
        {
            scheduleList = new ArrayList<>();
            recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
            //recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            scheduleList.add(new Schedule("Anando"));
            scheduleList.add(new Schedule("Boishakhi"));
            scheduleList.add(new Schedule("Boshonto"));
            scheduleList.add(new Schedule("Chittagong Road"));
            scheduleList.add(new Schedule("Choitaly"));
            scheduleList.add(new Schedule("Falguni"));
            scheduleList.add(new Schedule("Hemonto"));
            scheduleList.add(new Schedule("Ishakha"));
            scheduleList.add(new Schedule("Kinchit"));
            scheduleList.add(new Schedule("Khonika"));
            scheduleList.add(new Schedule("Moitree"));
            scheduleList.add(new Schedule("Srabon"));
            scheduleList.add(new Schedule("Taranga"));
            scheduleList.add(new Schedule("Ullash"));
            scheduleList.add(new Schedule("Wari"));
            scheduleAdapter = new ScheduleAdapter(this,scheduleList);
            recyclerView.setAdapter(scheduleAdapter);
        }

        else if(title.equals("Route And Stoppage"))
        {
            scheduleList = new ArrayList<>();
            recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
            //recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            scheduleList.add(new Schedule(" Anando"));
            scheduleList.add(new Schedule(" Boishakhi"));
            scheduleList.add(new Schedule(" Boshonto"));
            scheduleList.add(new Schedule(" Chittagong Road"));
            scheduleList.add(new Schedule(" Choitaly"));
            scheduleList.add(new Schedule(" Falguni"));
            scheduleList.add(new Schedule(" Hemonto"));
            scheduleList.add(new Schedule(" Ishakha"));
            scheduleList.add(new Schedule(" Kinchit"));
            scheduleList.add(new Schedule(" Khonika"));
            scheduleList.add(new Schedule(" Moitree"));
            scheduleList.add(new Schedule(" Srabon"));
            scheduleList.add(new Schedule(" Taranga"));
            scheduleList.add(new Schedule(" Ullash"));
            scheduleList.add(new Schedule(" Wari"));
            scheduleAdapter = new ScheduleAdapter(this,scheduleList);
            recyclerView.setAdapter(scheduleAdapter);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}