package com.example.asus.remindmemyself;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
        Log.d("jobaid","MapAnimator:getInstance");
        if(mapAnimator == null) mapAnimator = new MapAnimator();
        return mapAnimator;
    }


    public void animateRoute(GoogleMap googleMap, List<LatLng> bangaloreRoute) {

        Log.d("jobaid","MapAnimator:animateRoute");

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


//        optionsBackground = new PolylineOptions().add(bangaloreRoute.get(0)).color(GREY).width(15);
//        backgroundPolyline = googleMap.addPolyline(optionsBackground);

        optionsForeground = new PolylineOptions().add(bangaloreRoute.get(0)).color(Color.BLUE).width(8);
        foregroundPolyline = googleMap.addPolyline(optionsForeground);

        //region pore
//        final ValueAnimator percentageCompletion = ValueAnimator.ofInt(0, 100);
//        percentageCompletion.setDuration(1000);/// foreground coloring timing
//        percentageCompletion.setInterpolator(new DecelerateInterpolator());
//        percentageCompletion.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                List<LatLng> foregroundPoints = backgroundPolyline.getPoints();
//                Log.d("jobaid","updating");
//                int percentageValue = (int) animation.getAnimatedValue();
//                int pointcount = foregroundPoints.size();
//                int countTobeRemoved = (int) (pointcount * (percentageValue / 100.0f));
//                List<LatLng> subListTobeRemoved = foregroundPoints.subList(0, countTobeRemoved);
//                subListTobeRemoved.clear();
//
//                foregroundPolyline.setPoints(foregroundPoints);
//            }
//        });
//        percentageCompletion.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                foregroundPolyline.setColor(GREY);
//                foregroundPolyline.setPoints(backgroundPolyline.getPoints());
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
        //endregion


//        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), GREY, Color.BLACK);
//        colorAnimation.setInterpolator(new AccelerateInterpolator());
//        colorAnimation.setDuration(12000); // milliseconds

//        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
////            @Override
////            public void onAnimationUpdate(ValueAnimator animator) {
////                foregroundPolyline.setColor((int) animator.getAnimatedValue());
////            }
////
////        });

         foregroundRouteAnimator = ObjectAnimator.ofObject(this, "routeIncreaseForward", new RouteEvaluator(), bangaloreRoute.toArray());
        foregroundRouteAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        foregroundRouteAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //backgroundPolyline.setPoints(foregroundPolyline.getPoints());
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

//        firstRunAnimSet.playSequentially(foregroundRouteAnimator,
//                percentageCompletion);
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

//       secondLoopRunAnimSet.playSequentially(colorAnimation,
//               percentageCompletion);
        //secondLoopRunAnimSet.setStartDelay(20000);

//        secondLoopRunAnimSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                secondLoopRunAnimSet.start();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//
//        firstRunAnimSet.start();
    }
    // This is an handy method to call if you want to remove the polyline because of some condition like back press
    void stopAndRemovePolyLine(){

        Log.d("jobaid","MapAnimator:stopAndRemovePolyLine");

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
        Log.d("jobaid","MapAnimator:setRouteIncreaseForward");
        MapsActivity.getmMap().moveCamera(CameraUpdateFactory.newLatLngZoom(endLatLng,15f));
        List<LatLng> foregroundPoints = foregroundPolyline.getPoints();
       // Log.d("same",String.valueOf(endLatLng.latitude));

        if(endLatLng.latitude==23.75683891706815||endLatLng.latitude==23.739460763694638
                ||endLatLng.latitude==23.73052691449622)
        {
            MapsActivity.getmMap().addMarker(new MarkerOptions().position(endLatLng)
                    .title("Zigatala")
                    .snippet("Ecstasy")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        }


        foregroundPoints.add(endLatLng);
        foregroundPolyline.setPoints(foregroundPoints);
    }

}
