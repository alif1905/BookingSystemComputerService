package com.example.aliff.bookingsystemcomputerservice;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class displayBooking extends AppCompatActivity {
    private String userid, value;
    private FirebaseAuth mAuth;

    private TextView tvAdd, tvBrand, tvModel, tvPhone, tvPick, tvService,tvDate;
    private String accesslevel;
    private String CustId;
    private DatabaseReference myRef;
    private ProgressDialog progressDialog;
    private Button mBtnReject;
    private Button mBtnAccept;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_booking);
        Intent intent = getIntent();

        CustId = intent.getStringExtra("CustID");
        value = intent.getStringExtra("Value");
        accesslevel = intent.getStringExtra("ACCESSLEVEL");


        mBtnAccept = (Button) findViewById(R.id.btnAccept);
        mBtnReject = (Button) findViewById(R.id.btnReject);
        tvDate=(TextView)findViewById(R.id.tvDate);
        tvAdd = (TextView) findViewById(R.id.tvAddress);
        tvBrand = (TextView) findViewById(R.id.tvBrand);
        tvModel = (TextView) findViewById(R.id.tvModel);
        tvPhone = (TextView) findViewById(R.id.tvPhoneNo);
        tvPick = (TextView) findViewById(R.id.tvPickUpTime);
        tvService = (TextView) findViewById(R.id.tvServiceType);
        progressDialog = ProgressDialog.show(displayBooking.this, "Please wait...", "Processing...", true);
//

        if (accesslevel.equals("USER")) {
            mBtnAccept.setVisibility(View.INVISIBLE);
            mBtnReject.setVisibility(View.INVISIBLE);
        } else {
            mBtnAccept.setVisibility(View.VISIBLE);
            mBtnReject.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (accesslevel.equals("USER")) {
            super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.displaybookingsmenu, menu);
        } else {
        }
        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(displayBooking.this, bookingsList.class);
        i.putExtra("ACCESSLEVEL", accesslevel);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(displayBooking.this, bookingsList.class);
                startActivity(i);
                finish();
                break;

            case R.id.cancle:

                delete();
                break;


        }
        return true;
    }


    public void delete() {
        AlertDialog alertDialog = new AlertDialog.Builder(displayBooking.this).create();
        alertDialog.setTitle("Cancel Booking");
        alertDialog.setMessage("Are you sure you want to cancel this booking?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                deleteResponse();


                            }
                        });

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.dismiss();
                    }
                });
        alertDialog.show();


    }

    public void deleteResponse() {

        AlertDialog alertDialog = new AlertDialog.Builder(displayBooking.this).create();
        alertDialog.setMessage("Booking Canceled");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent i = new Intent(displayBooking.this, bookingsList.class);
                        i.putExtra("ACCESSLEVEL", accesslevel);
                        startActivity(i);
                        finish();
                    }
                });
        alertDialog.show();


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


        if (accesslevel.equals("USER")) {
            myRef = database.getReference().child("Bookings").child(userid).child(value);
        } else {
            myRef = database.getReference().child("Bookings").child(CustId).child(value);
        }

        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Services service = dataSnapshot.getValue(Services.class);
                tvDate.setText(service.Date);
                tvAdd.setText(service.Address);
                tvBrand.setText(service.Brand);
                tvModel.setText(service.Model);
                tvPhone.setText(service.PhoneNo);
                tvPick.setText(service.PickupTime);
                tvService.setText(service.Service);
                //   Toast.makeText(getApplicationContext(), service.Status.get(0),Toast.LENGTH_LONG).show();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);
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
        public  String Date;
        public String Address;
        public String Model;
        public String PhoneNo;
        public String PickupTime;
        public String Service;
        public String Brand;
        public Services() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Services(String address, String model, String phoneNo, String pickupTime, String service, String brand,String date) {
            Date=date;
            Address = address;
            Model = model;
            PhoneNo = phoneNo;
            PickupTime = pickupTime;
            Service = service;
            Brand = brand;
        }
    }




}
