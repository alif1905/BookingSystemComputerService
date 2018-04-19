package com.example.aliff.bookingsystemcomputerservice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class displayBooking extends AppCompatActivity implements View.OnClickListener {

    private String accesslevel;
    private String CustId;
    private String userid, value;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private TextView tvStatus,tvAdd, tvBrand, tvModel, tvPhone, tvPick, tvService, tvDate,tvReason,tvCharge,tvRepairType;
    private TextView titlereason,titleRepaired,titleCharge;

    private ProgressDialog progressDialog;
    private Button mBtnReject;
    private Button mBtnAccept;
    private Button mDoneService;
    private Button btnCancelBooking;
    private Button btnReturnService;
    private Button btnContact;
    private Menu menu;
    private boolean disable = false;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;
    private Context context;
    private boolean openNoti=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_booking);
        Intent intent = getIntent();



        //Notification
        context = this;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);

        remoteViews = new RemoteViews(getPackageName(),R.layout.custom_notification);
        remoteViews.setImageViewResource(R.id.notif_icon,R.drawable.fastplay2);
        remoteViews.setTextViewText(R.id.notif_title,"You have receive Notification");
//        remoteViews.setProgressBar(R.id.progressBar,100,40,true);



        CustId = intent.getStringExtra("CustID");
        value = intent.getStringExtra("Value");
        accesslevel = intent.getStringExtra("ACCESSLEVEL");

        btnReturnService = (Button) findViewById(R.id.btnReturnService);
        btnContact = (Button) findViewById(R.id.btnContact);
        mDoneService = (Button) findViewById(R.id.btnDoneService);
        mBtnAccept = (Button) findViewById(R.id.btnAccept);
        mBtnReject = (Button) findViewById(R.id.btnReject);
        btnCancelBooking = (Button) findViewById(R.id.btnCancelBooking);
        tvDate = (TextView) findViewById(R.id.tvDate);


        titleCharge = (TextView) findViewById(R.id.CostCharge);
        titlereason = (TextView) findViewById(R.id.giveReason);
        titleRepaired = (TextView) findViewById(R.id.RepairedPart);


        tvCharge = (EditText) findViewById(R.id.tvCostCharge);
        tvRepairType = (TextView) findViewById(R.id.tvRepairedType);
        tvStatus = (TextView) findViewById(R.id.tvStatusService);
        tvAdd = (TextView) findViewById(R.id.tvAddress);
        tvBrand = (TextView) findViewById(R.id.tvBrand);
        tvModel = (TextView) findViewById(R.id.tvModel);
        tvPhone = (TextView) findViewById(R.id.tvPhoneNo);
        tvPick = (TextView) findViewById(R.id.tvPickUpTime);
        tvService = (TextView) findViewById(R.id.tvServiceType);
        tvReason = (TextView) findViewById(R.id.tvReason);
        progressDialog = ProgressDialog.show(displayBooking.this, "Please wait...", "Processing...", true);
//

        mBtnAccept.setOnClickListener(this);
        mBtnReject.setOnClickListener(this);
        btnCancelBooking.setOnClickListener(this);
        btnReturnService.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        mDoneService.setOnClickListener(this);




        if (accesslevel.equals("USER")) {
            mBtnAccept.setVisibility(View.GONE);
            mBtnReject.setVisibility(View.GONE);
            mDoneService.setVisibility(View.GONE);
                tvReason.setVisibility(View.GONE);
                titlereason.setVisibility(View.GONE);
                tvRepairType.setVisibility(View.GONE);
                tvCharge.setVisibility(View.GONE);
            btnCancelBooking.setVisibility(View.GONE);
                titleRepaired.setVisibility(View.GONE);
                titleCharge.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnReturnService.setVisibility(View.GONE);
            mDoneService.setVisibility(View.GONE);

        } else {


                tvReason.setVisibility(View.GONE);
                titlereason.setVisibility(View.GONE);
                tvRepairType.setVisibility(View.GONE);
                tvCharge.setVisibility(View.GONE);
            btnCancelBooking.setVisibility(View.GONE);
                titleRepaired.setVisibility(View.GONE);
                titleCharge.setVisibility(View.GONE);
            mBtnAccept.setVisibility(View.VISIBLE);
            mBtnReject.setVisibility(View.VISIBLE);
            mDoneService.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnReturnService.setVisibility(View.GONE);
            mDoneService.setVisibility(View.GONE);

        }
    }

        @Override
        public void onClick (View view){


            switch (view.getId()) {


                case R.id.btnCancelBooking:

                    AlertDialog alertDialog2 = new AlertDialog.Builder(displayBooking.this).create();
                    alertDialog2.setTitle("Accept Request");
                    alertDialog2.setMessage("Are you sure accept this booking?");
                    alertDialog2.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int which) {
                                    Intent i = new Intent(displayBooking.this, displayBooking.class);

                                    cancelRequest();

                                }
                            });
                    alertDialog2.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            });
                    alertDialog2.show();

                    break;

                case R.id.btnAccept:

                    AlertDialog alertDialog = new AlertDialog.Builder(displayBooking.this).create();
                    alertDialog.setTitle("Accept Request");
                    alertDialog.setMessage("Are you sure accept this booking?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int which) {
                                    Intent i = new Intent(displayBooking.this, displayBooking.class);

                                    acceptRequest();

                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            });
                    alertDialog.show();


                    break;
                case R.id.btnReject:
                    Intent i = new Intent(displayBooking.this, UpdateBooking.class);
                    i.putExtra("ACCESSLEVEL", accesslevel);
                    i.putExtra("userid", CustId);
                    i.putExtra("value", value);
                    startActivity(i);
                    finish();


                    break;

                case R.id.btnDoneService:

                    AlertDialog alertDialog4 = new AlertDialog.Builder(displayBooking.this).create();
                    alertDialog4.setTitle("Done Service");
                    alertDialog4.setMessage("Are you sure Done Service this booking?");
                    alertDialog4.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int which) {
                                    Intent i = new Intent(displayBooking.this, displayBooking.class);

                            doneService();

                                }
                            });
                    alertDialog4.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            });
                    alertDialog4.show();

                    break;
                case R.id.btnReturnService:

//                    AlertDialog alertDialog3 = new AlertDialog.Builder(displayBooking.this).create();
//                    alertDialog3.setTitle("Accept Request");
//                    alertDialog3.setMessage("Are you sure accept this booking?");
//                    alertDialog3.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(final DialogInterface dialog, int which) {
//                                    Intent i = new Intent(displayBooking.this, displayBooking.class);
//
////                                    cancelRequest();
//
//                                }
//                            });
//                    alertDialog3.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    dialog.dismiss();
//
//                                }
//                            });
//                    alertDialog3.show();

                    break;

                case R.id.btnContact:



                    break;
            }

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


    public void doneService() {

        Toast.makeText(getApplicationContext(), "Done Service Triggered", Toast.LENGTH_LONG).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Bookings").child(CustId).child(value).child("Status");
        myRef.setValue("Done Service");
        getBookings();
        disable = false;
        if (accesslevel.equals("USER")) {
            myRef = database.getReference().child("Bookings").child(userid).child(value);
        } else {
            myRef = database.getReference().child("Bookings").child(CustId).child(value);
        }

    }


    public void cancelRequest(){
        Toast.makeText(getApplicationContext(), "cancelRequest Triggered", Toast.LENGTH_LONG).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Bookings").child(userid).child(value).child("Status");
        myRef.setValue("Booking Updated Cancel by Customer");
        getBookings();
        disable = false;
        if (accesslevel.equals("USER")) {
            myRef = database.getReference().child("Bookings").child(userid).child(value);
        } else {
            myRef = database.getReference().child("Bookings").child(CustId).child(value);
        }
    }
    public void cancelBookingHide(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Bookings").child(userid).child(value).child("Status");
//        myRef = database.getReference().child("Bookings").child(userid).child(value).child("Status Updated");
//        myRef=database.getReference().child("Bookings").child(userid).child("Brand" + " " + "Model");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String hide=dataSnapshot.getValue(String.class);


                if (hide.equals("Request Accepted")) {

                    btnCancelBooking.setVisibility(View.GONE);
                    tvRepairType.setVisibility(View.VISIBLE);
                    tvCharge.setVisibility(View.VISIBLE);
                    tvCharge.setFocusable(false);
                    titleRepaired.setVisibility(View.VISIBLE);
                    titleCharge.setVisibility(View.VISIBLE);
                    titlereason.setVisibility(View.GONE);
                    tvReason.setVisibility(View.GONE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.GONE);
                    btnReturnService.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    disable=true;

                    getBookings();


                }

                else if(hide.equals("Request Updated")){
                    btnCancelBooking.setVisibility(View.VISIBLE);
                    tvRepairType.setVisibility(View.GONE);
                    tvCharge.setVisibility(View.GONE);
                    tvCharge.setFocusable(false);
                    titleRepaired.setVisibility(View.GONE);
                    titleCharge.setVisibility(View.GONE);
                    titlereason.setVisibility(View.VISIBLE);
                    tvReason.setVisibility(View.VISIBLE);
                    mBtnAccept.setVisibility(View.VISIBLE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.GONE);
                    btnReturnService.setVisibility(View.GONE);

                    disable=false;
                    getBookings();
                }
                else if(hide.equals("Done Service")){
                    btnCancelBooking.setVisibility(View.GONE);
                    tvRepairType.setVisibility(View.VISIBLE);
                    tvCharge.setVisibility(View.VISIBLE);
                    tvCharge.setFocusable(false);
                    titleRepaired.setVisibility(View.VISIBLE);
                    titleCharge.setVisibility(View.VISIBLE);
                    titlereason.setVisibility(View.VISIBLE);
                    tvReason.setVisibility(View.VISIBLE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.GONE);
                    btnReturnService.setVisibility(View.GONE);

                    disable=true;
                    getBookings();
                }

                else if(hide.equals("Request Pending")){
                    btnCancelBooking.setVisibility(View.GONE);
                    tvRepairType.setVisibility(View.GONE);
                    tvCharge.setVisibility(View.GONE);
                    tvCharge.setFocusable(false);
                    titleRepaired.setVisibility(View.GONE);
                    titleCharge.setVisibility(View.GONE);
                    tvReason.setVisibility(View.GONE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.GONE);
                    btnReturnService.setVisibility(View.GONE);
                    disable=false;
                    getBookings();
                }

                else if(hide.equals("Booking Updated Cancel by Customer")){
                    btnCancelBooking.setVisibility(View.GONE);
                    tvRepairType.setVisibility(View.GONE);
                    tvCharge.setVisibility(View.GONE);
                    tvCharge.setFocusable(false);
                    titleRepaired.setVisibility(View.GONE);
                    titleCharge.setVisibility(View.GONE);
                    titlereason.setVisibility(View.VISIBLE);
                    tvReason.setVisibility(View.VISIBLE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.GONE);
                    btnReturnService.setVisibility(View.GONE);
                    disable=true;
                    getBookings();
                }

                else{
                    btnCancelBooking.setVisibility(View.GONE);
                    tvRepairType.setVisibility(View.GONE);
                    tvCharge.setVisibility(View.GONE);
                    tvCharge.setFocusable(false);
                    titleRepaired.setVisibility(View.GONE);
                    titleCharge.setVisibility(View.GONE);
                    titlereason.setVisibility(View.GONE);
                    tvReason.setVisibility(View.GONE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.GONE);
                    btnReturnService.setVisibility(View.GONE);
                    disable=true;
                    getBookings();

                }





                Toast.makeText(getApplication(),hide,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void hideButton(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Bookings").child(CustId).child(value).child("Status");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
         String hide=dataSnapshot.getValue(String.class);


                if (hide.equals("Request Accepted")) {
                    tvRepairType.setVisibility(View.VISIBLE);
                    tvCharge.setVisibility(View.VISIBLE);
                    btnCancelBooking.setVisibility(View.GONE);
                    titleRepaired.setVisibility(View.VISIBLE);
                    titleCharge.setVisibility(View.VISIBLE);
                    tvReason.setVisibility(View.GONE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.VISIBLE);
                    btnContact.setVisibility(View.GONE);
                    btnReturnService.setVisibility(View.GONE);
                    disable=true;
                }


                else if(hide.equals("Request Updated")){
                    btnCancelBooking.setVisibility(View.GONE);
                    tvRepairType.setVisibility(View.GONE);
                    tvCharge.setVisibility(View.GONE);
                    tvCharge.setFocusable(false);
                    titleRepaired.setVisibility(View.GONE);
                    titleCharge.setVisibility(View.GONE);
                    titlereason.setVisibility(View.VISIBLE);
                    tvReason.setVisibility(View.VISIBLE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.GONE);
                    btnReturnService.setVisibility(View.GONE);
                    disable=false;
                    getBookings();
                }

                else if(hide.equals("Done Service")){
                    btnCancelBooking.setVisibility(View.GONE);
                    tvRepairType.setVisibility(View.VISIBLE);
                    tvCharge.setVisibility(View.VISIBLE);
                    tvCharge.setFocusable(false);
                    titleRepaired.setVisibility(View.VISIBLE);
                    titleCharge.setVisibility(View.VISIBLE);
                    titlereason.setVisibility(View.VISIBLE);
                    tvReason.setVisibility(View.VISIBLE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.VISIBLE);
                    btnReturnService.setVisibility(View.VISIBLE);

                    disable=true;
                    getBookings();
                }

                else if(hide.equals("Request Pending")){
                    btnCancelBooking.setVisibility(View.GONE);
                    tvRepairType.setVisibility(View.GONE);
                    tvCharge.setVisibility(View.GONE);
                    tvCharge.setFocusable(false);
                    titleRepaired.setVisibility(View.GONE);
                    titleCharge.setVisibility(View.GONE);
                    titlereason.setVisibility(View.GONE);
                    tvReason.setVisibility(View.GONE);
                    mBtnAccept.setVisibility(View.VISIBLE);
                    mBtnReject.setVisibility(View.VISIBLE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.GONE);
                    btnReturnService.setVisibility(View.GONE);
                    disable=false;
                    getBookings();
                }

                else if(hide.equals("Booking Updated Cancel by Customer")){
                    btnCancelBooking.setVisibility(View.GONE);
                    tvRepairType.setVisibility(View.GONE);
                    tvCharge.setVisibility(View.GONE);
                    tvCharge.setFocusable(false);
                    titleRepaired.setVisibility(View.GONE);
                    titleCharge.setVisibility(View.GONE);
                    titlereason.setVisibility(View.VISIBLE);
                    tvReason.setVisibility(View.VISIBLE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.GONE);
                    btnReturnService.setVisibility(View.GONE);
                    disable=true;
                    getBookings();
                }

                else{
                    btnCancelBooking.setVisibility(View.GONE);
                    tvRepairType.setVisibility(View.GONE);
                    tvCharge.setVisibility(View.GONE);
                    tvCharge.setFocusable(false);
                    titleRepaired.setVisibility(View.GONE);
                    titleCharge.setVisibility(View.GONE);
                    titlereason.setVisibility(View.GONE);
                    tvReason.setVisibility(View.GONE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.GONE);
                    btnReturnService.setVisibility(View.GONE);
                    disable=true;
                    getBookings();

                }



                Toast.makeText(getApplication(),hide,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }






    public void acceptRequest() {

        if(accesslevel.equals("ADMIN")) {
            Toast.makeText(getApplicationContext(), "acceptRequest Triggered", Toast.LENGTH_LONG).show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            myRef = database.getReference().child("Bookings").child(CustId).child(value).child("Status");
            myRef.setValue("Request Accepted");
            getBookings();
            disable = true;
            if (accesslevel.equals("USER")) {
                myRef = database.getReference().child("Bookings").child(userid).child(value);
            } else {
                myRef = database.getReference().child("Bookings").child(CustId).child(value);
            }

        }

        else if(accesslevel.equals("USER")){
            Toast.makeText(getApplicationContext(), "acceptRequest Triggered", Toast.LENGTH_LONG).show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            myRef = database.getReference().child("Bookings").child(userid).child(value).child("Status");
            myRef.setValue("Request Accepted");
            getBookings();
            disable = true;
            if (accesslevel.equals("USER")) {
                myRef = database.getReference().child("Bookings").child(userid).child(value);
            } else {
                myRef = database.getReference().child("Bookings").child(CustId).child(value);
            }
        }
    }



@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (accesslevel.equals("USER")) {
            super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.displaybookingsmenu, menu);


        } else {




        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(accesslevel.equals("USER")){

            Intent i = new Intent(displayBooking.this, bookingsList.class);
            i.putExtra("ACCESSLEVEL", accesslevel);
            startActivity(i);
            finish();
        }



        else {

            Intent i = new Intent(displayBooking.this, bookingsList.class);
            i.putExtra("ACCESSLEVEL", accesslevel);
            startActivity(i);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(displayBooking.this, bookingsList.class);
                startActivity(i);
                finish();
                break;

            case R.id.cancle:


                if (disable==true) {

                    Toast.makeText(getApplicationContext(),"You are cannot cancel this booking",Toast.LENGTH_LONG).show();

                    break;
                } else {

                    delete();

                break;
                }



        }




        return true;
    }


    public void delete() {
        AlertDialog alertDialog = new AlertDialog.Builder(displayBooking.this).create();
        alertDialog.setTitle("Cancel Booking");
        alertDialog.setMessage("Are you sure you want to cancel this booking?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "cancelRequest Triggered", Toast.LENGTH_LONG).show();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        myRef = database.getReference().child("Bookings").child(userid).child(value).child("Status");
                        myRef.setValue("Booking Cancel");
                        getBookings();
                        disable = false;
                        if (accesslevel.equals("USER")) {
                            myRef = database.getReference().child("Bookings").child(userid).child(value);
                        } else {
                            myRef = database.getReference().child("Bookings").child(CustId).child(value);
                        }
                            deleteResponse();
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

        AlertDialog alertDialog = new AlertDialog.Builder(displayBooking.this).create();
        alertDialog.setMessage("Booking Canceled");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent i = new Intent(displayBooking.this, bookingsList.class);
                        i.putExtra("ACCESSLEVEL", accesslevel);
                        startActivity(i);
                        finish();
                    }
                });
        alertDialog.show();


    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(displayBooking.this, MainActivity.class);
            startActivity(i);
        }

        userid = currentUser.getUid();

            getBookings();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(accesslevel.equals("ADMIN")){
            hideButton();
            if (accesslevel.equals("ADMIN")) {

                myRef = database.getReference().child("AdminNoti");
                myRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        triggerNoti();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        triggerNoti();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        triggerNoti();
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
        else{

            mBtnAccept.setVisibility(View.GONE);
            mBtnReject.setVisibility(View.GONE);
            cancelBookingHide();


        }

    }



    public void getBookings() {

        if(accesslevel.equals("USER")) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();


            if (accesslevel.equals("USER")) {
                myRef = database.getReference().child("Bookings").child(userid).child(value);
            } else {


                        myRef = database.getReference().child("Bookings").child(CustId).child(value);


            }

            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Services service = dataSnapshot.getValue(Services.class);
                    tvDate.setText(service.Date);
                    tvAdd.setText(service.Address);
                    tvBrand.setText(service.Brand);
                    tvModel.setText(service.Model);
                    tvPhone.setText(service.PhoneNo);
                    tvPick.setText(service.PickupTime);
                    tvService.setText(service.Service);
                    tvReason.setText(service.Reason);

                    tvStatus.setText(service.Status);
                    //   Toast.makeText(getApplicationContext(), service.Status.get(0),Toast.LENGTH_LONG).show();


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 1000);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("bookingsList", "Failed to read value.", error.toException());
                }


            });
        }

        else{


            final FirebaseDatabase database = FirebaseDatabase.getInstance();


            if (accesslevel.equals("USER")) {
                myRef = database.getReference().child("Bookings").child(userid).child(value);
            } else {

                        myRef = database.getReference().child("Bookings").child(CustId).child(value);
            }

            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Services service = dataSnapshot.getValue(Services.class);
                    tvDate.setText(service.Date);
                    tvAdd.setText(service.Address);
                    tvBrand.setText(service.Brand);
                    tvModel.setText(service.Model);
                    tvPhone.setText(service.PhoneNo);
                    tvPick.setText(service.PickupTime);
                    tvService.setText(service.Service);
                    tvReason.setText(service.Reason);
                    tvStatus.setText(service.Status);
                    //   Toast.makeText(getApplicationContext(), service.Status.get(0),Toast.LENGTH_LONG).show();


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 1000);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("bookingsList", "Failed to read value.", error.toException());
                }


            });
        }
    }


    @IgnoreExtraProperties
    public static class Services {
        public String Date;
        public String Address;
        public String Model;
        public String PhoneNo;
        public String PickupTime;
        public String Service;
        public String Brand;
        public String Reason;
        public String Status;

        public boolean isAccepted;
        public boolean isUpdated;

        public Services() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Services(String status,String address, String model, String phoneNo, String pickupTime, String service, String brand, String date, String reason, boolean isAccepted, boolean isUpdated) {

            this.Status = status;
            this.Date = date;
            this.Address = address;
            this.Model = model;
            this.PhoneNo = phoneNo;
            this.PickupTime = pickupTime;
            this.Service = service;
            this.Brand = brand;
            this.Reason = reason;
            this.isAccepted = isAccepted;
            this.isUpdated = isUpdated;

        }
    }


}
