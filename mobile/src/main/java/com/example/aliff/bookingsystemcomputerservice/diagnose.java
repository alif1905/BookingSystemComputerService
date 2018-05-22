//package com.example.aliff.bookingsystemcomputerservice;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ListView;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.IgnoreExtraProperties;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//public class diagnose extends AppCompatActivity implements View.OnClickListener {
//
//
//    private FirebaseAuth mAuth;
//    private DatabaseReference myRef;
//
//    private ArrayList<InvoiceModalInventory> dataModels;
//    private ListView listView;
//    private diagnoseAdapter adapter;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_diagnose);
//
//        listView = (ListView) findViewById(R.id.listViewDiagnose);
//
//        dataModels = new ArrayList<>();
//
//
//        adapter = new diagnoseAdapter(dataModels, this);
//        listView.setAdapter(adapter);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent i = new Intent(diagnose.this, displayBooking.class);
//        startActivity(i);
//        finish();
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser == null) {
//            Intent i = new Intent(diagnose.this, Manage_Financial.class);
//            startActivity(i);
//        }
//
//    }
//
//    private void getInformation() {
//        //dataModels.clear();
//
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//        myRef = database.getReference().child("inventory");
//
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
//
//                    String key = uniqueKeySnapshot.getKey().toString();
//                    for (DataSnapshot RootSnapshot : uniqueKeySnapshot.getChildren()) {
//
//                        diagnose.InvoiceService invoice = RootSnapshot.getValue(diagnose.InvoiceService.class);
//
//
//                        dataModels.add(new InvoiceModalInventory(
//                               invoice.itemName,
//                                invoice.itemBrand,
//                                invoice.itemPrice,
//                                invoice.itemQuantity
//                        ));
//                        adapter.notifyDataSetChanged();
//
//
//                    }
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("bookingsList", "Failed to read value.", error.toException());
//            }
//
//
//        });
//    }
//
//    @IgnoreExtraProperties
//    public static class InvoiceService {
//        public String itemName;
//        public String itemBrand;
//        public String itemPrice;
//        public String itemQuantity;
//
//
//
//
//        public InvoiceService() {
//            // Default constructor required for calls to DataSnapshot.getValue(User.class)
//        }
//
//
//        public InvoiceService(String itemname, String itembrand, String itemprice, String itemquantity ) {
//            this.itemName =itemname;
//            this.itemBrand = itembrand;
//            this.itemPrice = itemprice;
//            this.itemQuantity = itemquantity;
//
//
//        }
//    }
//
//}
