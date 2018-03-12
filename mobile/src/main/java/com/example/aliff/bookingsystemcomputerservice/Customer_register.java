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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Customer_register extends AppCompatActivity {
    private TextView tEmail,tPassword,tConfirmPassword,tSignin;
    private EditText eEmail,ePassword,eConfirmPassword;
    private Button btnSignUp;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Register");

        tEmail = findViewById(R.id.textViewEmail);
        tPassword = findViewById(R.id.textViewPassword);
        tConfirmPassword = findViewById(R.id.textViewConfirmPassword);
        tSignin = findViewById(R.id.textViewSignIn);

        eEmail = findViewById(R.id.textEditorEmail);
        ePassword = findViewById(R.id.textEditorPassword);
        eConfirmPassword = findViewById(R.id.textEditorConfirmPassword);

        btnSignUp = findViewById(R.id.ButtonSignUp);

        tSignin.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(Customer_register.this, "Login as a customer", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Customer_register.this, Customer_LoginActivity.class);
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
    private void startRegister(){

        final String textEditorEmail = eEmail.getText().toString().trim();


        if(TextUtils.isEmpty(textEditorEmail)){
            Toast.makeText(Customer_register.this, "Email is required", Toast.LENGTH_SHORT).show();
            return;
        }
        final String textEditorPassword = ePassword.getText().toString().trim();
        if (textEditorPassword.length() < 6) {
            Toast.makeText(Customer_register.this, "Your password must have at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        final String textEditorConfirmPassword = eConfirmPassword.getText().toString().trim();
        if (!textEditorPassword.equals(textEditorConfirmPassword)) {
            Toast.makeText(Customer_register.this, "Password Not Matched", Toast.LENGTH_SHORT).show();
            return;
        }

        if((!TextUtils.isEmpty(textEditorEmail)&& !TextUtils.isEmpty(textEditorPassword)&& !TextUtils.isEmpty(textEditorConfirmPassword))){

            final ProgressDialog progressDialog = ProgressDialog.show(Customer_register.this, "Please wait...", "Processing...",true);

            mAuth.createUserWithEmailAndPassword(textEditorEmail,textEditorPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();

                    if(task.isSuccessful()) {

                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabaseRef.child(user_id);
                        current_user_db.child("Email").setValue(textEditorEmail);
                        current_user_db.child("accessLevel").setValue("Customer");

                        Toast.makeText(Customer_register.this, "Successfull Registered", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Customer_register.this, Customer_LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }else{
                        Log.e("Register error...", task.getException().toString());
                        Toast.makeText(Customer_register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                    }

                }

            });
        }

    }




}
