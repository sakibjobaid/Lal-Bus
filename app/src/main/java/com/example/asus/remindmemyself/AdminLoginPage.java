package com.example.asus.remindmemyself;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginPage extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private EditText  password;
    private TextView  textView;
    private Button loginButton, RegButton;
    private ProgressDialog progressDialog;
    private SharedPreferences passref;
    private static String emailString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_page);
        mAuth = FirebaseAuth.getInstance();
        textView = (TextView) findViewById(R.id.emailText);
        textView.setText("Admin of "+GlobalClass.BusName+" "+GlobalClass.BusTime);
        password = (EditText) findViewById(R.id.passwordText);
        loginButton = (Button) findViewById(R.id.logButton);

        progressDialog = new ProgressDialog(this);
        passref = getSharedPreferences("hellopassword", MODE_PRIVATE);
//        if (mAuth.getCurrentUser()== null )
//            Toast.makeText(this, "no current user", Toast.LENGTH_LONG).show();
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
//        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getEmail().equals("admintaranga@gmail.com")) {
//            finish();
//            Intent intent = new Intent(AdminLoginPage.this, AdminOptions.class);
//            intent.putExtra("name", "admin");
//            intent.putExtra("BUSNAME","Taranga");
//            intent.putExtra("TIME","7:00 up");
//            startActivity(intent);
//        }

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
//        if (v == RegButton) {
//            //emailString = email.getText().toString().trim();
//            GlobalClass.useremail=emailString;
//            passwordString = password.getText().toString().trim();
//
//            if (emailString.equals("admin"+GlobalClass.BusName.toLowerCase()+"@gmail.com") && passwordString.equals(passref.getString(GlobalClass.BusName,"sakib2522"))) {
//
//                progressDialog.setMessage("Registering user");
//                progressDialog.show();
//                mAuth.createUserWithEmailAndPassword(emailString, passwordString)
//                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                progressDialog.dismiss();
//
//                                if(task.getException() instanceof FirebaseAuthUserCollisionException)
//                                {
//                                    Toast.makeText(AdminLoginPage.this,"This ID is already in use",Toast.LENGTH_LONG).show();
//                                     return;
//                                }
//                                else if (!task.isSuccessful()) {
//                                    Toast.makeText(AdminLoginPage.this, "registration error", Toast.LENGTH_LONG).show();
//                                }
//
////                                else
////                                    {
////                                    finish();
////                                    startActivity(new Intent(AdminLoginPage.this, testing.class));
////                                    Toast.makeText(AdminLoginPage.this, "registration successful", Toast.LENGTH_LONG).show();
//
////                         String userId = mAuth.getCurrentUser().getUid();
////                         DatabaseReference currentUserRef= FirebaseDatabase.getInstance().getReference().child("Admin").child(userId);
////                         currentUserRef.setValue(true);
//  //                              }
//
//                            }
//                        });
//
//
//                FirebaseMessaging.getInstance().subscribeToTopic("joker")
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                String msg = "message is subsribed";
//                                if(task.getException() instanceof FirebaseAuthUserCollisionException)
//                                {
//                                    Toast.makeText(AdminLoginPage.this,"This ID is already in use",Toast.LENGTH_LONG).show();
//                                     return ;
//                                }
//                                if (!task.isSuccessful()) {
//                                    msg = "subsciption failed";
//                                }
//
//                            }
//                        });
//            }
//
//            else
//            {
//                Toast.makeText(this,"Your credentials are not valid. Try again!",Toast.LENGTH_LONG).show();
//            }
//
//
////             .addOnFailureListener(new OnFailureListener() {
////                 @Override
////                 public void onFailure(@NonNull Exception e) {
////                     Toast.makeText(AdminLoginPage.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
////                 }
////             });
//
//        }
        if (v == loginButton) {


            Log.d("vaivai",GlobalClass.BusName);
            Log.d("vaivai",passref.getString(GlobalClass.BusName,"sakib2522"));
            Log.d("vaivai",password.getText().toString());

            if(password.getText().toString().equals(passref.getString(GlobalClass.BusName,"sakib2522")))
            {
                Intent intent = new Intent(AdminLoginPage.this, firstActivity.class);
                intent.putExtra("BUSNAME",GlobalClass.BusName);
                intent.putExtra("TIME",GlobalClass.BusTime);
                intent.putExtra("name","admin");
                startActivity(intent);
                finish();
            }

            else
            {
                Toast.makeText(AdminLoginPage.this,"Your password is invalid",Toast.LENGTH_LONG).show();
                return ;
            }


//            //final String emailString = email.getText().toString();
//            GlobalClass.useremail=emailString;
//            final String passwordString = password.getText().toString();
//
//
//            progressDialog.setMessage("Logging in ");
//            progressDialog.show();
//
//            mAuth = FirebaseAuth.getInstance();
//
//            mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(AdminLoginPage.this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                    progressDialog.dismiss();
//                    Log.d("trimmer", "sakib44");
//
//                    if (!task.isSuccessful()) {
//                        Toast.makeText(AdminLoginPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//
//
//                    else {
//
//                    }
//                }
//            });

        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,StartActiviy.class));
    }
}
