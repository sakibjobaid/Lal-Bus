package com.anything.asus.remindmemyself;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.StringTokenizer;

public class SettingsPage extends AppCompatActivity implements View.OnClickListener {

    private Button add,delete,pass,route;
    private ProgressBar progressBar;
    private Spinner dir,routeNo;
    private TextView tv;
    String direction[] ={"Up","Down"};
    String route_no[] ={"Route1","Route2","Route3","Route4","Route5"};
    String selectedDir,selectedRoute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        this.setTitle("Admin Settings Page");


        add=(Button)findViewById(R.id.addButton);
        delete=(Button)findViewById(R.id.deleteButton);
        pass=(Button) findViewById(R.id.password);
        route=(Button)findViewById(R.id.routeChange);

        delete.setOnClickListener(this);
        add.setOnClickListener(this);
        pass.setOnClickListener(this);
        route.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==add )
        {
            if(timeChecking())
            startActivity(new Intent(SettingsPage.this, AddPage.class));
            else
            {
                showToast();
                return;
            }

        }

        else if(v==delete)
        {
            if(timeChecking())
            startActivity(new Intent(SettingsPage.this, DeletePage.class));
            else
            {
                showToast();
                return;
            }

        }



        else if(v==route )
        {
            if( timeChecking())
            startActivity(new Intent(SettingsPage.this,RouteOptions.class));
            else
            {
                showToast();
                return;
            }

        }

        else if(v==pass)
        {
            startActivity(new Intent(SettingsPage.this, PasswordsImage.class));

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


    private void showToast() {

        toastIconInfo("Settings are allowed to change within 8:00 pm - 5:00 am");
//        Toast toast= Toast.makeText(SettingsPage.this,"Settings are allowed to change within 8:00 pm - 5:00 am"
//                ,Toast.LENGTH_LONG);
//        TextView toastTv = (TextView) toast.getView().findViewById(android.R.id.message);
//        if( toastTv != null) toastTv.setGravity(Gravity.CENTER);
//        toast.show();
    }

    private boolean timeChecking() {


        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(GlobalClass.clock+System.currentTimeMillis());
        GlobalClass.timeStampTime= android.text.format.DateFormat.format("HH:mm", cal).toString();

        StringTokenizer sty = new StringTokenizer(GlobalClass.timeStampTime, ":");
        int c = Integer.parseInt((String) sty.nextElement());
        int d = Integer.parseInt((String) sty.nextElement());
        if(c>=20)return true;
        if(c==5 && d>0)return false;
        if(c<=5)return true;

        return false;
        //return true;

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
