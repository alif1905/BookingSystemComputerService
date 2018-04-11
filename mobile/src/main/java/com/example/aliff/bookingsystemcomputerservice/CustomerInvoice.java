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
    ArrayList<String> values = new ArrayList<String>();
    private String accesslevel;
    private String CustId;
    private String  value;
    ArrayList<String> rootValues = new ArrayList<String>();
    String userid;
    ArrayAdapter<String> adapterDate;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
   private Button mBackCustInvoice;

   private ListView lvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_invoice);
        Intent intent = getIntent();
        CustId = intent.getStringExtra("CustID");
        value = intent.getStringExtra("Value");
        accesslevel = intent.getStringExtra("ACCESSLEVEL");



        accesslevel = intent.getStringExtra("ACCESSLEVEL");


        lvDate=(ListView)findViewById(R.id.ListViewDate);

        mBackCustInvoice=(Button)findViewById(R.id.btnBckCustinvoice);



        mBackCustInvoice.setOnClickListener(this);


        adapterDate = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_row_layout, R.id.rowTextView, values);


        lvDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) lvDate.getItemAtPosition(position);

                // Show Alert
//                Toast.makeText(getApplicationContext(),
//                        "Position :" + itemPosition + "  ListItem : " + rootValues.get(itemPosition).toString(), Toast.LENGTH_LONG)
//                        .show();


                Intent i = new Intent(CustomerInvoice.this, CustomerInvoice.class);
                i.putExtra("CustID", rootValues.get(itemPosition).toString());
                i.putExtra("Value", itemValue);
                i.putExtra("ACCESSLEVEL", accesslevel);
                startActivity(i);


            }

        });

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

        userid = currentUser.getUid();
        getInformation();
        lvDate.setAdapter(adapterDate);
    }

    private void getInformation() {
        values.clear();
        rootValues.clear();
        adapterDate.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Bookings").child(userid).child(value).child("Date");;

            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    displayBooking.Services service = dataSnapshot.getValue(displayBooking.Services.class);
                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                        String value = uniqueKeySnapshot.getKey().toString();
                        for (DataSnapshot RootSnapshot : uniqueKeySnapshot.getChildren()) {
                            String whoReject = RootSnapshot.child("Date").child(userid).getKey();
                            rootValues.add(value);
                            String rootValue = RootSnapshot.getKey().toString();
                            adapterDate.add(rootValue);
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





