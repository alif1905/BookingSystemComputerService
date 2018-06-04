package com.example.aliff.bookingsystemcomputerservice;


import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
        CustId = intent.getStringExtra("custid");
        value = intent.getStringExtra("value");
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

   autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
       @Override
       public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
           Toast.makeText(getApplicationContext(),autoCompleteTextView.getListSelection()+"",Toast.LENGTH_SHORT).show();
           btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   usedItemAdapter.add(hold_item.get(i),usedItemAdapter.getItemCount());
                   totalPrice = usedItemAdapter.getTotalPrice();
                   mTotal.setText(String.valueOf(totalPrice));

               }
           });
       }
   });
        btnDoneDiagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                try {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Bookings").child(CustId).child(value).child("Repaired");
                    myRef.setValue(usedItemAdapter.getAll()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                int totalItemCost =0;
                                for(InvoiceModalInventory inventory : usedItemAdapter.getAll()){
                                    totalItemCost = Integer.parseInt(inventory.getItemQuantity())*Integer.parseInt(inventory.getItemPrice()) + totalItemCost;
                                }

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("Bookings").child(CustId).child(value).child("totalItemCost");
                                myRef.setValue(totalItemCost);
                            }
                        }
                    });

                } catch (Exception e) {


                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


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
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {


                    try {
                        String areaName = areaSnapshot.getKey();
                        InvoiceModalInventory DATA = areaSnapshot.getValue(InvoiceModalInventory.class);
                        hold_item.add(DATA); // added list
                        spinnerData.add(areaName);  // spinner's data
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }



}