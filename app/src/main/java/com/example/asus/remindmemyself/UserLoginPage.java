package com.example.asus.remindmemyself;

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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class UserLoginPage extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private EditText phone_number,verification_code;
    private Button resendButton,sendButton;
    String phoneNumber="",actualcode="";
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=null;
    PhoneAuthCredential credential=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_page);
        mAuth= FirebaseAuth.getInstance();
        Log.d("jobaid","userLoginPage:onCreate");
        phone_number= (EditText)findViewById(R.id.emailText);
        verification_code=(EditText)findViewById(R.id.passwordText);
        sendButton=(Button)findViewById(R.id.logButton);
        resendButton=(Button)findViewById(R.id.regButton);
//        firebaseAuthListener= new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
//                if(fUser!=null)
//                {
//                    Toast.makeText(UserLoginPage.this,fUser.toString(),Toast.LENGTH_LONG).show();
//                    Intent intent= new Intent(UserLoginPage.this,firstActivity.class);
//                    intent.putExtra("name","user");
//                    intent.putExtra("BUSNAME","BUS");
//                    intent.putExtra("TIME","TIME");
//                    startActivity(intent);
//                    finish();
//                    return ;
//                }
//            }
//        };

        mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d("mc",phoneAuthCredential.toString());
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d("mc",e.getMessage());

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.d("mc",s);

                actualcode=s;
            }
        };

        sendButton.setOnClickListener( this);
        resendButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onClick(View v) {
        if(v==sendButton)
        {


            phoneNumber = phone_number.getText().toString();
            Toast.makeText(this,phoneNumber,Toast.LENGTH_LONG).show();

            if(phoneNumber.length()<11)
            {
                Toast.makeText(this,"Phone number is not valid",Toast.LENGTH_LONG).show();
                return;
            }
            sendButton.setText("VERIFY");


            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    120,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
//            final String phone_numberString= phone_number.getText().toString().trim();
//            final String verification_codeString= verification_code.getText().toString().trim();
//            mAuth.createUserWithEmailAndPassword(phone_numberString,verification_codeString).addOnCompleteListener(UserLoginPage.this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(!task.isSuccessful())
//                    {
//                        Toast.makeText(UserLoginPage.this,"Network connection required",Toast.LENGTH_LONG).show();
//                    }
//                    else if(task.getException() instanceof FirebaseAuthUserCollisionException)
//                    {
//                        Toast.makeText(UserLoginPage.this,"This ID is already in use",Toast.LENGTH_LONG).show();
//
//                    }
//                    else
//                    {
//                        StartActiviy.st.finish();
//                        FirebaseMessaging.getInstance().subscribeToTopic("joker")
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        String msg = "message is subsribed";
//                                        if (!task.isSuccessful()) {
//                                            msg = "subsciption failed";
//                                        }
//                                        Log.d("tokesss", msg);
//                                        Toast.makeText(UserLoginPage.this, msg, Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//                        FirebaseInstanceId.getInstance().getInstanceId()
//                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                                        if (!task.isSuccessful()) {
//                                            Log.w("jobaid", "getInstanceId failed", task.getException());
//                                            return;
//                                        }
//
//                                        // Get new Instance ID token
//                                        String token = task.getResult().getToken();
//                                        Log.d("tokesss",token);
//
//                                        // Log and toast
//                                        String msg = getString(R.string.msg_token_fmt, token);
//                                        Log.d("jobaid", msg);
//                                        Toast.makeText(UserLoginPage.this, msg, Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                    }
//
//
//                }
//            });
//
        }


        if(v==resendButton)
        {
//            final String phone_numberString= phone_number.getText().toString().trim();
//            final String verification_codeString= verification_code.getText().toString().trim();
//
//            if(phone_numberString!=null && verification_codeString.length()>=6)
//            {
//                mAuth.signInWithEmailAndPassword(phone_numberString,verification_codeString).addOnCompleteListener(UserLoginPage.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        if(!task.isSuccessful())
//                        {
//                            Toast.makeText(UserLoginPage.this,"login error",Toast.LENGTH_LONG).show();
//                        }
//                        else
//                        {
//                            StartActiviy.st.finish();
//
//                        }
//                    }
//                });
//            }
//            else
//            {
//                Toast.makeText(UserLoginPage.this,"Fill informations",Toast.LENGTH_LONG).show();
//
//            }
            String usercode= verification_code.getText().toString();
            credential = PhoneAuthProvider.getCredential(actualcode, usercode);
            signInWithPhoneAuthCredential(credential);


        }


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("phoneAuth", "signInWithCredential:success");



                        } else {

                            Log.w("phoneAuth", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(UserLoginPage.this,"The verification code entered was invalid",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,StartActiviy.class));
    }
}
