package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminSchedule extends AppCompatActivity {

    TextView title;
    ListView artistsListView;
    Button createshowtime;
    public static String stage;
    public static String day;

    private DatabaseReference fb;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_schedule);

        // set up firebase database connection
        fb = FirebaseDatabase.getInstance().getReference();


        // get the stage number from AdminStageList Activity
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            stage = (String) bundle.get("Stage");
            day = (String) bundle.get("day");
        }

        // find title textview and artists listview and create button
        title = (TextView) findViewById(R.id.txtAStext);
        artistsListView = (ListView) findViewById(R.id.lsArtists);
        createshowtime = (Button) findViewById(R.id.bAdd);

        title.setText("Artists performing on stage" + stage);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        artistsListView.setAdapter(adapter);

        final ArrayList<String> artistID = new ArrayList<String>();
        final ArrayList<String> artistsNamesList = new ArrayList<String>();
        final ArrayList<String> artistsStartTime = new ArrayList<String>();
        final ArrayList<String> artistDate = new ArrayList<String>();
        final ArrayList<String> artistEndTime = new ArrayList<String>();
        final ArrayList<String> artistDay = new ArrayList<>();


        fb.child("STAGES").child(stage).child("ARTISTS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.getKey();
                    String name = ds.child("NAME").getValue().toString();
                    String startTime = ds.child("START_TIME").getValue().toString();
                    String endTime = ds.child("END_TIME").getValue().toString();
                    String Artday = ds.child("DAY").getValue().toString();
                    String is_active = ds.child("STATUS").getValue().toString();

                    // filter by day selected
                    if (Artday.equals(day) && is_active.equals("true"))
                    {
                        arrayList.add(name + "\t\t\t\t" + startTime + " - " + endTime);

                        artistID.add(id);
                        artistsNamesList.add(name);
                        artistsStartTime.add(startTime);
                        artistEndTime.add(endTime);
                        artistDay.add(Artday);


                        adapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        createshowtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoCreate = new Intent (AdminSchedule.this, CreateShowtime.class);
                gotoCreate.putExtra("Stage", stage);
                startActivity(gotoCreate);
            }
        });

        artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String artID = artistID.get(position);
                String name = artistsNamesList.get(position);
                String start = artistsStartTime.get(position);
                String end = artistEndTime.get(position);
                String day = artistDay.get(position);

                Intent gotoEditShowtime = new Intent (AdminSchedule.this, EditShowtime.class);

                gotoEditShowtime.putExtra("stage", stage);
                gotoEditShowtime.putExtra("ID", artID);
                gotoEditShowtime.putExtra("name", name);
                gotoEditShowtime.putExtra("start", start);
                gotoEditShowtime.putExtra("end", end);
                gotoEditShowtime.putExtra("day", day);

                startActivity(gotoEditShowtime);

            }
        });


    }
}
