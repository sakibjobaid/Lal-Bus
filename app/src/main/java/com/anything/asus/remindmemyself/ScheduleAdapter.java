package com.anything.asus.remindmemyself;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>{


    private Context cntxt;
    private List<Schedule> ScheduleList;
    Activity activity;


    public ScheduleAdapter(Context cntxt, List<Schedule> scheduleList) {
        this.cntxt = cntxt;
        ScheduleList = scheduleList;
        activity=(Activity) cntxt;
    }

    @NonNull
    @Override

    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(cntxt);
        View view = inflater.inflate(R.layout.list_layout,null);
        ScheduleViewHolder holder = new ScheduleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ScheduleViewHolder scheduleViewHolder, int position) {

        final Schedule schedule = ScheduleList.get(position);
        scheduleViewHolder.tv.setText(schedule.getName());
        scheduleViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(!(schedule.getName().charAt(0) ==' '))
                    {
                        Intent intent = new Intent(v.getContext(),ScheduleImage.class);
                        intent.putExtra("name",schedule.getName());
                        v.getContext().startActivity(intent);
                    }

                    else if(schedule.getName().charAt(0) ==' ')
                {

                    Intent intent = new Intent(v.getContext(),MapsActivity.class);
                    intent.putExtra("name",schedule.getName());
                    v.getContext().startActivity(intent);
                    activity.overridePendingTransition(R.anim.slidein_from_left,R.anim.slidein_from_right);

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return ScheduleList.size();
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder
    {

        LinearLayout linearLayout ;
        TextView tv;
        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);

            tv=itemView.findViewById(R.id.textViewTitle);
            linearLayout=itemView.findViewById(R.id.parentLayout);
        }
    }
}