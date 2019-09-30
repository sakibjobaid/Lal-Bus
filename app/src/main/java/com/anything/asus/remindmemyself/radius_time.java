package com.anything.asus.remindmemyself;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class radius_time extends AppCompatActivity implements View.OnClickListener {

    protected Button rad_before,rad_after,time_before,time_after;
    protected EditText radBefore,radAfter,timeBefore,timeAfter;
    protected DatabaseReference rad_before_ref,rad_after_ref,
            time_before_ref,time_after_ref;

    protected SharedPreferences RadBefore,RadAfter,TimeBefore,TimeAfter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radius_time);

        rad_before_ref= FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_admin_radius_aage));
        rad_after_ref= FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_admin_radius_pore));
        time_before_ref= FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_aage));
        time_after_ref= FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_pore));

        RadBefore = getSharedPreferences("helloadminradiusaage",MODE_PRIVATE);
        RadAfter = getSharedPreferences("helloadminradiuspore",MODE_PRIVATE);
        TimeBefore = getSharedPreferences("helloadminaage",MODE_PRIVATE);
        TimeAfter = getSharedPreferences("helloadminpore",MODE_PRIVATE);

        rad_before=findViewById(R.id.rad_before_btn);
        rad_after=findViewById(R.id.rad_after_btn);
        time_before=findViewById(R.id.time_before_btn);
        time_after=findViewById(R.id.time_after_btn);


        radBefore=findViewById(R.id.new_rad_before);
        radAfter=findViewById(R.id.new_rad_after);
        timeBefore=findViewById(R.id.new_time_before);
        timeAfter=findViewById(R.id.new_time_after);


        radBefore.setText(String.valueOf(RadBefore.getLong("admin_radius_aage",0)));
        radAfter.setText(String.valueOf(RadAfter.getLong("admin_radius_pore",0)));
        timeBefore.setText(String.valueOf(TimeBefore.getLong("admin_aage",0)));
        timeAfter.setText(String.valueOf(TimeAfter.getLong("admin_pore",0)));

        rad_after.setOnClickListener(this);
        rad_before.setOnClickListener(this);
        time_after.setOnClickListener(this);
        time_before.setOnClickListener(this);
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

        if(v==rad_before)
        {
            if(radBefore.getText().toString().trim().length()==0)
            {
                toastIconInfo("Empty input");
                return;
            }
            else
            {
                toastIconInfo("Radius before is set to "+radBefore.getText().toString());

                 rad_before_ref.setValue(radBefore.getText().toString().trim());

            }
        }
        else if(v==rad_after)
        {
            if(radAfter.getText().toString().trim().length()==0)
            {
                toastIconInfo("Empty input");
                return;
            }
            else
            {
                toastIconInfo("Radius after is set to "+radAfter.getText().toString().trim());


                rad_after_ref.setValue(radAfter.getText().toString().trim());
            }
        }
        else if(v==time_before)
        {
            if(timeBefore.getText().toString().trim().length()==0)
            {
                toastIconInfo("Empty input");
                return;
            }
            else
            {
                toastIconInfo("Time before is set to "+timeBefore.getText().toString().trim());


                time_before_ref.setValue(timeBefore.getText().toString().trim());
            }
        }
        else if(v==time_after)
        {
            if(timeAfter.getText().toString().trim().length()==0)
            {
                toastIconInfo("Empty input");
                return;
            }
            else
            {
                toastIconInfo("Time after is set to "+timeAfter.getText().toString().trim());

                time_after_ref.setValue(timeAfter.getText().toString().trim());

            }
        }

    }
}
