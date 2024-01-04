package com.example.prac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Update extends AppCompatActivity {
    Button update, update1;
    TextView age,email;
    EditText newage;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        update = findViewById(R.id.password);
        update1 = findViewById(R.id.update);
        newage = findViewById(R.id.newage);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        String userId = user.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(userId);

        databaseReference.child("Email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and whenever the data at this location is updated
                String powerValue = dataSnapshot.getValue(String.class);
                email.setText(powerValue);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("Age").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and whenever the data at this location is updated
                String powerValue = dataSnapshot.getValue(String.class);
                age.setText(powerValue);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Inside update1.setOnClickListener:
        update1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newAge = newage.getText().toString().trim();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(userId);
                userRef.child("Age").setValue(newAge)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Update.this, "Age Updated!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Home.class));
                                    finish();
                                } else {
                                    Toast.makeText(Update.this, "Failed to update age", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

// Inside update.setOnClickListener:
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPassword(user.getEmail());
            }
        });





        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("Email").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and whenever the data at this location is updated
                        String powerValue = dataSnapshot.getValue(String.class);
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(powerValue);
                        ResetPassword(powerValue);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }
    public void ResetPassword(String powerValue){
        mAuth.sendPasswordResetEmail(powerValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Update.this,"Reset Password link has been sent to your registered email", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Update.this, Home.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Update.this,"ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}