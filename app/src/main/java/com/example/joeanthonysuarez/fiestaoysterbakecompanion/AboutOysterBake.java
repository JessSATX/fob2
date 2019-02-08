package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

public class AboutOysterBake extends AppCompatActivity {

    TextView fobTitle1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_oyster_bake);

        int currentFOBCount = (Calendar.getInstance().get(Calendar.YEAR)) - 1916;

        fobTitle1 = (TextView) findViewById(R.id.fob_title_1);

        fobTitle1.setText(Integer.toString(currentFOBCount) + " Years and Counting");
    }
}
