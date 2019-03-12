package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DateSelect extends AppCompatActivity {
    TextView title;
    Button bfri, bsat;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_select);

        Intent intent = getIntent();

        type = intent.getStringExtra("Type");

        title = (TextView) findViewById(R.id.tTitle);
        bfri = (Button) findViewById(R.id.bFri);
        bsat = (Button) findViewById(R.id.bSat);

        title.setText("Please specify which day you would like to change for " + type + ".");

        if (type.equals("showtimes")) {

            bfri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent gotoStages = new Intent(DateSelect.this, AdminStageList.class);

                    gotoStages.putExtra("day", "1");

                    startActivity(gotoStages);
                }
            });

            bsat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent gotoStages = new Intent(DateSelect.this, AdminStageList.class);

                    gotoStages.putExtra("day", "2");

                    startActivity(gotoStages);
                }
            });
        } else {

        }
    }
}