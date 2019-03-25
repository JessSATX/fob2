package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SelectedArtist extends AppCompatActivity {

    public static String name;
    public static String bio;

    TextView nameTitle;
    TextView bioText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_artist);

        // Get data sent from ScheduleList.java activity.
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            name = (String) bundle.get("NAME");

        }

        // Set TextView title to to the artist's name.
        nameTitle = findViewById(R.id.artist_name);
        nameTitle.setText(name);

    }
}
