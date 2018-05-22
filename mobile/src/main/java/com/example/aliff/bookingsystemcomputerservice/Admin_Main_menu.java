package com.example.aliff.bookingsystemcomputerservice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class Admin_Main_menu extends AppCompatActivity implements View.OnClickListener {

    private Button mRecordInventory, mLogoutAdmin, mProfileAdmin;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String accesslevel;
    private Button mBookings;
    private Intent intent;
    private Button mMFinancial;



    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;
    private Context context;
    private boolean openNoti=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__main_menu);


      //  Notification
//        context = this;
//        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        builder = new NotificationCompat.Builder(this);
//
//        remoteViews = new RemoteViews(getPackageName(),R.layout.custom_notification);
//        remoteViews.setImageViewResource(R.id.notif_icon,R.drawable.fastplay2);
//        remoteViews.setTextViewText(R.id.notif_title,"You have receive Notification");
     //   remoteViews.setProgressBar(R.id.progressBar,100,40,true);



        mMFinancial=(Button)findViewById(R.id.btnFinancial);
        mLogoutAdmin =(Button) findViewById(R.id.LogoutAdmin);
        mProfileAdmin =(Button) findViewById(R.id.ProfileAdmin);
//        mRegisterAdmin =(Button) findViewById(R.id.RegisterAdmin);
        mBookings = (Button) findViewById(R.id.btnBookings);
        mRecordInventory = (Button) findViewById(R.id.btnRecordInventory);



        mBookings.setOnClickListener(this);
//        mRegisterAdmin.setOnClickListener(this);
        mProfileAdmin.setOnClickListener(this);
        mLogoutAdmin.setOnClickListener(this);
        mRecordInventory.setOnClickListener(this);
        mMFinancial.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {

        Intent intent;

        switch (view.getId()) {


            case R.id.btnFinancial:
                intent = new Intent(Admin_Main_menu.this, Manage_Financial.class);

                intent.putExtra("ACCESSLEVEL", accesslevel);

                startActivity(intent);
                finish();


                break;

            case R.id.btnRecordInventory:
                intent = new Intent(Admin_Main_menu.this, inventory_list.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnBookings:
                intent = new Intent(Admin_Main_menu.this, bookingsList.class);
                intent.putExtra("ACCESSLEVEL", "ADMIN");
                startActivity(intent);
                finish();

                break;
            case R.id.ProfileAdmin:
                intent = new Intent(Admin_Main_menu.this, Profile_Admin.class);
                startActivity(intent);
                finish();


                break;
//            case R.id.RegisterAdmin:
//                intent = new Intent(Admin_Main_menu.this, Admin_register.class);
//                startActivity(intent);
//                finish();
//                break;
            case R.id.LogoutAdmin:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(Admin_Main_menu.this, MainActivity.class);
                startActivity(intent);
                finish();

                break;


        }

    }


    @Override
    public void onStart() {
                super.onStart();
                // Check if user is signed in (non-null) and update UI accordingly.
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String id = currentUser.getUid();
                database = FirebaseDatabase.getInstance();
               myRef = database.getReference("Register").child(id);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                               Auth auth = dataSnapshot.getValue(Auth.class);
                               accesslevel = auth.accessLevel;
                               if (!accesslevel.equals("Admin")) {
                                        Toast.makeText(getApplicationContext(), "Login failed, please Login as admin...", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(Admin_Main_menu.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                else {
                                        Toast.makeText(getApplicationContext(), "Welcome "+auth.email, Toast.LENGTH_LONG).show();
                                    }
                            }

                    @Override
            public void onCancelled(DatabaseError databaseError) {

                                   }
      });

                            }

           @IgnoreExtraProperties
   public static class Auth {

                public String email;
                public String accessLevel;

               public Auth() {
               }

                public Auth(String email, String accessLevel) {
                        this.email = email;
                       this.accessLevel = accessLevel;
                  }

            }

}
