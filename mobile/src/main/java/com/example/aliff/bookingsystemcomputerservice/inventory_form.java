package com.example.aliff.bookingsystemcomputerservice;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

    private Spinner etName, etBrand, etPrice, etQuantity;
    private Button btAdd,mbtnDelete;
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
    private String accesslevel;

    ArrayAdapter<CharSequence> adapterItemName, adapterBrandItem, adapterPrice, adapterQuantity;
    private String ItemNameDb, BrandItemDb, PriceDb, QuantityDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_form);

        Intent intent = getIntent();
        itemID = intent.getStringExtra("ID");
        fromPage = intent.getStringExtra("PAGE");

        etName = (Spinner) findViewById(R.id.etItemName);
        etBrand = (Spinner) findViewById(R.id.etBrand);
        etPrice = (Spinner) findViewById(R.id.etPrice);
        etQuantity = (Spinner) findViewById(R.id.etQuantity);
        btAdd = (Button) findViewById(R.id.btnAdd);
        mbtnDelete=(Button)findViewById(R.id.btnDelete) ;
        btAdd.setOnClickListener(this);
        mbtnDelete.setOnClickListener(this);



        accesslevel = intent.getStringExtra("ACCESSLEVEL");


        adapterItemName = ArrayAdapter.createFromResource(this,
                R.array.list_item, android.R.layout.simple_spinner_item);

        adapterItemName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        etName.setAdapter(adapterItemName);

        etName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ItemNameDb = (String) adapterItemName.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        adapterBrandItem = ArrayAdapter.createFromResource(this,
                R.array.list_brand_item, android.R.layout.simple_spinner_item);

        adapterBrandItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        etBrand.setAdapter(adapterBrandItem);

        etBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BrandItemDb = (String) adapterBrandItem.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        adapterPrice = ArrayAdapter.createFromResource(this,
                R.array.list_price, android.R.layout.simple_spinner_item);

        adapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        etPrice.setAdapter(adapterPrice);

        etPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PriceDb = (String) adapterPrice.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adapterQuantity = ArrayAdapter.createFromResource(this,
                R.array.list_quantity, android.R.layout.simple_spinner_item);

        adapterQuantity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        etQuantity.setAdapter(adapterQuantity);

        etQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                QuantityDb = (String) adapterQuantity.getItem(position);

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

            case R.id.btnDelete:

                delete();
                break;
        }



    }

    public void delete() {
        AlertDialog alertDialog = new AlertDialog.Builder(inventory_form.this).create();
        alertDialog.setTitle("Delete Inventory");
        alertDialog.setMessage("Are you sure you want to delete this inventory?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                deleteResponse();


                            }
                        });

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.dismiss();
                    }
                });
        alertDialog.show();


    }

    public void deleteResponse() {

        AlertDialog alertDialog = new AlertDialog.Builder(inventory_form.this).create();
        alertDialog.setMessage("Booking Canceled");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent i = new Intent(inventory_form.this, inventory_list.class);
                        i.putExtra("ACCESSLEVEL", accesslevel);
                        startActivity(i);
                        finish();
                    }
                });
        alertDialog.show();


    }



    public void getData() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("inventory").child(itemID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Inventory inventory = dataSnapshot.getValue(Inventory.class);
                  itemName= (String) etName.getSelectedItem();
                itemBrand= (String) etBrand.getSelectedItem();
                itemPrice= (String) etPrice.getSelectedItem();
                itemQuantity= (String) etQuantity.getSelectedItem();
                etName.setFocusable(false);
                etBrand.setFocusable(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    public void submit() {
        itemName = etName.getSelectedItem().toString();
        itemBrand = etBrand.getSelectedItem().toString();
        itemPrice = etPrice.getSelectedItem().toString();
        itemQuantity = etQuantity.getSelectedItem().toString();
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
       inventoryPut.put(itemName + " " + itemBrand
                , inventoryValue);

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

