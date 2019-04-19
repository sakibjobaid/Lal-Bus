package com.example.asus.remindmemyself;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;

public class BusAndTime {

    SharedPreferences mPreferences;
    DatabaseReference firebaseDatabase;
    String name;
    String def[] = null;
    Context context;
    BusAndTime(String  name,Context context)
    {
        this.name=name;
        this.context=context;
        firebaseDatabase= FirebaseDatabase.getInstance().getReference().child("Bus_Schedule");
        firebaseDatabase.keepSynced(true);
        Log.d("sabeek","age");

        firebaseDatabase.child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("toads",String.valueOf(dataSnapshot.getChildrenCount()));
                  GlobalClass.childcount= (int) dataSnapshot.getChildrenCount();

                Log.d("sabeek","pore");


                if(GlobalClass.listTaranga.size()!=0)
                    GlobalClass.listTaranga.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("toot",snapshot.getKey());
                    GlobalClass.listTaranga.add(snapshot.getKey().toString());
                    Log.d("toad",String.valueOf(GlobalClass.listTaranga.size()));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public String[] getTime() {

        if(name.equals("Anando"))
        {
            String time[] = {"6:50 up","7:50 up","8:50 up","12:15 down","1:05 down","2:30 down","4:05 down","5:10 down"};
            return time;
        }
        else if(name.equals("Boishakhi"))
        {
            String time[] = {"6:30 up","6:50 up","7:30 up","8:00 up","9:00 up","9:45 up","12:30 down","1:15 down","2:20 down","3:30 down","4:30 down","5:30 down"};
            return time;
        }
        else if(name.equals("Boshonto"))
        {
            String time[] = {"7:00 up","7:50 up","8:40 up","10:00 up","12:15 down","1:30 down","2:35 down","3:45 down","5:15 down"};
            return time;
        }
        else if(name.equals("Chittagong Road"))
        {
            String time[] = {"8:10 up","8:20 up","9:20 up","2:30 down","4:10 down","5:10 down"};
            return time;
        }
        else if(name.equals("Choitaly"))
        {
            String time[] = {"6:50 up","7:15 up","8:15 up","9:40 up","1:10 down","2:10 down","3:20 down","4:00 down","4:30 down","5:00 down","5:40 down"};
            return time;
        }
        else if(name.equals("Falguni"))
        {
            String time[] = {"6:45 up","7:40 up","8:45 up","9:40 up","1:10 down","3:45 down","5:05 down"};
            return time;
        }
        else if(name.equals("Hemonto"))
        {
            String time[] = {"6:20 up","7:00 up","8:00 up","1:10 down","3:15 down","5:10 down"};
            return time;
        }
        else if(name.equals("Ishakha"))
        {
            String time[] = {"6:40 up","7:10 up","7:40 up","8:00 up","9:00 up","9:05 up","10:00 up","1:15 down","2:30 down","4:10 down","5:05 down","5:15 down"};
            return time;
        }
        else if(name.equals("Kinchit"))
        {
            String time[] = {"7:10 up","8:00 up","8:50 up","9:50 up","12:30 down","1:30 down","3:10 down","4:10 down","5:20 down"};
            return time;
        }
        else if(name.equals("Khonika"))
        {
            String time[] = {"5:55 up","6:20 up","6:35 up","6:50 up","7:30 up","8:10 up","9:15 up","12:10 down","1:00 down","1:10 down","1:50 down","2:30 down","3:30 down","4:15 down","5:00 down","5:30 down"};
            return time;
        }else if(name.equals("Moitree/null"))
        {
            String time[]=null;
            return time;
        }

        else if(name.equals("Srabon"))
        {
            String time[] = {"7:20 up","8:15 up","9:00 up","10:00 up","12:05 down","1:30 down","2:20 down","3:40 down","4:30 down","5:20 down"};
            return time;
        }

        else if(name.equals("Taranga"))
        {

            String s;
            mPreferences = context.getSharedPreferences("hellosakib", MODE_PRIVATE);
            s= mPreferences.getString("Taranga", "");
            int x=s.length(),count=0,l=0;
            for(int i=0;i<x;i++)
            {
                if(s.charAt(i)=='$')count++;
            }
            String[] time = new String[count];
            StringTokenizer stx = new StringTokenizer(s,"$");
            while(stx.hasMoreElements())
            {
                time[l++]= (String) stx.nextElement();
            }

            return time;
        }
        else if(name.equals("Ullash"))
        {
            String time[] = {"7:00 up","8:00 up","9:00 up","9:35 up","10:00 up","12:10 down","1:10 down","2:10 down","3:10 down","4:10 down","5:15 down"};
            return time;
        }
        else if(name.equals("Wari"))
        {
            String time[] = {"7:10 up","4:00 down"};
            return time;
        }
        else
            return def;
    }
}