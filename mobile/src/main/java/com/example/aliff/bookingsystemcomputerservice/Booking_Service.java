package com.example.aliff.bookingsystemcomputerservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Booking_Service extends AppCompatActivity {

    private Button mBtnBackBookCust,mAddBookCust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking__service);

        mBtnBackBookCust= findViewById(R.id.BtnBackBookingService);
        mAddBookCust= findViewById(R.id.AddBookCustomer);


        mBtnBackBookCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Booking_Service.this,Customer_Main_menu.class);
                startActivity(intent);
                finish();
                return;



            }
        });

        mAddBookCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Booking_Service.this,New_Booking.class);
                startActivity(intent);
                finish();
                return;



            }
        });

    }
}
