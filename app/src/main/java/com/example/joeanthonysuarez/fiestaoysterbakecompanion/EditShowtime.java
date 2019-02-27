package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditShowtime extends AppCompatActivity {
    public String Artistname, Showstart, Showend, day;
    EditText name, start, end, showDay;
    Button saveinfo, cancel;
    private DatabaseReference fb;
    public static String stage, artistID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_showtime);

        // set up connection to firebase database
        fb = FirebaseDatabase.getInstance().getReference();


        // get info from AdminSchedule Activity
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            stage = (String) bundle.get("stage");
            artistID = (String) bundle.get ("ID");
            Artistname = (String) bundle.get("name");
            Showstart = (String) bundle.get ("start");
            Showend = (String) bundle.get ("end");
            day = (String) bundle.get("day");

        }

        saveinfo = (Button) findViewById(R.id.bSave);
        cancel = (Button) findViewById(R.id.bCancel);
        name = (EditText) findViewById(R.id.etName);
        start = (EditText) findViewById(R.id.etStartTime);
        end = (EditText) findViewById(R.id.etEndTime);
        showDay = (EditText) findViewById(R.id.etDay);


        // set text on screen
        name.setText(Artistname);
        start.setText(Showstart);
        end.setText(Showend);
        showDay.setText(day);


        // save infp onto database
        saveinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fb.child("STAGES").child(stage).child("ARTISTS").child(artistID).child("NAME").setValue(name.getText().toString());
                    fb.child("STAGES").child(stage).child("ARTISTS").child(artistID).child("START_TIME").setValue(start.getText().toString());
                    fb.child("STAGES").child(stage).child("ARTISTS").child(artistID).child("END_TIME").setValue(end.getText().toString());
                    fb.child("STAGES").child(stage).child("ARTISTS").child(artistID).child("DAY").setValue(showDay.getText().toString());
                    Toast.makeText(EditShowtime.this, "Changed saved succesfully!", Toast.LENGTH_SHORT).show();

                    Intent goback = new Intent (EditShowtime.this, AdminSchedule.class);
                    startActivity(goback);
                }

                catch (Exception e){
                    Toast.makeText(EditShowtime.this, "Information could not be updated. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fb.child("STAGES").child(stage).child("ARTISTS").child(artistID).child("STATUS").setValue(false);
                Toast.makeText(EditShowtime.this, "Showtime Canceled", Toast.LENGTH_SHORT).show();
                Intent goback = new Intent (EditShowtime.this, AdminSchedule.class);
                startActivity(goback);

            }
        });




    }
}
