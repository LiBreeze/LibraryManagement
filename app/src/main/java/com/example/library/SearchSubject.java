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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class SearchSubject extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;
    ListView search_book;
    Button b;
    ArrayAdapter<String> adapter;

    DatabaseReference bookref;
    ListView listViewBooks;
    List<Books> booksList;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_subject);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        search_book=(ListView)findViewById(R.id.search_book);
        ArrayList<String> arraybook = new ArrayList<>();
        arraybook.addAll(Arrays.asList(getResources().getStringArray(R.array.my_book)));
        adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,arraybook);

        toolbar.setTitleTextColor(Color.parseColor("#222222"));
        NavigationView navigationView= (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        search_book.setAdapter(adapter);


        search_book.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String subject= (String) search_book.getItemAtPosition(position);
//                Intent i=new Intent(getApplicationContext(),Login.class);
//                i.putExtra("subject",subject);
//                startActivity(i);

                String subject= (String) search_book.getItemAtPosition(position);
                Intent i=new Intent(getApplicationContext(),Login.class);
                i.putExtra("subject",subject);
                startActivity(i);
            }
        });



        drawer=(DrawerLayout) findViewById(R.id.enter);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View navHeaderView = navigationView.getHeaderView(0);

        ImageView displayImageView=(ImageView)navHeaderView.findViewById(R.id.imageView_display);
        TextView nameTextView=(TextView)navHeaderView.findViewById(R.id.textView_name);
        TextView emailTextView=(TextView)navHeaderView.findViewById(R.id.textView_email);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
         if(firebaseUser.getPhotoUrl()!=null) {

            Glide.with(this)
                    .load(firebaseUser.getPhotoUrl().toString()).into(displayImageView);


        }


        nameTextView.setText(firebaseUser.getDisplayName());
        emailTextView.setText(firebaseUser.getEmail());
    }}


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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int ide=menuItem.getItemId();
        switch(ide)
        {
            case R.id.searchBySubject:
                startActivity(new Intent(getApplicationContext(),SearchSubject.class));
                break;

            case R.id.account :
                if(firebaseUser!=null) {
                    Intent i = new Intent(getApplicationContext(), UpdateAccount.class);
                    startActivity(i);
                }
                break;
            case R.id.DocumentUpload :
                if(firebaseUser!=null) {
                    DatabaseReference useref = FirebaseDatabase.getInstance().getReference("UserDetails");
                    useref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if ((dataSnapshot.child(firebaseUser.getUid()).child("role").getValue().toString()).equalsIgnoreCase("Student")) {
                                Intent o = new Intent(getApplicationContext(), RecyclerViewFiles.class);
                                startActivity(o);
                            } else
                                startActivity(new Intent(getApplicationContext(), DocUpload.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(), DocUpload.class));
                }

                break;

//            case R.id.pay :
//                Intent i1= new Intent(getApplicationContext(), ReturnBooks.class);
//                startActivity(i1);

//                break;
            case R.id.waiting :
                if(firebaseUser!=null)
                {Intent i3= new Intent(getApplicationContext(), WaitingList.class);
                startActivity(i3);}
                break;
            case R.id.issue :
                if(firebaseUser!=null)
                {Intent i2= new Intent(getApplicationContext(), Issue.class);
                startActivity(i2);}
                break;

            case R.id.home1:
                if(firebaseUser!=null)
                startActivity(new Intent(getApplicationContext(),Login.class));
                else
                    startActivity(new Intent(getApplicationContext(),AdminHome.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;

            case R.id.Help:
                if(firebaseUser!=null)
                startActivity(new Intent(getApplicationContext(),Help.class));
                break;

            case R.id.RequestBook:
                if(firebaseUser!=null)
                startActivity(new Intent(getApplicationContext(),RequestBooks.class));

        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.search_menu,menu);
        MenuItem menuItem= menu.findItem(R.id.listViewBooks);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }
}
