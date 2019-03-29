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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    // array list used to store all verified emails on database
    final ArrayList<String> emailList = new ArrayList<String>();
    public EditText Email, Password, PassConfirm;
    public Button signUp;
    private FirebaseAuth auth;
    private DatabaseReference rbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        rbRef = FirebaseDatabase.getInstance().getReference();

        Email = findViewById(R.id.nEmail);
        Password = findViewById(R.id.nPassword);
        PassConfirm = findViewById(R.id.cPassword);
        signUp = findViewById(R.id.btnsignUp);

        rbRef.child("UserEmails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    emailList.add(ds.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignUp.this, "An error occurred.", Toast.LENGTH_SHORT).show();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e_mail = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String cpass = PassConfirm.getText().toString().trim();
                boolean ver = VerifyEmail(e_mail);

                if (!e_mail.equals("") && !password.equals("") && !cpass.equals("") && ver == true && password.length() > 6) {

                    if (!cpass.equals(password)) {
                        Toast.makeText(SignUp.this, "Passwords do not match. Please verify.", Toast.LENGTH_SHORT).show();
                    } else {
                        auth.createUserWithEmailAndPassword(e_mail, password)
                                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        FirebaseUser user = auth.getCurrentUser();
                                        if (task.isSuccessful()) {
                                            // Send user a verification e-mail request
                                            user.sendEmailVerification();
                                            Toast.makeText(SignUp.this, "Please verify your e-mail.", Toast.LENGTH_SHORT).show();
                                            //Intent home = new Intent(SignUp.this, AdminLogin.class);
                                            //SignUp.this.startActivity(home);
                                            finish();
                                        } else {
                                            try {
                                                throw task.getException();
                                            }
                                            // check if the e-mail entered is an active one
                                            catch (FirebaseAuthUserCollisionException existEmail) {
                                                Toast.makeText(SignUp.this, "The e-mail you entered is already an existing one.", Toast.LENGTH_SHORT).show();
                                            } catch (Exception e) {
                                                Toast.makeText(SignUp.this, "Could not register. Please try again.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                    }
                } else {

                    if (ver == false) {
                        Toast.makeText(SignUp.this, "The E-mail you have entered does not match any in our records.", Toast.LENGTH_SHORT).show();
                    } else if (password.length() <= 6) {
                        Toast.makeText(SignUp.this, "Please select a password that is over 6 characters long.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUp.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public boolean VerifyEmail(String email) {
        final String em = email;

        return emailList.contains(em);
    }
}