package com.anything.asus.remindmemyself;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class profile_info_adapter extends RecyclerView.Adapter<profile_info_adapter.profile_infoViewHolder>{


    static int countImage=0;
    private Context cntxt;
    private List<firstActivity.profile_class> Profile_info_list;
    RequestOptions requestOptions;

    public profile_info_adapter(Context cntxt, List<firstActivity.profile_class> profile_infoList) {
        this.cntxt = cntxt;
        Profile_info_list = profile_infoList;




    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override

    public profile_infoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(cntxt);
        View view = inflater.inflate(R.layout.admin_profile_list,null);
        profile_infoViewHolder holder = new profile_infoViewHolder(view);




//        requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.default_user);


        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull  profile_infoViewHolder profileViewHolder, int position) {

        final firstActivity.profile_class adminProfileInfo = Profile_info_list.get(position);
        profileViewHolder.name_profile.setText(adminProfileInfo.name_class);
        profileViewHolder.dept_profile.setText(adminProfileInfo.dept_class);


        boolean lastAccompany_show_korbe=true;
        int i=0;
        for(String active_mobile: firstActivity.active_top10_mobileNo)
        {


            if(adminProfileInfo.feed_contact_class.equals(active_mobile))
            {

                lastAccompany_show_korbe=false;



                profileViewHolder.last_accompanied_profile.
                        setText("Active now :  "+firstActivity.active_top10_time.get(i));
                profileViewHolder.last_accompanied_profile.setTextColor(Color.BLUE);
                profileViewHolder.last_accompanied_profile.setSelected(true);
            }
            ++i;
        }
        if(lastAccompany_show_korbe)
        {
            profileViewHolder.last_accompanied_profile.
                    setText("Last accompanied : "+adminProfileInfo.last_accompanied);
            profileViewHolder.last_accompanied_profile.setTextColor(Color.GRAY);

        }
        profileViewHolder.trips_profile.setText(adminProfileInfo.trip_class);

        if(GlobalClass.admin_own_pic)
        {
            GlobalClass.admin_own_pic=false;
            SharedPreferences pic_show = cntxt.getSharedPreferences("hellopic_show", MODE_PRIVATE);

            GlobalClass.imgString = GlobalClass.PicStringPref.getString("profile_pic_name", "Not provided");
            if (pic_show.getString("pic_show_name","zzz").equals("true")) {

                if(!GlobalClass.imgString.equals("Not provided"))
                {

                    base64ToImage(GlobalClass.imgString,profileViewHolder);

                }
                else
                {
                    profileViewHolder.propic_profile.setImageResource(R.drawable.default_user);

                }

            }
            else {

                profileViewHolder.propic_profile.setImageResource(R.drawable.default_user);
            }

        }

        else
        {
            if(adminProfileInfo.pic_class.equals("Not provided"))
            {

                profileViewHolder.propic_profile.setImageResource(R.drawable.default_user);

            }

            else
            {
                    if(adminProfileInfo.imageURL_class!=null)
                        Glide.with(cntxt).load(Uri.parse(adminProfileInfo.imageURL_class))
                                //.apply(requestOptions)
                                .centerCrop()
                                .placeholder(R.drawable.default_user)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {


                                        return false;
                                    }
                                })
                                .into(profileViewHolder.propic_profile);


            }


        }





        profileViewHolder.linearLayout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(),adminprofile.class);
                    intent.putExtra("name_info",adminProfileInfo .name_class);
                    intent.putExtra("dept_info",adminProfileInfo .dept_class);
                    intent.putExtra("email_info",adminProfileInfo .mail_class);
                    intent.putExtra("contact_info",adminProfileInfo .contact_class);
                    intent.putExtra("fbId_info",adminProfileInfo .fbId_class);
                    intent.putExtra("URLString_info",adminProfileInfo.imageURL_class);
                    intent.putExtra("picString_info",adminProfileInfo.pic_class);
                    intent.putExtra("trip_info",adminProfileInfo .trip_class);
                    intent.putExtra("up_trip_info",adminProfileInfo .up_trip_class);
                    intent.putExtra("down_trip_info",adminProfileInfo .down_trip_class);
                    intent.putExtra("feed_contact",adminProfileInfo.feed_contact_class);
                    v.getContext().startActivity(intent);

            }
        });

    }
    private void base64ToImage(String imageString, profile_infoViewHolder profileViewHolder) {

        Glide.with(cntxt).load(Uri.parse(imageString))
                .into(profileViewHolder.propic_profile);
    }


    @Override
    public int getItemCount() {
        return Profile_info_list.size();
    }

    class profile_infoViewHolder extends RecyclerView.ViewHolder
    {

        CircleImageView propic_profile;
        TextView name_profile,dept_profile,last_accompanied_profile,trips_profile;
        MaterialRippleLayout linearLayout_profile ;
        public profile_infoViewHolder(@NonNull View itemView) {
            super(itemView);

            name_profile=itemView.findViewById(R.id.name);
            dept_profile=itemView.findViewById(R.id.dept);
            last_accompanied_profile=itemView.findViewById(R.id.last_accompanied);
            propic_profile=itemView.findViewById(R.id.propic_round);
            trips_profile=itemView.findViewById(R.id.trips);
            linearLayout_profile=itemView.findViewById(R.id.parent_layout);
        }
    }
}