package com.example.library;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateAccount extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userref;
    TextView tv1, tv2, tv3,tv4, tv5;
    ImageView iv;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        setSupportActionBar(toolbar);
        tv1=(TextView)findViewById(R.id.acc_name);
        tv2=(TextView)findViewById(R.id.acc_sapid);
        tv3=(TextView)findViewById(R.id.acc_stream);
        tv4=(TextView)findViewById(R.id.acc_course);
        tv5=(TextView)findViewById(R.id.acc_phno);
        iv=(ImageView)findViewById(R.id.photophoto);

//        databaseReference=FirebaseDatabase.getInstance().getReference("UserDetails").child(sapid);
if(firebaseUser!=null)
        userref=FirebaseDatabase.getInstance().getReference("UserDetails").child(firebaseUser.getUid());
else
    userref=FirebaseDatabase.getInstance().getReference("UserDetails").child(getIntent().getStringExtra("uid"));
        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tv1.setText(dataSnapshot.child("name").getValue().toString());
                tv2.setText(dataSnapshot.child("sapid").getValue().toString());
                tv3.setText(dataSnapshot.child("stream").getValue().toString());
                tv4.setText(dataSnapshot.child("course").getValue().toString());
                tv5.setText(dataSnapshot.child("phoneno").getValue().toString());

                if(firebaseUser!=null){
                    if(firebaseUser.getPhotoUrl()!=null)
                    {
                        assert firebaseUser != null;
                        Glide.with(getApplicationContext())
                                .load(firebaseUser.getPhotoUrl().toString()).into(iv);
                    }
                    FloatingActionButton fab = findViewById(R.id.fab);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getApplicationContext(),AddAccountDetails.class));

                        }
                    });
                }
                else
                {
                    FloatingActionButton fab = findViewById(R.id.fab);
                    fab.setVisibility(View.INVISIBLE);
                }

                //Glide.with(getApplicationContext()).load(firebaseUser.getPhotoUrl().toString()).into(iv);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

}
}



