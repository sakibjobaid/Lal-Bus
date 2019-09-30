package com.anything.asus.remindmemyself;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class central_admin_settings extends AppCompatActivity implements View.OnClickListener {


    protected Button force_out, radius_time, normal_password, master_password, admin_roster,forceInActiveOut;

    SharedPreferences datePref;
    protected DatabaseReference passChange_reference = null, masterpass_reference = null,
            admin_roster_ref,forceIn_activeOff_reference=null;
    protected String dateCentral;
    protected ValueEventListener pass_change = null, adminRoster = null,val_forceIn_activeOff=null;
    protected ArrayList<String> TimeTable;
    protected LinearLayout masterHide;

    protected Spinner busspinner, timespinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_admin_settings);

        force_out = findViewById(R.id.force_out);
        radius_time = findViewById(R.id.radius_time);
        normal_password = findViewById(R.id.password_central);
        master_password = findViewById(R.id.master_password_central);
        admin_roster = findViewById(R.id.admin_roster);
        forceInActiveOut=findViewById(R.id.force_in_active_off);
        masterHide=findViewById(R.id.masterHide);

        datePref = getSharedPreferences(getString(R.string.date_pref_file), MODE_PRIVATE);
        dateCentral = datePref.getString(getString(R.string.date_getter), "nothing");

        passChange_reference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_password));
        masterpass_reference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_masterAdmin))
                .child("1");
        admin_roster_ref = FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_adminRoster))
                .child(dateCentral);

        forceIn_activeOff_reference=FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_admin))
                .child("Location");

        force_out.setOnClickListener(this);
        radius_time.setOnClickListener(this);
        normal_password.setOnClickListener(this);
        master_password.setOnClickListener(this);
        admin_roster.setOnClickListener(this);
        forceInActiveOut.setOnClickListener(this);


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

    @Override
    public void onClick(View v) {

        if (v == force_out) {
            if(!isNetworkConnected(this))
            {
                toastIconInfo("Internet connection required");

                return;
            }
            else
            startActivity(new Intent(central_admin_settings.this, force_out.class));
        } else if (v == radius_time) {

            if(!isNetworkConnected(this))
            {
                toastIconInfo("Internet connection required");

                return;
            }
            else
            startActivity(new Intent(central_admin_settings.this, radius_time.class));

        } else if (v == normal_password) {
            if(!isNetworkConnected(this))
            {
                toastIconInfo("Internet connection required");

                return;
            }
            else
            customNormal_pass_dialog();
        } else if (v == master_password) {
            if(!isNetworkConnected(this))
            {
                toastIconInfo("Internet connection required");


                return;
            }
            else
            customMaster_pass_dialog();
        }
        else if(v==forceInActiveOut)
        {
            if(!isNetworkConnected(this))
            {
                toastIconInfo("Internet connection required");


                return;
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(central_admin_settings.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure to force in and active off ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        toastIconInfo("All force IN and active OFF");

                        changetoAllForceIn_activeOff();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();
                builder.setCancelable(false);
            }




        }else if (v == admin_roster) {

            if(!isNetworkConnected(this))
            {
                toastIconInfo("Internet connection required");

                return;
            }
            else
            custom_admin_roster();

        }

    }

    public static boolean isNetworkConnected(Context context) {

        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = true;
                    }
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        result = true;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }


    private void changetoAllForceIn_activeOff() {

        val_forceIn_activeOff= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot busName: dataSnapshot.getChildren())
                {
                    for(DataSnapshot busTime: busName.getChildren())
                    {
                        busTime.child("active_on").getRef().setValue("off");
                        busTime.child("force").getRef().setValue("in");
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        forceIn_activeOff_reference.addListenerForSingleValueEvent(val_forceIn_activeOff);

    }

    private void custom_admin_roster() {

        // custom dialog
        final Dialog dialog = new Dialog(central_admin_settings.this);
        dialog.setContentView(R.layout.admin_roster_layout_central);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        TimeTable = new ArrayList<>();

        busspinner = dialog.findViewById(R.id.busSpinner22);
        timespinner = dialog.findViewById(R.id.timeSpinner22);

        Button viewBtn, deleteBtn;


        ArrayAdapter<String> spinnerArrayAdapterBus = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        GlobalClass.BusNameKey);
        spinnerArrayAdapterBus.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        busspinner.setAdapter(spinnerArrayAdapterBus);

        busspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                GlobalClass.tempBusName = item.toString();

                BusAndTime b = new BusAndTime(GlobalClass.tempBusName, central_admin_settings.this);
                TimeTable = b.getTime();
                timePopulate(TimeTable);

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        viewBtn = dialog.findViewById(R.id.view22);
        deleteBtn = dialog.findViewById(R.id.delete22);

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                dialog.cancel();

                details_viewing();

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                dialog.cancel();
                admin_roster_ref.child(GlobalClass.tempBusName)
                        .child(GlobalClass.tempBustime).removeValue();

                toastIconInfo("This node deleted");


            }
        });

        dialog.show();


    }

    private void customNormal_pass_dialog() {

        // custom dialog
        final Dialog dialog = new Dialog(central_admin_settings.this);
        dialog.setContentView(R.layout.custom_normal_pass_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        TextView textabove = dialog.findViewById(R.id.txt);
        textabove.setText("Normal Password");


        Button viewBtn, allChangeBtn, selChangeBtn;

        viewBtn = dialog.findViewById(R.id.view_btn);
        allChangeBtn = dialog.findViewById(R.id.all_change_btn);
        selChangeBtn = dialog.findViewById(R.id.selective_change_Btn);

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                dialog.cancel();
                busSelect();

            }
        });

        allChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(central_admin_settings.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure to change all normal passwords?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        toastIconInfo( "All normal password is changed");
                        dialog.dismiss();
                        dialog.cancel();
                        passchange("normal");

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();
                builder.setCancelable(false);




            }
        });

        selChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
                dialog.cancel();
                custom_selChange("normal");

            }
        });
        // set the custom dialog components - text, image and button


        dialog.show();

    }

    private void customMaster_pass_dialog() {

        // custom dialog
        final Dialog dialog = new Dialog(central_admin_settings.this);
        dialog.setContentView(R.layout.custom_normal_pass_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        TextView textabove = dialog.findViewById(R.id.txt);
        textabove.setText("Master Password");

        Button viewBtn, allChangeBtn, selChangeBtn;

        viewBtn = dialog.findViewById(R.id.view_btn);
        allChangeBtn = dialog.findViewById(R.id.all_change_btn);
        selChangeBtn = dialog.findViewById(R.id.selective_change_Btn);

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                dialog.cancel();

                masterpass_viewing();
            }
        });

        allChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(central_admin_settings.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure to change all Master passwords?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        dialog.cancel();
                        toastIconInfo( "All master password is changed");
                        passchange("master");

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();
                builder.setCancelable(false);




            }
        });

        selChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                dialog.cancel();

                custom_selChange("master");

            }
        });
        // set the custom dialog components - text, image and button


        dialog.show();
    }

    private void details_viewing() {

        // custom dialog
        final Dialog dialog = new Dialog(central_admin_settings.this);
        dialog.setContentView(R.layout.detalis_view_admin_roster);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.MATCH_PARENT);
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        final TextView busNameTime, busNumber, count, department, mobileNo, name, status;

        busNameTime = dialog.findViewById(R.id.busNameTime22);
        busNumber = dialog.findViewById(R.id.busNumber22);
        count = dialog.findViewById(R.id.count22);
        department = dialog.findViewById(R.id.department22);
        mobileNo = dialog.findViewById(R.id.mobile22);
        name = dialog.findViewById(R.id.name22);
        status = dialog.findViewById(R.id.status22);


        busNameTime.setText(GlobalClass.tempBusName + " " + GlobalClass.tempBustime);

        adminRoster = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot inner : dataSnapshot.getChildren()) {

                    if (inner.getKey().equals("BusNumber"))
                        busNumber.setText(inner.getValue().toString());
                    else if (inner.getKey().equals("Count"))
                        count.setText(inner.getValue().toString());
                    else if (inner.getKey().equals("Department"))
                        department.setText(inner.getValue().toString());
                    else if (inner.getKey().equals("MobileNo"))
                        mobileNo.setText(inner.getValue().toString());
                    else if (inner.getKey().equals("Name"))
                        name.setText(inner.getValue().toString());
                    else if (inner.getKey().equals("Status"))
                        status.setText(inner.getValue().toString());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        admin_roster_ref.child(GlobalClass.tempBusName).
                child(GlobalClass.tempBustime).addListenerForSingleValueEvent(adminRoster);


        dialog.show();


    }

    private void masterpass_viewing() {

        startActivity(new Intent(central_admin_settings.this, masterpass_view_activity.class));


    }

    private void custom_selChange(final String purpose) {
        // custom dialog
        final Dialog dialog = new Dialog(central_admin_settings.this);
        dialog.setContentView(R.layout.custom_sel_change);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.MATCH_PARENT);
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        TimeTable = new ArrayList<>();

        busspinner = dialog.findViewById(R.id.busSpinner22);
        timespinner = dialog.findViewById(R.id.timeSpinner22);

        if (purpose.equals("master"))
            masterHide.setVisibility(View.GONE);
        Button manual = dialog.findViewById(R.id.manual);
        Button auto = dialog.findViewById(R.id.auto);

        final EditText edit = dialog.findViewById(R.id.manualEdit);

        ArrayAdapter<String> spinnerArrayAdapterBus = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        GlobalClass.BusNameKey);
        spinnerArrayAdapterBus.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        busspinner.setAdapter(spinnerArrayAdapterBus);

        busspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                GlobalClass.tempBusName = item.toString();

                BusAndTime b = new BusAndTime(GlobalClass.tempBusName, central_admin_settings.this);
                TimeTable = b.getTime();
                timePopulate(TimeTable);

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                dialog.cancel();

                if (purpose.equals("master")) {
                    Random rand = new Random();

                    int randomInt = rand.nextInt((999999 - 100000) + 1);

                    masterpass_reference.child(GlobalClass.tempBusName)
                            .setValue(String.valueOf(randomInt));
                } else {

                    sel_pass_change();

                }
            }
        });

        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
                if (edit.getText().toString().trim().length() == 0) {
                    toastIconInfo("Empty password");
                    return;

                } else {
                    if (purpose.equals("master")) {
                        if (edit.getText().toString().trim().length() < 6) {
                            toastIconInfo("Min pass length is 6");
                            return;
                        } else {

                            toastIconInfo("Manual master pass is set as " +
                                    edit.getText().toString().trim());

                            masterpass_reference.child(GlobalClass.tempBusName)
                                    .setValue(edit.getText().toString().trim());
                        }

                    } else {
                        toastIconInfo("Manual normal pass is set as " +
                                edit.getText().toString().trim());

                        passChange_reference.child(GlobalClass.tempBusName).child("1").child(GlobalClass.tempBustime)
                                .setValue(edit.getText().toString().trim());
                    }

                }
            }
        });


        dialog.show();

    }

    private void timePopulate(ArrayList timeTable) {

        ArrayAdapter<String> spinnerArrayAdapterTime = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        timeTable);
        spinnerArrayAdapterTime.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        timespinner.setAdapter(spinnerArrayAdapterTime);

        timespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                GlobalClass.tempBustime = item.toString();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void sel_pass_change() {

        Random rand = new Random();

        int randomInt = rand.nextInt((9999 - 1000) + 1);

        passChange_reference.child(GlobalClass.tempBusName).child("1").child(GlobalClass.tempBustime)
                .setValue(String.valueOf(randomInt));

    }

    private void busSelect() {

        // custom dialog
        final Dialog dialog = new Dialog(central_admin_settings.this);
        dialog.setContentView(R.layout.bus_select_in_central_admin);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        Spinner busspinner = dialog.findViewById(R.id.busSpinnerCentral);
        Button btn = dialog.findViewById(R.id.ButtonContinueCentral);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                dialog.cancel();


                startActivity(new Intent(central_admin_settings.this, PasswordsImage.class));


            }
        });

        ArrayAdapter<String> spinnerArrayAdapterBus = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        GlobalClass.BusNameKey);
        spinnerArrayAdapterBus.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        busspinner.setAdapter(spinnerArrayAdapterBus);

        busspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                GlobalClass.tempBusName = item.toString();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        dialog.show();
    }

    private void passchange(final String purpose) {


        if (purpose.equals("master")) {

            pass_change = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot busName : dataSnapshot.getChildren()) {

                        Random rand = new Random();
                        int randomInt = rand.nextInt((999999 - 100000) + 1);
                        busName.getRef().setValue(String.valueOf(randomInt));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            masterpass_reference.addListenerForSingleValueEvent(pass_change);

        } else {
            pass_change = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot busName : dataSnapshot.getChildren()) {

                        for (DataSnapshot version : busName.getChildren()) {

                            for (DataSnapshot time : version.getChildren()) {
                                Random rand = new Random();

                                int randomInt = rand.nextInt((9999 - 1000) + 1);
                                time.getRef().setValue(String.valueOf(randomInt));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            passChange_reference.addListenerForSingleValueEvent(pass_change);
        }

    }

}
