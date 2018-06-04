package com.example.aliff.bookingsystemcomputerservice;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;


public class diagnoseAdapter extends AppCompatActivity{
    private String CustId;
    private String userid, value,accesslevel;
    private ArrayList<String> spinnerData = new ArrayList<>();
    private AutoCompleteTextView   autoCompleteTextView;
    private RecyclerView recyclerView;
    private List<InvoiceModalInventory> hold_item = new ArrayList<>();
    private List<InvoiceModalInventory> selected_item = new ArrayList<>();
    private UsedItemAdapter usedItemAdapter;
    private Button btn,btnDoneDiagnose;
    private int totalPrice =0;
    private TextView mTotal;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose_adapter);
        Intent intent = getIntent();
        CustId = intent.getStringExtra("CustID");
        value = intent.getStringExtra("Value");
        accesslevel = intent.getStringExtra("ACCESSLEVEL");
        mTotal = findViewById(R.id.tvPriceHardware);
        btnDoneDiagnose=(Button)findViewById(R.id.DoneDiagnose);
        btn = findViewById(R.id.mBtnAdd);
        recyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        usedItemAdapter = new UsedItemAdapter(getApplication(), selected_item, R.layout.activity_diagnose);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.setAdapter(usedItemAdapter);





        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.tvAutoComplete);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, spinnerData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextView.setAdapter(aa);
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showDropDown();
            }
        });

        autoCompleteTextView.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {

                Toast.makeText(getApplicationContext(),autoCompleteTextView.getListSelection()+"",Toast.LENGTH_SHORT).show();
                final int selected_item = autoCompleteTextView.getListSelection()+1;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        usedItemAdapter.add(hold_item.get(selected_item),usedItemAdapter.getItemCount());
                        totalPrice = usedItemAdapter.getTotalPrice();
                        mTotal.setText(String.valueOf(totalPrice));

                    }
                });
            }
        });

        btnDoneDiagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(diagnoseAdapter.this,displayBooking.class);

                intent.putExtra("ACCESSLEVEL", accesslevel);
                intent.putExtra("userid", CustId);
//                intent.putExtra("Va")
                startActivity(intent);
                Toast.makeText(diagnoseAdapter.this,"Saved...", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getSpinnerData();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(diagnoseAdapter.this, MainActivity.class);
            startActivity(i);
        }

        userid = currentUser.getUid();


    }





    public void getSpinnerData() {
            hold_item.clear();
            spinnerData.clear();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference().child("inventory");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.getKey();
                    InvoiceModalInventory DATA = areaSnapshot.getValue(InvoiceModalInventory.class);
                   hold_item.add(DATA); // added list
                    spinnerData.add(areaName);  // spinner's data
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }



}