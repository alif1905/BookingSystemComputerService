package com.example.aliff.bookingsystemcomputerservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManageFinancial extends AppCompatActivity implements View.OnClickListener {
    private Button mBackbtn;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_financial);

        mBackbtn = findViewById(R.id.BackBtnFinancial);


        mBackbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Intent intent;

        switch (view.getId()) {
            case R.id.BackBtnFinancial:
                intent = new Intent(ManageFinancial.this, Admin_Main_menu.class);

                startActivity(intent);
                finish();

                break;
        }
    }
}
