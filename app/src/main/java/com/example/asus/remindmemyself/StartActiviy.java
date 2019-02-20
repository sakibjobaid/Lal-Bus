package com.example.asus.remindmemyself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StartActiviy extends AppCompatActivity implements View.OnClickListener {

    private Button mAdmin,mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activiy);
        mAdmin=(Button)findViewById(R.id.admin);
        mUser=(Button)findViewById(R.id.user);

        mAdmin.setOnClickListener(this);
        mUser.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        Intent intent=null;

        if(v==mAdmin)
        {
            intent= new Intent(this,firstActivity.class);
            intent.putExtra("name","admin");
            startActivity(intent);
            finish();
        }
        if(v==mUser)
        {
            intent= new Intent(this,firstActivity.class);
            intent.putExtra("name","user");
            startActivity(intent);
            finish();
        }
    }
    
}
