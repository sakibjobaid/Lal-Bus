package com.anything.asus.remindmemyself;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by amal.chandran on 22/12/16.
 */

public class MapAnimator {



    private static MapAnimator mapAnimator;
    private Polyline backgroundPolyline,foregroundPolyline;
    private PolylineOptions optionsForeground,optionsBackground;
    private AnimatorSet firstRunAnimSet;
    private AnimatorSet secondLoopRunAnimSet;
    public static ObjectAnimator foregroundRouteAnimator;
    static final int GREY = Color.parseColor("#ce0c0c");


    private MapAnimator(){

    }

    public static MapAnimator getInstance(){
        if(mapAnimator == null) mapAnimator = new MapAnimator();
        return mapAnimator;
    }


    public void animateRoute(GoogleMap googleMap, List<LatLng> bangaloreRoute) {


        if (firstRunAnimSet == null){
            firstRunAnimSet = new AnimatorSet();
        } else {
            firstRunAnimSet.removeAllListeners();
            firstRunAnimSet.end();
            firstRunAnimSet.cancel();

            firstRunAnimSet = new AnimatorSet();
        }
        if (secondLoopRunAnimSet == null) {
            secondLoopRunAnimSet = new AnimatorSet();
        } else {
            secondLoopRunAnimSet.removeAllListeners();
            secondLoopRunAnimSet.end();
            secondLoopRunAnimSet.cancel();

            secondLoopRunAnimSet = new AnimatorSet();
        }
        //Reset the polylines
        if (foregroundPolyline != null) foregroundPolyline.remove();
        if (backgroundPolyline != null) backgroundPolyline.remove();



        optionsForeground = new PolylineOptions().add(bangaloreRoute.get(0)).color(Color.BLUE).width(8);
        foregroundPolyline = googleMap.addPolyline(optionsForeground);

        foregroundRouteAnimator = ObjectAnimator.ofObject(this, "routeIncreaseForward", new RouteEvaluator(), bangaloreRoute.toArray());
        foregroundRouteAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        foregroundRouteAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        foregroundRouteAnimator.setDuration(5000); ///background color time
        foregroundRouteAnimator.start();

        firstRunAnimSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                secondLoopRunAnimSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }
    // This is an handy method to call if you want to remove the polyline because of some condition like back press
    void stopAndRemovePolyLine(){


        if(mapAnimator!=null) {
            backgroundPolyline.remove();
            foregroundPolyline.remove();
            firstRunAnimSet.cancel();
            secondLoopRunAnimSet.cancel();
        }
    }

    /**
     * This will be invoked by the ObjectAnimator multiple times. Mostly every 16ms.
     **/
    public void setRouteIncreaseForward(LatLng endLatLng) {

        MapsActivity.getmMap().moveCamera(CameraUpdateFactory.newLatLngZoom(endLatLng,15f));
        List<LatLng> foregroundPoints = foregroundPolyline.getPoints();




        foregroundPoints.add(endLatLng);
        foregroundPolyline.setPoints(foregroundPoints);
    }

}
