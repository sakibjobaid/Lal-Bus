package com.anything.asus.remindmemyself;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlobalClass extends Application {

    public static String useremail="asd",adminName="",adminDepartment=""
            ,adminselectedRoute="",adminselectedStoppage="",imgString="";
    public static Marker marker1;
    public static String routeDir="up",routeNumber="Route1";
    public static Marker[] markerArray;
    public static Uri profilePicUri=null;
    public static Bitmap bitMap=null;

    public static int childcount=0,up=1,down=0,countM=0;
    public static Circle geoFenceLimits;
    public static double lat=-1,currentlat=-1;
    public static double lon=-1,currentlon=-1;
    public static LatLng centreLoc=null,busLoc=null;
    public static List<LatLng> cRoute= new ArrayList<>();
    public static List<String> cStoppage= new ArrayList<>();
    public static int ct=0,pp=0,waiting=0,first=0,progress=1,locAlarm=0,
            busAlarm=0,busradius=0,locradius=0,mkcount=0,hiding=0,finalBusRadius=0;
    public static boolean selfdestroy=false,signal=false,title=false,animation=false,userModeFetchData=true,
            first_on_force=true,p=true,edit=true,first_on_status=true,cholbe=true,adminBackekbar=true,
            ekbarOff=true,menu_hiding_for_route=false,forcedKickedOut=false,adminHoise=false,
            ekbar_network_alarm=true,adminFeedbackHideInAdminMode=false;
    public static String BoxBusName = "BUS",BoxBusTime="TIME",BusName,BusTime,PreBusName="asdf",
            preBusTime="sdfs",tempBusName,tempBustime,timeStampDate,timeStampTime;

    public static boolean pic_show=false,mail_show=false,contact_show=false,
            fb_show=false,history_ekbar_update=true,ekbartime=false;

    public static long clock=0,timestamp_time=0;
    //public static long timestamp_time=0;

    public static List<String> listTaranga = new ArrayList<String>();
    public  static ArrayList<String>BusNameKey= new ArrayList<>();

    public static HashMap<String, String> markerName = new HashMap<String, String>();
    public static HashMap<String, String> markerDescription = new HashMap<String, String>();
    public static HashMap<String, LatLng> markerLatlng = new HashMap<String,LatLng >();
    public static HashMap<Integer, String> markerSerial = new HashMap<Integer, String>();

    public static HashMap<String, String> umarkerName = new HashMap<String, String>();
    public static HashMap<String, String> umarkerDescription = new HashMap<String, String>();
    public static HashMap<String, LatLng> umarkerLatlng = new HashMap<String,LatLng >();
    public static HashMap<Integer, String> umarkerSerial = new HashMap<Integer, String>();

    public static boolean bustouch=true,timetouch=false;
    public static HashMap<String, List<String>> activebus = new HashMap<String, List<String>>();
    public static HashMap<String, String> activetime=new HashMap<>();


    public static HashMap<String, List<firstActivity.profile_class>> top10list = null;

    private static final String TAG = GlobalClass.class.getSimpleName();

    public static  boolean AdminVirgin=false,up_bool_from_admin_background=false,
            down_bool_from_admin_background=false,UserVirgin=false,admin_own_pic=false;

    public static SharedPreferences NamePref,MailPref,FbPref,DeptPref,TripPref,ContactPref,PicStringPref
            ,upTripPref,downTripPref,feedContactPref;
    public static SharedPreferences.Editor NameEditor,MailEditor,FbEditor,DeptEditor,TripEditor,ContactEditor,
    PicStringEditor,upTripEditor,downTripEditor;

    public static  SharedPreferences admin_radius_aage_pref,admin_aage_pref,
            admin_pore_pref,admin_radius_pore_pref;

    public static SharedPreferences admin_successful_up,admin_successful_down;




    @Override
    public void onCreate() {
        super.onCreate();

    }
}

