package com.example.library;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;


public class Register extends AppCompatActivity implements View.OnClickListener{
    Button buttonRegister;
    static String role="Student";
    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewSignin;
    EditText sapid;
    ProgressDialog progressDialog;
  FirebaseAuth firebaseAuth;
  String u,p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       FirebaseApp.initializeApp(this);
      firebaseAuth=FirebaseAuth.getInstance();

        sapid=(EditText)findViewById(R.id.SAPid);
        buttonRegister=(Button)findViewById(R.id.buttonRegister);
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        textViewSignin=(TextView)findViewById(R.id.textViewSignin);
        progressDialog= new ProgressDialog(this);
        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==textViewSignin)
        {
            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }

        if(v==buttonRegister)
        {
            registerUser();
        }
    }

    private void registerUser() {
        u=editTextEmail.getText().toString();

        p=editTextPassword.getText().toString();


        progressDialog.setMessage("Registering user...");
        final String email=editTextEmail.getText().toString().trim();
        final String password=editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        final Intent i=new Intent(getApplicationContext(), AddAccountDetails.class);
        i.putExtra("Email",editTextEmail.getText().toString());
        i.putExtra("Password",editTextPassword.getText().toString());
        i.putExtra("SAPid",sapid.getText().toString());
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    finish();
                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Registered successfully. Check your email id for verification email", Toast.LENGTH_SHORT).show();
//                                editTextEmail.setText("");
//                                editTextPassword.setText("");

//                                Intent i=new Intent(getApplicationContext(),AddAccountDetails.class);
//                                i.putExtra("Email",editTextEmail.getText().toString());
//                                i.putExtra("Password",editTextPassword.getText().toString());

                                if((sapid.getText().toString()).startsWith("700")) {
                                    role="Student";
                                    startActivity(i);

                                }
                                else {
                                    role = "Teacher";
                                    startActivity(i);
                                }
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

    }
}

