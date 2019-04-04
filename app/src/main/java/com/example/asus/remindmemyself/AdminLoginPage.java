package com.example.asus.remindmemyself;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginPage extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private EditText email, password;
    private Button loginButton, RegButton;
    private ProgressDialog progressDialog;
    private static String emailString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_page);
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.emailText);
        password = (EditText) findViewById(R.id.passwordText);
        loginButton = (Button) findViewById(R.id.logButton);
        RegButton = (Button) findViewById(R.id.regButton);
        progressDialog = new ProgressDialog(this);
        if (mAuth.getCurrentUser()== null )
            Toast.makeText(this, "no current user", Toast.LENGTH_LONG).show();
//        firebaseAuthListener= new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
//                if(fUser!=null)
//                {
//                    Toast.makeText(AdminLoginPage.this,"vitor",Toast.LENGTH_LONG).show();
//
//                    Intent intent= new Intent(AdminLoginPage.this,testing.class);
//                    //intent.putExtra("name","admin");
//                    startActivity(intent);
//                    finish();
//                    return ;
//                }
//            }
//        };
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getEmail().equals("admintaranga@gmail.com")) {
            finish();
            Intent intent = new Intent(AdminLoginPage.this, AdminOptions.class);
            intent.putExtra("name", "admin");
            intent.putExtra("BUSNAME","Taranga");
            intent.putExtra("TIME","7:00 up");
            startActivity(intent);
        }
        RegButton.setOnClickListener(this);
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
        if (v == RegButton) {
            emailString = email.getText().toString().trim();
            passwordString = password.getText().toString().trim();
            if (passwordString.length() < 6) {
                Toast.makeText(AdminLoginPage.this, "password must be at least 6 characters length", Toast.LENGTH_LONG).show();
                return;
            }
            if (emailString.equals("admintaranga@gmail.com") && passwordString.equals("tarangabusdu")) {

                progressDialog.setMessage("Registering user");
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(emailString, passwordString)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();

                                if (!task.isSuccessful()) {
                                    Toast.makeText(AdminLoginPage.this, "registration error", Toast.LENGTH_LONG).show();
                                } else {
                                    finish();
                                    startActivity(new Intent(AdminLoginPage.this, testing.class));
                                    Toast.makeText(AdminLoginPage.this, "registration successful", Toast.LENGTH_LONG).show();

//                         String userId = mAuth.getCurrentUser().getUid();
//                         DatabaseReference currentUserRef= FirebaseDatabase.getInstance().getReference().child("Admin").child(userId);
//                         currentUserRef.setValue(true);
                                }

                            }
                        });
            }

            else
            {
                Toast.makeText(this,"invalid information. Enter again.",Toast.LENGTH_LONG).show();
            }


//             .addOnFailureListener(new OnFailureListener() {
//                 @Override
//                 public void onFailure(@NonNull Exception e) {
//                     Toast.makeText(AdminLoginPage.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
//                 }
//             });

        }
        if (v == loginButton) {


            final String emailString = email.getText().toString();
            final String passwordString = password.getText().toString();


            progressDialog.setMessage("Logging in ");
            progressDialog.show();

            mAuth = FirebaseAuth.getInstance();

            mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(AdminLoginPage.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    progressDialog.dismiss();
                    Log.d("trimmer", "sakib44");

                    if (!task.isSuccessful()) {
                        Toast.makeText(AdminLoginPage.this, "login error", Toast.LENGTH_LONG).show();
                    } else {
                        finish();
                        Intent intent = new Intent(AdminLoginPage.this, AdminOptions.class);
                        startActivity(intent);
                    }
                }
            });

        }
    }
}
