package com.anything.asus.remindmemyself;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class MasterAdminPass extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private EditText  password;
    private TextView  textView;
    private Button loginButton;
    private ProgressDialog progressDialog;
    private SharedPreferences masterpref,busnamePref;

    private static String pass,busname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_admin_pass);
        masterpref = getSharedPreferences(getString(R.string.master_pref_file), MODE_PRIVATE);
        busnamePref = getSharedPreferences(getString(R.string.bus_name_pref_file), MODE_PRIVATE);
        busname=busnamePref.getString(getString(R.string.bus_name_getter),"noBusName");



        textView = (TextView) findViewById(R.id.emailText);
        textView.setText("Master Admin of " +busname);
        password = (EditText) findViewById(R.id.mpasswordText);

        loginButton = (Button) findViewById(R.id.mlogButton);

        pass=masterpref.getString(GlobalClass.BusName,"sakib2522");


        progressDialog = new ProgressDialog(this);


        loginButton.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        // mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onClick(View v) {

        if (v == loginButton) {




            if(password.getText().toString().equals(pass))
            {
                startActivity(new Intent(MasterAdminPass.this,SettingsPage.class));
                finish();
            }

            else
            {
                toastIconInfo("Invalid password");
                return ;
            }

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,StartActiviy.class));
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

}
