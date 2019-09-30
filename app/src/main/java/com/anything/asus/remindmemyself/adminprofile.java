package com.anything.asus.remindmemyself;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.senab.photoview.PhotoView;

public class adminprofile extends AppCompatActivity implements View.OnClickListener {

    private TextView name, dept, email_content, fb_content, trip_completed, headline,
            contact_content, up_completed, down_completed,feedback_text;
    private static boolean disable_ratingButton = false;
    private CircleImageView circleImageView;
    private DatabaseReference ref = null;
    public boolean prechecking = false;
    private EditText  lekhar_text;
    private ValueEventListener listener = null;
    SharedPreferences date_pref;
    String fblink, feed_contact, aj_date, id;
    Spanned html;
    protected LinearLayout feedbackLayout;
    PhotoView img;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminprofile);

        date_pref = getSharedPreferences(getString(R.string.date_pref_file), MODE_PRIVATE);
        aj_date = date_pref.getString(getString(R.string.date_getter), "No date");

        //region findingview
        headline = findViewById(R.id.admin_bus);
        circleImageView = findViewById(R.id.propic_round);
        name = findViewById(R.id.name);
        dept = findViewById(R.id.dept);
        email_content = findViewById(R.id.email_content);
        contact_content = findViewById(R.id.contact_content);
        fb_content = findViewById(R.id.fb_content);
        //admin_busaddress = findViewById(R.id.admin_bus);
        feedback_text = findViewById(R.id.edit_text);
        trip_completed = findViewById(R.id.trip_completed);
        up_completed = findViewById(R.id.up_val);
        down_completed = findViewById(R.id.down_val);
        feedbackLayout = findViewById(R.id.feedback_layout);



        //endregion

        id = getIntent().getStringExtra("fbId_info");


        name.setText(getIntent().getStringExtra("name_info"));
        dept.setText(getIntent().getStringExtra("dept_info"));
        email_content.setText(getIntent().getStringExtra("email_info"));
        contact_content.setText(getIntent().getStringExtra("contact_info"));
        feed_contact = getIntent().getStringExtra("feed_contact");
        up_completed.setText(getIntent().getStringExtra("up_trip_info"));
        down_completed.setText(getIntent().getStringExtra("down_trip_info"));

        GlobalClass.feedContactPref = getSharedPreferences("feedContact", MODE_PRIVATE);


        //region populateView
        headline.setText("Admin of " + GlobalClass.BoxBusName);


        if (feed_contact.equals(GlobalClass.feedContactPref.getString("feedContact", "painai"))) {
            feedbackLayout.setVisibility(View.GONE);

            SharedPreferences pic_show = getSharedPreferences(getString
                    (R.string.pic_show_pref_file), MODE_PRIVATE);
            GlobalClass.PicStringPref = getSharedPreferences(getString
                    (R.string.globalclass_picString_name_pref_file), MODE_PRIVATE);
            GlobalClass.imgString = GlobalClass.PicStringPref.
                    getString(getString(R.string.profile_pic_name_getter), "Not provided");


            //region first ok version
            if (pic_show.getString("pic_show_name", "zzz").equals("true")) {

                if (!GlobalClass.imgString.equals("Not provided")) {
                    base64ToImage(GlobalClass.imgString, "gol");

                } else {
                    circleImageView.setImageResource(R.drawable.default_user);


                }

            } else {

                circleImageView.setImageResource(R.drawable.default_user);
            }
            //endregion

        } else {
            if (getIntent().getStringExtra("picString_info").equals("Not provided")) {

                circleImageView.setImageResource(R.drawable.default_user);
            } else {

                if(getIntent().getStringExtra("URLString_info").equals("Not provided"))
                    circleImageView.setImageResource(R.drawable.default_user);


                 else
                {
                    Glide.with(adminprofile.this).load(Uri.parse(getIntent().
                            getStringExtra("URLString_info")))
                            .centerCrop().into(circleImageView);
                }

            }
        }


        if(id.equals("Not provided"))
        {
            fb_content.setText(id);

        }
        else
        {
            //String x="<a href='https://www.facebook.com/sakib.jobaid.9'>Sakib Job</a>";
            fblink = "<a href='" + id + "'>" + getIntent().getStringExtra("name_info") + "</a>";


            html = Html.fromHtml(fblink);
            fb_content.setText(html);
            fb_content.setMovementMethod(LinkMovementMethod.getInstance());
        }

        if (getIntent().getStringExtra("trip_info").equals("1"))
            trip_completed.setText(getIntent().getStringExtra("trip_info") + " trip accompanied");
        else
            trip_completed.setText(getIntent().getStringExtra("trip_info") + " trips accompanied");
        //endregion

        //region other functionalities


        feedback_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomFeedDialog();
            }
        });

//        if (GlobalClass.BoxBusTime.equals("TIME") || !isNetworkConnected(this)) {
//            admin_busaddress.setText("Admin of " + GlobalClass.BoxBusName);
//        } else {
//            ref = FirebaseDatabase.getInstance().getReference();
//            listener = new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    if (dataSnapshot != null) {
//                        String value = dataSnapshot.child("active").getValue().toString();
//
//                        if (value.equals("off")) {
//
//                            admin_busaddress.setText("Admin of " + GlobalClass.BoxBusName);
//                        } else {
//                            admin_busaddress.setText("Admin of " + GlobalClass.BoxBusName + " " +
//                                    GlobalClass.BoxBusTime + "              " + "Admin of " + GlobalClass.BoxBusName + " " +
//                                    GlobalClass.BoxBusTime);
//                            admin_busaddress.setSelected(true);
//                        }
//                    }
//
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            };
//
//            ref.child("Admin").child("Location")
//                    .child(GlobalClass.BoxBusName)
//                    .child(GlobalClass.BoxBusTime)
//                    .addValueEventListener(listener);
//        }


        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (ref != null && listener != null)
                    ref.removeEventListener(listener);

            }
        }, 1200);
        //endregion

        circleImageView.setOnClickListener(this);
    }

    private void goThroughWeb() {
        fblink = "<a href='" + id + "'>" + getIntent().getStringExtra("name_info") + "</a>";


        html = Html.fromHtml(fblink);
        fb_content.setText(html);
        fb_content.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {

            final PackageInfo pInfo = context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            long versionCode;

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                versionCode = pInfo.getLongVersionCode(); // avoid huge version numbers and you will be ok
            } else {
                //noinspection deprecation
                versionCode = pInfo.versionCode;
            }
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + id;
            } else { //older versions of fb app
                return "goThroughWeb";
            }
        } catch (PackageManager.NameNotFoundException e) {
            return id; //normal web url
        }
    }


    public boolean isAppInstalled() {
        try {

            getApplicationContext().getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {

            return false;
        }
    }

    private void base64ToImage(String imageString, String size) {

//        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
//        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//        GlobalClass.bitMap=decodedImage;
//        propic.setImageBitmap(decodedImage);
        if (size.equals("gol"))
            Glide.with(adminprofile.this).load(Uri.parse(imageString))
                    .into(circleImageView);
        else
            Glide.with(adminprofile.this).load(Uri.parse(imageString))
                    .into(img);
    }

    private void openCustomFeedDialog() {


        // custom dialog
        final Dialog dialog = new Dialog(adminprofile.this);
        dialog.setContentView(R.layout.feedtext_layout);

        TextView cancel = dialog.findViewById(R.id.tvcancel);
        TextView feed = dialog.findViewById(R.id.tvFeed);
        lekhar_text = dialog.findViewById(R.id.feed_text);

        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isNetworkConnected(getApplicationContext())) {
                    toastIconInfo("Internet connection required");
                    return;
                }
                if (lekhar_text.getText().toString().trim().isEmpty()) {
                    toastIconInfo("Your feedback is empty");

                    return;
                }


                toastIconInfo("Feedback send");

                String id = String.valueOf(GlobalClass.clock + System.currentTimeMillis());



                firstActivity.admin_feedback.child(getString(R.string.Firebase_admins_feedback))
                        .child(feed_contact)
                        .child(id)
                        .child("text")
                        .setValue(lekhar_text.getText().toString());
                firstActivity.admin_feedback.child(getString(R.string.Firebase_admins_feedback))
                        .child(feed_contact)
                        .child(id)
                        .child("date")
                        .setValue(aj_date);

                dialog.dismiss();
                dialog.cancel();


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                dialog.cancel();

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


    @Override
    public void onClick(View v) {


        if (v == circleImageView) {


            openCustomDialog();

        }

    }

    public boolean isNetworkConnected(Context context) {

        prechecking = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        prechecking = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        prechecking = true;
                    }
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        prechecking = true;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        prechecking = true;
                    }
                }
            }
        }
        return prechecking;
    }

    @SuppressLint("RestrictedApi")
    public void openCustomDialog() {


        // custom dialog
        final Dialog dialog = new Dialog(adminprofile.this);
        dialog.setContentView(R.layout.propic);

        img = dialog.findViewById(R.id.propic_large);


        if (feed_contact.equals(GlobalClass.feedContactPref.getString("feedContact", "painai"))) {

            SharedPreferences pic_show = getSharedPreferences(getString
                    (R.string.pic_show_pref_file), MODE_PRIVATE);
            GlobalClass.PicStringPref = getSharedPreferences(getString
                    (R.string.globalclass_picString_name_pref_file), MODE_PRIVATE);
            GlobalClass.imgString = GlobalClass.PicStringPref.
                    getString(getString(R.string.profile_pic_name_getter), "Not provided");


            //region first ok version
            if (pic_show.getString("pic_show_name", "zzz").equals("true")) {

                if (!GlobalClass.imgString.equals("Not provided")) {
                    base64ToImage(GlobalClass.imgString, "boro");

                } else {
                    img.setImageResource(R.drawable.default_user);


                }

            } else {

                img.setImageResource(R.drawable.default_user);
            }
            //endregion

        } else {
            if (getIntent().getStringExtra("picString_info").equals("Not provided")) {
                img.setImageResource(R.drawable.default_user);
            } else {
                if(getIntent().getStringExtra("URLString_info").equals("Not provided"))
                    img.setImageResource(R.drawable.default_user);

                else
                {
                    Glide.with(adminprofile.this).load(Uri.parse(getIntent().
                            getStringExtra("URLString_info")))
                            .into(img);
                }

            }
        }


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (WindowManager.LayoutParams.WRAP_CONTENT);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);


        dialog.show();
    }

}
