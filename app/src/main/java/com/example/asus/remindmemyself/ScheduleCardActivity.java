package com.example.asus.remindmemyself;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScheduleCardActivity extends AppCompatActivity {

    private RecyclerView mblogList;
    private DatabaseReference databaseReference;
    private  LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerOptions<Schedule_CardView> options;
    private FirebaseRecyclerAdapter<Schedule_CardView,ViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_card);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Bus_Schedule");
        mblogList=(RecyclerView)findViewById(R.id.blog_list);
        mblogList.setHasFixedSize(true);
        mblogList.setLayoutManager(new LinearLayoutManager(this));

        options = new FirebaseRecyclerOptions.Builder<Schedule_CardView>()
                  .setQuery(databaseReference,Schedule_CardView.class).build();

        adapter= new FirebaseRecyclerAdapter<Schedule_CardView, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Schedule_CardView model) {

                holder.from.setText(model.getFrom());
                holder.to.setText(model.getTo());
                holder.type.setText(model.getType());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.schedule_image_layout,viewGroup,false);

                return new ViewHolder(view);
            }
        };

    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView time,from,to,type;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time=(TextView)itemView.findViewById(R.id.textViewTime);
            from= (TextView)itemView.findViewById(R.id.textViewFrom);
            to=(TextView)itemView.findViewById(R.id.textViewTo);
            type=(TextView)itemView.findViewById(R.id.textViewType);

        }
    }
}
