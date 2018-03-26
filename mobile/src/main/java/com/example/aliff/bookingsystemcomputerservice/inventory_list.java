package com.example.aliff.bookingsystemcomputerservice;

import android.app.ActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class inventory_list extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> values = new ArrayList<String>();
    private String userid;
    private FirebaseAuth mAuth;
    private String accesslevel;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_list);

        listView = (ListView) findViewById(R.id.listView);
        adapter =  new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                Intent i = new Intent(inventory_list.this, inventory_form.class);
                i.putExtra("ID", itemValue);
             
                i.putExtra("PAGE", "INVENTORY");
                startActivity(i);


            }

        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(inventory_list.this, MainActivity.class);
            startActivity(i);
        }
        userid = currentUser.getUid();
        getInventory();
        listView.setAdapter(adapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.inventorylist_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(inventory_list.this, Admin_Main_menu.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(inventory_list.this, Admin_Main_menu.class);
                startActivity(i);
                finish();
                break;

            case R.id.btnAdd:
                Intent j = new Intent(inventory_list.this, inventory_form.class);
                j.putExtra("ID", "NULL");
                j.putExtra("PAGE", "ADD");
                startActivity(j);
                finish();
                break;


        }
        return true;
    }


    public void getInventory() {
        values.clear();
        adapter.clear();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("inventory");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                    String value = uniqueKeySnapshot.getKey().toString();
                    adapter.add(value);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }



        });


    }
}
