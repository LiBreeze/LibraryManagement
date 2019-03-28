package com.example.library;

import android.app.ProgressDialog;
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
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class book_input extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseDatabase database;
    DatabaseReference bookref;
    ProgressDialog progressDialog;
    Button addbook;
    EditText tvbook, tvauth, tvrack, tvquan,tvsubject;

    ListView listViewBooks;
    List<Books> booksList;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    DrawerLayout drawer;

    public void qrgenerator(String name)
    {
        Intent i=new Intent(getApplicationContext(),QRGenerator.class);
        i.putExtra("Name",name);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_input);

        NavigationView navigationView= (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#222222"));


        drawer=(DrawerLayout) findViewById(R.id.enter);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        database=FirebaseDatabase.getInstance();
        bookref=FirebaseDatabase.getInstance().getReference("Books");
        tvbook= (EditText) findViewById(R.id.namebook);
        progressDialog=new ProgressDialog(this);
        addbook=(Button) findViewById(R.id.create);
        tvauth=(EditText) findViewById(R.id.bookauthor);
        tvrack=(EditText)findViewById(R.id.editTextRack);
        tvquan=(EditText)findViewById(R.id.editTextquan);
        tvsubject=(EditText)findViewById(R.id.editTextSubject);

        listViewBooks=(ListView) findViewById(R.id.listViewBooks);

        booksList=new ArrayList<>();

        firebaseAuth=FirebaseAuth.getInstance();
        //userref=F
        firebaseUser=firebaseAuth.getCurrentUser();



        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvrack.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Rack no", Toast.LENGTH_SHORT).show();
                }

                else if (tvauth.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter author", Toast.LENGTH_SHORT).show();
                }
                else if (tvbook.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter book name", Toast.LENGTH_SHORT).show();
                }
                else if (tvquan.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter quantity", Toast.LENGTH_SHORT).show();

                }

                else if(tvsubject.getText().toString().isEmpty())
                    Toast.makeText(book_input.this, "Enter subject", Toast.LENGTH_SHORT).show();
                else {

                    progressDialog.setMessage("Adding book details");
                    progressDialog.show();
//                    final String id = tvid.getText().toString().trim();
                    final String name=tvbook.getText().toString().toLowerCase().trim();
                    final int quantity = Integer.parseInt(tvquan.getText().toString());
                    //final String name=tvquan.getText().toString();
                    final int rack=Integer.parseInt(tvrack.getText().toString());
                    final Books books = new Books(tvbook.getText().toString(), tvauth.getText().toString(), quantity,quantity,rack,tvsubject.getText().toString());
                    bookref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child(name).exists()) {

                                //bookref.child(books.getBookname()).child("issue").child(firebaseUser.getUid()).setValue(firebaseUser.getEmail());

                                //bookref.child(books.getId()).child("id").child("extra").push();

                                int q,a; String name1;
                                q=0; a=0;
                                for(DataSnapshot childDataSnapshot:dataSnapshot.getChildren())
                                {
                                    name1=childDataSnapshot.getKey().toString();
                                    Toast.makeText(book_input.this, name1, Toast.LENGTH_SHORT).show();
                                    if(name1.equalsIgnoreCase(name))
                                    {q= Integer.parseInt(childDataSnapshot.child("quantity").getValue().toString());
                                    a=Integer.parseInt(childDataSnapshot.child("available").getValue().toString());
                                    break;}

                                }
                                q += quantity;
                                a+=quantity;
                                bookref.child(books.getBookname()).child("quantity").setValue(q);
                                bookref.child(books.getBookname()).child("available").setValue(a);


                                Toast.makeText(getApplicationContext(), "Book already exists. Quantity of book updated", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }

//
                            else {

                                //bookref.child(books.getId()).child("issue").setValue(firebaseUser.getUid());
                               bookref.child(books.getBookname()).setValue(books);
                                Toast.makeText(getApplicationContext(), "Book Added", Toast.LENGTH_SHORT).show();

                                progressDialog.dismiss();
                                qrgenerator(books.getBookname());
                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Can't add books", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }});
    }

    @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem menuItem){
            int ide = menuItem.getItemId();
            switch (ide) {

                case R.id.DocumentUpload:
                    Intent o = new Intent(getApplicationContext(), DocUpload.class);
                    startActivity(o);

                    break;

                case R.id.pay:
                    Intent i1 = new Intent(getApplicationContext(), ReturnBooks.class);
                    startActivity(i1);

                    break;


                case R.id.home1:
                    startActivity(new Intent(getApplicationContext(), AdminHome.class));
                    break;
                case R.id.logout:
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    break;

                case R.id.addbooks:
                    startActivity(new Intent(getApplicationContext(), book_input.class));
                    break;

                case R.id.users:
                    startActivity(new Intent(getApplicationContext(),RecyclerViewUsers.class));
                    break;

                case R.id.searchBySubject:
                    startActivity(new Intent(getApplicationContext(),SearchSubject.class));
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
