package com.example.aliff.bookingsystemcomputerservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomerInvoice extends AppCompatActivity implements View.OnClickListener {

   private Button mBackCustInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_invoice);


        mBackCustInvoice=(Button)findViewById(R.id.btnBckCustinvoice);



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


}
