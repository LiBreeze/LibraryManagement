package com.example.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminUser extends AppCompatActivity {
    ListView listViewBooks;
    List<Taken> booksList;
    DatabaseReference userissuref;

    @Override
    protected void onStart() {
        super.onStart();

        userissuref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                booksList.clear();
                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
//
                    Taken taken = bookSnapshot.getValue(Taken.class);
                    booksList.add(taken);
                }
                IssueList adapter = new IssueList(AdminUser.this, booksList);
                listViewBooks.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_user);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle("Admin MPSTME");

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uid=getIntent().getStringExtra("uid");
                    Intent intent=new Intent(getApplicationContext(),AdminUserWait.class);


                    intent.putExtra("uid",uid);
                    intent.putExtra("email",getIntent().getStringExtra("email"));
                    startActivity(intent);

//                    Snackbar.make(view, "Not in the waiting list for any book", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    //startActivity(new Intent(getApplicationContext(),AdminUserWait.class));

                }
            });
            userissuref = FirebaseDatabase.getInstance().getReference("Issue");
            userissuref = userissuref.child(getIntent().getStringExtra("uid"));

            listViewBooks = (ListView) findViewById(R.id.listViewBooks);

            booksList = new ArrayList<>();
        }
    }