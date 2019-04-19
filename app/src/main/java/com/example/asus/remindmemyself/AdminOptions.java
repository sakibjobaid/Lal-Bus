package com.example.asus.remindmemyself;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminOptions extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private String TimeTable [] = {""};
    private Spinner spinnerBus,spinnerTime;
    Context context;
    DatabaseReference passreference;
    public SharedPreferences passwordpref;
    private  String passfile="hellopassword";
    ValueEventListener val_pass=null;
    public String popUpBus [] = {"Anando","Boishakhi","Boshonto","Chittagong Road","Choitaly","Falguni","Hemonto","Ishakha","Kinchit","Khonika","Moitree/null","Srabon","Taranga","Ullash","Wari"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);
        context=this;
        passwordpref = getSharedPreferences(passfile, MODE_PRIVATE);
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

                   GlobalClass.BusName= item.toString();
                   BusAndTime b= new BusAndTime(GlobalClass.BusName,context);

                   TimeTable = b.getTime();

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
                GlobalClass.BusTime= item.toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void onClick(View v) {

        Log.d("dhabi", GlobalClass.BusTime);
        Log.d("dhabi",GlobalClass.BusName);

        if(!GlobalClass.BusName.equals("Taranga"))
        {
            Toast.makeText(AdminOptions.this,"Only Taranga bus tracking is available now",Toast.LENGTH_LONG).show();

        }
        else
        {
            finish();
            Intent intent = new Intent(AdminOptions.this,AdminLoginPage.class);

            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,StartActiviy.class));
    }
}