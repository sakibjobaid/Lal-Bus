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



        scheduleList = new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scheduleList.add(new Schedule("Taranga"));
        scheduleList.add(new Schedule("Srabon"));
        scheduleList.add(new Schedule("Baishakhi"));
        scheduleList.add(new Schedule("Kinchit"));
        scheduleList.add(new Schedule("Choitali"));
        scheduleList.add(new Schedule("Taranga"));
        scheduleList.add(new Schedule("Srabon"));
        scheduleList.add(new Schedule("Baishakhi"));
        scheduleList.add(new Schedule("Kinchit"));
        scheduleList.add(new Schedule("Choitali"));
        scheduleList.add(new Schedule("Taranga"));
        scheduleList.add(new Schedule("Srabon"));
        scheduleList.add(new Schedule("Baishakhi"));
        scheduleList.add(new Schedule("Kinchit"));
        scheduleList.add(new Schedule("Choitali"));
        scheduleAdapter = new ScheduleAdapter(this,scheduleList);
        recyclerView.setAdapter(scheduleAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
