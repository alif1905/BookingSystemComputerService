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
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;
    private Context context;
    private boolean openNoti=false;



    private FirebaseAuth mAuth;
    ArrayAdapter<CharSequence> adapterService, adapterbrand, adapterTime;

    private String userid,serviceDb, brandDb, modelDb, pickupTimeDb, addressDb, phoneNoDb,dateDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__booking);

        //Notification
        context = this;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);

        remoteViews = new RemoteViews(getPackageName(),R.layout.custom_notification);
        remoteViews.setImageViewResource(R.id.notif_icon,R.drawable.fastplay2);
        remoteViews.setTextViewText(R.id.notif_title,"You have receive Notification");
        remoteViews.setProgressBar(R.id.progressBar,100,40,true);



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


    public void triggerNoti(){

        if (notificationManager.getActiveNotifications().length<=0) {
            notification_id = (int) System.currentTimeMillis();

            Intent button_intent = new Intent(this,displayBooking.class);
            button_intent.putExtra("id",notification_id);
            PendingIntent button_pending_event = PendingIntent.getBroadcast(context,notification_id,
                    button_intent,0);

            remoteViews.setOnClickPendingIntent(R.id.buttonShowNotification,button_pending_event);

            Intent notification_intent = new Intent(context,MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notification_intent,0);

            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setCustomBigContentView(remoteViews)
                    .setContentIntent(pendingIntent);

            notificationManager.notify(notification_id,builder.build());

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


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        myRef = database.getReference().child("AdminNoti");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                triggerNoti();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                triggerNoti();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                triggerNoti();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
        if (TextUtils.isEmpty(phoneNoDb)) {
            Toast.makeText(New_Booking.this, "Phone Number is required", Toast.LENGTH_SHORT).show();
            return;
        }


        if ((!TextUtils.isEmpty(modelDb) && !TextUtils.isEmpty(addressDb) && !TextUtils.isEmpty(phoneNoDb))) {
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

            current_user_db.child("Status").setValue("Request Pending");
            current_user_db.child("isUpdated").setValue(false);
            current_user_db.child("isAccepted").setValue(false);

            Intent i = new Intent(New_Booking.this, Booking_Service.class);
            startActivity(i);

            Toast.makeText(New_Booking.this,"Successfull Book a Service...", Toast.LENGTH_SHORT).show();

            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("AdminNoti");
            DatabaseReference noti =mDatabaseRef;
            noti.child("Noti").setValue(brandDb+" "+modelDb);


            finish();

        }

    }




}