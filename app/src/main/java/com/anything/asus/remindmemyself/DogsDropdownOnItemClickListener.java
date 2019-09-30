package com.anything.asus.remindmemyself;


import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import java.util.ArrayList;

public class DogsDropdownOnItemClickListener implements OnItemClickListener {


    Context mContext;

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {

        int id= v.getId();

        // get the context and main activity to access variables
        mContext = v.getContext();
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
            GlobalClass.BoxBusTime=selectedItemText;

        }

        else
        {
            firstActivity.popupWindow.dismiss();
            GlobalClass.BoxBusName=selectedItemText;
            mainActivity.buttonBus.setText(selectedItemText);



            BusAndTime b= new BusAndTime(selectedItemText,mContext);
            ArrayList<String> time= new ArrayList<>();
            time= b.getTime();
            mainActivity.buttonTime.setText("TIME");

        }

    }

}