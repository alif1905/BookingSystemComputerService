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
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private Button mBackCustInvoice;



    private ArrayList<InvoiceMode> dataModels;
    private ListView listView;
    private static Invoice adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_invoice);

        listView=(ListView)findViewById(R.id.listView);
        mBackCustInvoice=(Button)findViewById(R.id.btnBckCustinvoice) ;
        dataModels= new ArrayList<>();


        adapter= new Invoice(dataModels,getApplicationContext());
        listView.setAdapter(adapter);
        mBackCustInvoice.setOnClickListener(this);
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
    }

    private void getInformation() {
        dataModels.clear();


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        myRef = database.getReference().child("Bookings");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {


                    for (DataSnapshot RootSnapshot : uniqueKeySnapshot.getChildren()) {

                        InvoiceService invoice =RootSnapshot.getValue(InvoiceService.class);




                        dataModels.add(new InvoiceMode(
                                invoice.Status,
                                invoice.Address,
                                invoice.Model,
                                invoice.PhoneNo,
                                invoice.PickupTime,
                                invoice.Service,
                                invoice.Brand,
                                invoice.Date,
                                invoice.Reason,
                                true,
                                true));


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


    @IgnoreExtraProperties
    public static class InvoiceService {
        public String Date;
        public String Address;
        public String Model;
        public String PhoneNo;
        public String PickupTime;
        public String Service;
        public String Brand;
        public String Reason;
        public String Status;

        public boolean isAccepted;
        public boolean isUpdated;

        public InvoiceService() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public InvoiceService(String status, String address, String model, String phoneNo, String pickupTime, String service, String brand, String date, String reason, boolean isAccepted, boolean isUpdated) {

            this.Status = status;
            this.Date = date;
            this.Address = address;
            this.Model = model;
            this.PhoneNo = phoneNo;
            this.PickupTime = pickupTime;
            this.Service = service;
            this.Brand = brand;
            this.Reason = reason;
            this.isAccepted = isAccepted;
            this.isUpdated = isUpdated;

        }
    }


}
