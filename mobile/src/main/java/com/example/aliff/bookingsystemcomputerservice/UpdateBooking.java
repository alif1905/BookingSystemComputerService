package com.example.aliff.bookingsystemcomputerservice;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class UpdateBooking extends AppCompatActivity implements View.OnClickListener {

    private String accesslevel;
    private String CustId;


    private String userid, value;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private Button submit;
    private Spinner mSpinnerTime;
    private EditText mEtDate;
    private TextView mEtreason, timeEt;
    private Spinner mReason;
    ArrayAdapter<CharSequence> adapterTime, adapterReason;
    private String timeDb, dateDb, reasonDb;
    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_booking);

        Intent intent = getIntent();


        CustId = intent.getStringExtra("CustID");

        accesslevel = intent.getStringExtra("ACCESSLEVEL");


        userid = intent.getStringExtra("userid");
        value = intent.getStringExtra("value");
        submit = (Button) findViewById(R.id.btnUpdateRequest);


        mSpinnerTime = (Spinner) findViewById(R.id.etTime);
        mEtDate = (EditText) findViewById(R.id.dateEt);
        mReason = (Spinner) findViewById(R.id.etReason);

        mEtreason = (TextView) findViewById(R.id.reasonEt);
        timeEt = (TextView) findViewById(R.id.timeEt);

        submit.setOnClickListener(this);

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
        switch (view.getId()) {
            case R.id.btnUpdateRequest:
                updateDetail();
                Intent i = new Intent(UpdateBooking.this, displayBooking.class);
                i.putExtra("ACCESSLEVEL", accesslevel);
                i.putExtra("CustID", CustId);
                i.putExtra("value", value);
                break;

            case R.id.dateEt:

                datePicker(view);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(UpdateBooking.this, MainActivity.class);
            startActivity(i);
        }



    }

    public void updateDetail() {

        dateDb = mEtDate.getText().toString();

        if (timeEt.getText().equals("--")) {
            Toast.makeText(UpdateBooking.this, "Select Time", Toast.LENGTH_SHORT).show();
            return;

        }

        if (TextUtils.isEmpty(dateDb)) {
            Toast.makeText(UpdateBooking.this, "Select Date", Toast.LENGTH_SHORT).show();
            return;

        }


        if (mEtreason.getText().equals("--")) {
            Toast.makeText(UpdateBooking.this, "Select Reason", Toast.LENGTH_SHORT).show();
            return;

        }


        if ((!TextUtils.isEmpty(dateDb) && !mEtreason.getText().equals("--") && !timeEt.getText().equals("--"))) {


            FirebaseDatabase database = FirebaseDatabase.getInstance();


            if (accesslevel.equals("ADMIN")) {
                myRef = database.getReference().child("Bookings").child(userid).child(value);


                Toast.makeText(getApplicationContext(), "updateRequest Triggered", Toast.LENGTH_LONG).show();

                myRef = database.getReference().child("Bookings").child(userid).child(value);
                myRef.child("Date").setValue(dateDb);
                myRef.child("PickupTime").setValue(timeDb);
                myRef.child("Reason").setValue(reasonDb);

                myRef.child("Status").setValue("Request Updated");
                myRef.child("isUpdated").setValue(true);

                myRef = database.getReference().child("CustNoti").child(userid).child(value);

                myRef.child("Status").setValue("Request Updated");

                myRef = database.getReference().child("AdminNoti").child(userid).child(value);

                myRef.child("Status").setValue("Request Updated");
                myRef.child("PickupTime").setValue(timeDb);
                myRef.child("Reason").setValue(reasonDb);
                myRef.child("Date").setValue(dateDb);
            }


//
//         {
//            timeEt.setError("Required");
//        }


        }
    }
}