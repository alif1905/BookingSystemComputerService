package com.example.aliff.bookingsystemcomputerservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_login extends AppCompatActivity {

    private EditText mEmailAdmin,mPasswordAdmin;
    private Button mLoginAdmin,mBackAdmin;



    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mAuth= FirebaseAuth.getInstance();

        firebaseAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(Admin_login.this,Admin_Main_menu.class);
                    startActivity(intent);
                    Toast.makeText(Admin_login.this,"Sign in successfull...", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
        };

        mEmailAdmin  = findViewById(R.id.emailAdmin);
        mPasswordAdmin = findViewById(R.id.passwordAdmin);

        mLoginAdmin = findViewById(R.id.ButtonLogindAdmin);
        mBackAdmin = findViewById(R.id.ButtonBackAdmin);

        mBackAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_login.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(Admin_login.this,"Main Menu", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        });


        mLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailAdmin= mEmailAdmin.getText().toString();
                final String passwordAdmin= mPasswordAdmin.getText().toString();

                final ProgressDialog progressDialog = ProgressDialog.show(Admin_login.this, "Please wait...", "Processing...",true);
                mAuth.signInWithEmailAndPassword(emailAdmin,passwordAdmin).addOnCompleteListener(Admin_login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(!task.isSuccessful()||emailAdmin==null || passwordAdmin==null){
                            Toast.makeText(Admin_login.this,"Sign in error...", Toast.LENGTH_SHORT).show();

                        }
                        else if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Sign in success...", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Admin_login.this, Admin_Main_menu.class);
                            startActivity(i);
                        }

                    }
                });
            }
        });

    }
}
