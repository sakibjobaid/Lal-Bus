package com.anything.asus.remindmemyself;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PasswordAdapter2 extends RecyclerView.Adapter<PasswordAdapter2.PassViewHolder> {

    List<Password_CardView> list;
    List<String>passString=new ArrayList<String>();

    Context context;

    public PasswordAdapter2(List<Password_CardView> listitems, Context context) {
        this.list=listitems;
        this.context=context;

    }

    @NonNull
    @Override
    public PassViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context)
                .inflate(R.layout.password_layout,null);
        return new PassViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PassViewHolder viewHolder, int position) {


        //final Password_CardView password =list.get(position);
        Password_CardView ls=list.get(position);
        String pass1=ls.getPass();
        passString.add(pass1);
        viewHolder.pass.setText(pass1);
        viewHolder.time.setText(ls.getTime());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PassViewHolder extends RecyclerView.ViewHolder{

        CardView linearLayout ;
        TextView time;
        TextView pass;
        public PassViewHolder(@NonNull View itemView) {
            super(itemView);

            pass=(TextView) itemView.findViewById(R.id.textsViewPass);

            time=(TextView)itemView.findViewById(R.id.timesTextView);

            linearLayout=(CardView) itemView.findViewById(R.id.hellox);
//            if(GlobalClass.edit)
//            pass.setEnabled(false);
//            else
//                pass.setEnabled(true);


        }
    }
}
