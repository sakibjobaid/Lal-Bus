package com.example.asus.remindmemyself;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ScheduleImage extends AppCompatActivity {

    int count=0;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
//    DatabaseReference databaseReference;
//    FirebaseRecyclerOptions<Schedule_CardView> options;
//    FirebaseRecyclerAdapter<Schedule_CardView,CardViewHolder> adapter;
    String name,temps;
    List<Schedule_CardView> listitems;
    public SharedPreferences references;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_image);
         name= getIntent().getStringExtra("name");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            this.setTitle(name);


        references = getSharedPreferences("hellojobaid", MODE_PRIVATE);
        temps=references.getString(name,"");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView= (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);
        listitems= new ArrayList<>();
        StringTokenizer ppx = new StringTokenizer(temps,"$");
        while(ppx.hasMoreElements())
        {
            String single= (String) ppx.nextElement();
            StringTokenizer stx = new StringTokenizer(single,"#");
            Schedule_CardView abc= new Schedule_CardView(
                    (String)stx.nextElement(),(String)stx.nextElement(),
                    (String)stx.nextElement(),(String)stx.nextElement());
            listitems.add(abc);
        }

//        for(int i=0;i<x;i++)
//        {
//            String tempox=(String) ppx.nextElement();
//
//            a=Integer.parseInt((String) stx.nextElement());
//            Schedule_CardView abc= new Schedule_CardView(
//                    "time ","from","to","type"
//            );
//            listitems.add(abc);
//        }
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Bus_Schedule");
        //Query query = FirebaseDatabase.getInstance().getReference().child("Bus_Schedule").child("Taranga");
        //=databaseReference.keepSynced(true);

//        options=new FirebaseRecyclerOptions.Builder<Schedule_CardView>()
//                .setQuery(databaseReference.child(name), new SnapshotParser<Schedule_CardView>() {
//                    @NonNull
//                    @Override
//                    public Schedule_CardView parseSnapshot(@NonNull DataSnapshot snapshot) {
//
//                        Log.d("toot toot",String.valueOf(snapshot.getKey()));
////                        if(snapshot.getValue().equals("Taranga"))
////                        {
//
//                        return new Schedule_CardView(String.valueOf(snapshot.child("time").getValue()),
//                                String.valueOf(snapshot.child("from").getValue()),
//                                String.valueOf(snapshot.child("to").getValue()),
//                                String.valueOf(snapshot.child("type").getValue()));
//                        //}
//////
//                       //return new Schedule_CardView("id1","id2","id3","id4");
//                    }
//                }).build();
//
//        adapter= new FirebaseRecyclerAdapter<Schedule_CardView, CardViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull CardViewHolder holder, int position, @NonNull Schedule_CardView model) {
//
//                //Toast.makeText(ScheduleImage.this,"saj",Toast.LENGTH_LONG).show();
//                Log.d("pap para rai", "onBindViewHolder: ");
//                holder.time.setText(model.getTime());
//                holder.from.setText(model.getFrom());
//                holder.to.setText(model.getTo());
//                holder.type.setText(model.getType());
//            }
//
//
//            @NonNull
//            @Override
//            public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//
//                //Toast.makeText(ScheduleImage.this,"sajOnCreate",Toast.LENGTH_LONG).show();
//                View view= LayoutInflater.from(viewGroup.getContext())
//                        .inflate(R.layout.schedule_image_layout,viewGroup,false);
//                return new CardViewHolder(view);
//            }
//        };
//
//        adapter.startListening();
        adapter= new ScheduleAdapter2(listitems,this);

        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}