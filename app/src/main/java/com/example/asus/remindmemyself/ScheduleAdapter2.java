package com.example.asus.remindmemyself;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ScheduleAdapter2  extends RecyclerView.Adapter<ScheduleAdapter2.ViewHolder>{

    List<Schedule_CardView> list;
    Context context;
    public ScheduleAdapter2(List<Schedule_CardView> listitems, Context context) {
                  this.list=listitems;
                  this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.schedule_image_layout,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Schedule_CardView ls=list.get(i);
        viewHolder.from.setText(ls.getFrom());
        viewHolder.to.setText(ls.getTo());
        viewHolder.type.setText(ls.getType());
        viewHolder.time.setText(ls.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView from,to,time,type;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            from=(TextView)itemView.findViewById(R.id.textViewFrom);
            to=(TextView) itemView.findViewById(R.id.textViewTo);
            time=(TextView)itemView.findViewById(R.id.textViewTime);
            type=(TextView)itemView.findViewById(R.id.textViewType);
        }
    }
}
