package the.fiesta.OysterBake.Companion;

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

    public ArrayList<String> artistID = new ArrayList<>();
    public ArrayList<String> artistsNamesList = new ArrayList<>();
    public ArrayList<String> artistsStartTime = new ArrayList<>();
    public ArrayList<String> artistEndTime = new ArrayList<>();
    public ArrayList<String> artistDay = new ArrayList<>();
    public ArrayList<Boolean> artistStatus = new ArrayList<>();

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
        title = findViewById(R.id.txtAStext);
        artistsListView = findViewById(R.id.lsArtists);
        createshowtime = findViewById(R.id.bAdd);

        title.setText("Artists performing on stage" + stage);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        artistsListView.setAdapter(adapter);


        fb.child("STAGES").child(stage).child("ARTISTS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String id = ds.getKey();
                    String name = ds.child("NAME").getValue(String.class);
                    String startTime = ds.child("START_TIME").getValue(String.class);
                    String endTime = ds.child("END_TIME").getValue(String.class);
                    String Artday = ds.child("DAY").getValue(String.class);
                    Boolean is_active = ds.child("STATUS").getValue(Boolean.class);

                    artistID.add(id);
                    artistsNamesList.add(name);
                    artistsStartTime.add(startTime);
                    artistEndTime.add(endTime);
                    artistDay.add(Artday);
                    artistStatus.add(is_active);

                    if (Artday.equals(day) && is_active) {
                        arrayList.add(name + "\t\t\t\t" + startTime + " - " + endTime);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        artistID.remove(id);
                        artistsNamesList.remove(name);
                        artistsStartTime.remove(startTime);
                        artistEndTime.remove(endTime);
                        artistDay.remove(Artday);
                        artistStatus.remove(is_active);
                    }
                }
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //filterArtists();

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

    private void filterArtists() {
        for (int i = artistsNamesList.size() - 1; i >= 0; i--) {
            if ( artistDay.get(i).equals(day)  && artistStatus.get(i).toString().equals("true") ) {
                String name = artistsNamesList.get(i);
                String start = artistsStartTime.get(i);
                String end = artistEndTime.get(i);
                arrayList.add(name + "\t\t\t\t" + start + " - " + end);
                adapter.notifyDataSetChanged();
            }
            else{
                artistID.remove(i);
                artistsNamesList.remove(i);
                artistsStartTime.remove(i);
                artistEndTime.remove(i);
                artistDay.remove(i);
                artistStatus.remove(i);

            }
        }
    }
}


