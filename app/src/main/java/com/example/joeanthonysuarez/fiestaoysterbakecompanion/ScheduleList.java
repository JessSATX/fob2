package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScheduleList extends AppCompatActivity {

    public static String stageNum;
    public static String day;

    private DatabaseReference fbRef;
    private FirebaseDatabase fbDatabase;
    private ListView artistsListView;
    private TextView stageTitle;

    private DataSnapshot dataSnapshot;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            stageNum = (String) bundle.get("STAGE_NUM");
            day = (String) bundle.get("day");
        }

        TextView stageTitle = (TextView) findViewById(R.id.txTitle);
        artistsListView = (ListView) findViewById(R.id.Artists_List);

        // Set the name of the stage.
        stageTitle.setText("STAGE " + stageNum);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        artistsListView.setAdapter(adapter);

        final ArrayList<String> artistsNamesList = new ArrayList<String>();
        final ArrayList<String> artistBioList = new ArrayList<String>();

        // Set up connection to database.
        fbRef = FirebaseDatabase.getInstance().getReference();

        // Specify the path one must take to retrieve the artist information.
        fbRef.child("STAGES").child(stageNum).child("ARTISTS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("NAME").getValue().toString();
                    String startTime = ds.child("START_TIME").getValue().toString();
                    String endTime = ds.child("END_TIME").getValue().toString();
                    String Artday = ds.child("DAY").getValue(String.class);
                    Boolean is_active = ds.child("STATUS").getValue(Boolean.class);


                    if (Artday.equals(day) && is_active) {
                        artistsNamesList.add(name);
                        arrayList.add(name + "\t\t\t\t" + startTime + " - " + endTime);
                        adapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Event listener for when an item in the ListView is clicked or tapped
        artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the name and the bio of the selected artist.
                String name = artistsNamesList.get(position);

                // Send the name and bio strings of the selected artist to SelectedArtist.java activity
                Intent artistSelectIntent = new Intent(ScheduleList.this, SelectedArtist.class);

                artistSelectIntent.putExtra("NAME", name);

                startActivity(artistSelectIntent);
            }
        });
    }
}
