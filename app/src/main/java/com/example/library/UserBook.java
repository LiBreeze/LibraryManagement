package com.example.library;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserBook extends AppCompatActivity {
DatabaseReference bookref;
RecyclerView recyclerView;
AdapterBook adapterBook;
List<Books>booksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        booksList=new ArrayList<>();
        adapterBook=new AdapterBook(this,booksList);
        recyclerView.setAdapter(adapterBook);

DatabaseReference dbBooks=FirebaseDatabase.getInstance().getReference("Books");
//        Toast.makeText(this, getIntent().getStringExtra("subject"), Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
if(!intent.hasExtra("subject"))
{
    dbBooks.addListenerForSingleValueEvent(valueEventListener);
}
else {
    Query query = FirebaseDatabase.getInstance().getReference("Books")
            .orderByChild("subject")
            .equalTo(getIntent().getStringExtra("subject"));

    query.addListenerForSingleValueEvent(valueEventListener);
}
    }
    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            booksList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Books books = snapshot.getValue(Books.class);
                    booksList.add(books);
                }
                adapterBook.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


}
