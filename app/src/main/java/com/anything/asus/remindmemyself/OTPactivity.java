package com.anything.asus.remindmemyself;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OTPactivity extends AppCompatActivity {

    //region variables
    Button btnGenerateOTP, btnSignIn;
    EditText etPhoneNumber, etOTP;
    String phoneNumber, otp;
    private String ajkerDate, finalphoneNumber;
    FirebaseAuth auth;
    public static DatabaseReference loc = null, roster = null, admin_profile = null,
            admin_history_ref = null, admin_list = null,dateReference=null;
    ValueEventListener val_roster = null, val_loc = null,val_date=null;
    public static ValueEventListener val_admin_history = null;
    public static boolean check = true, trip_increment = false, up_increment = false,
            down_increment = false,OTPvisited=false;
    private static int buildershow = 0;
    public SharedPreferences phonePref, busNamePref, busTimePref, datePref, pic_show, fb_show, mail_show;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    public static SharedPreferences contact_show;
    private String verificationCode;
    AlertDialog.Builder builder;
    public static DatabaseReference feedReference;


    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_activity);

        findViews();
        StartFirebaseLogin();


        firstchecking();


        pic_show = getSharedPreferences(getString(R.string.pic_show_pref_file), MODE_PRIVATE);
        fb_show = getSharedPreferences(getString(R.string.fb_show_pref_file), MODE_PRIVATE);
        contact_show = getSharedPreferences(getString(R.string.contact_show_pref_file), MODE_PRIVATE);
        mail_show = getSharedPreferences(getString(R.string.mail_show_pref_file), MODE_PRIVATE);

        admin_list = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_admin_list));

        if (GlobalClass.adminHoise) {
            etPhoneNumber.setEnabled(false);
            etPhoneNumber.setFocusable(false);
            etPhoneNumber.setText(GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter), ""));
            etPhoneNumber.setTextColor(Color.BLACK);
            etPhoneNumber.setTextSize(15);
        }

        if(isNetworkConnected(this))
        {
            //region date
            dateReference = FirebaseDatabase.getInstance().getReference()
                    .child(getString(R.string.Firebase_date));
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


        datePref = getSharedPreferences(getString(R.string.date_pref_file), MODE_PRIVATE);
        phonePref = getSharedPreferences(getString(R.string.phone_pref_file), MODE_PRIVATE);
        busNamePref = getSharedPreferences(getString(R.string.bus_name_pref_file), MODE_PRIVATE);
        busTimePref = getSharedPreferences(getString(R.string.bus_time_pref_file), MODE_PRIVATE);


        ajkerDate = datePref.getString(getString(R.string.date_getter), "nothing");

        btnGenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isNetworkConnected(OTPactivity.this))
                {
                    toastIconInfo("Internet connection required");
                    return;

                }

                else
                {
                    if (GlobalClass.adminHoise)
                        finalphoneNumber = GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter), "");

                    finalphoneNumber = etPhoneNumber.getText().toString();
                    phoneNumber = "+88" + etPhoneNumber.getText().toString();
                    btnGenerateOTP.setText("RESEND CODE");
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,                     // Phone number to verify
                            120,                           // Timeout duration
                            TimeUnit.SECONDS,                // Unit of timeout
                            OTPactivity.this,        // Activity (for callback binding)
                            mCallback);                      // OnVerificationStateChangedCallbacks
                }

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otp = etOTP.getText().toString();

                if(!isNetworkConnected(OTPactivity.this))
                {
                    toastIconInfo("Internet connection required");
                    return ;
                }

                else
                {
                    if (otp.isEmpty()) {
                        toastIconInfo("Enter verification code");
                        return;
                    }

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                    SigninWithPhone(credential);
                }


            }
        });
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

    private void firstchecking() {

        loc = FirebaseDatabase.getInstance().getReference().
                child(getString(R.string.Firebase_admin))
                .child("Location").child(GlobalClass.tempBusName)
                .child(GlobalClass.tempBustime).child("active_on");
        val_loc = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    if (dataSnapshot.getValue().toString().equals("on")) {
                        showAlert();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        loc.addValueEventListener(val_loc);

    }

    private void showAlert() {

        roster = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_adminRoster))
                .child(ajkerDate).child(GlobalClass.tempBusName).child(GlobalClass.tempBustime);

        val_roster = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    builder = new AlertDialog.Builder(OTPactivity.this);

                    builder.setTitle("Already logged in by this time !!");

                    builder.setMessage("Mob: " + dataSnapshot.child("MobileNo").getValue());

                    builder.setNegativeButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (loc != null && val_loc != null)
                                loc.removeEventListener(val_loc);
                            if (roster != null && val_roster != null)
                                roster.removeEventListener(val_roster);
                            //startActivity(new Intent(OTPactivity.this, StartActiviy.class));
                            finish();

                        }
                    });

                    builder.setCancelable(false);

                    if (buildershow != 2522)
                        builder.show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        roster.addValueEventListener(val_roster);


    }

    private void SigninWithPhone(PhoneAuthCredential credential) {


        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            toastIconInfo("Verification successful");

                            //region adminname & dept assign
                            if (GlobalClass.adminHoise) {
                                GlobalClass.adminName = GlobalClass.NamePref.getString(getString(R.string.profile_name_getter), "Not provided");
                                GlobalClass.adminDepartment = GlobalClass.DeptPref.getString(getString(R.string.dept_name_getter), "Not provided");
                            }
                            //endregion

                            //region sharedprefs default
                            GlobalClass.BusName = GlobalClass.tempBusName;
                            GlobalClass.BusTime = GlobalClass.tempBustime;
                            SharedPreferences.Editor preferencesEditor = phonePref.edit();
                            preferencesEditor.putString(getString(R.string.phone_number_getter), phoneNumber);
                            preferencesEditor.apply();
                            SharedPreferences.Editor preferenceEditor = busNamePref.edit();
                            preferenceEditor.putString(getString(R.string.bus_name_getter), GlobalClass.BusName);
                            preferenceEditor.apply();
                            SharedPreferences.Editor preEditor = busTimePref.edit();
                            preEditor.putString(getString(R.string.bus_time_getter), GlobalClass.BusTime);
                            preEditor.apply();

                            SharedPreferences NewAdmin = getSharedPreferences(getString(R.string.adminHoise_pref_file), MODE_PRIVATE);
                            SharedPreferences.Editor NewAdminEditor = NewAdmin.edit();
                            NewAdminEditor.putString(getString(R.string.adminHoise_getter), "Yes");
                            NewAdminEditor.apply();
                            //endregion

                            //region Admin Roster setting up in db
                            SharedPreferences datePref;
                            datePref = getSharedPreferences(getString(R.string.date_pref_file), MODE_PRIVATE);
                            String date = datePref.getString(getString(R.string.date_getter), "sakib25");
                            DatabaseReference firebaseDatabase = FirebaseDatabase.
                                    getInstance().getReference().child(getString(R.string.Firebase_adminRoster));


                            firebaseDatabase.child(date).child(GlobalClass.BusName)
                                    .child(GlobalClass.BusTime).child("Department").setValue(GlobalClass.adminDepartment);
                            firebaseDatabase.child(date).child(GlobalClass.BusName)
                                    .child(GlobalClass.BusTime).child("Name").setValue(GlobalClass.adminName);
                            firebaseDatabase.child(date).child(GlobalClass.BusName)
                                    .child(GlobalClass.BusTime).child("MobileNo").setValue(finalphoneNumber.toString().trim());
                            firebaseDatabase.child(date).child(GlobalClass.BusName)
                                    .child(GlobalClass.BusTime).child("Status").setValue("Running");

                            //endregion

                            //region putting admin_profile in sharedpreference

                            GlobalClass.DeptEditor.putString(getString(R.string.dept_name_getter), GlobalClass.adminDepartment);
                            GlobalClass.DeptEditor.apply();

                            GlobalClass.NameEditor.putString(getString(R.string.profile_name_getter), GlobalClass.adminName);
                            GlobalClass.NameEditor.apply();

                            GlobalClass.feedContactPref=getSharedPreferences("feedContact",MODE_PRIVATE);
                            SharedPreferences.Editor ed1=GlobalClass.feedContactPref.edit();
                            ed1.putString("feedContact",finalphoneNumber.toString().trim());
                            ed1.apply();

                            GlobalClass.ContactEditor.putString(getString(R.string.contact_name_getter), finalphoneNumber.toString().trim());
                            GlobalClass.ContactEditor.apply();

                            if (GlobalClass.AdminVirgin) {

                                admin_list.child(GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter), ""))
                                        .setValue(GlobalClass.BusName);

                                SharedPreferences picStringinfo = getSharedPreferences("picStringinfo",MODE_PRIVATE);
                                SharedPreferences.Editor editor=picStringinfo.edit();
                                editor.putString("picStringinfo","Not provided");
                                editor.apply();


                                GlobalClass.FbEditor.putString(getString(R.string.fb_name_getter), "Not provided");
                                GlobalClass.FbEditor.apply();

                                GlobalClass.MailEditor.putString(getString(R.string.mail_name_getter), "Not provided");
                                GlobalClass.MailEditor.apply();

                                GlobalClass.PicStringEditor.putString(getString(R.string.profile_pic_name_getter), "Not provided");
                                GlobalClass.PicStringEditor.apply();

                                //region show/hide permission pref
                                SharedPreferences.Editor mailEditor = mail_show.edit();
                                mailEditor.putString("mail_show_name", "false");
                                mailEditor.apply();

                                SharedPreferences.Editor picEditor = pic_show.edit();
                                picEditor.putString("pic_show_name", "false");
                                picEditor.apply();

                                SharedPreferences.Editor contactEditor = contact_show.edit();
                                contactEditor.putString(getString(R.string.contact_show_name_getter), "false");
                                contactEditor.apply();

                                SharedPreferences.Editor fbEditor = fb_show.edit();
                                fbEditor.putString("fb_show_name", "false");
                                fbEditor.apply();
                                //endregion


                            }


                            feedReference = FirebaseDatabase.getInstance().getReference()
                                    .child(getString(R.string.Firebase_admins_feedback)).child(GlobalClass.ContactPref.getString("contact_name", ""));


                            admin_profile = FirebaseDatabase.getInstance().getReference()
                                    .child(getString(R.string.Firebase_admin_profile)).child(GlobalClass.BusName)
                                    .child(GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter), ""));

                            admin_history_ref = FirebaseDatabase.getInstance().getReference()
                                    .child(getString(R.string.Firebase_admin_history))
                                    .child(GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter), ""));




                            OTPvisited=true;


                            //endregion

                            SharedPreferences admins_busname=getSharedPreferences("admins_busname",MODE_PRIVATE);
                            int count_no=admins_busname.getInt("admins_busname"+GlobalClass.BusName,0);
                            SharedPreferences.Editor ed44= admins_busname.edit();
                            ed44.putInt("admins_busname"+GlobalClass.BusName,count_no+1);
                            ed44.apply();


                            buildershow = 2522;
                            trip_increment = true;
                            up_increment = true;
                            down_increment = true;

                            //region intent start
                            Intent intent = new Intent(OTPactivity.this, firstActivity.class);
                            intent.putExtra("BUSNAME", GlobalClass.BusName);
                            intent.putExtra("TIME", GlobalClass.BusTime);
                            intent.putExtra("name", "admin");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            dowork();
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.admin_from_left, R.anim.admin_from_right);

                            //endregion

                        } else {

                            toastIconInfo("OTP not successful");
                        }
                    }
                });
    }

    private void dowork() {

        if (val_roster != null && roster != null)
            roster.removeEventListener(val_roster);
        if (val_loc != null && loc != null)
            loc.removeEventListener(val_loc);

    }

    private void findViews() {

        btnGenerateOTP = findViewById(R.id.btn_generate_otp);
        btnSignIn = findViewById(R.id.btn_sign_in);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etOTP = findViewById(R.id.et_otp);

    }

    private void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                if (check) {
                    SigninWithPhone(phoneAuthCredential);
                }
                toastIconInfo("OTP generated");
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                toastIconInfo(e.getMessage());

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    //mPhoneNumberField.setError("Invalid phone number.");
                    toastIconInfo("Invalid phone number");

                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    toastIconInfo("SMS Quota exceeded");


                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {


                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                check = false;
                toastIconInfo("Wait for the OTP");
            }
        };
    }


    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder = new AlertDialog.Builder(OTPactivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage("Do you want to quit this OTP session?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //startActivity(new Intent(OTPactivity.this, StartActiviy.class));
                finish();

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
}
