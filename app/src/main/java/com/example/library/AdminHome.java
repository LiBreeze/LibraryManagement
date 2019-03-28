package com.example.library;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    ListView listViewBooks;
    List<Books> booksList;
    ArrayAdapter<String> adapter;
    DatabaseReference bookref;


    RecyclerView recyclerView;
    AdapterBook adapterBook;


//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        bookref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                booksList.clear();
//                for(DataSnapshot bookSnapshot:dataSnapshot.getChildren())
//                {
////
//                    Books books=bookSnapshot.getValue(Books.class);
//                    booksList.add(books);
//                }
//                BooksList adapter=new BooksList(AdminHome.this,booksList);
//                listViewBooks.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bookref = FirebaseDatabase.getInstance().getReference("Books");
        //listViewBooks=(ListView) findViewById(R.id.listViewBooks);

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

        DatabaseReference dbBooks = FirebaseDatabase.getInstance().getReference("Books");
//        Toast.makeText(this, getIntent().getStringExtra("subject"), Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        if (!intent.hasExtra("subject")) {
            dbBooks.addListenerForSingleValueEvent(valueEventListener);
        } else {
            Query query = FirebaseDatabase.getInstance().getReference("Books")
                    .orderByChild("subject")
                    .equalTo(getIntent().getStringExtra("subject"));

            query.addListenerForSingleValueEvent(valueEventListener);


//        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                final Books a= (Books) listViewBooks.getItemAtPosition(position);
//                Toast.makeText(AdminHome.this, a.getBookname(), Toast.LENGTH_SHORT).show();
//
//                    Intent intent=new Intent(getApplicationContext(),BookDetail.class);
//                    intent.putExtra("Books",a);
//                    startActivity(intent);
//            }
//        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu,menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) searchItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                adapterBook.getFilter().filter(newText);
//                return false;

                final List<Books> filterList=filter(booksList,newText);

                adapterBook.update(filterList);
                return true;
            }
        });
        return true;
    }

    private List<Books>filter(List<Books>b,String query)
    {
        query=query.toLowerCase().trim();
        final List<Books> filteredBook=new ArrayList<>();
        for(Books books:b)
        {
            if(books.getBookname().toLowerCase().contains(query)||books.getAuthor().toLowerCase().contains(query))
            {
                filteredBook.add(books);

            }

        }
        return filteredBook;
    }
}
