package com.example.asus.remindmemyself;

import android.app.Application;

public class GlobalClass extends Application {

    public static double lat=-1,currentlat=-1;
    public static double lon=-1,currentlon=-1;
    public static int ct=0,pp=0,waiting=0,first=0;
    public static boolean selfdestroy=false,signal=false;
    public static String BusName = "Taranga",BusTime="7:00 up",PreBusName="Taranga",preBusTime="7:00 up";
}

