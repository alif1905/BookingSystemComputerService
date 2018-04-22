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

public class Customer_Main_menu extends AppCompatActivity {
    private Button mLogoutCustomer, mChatRoomCust, mProfileCust,mBookCust;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String accesslevel;
    private String userid, value;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;
    private Context context;
    private boolean openNoti=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__main_menu);


        //Notification
        context = this;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);

        remoteViews = new RemoteViews(getPackageName(),R.layout.custom_notification);
        remoteViews.setImageViewResource(R.id.notif_icon,R.drawable.fastplay2);
        remoteViews.setTextViewText(R.id.notif_title,"You have receive Notification");
//        remoteViews.setProgressBar(R.id.progressBar,100,40,true);

        Intent intent = getIntent();

        value = intent.getStringExtra("Value");


        mLogoutCustomer = findViewById(R.id.LogoutCustomer);
        mChatRoomCust = findViewById(R.id.chatroomCustomer);
        mProfileCust = findViewById(R.id.ProfileCustomer);
        mBookCust = findViewById(R.id.btnBookCust);



        mLogoutCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Customer_Main_menu.this, "Successfull Logout...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Customer_Main_menu.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mChatRoomCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Customer_Main_menu.this, "Chatroom...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Customer_Main_menu.this, Chat_Room1.class);
                startActivity(intent);
                finish();
                return;

            }
        });

        mProfileCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Customer_Main_menu.this, "Profile...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Customer_Main_menu.this, Profile_Customer.class);
                startActivity(intent);
                finish();
                return;

            }
        });



        mBookCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Customer_Main_menu.this, "Booking New Service", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Customer_Main_menu.this, Booking_Service.class);
                startActivity(intent);
                finish();
                return;

            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userid = currentUser.getUid();


        Toast.makeText(getApplicationContext(), "Welcome " + currentUser.getEmail(), Toast.LENGTH_LONG).show();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Register").child(userid);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Auth auth = dataSnapshot.getValue(Auth.class);
                accesslevel = auth.accessLevel;
                if (!accesslevel.equals("Customer")) {
                    Toast.makeText(getApplicationContext(), "Login failed, please Login as customer...", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Customer_Main_menu.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();



                    myRef = database.getReference().child("CustNoti");


                        myRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {




                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();



                                myRef = database.getReference().child("CustNoti");

                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String hide = dataSnapshot.getValue(String.class);


                                        if (hide.equals("Request Accepted")) {

                                            triggerNoti();


                                        } else if (hide.equals("Request Updated")) {
                                            triggerNoti();
                                        }
                                    else if (hide.equals("Return Service")) {
                                        triggerNoti();
                                    }
                                        else if (hide.equals("Done Service")) {
                                            triggerNoti();
                                        } else if (hide.equals("Request Pending")) {

                                        } else if (hide.equals("Booking Updated Cancel by Customer")) {
                                            triggerNoti();
                                        } else {
                                            triggerNoti();

                                        }


                                        Toast.makeText(getApplication(), hide, Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



    public void triggerNoti(){

        if (notificationManager.getActiveNotifications().length<=0) {
            notification_id = (int) System.currentTimeMillis();

            Intent button_intent = new Intent(this,displayBooking.class);
            button_intent.putExtra("id",notification_id);
            PendingIntent button_pending_event = PendingIntent.getBroadcast(context,notification_id,
                    button_intent,0);

//            remoteViews.setOnClickPendingIntent(R.id.buttonShowNotification,button_pending_event);

            Intent notification_intent = new Intent(context,MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notification_intent,0);

            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setCustomBigContentView(remoteViews)
                    .setContentIntent(pendingIntent);

            notificationManager.notify(notification_id,builder.build());

        }



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