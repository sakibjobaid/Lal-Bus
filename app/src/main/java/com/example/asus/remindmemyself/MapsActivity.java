package com.example.asus.remindmemyself;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener, View.OnClickListener {

    //region variables
    private static LatLng POINT_A = new LatLng(23.757011,90.3616399);
    private static  LatLng POINT_B = new LatLng(23.7272146,90.4004568);
    private int  upDown=0;
    private String  busName;
    private Toolbar toolbar;
    private List<LatLng> Route;
    private boolean flag=true;
    public static int count=0;
    private static GoogleMap mMap;
    public static Marker mk;
    private FloatingActionButton fab;
    private TextView tv;
    public Menu main_menu;

    SharedPreferences route_pref;
    String route_file="helloroute",temp="",routeCount_file="hellocount";
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        route_pref= getSharedPreferences(route_file, MODE_PRIVATE);
        fab= (FloatingActionButton) findViewById(R.id.fabtrip);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        busName = getIntent().getStringExtra("name");
        this.setTitle(busName+ " Up Trip");
        tv= findViewById(R.id.mywidget);
        tv.setText("Route may change in any unavoidable situation. "+"              "+" Tap on marker to see stoppage location");
        tv.setSelected(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Log.d("jobaid","MapsActivity:onCreate");
        fab.setOnClickListener((View.OnClickListener) this);

        fab.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.down));



        POINT_A = new LatLng(23.757011,90.3616399);

        POINT_B = new LatLng(23.7272146,90.4004568);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        createRouteUp(busName);
    }

    private void createRouteUp(String busName) {

        Log.d("jobaid","MapsActivity:createRoute");

        if (Route == null) {
            Log.d("jobaid","if");
            Route = new ArrayList<>();
        } else {
            Route.clear();
        }


        //region TarangaUp
        if(busName.equals(" Taranga"))
        {
            temp=route_pref.getString("TarangaUpRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }
        }
        //endregion
        //region KhonikaUp
        else if(busName.equals(" Khonika"))
        {

            temp=route_pref.getString("KhonikaUpRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }


        }
        //endregion
        //region BoishakhiUp
        else if(busName.equals(" Boishakhi"))
        {

            temp=route_pref.getString("BoishakhiUpRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }

        }
        //endregion
        //region FalguniUp
        else if(busName.equals(" Falguni"))
        {

            temp=route_pref.getString("FalguniUpRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }

        }
        //endregion
        //region UllashUp
        else if(busName.equals(" Ullash"))
        {

            temp=route_pref.getString("UllashUpRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }

        }
        //endregion
        //region SrabonUp
        else if(busName.equals(" Srabon"))
        {

            temp=route_pref.getString("SrabonUpRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }
        }
        //endregion
        //region KinchitUp
        else if(busName.equals(" Kinchit"))
        {

            temp=route_pref.getString("KinchitUpRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }

        }
        //endregion
        //region Boishakhi2Up
        else if(busName.equals(" Boishakhi2"))
        {

            temp=route_pref.getString("BoishakhiUpRoute2","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalal","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }

       }
        //endregion
        //region Boishakhi3Up
        else if(busName.equals(" Boishakhi3"))
        {

            temp=route_pref.getString("BoishakhiUpRoute3","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }


        }
        //endregion
        //region ChoitalyUp
        else if(busName.equals(" Choitaly"))
        {

            temp=route_pref.getString("ChoitalyUpRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }
        }
        //endregion
        //region AnandoUp
        else if(busName.equals(" Anando"))
        {

            temp=route_pref.getString("AnandoUpRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }

        }
        //endregion
        //region BoshontoUp
        else if(busName.equals(" Boshonto"))
        {
            temp=route_pref.getString("BoshontoUpRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }




        }
        // endregion
        //region IshakhaUp
        else if(busName.equals(" Ishakha"))
        {
            temp=route_pref.getString("IshakhaUpRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }


        }
        //endregion
        //region HemontoUp
        else if(busName.equals(" Hemonto"))
        {
            temp=route_pref.getString("HemontoUpRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }


        }
        //endregion



    }

    private void creatRouteDown(String busName) {

        Log.d("jobaid","MapsActivity:createRoute");

        if (Route == null) {
            Log.d("jobaid","if");
            Route = new ArrayList<>();
        } else {
            Route.clear();
        }

        //region TarangaDown
        if(busName.equals(" Taranga"))
        {
            temp=route_pref.getString("TarangaDownRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }

        }
        //endregion
        //region KhonikaDown
        else if(busName.equals(" Khonika"))
        {

            temp=route_pref.getString("KhonikaDownRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }
        }
        //endregion
        //region KhonikaDown2
        else if(busName.equals(" Khonika"))
        {
            Route.add(new LatLng(23.727741,90.400382));
            Route.add(new LatLng(23.7279178,90.4000816));
            Route.add(new LatLng(23.7272892,90.3975496));
            Route.add(new LatLng(23.7293715,90.3953824));
            Route.add(new LatLng(23.7305501,90.3953609));
            Route.add(new LatLng(23.7315126,90.3952322));
            Route.add(new LatLng(23.7320822,90.3951249));
            Route.add(new LatLng(23.7324751,90.3948674));
            Route.add(new LatLng(23.7328876,90.3944597));
            Route.add(new LatLng(23.7326715,90.3954682));
            Route.add(new LatLng(23.7329072,90.395597));
            Route.add(new LatLng(23.7341251,90.3954682));
            Route.add(new LatLng(23.735559,90.3954897));
            Route.add(new LatLng(23.7371894,90.3956184));
            Route.add(new LatLng(23.7384858,90.3958545));
            Route.add(new LatLng(23.7399,90.3959618));
            Route.add(new LatLng(23.7411963,90.3959832));
            Route.add(new LatLng(23.7424338,90.3957257));
            Route.add(new LatLng(23.7433569,90.3954468));
            Route.add(new LatLng(23.744614,90.3950391));
            Route.add(new LatLng(23.745596,90.3947172));
            Route.add(new LatLng(23.7487768,90.3936465));
            Route.add(new LatLng(23.7504014, 90.3930846));
            Route.add(new LatLng(23.7525127, 90.3922585));
            Route.add(new LatLng(23.7545258, 90.3914753));
            Route.add(new LatLng(23.7572753, 90.3903809));
            Route.add(new LatLng(23.7586206, 90.3899089));
            Route.add(new LatLng(23.7602408, 90.3894475));
            Route.add(new LatLng(23.7617726, 90.3892329));
            Route.add(new LatLng(23.7634419, 90.3890398));
            Route.add(new LatLng(23.7655138, 90.3889218));
            Route.add(new LatLng(23.7677623, 90.3890935));
            Route.add(new LatLng(23.7691566, 90.3893295));
            Route.add(new LatLng(23.7724066, 90.3899625));
            Route.add(new LatLng(23.7731822, 90.390102));
            Route.add(new LatLng(23.775303, 90.3898552));
            Route.add(new LatLng(23.7754699, 90.3912392));
            Route.add(new LatLng(23.7756957, 90.3927413));
            Route.add(new LatLng(23.7762259, 90.3938249));
            Route.add(new LatLng(23.7777772, 90.3966788));
            Route.add(new LatLng(23.7783368, 90.3978911));
            Route.add(new LatLng(23.7787884, 90.3982774));
            Route.add(new LatLng(23.7800059, 90.3985563));
            Route.add(new LatLng(23.78302, 90.3990069));
            Route.add(new LatLng(23.7880858, 90.3998545));
            Route.add(new LatLng(23.7905205,90.4002729 ));
            Route.add(new LatLng(23.7941822, 90.4009059));
            Route.add(new LatLng(23.7975788, 90.4014316));
            Route.add(new LatLng(23.8022907,90.402247 ));
            Route.add(new LatLng(23.8068454, 90.4030302));
            Route.add(new LatLng(23.8092896, 90.4034057));
            Route.add(new LatLng(23.812676, 90.404028));
            Route.add(new LatLng(23.8148942, 90.4043499));
            Route.add(new LatLng(23.8158659, 90.4048756));
            Route.add(new LatLng(23.816445, 90.4054657));
            Route.add(new LatLng(23.8168671, 90.4061952));
            Route.add(new LatLng(23.8169653, 90.4076651));
            Route.add(new LatLng(23.8168377, 90.4110339));
            Route.add(new LatLng(23.8169358, 90.4119244));
            Route.add(new LatLng(23.8177603, 90.4132548));
            Route.add(new LatLng(23.8185946, 90.4145208));
            Route.add(new LatLng(23.8200177, 90.4167739));
            Route.add(new LatLng(23.8212446, 90.4184368));
            Route.add(new LatLng(23.8220298, 90.4193488));
            Route.add(new LatLng(23.8230603, 90.4200032));
            Route.add(new LatLng(23.8244344, 90.4203788));
            Route.add(new LatLng(23.8257299, 90.420486));
            Route.add(new LatLng(23.8281541, 90.4200569));
            Route.add(new LatLng(23.8311965, 90.4195312));
            Route.add(new LatLng(23.8357501, 90.4188123));
            Route.add(new LatLng(23.8380269, 90.4181364));
            Route.add(new LatLng(23.8392536,90.4175463));
            Route.add(new LatLng(23.8407158, 90.4165915));
            Route.add(new LatLng(23.8425312, 90.4151431));
            Route.add(new LatLng(23.8452986, 90.412772));
            Route.add(new LatLng(23.8458186, 90.4123858));
            Route.add(new LatLng(23.8496358, 90.4091242));
            Route.add(new LatLng(23.8502835, 90.4085449));
            Route.add(new LatLng(23.8503325, 90.4083088));
            Route.add(new LatLng(23.8505484, 90.4082659));
            Route.add(new LatLng(23.8506956, 90.4083303));
            Route.add(new LatLng(23.8518535, 90.4073754));
            Route.add(new LatLng(23.8563181, 90.403674));
            Route.add(new LatLng(23.858624, 90.4016891));
            Route.add(new LatLng(23.8607041, 90.4001978));
            Route.add(new LatLng(23.8622445, 90.399876));
            Route.add(new LatLng(23.8638438, 90.3999189));
            Route.add(new LatLng(23.8662967, 90.4001227));
            Route.add(new LatLng(23.8679352, 90.4002622));
            Route.add(new LatLng(23.8715163, 90.4004982));
            Route.add(new LatLng(23.8738513, 90.4006162));
            Route.add(new LatLng(23.875058, 90.4004553));
            Route.add(new LatLng(23.8760784, 90.4007128));
            Route.add(new LatLng(23.8775401, 90.4011205));
            Route.add(new LatLng(23.8784525, 90.4013029));
            Route.add(new LatLng(23.8794532, 90.4012385));
            Route.add(new LatLng(23.8809836, 90.4009917));
            Route.add(new LatLng(23.882926,  90.4006699));
            Route.add(new LatLng(23.8841915,  90.4004446));
            Route.add(new LatLng(23.8854472,  90.4002515));
            Route.add(new LatLng(23.8863399,  90.4002622));
            Route.add(new LatLng(23.8872424,  90.4005841));
            Route.add(new LatLng(23.8882627,   90.400981));
            Route.add(new LatLng(23.8892633,   90.4013136));
            Route.add(new LatLng(23.8921865,   90.4019788));
            Route.add(new LatLng(23.8927457,   90.4019359));
            Route.add(new LatLng(23.8931478,   90.4017857));
            Route.add(new LatLng(23.8939718,   90.4011312));
            Route.add(new LatLng(23.8949037,   90.4002836));
            Route.add(new LatLng(23.8955609,   90.3997579));
            Route.add(new LatLng(23.8966105,   90.3991571));
            Route.add(new LatLng(23.8970519,   90.3989747));
            Route.add(new LatLng(23.89869,   90.3988782));
            Route.add(new LatLng(23.9017602,   90.3990498));
            Route.add(new LatLng(23.9037906,   90.3992322));
            Route.add(new LatLng(23.9053502,   90.3990606));
            Route.add(new LatLng(23.907253,    90.398567));
            Route.add(new LatLng(23.9088517,    90.3979984));



            Route.add(new LatLng(23.90965410,90.3976036));
            Route.add(new LatLng(23.91085070,90.3970135));
            Route.add(new LatLng(23.91264560,90.3961659));
            Route.add(new LatLng(23.91412660,90.3955115));
            Route.add(new LatLng(23.91684330,90.3943098));
            Route.add(new LatLng(23.92024650,90.3928293));
            Route.add(new LatLng(23.92259040,90.3918422));
            Route.add(new LatLng(23.9264740,90.3902543));
            Route.add(new LatLng(23.93023980,90.3886987));
            Route.add(new LatLng(23.93351520,90.387379));
            Route.add(new LatLng(23.93752590,90.3857375));
            Route.add(new LatLng(23.94050690,90.3843964));
            Route.add(new LatLng(23.94260530,90.383581));
            Route.add(new LatLng(23.94517440,90.3826047));
            Route.add(new LatLng(23.94768450,90.3817035));
            Route.add(new LatLng(23.94845910,90.381446));
            Route.add(new LatLng(23.9493710,90.3813065));
            Route.add(new LatLng(23.9508810,90.3811992));
            Route.add(new LatLng(23.9536950,90.3811563));
            Route.add(new LatLng(23.95695020,90.3809846));
            Route.add(new LatLng(23.95915620,90.3807915));
            Route.add(new LatLng(23.96161710,90.3805877));
            Route.add(new LatLng(23.96463670,90.3802122));
            Route.add(new LatLng(23.96656810,90.3801049));
            Route.add(new LatLng(23.96989150,90.3802122));
            Route.add(new LatLng(23.97353840,90.3803624));
            Route.add(new LatLng(23.97547950,90.380534));
            Route.add(new LatLng(23.97755770,90.3807271));
            Route.add(new LatLng(23.97968490,90.3808988));
            Route.add(new LatLng(23.98239050,90.3811241));
            Route.add(new LatLng(23.98391970,90.3812743));
            Route.add(new LatLng(23.98633110,90.3815425));
            Route.add(new LatLng(23.9887130,90.3817571));
            Route.add(new LatLng(23.98953640,90.3818644));
            Route.add(new LatLng(23.9896540,90.3827012));
            Route.add(new LatLng(23.98983050,90.3840423));
            Route.add(new LatLng(23.99000690,90.3848363));
            Route.add(new LatLng(23.990350,90.3868748));
            Route.add(new LatLng(23.9907420,90.3889132));
            Route.add(new LatLng(23.99108510,90.390941));
            Route.add(new LatLng(23.99137920,90.3929366));
            Route.add(new LatLng(23.99156540,90.394503));
            Route.add(new LatLng(23.99220250,90.399567));
            Route.add(new LatLng(23.99266320,90.4021634));
            Route.add(new LatLng(23.99302590,90.4046417));
            Route.add(new LatLng(23.99338860,90.4064978));
            Route.add(new LatLng(23.99392770,90.4083861));
            Route.add(new LatLng(23.99467260,90.410843));
            Route.add(new LatLng(23.99518230,90.4131282));
            Route.add(new LatLng(23.99555470,90.4145015));
            Route.add(new LatLng(23.99604480,90.4154027));
            Route.add(new LatLng(23.99677990,90.4169155));


        }
        //endregion
        //region BoishakhiDown
        else if(busName.equals(" Boishakhi"))
        {
            temp=route_pref.getString("BoishakhiDownRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }


        }
        //endregion
        //region FalguniDown
        else if(busName.equals(" Falguni"))
        {
            temp=route_pref.getString("FalguniDownRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }



        }
        //endregion
        //region UllashDown
        else if(busName.equals(" Ullash"))
        {
            temp=route_pref.getString("UllashDownRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }

        }
        //endregion
        //region SrabonDown
        else if(busName.equals(" Srabon"))
        {

            temp=route_pref.getString("SrabonDownRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }
        }
        //endregion
        //region KinchitDown
        else if(busName.equals(" Kinchit"))
        {

            temp=route_pref.getString("KinchitDownRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }

        }
        //endregion
        //region Boishakhi2Down
        else if(busName.equals(" Boishakhi2"))
        {

            temp=route_pref.getString("BoishakhiDownRoute2","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }

        }
        //endregion
        //region Boishakhi3Down
        else if(busName.equals(" Boishakhi3"))
        {

            temp=route_pref.getString("BoishakhiDownRoute3","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }

        }//endregion
        //region ChoitalyDown
        else if(busName.equals(" Choitaly"))
        {
            temp=route_pref.getString("ChoitalyDownRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }

        }
        //endregion
        //region AnandoDown

        else if(busName.equals(" Anando"))
        {


            temp=route_pref.getString("AnandoDownRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }


        }
        //endregion
        //region BoshontoDown
        else if(busName.equals(" Boshonto"))
        {
            temp=route_pref.getString("BoshontoDownRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }


        }
        // endregion
        //region IshakhaDown
        else if(busName.equals(" Ishakha"))
        {
            temp=route_pref.getString("IshakhaDownRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }

        }
        //endregion
        //region HemontoDown
        else if(busName.equals(" Hemonto"))
        {
            temp=route_pref.getString("HemontoDownRoute1","");
            StringTokenizer ppx = new StringTokenizer(temp,"$");
            Log.d("lalala","apple");

            Log.d("lalala",temp);
            while(ppx.hasMoreElements())
            {
                StringTokenizer stx = new StringTokenizer(ppx.nextElement().toString(),",");
                double lat=Double.parseDouble(stx.nextElement().toString());
                double lon=Double.parseDouble(stx.nextElement().toString());
                Log.d("friend", String.valueOf(lat)+" "+String.valueOf(lon));
                Route.add(new LatLng(lat,lon));

            }
        }
        //endregion

    }

    public static GoogleMap getmMap() {
        return mMap;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        Log.d("jobaid","MapsActivity:onMapReady");
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener) this);
        LatLng sydney = new LatLng(23.742461, 90.373904);
        mk =mMap.addMarker(new MarkerOptions().position(sydney)
                .title("Zigatala")
                .snippet("Ecstasy")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));


        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                Log.d("jobaid","ashse");
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(POINT_A);
                builder.include(POINT_B);
                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);

                mMap.moveCamera(cu);

                mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null);

                startAnim();
            }
        });
    }

    private void startAnim(){

        Log.d("jobaid","MapsActivity:startAnim");

        if(mMap != null) {
            MapAnimator.getInstance().animateRoute(mMap, Route);
        }

        else {
            Toast.makeText(getApplicationContext(), "Map not ready", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("jobaid","fowl");
        count++;
        if(count%2==0)
        {mk.hideInfoWindow();Log.d("jobaid","hide "+String.valueOf(count));}
        else
        {
            mk.showInfoWindow();
            Log.d("jobaid","show "+String.valueOf(count));

        }


        return true;
    }

    @Override
    public void onClick(View v) {
        ++upDown;
        if(upDown%2==1)
        {
            main_menu.clear();
            onCreateOptionsMenu(main_menu);
            creatRouteDown(busName);
            if(MapAnimator.foregroundRouteAnimator.isRunning())MapAnimator.foregroundRouteAnimator.cancel();
            startAnim();
            fab.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.up));
            GlobalClass.up=0;
            GlobalClass.down=1;
            this.setTitle(busName+ " Down Trip");
        }
        else
        {

            main_menu.clear();
            onCreateOptionsMenu(main_menu);
            createRouteUp(busName);
            if(MapAnimator.foregroundRouteAnimator.isRunning())MapAnimator.foregroundRouteAnimator.cancel();

            startAnim();
            fab.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.down));

            GlobalClass.up=1;
            GlobalClass.down=0;
            this.setTitle(busName+ " Up Trip");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        main_menu=menu;
        //getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        int item_count=-1;

        if(GlobalClass.up==1)
        {
            //region up menu
            if(busName.equals(" Taranga"))
            {
                item_count=route_pref.getInt("TarangaUp",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }


            }

            else if(busName.equals(" Boishakhi"))
            {
                item_count=route_pref.getInt("BoishakhiUp",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Hemonto"))
            {
                item_count=route_pref.getInt("HemontoUp",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }


            }

            else if(busName.equals(" Boshonto"))
            {
                item_count=route_pref.getInt("BoshontoUp",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Choitaly"))
            {
                item_count=route_pref.getInt("ChoitalyUp",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Falguni"))
            {
                item_count=route_pref.getInt("FalguniUp",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Ishakha"))
            {
                item_count=route_pref.getInt(" IshakhaUp",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Kinchit"))
            {
                item_count=route_pref.getInt("KinchitUp",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Khonika"))
            {
                item_count=route_pref.getInt("KhonikaUp",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Moitree"))
            {
                item_count=route_pref.getInt("MoitreeUp",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Srabon"))
            {
                item_count=route_pref.getInt("SrabonUp",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Ullash"))
            {
                item_count=route_pref.getInt("UllashUp",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Wari"))
            {
                item_count=route_pref.getInt("WariUp",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Anando"))
            {
                item_count=route_pref.getInt("AnandoUp",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Chittagong Road"))
            {
                item_count=route_pref.getInt("Chittagong RoadUp",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }
            //endregion
        }

        else if(GlobalClass.down==1)
        {
            //region down menu
            if(busName.equals(" Taranga"))
            {
                item_count=route_pref.getInt("TarangaDown",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }


            }

            else if(busName.equals(" Boishakhi"))
            {
                item_count=route_pref.getInt("BoishakhiDown",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Hemonto"))
            {
                item_count=route_pref.getInt("HemontoDown",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }


            }

            else if(busName.equals(" Boshonto"))
            {
                item_count=route_pref.getInt("BoshontoDown",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Choitaly"))
            {
                item_count=route_pref.getInt("ChoitalyDown",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Falguni"))
            {
                item_count=route_pref.getInt("FalguniDown",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Ishakha"))
            {
                item_count=route_pref.getInt(" IshakhaDown",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Kinchit"))
            {
                item_count=route_pref.getInt("KinchitDown",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Khonika"))
            {
                item_count=route_pref.getInt("KhonikaDown",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Moitree"))
            {
                item_count=route_pref.getInt("MoitreeDown",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Srabon"))
            {
                item_count=route_pref.getInt("SrabonDown",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Ullash"))
            {
                item_count=route_pref.getInt("UllashDown",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Wari"))
            {
                item_count=route_pref.getInt("WariDown",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Anando"))
            {
                item_count=route_pref.getInt("AnandoDown",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }

            else if(busName.equals(" Chittagong Road"))
            {
                item_count=route_pref.getInt("Chittagong RoadDown",-1);
                if(item_count>1)
                {
                    for(int i=1;i<=item_count;i++)
                    {
                        menu.add(Menu.NONE,i,Menu.NONE,"Route "+ String.valueOf(i));
                        menu.findItem(i);
                        menu.findItem(i).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

                    }
                    return true;

                }
            }
            //endregion
        }

       return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(GlobalClass.up==1)
        {
            //region up menu item
            if(busName.equals(" Taranga"))
            {
                switch (item.getItemId())
                {
                    case 1:
                    {
                        createRouteUp(" Taranga");
                        if(MapAnimator.foregroundRouteAnimator.isRunning())MapAnimator.foregroundRouteAnimator.cancel();
                        startAnim();
                        break;
                    }
                    case 2:
                    {
                        createRouteUp(" Taranga");
                        if(MapAnimator.foregroundRouteAnimator.isRunning())MapAnimator.foregroundRouteAnimator.cancel();
                        startAnim();
                        break;
                    }
                }
            }
            else if(busName.equals(" Hemonto"))
            {
                switch (item.getItemId())
                {
                    case 1:
                    {
                        createRouteUp(" Hemonto");
                        if(MapAnimator.foregroundRouteAnimator.isRunning())MapAnimator.foregroundRouteAnimator.cancel();
                        startAnim();
                        break;
                    }
                    case 2:
                    {
                        createRouteUp(" Hemonto");
                        if(MapAnimator.foregroundRouteAnimator.isRunning())MapAnimator.foregroundRouteAnimator.cancel();
                        startAnim();
                        break;
                    }
                }
            }

            else if(busName.equals(" Boishakhi"))
            {
                switch (item.getItemId())
                {
                    case 1:
                    {
                        createRouteUp(" Boishakhi");
                        if(MapAnimator.foregroundRouteAnimator.isRunning())MapAnimator.foregroundRouteAnimator.cancel();
                        startAnim();
                        break;
                    }
                    case 2:
                    {
                        createRouteUp(" Boishakhi2");
                        if(MapAnimator.foregroundRouteAnimator.isRunning())MapAnimator.foregroundRouteAnimator.cancel();
                        startAnim();
                        break;
                    }
                    case 3:
                    {
                        createRouteUp(" Boishakhi3");
                        if(MapAnimator.foregroundRouteAnimator.isRunning())MapAnimator.foregroundRouteAnimator.cancel();
                        startAnim();
                        break;
                    }
                }
            }
            //endregion
        }
        else if(GlobalClass.down==1)
        {
            //region down menu item
            if(busName.equals(" Taranga"))
            {
                switch (item.getItemId())
                {
                    case 1:
                    {
                        Toast.makeText(this,"Taranga up",Toast.LENGTH_LONG).show();
                        break;
                    }
                    case 2:
                    {
                        Toast.makeText(this,"asdf up",Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
            else if(busName.equals(" Hemonto"))
            {
                switch (item.getItemId())
                {
                    case 1:
                    {
                        Toast.makeText(this,"Hemonto up",Toast.LENGTH_LONG).show();
                        break;
                    }
                    case 2:
                    {
                        Toast.makeText(this,"kkkk up",Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }

            else if(busName.equals(" Boishakhi"))
            {
                switch (item.getItemId())
                {
                    case 1:
                    {
                        creatRouteDown(" Boishakhi");
                        if(MapAnimator.foregroundRouteAnimator.isRunning())MapAnimator.foregroundRouteAnimator.cancel();
                        startAnim();
                        break;
                    }
                    case 2:
                    {
                        creatRouteDown(" Boishakhi2");
                        if(MapAnimator.foregroundRouteAnimator.isRunning())MapAnimator.foregroundRouteAnimator.cancel();
                        startAnim();                    break;
                    }
                    case 3:
                    {
                        creatRouteDown(" Boishakhi3");
                        if(MapAnimator.foregroundRouteAnimator.isRunning())MapAnimator.foregroundRouteAnimator.cancel();
                        startAnim();                    break;
                    }
                }
            }
            //endregion
        }



        return super.onOptionsItemSelected(item);
    }


}