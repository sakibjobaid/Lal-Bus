package com.anything.asus.remindmemyself;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class RouteOptions extends AppCompatActivity implements View.OnClickListener {

    //region variable
    private Button button;
    private TextView dirtv,routetv;
    private SharedPreferences route_pref;
    private String dir[] = {"up","down"};
    private String routeNo[] = {"Route1","Route2","Route3","Route4","Route5"};
    private Spinner spinnerDir, spinnerRoute;
    Context context;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_options);
        context = this;

        route_pref=getSharedPreferences(getString(R.string.route_pref_file),MODE_PRIVATE);

        spinnerDir =(Spinner) findViewById(R.id.dirSpinner);
        spinnerRoute = (Spinner)findViewById(R.id.routeSpinner);
        dirtv=(TextView) findViewById(R.id.ParaTvDir);
        routetv=(TextView)findViewById(R.id.ParaTvRoute);



              ArrayAdapter<String> spinnerArrayAdapterDir = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        dir);
        spinnerArrayAdapterDir.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerDir.setAdapter(spinnerArrayAdapterDir);

        spinnerDir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                GlobalClass.routeDir = item.toString();



            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        ArrayAdapter<String> spinnerArrayAdapterRoute = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        routeNo);
        spinnerArrayAdapterRoute.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerRoute.setAdapter(spinnerArrayAdapterRoute);

        spinnerRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                GlobalClass.routeNumber = item.toString();




            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        button = findViewById(R.id.ButtonContinue);
        button.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, StartActiviy.class));
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(pass_change!=null)
//        passChange_reference.removeEventListener(pass_change);

    }

    @Override
    public void onClick(View v) {

        int number=route_pref.getInt(GlobalClass.tempBusName+GlobalClass.routeDir,0);
        int selectedNumber= (GlobalClass.routeNumber.charAt(5)-'0');





        if(selectedNumber-number>1 )
        {
            toastIconInfo("Select Route"+String.valueOf(number+1));
            return;
        }


        if(v==button)
        {
            startActivity(new Intent(RouteOptions.this,RouteSettings.class));
            finish();
            overridePendingTransition(R.anim.slidein_from_left,R.anim.slidein_from_right);

        }


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
}