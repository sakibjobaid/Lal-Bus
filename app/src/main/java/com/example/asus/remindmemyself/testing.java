package com.example.asus.remindmemyself;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class testing extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private FirebaseAuth firebaseAuth;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        button=findViewById(R.id.logoutBtn);
        tv=findViewById(R.id.tview);
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        if(firebaseUser==null)
        {
            finish();
            startActivity(new Intent(testing.this,AdminLoginPage.class));
        }
        tv.setText("Welcome "+ firebaseUser.getEmail());

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(testing.this,AdminLoginPage.class));
    }
}
