package com.example.aliff.bookingsystemcomputerservice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class inventory_form extends AppCompatActivity implements View.OnClickListener {

    private EditText etName, etBrand, etPrice, etQuantity;
    private Button btAdd;
    private String itemName;
    private String itemBrand;
    private String itemPrice;
    private String itemQuantity;
    private DatabaseReference myRef;
    private String fromPage;
    private FirebaseAuth mAuth;
    private String userid;
    private String itemID;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_form);

        Intent intent = getIntent();
        itemID = intent.getStringExtra("ID");
        fromPage = intent.getStringExtra("PAGE");

        etName = (EditText) findViewById(R.id.etItemName);
        etBrand = (EditText) findViewById(R.id.etBrand);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etQuantity = (EditText) findViewById(R.id.etQuantity);
        btAdd = (Button) findViewById(R.id.btnAdd);
        btAdd.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(inventory_form.this, MainActivity
                    .class);
            startActivity(i);
        }

        userid = currentUser.getUid();

        if (fromPage.equals("INVENTORY")) {
            btAdd.setText("UPDATE");
            getData();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(inventory_form.this, inventory_list.class);
        startActivity(i);
        finish();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnAdd:
                submit();
                break;
        }

    }


    public void getData() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("inventory").child(itemID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Inventory inventory = dataSnapshot.getValue(Inventory.class);
                etName.setText(inventory.itemName);
                etBrand.setText(inventory.itemBrand);
                etPrice.setText(inventory.itemPrice);
                etQuantity.setText(inventory.itemQuantity);
                etName.setFocusable(false);
                etBrand.setFocusable(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    public void submit() {
        itemName = etName.getText().toString();
        itemBrand = etBrand.getText().toString();
        itemPrice = etPrice.getText().toString();
        itemQuantity = etQuantity.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("inventory");

        if(fromPage.equals("INVENTORY")){
            key = itemID;
        }
            else {
            key = itemName +" " +itemBrand;
        }

        Inventory inventory = new Inventory(itemName, itemBrand, itemPrice, itemQuantity);
        Map<String, Object> inventoryValue = inventory.toMap();
        Map<String, Object> inventoryPut = new HashMap<>();
//        inventoryPut.put(itemName + " " + itemBrand
//                , inventoryValue);

        //myRef.setValue(inventoryPut);
        myRef.child(key).setValue(inventoryValue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Inventory Successfully Updated",Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Inventory Update Failed, Please Try Again",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @IgnoreExtraProperties
    public static class Inventory {

        public String itemName;
        public String itemBrand;
        public String itemPrice;
        public String itemQuantity;

        public Inventory() {

        }

        public Inventory(String itemName, String itemBrand, String itemPrice, String itemQuantity) {
            this.itemName = itemName;
            this.itemBrand = itemBrand;
            this.itemPrice = itemPrice;
            this.itemQuantity = itemQuantity;
        }


        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("itemName", itemName);
            result.put("itemBrand", itemBrand);
            result.put("itemPrice", itemPrice);
            result.put("itemQuantity", itemQuantity);
            return result;
        }


    }
}

