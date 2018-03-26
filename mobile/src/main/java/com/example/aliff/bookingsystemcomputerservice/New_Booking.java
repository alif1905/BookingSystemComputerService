package com.example.aliff.bookingsystemcomputerservice;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Map;

public class New_Booking extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabaseRef;
    private Button mBackBtn,mBtnSubmit;
    private TextView mDisplayTimeDate, mModel, mAddress, mphoneCust;
    private EditText mEditModel, mEditAddress, mEditPhone;
    private Spinner mspinnerService, mspinnerBrand, mSpinnerTime;
    private String accesslevel;

    private FirebaseAuth mAuth;
    ArrayAdapter<CharSequence> adapterService, adapterbrand, adapterTime;

    private String userid, dateDb, serviceDb, brandDb, modelDb, pickupTimeDb, addressDb, phoneNoDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__booking);

        Intent intent = getIntent();
        accesslevel = intent.getStringExtra("ACCESSLEVEL");


        mBackBtn = findViewById(R.id.BackBtn);
        mBtnSubmit = findViewById(R.id.BtnBookServiceCust);
        mspinnerService = findViewById(R.id.spinnerService_Require);
        mspinnerBrand = findViewById(R.id.spinnerBrand);
        mSpinnerTime = findViewById(R.id.spinnerSelecttimePicker);

        mDisplayTimeDate = findViewById(R.id.DisplayTimeAndDate);
        mModel = findViewById(R.id.model);
        mAddress = findViewById(R.id.addressCust);
        mphoneCust = findViewById(R.id.PhoneCust);

        mEditModel = findViewById(R.id.editTextModel);
        mEditAddress = findViewById(R.id.editTextAddressCust);
        mEditPhone = findViewById(R.id.editTextphoneCust);

        updateTimeDate();


        adapterService = ArrayAdapter.createFromResource(this,
                R.array.list_service, android.R.layout.simple_spinner_item);

        adapterService.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mspinnerService.setAdapter(adapterService);

        mspinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serviceDb = (String) adapterService.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adapterbrand = ArrayAdapter.createFromResource(this,
                R.array.list_brand, android.R.layout.simple_spinner_item);

        adapterbrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mspinnerBrand.setAdapter(adapterbrand);

        mspinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brandDb = (String) adapterbrand.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterTime = ArrayAdapter.createFromResource(this,
                R.array.list_time, android.R.layout.simple_spinner_item);

        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerTime.setAdapter(adapterTime);

        mSpinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pickupTimeDb = (String) adapterTime.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        mBackBtn.setOnClickListener(this);

        mBtnSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.BackBtn:
                Intent intent = new Intent(New_Booking.this, Booking_Service.class);
                startActivity(intent);
                finish();
                break;

            case R.id.BtnBookServiceCust:
                submitForm();


                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(New_Booking.this, MainActivity.class);
            startActivity(i);
        }
        userid = currentUser.getUid();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(New_Booking.this, Booking_Service.class);
        i.putExtra("ACCESSLEVEL", accesslevel);
        startActivity(i);
        finish();
    }


    public void submitForm() {

        modelDb = mEditModel.getText().toString();

        addressDb = mEditAddress.getText().toString();
        phoneNoDb = mEditPhone.getText().toString();
        dateDb = mDisplayTimeDate.getText().toString();

        if (TextUtils.isEmpty(modelDb)) {
            Toast.makeText(New_Booking.this, "Model PC/Laptop is required", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(addressDb)) {
            Toast.makeText(New_Booking.this, "Address is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phoneNoDb)) {
            Toast.makeText(New_Booking.this, "Phone Number is required", Toast.LENGTH_SHORT).show();
            return;
        }


        if ((!TextUtils.isEmpty(modelDb) && !TextUtils.isEmpty(addressDb) && !TextUtils.isEmpty(phoneNoDb))) {
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Bookings");
            DatabaseReference current_user_db = mDatabaseRef.child(userid).child(brandDb + " " + modelDb);
            current_user_db.child("Date").setValue(dateDb);
            current_user_db.child("Service").setValue(serviceDb);
            current_user_db.child("Brand").setValue(brandDb);
            current_user_db.child("Model").setValue(modelDb);
            current_user_db.child("PickupTime").setValue(pickupTimeDb);
            current_user_db.child("Address").setValue(addressDb);
            current_user_db.child("PhoneNo").setValue(phoneNoDb);
            Intent i = new Intent(New_Booking.this, Booking_Service.class);
            startActivity(i);

            Toast.makeText(New_Booking.this,"Successfull Book a Service...", Toast.LENGTH_SHORT).show();
            finish();

        }

    }



    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    Calendar dateTime = Calendar.getInstance();
    int day, month, year;
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, month);
            dateTime.set(Calendar.DAY_OF_MONTH, day);
            updateTimeDate();


        }
    };

    private void updateDate() {
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void updateTimeDate() {
        mDisplayTimeDate.setText(formatDateTime.format(dateTime.getTime()));
    }

}