package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AdminStageList extends AppCompatActivity {

    public static String day;
    TextView title;
    Button bstage1, bstage2, bstage3, bstage4, bstage5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stage_list);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        day = (String) bundle.get("day");

        title = (TextView) findViewById(R.id.txtStagetitle);
        bstage1 = (Button) findViewById(R.id.bStage1);
        bstage2 = (Button) findViewById(R.id.bStage2);
        bstage3 = (Button) findViewById(R.id.bStage3);
        bstage4 = (Button) findViewById(R.id.bStage4);
        bstage5 = (Button) findViewById(R.id.bStage5);


        title.setText("Stages for " + (day.equals("1") ? "Friday" : "Saturday"));


        bstage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToSchedule = new Intent(AdminStageList.this, AdminSchedule.class);

                goToSchedule.putExtra("Stage", "1");
                goToSchedule.putExtra("day", day);

                startActivity(goToSchedule);
            }
        });


        bstage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToSchedule = new Intent(AdminStageList.this, AdminSchedule.class);

                goToSchedule.putExtra("Stage", "2");
                goToSchedule.putExtra("day", day);


                startActivity(goToSchedule);
            }
        });

        bstage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToSchedule = new Intent(AdminStageList.this, AdminSchedule.class);

                goToSchedule.putExtra("Stage", "3");
                goToSchedule.putExtra("day", day);

                startActivity(goToSchedule);
            }
        });

        bstage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToSchedule = new Intent(AdminStageList.this, AdminSchedule.class);

                goToSchedule.putExtra("Stage", "4");
                goToSchedule.putExtra("day", day);

                startActivity(goToSchedule);
            }
        });

        bstage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToSchedule = new Intent(AdminStageList.this, AdminSchedule.class);

                goToSchedule.putExtra("Stage", "5");
                goToSchedule.putExtra("day", day);

                startActivity(goToSchedule);
            }
        });
    }
}