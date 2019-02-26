package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminHomePage extends AppCompatActivity {
    Button bmap, bshowtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        bmap = (Button) findViewById(R.id.bMaps);
        bshowtime = (Button) findViewById(R.id.bShowtimes);

        bmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoSelectdate = new Intent(AdminHomePage.this, DateSelect.class);

                gotoSelectdate.putExtra("Type", "maps");

                startActivity(gotoSelectdate);
            }
        });

        bshowtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoSelectdate = new Intent(AdminHomePage.this, DateSelect.class);

                gotoSelectdate.putExtra("Type", "showtimes");

                startActivity(gotoSelectdate);
            }
        });
    }
}
