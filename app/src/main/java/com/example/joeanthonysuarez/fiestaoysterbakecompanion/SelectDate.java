package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectDate extends AppCompatActivity {

    public static String stageNum;
    Button fri, sat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        stageNum = (String) bundle.get("STAGE_NUM");

        fri = findViewById(R.id.friB);
        sat = findViewById(R.id.satB);


        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotosch = new Intent (SelectDate.this, ScheduleList.class);
                gotosch.putExtra("STAGE_NUM", stageNum);
                gotosch.putExtra("day", "1");
                startActivity(gotosch);
            }
        });

        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotosch = new Intent (SelectDate.this, ScheduleList.class);
                gotosch.putExtra("STAGE_NUM", stageNum);
                gotosch.putExtra("day", "2");
                startActivity(gotosch);
            }
        });


    }
}
