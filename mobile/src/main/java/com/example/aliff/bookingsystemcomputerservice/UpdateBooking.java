package com.example.aliff.bookingsystemcomputerservice;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateBooking extends AppCompatActivity implements View.OnClickListener {

    private String itemTime;
    private String itemReason;
    private String itemDate;

    ArrayAdapter<CharSequence> adapterTime,adapterReason;

    private TextView timeEt,reasonEt;

    private EditText dateEt;

    private Spinner etTime,etReason;

    private Button btnUpdate,btnBack;

        private String accesslevel;
        private String CustId;
        private String userid, value;
        private FirebaseAuth mAuth;
        public DatabaseReference myRef;

        private String timeDb,dateDb,reasonDb;
        private int mYear, mMonth, mDay;
        private String itemID;
        private String key;
        private String fromPage;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_update_booking);
            Intent intent = getIntent();
            itemID = intent.getStringExtra("ID");
            fromPage = intent.getStringExtra("PAGE");

            userid = intent.getStringExtra("userid");
            value = intent.getStringExtra("value");
            accesslevel = intent.getStringExtra("ACCESSLEVEL");

            userid = intent.getStringExtra("userid");

            btnUpdate = (Button)findViewById(R.id.btnUpdateRequest);
            btnBack = (Button)findViewById(R.id.btnBackRequest);


            etTime=(Spinner)findViewById(R.id.etTime) ;

            etReason=(Spinner)findViewById(R.id.etReason) ;


            timeEt=(TextView)findViewById(R.id.timeEt);
            dateEt=(EditText) findViewById(R.id.dateEt);
            reasonEt=(TextView)findViewById(R.id.reasonEt);

            btnUpdate.setOnClickListener(this);
            btnBack.setOnClickListener(this);
            dateEt.setOnClickListener(this);


            adapterTime = ArrayAdapter.createFromResource(this,
                    R.array.list_time, android.R.layout.simple_spinner_item);

            adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            etTime.setAdapter(adapterTime);

            etTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String value = (String) adapterTime.getItem(position);
                    timeEt.setText(value);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });






            adapterReason = ArrayAdapter.createFromResource(this,
                    R.array.list_reason, android.R.layout.simple_spinner_item);

            adapterReason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            etReason.setAdapter(adapterReason);

            etReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String value = (String) adapterReason.getItem(position);
                    reasonEt.setText(value);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


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

            userid = currentUser.getUid();

            getData();



        }

        private void datePicker(View view) {

            if (view == dateEt) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dateEt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnUpdateRequest :

                  submit();
                    break;
                case R.id.btnBackRequest:
                    Intent i = new Intent(UpdateBooking.this, displayBooking.class);

                    startActivity(i);
                    finish();
                    break;
                case R.id.dateEt:

                    datePicker(view);
                    break;
            }
        }

        public void getData(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Bookings").child(userid);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UpdateBooking.Update update = dataSnapshot.getValue(UpdateBooking.Update.class);
                timeEt.setText(update.itemTime);
                reasonEt.setText(update.itemReason);
                dateEt.setText(update.itemDate);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    public void submit() {
        itemTime = timeEt.getText().toString();
        itemReason = reasonEt.getText().toString();
        itemDate = dateEt.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Bookings");

//        if (fromPage.equals("Bookings")) {
//            key = itemID;
//
//        } else {
//            key = itemTime + " " + itemDate;
//        }

        UpdateBooking.Update update = new UpdateBooking.Update(itemTime, itemReason, itemDate);
        Map<String, Object> updateValue = update.toMap();
        Map<String, Object> updatePut = new HashMap<>();
        updatePut.put(itemTime + " " + itemDate
                , updateValue);

        //myRef.setValue(inventoryPut);
        myRef.child(key).setValue(updateValue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Update Successfully Updated", Toast.LENGTH_LONG).show();
                    onBackPressed();
                } else {
                    Toast.makeText(getApplicationContext(), "Update Failed, Please Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @IgnoreExtraProperties
    public static class Update {

        public String itemTime;
        public String itemReason;
        public String itemDate;

        public Update() {

        }

        public Update(String itemTime, String itemReason, String itemDate) {
            this.itemTime = itemTime;
            this.itemReason = itemReason;
            this.itemDate = itemDate;

        }


        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("itemTime", itemTime);
            result.put("itemReason", itemReason);
            result.put("itemDate", itemDate);

            return result;
        }


    }

}
