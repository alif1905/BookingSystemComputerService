package com.example.aliff.bookingsystemcomputerservice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.io.Serializable;
import java.util.ArrayList;

public class displayBooking extends AppCompatActivity implements View.OnClickListener {

    private String accesslevel;
    private String CustId,name,add,brand,model,phone,service,charge,repaired,reason,date;
    private String userid, value;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private TextView tvStatus,tvAdd, tvBrand, tvModel, tvPhone, tvPick, tvService, tvDate,tvReason;
    private TextView tvCharge;
    private ListView tvRepairType;
    private TextView titlereason,titleRepaired,titleCharge;

    private ProgressDialog progressDialog;
    private Button mBtnReject;
    private Button mBtnDoneCollect;
    private Button mBtnAccept;
    private Button mDoneService;
    private Button btnCancelBooking;
    private Button btnReturnService;
    private Button btnContact;
    private Button btnDiagnose;
    private Button btnSubmitDiagnose;
    private Menu menu;
    private boolean disable = false;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;
    private Context context;

    private boolean openNoti=false;
    private String chargeRMDb;
    private String repairingDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_booking);
        Intent intent = getIntent();



        //Notification
//        context = this;
//        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        builder = new NotificationCompat.Builder(this);
//
//        remoteViews = new RemoteViews(getPackageName(),R.layout.custom_notification);
//        remoteViews.setImageViewResource(R.id.notif_icon,R.drawable.fastplay2);
//        remoteViews.setTextViewText(R.id.notif_title,"You have receive Notification");
//        remoteViews.setProgressBar(R.id.progressBar,100,40,true);


        name = intent.getStringExtra("name");
        add = intent.getStringExtra("address");
        charge = intent.getStringExtra("charge");
        brand = intent.getStringExtra("brand");
        model = intent.getStringExtra("model");
        phone = intent.getStringExtra("phone");
        service = intent.getStringExtra("service");
        reason = intent.getStringExtra("reason");
        repaired = intent.getStringExtra("repairedtype");
        date = intent.getStringExtra("date");



        CustId = intent.getStringExtra("CustID");
        value = intent.getStringExtra("Value");
        accesslevel = intent.getStringExtra("ACCESSLEVEL");

        mBtnDoneCollect=(Button) findViewById(R.id.btnDoneCollectItem);
        btnReturnService = (Button) findViewById(R.id.btnReturnService);
        btnContact = (Button) findViewById(R.id.btnContact);
        mDoneService = (Button) findViewById(R.id.btnDoneService);
        mBtnAccept = (Button) findViewById(R.id.btnAccept);
        mBtnReject = (Button) findViewById(R.id.btnReject);
        btnDiagnose = (Button) findViewById(R.id.btnDiagnose);
        btnCancelBooking = (Button) findViewById(R.id.btnCancelBooking);
        btnSubmitDiagnose= (Button) findViewById(R.id.btnSubmitDiagnoseRequest);

        tvDate = (TextView) findViewById(R.id.tvDate);


        titleCharge = (TextView) findViewById(R.id.CostCharge);
        titlereason = (TextView) findViewById(R.id.giveReason);
        titleRepaired = (TextView) findViewById(R.id.RepairedPart);


        tvCharge = (TextView) findViewById(R.id.tvCostCharge);
        tvRepairType = (ListView) findViewById(R.id.tvRepairedType);
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
        mBtnDoneCollect.setOnClickListener(this);
        btnDiagnose.setOnClickListener(this);
        btnSubmitDiagnose.setOnClickListener(this);

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
            mBtnDoneCollect.setVisibility(View.GONE);
            btnDiagnose.setVisibility(View.GONE);
            btnSubmitDiagnose.setVisibility(View.GONE);

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
            mBtnDoneCollect.setVisibility(View.GONE);
            btnDiagnose.setVisibility(View.GONE);
            btnSubmitDiagnose.setVisibility(View.GONE);

        }
    }

        @Override
        public void onClick (View view){


            switch (view.getId()) {
                case R.id.btnSubmitDiagnoseRequest:
                    AlertDialog alertDialog8 = new AlertDialog.Builder(displayBooking.this).create();
                    alertDialog8.setTitle("Done Collect Item");
                    alertDialog8.setMessage("Are you done collect item this booking?");
                    alertDialog8.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int which) {
                                    Intent i = new Intent(displayBooking.this, displayBooking.class);

                                    //   doneCollect();

                                }
                            });
                    alertDialog8.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            });
                    alertDialog8.show();

                    break;


                case R.id.btnDiagnose:
//                    AlertDialog alertDialog7 = new AlertDialog.Builder(displayBooking.this).create();
//                    alertDialog7.setTitle("Diagnose Request");
//                    alertDialog7.setMessage("Are sure to Submit this Confirmation service to Customer?");
//                    alertDialog7.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(final DialogInterface dialog, int which) {
                    Intent j = new Intent(displayBooking.this, diagnoseAdapter.class);
                    j.putExtra("ACCESSLEVEL", accesslevel);
                    j.putExtra("custid", CustId);
                    j.putExtra("value", value);


                    startActivity(j);

                    finish();
                                 //   doneCollect();
//
//                                }
//                            });
//                    alertDialog7.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    dialog.dismiss();
//
//                                }
//                            });
             //       alertDialog7.show();

                    break;

                case R.id.btnDoneCollectItem:
                    AlertDialog alertDialog5 = new AlertDialog.Builder(displayBooking.this).create();
                    alertDialog5.setTitle("Done Collect Item");
                    alertDialog5.setMessage("Are you done collect item this booking?");
                    alertDialog5.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int which) {
                                    Intent i = new Intent(displayBooking.this, displayBooking.class);

                                    doneCollect();

                                }
                            });
                    alertDialog5.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            });
                    alertDialog5.show();

                    break;
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

                    AlertDialog alertDialog3 = new AlertDialog.Builder(displayBooking.this).create();
                    alertDialog3.setTitle("Return Service");
                    alertDialog3.setMessage("Are you sure to return service?");
                    alertDialog3.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int which) {
                                    Intent i = new Intent(displayBooking.this, displayBooking.class);

                         returnService();
                                }
                            });
                    alertDialog3.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            });
                    alertDialog3.show();

                    break;

                case R.id.btnContact:

                    AlertDialog alertDialog6 = new AlertDialog.Builder(displayBooking.this).create();
                    alertDialog6.setTitle("Contact Customer");
                    alertDialog6.setMessage("Are you sure to Contact Customer?");
                    alertDialog6.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int which) {



                                    String toNumber ="+6"+ tvPhone.getText().toString(); // contains spaces.
                                    toNumber = toNumber.replace("+", "").replace(" ", "");

                                    Uri uri = Uri.parse("https://api.whatsapp.com/send?phone="+toNumber+"&text=Hello%20");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);

                                }
                            });
                    alertDialog6.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            });
                    alertDialog6.show();


                    break;
            }

        }


        public void returnService(){
            Toast.makeText(getApplicationContext(), "Return Service Triggered", Toast.LENGTH_LONG).show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            myRef = database.getReference().child("Bookings").child(CustId).child(value).child("Status");
            myRef.setValue("Return Service");

            myRef = database.getReference().child("CustNoti").child(CustId).child(value).child("Status");

            myRef.setValue("Return Service");

            myRef = database.getReference().child("AdminNoti").child(CustId).child(value).child("Status");

            myRef.setValue("Return Service");



        }

        /**
         * NOTE:
         * Message is shared with only one user at a time. and to navigate back to main application user need to click back button
         */



//    public void triggerNoti(){
//
//        if (notificationManager.getActiveNotifications().length<=0) {
//            notification_id = (int) System.currentTimeMillis();
//
//            Intent button_intent = new Intent(this,displayBooking.class);
//            button_intent.putExtra("id",notification_id);
//            PendingIntent button_pending_event = PendingIntent.getBroadcast(context,notification_id,
//                    button_intent,0);
//
////            remoteViews.setOnClickPendingIntent(R.id.buttonShowNotification,button_pending_event);
//
//            Intent notification_intent = new Intent(context,MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notification_intent,0);
//
//            builder.setSmallIcon(R.mipmap.ic_launcher)
//                    .setAutoCancel(true)
//                    .setCustomBigContentView(remoteViews)
//                    .setContentIntent(pendingIntent);
//
//            notificationManager.notify(notification_id,builder.build());
//
//        }
//
//
//
//    }


    public void doneService() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();


//        repairingDb=tvRepairType .getText().toString();
//        if (TextUtils.isEmpty(repairingDb)) {
//            Toast.makeText(displayBooking.this, "Please State Repairing!", Toast.LENGTH_SHORT).show();
//            return;
//        }





        myRef = database.getReference().child("Bookings").child(CustId).child(value).child("Repairing");

        myRef.setValue(repairingDb);




        chargeRMDb=tvCharge .getText().toString();
        if (TextUtils.isEmpty(chargeRMDb)) {
            Toast.makeText(displayBooking.this, "Please State Charge Service!", Toast.LENGTH_SHORT).show();
            return;
        }





        myRef = database.getReference().child("Bookings").child(CustId).child(value).child("ChargeRM");

        myRef.setValue(chargeRMDb);


        Toast.makeText(getApplicationContext(), "Done Service Triggered", Toast.LENGTH_LONG).show();
        myRef = database.getReference().child("Bookings").child(CustId).child(value).child("Status");
        myRef.setValue("Done Service");

        myRef = database.getReference().child("CustNoti").child(CustId).child(value).child("Status");

        myRef.setValue("Done Service");

        myRef = database.getReference().child("AdminNoti").child(CustId).child(value).child("Status");

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

        myRef = database.getReference().child("CustNoti").child(userid).child(value).child("Status");

        myRef.setValue("Booking Updated Cancel by Customer");


        myRef = database.getReference().child("AdminNoti").child(CustId).child(value).child("Status");

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


                else  if (hide.equals("Staff Diagnosing Your PC")) {
                    tvRepairType.setVisibility(View.GONE);
                    tvCharge.setVisibility(View.GONE);
                    btnCancelBooking.setVisibility(View.GONE);
                    titleRepaired.setVisibility(View.GONE);
                    titleCharge.setVisibility(View.GONE);
                    tvReason.setVisibility(View.GONE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.GONE);
                    btnReturnService.setVisibility(View.GONE);
                    mBtnDoneCollect.setVisibility(View.GONE);
                    btnDiagnose.setVisibility(View.GONE);
                    btnSubmitDiagnose.setVisibility(View.GONE);
                    disable=true;
                }

                else if(hide.equals("Return Service")){
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

                    titleRepaired.setVisibility(View.VISIBLE);
                    titleCharge.setVisibility(View.VISIBLE);
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
        if (accesslevel.equals("USER")||accesslevel.equals("ADMIN")) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            myRef = database.getReference().child("Bookings").child(CustId).child(value).child("Status");
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
         String hide=dataSnapshot.getValue(String.class);


                if (hide.equals("Request Accepted")) {
                    tvRepairType.setVisibility(View.GONE);
                    tvCharge.setVisibility(View.GONE);
                    btnCancelBooking.setVisibility(View.GONE);
                    titleRepaired.setVisibility(View.GONE);
                    titleCharge.setVisibility(View.GONE);
                    tvReason.setVisibility(View.GONE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.VISIBLE);
                    btnReturnService.setVisibility(View.GONE);
                    mBtnDoneCollect.setVisibility(View.VISIBLE);
                    disable=true;
                }

                else  if (hide.equals("Staff Diagnosing Your PC")) {
                    tvRepairType.setVisibility(View.VISIBLE);
                    tvCharge.setVisibility(View.VISIBLE);
                    btnCancelBooking.setVisibility(View.GONE);
                    titleRepaired.setVisibility(View.VISIBLE);
                    titleCharge.setVisibility(View.VISIBLE);
                    tvReason.setVisibility(View.GONE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.GONE);
                    btnReturnService.setVisibility(View.GONE);
                    mBtnDoneCollect.setVisibility(View.GONE);
                    btnDiagnose.setVisibility(View.VISIBLE);
                    btnSubmitDiagnose.setVisibility(View.VISIBLE);
                    disable=true;
                }

              else  if (hide.equals("Service In Progress")) {
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
                    mBtnDoneCollect.setVisibility(View.GONE);
                    disable=true;
                }

                else if(hide.equals("Return Service")){
                    btnCancelBooking.setVisibility(View.GONE);
                    tvRepairType.setVisibility(View.VISIBLE);

                    tvCharge.setVisibility(View.VISIBLE);

                    titleRepaired.setVisibility(View.VISIBLE);
                    titleCharge.setVisibility(View.VISIBLE);
                    titlereason.setVisibility(View.GONE);
                    tvReason.setVisibility(View.GONE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.VISIBLE);
                    btnContact.setVisibility(View.GONE);
                    btnReturnService.setVisibility(View.GONE);
                    mBtnDoneCollect.setVisibility(View.GONE);
                    disable=true;
                    getBookings();
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
                    mBtnDoneCollect.setVisibility(View.GONE);
                    disable=false;
                    getBookings();
                }

                else if(hide.equals("Done Service")){
                    btnCancelBooking.setVisibility(View.GONE);
                    tvRepairType.setVisibility(View.VISIBLE);
                    tvRepairType.setFocusable(false);
                    tvCharge.setVisibility(View.VISIBLE);
                    tvCharge.setFocusable(false);
                    titleRepaired.setVisibility(View.VISIBLE);
                    titleCharge.setVisibility(View.VISIBLE);
                    titlereason.setVisibility(View.GONE);
                    tvReason.setVisibility(View.GONE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.VISIBLE);
                    btnReturnService.setVisibility(View.VISIBLE);
                    mBtnDoneCollect.setVisibility(View.GONE);
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
                    mBtnDoneCollect.setVisibility(View.GONE);
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
                    titlereason.setVisibility(View.GONE);
                    tvReason.setVisibility(View.GONE);
                    mBtnAccept.setVisibility(View.GONE);
                    mBtnReject.setVisibility(View.GONE);
                    mDoneService.setVisibility(View.GONE);
                    btnContact.setVisibility(View.GONE);
                    btnReturnService.setVisibility(View.GONE);
                    mBtnDoneCollect.setVisibility(View.GONE);
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
                    mBtnDoneCollect.setVisibility(View.GONE);
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




public void doneCollect(){
    if(accesslevel.equals("ADMIN")) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();




        Toast.makeText(getApplicationContext(), "Done Collect Item Triggered", Toast.LENGTH_LONG).show();

        myRef = database.getReference().child("Bookings").child(CustId).child(value).child("Status");
        myRef.setValue("Staff Diagnosing Your PC");



        myRef = database.getReference().child("CustNoti").child(CustId).child(value).child("Status");

        myRef.setValue("Staff Diagnosing Your PC");


        myRef = database.getReference().child("AdminNoti").child(CustId).child(value).child("Status");

        myRef.setValue("Staff Diagnosing Your PC");

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
        myRef.setValue("Staff Diagnosing Your PC");

        myRef = database.getReference().child("CustNoti").child(userid).child(value).child("Status");

        myRef.setValue("Staff Diagnosing Your PC");


        myRef = database.getReference().child("AdminNoti").child(userid).child(value).child("Status");

        myRef.setValue("Staff Diagnosing Your PC");


        getBookings();
        disable = true;
        if (accesslevel.equals("USER")) {
            myRef = database.getReference().child("Bookings").child(userid).child(value);
        } else {
            myRef = database.getReference().child("Bookings").child(CustId).child(value);
        }
    }
}

    public void acceptRequest() {

        if(accesslevel.equals("ADMIN")) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();


            Toast.makeText(getApplicationContext(), "acceptRequest Triggered", Toast.LENGTH_LONG).show();

            myRef = database.getReference().child("Bookings").child(CustId).child(value).child("Status");
            myRef.setValue("Request Accepted");



            myRef = database.getReference().child("CustNoti").child(CustId).child(value).child("Status");

            myRef.setValue("Request Accepted");


            myRef = database.getReference().child("AdminNoti").child(CustId).child(value).child("Status");

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

            myRef = database.getReference().child("CustNoti").child(userid).child(value).child("Status");

            myRef.setValue("Request Accepted");


            myRef = database.getReference().child("AdminNoti").child(userid).child(value).child("Status");

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



                        myRef = database.getReference().child("CustNoti").child(userid).child(value).child("Status");

                        myRef.setValue("Booking Cancel");


                        myRef = database.getReference().child("AdminNoti").child(userid).child(value).child("Status");

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
//            if (accesslevel.equals("ADMIN")) {
//
//                myRef = database.getReference().child("AdminNoti");
//                myRef.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        triggerNoti();
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                        triggerNoti();
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//                        triggerNoti();
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//
//
//            }
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
                //    tvRepairType.setText(service.Repairing);
                    tvCharge.setText(service.ChargeRM);
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
               //     tvRepairType.setText(service.Repairing);
                    tvCharge.setText(service.ChargeRM);
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
        public  String Repairing;

        public  String ChargeRM;
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

        public Services(String repairing,String chargeRM,String status,String address, String model, String phoneNo, String pickupTime, String service, String brand, String date, String reason, boolean isAccepted, boolean isUpdated) {
            this.Repairing=repairing;
            this.ChargeRM=chargeRM;
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
