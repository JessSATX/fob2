package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminLogin extends AppCompatActivity {

    public EditText email, password;
    public Button blogin, bsignup, bforgotpass, bexport;
    public TextView verify_msg;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        //FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);
        blogin = (Button) findViewById(R.id.bLogin);
        bsignup = (Button) findViewById(R.id.bSignUp);
        bforgotpass = (Button) findViewById(R.id.bForgotPassword);
        verify_msg = (TextView) findViewById(R.id.VerifyText);
        bexport = (Button) findViewById(R.id.bExport);

        bsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignUp = new Intent(AdminLogin.this, SignUp.class);
                startActivity(goToSignUp);
            }
        });

        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e_mail = email.getText().toString().trim();
                String pass_word = password.getText().toString().trim();

                if (!e_mail.equals("") && !pass_word.equals("")) {
                    auth.signInWithEmailAndPassword(e_mail, pass_word).addOnCompleteListener(AdminLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            final FirebaseUser user = auth.getCurrentUser();

                            if (task.isSuccessful() && user.isEmailVerified()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(AdminLogin.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                Intent goToHome = new Intent(AdminLogin.this, AdminHomePage.class);
                                startActivity(goToHome);
                               // finish();
                            } else if (user != null && !user.isEmailVerified()) {
                                verify_msg.setText("Your e-mail is not verified. Please check your inbox for a verification e-mail" +
                                        " Click here to send a new verification e-mail.");
                                verify_msg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        user.sendEmailVerification();
                                        verify_msg.setText("");
                                        Toast.makeText(AdminLogin.this, "A new verification email has been sent.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else // !task.isSuccessful()
                            {
                                Toast.makeText(AdminLogin.this, "Password and email combination did not match.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(AdminLogin.this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bforgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToForgotPass = new Intent(AdminLogin.this, ForgotPassword.class);
                startActivity(goToForgotPass);
            }
        });

        bexport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToExport = new Intent (AdminLogin.this, Export.class);
                startActivity(goToExport);
            }
        });
    }
}
