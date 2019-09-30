package com.anything.asus.remindmemyself;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;

public class BusAndTime {

    SharedPreferences time_pref;
    DatabaseReference timeReference;
    String name;
    String def[] = null;
    Context context;
    ValueEventListener val_time=null;
    BusAndTime(String  name,Context context)
    {
        this.name=name;
        this.context=context;
        timeReference= FirebaseDatabase.getInstance().getReference()
                .child("BUS_SCHEDULE");
        timeReference.keepSynced(true);

        val_time= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(GlobalClass.listTaranga.size()!=0)
                    GlobalClass.listTaranga.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GlobalClass.listTaranga.add(snapshot.getKey().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        timeReference.child(name).addValueEventListener(val_time);
    }

    public ArrayList<String> getTime() {

           timeReference.removeEventListener(val_time);


            String s;
            time_pref = context.getSharedPreferences("hellotime", MODE_PRIVATE);
            s= time_pref.getString(name, "");

            ArrayList<String> timeList= new ArrayList<>();

            StringTokenizer stx = new StringTokenizer(s,"$");
            while(stx.hasMoreElements())
            {
                timeList.add((String) stx.nextElement());
            }

            return timeList;

    }
}