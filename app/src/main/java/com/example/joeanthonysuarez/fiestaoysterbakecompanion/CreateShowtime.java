package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateShowtime extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    public static String stage;

    EditText name, start, end;
    Button create;

    public String day;

    Spinner daySelect;

    private DatabaseReference fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_showtime);

        // set up database connection
        fb = FirebaseDatabase.getInstance().getReference();

        // get the stage number from Admin Schedule activity
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            stage = (String) bundle.get("Stage");
        }


        daySelect = (Spinner) findViewById(R.id.sDaySelect);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.day_select, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySelect.setAdapter(adapter);
        daySelect.setOnItemSelectedListener(this);

// get the user inputted data
        name = (EditText) findViewById(R.id.etName);
        start = (EditText) findViewById(R.id.etStartTime);
        end = (EditText) findViewById(R.id.etEndTime);
        create = (Button) findViewById(R.id.bcreate);


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Artistname = name.getText().toString().trim();
                String startTime = start.getText().toString().trim();
                String endTime = end.getText().toString().trim();

                if (!Artistname.equals("") && !day.equals("") & !startTime.equals("") && !endTime.equals("")){

                    try {
                        String key = fb.child("STAGES").child(stage).child("ARTISTS").push().getKey();
                        fb.child("STAGES").child(stage).child("ARTISTS").child(key).child("NAME").setValue(name.getText().toString());
                        fb.child("STAGES").child(stage).child("ARTISTS").child(key).child("START_TIME").setValue(start.getText().toString());
                        fb.child("STAGES").child(stage).child("ARTISTS").child(key).child("END_TIME").setValue(end.getText().toString());
                        fb.child("STAGES").child(stage).child("ARTISTS").child(key).child("STATUS").setValue(true);

                        if (day.equals("Friday")) {
                            fb.child("STAGES").child(stage).child("ARTISTS").child(key).child("DAY").setValue("1");
                        } else if (day.equals("Saturday")) {
                            fb.child("STAGES").child(stage).child("ARTISTS").child(key).child("DAY").setValue("2");
                        }


                        Toast.makeText(CreateShowtime.this, "Showtime created!", Toast.LENGTH_SHORT).show();
                        Intent gotoList = new Intent(CreateShowtime.this, AdminHomePage.class);
                        startActivity(gotoList);
                    }
                    catch (Exception e){
                        Toast.makeText(CreateShowtime.this, "Could not create the showtime.", Toast.LENGTH_SHORT).show();
                    }

                }

                else {
                    Toast.makeText(CreateShowtime.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        day = adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
