package com.anything.asus.remindmemyself;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class feedback_adapter extends RecyclerView.Adapter<feedback_adapter.FeedbackViewHolder >{


    private Context cntxt;
    private List<feedback_list_class>fb;
    public static  List<feedback_list_class> deleted_ones;
    public static int counter=0;

    public feedback_adapter()
    {

    }


    public feedback_adapter(Context cntxt, List<feedback_list_class> fb) {

        this.cntxt = cntxt;
        this.fb=fb;
        counter=0;
    }

    @NonNull
    @Override

    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(cntxt);

        View view = inflater.inflate(R.layout.feedback_admin,null);

        FeedbackViewHolder holder = new FeedbackViewHolder(view);
        deleted_ones=new ArrayList<>();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedbackViewHolder feedViewHolder, final int position) {

        final feedback_list_class f_class = fb.get(position);
        feedViewHolder.feed.setText(f_class.getText());
        feedViewHolder.date.setText(f_class.getDate());

        feedViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(feedViewHolder.checkBox.isChecked())
                {
                    counter++;
                    deleted_ones.add(fb.get(position));
                }
                else
                {
                    counter--;
                    deleted_ones.remove(fb.get(position));
                }
                if(counter>1)
                    RecylerViewActivity.toolbar.setTitle(counter+" items selected");
                else
                    RecylerViewActivity.toolbar.setTitle(counter+" item selected");
            }
        });

        if(!RecylerViewActivity.is_in_checked_mode)
        {
            feedViewHolder.checkBox.setVisibility(View.GONE);
        }
        else
        {
            feedViewHolder.checkBox.setVisibility(View.VISIBLE);
            feedViewHolder.checkBox.setChecked(false);

        }

         if(RecylerViewActivity.is_all_checked)
        {
            feedViewHolder.checkBox.setChecked(true);
        }
        else
         {
             feedViewHolder.checkBox.setChecked(false);
         }


        feedViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





            }
        });




    }

    @Override
    public int getItemCount() {
        return fb.size();
    }

    class FeedbackViewHolder extends RecyclerView.ViewHolder  {


        CheckBox checkBox;
        RecylerViewActivity recylerViewActivity;
        TextView feed,date;
        LinearLayout linearLayout;
        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout=itemView.findViewById(R.id.parentadmin);
            feed=itemView.findViewById(R.id.textViewFeed);
            date=itemView.findViewById(R.id.textViewdate);
            checkBox=itemView.findViewById(R.id.del_checkbox);

            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick( View v)
                {
//                    RecylerViewActivity.is_in_checked_mode=true;
//                    RecylerViewActivity.toolbar.inflateMenu(R.menu.toolbar_tarangaup);
//                    RecylerViewActivity.toolbar.setTitle("0 item selected");
//                    RecylerViewActivity.adapter.notifyDataSetChanged();
                    return true;
                }

            });
        }


    }

    public void updateAdapter(List<feedback_list_class> list)
    {
        int list_size=list.size(),x=0;

        if(list.size()== RecylerViewActivity.feed_List.size())
        {


            for(feedback_list_class feed:list)
            {

                firstActivity.feedReference.child(feed.id).removeValue();
            }
            RecylerViewActivity.feed_List.clear();

        }
        else
        {
            for(feedback_list_class feed:list)
            {

                RecylerViewActivity.feed_List.remove(feed);
                firstActivity.feedReference.child(feed.id).removeValue();
            }
        }

    }
}