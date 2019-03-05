package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.Touch;
import android.view.ScaleGestureDetector;
import android.widget.ExpandableListView;
import android.widget.ImageView;

public class FridayBackupMap extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private ExpandableListViewAdapterForBackupMap expandableListViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friday_backup_map);
        TouchImageView map = (TouchImageView) findViewById(R.id.fridayBackup);

        expandableListView = (ExpandableListView) findViewById(R.id.backupListFriday);
        expandableListViewAdapter = new ExpandableListViewAdapterForBackupMap(this);

        expandableListView.setAdapter(expandableListViewAdapter);

    }
}

