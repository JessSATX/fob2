package the.fiesta.OysterBake.Companion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AdminHomePage extends AppCompatActivity {

    Button bmap, bshowtime, logout;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        //FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        bmap = findViewById(R.id.bMaps);
        bshowtime = findViewById(R.id.bShowtimes);
        logout = findViewById(R.id.bLogout);
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    auth.signOut();
                    Intent gotoMain = new Intent(AdminHomePage.this, MainActivity.class);
                    startActivity(gotoMain);
                    Toast.makeText(AdminHomePage.this, "Succesfully Logged Out!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AdminHomePage.this, "Could not log out. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}