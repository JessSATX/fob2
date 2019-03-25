package the.fiesta.OysterBake.Companion;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Export extends AppCompatActivity {

    public TextView data;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        // set up connection to database
        dbRef = FirebaseDatabase.getInstance().getReference();

        data = findViewById(R.id.dataText);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.child("UserEmails").getValue().toString();
                data.setText(value);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Write to file
        String filename = "FOBdata.txt";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.toString().getBytes());
            outputStream.close();
            Toast.makeText(Export.this, "File Created", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(Export.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // read from file
        try {
            data.setText(openFileInput("FOBdata.txt").toString());
            data.setTextColor(Color.GREEN);
            data. setTextSize(20);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
