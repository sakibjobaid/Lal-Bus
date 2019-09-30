package com.anything.asus.remindmemyself;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class timeline_info_adapter extends RecyclerView.Adapter<timeline_info_adapter.timeline_info_ViewHolder>{


    static int countImage=0;
    private Context cntxt;
    private List<firstActivity.timeline_class> timeline_info_list;
    RequestOptions requestOptions;

    public timeline_info_adapter(Context cntxt, List<firstActivity.timeline_class> timeline_infoList) {
        this.cntxt = cntxt;
        this.timeline_info_list = timeline_infoList;

    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override

    public timeline_info_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(cntxt);
        View view = inflater.inflate(R.layout.timeline_list_layout,null);
        timeline_info_ViewHolder holder = new timeline_info_ViewHolder(view);

//        requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.default_user);

        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override

    public void onBindViewHolder(@NonNull timeline_info_ViewHolder profileViewHolder, int position) {

        final firstActivity.timeline_class timeline_Info = timeline_info_list.get(position);
        profileViewHolder.busNameTime.setText(timeline_Info.busName_timeline+" "+timeline_Info.busTime_timeline);
        profileViewHolder.busFromTo.setText(timeline_Info.busFrom_timeline+" - "+timeline_Info.busTo_timeline);


         profileViewHolder.countdown_timer.setText("hello");

       // profileViewHolder.countdown_timer.setVisibility(View.GONE);

//        profileViewHolder.routeImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return timeline_info_list.size();
    }

    class timeline_info_ViewHolder extends RecyclerView.ViewHolder
    {

        CircleImageView routeImage;
        TextView busNameTime,busFromTo,countdown_timer;

        public timeline_info_ViewHolder(@NonNull View itemView) {
            super(itemView);

            busNameTime=itemView.findViewById(R.id.Name_Time);
            busFromTo=itemView.findViewById(R.id.From_To);
            countdown_timer=itemView.findViewById(R.id.Timer);
            //routeImage=itemView.findViewById(R.id.fab_route);


        }
    }
}