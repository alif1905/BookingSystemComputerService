package com.example.aliff.bookingsystemcomputerservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Manage_Financial extends AppCompatActivity implements View.OnClickListener {

    private Button mBackbtn,mCustomerInvoice;
//    private Button mBtnInventoriInvoice;
    private Intent intent;


    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private String userid;
    private String accesslevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage__financial);

    //    accesslevel = intent.getStringExtra("ACCESSLEVEL");


        mBackbtn = (Button)findViewById(R.id.btnBackFinancial);
        mCustomerInvoice=(Button)findViewById(R.id.btnCustomerInvoice);
//        mBtnInventoriInvoice=(Button) findViewById(R.id.btnInventoryInvoice);


//        mBtnInventoriInvoice.setOnClickListener(this);
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

             //  intent.putExtra("ACCESSLEVEL", accesslevel);
                startActivity(intent);
                finish();

                break;


//
//                case R.id.btnInventoryInvoice:
//                    intent = new Intent(Manage_Financial.this, FinancialInventoryInvoice.class);
//
//                    startActivity(intent);
//                    finish();
//
//                    break;
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Manage_Financial.this, Admin_Main_menu.class);
        startActivity(i);
        finish();
    }



    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(Manage_Financial.this, MainActivity
                    .class);
            startActivity(i);
        }

//        userid = currentUser.getUid();


    }


}
