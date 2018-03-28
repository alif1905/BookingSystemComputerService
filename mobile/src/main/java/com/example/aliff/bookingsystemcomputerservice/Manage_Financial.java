package com.example.aliff.bookingsystemcomputerservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Manage_Financial extends AppCompatActivity implements View.OnClickListener {

    private Button mBackbtn,mCustomerInvoice,mBtnInventoriInvoice;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage__financial);

        mBackbtn = (Button)findViewById(R.id.btnBackFinancial);
        mCustomerInvoice=(Button)findViewById(R.id.btnCustomerInvoice);
        mBtnInventoriInvoice=(Button) findViewById(R.id.btnInventoryInvoice);


        mBtnInventoriInvoice.setOnClickListener(this);
        mBackbtn.setOnClickListener(this);
        mCustomerInvoice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent;

        switch (view.getId()) {
            case R.id.btnBackFinancial:
                intent = new Intent(Manage_Financial.this, Admin_Main_menu.class);

                startActivity(intent);
                finish();

                break;

            case R.id.btnCustomerInvoice:
                intent = new Intent(Manage_Financial.this, CustomerInvoice.class);

                startActivity(intent);
                finish();

                break;



                case R.id.btnInventoryInvoice:
                    intent = new Intent(Manage_Financial.this, FinancialInventoryInvoice.class);

                    startActivity(intent);
                    finish();

                    break;
        }
    }
}
