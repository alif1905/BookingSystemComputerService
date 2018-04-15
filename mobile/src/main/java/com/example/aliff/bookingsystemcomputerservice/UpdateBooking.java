package com.example.aliff.bookingsystemcomputerservice;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class UpdateBooking extends AppCompatActivity implements View.OnClickListener {

    private String userid, value;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private Button submit, cancel;
    private Spinner mSpinnerTime;
    private EditText mEtDate;
    private TextView mEtreason, timeEt;
    private Spinner mReason;
    ArrayAdapter<CharSequence> adapterTime, adapterReason;
    private String timeDb,dateDb,reasonDb;
    private int mYear, mMonth, mDay;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_booking);

        Intent intent = getIntent();

        userid = intent.getStringExtra("userid");
        value = intent.getStringExtra("value");
        submit = (Button)findViewById(R.id.btnUpdateRequest);
        cancel = (Button)findViewById(R.id.cancel);


        mSpinnerTime=(Spinner)findViewById(R.id.etTime) ;
        mEtDate=(EditText)findViewById(R.id.dateEt);
        mReason=(Spinner)findViewById(R.id.etReason) ;

        mEtreason= (TextView) findViewById(R.id.reasonEt);
        timeEt = (TextView)findViewById(R.id.timeEt);

        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        mEtDate.setOnClickListener(this);

        adapterTime = ArrayAdapter.createFromResource(this,
                R.array.list_time, android.R.layout.simple_spinner_item);

        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerTime.setAdapter(adapterTime);

        mSpinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeDb = (String) adapterTime.getItem(position);
                timeEt.setText(timeDb);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adapterReason = ArrayAdapter.createFromResource(this,
                R.array.list_reason, android.R.layout.simple_spinner_item);

        adapterReason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mReason.setAdapter(adapterReason);

        mReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reasonDb = (String) adapterReason.getItem(position);
                mEtreason.setText(reasonDb);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }


    private void datePicker(View view) {

        if (view == mEtDate) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            mEtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnUpdateRequest :
                updateDetail();
                break;
            case R.id.cancel : break;
            case R.id.dateEt:

                datePicker(view);
                break;
        }
    }


    public void updateDetail (){

        dateDb=mEtDate.getText().toString();


        Toast.makeText(getApplicationContext(),"updateRequest Triggered", Toast.LENGTH_LONG).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Bookings").child(userid).child(value);
        myRef.child("Date").setValue(dateDb);
        myRef.child("PickupTime").setValue(timeDb);
        myRef.child("Reason").setValue(reasonDb);
        myRef.child("Statys").setValue("Updated");
        myRef.child("isUpdated").setValue(true);

    }




}