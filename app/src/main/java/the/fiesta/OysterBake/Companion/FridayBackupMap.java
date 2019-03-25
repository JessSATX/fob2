package the.fiesta.OysterBake.Companion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;


public class FridayBackupMap extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private ExpandableListViewAdapterForBackupMap expandableListViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friday_backup_map);
        TouchImageView map = findViewById(R.id.fridayBackup);

        expandableListView = findViewById(R.id.backupListFriday);
        expandableListViewAdapter = new ExpandableListViewAdapterForBackupMap(this);

        expandableListView.setAdapter(expandableListViewAdapter);
    }
}

