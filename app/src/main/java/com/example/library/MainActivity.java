
package com.example.library;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonLogin;
    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewSignup, textForgotPassword;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    DatabaseReference adminLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();


        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textForgotPassword = (TextView) findViewById(R.id.forgotPassword);
        textViewSignup = (TextView) findViewById(R.id.textViewSignup);
        progressDialog = new ProgressDialog(this);
        buttonLogin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
        textForgotPassword.setOnClickListener(this);

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == textViewSignup) {
            Intent i = new Intent(getApplicationContext(), Register.class);
            startActivity(i);
        }
        if (v == buttonLogin) {
            loginUser();
        }

        if (v == textForgotPassword) {
            Intent i2 = new Intent(getApplicationContext(), ForgotPassword.class);
            startActivity(i2);
        }

    }

    private void loginUser() {

        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();


        if (TextUtils.isEmpty(email)) {            //Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            editTextEmail.setError("Email required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password required");
            //Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logging in...");
        progressDialog.show();

//         adminLogin = FirebaseDatabase.getInstance().getReference("Admin Login");
//        adminLogin.addListenerForSingleValueEvent(new ValueEventListener() {
//
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child(email).exists()) {
//                    String pass = dataSnapshot.child(email).child("Password").getValue().toString();
//                    Toast.makeText(MainActivity.this, "Works", Toast.LENGTH_SHORT).show();
//
//                    if (pass.equals(password)) {
//                        Toast.makeText(MainActivity.this, "Admin Login", Toast.LENGTH_SHORT).show();
//                    } else
//                        Toast.makeText(MainActivity.this, "Incorrect password for admin", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                } else {
//                    UserLogin(email,password);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });

        if(email.equals("iamadmin")&&password.equals("adminpass@123")) {
            Toast.makeText(this, "Admin", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            startActivity(new Intent(getApplicationContext(),AdminHome.class));
//            startActivity(new Intent(getApplicationContext(),UserBook.class));
        }

        else
            UserLogin(email,password);
    }

    public void UserLogin(String email,String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {

                            finish();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                        } else
                            Toast.makeText(MainActivity.this, "Login unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
