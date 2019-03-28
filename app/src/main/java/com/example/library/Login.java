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
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
//import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Login extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    ListView search_book;
    Button b;
    ArrayAdapter<String> adapter;

    DatabaseReference bookref;
    List<Books> booksList;

    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    AdapterBook adapterBook;

    static int f=0;

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
//                BooksList adapter=new BooksList(Login.this,booksList);
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
        setContentView(R.layout.activity_login);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        booksList = new ArrayList<>();
        adapterBook = new AdapterBook(this, booksList);
        recyclerView.setAdapter(adapterBook);

        DatabaseReference dbBooks = FirebaseDatabase.getInstance().getReference("Books");
//        Toast.makeText(this, getIntent().getStringExtra("subject"), Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        if (!intent.hasExtra("subject")) {
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

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            assert firebaseUser != null;
            if (firebaseUser.getPhotoUrl() != null) {

                Glide.with(this)
                        .load(firebaseUser.getPhotoUrl().toString()).into(displayImageView);


            }

            nameTextView.setText(firebaseUser.getDisplayName());
            emailTextView.setText(firebaseUser.getEmail());

            dbBooks.addListenerForSingleValueEvent(valueEventListener);
        } else {

            bookref = FirebaseDatabase.getInstance().getReference("Books");
            //booksList = new ArrayList<>();

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

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if(firebaseUser!=null)
            {if (firebaseUser.getPhotoUrl() != null) {

                Glide.with(this)
                        .load(firebaseUser.getPhotoUrl().toString()).into(displayImageView);


            }

            nameTextView.setText(firebaseUser.getDisplayName());
            emailTextView.setText(firebaseUser.getEmail());}
            Query query = FirebaseDatabase.getInstance().getReference("Books")
                    .orderByChild("subject")
                    .equalTo(getIntent().getStringExtra("subject"));

            query.addListenerForSingleValueEvent(valueEventListener);

//    listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        final Books a= (Books) listViewBooks.getItemAtPosition(position);
//        Toast.makeText(Login.this, a.getBookname(), Toast.LENGTH_SHORT).show();
//        if(a.getAvailable()<=0)
//        {
//            new AlertDialog.Builder(Login.this)
//                    .setTitle("Unavailable book")
//                    .setMessage("do you want to add your name to the waiting list for this particular book : "+a.getBookname())
//                    .setPositiveButton("+Waiting list", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent=new Intent(getApplicationContext(),WaitingList.class);
//                            intent.putExtra("Name",a.getBookname());
//                            startActivity(intent);
//                        }
//                    })
//                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).create().show();
//        }
//        else{
//        Intent intent=new Intent(getApplicationContext(),BookDetail.class);
//        intent.putExtra("Books",a);
//        startActivity(intent);}
//    }
//});
        }
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
            case R.id.account :
                Intent i= new Intent(getApplicationContext(), UpdateAccount.class);
                startActivity(i);

                break;
            case R.id.DocumentUpload :
                DatabaseReference useref=FirebaseDatabase.getInstance().getReference("UserDetails");
                useref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
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
