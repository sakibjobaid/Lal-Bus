package com.example.asus.remindmemyself;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalClass extends Application {

    public static String useremail="asd";
    public static Marker marker1;
    public static int childcount=0,up=1,down=0;
    public static Circle geoFenceLimits;
    public static double lat=-1,currentlat=-1;
    public static double lon=-1,currentlon=-1;
    public static LatLng centreLoc=null,busLoc=null;
    public static int ct=0,pp=0,waiting=0,first=0,progress=1,locAlarm=0,busAlarm=0,busradius=0,locradius=0;
    public static boolean selfdestroy=false,signal=false,title=false;
    public static String BusName = "Taranga",BusTime="7:00 up",PreBusName="asdf",preBusTime="sdfs";
    public static List<String> listTaranga = new ArrayList<String>();
    //public static List<String> listUllash = new ArrayList<String>();

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        final FirebaseRemoteConfig remoteConfig =FirebaseRemoteConfig.getInstance();

        Map<String,Object> defaultValue = new HashMap();
        defaultValue.put(ForceUpdateChecker.update,false);
        defaultValue.put(ForceUpdateChecker.version,"1.0");
        defaultValue.put(ForceUpdateChecker.update_url,"will be given");

        remoteConfig.setDefaults(defaultValue);
        remoteConfig.fetch(60).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                 if(task.isSuccessful())
                 {
                     remoteConfig.activate();
                 }
            }
        });
    }
}

