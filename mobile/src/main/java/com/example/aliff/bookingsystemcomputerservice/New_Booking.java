package com.example.aliff.bookingsystemcomputerservice;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Map;

public class New_Booking extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabaseRef;
    private Button mBackBtn,mBtnSubmit;
    private TextView  mModel, mAddress, mphoneCust,mdateCust;
    private EditText mEditModel, mEditAddress, mEditPhone,mEditDatePicker;
    private Spinner mspinnerService, mspinnerBrand, mSpinnerTime;
    private String accesslevel;
    private int mYear, mMonth, mDay;
    private DatabaseReference myRef;




    private FirebaseAuth mAuth;
    ArrayAdapter<CharSequence> adapterService, adapterbrand, adapterTime;

    private String userid,serviceDb, brandDb, modelDb, pickupTimeDb, addressDb, phoneNoDb,dateDb;

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


        mModel = findViewById(R.id.model);
        mAddress = findViewById(R.id.addressCust);
        mphoneCust = findViewById(R.id.PhoneCust);
        mdateCust=findViewById(R.id.DatePicker);


        mEditModel = findViewById(R.id.editTextModel);
        mEditAddress = findViewById(R.id.editTextAddressCust);
        mEditPhone = findViewById(R.id.editTextphoneCust);
        mEditDatePicker = findViewById(R.id.editTextDate);



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

        mEditDatePicker.setOnClickListener(this);
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

            case R.id.editTextDate:

                datePicker(view);
                break;
        }
    }




    private void datePicker(View view) {

        if (view == mEditDatePicker) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            mEditDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
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

        assert currentUser != null;
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
        dateDb = mEditDatePicker.getText().toString();











        if (TextUtils.isEmpty(dateDb)) {
            Toast.makeText(New_Booking.this, "Select Date", Toast.LENGTH_SHORT).show();
            return;

        }


        if (TextUtils.isEmpty(modelDb)) {
            Toast.makeText(New_Booking.this, "Model PC/Laptop is required", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(addressDb)) {
            Toast.makeText(New_Booking.this, "Address is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if ((!phoneNoDb.startsWith("01")) && phoneNoDb.length()< 12 && TextUtils.isEmpty(phoneNoDb)) {

            Toast.makeText(New_Booking.this, "Phone Number is Invalid", Toast.LENGTH_LONG).show();

            return;
        }







        if ((!TextUtils.isEmpty(modelDb) && !TextUtils.isEmpty(addressDb) && !TextUtils.isEmpty(phoneNoDb)&&(phoneNoDb.startsWith("01")) && (phoneNoDb.length()< 12) && !TextUtils.isEmpty(phoneNoDb))) {
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Bookings");
            DatabaseReference current_user_db = mDatabaseRef.child(userid).child(brandDb + " " + modelDb);
            current_user_db.child("BrandModel").setValue(brandDb+" "+modelDb);
            current_user_db.child("Date").setValue(dateDb);
            current_user_db.child("Service").setValue(serviceDb);
            current_user_db.child("Brand").setValue(brandDb);
            current_user_db.child("Model").setValue(modelDb);
            current_user_db.child("PickupTime").setValue(pickupTimeDb);
            current_user_db.child("Address").setValue(addressDb);
            current_user_db.child("PhoneNo").setValue(phoneNoDb);
            current_user_db.child("Reason").setValue(" ");
            current_user_db.child("ChargeRM").setValue(" ");
            current_user_db.child("Repairing").setValue(" ");
            current_user_db.child("Status").setValue("Request Pending");
            current_user_db.child("isUpdated").setValue(false);
            current_user_db.child("isAccepted").setValue(false);

            Intent i = new Intent(New_Booking.this, Booking_Service.class);
            startActivity(i);

            Toast.makeText(New_Booking.this,"Successfull Book a Service...", Toast.LENGTH_SHORT).show();

//            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("AdminNoti").child(userid).child(brandDb+" "+modelDb);
//            DatabaseReference notiAdmin =mDatabaseRef;
//            notiAdmin.child("Date").setValue(dateDb);
//            notiAdmin.child("PickupTime").setValue(pickupTimeDb);
//            notiAdmin.child("Status").setValue("Request Pending");
//            notiAdmin.child("Reason").setValue(" ");

            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("CustNoti").child(userid).child(brandDb+" "+modelDb);
            DatabaseReference notiCust =mDatabaseRef;
            notiCust.child("Status").setValue("Request Pending");




            finish();

        }

    }




}