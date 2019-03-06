package com.example.asus.remindmemyself;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class DogsDropdownOnItemClickListener implements OnItemClickListener {

      //public static String BusTime="7:00 up";

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {

        int id= v.getId();
        // get the context and main activity to access variables
        Context mContext = v.getContext();
        firstActivity mainActivity = ((firstActivity) mContext);

        // add some animation when a list item was clicked
        Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
        fadeInAnimation.setDuration(10);
        v.startAnimation(fadeInAnimation);

        // dismiss the pop up
        mainActivity.popupWindowDogs.dismiss();

        // get the text and set it as the button text
        String selectedItemText = ((TextView) v).getText().toString();
        //setBusName(selectedItemText);
        if(selectedItemText.contains(":"))
        {
            firstActivity.popupWindow.dismiss();
            mainActivity.buttonTime.setText(selectedItemText);
            GlobalClass.BusTime=selectedItemText;
            Log.d("problem1",selectedItemText);
        }

        else
        {
            firstActivity.popupWindow.dismiss();
            GlobalClass.BusName=selectedItemText;
            Log.d("problem1",selectedItemText);
            mainActivity.buttonBus.setText(selectedItemText);


        }

    }
//
//    public String getBusName() {
//        return BusName;
//    }
//
//    public void setBusName(String busName) {
//        BusName = busName;
//    }
}