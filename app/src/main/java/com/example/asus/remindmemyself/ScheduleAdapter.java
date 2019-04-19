package com.example.asus.remindmemyself;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        final Schedule schedule = ScheduleList.get(position);
        scheduleViewHolder.tv.setText(schedule.getName());
        scheduleViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(schedule.getName().equals("Taranga"))
                {
                    Intent intent = new Intent(v.getContext(),ScheduleImage.class);
                    intent.putExtra("name","Taranga");
                    v.getContext().startActivity(intent);
                }

                else if(schedule.getName().equals("Ullash"))
                {
                    Intent intent = new Intent(v.getContext(),ScheduleImage.class);
                    intent.putExtra("name","Ullash");
                    v.getContext().startActivity(intent);
                }
                else if(schedule.getName().equals("Boishakhi"))
                {
                    Intent intent = new Intent(v.getContext(),ScheduleImage.class);
                    intent.putExtra("name","Boishakhi");
                    v.getContext().startActivity(intent);
                }

                else if(schedule.getName().equals(" Taranga"))
                {

                    Intent intent = new Intent(v.getContext(),MapsActivity.class);
                    intent.putExtra("name"," Taranga");
                    v.getContext().startActivity(intent);
                }

                else if(schedule.getName().equals(" Khonika"))
                {

                    Intent intent = new Intent(v.getContext(),MapsActivity.class);
                    intent.putExtra("name"," Khonika");
                    v.getContext().startActivity(intent);
                }

                else if(schedule.getName().equals(" Boishakhi"))
                {

                    Intent intent = new Intent(v.getContext(),MapsActivity.class);
                    intent.putExtra("name"," Boishakhi");
                    v.getContext().startActivity(intent);
                }

                else if(schedule.getName().equals(" Falguni"))
                {

                    Intent intent = new Intent(v.getContext(),MapsActivity.class);
                    intent.putExtra("name"," Falguni");
                    v.getContext().startActivity(intent);
                }
                else if(schedule.getName().equals(" Ullash"))
                {

                    Intent intent = new Intent(v.getContext(),MapsActivity.class);
                    intent.putExtra("name"," Ullash");
                    v.getContext().startActivity(intent);
                }

                else if(schedule.getName().equals(" Srabon"))
                {

                    Intent intent = new Intent(v.getContext(),MapsActivity.class);
                    intent.putExtra("name"," Srabon");
                    v.getContext().startActivity(intent);
                }

                else if(schedule.getName().equals(" Kinchit"))
                {

                    Intent intent = new Intent(v.getContext(),MapsActivity.class);
                    intent.putExtra("name"," Kinchit");
                    v.getContext().startActivity(intent);
                }
                else if(schedule.getName().equals(" Choitaly"))
                {

                    Intent intent = new Intent(v.getContext(),MapsActivity.class);
                    intent.putExtra("name"," Choitaly");
                    v.getContext().startActivity(intent);
                }
                else if(schedule.getName().equals(" Anando"))
                {

                    Intent intent = new Intent(v.getContext(),MapsActivity.class);
                    intent.putExtra("name"," Anando");
                    v.getContext().startActivity(intent);
                }
                else if(schedule.getName().equals(" Boshonto"))
                {

                    Intent intent = new Intent(v.getContext(),MapsActivity.class);
                    intent.putExtra("name"," Boshonto");
                    v.getContext().startActivity(intent);
                }

                else if(schedule.getName().equals(" Ishakha"))
                {

                    Intent intent = new Intent(v.getContext(),MapsActivity.class);
                    intent.putExtra("name"," Ishakha");
                    v.getContext().startActivity(intent);
                }
                else if(schedule.getName().equals(" Hemonto"))
                {

                    Intent intent = new Intent(v.getContext(),MapsActivity.class);
                    intent.putExtra("name"," Hemonto");
                    v.getContext().startActivity(intent);
                }

                else
                    Toast.makeText(v.getContext(),"schedule will be added soon",Toast.LENGTH_LONG).show();


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