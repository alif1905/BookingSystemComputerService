package com.example.aliff.bookingsystemcomputerservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class Customer_LoginActivity extends AppCompatActivity {
    private EditText mEmail, mPassword;
    private Button mLogin,mBackCust;
    private TextView mRegistration;


    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__login);

        mAuth = FirebaseAuth.getInstance();

//        firebaseAuthListener= new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if(user!=null){
//                    Intent intent = new Intent(Customer_LoginActivity.this,Customer_Main_menu.class);
//                    startActivity(intent);
//                    Toast.makeText(Customer_LoginActivity.this,"Sign in successfull...", Toast.LENGTH_SHORT).show();
//                    finish();
//                    return;
//                }
//            }
//        };

        mEmail = findViewById(R.id.emailCust);
        mPassword = findViewById(R.id.PasswordCust);
        mLogin = findViewById(R.id.customerSignIn);
        mBackCust = findViewById(R.id.ButtonBackCustomer);
        mRegistration = findViewById(R.id.customerRegister);


        mBackCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Customer_LoginActivity.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(Customer_LoginActivity.this,"Main Menu", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailCust = mEmail.getText().toString();
                final String PasswordCust = mPassword.getText().toString();

                final ProgressDialog progressDialog = ProgressDialog.show(Customer_LoginActivity.this, "Please wait...", "Processing...",true);
                mAuth.signInWithEmailAndPassword(emailCust, PasswordCust).addOnCompleteListener(Customer_LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (!task.isSuccessful() || emailCust == null || PasswordCust == null) {
                            Toast.makeText(Customer_LoginActivity.this, "Sign in error...", Toast.LENGTH_SHORT).show();
                        }
                        else if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Sign in success...", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Customer_LoginActivity.this, Customer_Main_menu.class);
                            startActivity(i);
                        }

                    }
                });
            }
        });
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Customer_LoginActivity.this, "Create a account...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Customer_LoginActivity.this, Customer_register.class);
                startActivity(intent);


            }
        });
    }


}
