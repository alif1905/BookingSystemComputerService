package com.example.aliff.bookingsystemcomputerservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class displayBooking extends AppCompatActivity {
    String userid , value;
    private FirebaseAuth mAuth;

    private TextView tvAdd,tvBrand,tvModel,tvPhone,tvPick,tvService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_booking);
        Intent intent = getIntent();
        value = intent.getStringExtra("Value");
        tvAdd = (TextView)findViewById(R.id.tvAddress);
        tvBrand = (TextView)findViewById(R.id.tvBrand);
        tvModel = (TextView)findViewById(R.id.tvModel);
        tvPhone = (TextView)findViewById(R.id.tvPhoneNo);
        tvPick = (TextView)findViewById(R.id.tvPickUpTime);
        tvService = (TextView)findViewById(R.id.tvServiceType);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.displaybookingsmenu,menu);


        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(displayBooking.this,bookingsList.class);
                startActivity(i);
                break;

            case R.id.cancle :
                Toast.makeText(getApplicationContext(),"SERVICE CANCELED!!", Toast.LENGTH_LONG).show();
                break;


        }
        return true;
    }





    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(displayBooking.this, MainActivity.class);
            startActivity(i);
        }

        userid = currentUser.getUid();
        getBookings();
    }

    public void getBookings() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Bookings").child(userid).child(value);
        Toast.makeText(getApplicationContext(),value,Toast.LENGTH_LONG).show();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Services service = dataSnapshot.getValue(Services.class);

                tvAdd.setText(service.Address);
                tvBrand.setText(service.Brand);
                tvModel.setText(service.Model);
                tvPhone.setText(service.PhoneNo);
                tvPick.setText(service.PickupTime);
                tvService.setText(service.Service);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("bookingsList", "Failed to read value.", error.toException());
            }
        });

    }
    @IgnoreExtraProperties
    public static class Services {

      public String  Address;
        public String Model;
        public String PhoneNo;
        public String PickupTime;
        public String Service;
        public String Brand;


        public Services() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }
        public Services(String address, String model, String phoneNo, String pickupTime, String service, String brand) {
            Address = address;
            Model = model;
            PhoneNo = phoneNo;
            PickupTime = pickupTime;
            Service = service;
            Brand = brand;
        }
        


    }
}
