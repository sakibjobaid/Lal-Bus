package com.example.asus.remindmemyself;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class CardViewHolder extends RecyclerView.ViewHolder {

    public TextView time,from,to,type;
    public CardViewHolder(@NonNull View itemView) {
        super(itemView);
        time=(TextView)itemView.findViewById(R.id.textViewTime);
        from=(TextView)itemView.findViewById(R.id.textViewFrom);
        to= (TextView)itemView.findViewById(R.id.textViewTo);
        type=(TextView)itemView.findViewById(R.id.textViewType);
    }
}
