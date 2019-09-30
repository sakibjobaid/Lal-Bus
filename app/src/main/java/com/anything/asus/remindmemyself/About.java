package com.anything.asus.remindmemyself;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    TextView tauhid_vai,sakib;
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tauhid_vai=findViewById(R.id.tauhidID);
        sakib= findViewById(R.id.sakibID);

        tauhid_vai.setText("Tauhid Tanjim");
        sakib.setText("Sakib Jobaid");

        this.setTitle("Contributor");
    }

    public String getFacebookPageURL(Context context,String name ,String link) {

        String fblink = link;
        String final_href = "<a href='" + fblink + "'>" +
                name + "</a>";

        PackageManager packageManager = context.getPackageManager();
        try {

            final PackageInfo pInfo = context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            long versionCode;

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                versionCode = pInfo.getLongVersionCode(); // avoid huge version numbers and you will be ok
            } else {
                //noinspection deprecation
                versionCode = pInfo.versionCode;
            }
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + final_href;
            } else { //older versions of fb app
                return "goThroughWeb";
            }
        } catch (PackageManager.NameNotFoundException e) {
            return final_href; //normal web url
        }
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

    public boolean isAppInstalled() {
        try {

            getApplicationContext().getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {

            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void goThroughWeb(String name,String link) {


        String final_href = "<a href='" + link + "'>" +
                name + "</a>";


        Spanned html;
        html = Html.fromHtml(final_href);
        if(name.equals("Tauhid Tanjim"))
        {
            tauhid_vai.setText(html);
            tauhid_vai.setMovementMethod(LinkMovementMethod.getInstance());
        }
        else
        {
            sakib.setText(html);
            sakib.setMovementMethod(LinkMovementMethod.getInstance());
        }

    }
}
