package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AdminStageList extends AppCompatActivity {

    TextView title;
    Button bstage1, bstage2, bstage3, bstage4, bstage5;
    public static String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stage_list);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        day = (String) bundle.get("day");

        title = (TextView) findViewById(R.id.txtStagetitle);
        bstage1 = (Button) findViewById (R.id.bStage1);
        bstage2 = (Button) findViewById(R.id.bStage2);
        bstage3 = (Button) findViewById(R.id.bStage3);
        bstage4 = (Button) findViewById (R.id.bStage4);
        bstage5 = (Button) findViewById(R.id.bStage5);


        title.setText("Stages for " + day);


        bstage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoshedule = new Intent (AdminStageList.this, AdminSchedule.class);

                gotoshedule.putExtra("Stage", "1");
                gotoshedule.putExtra("day", day);

                startActivity(gotoshedule);
            }
        });



        bstage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoshedule = new Intent (AdminStageList.this, AdminSchedule.class);

                gotoshedule.putExtra("Stage", "2");
                gotoshedule.putExtra("day", day);


                startActivity(gotoshedule);
            }
        });

        bstage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoshedule = new Intent (AdminStageList.this, AdminSchedule.class);

                gotoshedule.putExtra("Stage", "3");
                gotoshedule.putExtra("day", day);

                startActivity(gotoshedule);
            }
        });

        bstage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoshedule = new Intent (AdminStageList.this, AdminSchedule.class);

                gotoshedule.putExtra("Stage", "4");
                gotoshedule.putExtra("day", day);

                startActivity(gotoshedule);
            }
        });

        bstage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoshedule = new Intent (AdminStageList.this, AdminSchedule.class);

                gotoshedule.putExtra("Stage", "5");
                gotoshedule.putExtra("day", day);

                startActivity(gotoshedule);
            }
        });

    }
}
