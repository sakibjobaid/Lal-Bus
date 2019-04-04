package com.example.asus.remindmemyself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class AdminOptions extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private String Bus="",Time="";
    private String TimeTable [] = {" "};
    private Spinner spinnerBus,spinnerTime;
    public String popUpBus [] = {"Anando","Boishakhi","Boshonto","Chittagong Road","Choitaly","Falguni","Hemonto","Ishakha","Kinchit","Khonika","Moitree/null","Srabon","Taranga","Ullash","Wari"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);
        spinnerBus = findViewById(R.id.busSpinner);
        spinnerTime = findViewById(R.id.timeSpinner);

        ArrayAdapter<String> spinnerArrayAdapterBus = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        popUpBus);
        spinnerArrayAdapterBus.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerBus.setAdapter(spinnerArrayAdapterBus);

        spinnerBus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                Bus= item.toString();
                TimeTable = new BusAndTime(Bus).getTime();

                timePopulate(TimeTable);

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        button=findViewById(R.id.ButtonContinue);
        button.setOnClickListener(this);

    }

    private void timePopulate(String[] timeTable) {

        ArrayAdapter<String> spinnerArrayAdapterTime = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        TimeTable );
        spinnerArrayAdapterTime.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerTime.setAdapter(spinnerArrayAdapterTime);

        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                Time = item.toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void onClick(View v) {

        Log.d("dhabi", Time);
        Log.d("dhabi",Bus);

        if(!Bus.equals("Taranga"))
        {
            Toast.makeText(AdminOptions.this,"Only Taranga bus tracking is available now",Toast.LENGTH_LONG).show();

        }
        else
        {
            finish();
            Intent intent = new Intent(AdminOptions.this,firstActivity.class);
            intent.putExtra("BUSNAME",Bus);
            intent.putExtra("TIME",Time);
            intent.putExtra("name","admin");
            startActivity(intent);
        }
    }
}