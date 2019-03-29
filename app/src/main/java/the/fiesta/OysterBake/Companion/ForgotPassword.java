package the.fiesta.OysterBake.Companion;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText email;
    Button sendem;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.etEmail);
        sendem = findViewById(R.id.bSendEmail);

        sendem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e_mail = email.getText().toString().trim();

                try {
                    auth.sendPasswordResetEmail(e_mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(ForgotPassword.this, "An email has been sent to recover your password.", Toast.LENGTH_SHORT).show();
                                //Intent home = new Intent(ForgotPassword.this, AdminLogin.class);
                                //startActivity(home);
                                finish();
                            } else {
                                Toast.makeText(ForgotPassword.this, "An error occured. Please verify your email address.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(ForgotPassword.this, "Please enter an e-mail.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}