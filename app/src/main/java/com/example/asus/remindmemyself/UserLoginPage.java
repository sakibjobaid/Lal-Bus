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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserLoginPage extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private EditText email,password;
    private Button loginButton,RegButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_page);
        mAuth= FirebaseAuth.getInstance();
        Log.d("jobaid","userLoginPage:onCreate");
        email= (EditText)findViewById(R.id.emailText);
        password=(EditText)findViewById(R.id.passwordText);
        loginButton=(Button)findViewById(R.id.logButton);
        RegButton=(Button)findViewById(R.id.regButton);
        firebaseAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                if(fUser!=null)
                {
                    Toast.makeText(UserLoginPage.this,fUser.toString(),Toast.LENGTH_LONG).show();
                    Intent intent= new Intent(UserLoginPage.this,firstActivity.class);
                    intent.putExtra("name","user");
                    intent.putExtra("BUSNAME","Taranga");
                    intent.putExtra("TIME","7:00 up");
                    startActivity(intent);
                    finish();
                    return ;
                }
            }
        };

        RegButton.setOnClickListener( this);
        loginButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onClick(View v) {
        if(v==RegButton)
        {
            final String emailString= email.getText().toString().trim();
            final String passwordString= password.getText().toString().trim();
            mAuth.createUserWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(UserLoginPage.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful())
                    {
                        Toast.makeText(UserLoginPage.this,"registration error",Toast.LENGTH_LONG).show();
                    }


                }
            });

        }
        if(v==loginButton)
        {
            final String emailString= email.getText().toString().trim();
            final String passwordString= password.getText().toString().trim();
            mAuth.signInWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(UserLoginPage.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(!task.isSuccessful())
                    {
                        Toast.makeText(UserLoginPage.this,"login error",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
