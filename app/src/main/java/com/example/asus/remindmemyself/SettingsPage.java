package com.example.asus.remindmemyself;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SettingsPage extends AppCompatActivity implements View.OnClickListener {

    private Button upload,choose;
    private ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ProgressBar progressBar;
    private Uri mImageUri;
    private StorageReference storageReference,st;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        this.setTitle(getIntent().getStringExtra("name"));

        upload=(Button)findViewById(R.id.upload);
        choose=(Button)findViewById(R.id.choose);
        imageView=(ImageView)findViewById(R.id.img);

        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        storageReference= FirebaseStorage.getInstance().getReference("ScheduleImage");

        upload.setOnClickListener(this);
        choose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==choose)
        {
             openFileChooser();
        }
        else
        {
            Toast.makeText(this,"Upload file clicked",Toast.LENGTH_LONG).show();
                  uploadImage();
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton().getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage() {

        if(mImageUri!=null)
        {
          st= storageReference.child(GlobalClass.BusName);
          if(st!=null)
          {
              Toast.makeText(this,"St not  null",Toast.LENGTH_LONG).show();

              st.putFile(mImageUri).
                      addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             //progressBar.setVisibility(View.VISIBLE);
                              Toast.makeText(SettingsPage.this,"Upload Successful",Toast.LENGTH_LONG).show();

                          }
                      }).
                      addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {

                              Toast.makeText(SettingsPage.this,"Upload Unsuccessful.Check your network connection",Toast.LENGTH_LONG).show();
                          }
                      }).
                      addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                              //progressBar.setVisibility(View.GONE);
                              Toast.makeText(SettingsPage.this,"on Complete listener",Toast.LENGTH_LONG).show();

                          }
                      })
              ;



          }
        }
        else
            Toast.makeText(this,"Please select one image",Toast.LENGTH_LONG).show();
    }

    private void openFileChooser() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode== RESULT_OK &&
                data != null && data.getData()!=null)
        {
            Toast.makeText(this,"onActivityResult",Toast.LENGTH_LONG).show();
            mImageUri=data.getData();
            imageView.setImageURI(mImageUri);
        }
    }
}
