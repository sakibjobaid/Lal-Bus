package com.anything.asus.remindmemyself;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecylerViewActivity extends AppCompatActivity {

    public static boolean is_in_checked_mode,is_all_checked;
    RecyclerView recyclerView;
    ScheduleAdapter scheduleAdapter;
    //feedback_adapter feedback_adapter1;
    profile_info_adapter profileInfoAdapter;
    List<Schedule> scheduleList;
    public static List<feedback_list_class> feed_List;
    List<admin_profile_info> profile_info_list;
    SharedPreferences stoppage_pref;
    String title;
    public static RecyclerView.Adapter adapter;
    public static Toolbar toolbar;

    static int counter=1;

    android.support.v7.app.ActionBar actionBar;

    Context context;

    CheckBox cb;

    Menu menu;

    public static DatabaseReference top10 = null;
    public static ValueEventListener val_top10 = null;
    int top10count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyler_view);
        stoppage_pref = getSharedPreferences(getString(R.string.stoppage_pref_file), MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        is_in_checked_mode = false;
        is_all_checked=false;
        context = this;


        //toolText=findViewById(R.id.tool_text);
        top10 = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.Firebase_admin_profile))
                .child(GlobalClass.BoxBusName);
        val_top10 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null)
                    top10count = (int) dataSnapshot.getChildrenCount();

                if (top10count >= 10)
                    toolbar.setTitle("Top 10 admins of " + GlobalClass.BoxBusName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        title = getIntent().getStringExtra("name");
        switch (title) {
            case "Bus Schedule":
                this.setTitle("Bus Schedule");
                break;
            case "Route And Stoppage":
                this.setTitle("Route");
                break;
            case "Admin profile":
                this.setTitle("Top admins of " + GlobalClass.BoxBusName);
                break;
            case "My Review":
                this.setTitle("My Review");
                break;
        }

        if (title.equals("Bus Schedule")) {
            scheduleList = new ArrayList<>();
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            int x = GlobalClass.BusNameKey.size();
            for (int i = 0; i < x; i++)
                scheduleList.add(new Schedule(GlobalClass.BusNameKey.get(i)));

            scheduleAdapter = new ScheduleAdapter(this, scheduleList);
            recyclerView.setAdapter(scheduleAdapter);
        } else if (title.equals("Route And Stoppage")) {
            scheduleList = new ArrayList<>();
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            int x = GlobalClass.BusNameKey.size();
            for (int i = 0; i < x; i++)
                scheduleList.add(new Schedule(" " + GlobalClass.BusNameKey.get(i)));
            scheduleAdapter = new ScheduleAdapter(this, scheduleList);
            recyclerView.setAdapter(scheduleAdapter);
        } else if (title.equals("Admin profile")) {
            profile_info_list = new ArrayList<>();
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            int x = GlobalClass.BusNameKey.size();
            List<firstActivity.profile_class> itemsList = GlobalClass.top10list.get(GlobalClass.BoxBusName);

            profileInfoAdapter = new profile_info_adapter(this, itemsList);
            recyclerView.setAdapter(profileInfoAdapter);

        } else if (title.equals("My Review")) {

            feed_List = new ArrayList<>();
            feed_List.clear();

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


            for (int i = firstActivity.feed_count - 1; i >= 0; i--) {
                feed_List.add(new feedback_list_class(firstActivity.feed_array[i].feed_class_text
                        , firstActivity.feed_array[i].feed_class_date
                        , firstActivity.feed_array[i].feed_class_id));


            }

            firstActivity.feed_count=0;



            adapter = new feedback_adapter(this, feed_List);
            recyclerView.setAdapter(adapter);

        }

        actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (title.equals("My Review")) {
            getMenuInflater().inflate(R.menu.toolbar_tarangaup, menu);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (title.equals("My Review")) {
            switch (item.getItemId()) {
                case R.id.tool_delete_feedback:
                    toolbar.getMenu().clear();
                    toolbar.setTitle("My Review");
                    is_in_checked_mode = true;
                    is_all_checked=false;
                    toolbar.inflateMenu(R.menu.delete_confirmation);

                    feedback_adapter.counter=0;

                    feedback_adapter.deleted_ones.clear();

                    if(feedback_adapter.counter>1)
                    toolbar.setTitle(feedback_adapter.counter+" items selected");
                    else
                        toolbar.setTitle(feedback_adapter.counter+" item selected");


                    adapter.notifyDataSetChanged();

                    break;

                case R.id.tool_delete_confirmation:

                    if(feedback_adapter.counter==0)
                    {
                        toastIconInfo("No item is selected");

                    }
                    else
                        alertDialog();
                    break;

                case R.id.tool_delete_all:



                    if(feedback_adapter.counter!=RecylerViewActivity.feed_List.size())
                    {

                        feedback_adapter.deleted_ones.clear();
                        feedback_adapter.deleted_ones=new ArrayList<>(feed_List);
                        //Collections.copy(feedback_adapter1.deleted_ones,feed_List);

                        counter=2;
                        feedback_adapter.counter=RecylerViewActivity.feed_List.size();
                        is_all_checked=true;
                        if(feedback_adapter.counter>1)
                        toolbar.setTitle(feedback_adapter.counter+" items selected");
                        else
                            toolbar.setTitle(feedback_adapter.counter+" item selected");

                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        feedback_adapter.deleted_ones.clear();
                        counter=1;
                        feedback_adapter.counter=0;
                        toolbar.setTitle(feedback_adapter.counter+" item selected");

                            is_all_checked=false;
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }

        }
        return super.onOptionsItemSelected(item);

    }

    private void toastIconInfo(String purpose) {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(purpose);
        ((CardView) custom_view.findViewById(R.id.parent_view2)).setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));

        toast.setView(custom_view);
        toast.show();
    }



    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RecylerViewActivity.this);
        builder.setTitle("Confirmation");
        if (feedback_adapter.counter > 1)
            builder.setMessage("Want to delete " + feedback_adapter.counter + " items?");
        else
            builder.setMessage("Want to delete " + feedback_adapter.counter + " item?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                is_in_checked_mode = false;

                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.toolbar_tarangaup);
                toolbar.setTitle("My Review");


                feedback_adapter feeder = new feedback_adapter();

                if(feedback_adapter.counter==RecylerViewActivity.feed_List.size())
                {
                    feeder.updateAdapter(RecylerViewActivity.feed_List);
                }
                else
                feeder.updateAdapter(feedback_adapter.deleted_ones);

                feedback_adapter.counter = 0;


                //feeder.deleted_ones.clear();
                adapter = new feedback_adapter(context, feed_List);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (feed_List.isEmpty()) {
                    finish();


                }

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
        builder.setCancelable(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        SharedPreferences busNamePref = getSharedPreferences(getString(R.string.bus_name_pref_file), MODE_PRIVATE);
        SharedPreferences busTimePref = getSharedPreferences(getString(R.string.bus_time_pref_file), MODE_PRIVATE);
        SharedPreferences.Editor preEditor = busTimePref.edit();
        preEditor.putString(getString(R.string.bus_time_getter), GlobalClass.BusTime);
        preEditor.apply();

        SharedPreferences.Editor pre1Editor = busNamePref.edit();
        pre1Editor.putString(getString(R.string.bus_name_getter), GlobalClass.BusName);
        pre1Editor.apply();

        super.onStop();
    }


}