package com.example.library;

import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WaitingList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;
    DatabaseReference waitref,adminwaitref,userwaitref;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseUser firebaseUser;

    ArrayAdapter<String> adapter;

    ListView listViewBooks;
    List<AdminWait> booksList;

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
                UserWaitList adapter=new UserWaitList(WaitingList.this,booksList);
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
        setContentView(R.layout.activity_waiting_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        userwaitref=FirebaseDatabase.getInstance().getReference("Waiting List");
        userwaitref=userwaitref.child(firebaseUser.getUid());

        waitref= FirebaseDatabase.getInstance().getReference("Waiting List");
        adminwaitref=FirebaseDatabase.getInstance().getReference("Admin Waiting List");

        listViewBooks=(ListView) findViewById(R.id.listViewBooks);

        booksList=new ArrayList<>();

        navigationView= (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.getHeaderView(0);

        ImageView displayImageView=(ImageView)navHeaderView.findViewById(R.id.imageView_display);
        TextView nameTextView=(TextView)navHeaderView.findViewById(R.id.textView_name);
        TextView emailTextView=(TextView)navHeaderView.findViewById(R.id.textView_email);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        if(firebaseUser.getPhotoUrl()!=null) {

            Glide.with(this)
                    .load(firebaseUser.getPhotoUrl().toString()).into(displayImageView);


        }

        nameTextView.setText(firebaseUser.getDisplayName());
        emailTextView.setText(firebaseUser.getEmail());

        drawer = (DrawerLayout) findViewById(R.id.enter);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        String bookname="";
        Intent intent = getIntent();
        if (intent.hasExtra("Name"))
        {
            bookname=getIntent().getStringExtra("Name");
            addWaiting(bookname);
        }
//        bookname=getIntent().getStringExtra("Name");
//        Toast.makeText(this, bookname, Toast.LENGTH_SHORT).show();
//        if(bookname.equalsIgnoreCase(""))
//        {
//
//        }

        }



    private void addWaiting(final String bookname) {
        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        waitref.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(bookname).exists())
                {
                    Toast.makeText(WaitingList.this, "You are already in the waiting list for the book : "+bookname, Toast.LENGTH_SHORT).show();
                }
                else {
                    //waitref.child(firebaseUser.getUid()).child(bookname).setValue(bookname);
                    String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


                    //adminwaitref.child(bookname).child(firebaseUser.getUid()).setPriority(ServerValue.TIMESTAMP);

                    //final AdminWait adminWait=new AdminWait(currentDateTimeString,4,firebaseUser.getEmail());
//
                    //adminwaitref.child(bookname).child(firebaseUser.getUid()).setValue(adminWait);
                    adminwaitref=adminwaitref.child(bookname);
                    adminwaitref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         if(dataSnapshot.exists())
                         {
                             long waitno=dataSnapshot.getChildrenCount();
                             waitno=waitno+1;
                             Toast.makeText(WaitingList.this, "wait no"+waitno, Toast.LENGTH_SHORT).show();
                             final AdminWait adminWait=new AdminWait(currentDateTimeString,waitno,firebaseUser.getEmail(),bookname);
                             adminwaitref.child(firebaseUser.getUid()).setValue(adminWait);
                             waitref.child(firebaseUser.getUid()).child(bookname).setValue(adminWait);

                         }

                         else
                         {
                             long waitno=1;
                             final AdminWait adminWait=new AdminWait(currentDateTimeString,waitno,firebaseUser.getEmail(),bookname);
                             adminwaitref.child(firebaseUser.getUid()).setValue(adminWait);
                             waitref.child(firebaseUser.getUid()).child(bookname).setValue(adminWait);

                         }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        waitref.child(firebaseUser.getUid()).child("Book Name").setValue(bookname);

        //waitref.child(firebaseUser.getUid()).child("Book Name").child("bookname").setValue(bookname);
       // waitref.child(firebaseUser.getUid()).child("Book Name").child("date").setValue(currentDateTimeString);




    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int ide = menuItem.getItemId();
        switch (ide) {
            case R.id.searchBySubject:
                startActivity(new Intent(getApplicationContext(),SearchSubject.class));
                break;

            case R.id.DocumentUpload :
                DatabaseReference useref=FirebaseDatabase.getInstance().getReference("UserDetails");
                useref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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


            case R.id.account:
                Intent i= new Intent(getApplicationContext(), UpdateAccount.class);
                startActivity(i);

                break;


            case R.id.waiting:
                Intent i6= new Intent(getApplicationContext(), WaitingList.class);
                startActivity(i6);
                break;
            case R.id.issue:
                Intent i2 = new Intent(getApplicationContext(), Issue.class);
                startActivity(i2);
                break;


            case R.id.Help:
                startActivity(new Intent(getApplicationContext(),Help.class));
                break;

            case R.id.RequestBook:
                startActivity(new Intent(getApplicationContext(),RequestBooks.class));

            case R.id.home1:
                Intent i3= new Intent(getApplicationContext(), Login.class);
                startActivity(i3);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return false;
    }
}
