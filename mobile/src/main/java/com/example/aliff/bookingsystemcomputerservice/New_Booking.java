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

import java.text.DateFormat;
import java.util.Calendar;

public class New_Booking extends AppCompatActivity {

    private Button mBackBtn;
    private TextView mDisplayTimeDate,mModel,mAddress,mphoneCust;
    private EditText mEditModel,mEditAddress,mEditPhone;
    private Spinner mspinnerService,mspinnerBrand,mSpinnerTime;

    ArrayAdapter<CharSequence> adapterService,adapterbrand,adapterTime;

    private String spinnerServ,spinnerBrand,spinnerTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__booking);


        mBackBtn = findViewById(R.id.BackBtn);

        mspinnerService= findViewById(R.id.spinnerService_Require);
        mspinnerBrand= findViewById(R.id.spinnerBrand);
        mSpinnerTime= findViewById(R.id.spinnerSelecttimePicker);

        mDisplayTimeDate = findViewById(R.id.DisplayTimeAndDate);
        mModel= findViewById(R.id.model);
        mAddress= findViewById(R.id.addressCust);
        mphoneCust= findViewById(R.id.PhoneCust);

        mEditModel= findViewById(R.id.editTextModel);
        mEditAddress= findViewById(R.id.editTextAddressCust);
        mEditPhone= findViewById(R.id.editTextphoneCust);

        updateTimeDate();


        adapterService =ArrayAdapter.createFromResource(this,
                R.array.list_service, android.R.layout.simple_spinner_item);

        adapterService.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mspinnerService.setAdapter(adapterService);

        mspinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        adapterbrand =ArrayAdapter.createFromResource(this,
                R.array.list_brand, android.R.layout.simple_spinner_item);

        adapterbrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mspinnerBrand.setAdapter(adapterbrand);

        mspinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterTime =ArrayAdapter.createFromResource(this,
                R.array.list_time, android.R.layout.simple_spinner_item);

        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerTime.setAdapter(adapterTime);

        mSpinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });







        mBackBtn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {


                Intent intent = new Intent(New_Booking.this, Booking_Service.class);
                startActivity(intent);
                Toast.makeText(New_Booking.this, "Back...", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        });


    }
    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    Calendar dateTime= Calendar.getInstance();
    int day, month, year;
    DatePickerDialog.OnDateSetListener d= new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateTime.set(Calendar.YEAR,year);
            dateTime.set(Calendar.MONTH,month);
            dateTime.set(Calendar.DAY_OF_MONTH,day);
            updateTimeDate();

            //getUserInfo();
        }
    };

    private void updateDate() {
        new DatePickerDialog(this,d,dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH),dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void updateTimeDate() {
        mDisplayTimeDate.setText(formatDateTime.format(dateTime.getTime()));
    }

}