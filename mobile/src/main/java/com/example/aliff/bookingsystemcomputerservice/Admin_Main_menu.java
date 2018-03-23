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

public class Admin_Main_menu extends AppCompatActivity implements View.OnClickListener {

    private Button mRecordInventory, mLogoutAdmin, mProfileAdmin, mRegisterAdmin;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String accesslevel;
    private Button mBookings;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__main_menu);

        mLogoutAdmin = findViewById(R.id.LogoutAdmin);
        mProfileAdmin = findViewById(R.id.ProfileAdmin);
        mRegisterAdmin = findViewById(R.id.RegisterAdmin);
        mBookings = (Button) findViewById(R.id.btnBookings);
        mRecordInventory = (Button) findViewById(R.id.btnRecordInventory);

        mBookings.setOnClickListener(this);
        mRegisterAdmin.setOnClickListener(this);
        mProfileAdmin.setOnClickListener(this);
        mLogoutAdmin.setOnClickListener(this);
        mRecordInventory.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        Intent intent;

        switch (view.getId()) {

            case R.id.btnRecordInventory:
                intent = new Intent(Admin_Main_menu.this, inventory_list.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnBookings:
                intent = new Intent(Admin_Main_menu.this, bookingsList.class);
                intent.putExtra("ACCESSLEVEL", "ADMIN");
                startActivity(intent);
                finish();

                break;
            case R.id.ProfileAdmin:
                intent = new Intent(Admin_Main_menu.this, Profile_Admin.class);
                startActivity(intent);
                finish();


                break;
            case R.id.RegisterAdmin:
                intent = new Intent(Admin_Main_menu.this, Admin_register.class);
                startActivity(intent);
                finish();
                break;
            case R.id.LogoutAdmin:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(Admin_Main_menu.this, MainActivity.class);
                startActivity(intent);
                finish();

                break;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String id = currentUser.getUid();

        Toast.makeText(getApplicationContext(), "Welcome " + currentUser.getEmail(), Toast.LENGTH_LONG).show();


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
                } else {
                    Toast.makeText(getApplicationContext(), "Welcome " + auth.email, Toast.LENGTH_LONG).show();
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
