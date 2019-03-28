package com.example.library;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminRequest extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;
    RecyclerView recyclerView;
    AdapterBook adapterBook;
    List<Books> booksList;
    DatabaseReference reqref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        reqref=FirebaseDatabase.getInstance().getReference("Request Books");
        reqref.addListenerForSingleValueEvent(valueEventListener);

        booksList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Admin MPSTME");

        toolbar.setTitleTextColor(Color.parseColor("#222222"));


        drawer = (DrawerLayout) findViewById(R.id.enter);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        booksList = new ArrayList<>();
        adapterBook = new AdapterBook(this, booksList);
        recyclerView.setAdapter(adapterBook);
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int ide=menuItem.getItemId();
        switch(ide)
        {
            case R.id.searchBySubject:
                startActivity(new Intent(getApplicationContext(),SearchSubject.class));
                break;

            case R.id.DocumentUpload :
                Intent o= new Intent(getApplicationContext(), DocUpload.class);
                startActivity(o);

                break;

            case R.id.pay :
                Intent i1= new Intent(getApplicationContext(), ReturnBooks.class);
                startActivity(i1);

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

            case R.id.addbooks:
                startActivity(new Intent(getApplicationContext(),book_input.class));
                break;

            case R.id.users:
                startActivity(new Intent(getApplicationContext(),RecyclerViewUsers.class));

        }
        return false;

    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

}
