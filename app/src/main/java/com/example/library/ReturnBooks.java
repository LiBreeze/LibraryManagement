package com.example.library;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReturnBooks extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    ListView listViewBooks;
    List<Books> booksList;
    ArrayAdapter<String> adapter;
    DatabaseReference bookref;
    DatabaseReference adminissueref;

    @Override
    protected void onStart() {
        super.onStart();

        adminissueref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                booksList.clear();
                for(DataSnapshot bookSnapshot:dataSnapshot.getChildren())
                {
                    Books books=bookSnapshot.getValue(Books.class);
                    booksList.add(books);
                }
                BooksList adapter=new BooksList(ReturnBooks.this,booksList);
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
        setContentView(R.layout.activity_return_books);

        NavigationView navigationView= (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        adminissueref= FirebaseDatabase.getInstance().getReference("Books");
        listViewBooks=(ListView) findViewById(R.id.listViewReturn);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Admin MPSTME");
        toolbar.setTitleTextColor(Color.parseColor("#222222"));


        drawer=(DrawerLayout) findViewById(R.id.enter);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        booksList=new ArrayList<>();

        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Books a= (Books) listViewBooks.getItemAtPosition(position);
                Intent i=new Intent(getApplicationContext(),BookReturn.class);
                i.putExtra("Books",a);
                startActivity(i);
            }
        });
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
