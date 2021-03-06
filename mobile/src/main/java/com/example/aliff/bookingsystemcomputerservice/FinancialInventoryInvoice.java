//package com.example.aliff.bookingsystemcomputerservice;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.Toast;
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
//import java.util.Iterator;
//
//public class FinancialInventoryInvoice extends AppCompatActivity implements View.OnClickListener {
//    private Button mBackInvntoryFinancial;
//    private FirebaseAuth mAuth;
//    private DatabaseReference myRef;
//
//    private ArrayList<InvoiceModalInventory> dataModels;
//    private ListView listView;
//    private   InvoiceInventory adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_financial_inventory_invoice);
//
//        listView=(ListView)findViewById(R.id.listViewInventoryManage);
//        mBackInvntoryFinancial=(Button)findViewById(R.id.btnBckInventoryinvoice);
//        dataModels= new ArrayList<>();
//        getInformation();
//
//
//
//
//        adapter= new InvoiceInventory(dataModels,getApplicationContext());
//        listView.setAdapter(adapter);
//
//        mBackInvntoryFinancial.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId()) {
//
//
//            case R.id.btnBckInventoryinvoice:
//
//                Intent i = new Intent(FinancialInventoryInvoice.this, Manage_Financial.class);
//                startActivity(i);
//                finish();
//                break;
//        }
//
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent i = new Intent(FinancialInventoryInvoice.this, Manage_Financial.class);
//        startActivity(i);
//        finish();
//    }
//
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser == null) {
//            Intent i = new Intent(FinancialInventoryInvoice.this, Manage_Financial.class);
//            startActivity(i);
//        }
//
//    }
//
//    private void getInformation() {
//       // dataModels.clear();
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
//                for (Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator(); it.hasNext(); ) {
//                    DataSnapshot RootSnapshot = it.next();
//
//
//                    InvoiceService invoice = RootSnapshot.getValue(InvoiceService.class);
//
//
//                    dataModels.add(new InvoiceModalInventory(
//
//                            invoice.ItemName,
//                            invoice.ItemBrand,
//                            invoice.ItemPrice,
//                            invoice.ItemQuantity,
//                            invoice.ItemAvailable
//                    ));
//                    adapter.notifyDataSetChanged();
//
//                    Toast.makeText(getApplicationContext(), invoice.ItemBrand, Toast.LENGTH_SHORT).show();
//                }
//
//                }
//
//
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
//
//    @IgnoreExtraProperties
//    public static class InvoiceService {
//
//        public String ItemName;
//        public String ItemBrand;
//        public String ItemPrice;
//        public String ItemQuantity;
//        public String ItemAvailable;
//
//
//        public InvoiceService() {
//            // Default constructor required for calls to DataSnapshot.getValue(User.class)
//        }
//
//        public InvoiceService(String itemName, String itemBrand, String itemPrice, String itemQuantity, String itemAvailable) {
//
//
//
//            this.ItemName = itemName;
//            this.ItemBrand = itemBrand;
//            this.ItemPrice = itemPrice;
//            this.ItemQuantity = itemQuantity;
//            this.ItemAvailable = itemAvailable;
//
//        }
//    }
//
//
//}