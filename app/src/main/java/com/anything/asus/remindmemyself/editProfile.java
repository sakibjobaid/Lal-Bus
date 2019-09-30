package com.anything.asus.remindmemyself;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.anything.asus.remindmemyself.core.ImageCompressTask;
import com.anything.asus.remindmemyself.listeners.IImageCompressTaskListener;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.senab.photoview.PhotoView;

public class editProfile extends AppCompatActivity implements View.OnClickListener {


    //region variables

    Timestamp tt;
    EditText name, dept, fb, new_contact,email;
    LinearLayout trip,contct_layout;
    Button saveProfile;
    TextView tv, old_contact, contact,email_tv,trip_number,up_val,down_val;
    CircleImageView propic;
    MenuItem  edit;
    Menu menu;
    Toolbar toolbar;
    ActionBar actionBar;
    String  contact_new, final_number, picdise="Not provided",image_path, number22,
            verificationCode, fblink="", final_href="",saved_contact,downloadUrl;
    FirebaseAuth auth;
    Switch mail_switch, fb_switch, contact_switch, pic_switch;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    boolean check2 = true ;
    Spanned html;
    Dialog dialog2;
    UploadTask uploadTask;
    DatabaseReference db,feedback_copy=null;
    StorageReference sb;
    ValueEventListener val_feedback_copy=null;
    SharedPreferences pic_show, fb_show, contact_show, mail_show, phonePref = null,imageURL=null;

    ImageView up_icon,down_icon;

    File compressed_file=null;

    static boolean picbig ,first,edit_save=false,hide_save=false;

    private ImageCompressTask imageCompressTask;
    private ExecutorService mExecutorService;


    //endregion

    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        //region shurur default kaj
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        this.setTitle("Profile");
        //endregion

        final Handler handler44 = new Handler();
        handler44.postDelayed(new Runnable() {
            @Override
            public void run() {

                ((Toolbar) findViewById(R.id.toolbar)).showOverflowMenu();

            }
        }, 1000);


        findingView();
        first=true;
        picbig=true;
        propic.setOnClickListener(this);

        sb= FirebaseStorage.getInstance().getReference("profile_pic");
        db=FirebaseDatabase.getInstance().getReference();
        feedback_copy=FirebaseDatabase.getInstance().getReference();

        mExecutorService = Executors.newFixedThreadPool(1);





        if(StartActiviy.admin_list_ref!=null && StartActiviy.val_admin_list_ref!=null)
            StartActiviy.admin_list_ref.removeEventListener(StartActiviy.val_admin_list_ref);


        //region saveprofile onclick
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_save)
                {
                    saveChanges();

                }
                else if(hide_save)
                {
                    hidesave();
                }
            }
        });
        //endregion

        //region sharedopreferences
        phonePref = getSharedPreferences(getString(R.string.phone_pref_file), MODE_PRIVATE);
        GlobalClass.PicStringPref = getSharedPreferences(getString(R.string.globalclass_picString_name_pref_file), MODE_PRIVATE);
        GlobalClass.NamePref = getSharedPreferences(getString(R.string.globalclass_profile_name_pref_file), MODE_PRIVATE);
        GlobalClass.DeptPref = getSharedPreferences(getString(R.string.globalclass_dept_name_pref_file), MODE_PRIVATE);
        GlobalClass.MailPref = getSharedPreferences(getString(R.string.globalclass_mail_name_pref_file), MODE_PRIVATE);
        GlobalClass.ContactPref = getSharedPreferences(getString(R.string.globalclass_contact_name_pref_file), MODE_PRIVATE);
        GlobalClass.FbPref = getSharedPreferences(getString(R.string.globalclass_fb_name_pref_file), MODE_PRIVATE);
        GlobalClass.TripPref = getSharedPreferences(getString(R.string.globalclass_trip_name_pref_file), MODE_PRIVATE);

        pic_show = getSharedPreferences(getString(R.string.pic_show_pref_file), MODE_PRIVATE);
        fb_show = getSharedPreferences(getString(R.string.fb_show_pref_file), MODE_PRIVATE);
        contact_show = getSharedPreferences(getString(R.string.contact_show_pref_file), MODE_PRIVATE);
        mail_show = getSharedPreferences(getString(R.string.mail_show_pref_file), MODE_PRIVATE);
        imageURL=getSharedPreferences(getString(R.string.imageURL_pref_file),MODE_PRIVATE);
        //endregion

        //region sharedpreferences editor
        GlobalClass.NameEditor = GlobalClass.NamePref.edit();
        GlobalClass.DeptEditor = GlobalClass.DeptPref.edit();
        GlobalClass.MailEditor = GlobalClass.MailPref.edit();
        GlobalClass.ContactEditor = GlobalClass.ContactPref.edit();
        GlobalClass.FbEditor = GlobalClass.FbPref.edit();
        GlobalClass.TripEditor = GlobalClass.TripPref.edit();
        GlobalClass.PicStringEditor = GlobalClass.PicStringPref.edit();
        //endregion

        StartFirebaseLogin();

        //region switchOnClickListener

        //region fbswitch
        fb_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if (!fblink.matches("(.*)www.facebook.com(.*)")) {

                        fb_switch.setChecked(false);
                        toastIconInfo("No link is provided yet");
                        tv.setText("Not provided");
                        tv.setTextColor(Color.GRAY);

                    } else {
                        GlobalClass.mail_show = true;
                        SharedPreferences.Editor mailEditor = mail_show.edit();
                        mailEditor.putString("mail_show_name", "true");
                        mailEditor.apply();

                        tv.setText(html);

                    }

                }
                else {
                    if(fb_show.getString("fb_show_name", "zzz").equals("true"))
                    {
                        tv.setText(GlobalClass.NamePref.getString(getString(R.string.profile_name_getter), "Not provided"));
                        tv.setTextColor(Color.GRAY);
                    }
                    else
                    {
                        tv.setText("Not provided");
                        tv.setTextColor(Color.GRAY);
                    }

                }
            }
        });
        //endregion

        //region mail_switch
        mail_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (GlobalClass.MailPref.getString(getString(R.string.mail_name_getter), "Not provided").equals("Not provided")) {
                        mail_switch.setChecked(false);
                        toastIconInfo("No email is provided yet");

                    } else
                    {
                        email_tv.setText(GlobalClass.MailPref.getString(getString(R.string.mail_name_getter), "Not provided"));
                        email_tv.setTextColor(Color.BLACK);
                        SharedPreferences.Editor mailEditor = mail_show.edit();
                        mailEditor.putString("mail_show_name", "true");
                        mailEditor.apply();

                    }
                }
                else
                {
                    if(mail_show.getString("mail_show_name", "zzz").equals("true"))
                    {
                        email_tv.setText(GlobalClass.MailPref.getString(getString(R.string.mail_name_getter), "Not provided"));
                        email_tv.setTextColor(Color.GRAY);
                    }
                    else
                    {
                        email_tv.setText("Not provided");
                        email_tv.setTextColor(Color.GRAY);
                    }

                }
            }
        });
        //endregion

        //region contact_switch
        contact_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    SharedPreferences.Editor contactEditor = contact_show.edit();
                    contactEditor.putString(getString(R.string.contact_name_getter), "true");
                    contactEditor.apply();
                    contact.setText(GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter),"Not provided"));
                    contact.setTextColor(Color.BLACK);

                }
                else
                {
                    contact.setTextColor(Color.GRAY);
                }
            }
        });
        //endregion

        //region pic_switch
        pic_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!GlobalClass.imgString.equals("Not provided"))
                    {
                        base64ToImage(GlobalClass.imgString);
                        SharedPreferences.Editor picEditor = pic_show.edit();
                        picEditor.putString("pic_show_name", "true");
                        picEditor.apply();

                    }
                    else {
                        SharedPreferences.Editor picEditor = pic_show.edit();
                        picEditor.putString("pic_show_name", "false");
                        picEditor.apply();
                        pic_switch.setChecked(false);
                        toastIconInfo("No image is provided yet");
                    }

                } else
                {
                    SharedPreferences.Editor picEditor = pic_show.edit();
                    picEditor.putString("pic_show_name", "false");
                    picEditor.apply();
                    propic.setImageResource(R.drawable.default_user);
                }

            }
        });
        //endregion

        //endregion

        populateProfile();

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

    private void goThroughWeb() {


        String id22= GlobalClass.FbPref.getString(getString(R.string.fb_name_getter), "Not provided");

        final_href = "<a href='" + id22 + "'>" +
                GlobalClass.NamePref.getString(getString(R.string.profile_name_getter), "Not provided") + "</a>";



        html = Html.fromHtml(final_href);
        tv.setText(html);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void hidesave() {
        mail_switch.setVisibility(View.GONE);
        contact_switch.setVisibility(View.GONE);
        fb_switch.setVisibility(View.GONE);
        pic_switch.setVisibility(View.GONE);

        saveProfile.setVisibility(View.GONE);
        fb.setVisibility(View.GONE);
        email.setVisibility(View.GONE);

        tv.setVisibility(View.VISIBLE);
        email_tv.setVisibility(View.VISIBLE);
        trip.setVisibility(View.VISIBLE);
        contct_layout.setVisibility(View.VISIBLE);
        up_val.setVisibility(View.VISIBLE);
        down_val.setVisibility(View.VISIBLE);
        up_icon.setVisibility(View.VISIBLE);
        down_icon.setVisibility(View.VISIBLE);

        name.setEnabled(false);
        dept.setEnabled(false);

        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Profile");
        toolbar.getMenu().clear();
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        picbig = true;


        String localname = name.getText().toString().trim();
        String localdept = dept.getText().toString().trim();
        String localpic,localemail,localcontact,localfb;




        pic_show = getSharedPreferences(getString(R.string.pic_show_pref_file), MODE_PRIVATE);
        fb_show = getSharedPreferences(getString(R.string.fb_show_pref_file), MODE_PRIVATE);
        contact_show = getSharedPreferences(getString(R.string.contact_show_pref_file), MODE_PRIVATE);
        mail_show = getSharedPreferences(getString(R.string.mail_show_pref_file), MODE_PRIVATE);


        //region mail_switch check
        if (mail_switch.isChecked()) {

            GlobalClass.mail_show = true;
            localemail = GlobalClass.MailPref.getString(getString(R.string.mail_name_getter), "Not provided");
            SharedPreferences.Editor mailEditor = mail_show.edit();
            mailEditor.putString("mail_show_name", "true");
            mailEditor.apply();
        } else {
            localemail = "Not provided";
            SharedPreferences.Editor mailEditor = mail_show.edit();
            mailEditor.putString("mail_show_name", "false");
            mailEditor.apply();

        }
        //endregion

        //region contacat_switch check
        if (contact_switch.isChecked()) {
            GlobalClass.contact_show = true;
            localcontact = GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter), "Not provided");
            SharedPreferences.Editor contactEditor = contact_show.edit();
            contactEditor.putString(getString(R.string.contact_show_name_getter), "true");
            contactEditor.apply();
        } else {
            localcontact = "Not provided";
            SharedPreferences.Editor contactEditor = contact_show.edit();
            contactEditor.putString(getString(R.string.contact_show_name_getter), "false");
            contactEditor.apply();
        }
        //endregion

        //region fb_switch check
        if (fb_switch.isChecked()) {
            GlobalClass.fb_show = true;
            localfb = GlobalClass.FbPref.getString(getString(R.string.fb_name_getter), "Not provided");
            SharedPreferences.Editor fbEditor = fb_show.edit();
            fbEditor.putString("fb_show_name", "true");
            fbEditor.apply();
        } else {
            localfb = "Not provided";
            SharedPreferences.Editor fbEditor = fb_show.edit();
            fbEditor.putString("fb_show_name", "false");
            fbEditor.apply();
        }
        //endregion

        //region pic_switch check
        if (pic_switch.isChecked()) {
            GlobalClass.pic_show = true;
            localpic = "Provided";
            SharedPreferences picStringinfo = getSharedPreferences("picStringinfo",MODE_PRIVATE);
            SharedPreferences.Editor editor1=picStringinfo.edit();
            editor1.putString("picStringinfo","Provided");
            editor1.apply();
            SharedPreferences.Editor picEditor = pic_show.edit();
            picEditor.putString("pic_show_name", "true");
            picEditor.apply();
        } else {
            localpic = "Not provided";
            SharedPreferences picStringinfo = getSharedPreferences("picStringinfo",MODE_PRIVATE);
            SharedPreferences.Editor editor1=picStringinfo.edit();
            editor1.putString("picStringinfo","Not provided");
            editor1.apply();
            SharedPreferences.Editor picEditor = pic_show.edit();
            picEditor.putString("pic_show_name", "false");
            picEditor.apply();
        }
        //endregion

        GlobalClass.NameEditor.putString(getString(R.string.profile_name_getter), localname);
        GlobalClass.NameEditor.apply();

        GlobalClass.DeptEditor.putString(getString(R.string.dept_name_getter), localdept);
        GlobalClass.DeptEditor.apply();


        saveinfoToDatabase(localname, localdept, localcontact,
                localemail, localfb,
                localpic,GlobalClass.TripPref.
                        getLong(getString(R.string.trip_name_getter),0),
                GlobalClass.upTripPref.getLong(getString(R.string.up_trip_name_getter),0),
                GlobalClass.downTripPref.getLong(getString(R.string.down_trip_name_getter),0));
    }

    private void findingView() {

        propic = findViewById(R.id.edit_propic_round);
        name = findViewById(R.id.edit_name);
        dept = findViewById(R.id.edit_dept);
        email = findViewById(R.id.edit_email_content);
        email_tv=findViewById(R.id.edit_in_text_email_content);
        contact = findViewById(R.id.edit_contact_content);
        fb = findViewById(R.id.edit_fb_content);
        tv = findViewById(R.id.edit_in_text_fb_content);
        saveProfile = findViewById(R.id.saveProfile);
        trip_number=findViewById(R.id.trip_completed);

        mail_switch = findViewById(R.id.mail_switch);
        contact_switch = findViewById(R.id.contact_switch);
        fb_switch = findViewById(R.id.fb_switch);
        pic_switch = findViewById(R.id.pic_switch);

        trip = findViewById(R.id.trip_layout);
        contct_layout=findViewById(R.id.contact_layout);

        up_val=findViewById(R.id.up_val22);
        down_val=findViewById(R.id.down_val22);

        up_icon=findViewById(R.id.up_trip22);
        down_icon=findViewById(R.id.down_trip22);




    }

    private void populateProfile() {

        //region first_time
        if(first)
        {
            first=false;
            fb.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            saveProfile.setVisibility(View.GONE);

            pic_switch.setVisibility(View.GONE);
            mail_switch.setVisibility(View.GONE);
            contact_switch.setVisibility(View.GONE);
            fb_switch.setVisibility(View.GONE);

            name.setEnabled(false);
            dept.setEnabled(false);

        }
        //endregion

        //region propic initialization
        GlobalClass.imgString = GlobalClass.PicStringPref.getString(getString(R.string.profile_pic_name_getter), "Not provided");
        if (pic_show.getString("pic_show_name","zzz").equals("true")) {

            if(!GlobalClass.imgString.equals("Not provided"))
            {

                base64ToImage(GlobalClass.imgString);

            }
            else             propic.setImageResource(R.drawable.default_user);

        }
        else {
            propic.setImageResource(R.drawable.default_user);
        }
        //endregion

        //region name initiallzation
        name.setText(GlobalClass.NamePref.getString(getString(R.string.profile_name_getter), "Not provided"));
        //endregion

        //region dept initialization
        dept.setText(GlobalClass.DeptPref.getString(getString(R.string.dept_name_getter), "Not provided"));
        //endregion

        //region mail initialization
        if (mail_show.getString("mail_show_name", "zzz").equals("true"))
        {
            if(GlobalClass.MailPref.getString(getString(R.string.mail_name_getter), "Not provided").equals("Not provided"))
            {
                email_tv.setText("Not provided");
                email_tv.setTextColor(Color.GRAY);
            }
            else
            {
                email_tv.setTextColor(Color.BLACK);
                email_tv.setText(GlobalClass.MailPref.getString(getString(R.string.mail_name_getter), "Not provided"));
            }
        }
        else
        {
            if(GlobalClass.MailPref.getString(getString(R.string.mail_name_getter), "Not provided").equals("Not provided"))
            {
                email_tv.setText("Not provided");
                email_tv.setTextColor(Color.GRAY);
            }
            else
            {
                email_tv.setTextColor(Color.GRAY);
                email_tv.setText(GlobalClass.MailPref.getString(getString(R.string.mail_name_getter), "Not provided"));
            }
        }
        //endregion

        //region contact_initialization
        if (contact_show.getString(getString(R.string.contact_show_name_getter), "zzz").equals("true"))
        {
            contact.setText(GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter), "Not provided"));
            contact.setTextColor(Color.BLACK);
        }
        else
        {
            contact.setText(GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter), "Not provided"));
            contact.setTextColor(Color.GRAY);
        }
        //endregion

        //region fb initialization
        fblink = GlobalClass.FbPref.getString(getString(R.string.fb_name_getter), "Not provided");
        final_href = "<a href='" + fblink + "'>" +
                GlobalClass.NamePref.getString(getString(R.string.profile_name_getter), "Not provided") + "</a>";

        if(fb_show.getString("fb_show_name", "zzz").equals("true"))
        {
            if (final_href.matches("(.*)www.facebook.com(.*)"))
            {
                html = Html.fromHtml(final_href);
                tv.setText(html);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
            else
            {
                tv.setText("Not provided");
                tv.setTextColor(Color.GRAY);

            }
        }

        else
        {
            if (final_href.matches("(.*)www.facebook.com(.*)"))
            {
                tv.setText(GlobalClass.NamePref.getString(getString(R.string.profile_name_getter), "Not provided"));
                tv.setTextColor(Color.GRAY);

            }
            else
            {
                tv.setText("Not provided");
                tv.setTextColor(Color.GRAY);

            }
        }
        //endregion

        long number=GlobalClass.TripPref.getLong(getString(R.string.trip_name_getter),0);
        if(number>1)
            trip_number.setText(String.valueOf(number)+" trips accompanied");
        else
            trip_number.setText(String.valueOf(number)+" trip accompanied");



    }

    private void openHideDialog() {

        //region propic initialization
        if (pic_show.getString("pic_show_name","zzz").equals("true")) {

            if(!GlobalClass.imgString.equals("Not provided"))
            {
                pic_switch.setChecked(true);
                base64ToImage(GlobalClass.imgString);

            }
            else
            {
                pic_switch.setChecked(false);
                propic.setImageResource(R.drawable.default_user);
            }

        }
        else {
            pic_switch.setChecked(false);
            propic.setImageResource(R.drawable.default_user);
        }
        //endregion

        //region mail initialization
        if (mail_show.getString("mail_show_name", "zzz").equals("true"))
        {
            if(GlobalClass.MailPref.getString(getString(R.string.mail_name_getter), "Not provided").equals("Not provided"))
            {
                mail_switch.setChecked(false);
                email_tv.setText("Not provided");
                email_tv.setTextColor(Color.GRAY);
            }
            else
            {
                mail_switch.setChecked(true);
                email_tv.setTextColor(Color.BLACK);
                email_tv.setText(GlobalClass.MailPref.getString(getString(R.string.mail_name_getter), "Not provided"));
            }
        }
        else
        {
            if(GlobalClass.MailPref.getString(getString(R.string.mail_name_getter), "Not provided").equals("Not provided"))
            {
                mail_switch.setChecked(false);
                email_tv.setText("Not provided");
                email_tv.setTextColor(Color.GRAY);
            }
            else
            {
                mail_switch.setChecked(false);
                email_tv.setTextColor(Color.GRAY);
                email_tv.setText(GlobalClass.MailPref.getString(getString(R.string.mail_name_getter), "Not provided"));
            }
        }
        //endregion

        //region contact_initialization
        if (contact_show.getString(getString(R.string.contact_show_name_getter), "zzz").equals("true"))
        {
            contact_switch.setChecked(true);
            contact.setText(GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter), "Not provided"));
            contact.setTextColor(Color.BLACK);
        }
        else
        {
            contact_switch.setChecked(false);
            contact.setText(GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter), "Not provided"));
            contact.setTextColor(Color.GRAY);
        }
        //endregion

        //region fb initialization
        fblink = GlobalClass.FbPref.getString(getString(R.string.fb_name_getter), "Not provided");
        final_href = "<a href='" + fblink + "'>" +
                GlobalClass.NamePref.getString(getString(R.string.profile_name_getter), "Not provided") + "</a>";

        if(fb_show.getString("fb_show_name", "zzz").equals("true"))
        {
            if (final_href.matches("(.*)www.facebook.com(.*)"))
            {
                fb_switch.setChecked(true);
                html = Html.fromHtml(final_href);

                tv.setText(html);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
            else
            {
                fb_switch.setChecked(false);
                tv.setText("Not provided");
                tv.setTextColor(Color.GRAY);

            }
        }

        else
        {
            if (final_href.matches("(.*)www.facebook.com(.*)"))
            {
                fb_switch.setChecked(false);
                tv.setText(GlobalClass.NamePref.getString(getString(R.string.profile_name_getter), "Not provided"));
                tv.setTextColor(Color.GRAY);

            }
            else
            {
                fb_switch.setChecked(false);
                tv.setText("Not provided");
                tv.setTextColor(Color.GRAY);

            }
        }
        //endregion


        name.setText(GlobalClass.NamePref.getString(getString(R.string.profile_name_getter), "Not provided"));
        dept.setText(GlobalClass.DeptPref.getString(getString(R.string.dept_name_getter), "Not provided"));

        trip.setVisibility(View.GONE);
        up_val.setVisibility(View.GONE);
        down_val.setVisibility(View.GONE);
        up_icon.setVisibility(View.GONE);
        down_icon.setVisibility(View.GONE);

        mail_switch.setVisibility(View.VISIBLE);
        pic_switch.setVisibility(View.VISIBLE);
        contact_switch.setVisibility(View.VISIBLE);
        fb_switch.setVisibility(View.VISIBLE);

        saveProfile.setVisibility(View.VISIBLE);





    }

    private void imageToBase64() {

        //encode image to base64 string

        if (GlobalClass.bitMap != null) {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
//            GlobalClass.bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] imageBytes = baos.toByteArray();
//            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            GlobalClass.imgString = GlobalClass.profilePicUri.toString();

        }
    }



    private void base64ToImage(String imageString) {

//        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
//        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//        GlobalClass.bitMap=decodedImage;
//        propic.setImageBitmap(decodedImage);

        Glide.with(editProfile.this).load(Uri.parse(imageString))
                .into(propic);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tool_hide:

                //region toolbar settings
                toolbar.getMenu().clear();
                actionBar.setDisplayHomeAsUpEnabled(false);
                this.setTitle("  Profile");
                //endregion
                edit_save=false;
                hide_save=true;
                openHideDialog();
                break;

            case R.id.tool_edit:

                edit_save=true;
                hide_save=false;
                //region toolbar settings
                toolbar.getMenu().clear();
                actionBar.setDisplayHomeAsUpEnabled(false);
                this.setTitle("  Profile");
                //endregion

                picbig = false;

                //region view Gone visible

                contct_layout.setVisibility(View.GONE);
                trip.setVisibility(View.GONE);
                tv.setVisibility(View.GONE);
                email_tv.setVisibility(View.GONE);
                up_val.setVisibility(View.GONE);
                down_val.setVisibility(View.GONE);
                up_icon.setVisibility(View.GONE);
                down_icon.setVisibility(View.GONE);

                fb.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                saveProfile.setVisibility(View.VISIBLE);

                //endregion

                name.setEnabled(true);
                dept.setEnabled(true);


                if (GlobalClass.MailPref.
                        getString(getString(R.string.mail_name_getter), "Not provided")
                        .equals("Not provided")) {
                    email.setHint("Enter your E-mail");
                }
                else
                {
                    email.setTextColor(Color.BLACK);
                    email.setText(GlobalClass.MailPref.getString(getString(R.string.mail_name_getter),"Not provided"));

                }

                if (GlobalClass.FbPref.getString(getString(R.string.fb_name_getter), "Not provided").equals("Not provided")) {
                    fb.setHint("Paste FB ID link");
                }
                else
                {
                    fb.setTextColor(Color.BLACK);
                    fb.setText(GlobalClass.FbPref.getString(getString(R.string.fb_name_getter), "Not provided"));
                }


                break;

            case R.id.tool_change:

                change_contact();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mExecutorService.shutdown();
        mExecutorService = null;
        imageCompressTask = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mExecutorService = Executors.newFixedThreadPool(1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
            GlobalClass.profilePicUri = data.getData();
            Cursor cursor = MediaStore.Images.Media.query(getContentResolver(), GlobalClass.profilePicUri, new String[]{MediaStore.Images.Media.DATA});

            if(cursor != null && cursor.moveToFirst()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                //Create ImageCompressTask and execute with Executor.
                imageCompressTask = new ImageCompressTask(this, path, iImageCompressTaskListener);

                mExecutorService.execute(imageCompressTask);
            }

            try {

                GlobalClass.bitMap = MediaStore.Images.Media.
                        getBitmap(this.getContentResolver(), GlobalClass.profilePicUri);

                fix_orientation(GlobalClass.bitMap);

                imageToBase64();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //image compress task callback
    private IImageCompressTaskListener iImageCompressTaskListener = new IImageCompressTaskListener() {
        @Override
        public void onComplete(List<File> compressed) {


            compressed_file = compressed.get(0);


        }

        @Override
        public void onError(Throwable error) {
            //very unlikely, but it might happen on a device with extremely low storage.
            //log it, log.WhatTheFuck?, or show a dialog asking the user to delete some files....etc, etc
            Log.wtf("ImageCompressor", "Error occurred", error);
        }
    };

    private void saveChanges() {


        String localname = name.getText().toString().trim();
        String localdept = dept.getText().toString().trim();
        String localemail=email.getText().toString().trim();
        String localfb = fb.getText().toString().trim();

        //region gone visible
        saveProfile.setVisibility(View.GONE);
        fb.setVisibility(View.GONE);
        email.setVisibility(View.GONE);


        contct_layout.setVisibility(View.VISIBLE);
        trip.setVisibility(View.VISIBLE);
        tv.setVisibility(View.VISIBLE);
        email_tv.setVisibility(View.VISIBLE);
        up_val.setVisibility(View.VISIBLE);
        down_val.setVisibility(View.VISIBLE);
        up_icon.setVisibility(View.VISIBLE);
        down_icon.setVisibility(View.VISIBLE);
        //endregion

        //region enabled true false
        name.setEnabled(false);
        dept.setEnabled(false);
        //endregion

        actionBar.setDisplayHomeAsUpEnabled(true);
        this.setTitle("   Profile");
        toolbar.getMenu().clear();
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        picbig = true;

        name.setText(localname);

        if(localemail.length()==0)
        {
            localemail="Not provided";
            email_tv.setText(localemail);
            email_tv.setTextColor(Color.GRAY);

            SharedPreferences.Editor mailEditor= mail_show.edit();
            mailEditor.putString("mail_show_name","false");
            mailEditor.apply();

        }

        else
        {
            localemail = email.getText().toString().trim();
            email_tv.setText(localemail);
            email_tv.setTextColor(Color.BLACK);
            SharedPreferences.Editor mailEditor= mail_show.edit();
            mailEditor.putString("mail_show_name","true");
            mailEditor.apply();

        }
        contact.setText(GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter), "Not provided"));
        dept.setText(localdept);

        if(localfb.length()!=0)
        {


            fblink = "<a href='" + localfb + "'>" + localname + "</a>";
            if (fblink.matches("(.*)www.facebook.com(.*)")) {



                    html = Html.fromHtml(fblink);
                    tv.setText(html);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());




                GlobalClass.FbEditor.putString(getString(R.string.fb_name_getter), localfb);
                GlobalClass.FbEditor.apply();

                SharedPreferences.Editor fbEditor= fb_show.edit();
                fbEditor.putString("fb_show_name","true");
                fbEditor.apply();
            }
        }
        else
        {
            localfb="Not provided";
            tv.setText(localfb);
            tv.setTextColor(Color.GRAY);


            GlobalClass.FbEditor.putString(getString(R.string.fb_name_getter), localfb);
            GlobalClass.FbEditor.apply();

            SharedPreferences.Editor fbEditor= fb_show.edit();
            fbEditor.putString("fb_show_name","false");
            fbEditor.apply();
        }




        GlobalClass.NameEditor.putString(getString(R.string.profile_name_getter), localname);
        GlobalClass.NameEditor.apply();

        GlobalClass.DeptEditor.putString(getString(R.string.dept_name_getter), localdept);
        GlobalClass.DeptEditor.apply();

        GlobalClass.MailEditor.putString(getString(R.string.mail_name_getter), localemail);
        GlobalClass.MailEditor.apply();



        if (GlobalClass.bitMap != null) {


            String image_id="images/"+GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter),"")+".jpg";

            final StorageReference ref = sb.child(image_id);
            if(compressed_file!=null)
            {
                Uri compressedUri= Uri.fromFile(compressed_file);
                uploadTask = ref.putFile(compressedUri);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            SharedPreferences picStringinfo = getSharedPreferences("picStringinfo",MODE_PRIVATE);
                            SharedPreferences.Editor editor1=picStringinfo.edit();
                            editor1.putString("picStringinfo","Provided");
                            editor1.apply();
                            Uri downloadUri = task.getResult();

                            String contact_Number=GlobalClass.ContactPref
                                    .getString(getString(R.string.contact_name_getter),"Not provided");


                            if(GlobalClass.tempBusName!=null)
                                db.child(getString(R.string.Firebase_admin_profile)).child(GlobalClass.tempBusName).child(contact_Number).child("imageURL")
                                        .setValue(downloadUri.toString());
                            else if(GlobalClass.BusName!=null)
                                db.child(getString(R.string.Firebase_admin_profile)).child(GlobalClass.BusName).child(contact_Number).child("imageURL")
                                        .setValue(downloadUri.toString());


                            SharedPreferences.Editor editor= imageURL.edit();
                            editor.putString("imageURL",downloadUri.toString());
                            editor.apply();

                            if(GlobalClass.tempBusName!=null)
                                db.child(getString(R.string.Firebase_admin_profile)).child(GlobalClass.tempBusName).child(contact_Number).child("picString")
                                        .setValue(picStringinfo.getString("picStringinfo","painai"));
                            else if(GlobalClass.BusName!=null)
                                db.child(getString(R.string.Firebase_admin_profile)).child(GlobalClass.BusName).child(contact_Number).child("picString")
                                        .setValue(picStringinfo.getString("picStringinfo","painai"));

                        } else {

                        }
                    }
                });

            }

            imageToBase64();
            GlobalClass.PicStringEditor.putString(getString(R.string.profile_pic_name_getter), GlobalClass.imgString);
            GlobalClass.PicStringEditor.apply();


            SharedPreferences.Editor picEditor= pic_show.edit();
            picEditor.putString("pic_show_name","true");
            picEditor.apply();

        }
        else
        {
            SharedPreferences.Editor picEditor= pic_show.edit();
            picEditor.putString("pic_show_name","false");
            picEditor.apply();
        }



        if(contact_show.getString(getString(R.string.contact_show_name_getter), "zzz").equals("false"))
        {
            saveinfoToDatabase(localname, localdept,  "Not provided", localemail, localfb,
                    picdise,GlobalClass.TripPref.
                            getLong(getString(R.string.trip_name_getter),0),
                    GlobalClass.upTripPref.getLong(getString(R.string.up_trip_name_getter),0),
                    GlobalClass.downTripPref.getLong(getString(R.string.down_trip_name_getter),0));
        }

        else
        {
            saveinfoToDatabase(localname, localdept,
                    GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter),"Not provided"), localemail, localfb,
                    picdise,GlobalClass.TripPref.getLong(getString(R.string.trip_name_getter),0),
                    GlobalClass.upTripPref.getLong(getString(R.string.up_trip_name_getter),0),
                    GlobalClass.downTripPref.getLong(getString(R.string.down_trip_name_getter),0));
        }

    }



    private void change_contact() {

        final Dialog dialog = new Dialog(editProfile.this);
        dialog.setContentView(R.layout.custom_change_contact);

        old_contact = dialog.findViewById(R.id.old_contact);
        new_contact = dialog.findViewById(R.id.new_contactOTP);


        saved_contact = GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter), "Not provided");

        old_contact.setText(saved_contact);


        Button send_code = dialog.findViewById(R.id.send_codeOTP);

        send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                contact_new = new_contact.getText().toString().trim();


                dialog.dismiss();
                dialog.cancel();
                custom_otp();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);


        dialog.show();


    }

    private void callCall_back() {

        final_number = "+88" + contact_new;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                final_number,                     // Phone number to verify
                120,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                editProfile.this,        // Activity (for callback binding)
                mCallback);
    }

    private void custom_otp() {

        callCall_back();

        dialog2 = new Dialog(editProfile.this);
        dialog2.setContentView(R.layout.custom_otp);

        final EditText verification_code = dialog2.findViewById(R.id.verification_codeOTP);

        Button verify = dialog2.findViewById(R.id.verifyOTP);
        TextView resend = dialog2.findViewById(R.id.resendOTP);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp;
                otp = verification_code.getText().toString();
                if (otp.isEmpty()) {
                    toastIconInfo("Enter verification code");
                    return;
                }

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                SigninWithPhone(credential,"something");
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCall_back();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog2.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog2.getWindow().setAttributes(lp);

        dialog2.setCanceledOnTouchOutside(false);
        dialog2.show();


    }

    private void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                if (check2) {
                    SigninWithPhone(phoneAuthCredential,"nothing");
                }
                toastIconInfo("OTP generated");
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                toastIconInfo(e.getMessage());

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    //mPhoneNumberField.setError("Invalid phone number.");
                    toastIconInfo("Invalid phone number");

                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    toastIconInfo("SMS Quota exceeded");

                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {


                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                check2 = false;
                toastIconInfo("Wait for the OTP");
            }
        };
    }

    private void SigninWithPhone(PhoneAuthCredential credential, final String purpose) {


        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if(purpose.equals("something"))
                            {
                                toastIconInfo("Contact changed successfully");
                            }

                            SharedPreferences.Editor preferencesEditor = phonePref.edit();
                            preferencesEditor.putString(getString(R.string.phone_number_getter), final_number);
                            preferencesEditor.apply();

                            number22="";
                            for(int i=3;i<final_number.length();i++)
                                number22+=final_number.charAt(i);

                            //region Admin Roster setting up in db
                            SharedPreferences datePref;

                            datePref = getSharedPreferences(getString(R.string.date_pref_file), MODE_PRIVATE);
                            String date = datePref.getString(getString(R.string.date_getter), "sakib25");
                            DatabaseReference firebaseDatabase = FirebaseDatabase.
                                    getInstance().getReference().child(getString(R.string.Firebase_adminRoster));



                            if(GlobalClass.tempBusName!=null && GlobalClass.tempBustime!=null)
                            firebaseDatabase.child(date).child(GlobalClass.tempBusName)
                                    .child(GlobalClass.tempBustime).child("MobileNo").setValue(number22);
                            else if(GlobalClass.BusName!=null && GlobalClass.BusTime!=null)
                                firebaseDatabase.child(date).child(GlobalClass.BusName)
                                        .child(GlobalClass.BusTime).child("MobileNo").setValue(number22);
                            //endregion

                            //region putting admin_profile in sharedpreference

                            GlobalClass.feedContactPref=getSharedPreferences("feedContact",MODE_PRIVATE);
                            SharedPreferences.Editor ed1=GlobalClass.feedContactPref.edit();
                            ed1.putString("feedContact",contact_new);
                            ed1.apply();


                            GlobalClass.ContactEditor.putString(getString(R.string.contact_name_getter), contact_new);
                            GlobalClass.ContactEditor.apply();


                            //endregion

                            //region admin profile setup in db
                            if(GlobalClass.tempBusName!=null)
                            {
                                final DatabaseReference admin_profile = FirebaseDatabase.getInstance().getReference()
                                        .child(getString(R.string.Firebase_admin_profile))
                                        .child(GlobalClass.tempBusName)
                                        .child(saved_contact);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        admin_profile.removeValue();

                                    }
                                }, 2000);

                            }


                            else if(GlobalClass.BusName!=null)
                            {

                                final DatabaseReference admin_profile = FirebaseDatabase.getInstance().getReference()
                                        .child(getString(R.string.Firebase_admin_profile))
                                        .child(GlobalClass.BusName).child(saved_contact);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        admin_profile.removeValue();

                                    }
                                }, 2000);
                            }


                            DatabaseReference admin_profile2;
                            if(GlobalClass.tempBusName!=null)
                            {
                                admin_profile2 = FirebaseDatabase.getInstance().getReference()
                                        .child(getString(R.string.Firebase_admin_profile)).child(GlobalClass.tempBusName).child(number22);
                            }
                            else
                            {
                                admin_profile2 = FirebaseDatabase.getInstance().getReference()
                                        .child(getString(R.string.Firebase_admin_profile))
                                        .child(GlobalClass.BusName).child(number22);
                            }





                            admin_profile2.child("name").setValue(GlobalClass.NamePref.
                                    getString("profile_name", "Not provided"));
                            admin_profile2.child("dept").setValue(GlobalClass.DeptPref.
                                    getString(getString(R.string.dept_name_getter), "Not provided"));
                            admin_profile2.child("email").setValue(GlobalClass.MailPref.
                                    getString(getString(R.string.mail_name_getter), "Not provided"));
                            admin_profile2.child("fbId").setValue(GlobalClass.FbPref.
                                    getString(getString(R.string.fb_name_getter), "Not provided"));

                            if(!imageURL.getString("imageURL","painai").equals("painai"))
                            admin_profile2.child("imageURL").
                                    setValue(imageURL.getString("imageURL",""));

                            SharedPreferences picStringinfo = getSharedPreferences("picStringinfo",MODE_PRIVATE);

                            admin_profile2.child("picString").setValue(picStringinfo.getString("picStringinfo","painai"));
                            admin_profile2.child("trip").setValue(GlobalClass.TripPref.
                                    getLong(getString(R.string.trip_name_getter), 0));
                            admin_profile2.child("down_trip").setValue(GlobalClass.upTripPref.
                                    getLong(getString(R.string.trip_name_getter), 0));
                            admin_profile2.child("up_trip").setValue(GlobalClass.downTripPref.
                                    getLong(getString(R.string.trip_name_getter), 0));
                            admin_profile2.child("contact").setValue(number22);
                            admin_profile2.child("feed_contact").setValue(number22);
                            admin_profile2.child("last_accompanied").setValue(date);

                            //endregion

                            final DatabaseReference dbref=FirebaseDatabase.
                                    getInstance().getReference().child(getString(R.string.Firebase_admin_list));
                            dbref.child(saved_contact).removeValue();

                            dbref.removeValue();

                            final Handler handler22 = new Handler();
                            handler22.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(GlobalClass.BusName!=null)
                                    dbref.child(number22).setValue(GlobalClass.tempBusName);
                                    else
                                        dbref.child(number22).setValue(GlobalClass.BusName);

                                }
                            }, 1000);


                            contact.setText(number22);
                            dialog2.dismiss();
                            dialog2.cancel();



                        } else {

                            toastIconInfo("OTP not successful");
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {

        if (v == propic && picbig) {
            openCustomDialog();
        }
        if (v == propic && !picbig) {


            if(isReadStoragePermissionGranted())
            openGallery();
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);

            }
        }


    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 3:
                if(grantResults.length > 0)
                {
                    if(grantResults[0]== PackageManager.PERMISSION_GRANTED){

                        openGallery();

                    }else{

                    }
                }

                break;
        }
    }


    private void saveinfoToDatabase(String localname, String localdept,
                                    String localcontact, String localemail,
                                    String localfb, String img_string, long trip,long upTrip,long downTrip) {


        String busName;

        if(GlobalClass.BusName!=null)
        {
            busName=GlobalClass.BusName;
        }
        else
            busName=GlobalClass.tempBusName;

        DatabaseReference localdb=db .child(getString(R.string.Firebase_admin_profile))
                .child(busName)
                .child(GlobalClass.ContactPref
                        .getString(getString(R.string.contact_name_getter),"Not provided"));



        localdb.child("name").setValue(localname);
        localdb.child("dept").setValue(localdept);
        localdb.child("contact").setValue(localcontact);
        localdb.child("feed_contact").setValue(GlobalClass.ContactPref
                .getString(getString(R.string.contact_name_getter),"Not provided"));
        localdb.child("email").setValue(localemail);
        localdb.child("fbId").setValue(localfb);
        localdb.child("picString").setValue(img_string);
        localdb.child("trip").setValue(trip);
        localdb.child("up_trip").setValue(upTrip);
        localdb.child("down_trip").setValue(downTrip);



    }

    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);//one can be replaced with any action code


    }

    private void fix_orientation(Bitmap bitMap) {

        Glide.with(editProfile.this).load(GlobalClass.profilePicUri)
                .into(propic);
        //propic.setImageURI(GlobalClass.profilePicUri);

        ExifInterface exifInterface=null;


    }

    @SuppressLint("RestrictedApi")
    public void openCustomDialog() {


        final Dialog dialog = new Dialog(editProfile.this);
        dialog.setContentView(R.layout.propic);
        PhotoView img = dialog.findViewById(R.id.propic_large);




        String imgStringLocal = GlobalClass.PicStringPref.getString(getString(R.string.profile_pic_name_getter), "Not provided");
        if (!imgStringLocal.equals("Not provided")) {

            if(pic_show.getString("pic_show_name","zzz").equals("true"))
            {
                Glide.with(editProfile.this).load(Uri.parse(GlobalClass.imgString))
                        .into(img);
            }
            else
                img.setImageResource(R.drawable.default_user);

        } else {
            img.setImageResource(R.drawable.default_user);
        }


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);


        dialog.show();
    }

    @Override
    public void onBackPressed() {

        if(hide_save)
        {
            hidesave();
            return;
        }
        else if(edit_save)
        {
            String localname = name.getText().toString().trim();
            String localemail = email.getText().toString().trim();
            String localcontact = contact.getText().toString().trim();
            String localfb = fb.getText().toString().trim();
            String localdept = dept.getText().toString().trim();
            String localpic = GlobalClass.imgString;

            String prename = GlobalClass.NamePref.getString("profile_name", "Not provided");
            String preemail = GlobalClass.MailPref.getString(getString(R.string.mail_name_getter), "Not provided");
            String precontact = GlobalClass.ContactPref.getString(getString(R.string.contact_name_getter), "Not provided");
            String prefb = GlobalClass.FbPref.getString(getString(R.string.fb_name_getter), "Not provided");
            String predept = GlobalClass.DeptPref.getString(getString(R.string.dept_name_getter), "Not provided");
            String prepic = GlobalClass.PicStringPref.getString(getString(R.string.profile_pic_name_getter), "Not provided");

            AlertDialog.Builder builder = new AlertDialog.Builder(editProfile.this);

            imageToBase64();
            boolean picchaged = false;
            if (!prepic.equals(localpic))
                picchaged = true;
            if (!localname.equals(prename) || !localdept.equals(predept) || picchaged ||
                    (!localemail.equals(preemail)&& localemail.length()>0)
                    ||(!localfb.equals(prefb))&&localfb.length()>0) {

                builder.setTitle("Confirmation");
                builder.setMessage("Want to save your changes ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        saveChanges();
                        finish();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        finish();


                    }
                });

                builder.show();
            } else
                super.onBackPressed();

            return;
        }

        if(dialog2!=null && dialog2.isShowing())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(editProfile.this);
            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to quit this OTP session?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog2.dismiss();
                    dialog2.cancel();
                    finish();

                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.show();
            builder.setCancelable(false);
        }

    }


}

