package com.example.library;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.List;

public class RequestBooks extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_books);

        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.parseColor("#222222"));


        drawer = (DrawerLayout) findViewById(R.id.enter);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View navHeaderView = navigationView.getHeaderView(0);

        ImageView displayImageView = (ImageView) navHeaderView.findViewById(R.id.imageView_display);
        TextView nameTextView = (TextView) navHeaderView.findViewById(R.id.textView_name);
        TextView emailTextView = (TextView) navHeaderView.findViewById(R.id.textView_email);

        assert firebaseUser != null;
        if (firebaseUser.getPhotoUrl() != null) {

            Glide.with(this)
                    .load(firebaseUser.getPhotoUrl().toString()).into(displayImageView);


        }

        nameTextView.setText(firebaseUser.getDisplayName());
        emailTextView.setText(firebaseUser.getEmail());


        final EditText tvbook, tvauth,tvsubject;
        tvbook= (EditText) findViewById(R.id.namebook);

        Button reqbook=(Button) findViewById(R.id.create);
        tvauth=(EditText) findViewById(R.id.bookauthor);

        tvsubject=(EditText)findViewById(R.id.editTextSubject);

        reqbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvbook.getText().toString().isEmpty()||tvauth.getText().toString().isEmpty()||tvsubject.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Enter all the fields", Toast.LENGTH_SHORT).show();

                DatabaseReference reqref= FirebaseDatabase.getInstance().getReference("Request Books");
                Books b=new Books(tvbook.getText().toString(),tvauth.getText().toString(),0,0,0,tvsubject.getText().toString());
                reqref.child(tvbook.getText().toString()).setValue(b);

                Intent intent=new Intent(Intent.ACTION_SEND);
                String recepientList="aasthaasher24@gmail.com,sakchahande@gmail.com,meghajcr7@gmail.com,nish13199@gmail.com";
                String recepient[]=recepientList.split(",");

                intent.putExtra(Intent.EXTRA_EMAIL,recepient);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Book request");
                intent.putExtra(Intent.EXTRA_TEXT,"I would like to request an addition of the following book to the library: "+tvbook.getText().toString()+"\nAuthor: "+tvauth.getText().toString()+"\nSubject: "+tvsubject.getText().toString());
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent,"Choose an email address"));
            }
        });


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        int ide=menuItem.getItemId();
        switch(ide)
        {
            case R.id.account :
                Intent i= new Intent(getApplicationContext(), UpdateAccount.class);
                startActivity(i);

                break;
            case R.id.DocumentUpload :
                DatabaseReference useref=FirebaseDatabase.getInstance().getReference("UserDetails");
                useref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if((dataSnapshot.child(firebaseUser.getUid()).child("role").getValue().toString()).equalsIgnoreCase("Student")) {
                            Intent o= new Intent(getApplicationContext(), RecyclerViewFiles.class);
                            startActivity(o);
                        }
                        else
                            startActivity(new Intent(getApplicationContext(),DocUpload.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                break;

            case R.id.waiting :
                Intent i3= new Intent(getApplicationContext(), WaitingList.class);
                startActivity(i3);
                break;
            case R.id.issue :
                Intent i2= new Intent(getApplicationContext(), Issue.class);
                startActivity(i2);
                break;

            case R.id.home1:
                startActivity(new Intent(getApplicationContext(),Login.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;

            case R.id.searchBySubject:
                startActivity(new Intent(getApplicationContext(),SearchSubject.class));
                break;

            case R.id.Help:
                startActivity(new Intent(getApplicationContext(),Help.class));
                break;

            case R.id.RequestBook:
                startActivity(new Intent(getApplicationContext(),RequestBooks.class));

        }
        return false;
    }
}
