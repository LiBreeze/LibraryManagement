package com.example.library;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminUserWait extends AppCompatActivity {
    ListView listViewBooks;
    List<AdminWait> booksList;
    DatabaseReference userwaitref;

    @Override
    protected void onStart() {
        super.onStart();
        userwaitref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                booksList.clear();
                for(DataSnapshot bookSnapshot:dataSnapshot.getChildren())
                {
//
                    AdminWait adminWait=bookSnapshot.getValue(AdminWait.class);
                    booksList.add(adminWait);
                }
                UserWaitList adapter=new UserWaitList(AdminUserWait.this,booksList);
                listViewBooks.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_wait);

        userwaitref= FirebaseDatabase.getInstance().getReference("Waiting List");
        String uid=getIntent().getStringExtra("uid");
        userwaitref=userwaitref.child(uid);
        listViewBooks = (ListView) findViewById(R.id.listViewBooks);

        booksList = new ArrayList<>();
    }
}
