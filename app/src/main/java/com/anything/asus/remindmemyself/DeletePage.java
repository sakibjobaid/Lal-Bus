package com.anything.asus.remindmemyself;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class DeletePage extends AppCompatActivity implements View.OnClickListener {


    private ArrayList<String> TimeTable= new ArrayList<>();
    String finalTime,temps;
    Spinner spinnerHour;
    Button saveChange;
    TextView type,from,to;

    SharedPreferences time_pref, schedule_pref, password_pref,repeatTime_pref;

    DatabaseReference firebaseDatabase=null,firepass=null,
            locationreference=null,schedule_ref=null,
            time_ref=null,password_ref=null;

    private OnCompleteListener delete_listener=null;

    private  ProgressDialog progressDialog;

    ValueEventListener val_schedule,val_pass,val_time;
    SharedPreferences.Editor repeatTimeEditor;

    intt[] up = null;
    intt[] down = null;
    intt2[] up2 = null;
    intt2[] down2 = null;
    int k, l, k1, l1,i;
    String temp2,temp;
    boolean up2deleted=false,down2deleted=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        this.setTitle("Delete information");


        time_pref= getSharedPreferences(getString(R.string.time_pref_file), MODE_PRIVATE);
        schedule_pref = getSharedPreferences(getString(R.string.schedule_pref_file), MODE_PRIVATE);
        password_pref = getSharedPreferences(getString(R.string.pass_pref_file), MODE_PRIVATE);
        repeatTime_pref = getSharedPreferences(getString(R.string.repeatTime_pref_file), MODE_PRIVATE);


        //region default kaj
        repeatTimeEditor=repeatTime_pref.edit();
        temps=schedule_pref.getString(GlobalClass.tempBusName,"");


        saveChange=(Button)findViewById(R.id.saveButton);
        saveChange.setOnClickListener(this);
        from=(TextView) findViewById(R.id.fromText);
        to=(TextView) findViewById(R.id.toText);

        spinnerHour=(Spinner)findViewById(R.id.hourSpinner);
        type= (TextView)findViewById(R.id.typeText);

        BusAndTime b = new BusAndTime(GlobalClass.tempBusName, this);

        TimeTable = b.getTime();


        ArrayAdapter<String> spinnerArrayAdapterBus = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        TimeTable);
        spinnerArrayAdapterBus.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerHour.setAdapter(spinnerArrayAdapterBus);

        spinnerHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                finalTime = item.toString();

                datapopulate(finalTime);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //endregion


        firepass = FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_password))
                .child(GlobalClass.tempBusName);
        locationreference=FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_admin))
                .child("Location")
                .child(GlobalClass.tempBusName);

        schedule_ref = FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_busSchedule))
                .child(GlobalClass.tempBusName).child("1");


        time_ref = FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_time))
                .child(GlobalClass.tempBusName).child("1");


        password_ref = FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_password))
                .child(GlobalClass.tempBusName).child("1");



    }

    private void datapopulate(String finalTime) {

        StringTokenizer ppx = new StringTokenizer(temps,"$");
        while(ppx.hasMoreElements())
        {
            String single= (String) ppx.nextElement();
            StringTokenizer stx = new StringTokenizer(single,"#");

            if(stx.nextElement().equals(finalTime))
            {
                from.setText((String)stx.nextElement());
                to.setText((String)stx.nextElement());
                type.setText((String)stx.nextElement());
                break;
            }

        }

    }


    @Override
    public void onClick(View v) {

        if(v==saveChange)
        {

                AlertDialog.Builder builder = new AlertDialog.Builder(DeletePage.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Your changes will be saved in the database. Do you want to continue?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String x="";
                        for(int i=0;i<finalTime.length();i++)
                        {
                            if(finalTime.charAt(i)==' ')
                            {
                                break;
                            }
                            else
                            {
                                x+=finalTime.charAt(i);
                            }
                        }


                        if(finalTime.matches("(.*)up2"))
                        {
                            repeatTimeEditor.putString(GlobalClass.tempBusName+x+"up2","0" );
                            repeatTimeEditor.apply();
                        }
                        if(finalTime.matches("(.*)down2"))
                        {
                            repeatTimeEditor.putString(GlobalClass.tempBusName+x+"down2","0" );
                            repeatTimeEditor.apply();
                        }

                        if(finalTime.matches("(.*)down")&&repeatTime_pref.getString(GlobalClass.tempBusName+x+"down2","0")
                                .equals("1"))
                        {
                            down2deleted=true;
                            toastIconInfo("Delete "+x+"down2 at first.");
                            return ;
                        }
                        if(finalTime.matches("(.*)up")&&repeatTime_pref.getString(GlobalClass.tempBusName
                                +x+"up2","0")
                                .equals("1"))
                        {
                            up2deleted=true;
                            toastIconInfo("Delete "+x+"up2 at first.");
                            return ;
                        }

                        if(up2deleted)
                        {
                            repeatTimeEditor.putString(GlobalClass.tempBusName+x+"up2","0" );
                            repeatTimeEditor.apply();
                        }
                        if(down2deleted)
                        {
                            repeatTimeEditor.putString(GlobalClass.tempBusName+x+"down2","0" );
                            repeatTimeEditor.apply();
                        }

                        //region Bus_schedule

                        delete_listener= new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                if(task.isSuccessful())
                                {
                                    if(progressDialog!=null)
                                    progressDialog.dismiss();

                                    if(schedule_ref!=null && val_schedule!=null)
                                        schedule_ref.removeEventListener(val_schedule);
                                    if(password_ref!=null && val_pass!=null)
                                        password_ref.removeEventListener(val_pass);
                                    if(time_ref!=null && val_time!=null)
                                        time_ref.removeEventListener(val_time);

                                    finish();

                                }
                            }
                        };
                        schedule_ref.child(finalTime).removeValue().addOnCompleteListener(delete_listener);

                        final Handler handlerschedule_read = new Handler();
                        handlerschedule_read.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                val_schedule = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        up2 = new intt2[50];
                                        down2 = new intt2[50];
                                        k1 = 0;
                                        l1 = 0;
                                        for (DataSnapshot inner : dataSnapshot.getChildren()) {

                                            if (inner.getKey().matches("(.*)up")) {
                                                up2[k1] = new intt2();
                                                up2[k1].time = (String) inner.child("time").getValue();
                                                up2[k1].from = (String) inner.child("from").getValue();
                                                up2[k1].to = (String) inner.child("to").getValue();
                                                up2[k1].type = (String) inner.child("type").getValue();

                                                k1++;
                                            } else if (inner.getKey().matches("(.*)up2")) {
                                                up2[k1] = new intt2();
                                                up2[k1].time = (String) inner.child("time").getValue();
                                                up2[k1].from = (String) inner.child("from").getValue();
                                                up2[k1].to = (String) inner.child("to").getValue();
                                                up2[k1].type = (String) inner.child("type").getValue();

                                                k1++;
                                            } else if (inner.getKey().matches("(.*)down")) {
                                                down2[l1] = new intt2();
                                                down2[l1].time = (String) inner.child("time").getValue();
                                                down2[l1].from = (String) inner.child("from").getValue();
                                                down2[l1].to = (String) inner.child("to").getValue();
                                                down2[l1].type = (String) inner.child("type").getValue();
                                                l1++;
                                            } else if (inner.getKey().matches("(.*)down2")) {
                                                down2[l1] = new intt2();
                                                down2[l1].time = (String) inner.child("time").getValue();
                                                down2[l1].from = (String) inner.child("from").getValue();
                                                down2[l1].to = (String) inner.child("to").getValue();
                                                down2[l1].type = (String) inner.child("type").getValue();
                                                l1++;
                                            }
                                        }
                                        sorting2(GlobalClass.tempBusName);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };
                                schedule_ref.addValueEventListener(val_schedule);

                            }
                        }, 1000);

                        //endregion

                        //region time

                        time_ref.child(finalTime).removeValue();


                        final Handler handlertime_read = new Handler();
                        handlertime_read.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                val_time = new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        up = new intt[50];
                                        down = new intt[50];
                                        k = 0;
                                        l = 0;

                                        for (DataSnapshot inner : dataSnapshot.getChildren()) {


                                            char lastChar = inner.getKey().charAt(inner.getKey().length() - 1);

                                            if (inner.getKey().matches("(.*)up") && lastChar == 'p') {
                                                up[k] = new intt();
                                                StringTokenizer pp = new StringTokenizer(inner.getKey(), " up");
                                                up[k++].z = (String) pp.nextElement();

                                            } else if (inner.getKey().matches("(.*)up2") && lastChar == '2') {
                                                up[k] = new intt();

                                                StringTokenizer pp = new StringTokenizer(inner.getKey(), " up");
                                                up[k++].z = (String) pp.nextElement();

                                            } else if (inner.getKey().matches("(.*)down") && lastChar == 'n') {
                                                String tempo;
                                                down[l] = new intt();
                                                StringTokenizer pp = new StringTokenizer(inner.getKey(), " down");
                                                tempo = (String) pp.nextElement();


                                                StringTokenizer stx = new StringTokenizer(tempo, ":");
                                                int a = Integer.parseInt((String) stx.nextElement());
                                                int b = Integer.parseInt((String) stx.nextElement());
                                                if (a != 12)
                                                    a += 12;
                                                if (b == 0)
                                                    down[l++].z = String.valueOf(a) + ":00";
                                                else
                                                    down[l++].z = String.valueOf(a) + ":" + String.valueOf(b);

                                            } else if (inner.getKey().matches("(.*)down2") && lastChar == '2') {
                                                String tempo = "";
                                                down[l] = new intt();
                                                StringTokenizer pp = new StringTokenizer(inner.getKey(), " down");
                                                tempo = (String) pp.nextElement();


                                                StringTokenizer stx = new StringTokenizer(tempo, ":");
                                                int a = Integer.parseInt((String) stx.nextElement());
                                                int b = Integer.parseInt((String) stx.nextElement());
                                                if (a != 12)
                                                    a += 12;
                                                if (b == 0)
                                                    down[l++].z = String.valueOf(a) + ":00";
                                                else
                                                    down[l++].z = String.valueOf(a) + ":" + String.valueOf(b);

                                            }

                                        }

                                        sorting(GlobalClass.tempBusName);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        System.out.println("The read failed: " + databaseError.getCode());
                                    }
                                };
                                time_ref.addValueEventListener(val_time);

                            }
                        }, 1000);




                        //endregion

                        //region password

                        password_ref.child(finalTime).removeValue();

                        final Handler handlerpassword_read = new Handler();
                        handlertime_read.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                val_pass = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot inner : dataSnapshot.getChildren()) {
                                            SharedPreferences.Editor preferencesEditor = password_pref.edit();
                                            preferencesEditor.putString(GlobalClass.tempBusName + inner.getKey(), inner.getValue().toString());
                                            preferencesEditor.apply();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };

                                password_ref.addValueEventListener(val_pass);

                            }
                        }, 1000);





                        //endregion



                         progressDialog = new ProgressDialog(DeletePage.this);
                        progressDialog.setMessage("Saving your changes...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();



                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();

        }
    }

    private void toastIconInfo(String purpose) {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(purpose);
        ((CardView) custom_view.findViewById(R.id.parent_view2)).setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));

        toast.setView(custom_view);
        toast.show();
    }
    private void sorting(String name) {

        SharedPreferences.Editor repeatTimeEditor = repeatTime_pref.edit();

        temp = "";
        Arrays.sort(up, 0, k, new DeletePage.Cmp());
        for (i = 0; i < k; ++i) {

            System.out.print(" " + up[i].z);
            if (i > 0 && up[i].z.equals(up[i - 1].z))
            {
                temp += (up[i].z + " up2");
                repeatTimeEditor.putString(name+up[i].z + " up2","1" );
                repeatTimeEditor.apply();
                temp+="$";

            }
            else
            {
                temp += (up[i].z + " up");
                repeatTimeEditor.putString(name+up[i].z + " up","1" );
                repeatTimeEditor.apply();
                temp+="$";

            }



        }
        Arrays.sort(down, 0, l, new DeletePage.Cmp());
        for (i = 0; i < l; ++i) {
            String tempo1, tempo2;

            boolean checkFlag=false;
            if (i > 0 && down[i].z.equals(down[i - 1].z))
            {
                checkFlag=true;
            }
            System.out.print(" " + down[i].z);
            StringTokenizer stx = new StringTokenizer(down[i].z, ":");
            int a = Integer.parseInt((String) stx.nextElement());
            int b = Integer.parseInt((String) stx.nextElement());
            if (a > 12) {
                a -= 12;
                tempo1 = String.valueOf(a);

            } else tempo1 = String.valueOf(a);
            if (b == 0)
                tempo2 = ":00";
            else if (b < 10)
                tempo2 = ":0" + b;
            else
                tempo2 = ":" + String.valueOf(b);
            if(checkFlag)
            {
                temp += (tempo1 + tempo2 + " down2");
                repeatTimeEditor.putString(name+tempo1 + tempo2 + " down2","1" );
                repeatTimeEditor.apply();
                temp+="$";
            }
            else
            {
                temp += (tempo1 + tempo2 + " down");
                repeatTimeEditor.putString(name+tempo1 + tempo2 + " down","1" );
                repeatTimeEditor.apply();
                temp+="$";
            }




        }



        SharedPreferences.Editor preferencesEditor = time_pref.edit();
        preferencesEditor.putString(name, temp);
        preferencesEditor.apply();

    }
    private void sorting2(String name) {
        temp2 = "";

        Arrays.sort(up2, 0, k1, new DeletePage.Cmp2());
        for (i = 0; i < k1; ++i) {
            temp2 += (up2[i].time + "#");
            temp2 += (up2[i].from + "#");
            temp2 += (up2[i].to + "#");
            temp2 += (up2[i].type + "$");
        }
        Arrays.sort(down2, 0, l1, new DeletePage.Cmp2());
        for (i = 0; i < l1; ++i) {
            temp2 += (down2[i].time + "#");
            temp2 += (down2[i].from + "#");
            temp2 += (down2[i].to + "#");
            temp2 += (down2[i].type + "$");
        }

        SharedPreferences.Editor preferencesEditor = schedule_pref.edit();
        preferencesEditor.putString(name, temp2);
        preferencesEditor.apply();

    }
    class Cmp implements Comparator<intt> {
        public int compare(intt x, intt y) {

            String strx = x.z, stry = y.z;


            StringTokenizer stx = new StringTokenizer(strx, ":");
            int a = Integer.parseInt((String) stx.nextElement());

            int b = Integer.parseInt((String) stx.nextElement());

            StringTokenizer sty = new StringTokenizer(stry, ":");
            int c = Integer.parseInt((String) sty.nextElement());

            int d = Integer.parseInt((String) sty.nextElement());



            if (a == c) {
                if (b == d) return 0;
                if (b < d) return -1;
                else return 1;
            } else if (a < c) return -1;
            else return 1;

        }
    }
    class Cmp2 implements Comparator<intt2> {
        public int compare(intt2 x, intt2 y) {

            String strx = x.time, stry = y.time;
            int a = 0, b = 0, c = 0, d = 0;
            if (x.time.matches("(.*)down")) {
                StringTokenizer ppx = new StringTokenizer(strx, " down");
                String tempox = (String) ppx.nextElement();
                StringTokenizer stx = new StringTokenizer(tempox, ":");
                a = Integer.parseInt((String) stx.nextElement());
                b = Integer.parseInt((String) stx.nextElement());
                if (a != 12)
                    a += 12;
                StringTokenizer ppy = new StringTokenizer(stry, " down");
                String tempoy = (String) ppy.nextElement();
                StringTokenizer sty = new StringTokenizer(tempoy, ":");
                c = Integer.parseInt((String) sty.nextElement());
                if (c != 12)
                    c += 12;
                d = Integer.parseInt((String) sty.nextElement());

            } else if (x.time.matches("(.*)down2")) {
                StringTokenizer ppx = new StringTokenizer(strx, " down");
                String tempox = (String) ppx.nextElement();
                StringTokenizer stx = new StringTokenizer(tempox, ":");
                a = Integer.parseInt((String) stx.nextElement());
                b = Integer.parseInt((String) stx.nextElement());
                if (a != 12)
                    a += 12;
                StringTokenizer ppy = new StringTokenizer(stry, " down");
                String tempoy = (String) ppy.nextElement();
                StringTokenizer sty = new StringTokenizer(tempoy, ":");
                c = Integer.parseInt((String) sty.nextElement());
                if (c != 12)
                    c += 12;
                d = Integer.parseInt((String) sty.nextElement());

            } else if (x.time.matches("(.*)up")) {
                StringTokenizer ppx = new StringTokenizer(strx, " up");
                String tempox = (String) ppx.nextElement();

                StringTokenizer stx = new StringTokenizer(tempox, ":");
                a = Integer.parseInt((String) stx.nextElement());

                b = Integer.parseInt((String) stx.nextElement());

                StringTokenizer ppy = new StringTokenizer(stry, " up");
                String tempoy = (String) ppy.nextElement();

                StringTokenizer sty = new StringTokenizer(tempoy, ":");
                c = Integer.parseInt((String) sty.nextElement());

                d = Integer.parseInt((String) sty.nextElement());


            } else if (x.time.matches("(.*)up2")) {
                StringTokenizer ppx = new StringTokenizer(strx, " up");
                String tempox = (String) ppx.nextElement();
                StringTokenizer stx = new StringTokenizer(tempox, ":");

                a = Integer.parseInt((String) stx.nextElement());

                b = Integer.parseInt((String) stx.nextElement());

                StringTokenizer ppy = new StringTokenizer(stry, " up");
                String tempoy = (String) ppy.nextElement();

                StringTokenizer sty = new StringTokenizer(tempoy, ":");
                c = Integer.parseInt((String) sty.nextElement());

                d = Integer.parseInt((String) sty.nextElement());


            }

            if (a == c) {
                if (b == d) return 0;
                if (b < d) return -1;
                else return 1;
            } else if (a < c) return -1;
            else return 1;

        }
    }
}
