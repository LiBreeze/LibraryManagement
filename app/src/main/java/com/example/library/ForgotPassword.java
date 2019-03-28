package com.example.library;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText passwordEmail;
    Button resetPassword;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        progressDialog= new ProgressDialog(this);
        passwordEmail=(EditText)findViewById(R.id.email);
        resetPassword=(Button)findViewById(R.id.btn_reset_password);
        btnBack=(Button)findViewById(R.id.btn_back);
        firebaseAuth=FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String useremail=passwordEmail.getText().toString().trim();
                if(useremail.isEmpty())
                {
                    Toast.makeText(ForgotPassword.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.setMessage("Sending email for resetting password...");
                    progressDialog.show();
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ForgotPassword.this, "Password reset email has been sent to registered email id", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(ForgotPassword.this, "Password reset email has been sent to registered email id", Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                    });
                }

            }
        });
    }
}
