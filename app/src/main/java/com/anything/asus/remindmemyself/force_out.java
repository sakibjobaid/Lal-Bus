package com.anything.asus.remindmemyself;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class force_out extends AppCompatActivity implements View.OnClickListener {

    protected DatabaseReference force_out_ref;

    protected Button out,in;
    protected Spinner spinnerBus, spinnerTime;

    protected ArrayList<String> TimeTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_force_out);

        force_out_ref= FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_admin))
                .child("Location");

        spinnerBus = findViewById(R.id.busSpinner2);
        spinnerTime = findViewById(R.id.timeSpinner2);

        TimeTable = new ArrayList<>();


        ArrayAdapter<String> spinnerArrayAdapterBus = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        GlobalClass.BusNameKey);
        spinnerArrayAdapterBus.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerBus.setAdapter(spinnerArrayAdapterBus);

        spinnerBus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                GlobalClass.tempBusName = item.toString();
                BusAndTime b = new BusAndTime(GlobalClass.tempBusName, force_out.this);


                TimeTable = b.getTime();


                timePopulate(TimeTable);


            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        in=findViewById(R.id.force_in_btn);
        out=findViewById(R.id.force_out_btn);

        in.setOnClickListener(this);
        out.setOnClickListener(this);
    }


    private void timePopulate(ArrayList timeTable) {

        ArrayAdapter<String> spinnerArrayAdapterTime = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        timeTable);
        spinnerArrayAdapterTime.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerTime.setAdapter(spinnerArrayAdapterTime);

        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                GlobalClass.tempBustime = item.toString();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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

        if(v==out)
        {

            toastIconInfo("force out active off");
            force_out_ref.child(GlobalClass.tempBusName).child(GlobalClass.tempBustime)
                    .child("force").setValue("out");
            force_out_ref.child(GlobalClass.tempBusName).child(GlobalClass.tempBustime)
                    .child("active_on").setValue("off");
        }
        else if(v==in)
        {
            toastIconInfo("force in active off");

            force_out_ref.child(GlobalClass.tempBusName).child(GlobalClass.tempBustime)
                    .child("force").setValue("in");
            force_out_ref.child(GlobalClass.tempBusName).child(GlobalClass.tempBustime)
                    .child("active_on").setValue("off");
        }


    }
}
