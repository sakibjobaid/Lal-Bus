package com.anything.asus.remindmemyself;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.StringTokenizer;

public class AdminOptions extends AppCompatActivity implements View.OnClickListener {

    //region variable
    private Button button;
    private String ajkerDate, customPass,tempo;
    private ArrayList<String> TimeTable;
    private Spinner spinnerBus, spinnerTime;
    private TextView masterAdmin, customtextView,central_admin;
    Context context;
    private EditText customeditText;
    LinearLayout adminhoise ;
    int  difference=0;
    boolean pore,previous=false;
    private ProgressDialog progressDialog;

    private  EditText password,busNumberText;
    private  TextView header;

    private Dialog dialogPassword,dialogMaster;


    private String s;



    DatabaseReference passChange_reference = null, loc = null, roster = null,
            dateReference=null,passwordReference=null,masterReference=null;
    public SharedPreferences datePref, busNamePref, busTimePref;

    public static ValueEventListener pass_change = null, val_loc = null, val_roster = null,
            val_date=null,passwordListener=null,masterListener=null;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);
        context = this;
        datePref = getSharedPreferences(getString(R.string.date_pref_file), MODE_PRIVATE);
        busNamePref = getSharedPreferences(getString(R.string.bus_name_pref_file), MODE_PRIVATE);
        busTimePref = getSharedPreferences(getString(R.string.bus_time_pref_file), MODE_PRIVATE);
        ajkerDate = datePref.getString(getString(R.string.date_getter), "nothing");

        masterReference=FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_masterAdmin))
        .child("1");

        adminhoise=findViewById(R.id.adminHoise);
        masterAdmin = (TextView) findViewById(R.id.masteradmin);
        masterAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openCustomDialog();
            }
        });

        if(isNetworkConnected(this))
        {
            //region date
            dateReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_date));
            val_date = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    SharedPreferences.Editor preferencesEditor = datePref.edit();
                    preferencesEditor.putString(getString(R.string.date_getter), dataSnapshot.getValue().toString());
                    preferencesEditor.apply();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            dateReference.addValueEventListener(val_date);
            //endregion
        }


        progressDialog = new ProgressDialog(AdminOptions.this);
        TimeTable = new ArrayList<>();


        spinnerBus = findViewById(R.id.busSpinner);
        spinnerTime = findViewById(R.id.timeSpinner);

        ArrayAdapter<String> spinnerArrayAdapterBus = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        GlobalClass.BusNameKey);
        spinnerArrayAdapterBus.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerBus.setAdapter(spinnerArrayAdapterBus);

        spinnerBus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                GlobalClass.tempBusName = item.toString();
                BusAndTime b = new BusAndTime(GlobalClass.tempBusName, context);


                TimeTable = b.getTime();


                timePopulate(TimeTable);


            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        button = findViewById(R.id.ButtonContinue);
        central_admin=findViewById(R.id.centraladmin);

        //central_admin.setVisibility(View.GONE);

         central_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                central_admin_custom_dialog();
            }
        });

        button.setOnClickListener(this);

        if(GlobalClass.adminHoise)
        {
            adminhoise.setVisibility(View.VISIBLE);
            TextView profile_settings = findViewById(R.id.profile_settings);
            profile_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showAlertConfirmation();

                }
            });

        }
        else
            adminhoise.setVisibility(View.GONE);



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

    private void central_admin_custom_dialog() {

        // custom dialog
        final Dialog dialog = new Dialog(AdminOptions.this);
        dialog.setContentView(R.layout.central_admin_custom_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);


        customeditText = (EditText) dialog.findViewById(R.id.centralText);

        Button loginButton = (Button) dialog.findViewById(R.id.mlogButton2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customeditText.getText().toString().equals("442522")) {
                    startActivity(new Intent(AdminOptions.this, central_admin_settings.class));
                } else {
                    toastIconInfo("Invalid password");
                    return;
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void toastIconInfo(String purpose) {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(purpose);
        ((CardView) custom_view.findViewById(R.id.parent_view2)).setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));

        toast.setView(custom_view);
        toast.show();
    }

    private void showAlertConfirmation() {

//        AlertDialog.Builder builder = new AlertDialog.Builder(AdminOptions.this);
//
//            builder.setTitle("Confirmation");
//            builder.setMessage("Are you an admin of "+
//                    GlobalClass.tempBusName+" ?");
//
//            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Intent intent = new Intent(AdminOptions.this, editProfile.class);
//                    startActivity(intent);
//                }
//            });
//
//            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
//
//            builder.show();
        progressDialog.setMessage("Loading your profile...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StartActiviy.val_admin_list_ref= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    progressDialog.dismiss();
                    progressDialog.cancel();
                    GlobalClass.tempBusName=dataSnapshot.getValue().toString();
                    Intent intent = new Intent(AdminOptions.this, editProfile.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        StartActiviy.admin_list_ref.addListenerForSingleValueEvent(StartActiviy.val_admin_list_ref);

        }

    private void openCustomDialog() {

        // custom dialog
        dialogMaster = new Dialog(AdminOptions.this);
        dialogMaster.setContentView(R.layout.activity_master_admin_pass);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogMaster.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogMaster.getWindow().setAttributes(lp);

        // set the custom dialog components - text, image and button

        customtextView = (TextView) dialogMaster.findViewById(R.id.emailText);
        customtextView.setText("Master Admin of " + GlobalClass.tempBusName);
        customeditText = (EditText) dialogMaster.findViewById(R.id.mpasswordText);

        Button loginButton = (Button) dialogMaster.findViewById(R.id.mlogButton);
        SharedPreferences masterpref = getSharedPreferences(getString(R.string.master_pref_file), MODE_PRIVATE);

        customPass = masterpref.getString(GlobalClass.tempBusName, "sakib2522");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toastIconInfo("Verification processing...");

                checkMasterPassword();

            }
        });

        dialogMaster.show();
    }

    private void checkMasterPassword() {

        masterListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot!=null)
                {
                    if(dataSnapshot.toString()!=null)
                    {


                        if (customeditText.getText().toString().equals(dataSnapshot.getValue().toString())) {

                            final Handler handler22 = new Handler();
                            handler22.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    dialogMaster.dismiss();

                                    startActivity(new Intent(AdminOptions.this, SettingsPage.class));

                                }
                            }, 2000);

                        } else {

                            toastIconInfo("Invalid password");
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        masterReference.child(GlobalClass.tempBusName).addListenerForSingleValueEvent(masterListener);


    }

    private void passchange() {

        passChange_reference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_password));
        pass_change = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot inner : ds.getChildren()) {
                        Random rand = new Random();

                        int randomInt = rand.nextInt((9999 - 1000) + 1);
                        inner.getRef().setValue(String.valueOf(randomInt));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        passChange_reference.addValueEventListener(pass_change);

    }

    private void timePopulate(ArrayList timeTable) {

        ArrayAdapter<String> spinnerArrayAdapterTime = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        timeTable);
        spinnerArrayAdapterTime.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerTime.setAdapter(spinnerArrayAdapterTime);

        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                GlobalClass.tempBustime = item.toString();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void onClick(View v) {

        long before_time_limit= GlobalClass.admin_aage_pref.getLong(getString(R.string.admin_aage_getter),-1);
        long after_time_limit=GlobalClass.admin_pore_pref.getLong(getString(R.string.admin_pore_getter),-1);

        GlobalClass.p=true;
        pore=false;
        difference=0;

        if(!isNetworkConnected(this))
        {
            toastIconInfo("Internet connection required");
            return;
        }
        if(GlobalClass.tempBustime.matches("(.*)down(.*)"))
        {
            StringTokenizer pp = new StringTokenizer(GlobalClass.tempBustime, " ");
            tempo = (String) pp.nextElement();

            StringTokenizer stx = new StringTokenizer(tempo, ":");
            int a = Integer.parseInt((String) stx.nextElement());
            int b = Integer.parseInt((String) stx.nextElement());
            if (a != 12)
                a += 12;

            StringTokenizer sty = new StringTokenizer(GlobalClass.timeStampTime, ":");
            int c = Integer.parseInt((String) sty.nextElement());
            int d = Integer.parseInt((String) sty.nextElement());

            calculate(a,b,c,d);

        }

        else if(GlobalClass.tempBustime.matches("(.*)up(.*)"))


        {
            StringTokenizer pp = new StringTokenizer(GlobalClass.tempBustime, " ");
            tempo = (String) pp.nextElement();

            StringTokenizer stx = new StringTokenizer(tempo, ":");
            int a = Integer.parseInt((String) stx.nextElement());
            int b = Integer.parseInt((String) stx.nextElement());

            StringTokenizer sty = new StringTokenizer(GlobalClass.timeStampTime, ":");
            int c = Integer.parseInt((String) sty.nextElement());
            int d = Integer.parseInt((String) sty.nextElement());

            calculate(a,b,c,d);

        }

    if(difference>before_time_limit && !pore)
        {
            toastIconInfo("Enter before "+String.valueOf(before_time_limit) +" minutes of a trip");
            return ;
        }

        else if (difference>after_time_limit && pore)
    {
        toastIconInfo("Time of this trip is already over");
        return ;
    }

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Checking log in status...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            loc = FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_admin))
                    .child("Location").child(GlobalClass.tempBusName)
                    .child(GlobalClass.tempBustime).child("active_on");
            val_loc = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        if (dataSnapshot.getValue().toString().equals("on") && GlobalClass.p) {
                            progressDialog.dismiss();
                            showAlert();
                        } else if (GlobalClass.p) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (progressDialog != null)
                                        progressDialog.dismiss();


                                    if (passChange_reference != null && pass_change != null)
                                        passChange_reference.removeEventListener(pass_change);
                                    if (roster != null && val_roster != null)
                                        roster.removeEventListener(val_roster);
                                    if (loc != null && val_loc != null)
                                        loc.removeEventListener(val_loc);

                                    GlobalClass.p = false;
                                    if(GlobalClass.adminHoise)
                                    {
                                        open_profile_otp();
                                    }
                                    else
                                    {
                                        dowork();
                                        Intent intent = new Intent(AdminOptions.this, AdminLoginPage.class);
                                        startActivity(intent);
                                        finish();
                                    }



                                }
                            }, 2000);
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            loc.addValueEventListener(val_loc);






    }

    private void dowork() {

        if(dateReference!=null && val_date!=null)
            dateReference.removeEventListener(val_date);
        if(loc!=null && val_loc!=null)
            loc.removeEventListener(val_loc);
        if(roster!=null && val_roster!=null)
            roster.removeEventListener(val_roster);
    }

    private void calculate(int a, int b, int c, int d) {
        if(c>a)
        {
            pore=true;
            int temp1=a,temp2=b;
            a=c;
            c=temp1;
            b=d;
            d=temp2;
        }

        if(c==a)
        {
            if(d>b)
            {
                pore=true;
                int temp2=b;
                b=d;
                d=temp2;
            }
        }

        if(!pore)
        {
            if(b<d)
            {
                b+=60;
                a-=1;
            }
            difference+=(b-d);
            difference+=((a-c)*60);

        }

        if(pore)
        {
            if(b<d)
            {
                b+=60;
                a-=1;
            }
            difference+=(b-d);
            difference+=((a-c)*60);
        }




    }

    private void open_profile_otp() {


        dialogPassword = new Dialog(AdminOptions.this);
        dialogPassword.setContentView(R.layout.profile_settings_and_password);






        busNumberText=dialogPassword.findViewById(R.id.busNumberText);

        busNumberText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {
                s=busNumberText.getText().toString().trim();

                if(s.length()==2 && !s.matches("(.*)-(.*)") )
              {
                  busNumberText.setText(s+"-");
                  busNumberText.setSelection(3);
              }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        password = dialogPassword.findViewById(R.id.passwordText);
        header = dialogPassword.findViewById(R.id.emailText23);

        header.setText(GlobalClass.tempBusName+" "+GlobalClass.tempBustime);


        SharedPreferences passref = getSharedPreferences(getString(R.string.pass_pref_file), MODE_PRIVATE);


        passwordReference=FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_password)).child(GlobalClass.tempBusName).child("1")
                .child(GlobalClass.tempBustime);

        Button final_login = dialogPassword.findViewById(R.id.final_logButton);

        final_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(s.length()<5)
                {
                    toastIconInfo("Bus number is not valid");
                    return;
                }


                else
                {

                    if(isNetworkConnected(AdminOptions.this))
                    {
                        checkpassword();
                        toastIconInfo("Verification processing...");
                    }
                    else
                    {
                        toastIconInfo("Internet connection required");

                    }


//                    if (password.getText().toString().trim().equals(pass)) {
//
//                        SharedPreferences busNumber=getSharedPreferences("helloBusNumber",MODE_PRIVATE);
//                        SharedPreferences.Editor ed22= busNumber.edit();
//                        ed22.putString("BusNumber",busNumberText.getText().toString().trim());
//                        ed22.apply();
//
//
//                        dialog.dismiss();
//                        dialog.cancel();
//                        dowork();
//                        startActivity(new Intent(AdminOptions.this, OTPactivity.class));
//                        // finish();
//
//                    } else {
//                        toastIconInfo("Invalid password");
//                        return;
//                    }
                }


            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogPassword.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogPassword.getWindow().setAttributes(lp);


        dialogPassword.setCanceledOnTouchOutside(false);
        dialogPassword.show();
    }

    private void checkpassword() {


        passwordListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot!=null)
                {

                    if(dataSnapshot.toString()!=null)
                    {


                        if (password.getText().toString().trim().equals(dataSnapshot.getValue().toString())) {


                            final Handler handler22 = new Handler();
                            handler22.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    SharedPreferences busNumber=getSharedPreferences("helloBusNumber",MODE_PRIVATE);
                                    SharedPreferences.Editor ed22= busNumber.edit();
                                    ed22.putString("BusNumber",busNumberText.getText().toString().trim());
                                    ed22.apply();


                                    dialogPassword.dismiss();
                                    dialogPassword.cancel();
                                    dowork();
                                    startActivity(new Intent(AdminOptions.this, OTPactivity.class));
                                }
                            }, 2000);

                        } else {

                            toastIconInfo("Invalid password");
                            return;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        passwordReference.addListenerForSingleValueEvent(passwordListener);
    }

    private void showAlert() {

        roster = FirebaseDatabase.getInstance().getReference().child(getString(R.string.Firebase_adminRoster))
                .child(ajkerDate).child(GlobalClass.tempBusName).child(GlobalClass.tempBustime);

        val_roster = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminOptions.this);

                    builder.setTitle("Already logged in !!");

                    builder.setMessage("Mob: " + dataSnapshot.child("MobileNo").getValue());

                    builder.setNegativeButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    if(builder!=null)
                    builder.show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        roster.addValueEventListener(val_roster);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}