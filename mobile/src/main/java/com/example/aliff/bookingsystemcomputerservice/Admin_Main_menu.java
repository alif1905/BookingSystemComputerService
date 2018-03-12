package com.example.aliff.bookingsystemcomputerservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class Admin_Main_menu extends AppCompatActivity {

    private Button mLogoutAdmin,mProfileAdmin,mRegisterAdmin;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String accesslevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__main_menu);

        mLogoutAdmin= findViewById(R.id.LogoutAdmin);
        mProfileAdmin= findViewById(R.id.ProfileAdmin);
        mRegisterAdmin = findViewById(R.id.RegisterAdmin);

        mLogoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Admin_Main_menu.this, "Successfull Logout...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Admin_Main_menu.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mProfileAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Admin_Main_menu.this, "Profile...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Admin_Main_menu.this, Profile_Admin.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        mRegisterAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Admin_Main_menu.this, "Register New User...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Admin_Main_menu.this, Admin_register.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String id = currentUser.getUid();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Register").child(id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Auth auth = dataSnapshot.getValue(Auth.class);
                accesslevel = auth.accessLevel;
                if (!accesslevel.equals("Admin")) {
                    Toast.makeText(getApplicationContext(), "Login failed, please Login as admin...", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Admin_Main_menu.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Welcome "+auth.email, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @IgnoreExtraProperties
    public static class Auth {

        public String email;
        public String accessLevel;

        public Auth() {
        }

        public Auth(String email, String accessLevel) {
            this.email = email;
            this.accessLevel = accessLevel;
        }

    }

}
