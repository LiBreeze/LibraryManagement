package com.example.library;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerViewUsers extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    DrawerLayout drawer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_users);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Admin MPSTME");

        toolbar.setTitleTextColor(Color.parseColor("#222222"));


        drawer = (DrawerLayout) findViewById(R.id.enter);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


//        ActionBar actionBar=getSupportActionBar();
//        actionBar.setTitle("Admin MPSTME");
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @android.support.annotation.Nullable String s) {
                String uid = dataSnapshot.getKey();
                String name= dataSnapshot.child("name").getValue(String.class);
                String email= dataSnapshot.child("email").getValue(String.class);
                String sapid= dataSnapshot.child("sapid").getValue().toString();

                ((AdapterUsers) recyclerView.getAdapter()).update(uid, name,email,sapid);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @android.support.annotation.Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @android.support.annotation.Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView = findViewById(R.id.recyclerViewUsers);

        recyclerView.setLayoutManager(new LinearLayoutManager(RecyclerViewUsers.this));
        AdapterUsers adapterUsers = new AdapterUsers(recyclerView, RecyclerViewUsers.this, new ArrayList<String>(), new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>());
        recyclerView.setAdapter(adapterUsers);
    }

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


            case R.id.home1:
                startActivity(new Intent(getApplicationContext(),AdminHome.class));
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
                break;

            case R.id.req:
                startActivity(new Intent(getApplicationContext(),AdminRequest.class));
                break;
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
