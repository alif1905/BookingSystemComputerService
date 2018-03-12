package com.example.aliff.bookingsystemcomputerservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class Customer_Main_menu extends AppCompatActivity {
    private Button mLogoutCustomer, mChatRoomCust, mProfileCust,mBookCust;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String accesslevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__main_menu);


        mLogoutCustomer = findViewById(R.id.LogoutCustomer);
        mChatRoomCust = findViewById(R.id.chatroomCustomer);
        mProfileCust = findViewById(R.id.ProfileCustomer);
        mBookCust = findViewById(R.id.btnBookCust);



        mLogoutCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Customer_Main_menu.this, "Successfull Logout...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Customer_Main_menu.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mChatRoomCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Customer_Main_menu.this, "Chatroom...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Customer_Main_menu.this, Chat_Room1.class);
                startActivity(intent);
                finish();
                return;

            }
        });

        mProfileCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Customer_Main_menu.this, "Profile...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Customer_Main_menu.this, Profile_Customer.class);
                startActivity(intent);
                finish();
                return;

            }
        });



        mBookCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Customer_Main_menu.this, "Booking New Service", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Customer_Main_menu.this, Booking_Service.class);
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
                if (!accesslevel.equals("Customer")) {
                    Toast.makeText(getApplicationContext(), "Login failed, please Login as customer...", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Customer_Main_menu.this, MainActivity.class);
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
