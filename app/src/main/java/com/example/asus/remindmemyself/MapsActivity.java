package com.example.asus.remindmemyself;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fab= (FloatingActionButton) findViewById(R.id.fabtrip);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        busName = getIntent().getStringExtra("name");
        this.setTitle(busName+ " Up Trip");

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

        createRouteUp();
    }

    private void createRouteUp() {

        Log.d("jobaid","MapsActivity:createRoute");

        if (Route == null) {
            Log.d("jobaid","if");
            Route = new ArrayList<>();
        } else {
            Route.clear();
        }

        //region tarangaUp
        if(busName.equals(" Taranga"))
        {
            Route.add(new LatLng(23.757011,90.3616399));
            Route.add(new LatLng(23.7559504,90.3627772));
            Route.add(new LatLng(23.7553612,90.363614));
            Route.add(new LatLng(23.7544185,90.3646869));
            Route.add(new LatLng(23.7535544,90.3656525));
            Route.add(new LatLng(23.7527884,90.3665966));
            Route.add(new LatLng(23.7521992,90.3672833));
            Route.add(new LatLng(23.7514136,90.3678626));
            Route.add(new LatLng(23.7505494,90.3683991));
            Route.add(new LatLng(23.7505494,90.3683991));
            Route.add(new LatLng(23.7489782,90.369472));
            Route.add(new LatLng(23.7482122,90.3700299));
            Route.add(new LatLng(23.7474855,90.3705234));
            Route.add(new LatLng(23.7457375,90.3717036));
            Route.add(new LatLng(23.7446375,90.3723902));
            Route.add(new LatLng(23.7435966,90.3731412));
            Route.add(new LatLng(23.7425359,90.3738064));
            Route.add(new LatLng(23.741652,90.3742356));
            Route.add(new LatLng(23.7407878,90.3747076));
            Route.add(new LatLng(23.7398646,90.3751582));
            Route.add(new LatLng(23.7389415,90.3756732));
            Route.add(new LatLng(23.7383915,90.3760165));
            Route.add(new LatLng(23.73847,90.3770894));
            Route.add(new LatLng(23.7386075,90.3784198));
            Route.add(new LatLng(23.738804,90.3796429));
            Route.add(new LatLng(23.73902,90.3807372));
            Route.add(new LatLng(23.7392361,90.3817028));
            Route.add(new LatLng(23.7393539,90.382604));
            Route.add(new LatLng(23.7394914,90.3831834));
            Route.add(new LatLng(23.74067,90.3829045));
            Route.add(new LatLng(23.7407092,90.3831405));
            Route.add(new LatLng(23.7396879,90.3833336));
            Route.add(new LatLng(23.7384308,90.3835696));
            Route.add(new LatLng(23.7372522,90.3838271));
            Route.add(new LatLng(23.736388,90.3839988));
            Route.add(new LatLng(23.7347576,90.384385));
            Route.add(new LatLng(23.7336773,90.3846854));
            Route.add(new LatLng(23.7324594,90.3850288));
            Route.add(new LatLng(23.732479,90.3861231));
            Route.add(new LatLng(23.7326362,90.3871316));
            Route.add(new LatLng(23.7327933,90.3883976));
            Route.add(new LatLng(23.7329701,90.3897495));
            Route.add(new LatLng(23.7331862,90.3911657));
            Route.add(new LatLng(23.7331665,90.3926892));
            Route.add(new LatLng(23.733088,90.3937191));
            Route.add(new LatLng(23.7328719,90.3943629));
            Route.add(new LatLng(23.7323415,90.3949637));
            Route.add(new LatLng(23.7315951,90.3952426));
            Route.add(new LatLng(23.7307111,90.3953499));
            Route.add(new LatLng(23.7298076,90.3954143));
            Route.add(new LatLng(23.7292968,90.3954572));
            Route.add(new LatLng(23.7288843,90.3960151));
            Route.add(new LatLng(23.7281771,90.3966803));
            Route.add(new LatLng(23.7276075,90.3972596));
            Route.add(new LatLng(23.7272539,90.397603));
            Route.add(new LatLng(23.72747,90.3983325));
            Route.add(new LatLng(23.727686,90.3990621));
            Route.add(new LatLng(23.7279218,90.4000491));
            Route.add(new LatLng(23.7278039,90.4003925));
            Route.add(new LatLng(23.7272146,90.4004568));
        }
        //endregion
        //region KhonikaUp
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

        }
        //endregion
        //region BoishakhiUp
        else if(busName.equals(" Boishakhi"))
        {
            Route.add(new LatLng(23.72791750,90.4000751));
            Route.add(new LatLng(23.72728890,90.3975645));
            Route.add(new LatLng(23.72819250,90.3966848));
            Route.add(new LatLng(23.7291550,90.3957084));
            Route.add(new LatLng(23.72939080,90.3953973));
            Route.add(new LatLng(23.73034350,90.3953866));
            Route.add(new LatLng(23.73107030,90.3952578));
            Route.add(new LatLng(23.73159080,90.395172));
            Route.add(new LatLng(23.73212120,90.3950432));
            Route.add(new LatLng(23.73247480,90.3948501));
            Route.add(new LatLng(23.73271050,90.394539));
            Route.add(new LatLng(23.73292660,90.3942064));
            Route.add(new LatLng(23.73306410,90.3937772));
            Route.add(new LatLng(23.73304440,90.3931013));
            Route.add(new LatLng(23.73309350,90.3925649));
            Route.add(new LatLng(23.73316230,90.392125));
            Route.add(new LatLng(23.73318190,90.3918782));
            Route.add(new LatLng(23.73316230,90.3915027));
            Route.add(new LatLng(23.73305910,90.3907946));
            Route.add(new LatLng(23.73316230,90.3907839));
            Route.add(new LatLng(23.73326050,90.3914437));
            Route.add(new LatLng(23.73327030,90.3920714));
            Route.add(new LatLng(23.7332310,90.3924737));
            Route.add(new LatLng(23.7331770,90.392935));
            Route.add(new LatLng(23.73315740,90.3935412));
            Route.add(new LatLng(23.73315250,90.3939221));
            Route.add(new LatLng(23.73299530,90.394362));
            Route.add(new LatLng(23.73283330,90.3947428));
            Route.add(new LatLng(23.7327350,90.3950915));
            Route.add(new LatLng(23.73270070,90.3954295));
            Route.add(new LatLng(23.73282340,90.3955368));
            Route.add(new LatLng(23.73311320,90.3955046));
            Route.add(new LatLng(23.73357970,90.3954456));
            Route.add(new LatLng(23.73415420,90.3954027));
            Route.add(new LatLng(23.73457660,90.3953866));
            Route.add(new LatLng(23.73496940,90.3953973));
            Route.add(new LatLng(23.73548010,90.3954027));
            Route.add(new LatLng(23.7358190,90.395408));
            Route.add(new LatLng(23.73632480,90.3954617));
            Route.add(new LatLng(23.73711540,90.3955797));
            Route.add(new LatLng(23.7377930,90.3957031));
            Route.add(new LatLng(23.73852960,90.3958479));
            Route.add(new LatLng(23.73932510,90.3958747));
            Route.add(new LatLng(23.73992420,90.3959016));
            Route.add(new LatLng(23.74074920,90.3959445));
            Route.add(new LatLng(23.74118620,90.395923));
            Route.add(new LatLng(23.74168210,90.3958425));
            Route.add(new LatLng(23.7422370,90.3957299));
            Route.add(new LatLng(23.74281640,90.3956065));
            Route.add(new LatLng(23.74351860,90.395349));
            Route.add(new LatLng(23.74423060,90.3951076));
            Route.add(new LatLng(23.74478550,90.3949038));
            Route.add(new LatLng(23.74524710,90.3947858));
            Route.add(new LatLng(23.74570370,90.3946677));
            Route.add(new LatLng(23.7461260,90.3945229));
            Route.add(new LatLng(23.74687240,90.3942654));
            Route.add(new LatLng(23.74726520,90.3941313));
            Route.add(new LatLng(23.74846330,90.393729));
            Route.add(new LatLng(23.74897390,90.3935573));
            Route.add(new LatLng(23.74966130,90.3932408));
            Route.add(new LatLng(23.74969080,90.393155));
            Route.add(new LatLng(23.74970550,90.3930316));
            Route.add(new LatLng(23.74978410,90.3929404));
            Route.add(new LatLng(23.74986270,90.3929243));
            Route.add(new LatLng(23.74996580,90.3929136));
            Route.add(new LatLng(23.75003450,90.3929565));
            Route.add(new LatLng(23.75009830,90.3930423));
            Route.add(new LatLng(23.75023090,90.3930638));
            Route.add(new LatLng(23.75039290,90.393037));
            Route.add(new LatLng(23.75058440,90.3929672));
            Route.add(new LatLng(23.75122280,90.3927365));
            Route.add(new LatLng(23.75181690,90.3925273));
            Route.add(new LatLng(23.75219490,90.3923771));
            Route.add(new LatLng(23.75280380,90.3921089));
            Route.add(new LatLng(23.75321620,90.3919212));
            Route.add(new LatLng(23.75385940,90.3916529));
            Route.add(new LatLng(23.75439460,90.3914652));
            Route.add(new LatLng(23.75506730,90.3911755));
            Route.add(new LatLng(23.75560250,90.3909663));
            Route.add(new LatLng(23.7562260,90.3907142));
            Route.add(new LatLng(23.75695270,90.3904513));
            Route.add(new LatLng(23.75744860,90.3902743));
            Route.add(new LatLng(23.75804270,90.3900382));
            Route.add(new LatLng(23.75850420,90.3898773));
            Route.add(new LatLng(23.75844530,90.3890458));
            Route.add(new LatLng(23.75833720,90.387297));
            Route.add(new LatLng(23.75827830,90.3862778));
            Route.add(new LatLng(23.75820960,90.3855804));
            Route.add(new LatLng(23.75814090,90.3849796));
            Route.add(new LatLng(23.7581310,90.3844968));
            Route.add(new LatLng(23.75812610,90.3839067));
            Route.add(new LatLng(23.7582390,90.3836492));
            Route.add(new LatLng(23.75832250,90.3835097));
            Route.add(new LatLng(23.75844530,90.38344));
            Route.add(new LatLng(23.75861220,90.38344));
            Route.add(new LatLng(23.758730,90.3835366));
            Route.add(new LatLng(23.75884790,90.3836278));
            Route.add(new LatLng(23.7591130,90.3836224));
            Route.add(new LatLng(23.76012930,90.3835688));
            Route.add(new LatLng(23.76057120,90.3835312));
            Route.add(new LatLng(23.76111620,90.3834937));
            Route.add(new LatLng(23.76165130,90.3834561));
            Route.add(new LatLng(23.76231410,90.3833971));
            Route.add(new LatLng(23.763080,90.3833381));
            Route.add(new LatLng(23.76367410,90.3833005));
            Route.add(new LatLng(23.76450870,90.3832415));
            Route.add(new LatLng(23.76505860,90.3831986));
            Route.add(new LatLng(23.76690460,90.3830068));
            Route.add(new LatLng(23.76875050,90.3827493));
            Route.add(new LatLng(23.77043930,90.3824274));
            Route.add(new LatLng(23.77214780,90.3819554));
            Route.add(new LatLng(23.77346350,90.3815477));
            Route.add(new LatLng(23.77709620,90.3804748));
            Route.add(new LatLng(23.77935440,90.3798096));
            Route.add(new LatLng(23.78353680,90.3785651));
            Route.add(new LatLng(23.78628570,90.3774922));
            Route.add(new LatLng(23.78885790,90.3763978));
            Route.add(new LatLng(23.79042860,90.3756897));
            Route.add(new LatLng(23.79270620,90.3746812));
            Route.add(new LatLng(23.79525860,90.3736513));
            Route.add(new LatLng(23.79724160,90.3727715));
            Route.add(new LatLng(23.79997060,90.3716342));
            Route.add(new LatLng(23.80218910,90.370733));
            Route.add(new LatLng(23.80478060,90.3695528));
            Route.add(new LatLng(23.80697930,90.3685873));
            Route.add(new LatLng(23.80711680,90.3683727));
            Route.add(new LatLng(23.80729350,90.368716));
            Route.add(new LatLng(23.80725420,90.369467));
            Route.add(new LatLng(23.80655210,90.3736682));
            Route.add(new LatLng(23.80606580,90.3747738));
            Route.add(new LatLng(23.80506460,90.3761471));
            Route.add(new LatLng(23.80380810,90.3775847));
            Route.add(new LatLng(23.8025320,90.3791511));
            Route.add(new LatLng(23.80113810,90.3809321));
            Route.add(new LatLng(23.79999940,90.383035));
            Route.add(new LatLng(23.79937110,90.3847087));
            Route.add(new LatLng(23.79860540,90.386597));
            Route.add(new LatLng(23.79823240,90.3883565));

        }
        //endregion


    }

    private void creatRouteDown() {

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
            Route.add(new LatLng(23.7272146,90.4004568));
            Route.add(new LatLng(23.7278039,90.4003925));
            Route.add(new LatLng(23.7279218,90.4000491));
            Route.add(new LatLng(23.727686,90.3990621));
            Route.add(new LatLng(23.72747,90.3983325));
            Route.add(new LatLng(23.7272539,90.397603));
            Route.add(new LatLng(23.7276075,90.3972596));
            Route.add(new LatLng(23.7281771,90.3966803));
            Route.add(new LatLng(23.7288843,90.3960151));
            Route.add(new LatLng(23.7292968,90.3954572));
            Route.add(new LatLng(23.7298076,90.3954143));
            Route.add(new LatLng(23.7307111,90.3953499));
            Route.add(new LatLng(23.7315951,90.3952426));
            Route.add(new LatLng(23.7323415,90.3949637));
            Route.add(new LatLng(23.7328719,90.3943629));
            Route.add(new LatLng(23.733088,90.3937191));
            Route.add(new LatLng(23.7331665,90.3926892));
            Route.add(new LatLng(23.7331862,90.3911657));
            Route.add(new LatLng(23.7329701,90.3897495));
            Route.add(new LatLng(23.7327933,90.3883976));
            Route.add(new LatLng(23.7326362,90.3871316));
            Route.add(new LatLng(23.732479,90.3861231));
            Route.add(new LatLng(23.7324594,90.3850288));
            Route.add(new LatLng(23.7336773,90.3846854));
            Route.add(new LatLng(23.7347576,90.384385));
            Route.add(new LatLng(23.736388,90.3839988));
            Route.add(new LatLng(23.7372522,90.3838271));
            Route.add(new LatLng(23.7384308,90.3835696));
            Route.add(new LatLng(23.7396879,90.3833336));
            Route.add(new LatLng(23.7407092,90.3831405));
            Route.add(new LatLng(23.74067,90.3829045));
            Route.add(new LatLng(23.7394914,90.3831834));
            Route.add(new LatLng(23.7393539,90.382604));
            Route.add(new LatLng(23.7392361,90.3817028));
            Route.add(new LatLng(23.73902,90.3807372));
            Route.add(new LatLng(23.738804,90.3796429));
            Route.add(new LatLng(23.7386075,90.3784198));
            Route.add(new LatLng(23.73847,90.3770894));
            Route.add(new LatLng(23.7383915,90.3760165));
            Route.add(new LatLng(23.7389415,90.3756732));
            Route.add(new LatLng(23.7398646,90.3751582));
            Route.add(new LatLng(23.7407878,90.3747076));
            Route.add(new LatLng(23.741652,90.3742356));
            Route.add(new LatLng(23.7425359,90.3738064));
            Route.add(new LatLng(23.7435966,90.3731412));
            Route.add(new LatLng(23.7446375,90.3723902));
            Route.add(new LatLng(23.7457375,90.3717036));
            Route.add(new LatLng(23.7474855,90.3705234));
            Route.add(new LatLng(23.7482122,90.3700299));
            Route.add(new LatLng(23.7489782,90.369472));
            Route.add(new LatLng(23.7505494,90.3683991));
            Route.add(new LatLng(23.7505494,90.3683991));
            Route.add(new LatLng(23.7514136,90.3678626));
            Route.add(new LatLng(23.7521992,90.3672833));
            Route.add(new LatLng(23.7527884,90.3665966));
            Route.add(new LatLng(23.7535544,90.3656525));
            Route.add(new LatLng(23.7544185,90.3646869));
            Route.add(new LatLng(23.7553612,90.363614));
            Route.add(new LatLng(23.7559504,90.3627772));
            Route.add(new LatLng(23.757011,90.3616399));
        }
        //endregion
        //region KhonikaDown
        else if(busName.equals(" Khonika"))
        {
            Route.add(new LatLng(23.9088517,    90.3979984));
            Route.add(new LatLng(23.907253,    90.398567));
            Route.add(new LatLng(23.9053502,   90.3990606));
            Route.add(new LatLng(23.9037906,   90.3992322));
            Route.add(new LatLng(23.9017602,   90.3990498));
            Route.add(new LatLng(23.89869,   90.3988782));
            Route.add(new LatLng(23.8970519,   90.3989747));
            Route.add(new LatLng(23.8966105,   90.3991571));
            Route.add(new LatLng(23.8955609,   90.3997579));
            Route.add(new LatLng(23.8949037,   90.4002836));
            Route.add(new LatLng(23.8939718,   90.4011312));
            Route.add(new LatLng(23.8931478,   90.4017857));
            Route.add(new LatLng(23.8927457,   90.4019359));
            Route.add(new LatLng(23.8921865,   90.4019788));
            Route.add(new LatLng(23.8892633,   90.4013136));
            Route.add(new LatLng(23.8882627,   90.400981));
            Route.add(new LatLng(23.8872424,  90.4005841));
            Route.add(new LatLng(23.8863399,  90.4002622));
            Route.add(new LatLng(23.8854472,  90.4002515));
            Route.add(new LatLng(23.8841915,  90.4004446));
            Route.add(new LatLng(23.882926,  90.4006699));
            Route.add(new LatLng(23.8809836, 90.4009917));
            Route.add(new LatLng(23.8794532, 90.4012385));
            Route.add(new LatLng(23.8784525, 90.4013029));
            Route.add(new LatLng(23.8775401, 90.4011205));
            Route.add(new LatLng(23.8760784, 90.4007128));
            Route.add(new LatLng(23.875058, 90.4004553));
            Route.add(new LatLng(23.8738513, 90.4006162));
            Route.add(new LatLng(23.8715163, 90.4004982));
            Route.add(new LatLng(23.8679352, 90.4002622));
            Route.add(new LatLng(23.8662967, 90.4001227));
            Route.add(new LatLng(23.8638438, 90.3999189));
            Route.add(new LatLng(23.8622445, 90.399876));
            Route.add(new LatLng(23.8607041, 90.4001978));
            Route.add(new LatLng(23.858624, 90.4016891));
            Route.add(new LatLng(23.8563181, 90.403674));
            Route.add(new LatLng(23.8518535, 90.4073754));
            Route.add(new LatLng(23.8506956, 90.4083303));
            Route.add(new LatLng(23.8505484, 90.4082659));
            Route.add(new LatLng(23.8503325, 90.4083088));
            Route.add(new LatLng(23.8502835, 90.4085449));
            Route.add(new LatLng(23.8496358, 90.4091242));
            Route.add(new LatLng(23.8458186, 90.4123858));
            Route.add(new LatLng(23.8452986, 90.412772));
            Route.add(new LatLng(23.8425312, 90.4151431));
            Route.add(new LatLng(23.8407158, 90.4165915));
            Route.add(new LatLng(23.8392536,90.4175463));
            Route.add(new LatLng(23.8380269, 90.4181364));
            Route.add(new LatLng(23.8357501, 90.4188123));
            Route.add(new LatLng(23.8311965, 90.4195312));
            Route.add(new LatLng(23.8281541, 90.4200569));
            Route.add(new LatLng(23.8257299, 90.420486));
            Route.add(new LatLng(23.8244344, 90.4203788));
            Route.add(new LatLng(23.8230603, 90.4200032));
            Route.add(new LatLng(23.8220298, 90.4193488));
            Route.add(new LatLng(23.8212446, 90.4184368));
            Route.add(new LatLng(23.8200177, 90.4167739));
            Route.add(new LatLng(23.8185946, 90.4145208));
            Route.add(new LatLng(23.8177603, 90.4132548));
            Route.add(new LatLng(23.8169358, 90.4119244));
            Route.add(new LatLng(23.8168377, 90.4110339));
            Route.add(new LatLng(23.8169653, 90.4076651));
            Route.add(new LatLng(23.8168671, 90.4061952));
            Route.add(new LatLng(23.816445, 90.4054657));
            Route.add(new LatLng(23.8158659, 90.4048756));
            Route.add(new LatLng(23.8148942, 90.4043499));
            Route.add(new LatLng(23.812676, 90.404028));
            Route.add(new LatLng(23.8092896, 90.4034057));
            Route.add(new LatLng(23.8068454, 90.4030302));
            Route.add(new LatLng(23.8022907,90.402247 ));
            Route.add(new LatLng(23.7975788, 90.4014316));
            Route.add(new LatLng(23.7941822, 90.4009059));
            Route.add(new LatLng(23.7905205,90.4002729 ));
            Route.add(new LatLng(23.7880858, 90.3998545));
            Route.add(new LatLng(23.78302, 90.3990069));
            Route.add(new LatLng(23.7800059, 90.3985563));
            Route.add(new LatLng(23.7787884, 90.3982774));
            Route.add(new LatLng(23.7783368, 90.3978911));
            Route.add(new LatLng(23.7777772, 90.3966788));
            Route.add(new LatLng(23.7762259, 90.3938249));
            Route.add(new LatLng(23.7756957, 90.3927413));
            Route.add(new LatLng(23.7754699, 90.3912392));
            Route.add(new LatLng(23.775303, 90.3898552));
            Route.add(new LatLng(23.7731822, 90.390102));
            Route.add(new LatLng(23.7724066, 90.3899625));
            Route.add(new LatLng(23.7691566, 90.3893295));
            Route.add(new LatLng(23.7677623, 90.3890935));
            Route.add(new LatLng(23.7655138, 90.3889218));
            Route.add(new LatLng(23.7634419, 90.3890398));
            Route.add(new LatLng(23.7617726, 90.3892329));
            Route.add(new LatLng(23.7602408, 90.3894475));
            Route.add(new LatLng(23.7586206, 90.3899089));
            Route.add(new LatLng(23.7572753, 90.3903809));
            Route.add(new LatLng(23.7545258, 90.3914753));
            Route.add(new LatLng(23.7525127, 90.3922585));
            Route.add(new LatLng(23.7504014, 90.3930846));
            Route.add(new LatLng(23.7487768,90.3936465));
            Route.add(new LatLng(23.745596,90.3947172));
            Route.add(new LatLng(23.744614,90.3950391));
            Route.add(new LatLng(23.7433569,90.3954468));
            Route.add(new LatLng(23.7424338,90.3957257));
            Route.add(new LatLng(23.7411963,90.3959832));
            Route.add(new LatLng(23.7399,90.3959618));
            Route.add(new LatLng(23.7384858,90.3958545));
            Route.add(new LatLng(23.7371894,90.3956184));
            Route.add(new LatLng(23.735559,90.3954897));
            Route.add(new LatLng(23.7341251,90.3954682));
            Route.add(new LatLng(23.7329072,90.395597));
            Route.add(new LatLng(23.7326715,90.3954682));
            Route.add(new LatLng(23.7328876,90.3944597));
            Route.add(new LatLng(23.7324751,90.3948674));
            Route.add(new LatLng(23.7320822,90.3951249));
            Route.add(new LatLng(23.7315126,90.3952322));
            Route.add(new LatLng(23.7305501,90.3953609));
            Route.add(new LatLng(23.7293715,90.3953824));
            Route.add(new LatLng(23.7272892,90.3975496));
            Route.add(new LatLng(23.7279178,90.4000816));
            Route.add(new LatLng(23.727741,90.400382));



        }
        //endregion
        //region BoishakhiDown
        else if(busName.equals(" Boishakhi"))
        {
            Route.add(new LatLng(23.79823240,90.3883565));
            Route.add(new LatLng(23.79860540,90.386597));
            Route.add(new LatLng(23.79937110,90.3847087));
            Route.add(new LatLng(23.79999940,90.383035));
            Route.add(new LatLng(23.80113810,90.3809321));
            Route.add(new LatLng(23.8025320,90.3791511));
            Route.add(new LatLng(23.80380810,90.3775847));
            Route.add(new LatLng(23.80506460,90.3761471));
            Route.add(new LatLng(23.80606580,90.3747738));
            Route.add(new LatLng(23.80655210,90.3736682));
            Route.add(new LatLng(23.80725420,90.369467));
            Route.add(new LatLng(23.80729350,90.368716));
            Route.add(new LatLng(23.80711680,90.3683727));
            Route.add(new LatLng(23.80697930,90.3685873));
            Route.add(new LatLng(23.80478060,90.3695528));
            Route.add(new LatLng(23.80218910,90.370733));
            Route.add(new LatLng(23.79997060,90.3716342));
            Route.add(new LatLng(23.79724160,90.3727715));
            Route.add(new LatLng(23.79525860,90.3736513));
            Route.add(new LatLng(23.79270620,90.3746812));
            Route.add(new LatLng(23.79042860,90.3756897));
            Route.add(new LatLng(23.78885790,90.3763978));
            Route.add(new LatLng(23.78628570,90.3774922));
            Route.add(new LatLng(23.78353680,90.3785651));
            Route.add(new LatLng(23.77935440,90.3798096));
            Route.add(new LatLng(23.77709620,90.3804748));
            Route.add(new LatLng(23.77346350,90.3815477));
            Route.add(new LatLng(23.77214780,90.3819554));
            Route.add(new LatLng(23.77043930,90.3824274));
            Route.add(new LatLng(23.76875050,90.3827493));
            Route.add(new LatLng(23.76690460,90.3830068));
            Route.add(new LatLng(23.76505860,90.3831986));
            Route.add(new LatLng(23.76450870,90.3832415));
            Route.add(new LatLng(23.76367410,90.3833005));
            Route.add(new LatLng(23.763080,90.3833381));
            Route.add(new LatLng(23.76231410,90.3833971));
            Route.add(new LatLng(23.76165130,90.3834561));
            Route.add(new LatLng(23.76111620,90.3834937));
            Route.add(new LatLng(23.76057120,90.3835312));
            Route.add(new LatLng(23.76012930,90.3835688));
            Route.add(new LatLng(23.7591130,90.3836224));
            Route.add(new LatLng(23.75884790,90.3836278));
            Route.add(new LatLng(23.758730,90.3835366));
            Route.add(new LatLng(23.75861220,90.38344));
            Route.add(new LatLng(23.75844530,90.38344));
            Route.add(new LatLng(23.75832250,90.3835097));
            Route.add(new LatLng(23.7582390,90.3836492));
            Route.add(new LatLng(23.75812610,90.3839067));
            Route.add(new LatLng(23.7581310,90.3844968));
            Route.add(new LatLng(23.75814090,90.3849796));
            Route.add(new LatLng(23.75820960,90.3855804));
            Route.add(new LatLng(23.75827830,90.3862778));
            Route.add(new LatLng(23.75833720,90.387297));
            Route.add(new LatLng(23.75844530,90.3890458));
            Route.add(new LatLng(23.75850420,90.3898773));
            Route.add(new LatLng(23.75804270,90.3900382));
            Route.add(new LatLng(23.75744860,90.3902743));
            Route.add(new LatLng(23.75695270,90.3904513));
            Route.add(new LatLng(23.7562260,90.3907142));
            Route.add(new LatLng(23.75560250,90.3909663));
            Route.add(new LatLng(23.75506730,90.3911755));
            Route.add(new LatLng(23.75439460,90.3914652));
            Route.add(new LatLng(23.75385940,90.3916529));
            Route.add(new LatLng(23.75321620,90.3919212));
            Route.add(new LatLng(23.75280380,90.3921089));
            Route.add(new LatLng(23.75219490,90.3923771));
            Route.add(new LatLng(23.75181690,90.3925273));
            Route.add(new LatLng(23.75122280,90.3927365));
            Route.add(new LatLng(23.75058440,90.3929672));
            Route.add(new LatLng(23.75039290,90.393037));
            Route.add(new LatLng(23.75023090,90.3930638));
            Route.add(new LatLng(23.75009830,90.3930423));
            Route.add(new LatLng(23.75003450,90.3929565));
            Route.add(new LatLng(23.74996580,90.3929136));
            Route.add(new LatLng(23.74986270,90.3929243));
            Route.add(new LatLng(23.74978410,90.3929404));
            Route.add(new LatLng(23.74970550,90.3930316));
            Route.add(new LatLng(23.74969080,90.393155));
            Route.add(new LatLng(23.74966130,90.3932408));
            Route.add(new LatLng(23.74897390,90.3935573));
            Route.add(new LatLng(23.74846330,90.393729));
            Route.add(new LatLng(23.74726520,90.3941313));
            Route.add(new LatLng(23.74687240,90.3942654));
            Route.add(new LatLng(23.7461260,90.3945229));
            Route.add(new LatLng(23.74570370,90.3946677));
            Route.add(new LatLng(23.74524710,90.3947858));
            Route.add(new LatLng(23.74478550,90.3949038));
            Route.add(new LatLng(23.74423060,90.3951076));
            Route.add(new LatLng(23.74351860,90.395349));
            Route.add(new LatLng(23.74281640,90.3956065));
            Route.add(new LatLng(23.7422370,90.3957299));
            Route.add(new LatLng(23.74168210,90.3958425));
            Route.add(new LatLng(23.74118620,90.395923));
            Route.add(new LatLng(23.74074920,90.3959445));
            Route.add(new LatLng(23.73992420,90.3959016));
            Route.add(new LatLng(23.73932510,90.3958747));
            Route.add(new LatLng(23.73852960,90.3958479));
            Route.add(new LatLng(23.7377930,90.3957031));
            Route.add(new LatLng(23.73711540,90.3955797));
            Route.add(new LatLng(23.73632480,90.3954617));
            Route.add(new LatLng(23.7358190,90.395408));
            Route.add(new LatLng(23.73548010,90.3954027));
            Route.add(new LatLng(23.73496940,90.3953973));
            Route.add(new LatLng(23.73457660,90.3953866));
            Route.add(new LatLng(23.73415420,90.3954027));
            Route.add(new LatLng(23.73357970,90.3954456));
            Route.add(new LatLng(23.73311320,90.3955046));
            Route.add(new LatLng(23.73282340,90.3955368));
            Route.add(new LatLng(23.73270070,90.3954295));
            Route.add(new LatLng(23.7327350,90.3950915));
            Route.add(new LatLng(23.73283330,90.3947428));
            Route.add(new LatLng(23.73299530,90.394362));
            Route.add(new LatLng(23.73315250,90.3939221));
            Route.add(new LatLng(23.73315740,90.3935412));
            Route.add(new LatLng(23.7331770,90.392935));
            Route.add(new LatLng(23.7332310,90.3924737));
            Route.add(new LatLng(23.73327030,90.3920714));
            Route.add(new LatLng(23.73326050,90.3914437));
            Route.add(new LatLng(23.73316230,90.3907839));
            Route.add(new LatLng(23.73305910,90.3907946));
            Route.add(new LatLng(23.73316230,90.3915027));
            Route.add(new LatLng(23.73318190,90.3918782));
            Route.add(new LatLng(23.73316230,90.392125));
            Route.add(new LatLng(23.73309350,90.3925649));
            Route.add(new LatLng(23.73304440,90.3931013));
            Route.add(new LatLng(23.73306410,90.3937772));
            Route.add(new LatLng(23.73292660,90.3942064));
            Route.add(new LatLng(23.73271050,90.394539));
            Route.add(new LatLng(23.73247480,90.3948501));
            Route.add(new LatLng(23.73212120,90.3950432));
            Route.add(new LatLng(23.73159080,90.395172));
            Route.add(new LatLng(23.73107030,90.3952578));
            Route.add(new LatLng(23.73034350,90.3953866));
            Route.add(new LatLng(23.72939080,90.3953973));
            Route.add(new LatLng(23.7291550,90.3957084));
            Route.add(new LatLng(23.72819250,90.3966848));
            Route.add(new LatLng(23.72728890,90.3975645));
            Route.add(new LatLng(23.72791750,90.4000751));

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
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.738287, 90.372810), 15f));

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
            creatRouteDown();
            if(MapAnimator.foregroundRouteAnimator.isRunning())MapAnimator.foregroundRouteAnimator.cancel();
            startAnim();
            fab.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.up));

            this.setTitle(busName+ " Down Trip");
        }
        else
        {

            createRouteUp();
            if(MapAnimator.foregroundRouteAnimator.isRunning())MapAnimator.foregroundRouteAnimator.cancel();

            startAnim();
            fab.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.down));

            this.setTitle(busName+ " Up Trip");
        }
    }


}
