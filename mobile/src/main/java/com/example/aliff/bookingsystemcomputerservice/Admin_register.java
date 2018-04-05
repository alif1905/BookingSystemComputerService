package com.example.aliff.bookingsystemcomputerservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class Admin_register extends AppCompatActivity {
    private TextView tEmail,tPassword,tConfirmPassword;
    private EditText eEmail,ePassword,eConfirmPassword;
    private Button btnSignUp,btNBack;

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private String accesslevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Register");

        tEmail = findViewById(R.id.textViewEmailAdmin);
        tPassword = findViewById(R.id.textViewPasswordAdmin);
        tConfirmPassword = findViewById(R.id.textViewConfirmPasswordAdmin);
        btNBack = findViewById(R.id.btnBackRegisterAdmin);

        eEmail = findViewById(R.id.textEditorEmailAdmin);
        ePassword = findViewById(R.id.textEditorPasswordAdmin);
        eConfirmPassword = findViewById(R.id.textEditorConfirmPasswordAdmin);

        btnSignUp = findViewById(R.id.ButtonSignUpAdmin);

        btNBack.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(Admin_register.this, "Back to Main Menu...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Admin_register.this, Admin_Main_menu.class);
                startActivity(intent);
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String id = currentUser.getUid();
        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("Register").child(id);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Customer_Main_menu.Auth auth = dataSnapshot.getValue(Customer_Main_menu.Auth.class);
                accesslevel = auth.accessLevel;
                if (!accesslevel.equals("Admin")) {
                    Intent i = new Intent(Admin_register.this, MainActivity.class);
                    startActivity(i);
                    finish();
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




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Admin_register.this, Admin_Main_menu.class);
        i.putExtra("ACCESSLEVEL", accesslevel);
        startActivity(i);
        finish();
    }

    private void startRegister(){
        final String textEditorEmail = eEmail.getText().toString().trim();

        if(TextUtils.isEmpty(textEditorEmail)){
            Toast.makeText(Admin_register.this, "Email is required", Toast.LENGTH_SHORT).show();
            return;
        }

//
        final String textEditorPassword = ePassword.getText().toString().trim();
        if (textEditorPassword.length() < 6) {
            Toast.makeText(Admin_register.this, "Your password must have at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }


        final String textEditorConfirmPassword = eConfirmPassword.getText().toString().trim();
        if (!textEditorPassword.equals(textEditorConfirmPassword)) {
            Toast.makeText(Admin_register.this, "Password Not Matched", Toast.LENGTH_SHORT).show();
            return;
        }

        if((!TextUtils.isEmpty(textEditorEmail)&& !TextUtils.isEmpty(textEditorPassword)&& !TextUtils.isEmpty(textEditorConfirmPassword))){

            final ProgressDialog progressDialog = ProgressDialog.show(Admin_register.this, "Please wait...", "Processing...",true);

            mAuth.createUserWithEmailAndPassword(textEditorEmail,textEditorPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if (!task.isSuccessful())
                    {
                        progressDialog.dismiss();

                        try
                        {
                            throw task.getException();
                        }
                        // if user enters wrong email.
                        catch (FirebaseAuthUserCollisionException invalidEmail)
                        {

                            Toast.makeText(Admin_register.this, "Email are existed", Toast.LENGTH_SHORT).show();


                            // TODO: take your actions!
                        }





                        catch (Exception e)
                        {

                        }



                    }

                    else{

                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabaseRef.child(user_id);
                        current_user_db.child("Email").setValue(textEditorEmail);
                        current_user_db.child("accessLevel").setValue("Customer");


                        Toast.makeText(Admin_register.this, "Successfull Registered", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Admin_register.this, Admin_login.class);
                        startActivity(intent);
                        finish();
                    }
                }



            });
        }

    }


}
