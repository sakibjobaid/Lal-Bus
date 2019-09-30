package com.anything.asus.remindmemyself;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdminLoginPage extends AppCompatActivity implements View.OnClickListener {


    //region variables
    private FirebaseAuth mAuth;
    private EditText name, department;
    private TextView textView;
    private Button loginButton, RegButton;
    private ProgressDialog progressDialog;
    private SharedPreferences passref;
    private static String pass;
    LinearLayout adminhoise;

    String s;
    private DatabaseReference passwordReference=null;
    private ValueEventListener passwordListener=null;

    private EditText password,busNumberText;
    private Dialog dialogPassword;

    // endregion



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_login_page);
        passref = getSharedPreferences(getString(R.string.pass_pref_file), MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        textView = (TextView) findViewById(R.id.emailText);
        textView.setText("Admin of " + GlobalClass.tempBusName + " " + GlobalClass.tempBustime);
        name = (EditText) findViewById(R.id.nameText);
        department = (EditText) findViewById(R.id.departmentText);
        loginButton = (Button) findViewById(R.id.logButton);


        progressDialog = new ProgressDialog(this);
        loginButton.setOnClickListener(this);




        GlobalClass.NamePref=getSharedPreferences(getString(R.string.globalclass_profile_name_pref_file),MODE_PRIVATE);
        GlobalClass.DeptPref=getSharedPreferences(getString(R.string.globalclass_dept_name_pref_file),MODE_PRIVATE);

        GlobalClass.NameEditor=GlobalClass.NamePref.edit();
        GlobalClass.DeptEditor=GlobalClass.DeptPref.edit();



    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {

        if (v == loginButton ) {


            if (department.getText().toString().trim().length() == 0 || name.getText().toString().trim().length() == 0) {
                toastIconInfo("Your data field is empty");
                return;
            }

            else
            {
                GlobalClass.NameEditor.putString(getString(R.string.profile_name_getter),name.getText().toString().trim());
                GlobalClass.NameEditor.apply();
                GlobalClass.DeptEditor.putString(getString(R.string.dept_name_getter),department.getText().toString().trim());
                GlobalClass.DeptEditor.apply();
                GlobalClass.adminDepartment = department.getText().toString().trim();
                GlobalClass.adminName = name.getText().toString().trim();
                open_profile_otp();
            }

        }
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


    private void open_profile_otp() {

        dialogPassword = new Dialog(AdminLoginPage.this);
        dialogPassword.setContentView(R.layout.profile_settings_and_password);


        final TextView header;




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
        header=dialogPassword.findViewById(R.id.emailText23);

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


                    if(isNetworkConnected(AdminLoginPage.this))
                    {
                        checkpassword();
                        toastIconInfo("Verification processing...");
                    }
                    else
                    {
                        toastIconInfo("Internet connection required");

                    }


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
                                    ed22.putString("BusNumber",s);
                                    ed22.apply();


                                    dialogPassword.dismiss();
                                    dialogPassword.cancel();
                                    dowork();
                                    startActivity(new Intent(AdminLoginPage.this, OTPactivity.class));
                                    // finish();


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

    private void dowork() {
        if(StartActiviy.admin_list_ref!=null && StartActiviy.val_admin_list_ref!=null)
            StartActiviy.admin_list_ref.removeEventListener(StartActiviy.val_admin_list_ref);
    }

    @Override
    public void onBackPressed() {

        if(dialogPassword!=null)
        {
            if(dialogPassword.isShowing())
            {
                dialogPassword.dismiss();
                dialogPassword.cancel();
                return;
            }
            else super.onBackPressed();

        }

        else super.onBackPressed();

    }
}
