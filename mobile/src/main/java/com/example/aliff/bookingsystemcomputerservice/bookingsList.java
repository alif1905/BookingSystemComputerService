package com.example.aliff.bookingsystemcomputerservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class bookingsList extends AppCompatActivity {

    String userid;
    String[] stockArr;
    ListView listView;
    private FirebaseAuth mAuth;
    ArrayList<String> values = new ArrayList<String>();
    ArrayList<String> rootValues = new ArrayList<String>();

    ArrayAdapter<String> adapter;
    private String accesslevel;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings_list);
        Intent intent = getIntent();
        accesslevel = intent.getStringExtra("ACCESSLEVEL");

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_row_layout, R.id.rowTextView, values);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
//                Toast.makeText(getApplicationContext(),
//                        "Position :" + itemPosition + "  ListItem : " + rootValues.get(itemPosition).toString(), Toast.LENGTH_LONG)
//                        .show();


                    Intent i = new Intent(bookingsList.this, displayBooking.class);
                    i.putExtra("CustID", rootValues.get(itemPosition));
                    i.putExtra("Value", itemValue);
                    i.putExtra("ACCESSLEVEL", accesslevel);
                    startActivity(i);


            }

        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (accesslevel.equals("USER")) {
            Intent i = new Intent(bookingsList.this, Booking_Service.class);
            startActivity(i);
            finish();
        } else if (accesslevel.equals("ADMIN")) {
            Intent i = new Intent(bookingsList.this, Admin_Main_menu.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(bookingsList.this, Booking_Service.class);
            startActivity(i);
        }

        userid = currentUser.getUid();
        getBookings();
        listView.setAdapter(adapter);
    }


    public void getBookings() {
        values.clear();
        rootValues.clear();
        adapter.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        if (accesslevel.equals("ADMIN")) {
            myRef = database.getReference().child("Bookings");


        } else if (accesslevel.equals("USER")) {
            myRef = database.getReference().child("Bookings").child(userid);
        }


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                    String value = uniqueKeySnapshot.getKey().toString();

                    if (accesslevel.equals("USER")) {
                        rootValues.add(value);
                        adapter.add(value);


                    } else if (accesslevel.equals("ADMIN")) {
                        for (DataSnapshot RootSnapshot : uniqueKeySnapshot.getChildren()) {
                            rootValues.add(value);
                            String rootValue = RootSnapshot.getKey().toString();
                            adapter.add(rootValue);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("bookingsList", "Failed to read value.", error.toException());
            }
        });

    }

}
