package com.example.aliff.bookingsystemcomputerservice;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.itextpdf.text.PageSize.A4;

public class CustomerInvoice extends AppCompatActivity implements View.OnClickListener {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    private String accesslevel;
    private String CustId;
    String userid;
    private String value;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;


    private ArrayList<InvoiceMode> dataModels;
    private ListView listView;
    private Invoice adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_invoice);

        listView = (ListView) findViewById(R.id.listViewCustInvoice);

        dataModels = new ArrayList<>();
        getInformation();

        adapter = new Invoice(dataModels, this);
        listView.setAdapter(adapter);

    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(CustomerInvoice.this, Manage_Financial.class);
        startActivity(i);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(CustomerInvoice.this, Manage_Financial.class);
            startActivity(i);
        }

    }

    private void getInformation() {
        //dataModels.clear();


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        myRef = database.getReference().child("Bookings");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {

                    String key = uniqueKeySnapshot.getKey().toString();
                    for (DataSnapshot RootSnapshot : uniqueKeySnapshot.getChildren()) {

                        InvoiceService invoice = RootSnapshot.getValue(InvoiceService.class);


                        dataModels.add(new InvoiceMode(
                                invoice.ChargeRM,
                                invoice.Name,
                                invoice.Status,
                                invoice.Address,
                                invoice.Model,
                                invoice.PhoneNo,
                                invoice.PickupTime,
                                invoice.Service,
                                invoice.Brand,
                                invoice.Date,
                                invoice.Reason,
                                true,
                                true,
                                key));
                        adapter.notifyDataSetChanged();


                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("bookingsList", "Failed to read value.", error.toException());
            }


        });
    }


    public void print(String A, String B, String C ) {
        String[] a = {A};
        String[] b = {B};
        String[] c = {C};


        try {
            sendData(a,b,c,1,"K");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void sendData(String[] a, String[] b, String[] c, int i, String k) throws FileNotFoundException {


        try {
            createPdfWrapper(a, b, c, i, k);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    public void createPdfWrapper(String[] a, String[] b, String[] c, int i, String k) throws FileNotFoundException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            createPdf(a, b, c, i, k);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void createPdf(String[] a, String[] b, String[] c, int i, String k) throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i("CREATEPDF", "Created a new directory for PDF");
        }

        pdfFile = new File(docsFolder.getAbsolutePath(), "A.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.setPageSize(A4);
        document.open();

        Paragraph d = new Paragraph("Date : " + "TODAY" + "  Bill no : ");
        d.setAlignment(Element.ALIGN_LEFT);
        document.add(d);
        document.add(Chunk.NEWLINE);


        Paragraph com_name = new Paragraph("Michael Electronic Services", FontFactory.getFont(FontFactory.TIMES_ROMAN, 24));
        com_name.setAlignment(Element.ALIGN_CENTER);
        document.add(com_name);

        Paragraph address = new Paragraph("190 GF Jalan Tun Ahmad Zaidi Parkcity Phase II, 97000 Bintulu");
        address.setAlignment(Element.ALIGN_CENTER);
        document.add(address);


        Paragraph phone = new Paragraph("Office : 086-331925 Handphone : 014-583-3303");
        phone.setAlignment(Element.ALIGN_CENTER);
        document.add(phone);


        document.add(Chunk.NEWLINE);


        PdfPTable table = new PdfPTable(4);

        table.setTotalWidth(new float[]{1, 2, 2, 2});
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.setLockedWidth(true);

        table.addCell("No");
        table.addCell("Product");
        table.addCell("Faulty");
        table.addCell("Price");
        table.completeRow();

        int l = 1;
        for (int j = 0; j < i; j++) {
            table.addCell(l++ + "");
            table.addCell(a[j].toString());
            table.addCell(b[j]);
            table.addCell("RM " + c[j].toString());
            table.completeRow();

        }
//        int m = 0;
//        double total = 0;
//        while (m < i) {
//
//            total = total + Double.parseDouble(c[m++]);
//        }


        PdfPCell cellOne = new PdfPCell();
        cellOne.setBorder(Rectangle.NO_BORDER);

        table.addCell(cellOne);
        table.addCell(cellOne);
        table.addCell("Total");
//        table.addCell("RM " + String.format("%.2f", total) + "");
        table.completeRow();

        document.add(table);

        document.close();
        previewPdf();

    }

    private void previewPdf() {

        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");

            startActivity(intent);
        } else {
            Toast.makeText(this, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
        }
    }


    @IgnoreExtraProperties
    public static class InvoiceService {
        public String Name;
        public String Date;
        public String Address;
        public String Model;
        public String PhoneNo;
        public String PickupTime;
        public String Service;
        public String Brand;
        public String Reason;
        public String Status;
        public String ChargeRM;
        public boolean isAccepted;
        public boolean isUpdated;

        public InvoiceService() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }


        public InvoiceService(String chargeRM, String name, String status, String address, String model, String phoneNo, String pickupTime, String service, String brand, String date, String reason, boolean isAccepted, boolean isUpdated) {
            this.ChargeRM = chargeRM;
            this.Name = name;
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
