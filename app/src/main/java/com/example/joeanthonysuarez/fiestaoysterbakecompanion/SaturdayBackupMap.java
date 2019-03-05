package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class SaturdayBackupMap extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ExpandableListViewAdapterForBackupMap expandableListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saturday_backup_map);
        TouchImageView map = (TouchImageView) findViewById(R.id.saturdayBackup);

        expandableListView = (ExpandableListView) findViewById(R.id.backupListSaturday);
        expandableListViewAdapter = new ExpandableListViewAdapterForBackupMap(this);

        expandableListView.setAdapter(expandableListViewAdapter);

    }
}
