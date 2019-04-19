package com.example.asus.remindmemyself;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class FeedbackPage extends AppCompatActivity {

    public static DatabaseReference ref;
    private Button feedback_button;
    private static  String feed_message;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editText=(EditText)findViewById(R.id.feed_text);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        this.setTitle("Feedback Page");
    }

    public void send(View v)
    {
        feed_message=editText.getText().toString().trim();
        if(feed_message.length()==0)
        {
            Toast.makeText(FeedbackPage.this,"Your feedback is empty",Toast.LENGTH_LONG).show();
            return;
        }
        ref = FirebaseDatabase.getInstance().getReference();
        Toast.makeText(this,"feedback send",Toast.LENGTH_LONG).show();
        String id = UUID.randomUUID().toString();
        ref.child("Feedback").child(id).setValue(feed_message);
        finish();
    }
}
