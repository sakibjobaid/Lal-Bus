package com.anything.asus.remindmemyself;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MasterPasswordAdapter extends
        RecyclerView.Adapter<MasterPasswordAdapter.PassViewHolder> {

    List<master_password_cardview> list;
    //List<String>passString=new ArrayList<String>();

    Context context;

    public MasterPasswordAdapter(List<master_password_cardview> listitems, Context context) {
        this.list=listitems;
        this.context=context;

    }

    @NonNull
    @Override
    public PassViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context)
                .inflate(R.layout.master_pass_layout,null);
        return new PassViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PassViewHolder viewHolder, int position) {


        master_password_cardview ls=list.get(position);
        String pass1=ls.getPass();
        String busName=ls.getBusName();
        viewHolder.masterBusPassword.setText(pass1);
        viewHolder.masterBusName.setText(busName);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PassViewHolder extends RecyclerView.ViewHolder{

        CardView linearLayout ;
        TextView masterBusName,masterBusPassword;
        public PassViewHolder(@NonNull View itemView) {
            super(itemView);

            masterBusName=(TextView) itemView.findViewById(R.id.masterBusName);

            masterBusPassword=(TextView)itemView.findViewById(R.id.masterBusPassword);

            linearLayout=(CardView) itemView.findViewById(R.id.hellox22);
//            if(GlobalClass.edit)
//            pass.setEnabled(false);
//            else
//                pass.setEnabled(true);


        }
    }
}
