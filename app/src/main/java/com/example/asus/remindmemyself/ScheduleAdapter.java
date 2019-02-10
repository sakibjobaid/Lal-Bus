package com.example.asus.remindmemyself;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>{

    private Context cntxt;
    private List<Schedule> ScheduleList;

    public ScheduleAdapter(Context cntxt, List<Schedule> scheduleList) {
        this.cntxt = cntxt;
        ScheduleList = scheduleList;
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
    public void onBindViewHolder(@NonNull ScheduleViewHolder scheduleViewHolder, int position) {

        Schedule schedule = ScheduleList.get(position);
        scheduleViewHolder.tv.setText(schedule.getName());
    }

    @Override
    public int getItemCount() {
        return ScheduleList.size();
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder
     {

         TextView tv;
         public ScheduleViewHolder(@NonNull View itemView) {
             super(itemView);
             tv=itemView.findViewById(R.id.textViewTitle);
         }
     }
}
