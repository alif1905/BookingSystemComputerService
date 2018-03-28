package com.example.aliff.bookingsystemcomputerservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FinancialInventoryInvoice extends AppCompatActivity implements View.OnClickListener {
    private Button mBackInvntoryFinancial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_inventory_invoice);


        mBackInvntoryFinancial=(Button)findViewById(R.id.btnBckInventoryinvoice);


        mBackInvntoryFinancial.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.btnBckInventoryinvoice:

                Intent i = new Intent(FinancialInventoryInvoice.this, Manage_Financial.class);
                startActivity(i);
                finish();
                break;
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(FinancialInventoryInvoice.this, Manage_Financial.class);
        startActivity(i);
        finish();
    }
}
