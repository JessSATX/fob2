package the.fiesta.OysterBake.Companion;

import android.content.Intent;
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

public class EditShowtime extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static String stage, artistID;
    public String Artistname, Showstart, Showend, day;

    EditText name, start, end, showDay;
    Button saveinfo, cancel;
    Spinner daySelect;

    private DatabaseReference fb;

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
            artistID = (String) bundle.get("ID");
            Artistname = (String) bundle.get("name");
            Showstart = (String) bundle.get("start");
            Showend = (String) bundle.get("end");
            day = (String) bundle.get("day");

        }

        daySelect = findViewById(R.id.sDay);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.day_select, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySelect.setAdapter(adapter);
        daySelect.setOnItemSelectedListener(this);

        saveinfo = findViewById(R.id.bSave);
        cancel = findViewById(R.id.bCancel);
        name = findViewById(R.id.etName);
        start = findViewById(R.id.etStartTime);
        end = findViewById(R.id.etEndTime);


        // set text on screen
        name.setText(Artistname);
        start.setText(Showstart);
        end.setText(Showend);

        if (day.equals("1")) {

            daySelect.setSelection(0);
        } else {
            daySelect.setSelection(1);
        }

        // save infp onto database
        saveinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fb.child("STAGES").child(stage).child("ARTISTS").child(artistID).child("NAME").setValue(name.getText().toString());
                    fb.child("STAGES").child(stage).child("ARTISTS").child(artistID).child("START_TIME").setValue(start.getText().toString());
                    fb.child("STAGES").child(stage).child("ARTISTS").child(artistID).child("END_TIME").setValue(end.getText().toString());

                    if (day.equals("Friday")) {
                        fb.child("STAGES").child(stage).child("ARTISTS").child(artistID).child("DAY").setValue("1");
                    } else if (day.equals("Saturday")) {
                        fb.child("STAGES").child(stage).child("ARTISTS").child(artistID).child("DAY").setValue("2");
                    }

                    Toast.makeText(EditShowtime.this, "Changed saved succesfully!", Toast.LENGTH_SHORT).show();

                    Intent goback = new Intent(EditShowtime.this, AdminHomePage.class);
                    startActivity(goback);
                } catch (Exception e) {
                    Toast.makeText(EditShowtime.this, "Information could not be updated. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fb.child("STAGES").child(stage).child("ARTISTS").child(artistID).child("STATUS").setValue(false);
                Toast.makeText(EditShowtime.this, "Showtime Canceled", Toast.LENGTH_SHORT).show();
                Intent goback = new Intent(EditShowtime.this, AdminSchedule.class);
                startActivity(goback);

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