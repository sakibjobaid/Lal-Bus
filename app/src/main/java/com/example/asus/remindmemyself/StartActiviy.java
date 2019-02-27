package com.example.asus.remindmemyself;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActiviy extends AppCompatActivity implements View.OnClickListener {

    private Button mAdmin,mUser;
    private AlertDialog.Builder dialog;
    private AlertDialog alert;
    private LocationManager lm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            alertMethod();
//        }
//        if(!isNetworkConnected(this))
//        {
//            networkAlert(StartActiviy.this);
//        }

        setContentView(R.layout.activity_start_activiy);
        mAdmin=(Button)findViewById(R.id.admin);
        mUser=(Button)findViewById(R.id.user);

        mAdmin.setOnClickListener(this);
        mUser.setOnClickListener(this);
    }

    public static boolean isNetworkConnected(Context context) {

        Log.d("jobaid","firstactivity: is NetworkConnected [outside isNetworkConnected]");
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = true;
                    }
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        result = true;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    private void networkAlert(StartActiviy firstActivity) {

        Log.d("jobaid", "firstactivity : networkalert");
        final Context context =StartActiviy.this;
        dialog = new AlertDialog.Builder(context);
        dialog.setMessage(R.string.network_msg);
        dialog.setTitle(R.string.network_unavailable);
        dialog.setPositiveButton("Network settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               // check = true;
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                context.startActivity(intent);
            }
        });
        alert = dialog.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);

    }

    private void alertMethod() {

        Log.d("jobaid", "firstactivity : alertMethod [alertdialog]");
        final Context context = StartActiviy.this;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.GPS_unavailable);
        dialog.setMessage(R.string.GPS_msg);

        dialog.setPositiveButton("GPS settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //check = true;
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);

            }
        });
        alert = dialog.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
    }


    @Override
    public void onClick(View v) {

        Intent intent=null;

        if(v==mAdmin)
        {
            //intent= new Intent(this,firstActivity.class);
            //intent.putExtra("name","admin");
            Toast.makeText(this,"button clicked",Toast.LENGTH_LONG).show();
            intent= new Intent(this,AdminLoginPage.class);
            startActivity(intent);
            finish();
        }
        if(v==mUser)
        {
           // intent= new Intent(this,firstActivity.class);
            //intent.putExtra("name","user");
            intent= new Intent(this,UserLoginPage.class);
            startActivity(intent);
            finish();
        }
    }
    
}
