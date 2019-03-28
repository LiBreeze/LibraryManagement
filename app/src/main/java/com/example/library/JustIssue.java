package com.example.library;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class JustIssue extends AppCompatActivity implements Serializable {
DatabaseReference bookref;
Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_just_issue);
        button=findViewById(R.id.buttonHome);
        Taken taken= (Taken) getIntent().getSerializableExtra("Taken");
        bookref= FirebaseDatabase.getInstance().getReference("Books");
        final String bookname=getIntent().getStringExtra("bookname");
        TextView textViewName=(TextView)findViewById(R.id.bookname);
        final TextView textViewAuthor=(TextView)findViewById(R.id.authorbook);
        final TextView textViewSubject=(TextView)findViewById(R.id.booksubject);
        TextView textViewIssueDate=(TextView)findViewById(R.id.issuedate);

        textViewName.setText(taken.getBookname());

        bookref.child(bookname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    textViewAuthor.setText(dataSnapshot.child("author").getValue().toString());
                    textViewSubject.setText(dataSnapshot.child("subject").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        textViewIssueDate.setText(taken.issue_date);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}
