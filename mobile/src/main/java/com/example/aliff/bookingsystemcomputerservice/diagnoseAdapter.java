package com.example.aliff.bookingsystemcomputerservice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class diagnoseAdapter extends AppCompatActivity{

    private DatabaseReference myRef;


    LinearLayout container;
    private FirebaseAuth mAuth;
    private Spinner areaSpinner;
    private ArrayAdapter<String> areasAdapter;
    private ArrayList<String> items = new ArrayList<>();
    private ArrayList<InvoiceModalInventory> hold_item = new ArrayList<>();
    private ArrayList<InvoiceModalInventory> selected_item = new ArrayList<>();

    private TextView itemName,itemQuantity,QuantityInneed;
    private LinearLayout linearLayout;

    private ArrayAdapter<InvoiceModalInventory> arrayListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose_adapter);
//

        itemName=(TextView)findViewById(R.id.tvItemName);
        itemQuantity=(TextView)findViewById(R.id.tvItemQuantity);
        QuantityInneed=(TextView)findViewById(R.id.tvQuantityInNeed);
        linearLayout = (LinearLayout)findViewById(R.id.holder2);


        arrayListAdapter = new UsedItemAdapter(selected_item, getApplicationContext());
//        areaSpinner.setAdapter(arrayListAdapter);


        areaSpinner = findViewById(R.id.spinnerSelectItemDiagnose);
        areasAdapter = new ArrayAdapter<>(diagnoseAdapter.this, android.R.layout.simple_spinner_item, items);
        areaSpinner.setAdapter(areasAdapter);



        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areasAdapter.getItem(position);
                selected_item.add(hold_item.get(position));
                arrayListAdapter.notifyDataSetChanged();

            };

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        getSpinnerData();


    }

    public void getSpinnerData() {


        FirebaseDatabase database = FirebaseDatabase.getInstance();


        myRef = database.getReference().child("inventory");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.getKey();
                    InvoiceModalInventory DATA = areaSnapshot.getValue(InvoiceModalInventory.class);
                    hold_item.add(DATA);
                    items.add(areaName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }






//    private void listAllAddView() {
//        itemName.setText("");
//
//        int childCount = listView.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View thisChild = listView.getChildAt(i);
//            itemName.append(thisChild + "\n");
//
//            // AutoCompleteTextView childTextView = (AutoCompleteTextView) thisChild.findViewById(R.id.textout);
//            //String childTextViewValue = childTextView.getText().toString();
//            //itemName.append("= " + childTextViewValue + "\n");
//        }
//    }
}