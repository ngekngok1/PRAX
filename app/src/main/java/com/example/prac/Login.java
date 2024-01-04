package com.example.prac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    Button register,login;
    EditText name1,password1;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        name1 = findViewById(R.id.name1);

        password1 = findViewById(R.id.password1);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                // Start the NextActivity
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name1.getText().toString().trim();
                String password = password1.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(name, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                // Email is verified, proceed with login
                                Toast.makeText(Login.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Home.class));
                                finish();
                            } else {
                                // Email is not verified, prevent login
                                Toast.makeText(Login.this, "Email not yet verified. Please verify your email.", Toast.LENGTH_SHORT).show();
                                // Consider logging the user out or prompting them to verify their email before allowing login
                                FirebaseAuth.getInstance().signOut();
                            }
                        } else {
                            Toast.makeText(Login.this, "WRONG EMAIL OR PASSWORD!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



    }
}