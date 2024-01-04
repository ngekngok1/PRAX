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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button register,login;
    EditText name1,age1,password1;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        name1 = findViewById(R.id.name1);
        age1 = findViewById(R.id.age1);
        password1 = findViewById(R.id.password1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                // Start the NextActivity
                startActivity(intent);
            }


        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name1.getText().toString().trim();
                String password = password1.getText().toString().trim();
                String age = age1.getText().toString().trim();

                mAuth.createUserWithEmailAndPassword(name, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String userId = user.getUid();

                                        DatabaseReference usersRef = database.getReference(userId);
                                        usersRef.child("Email").setValue(name);
                                        usersRef.child("Password").setValue(password);
                                        usersRef.child("Age").setValue(age);

                                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> verificationTask) {
                                                if (verificationTask.isSuccessful()) {
                                                    Toast.makeText(MainActivity.this, "Email has been sent to your email address", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        mAuth.signOut();
                                        startActivity(new Intent(MainActivity.this, Login.class));
                                        finish();
                                    }
                                } else {
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        name1.setError("Email is already Registered");
                                    } else {
                                        Toast.makeText(MainActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
    }
}