package com.example.aliff.bookingsystemcomputerservice;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerInvoice extends AppCompatActivity implements View.OnClickListener {

    private String accesslevel;
    private String CustId;
    String userid;
    private String value;


    private ListView lvDate;

    ArrayList<String> values = new ArrayList<String>();
    ArrayList<String> rootValues = new ArrayList<String>();
    ArrayAdapter<String> adapterDate;


    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private Button mBackCustInvoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_invoice);
        Intent intent = getIntent();
//        CustId = intent.getStringExtra("CustID");
//        value = intent.getStringExtra("Value");
//        accesslevel = intent.getStringExtra("ACCESSLEVEL");


        lvDate = (ListView) findViewById(R.id.ListViewDate);
        adapterDate = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_row_layout, R.id.rowTextView, values);

        mBackCustInvoice = (Button) findViewById(R.id.btnBckCustinvoice);

        mBackCustInvoice.setOnClickListener(this);


//
//        lvDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                // ListView Clicked item index
//                int itemPosition = position;
//
//                // ListView Clicked item value
//                String itemValue = (String) lvDate.getItemAtPosition(position);
//
//                // Show Alert
////                Toast.makeText(getApplicationContext(),
////                        "Position :" + itemPosition + "  ListItem : " + rootValues.get(itemPosition).toString(), Toast.LENGTH_LONG)
////                        .show();
//
//
//                Intent i = new Intent(CustomerInvoice.this, CustomerInvoice.class);
//                i.putExtra("CustID", rootValues.get(itemPosition).toString());
//                i.putExtra("Value", itemValue);
//                i.putExtra("ACCESSLEVEL", accesslevel);
//                startActivity(i);
//
//
//            }

        //     });

    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.btnBckCustinvoice:
                Intent i = new Intent(CustomerInvoice.this, Manage_Financial.class);
                startActivity(i);
                finish();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(CustomerInvoice.this, Manage_Financial.class);
        startActivity(i);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(CustomerInvoice.this, Manage_Financial.class);
            startActivity(i);
        }


        getInformation();
        lvDate.setAdapter(adapterDate);
    }

    private void getInformation() {
        values.clear();
        rootValues.clear();
        adapterDate.clear();

        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //      myRef = database.getReference().child("Bookings").child(userid).child(value);

//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//
//
//     //               myRef = database.getReference().child("Bookings").child(userid).child(value);
//
//
//
//                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
//                        myRef=database.getReference().child("Date");
//                        String value = uniqueKeySnapshot.getKey().toString();
//                        for (DataSnapshot RootSnapshot : uniqueKeySnapshot.getChildren()) {
//
//                            rootValues.add(value);
//                            String rootValue = RootSnapshot.getKey().toString();
//                            adapterDate.add(rootValue);
//                        }
//
//
//                    }
//                }
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    // Failed to read value
//                    Log.w("bookingsList", "Failed to read value.", error.toException());
//                }
//
//
//            });
//        }


    }
}





