    package com.example.library;

    import android.content.DialogInterface;
    import android.content.Intent;
    import android.graphics.Color;
    import android.support.annotation.NonNull;
    import android.support.design.widget.NavigationView;
    import android.support.v4.view.GravityCompat;
    import android.support.v4.widget.DrawerLayout;
    import android.support.v7.app.ActionBarDrawerToggle;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.support.v7.widget.Toolbar;
    import android.telephony.SmsManager;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.ListView;
    import android.widget.Toast;

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.Query;
    import com.google.firebase.database.ValueEventListener;

    import java.text.DateFormat;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.Date;
    import java.util.List;
    import java.util.Locale;

    public class BookReturn extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
        DrawerLayout drawer;
        ArrayAdapter<String> adapter;

        ListView listViewBooks;
        List<Taken> booksList;
        FirebaseUser firebaseUser;
        DatabaseReference adminissue;
        DatabaseReference bookref,userissueref;
        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_book_return);
            firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            final Books b = (Books) getIntent().getSerializableExtra("Books");

            NavigationView navigationView= (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            Toolbar toolbar=findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle("Admin MPSTME");
            toolbar.setTitleTextColor(Color.parseColor("#222222"));

            drawer=(DrawerLayout) findViewById(R.id.enter);
            ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();


            listViewBooks=(ListView) findViewById(R.id.listViewReturn);
            booksList=new ArrayList<>();

            final String bookname = b.getBookname();
            adminissue = FirebaseDatabase.getInstance().getReference("AdminIssue").child(bookname);
            bookref=FirebaseDatabase.getInstance().getReference("Books").child(bookname).child("available");
    //        userissueref=FirebaseDatabase.getInstance().getReference("Issue").child(firebaseUser.getUid());
            listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final Taken t= (Taken) listViewBooks.getItemAtPosition(position);
                    userissueref=FirebaseDatabase.getInstance().getReference("Issue").child(t.getUid());
                    new AlertDialog.Builder(BookReturn.this)
                            .setTitle("Returning...")
                            .setMessage("do you want to return this particular book : "+ t.getBookname())
                            .setPositiveButton("return", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bookref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                            int avail = Integer.parseInt(dataSnapshot1.getValue().toString());
                                            avail=avail+1;

                                            Calendar c = Calendar.getInstance();
                                            int day = c.get(c.DAY_OF_WEEK);
                                            c.set(Calendar.DAY_OF_WEEK, day);
                                            DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                            final String returnDate;

                                           returnDate = df.format(c.getTime());
                                           adminissue.child(t.getUid()).child("return_date").setValue(returnDate);
                                            final int finalAvail = avail;
                                            adminissue.child(t.getUid()).child("expected_date").addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                                   String startDate =dataSnapshot2.getValue().toString();
                                                   TimeCalc(startDate,returnDate,bookname, finalAvail,t);
                                               }

                                               @Override
                                               public void onCancelled(@NonNull DatabaseError databaseError) {

                                               }
                                           });



                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            })
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                }
            });

        }


        @Override
        protected void onStart() {
            super.onStart();

            adminissue.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    booksList.clear();
                    for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
    //
                        Taken taken = bookSnapshot.getValue(Taken.class);
                        booksList.add(taken);
                    }
                    IssueList adapter = new IssueList(BookReturn.this, booksList);
                    listViewBooks.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void TimeCalc(String startDate, String returnDate, final String bookname, final int avail, final Taken t)
        {
            SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());




                                            Date d1=null,d2=null;
                                            try {
                                                d1 = sdf.parse(startDate);
                                                d2=sdf.parse(returnDate);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
    //                                                                    //Date d1=sdf.parse(startDate,new ParsePosition(0));


                                            long diff=d2.getTime()-d1.getTime();
                                            float days=(float) diff/(1000*60*60*24);
                                            final int dayint=(int)days;



                                            if(dayint==-7)
                                                Toast.makeText(this, "Can't return book on the day of issue", Toast.LENGTH_SHORT).show();

                                            else if(dayint>0) {
                                                Toast.makeText(this, dayint + " days late. Fine applicable=" + dayint * 2, Toast.LENGTH_SHORT).show();
                                                new AlertDialog.Builder(BookReturn.this)
                                                        .setTitle("Late Fee payement")
                                                        .setMessage("Late fees payed? : ")
                                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                userissueref=userissueref.child(bookname);
                                                                userissueref.removeValue();
                                                                adminissue.child(t.getUid()).removeValue();
                                                                bookref.setValue(avail);
                                                                Intent intent=new Intent(Intent.ACTION_SEND);
                                                                String recepientList=t.getUseremail();
                                                                String recepient[]=recepientList.split(",");

                                                                intent.putExtra(Intent.EXTRA_EMAIL,recepient);

                                                                intent.putExtra(Intent.EXTRA_SUBJECT,"Book "+t.getBookname()+" returned");
                                                                intent.putExtra(Intent.EXTRA_TEXT,"The following book has been checked in: "+t.getBookname()+"\nIssue Date: "+t.getIssue_date()+"\nExpected Return Date: "+t.getExpected_date()+"\nLate payment= "+dayint * 2);
                                                                intent.setType("message/rfc822");
                                                                startActivity(Intent.createChooser(intent,"Choose an email address"));
                                                            }
                                                        })
                                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                dialog.dismiss();
                                                            }
                                                        }).create().show();
                                            }else
                                            {//Toast.makeText(this, "Returned book on time. No fine", Toast.LENGTH_SHORT).show();
                                                userissueref=userissueref.child(bookname);
                                                userissueref.removeValue();
                                                adminissue.child(t.getUid()).removeValue();
                                                bookref.setValue(avail);
                                                Intent intent=new Intent(Intent.ACTION_SEND);
                                                String recepientList=t.getUseremail();
                                                String recepient[]=recepientList.split(",");

                                                intent.putExtra(Intent.EXTRA_EMAIL,recepient);

                                                intent.putExtra(Intent.EXTRA_SUBJECT,"Book Returned");
                                                intent.putExtra(Intent.EXTRA_TEXT,"The following book has been checked in: "+t.getBookname()+"\nIssue Date: "+t.getIssue_date()+"\nExpected Return Date: "+t.getExpected_date()+"\nBook returned on time hence No late payment fees");
                                                intent.setType("message/rfc822");
                                                startActivity(Intent.createChooser(intent,"Choose an email client"));

                                                DatabaseReference waitref=FirebaseDatabase.getInstance().getReference("Admin Waiting List");
                                                waitref=waitref.child(t.getBookname());
                                                waitref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for(DataSnapshot childDataSnapshot:dataSnapshot.getChildren()) {
                                                            int waitno =Integer.parseInt(childDataSnapshot.child("waitno").getValue().toString());
                                                           String email=childDataSnapshot.child("email").getValue().toString();
                                                           if(waitno>0)
                                                            waitno=waitno-1;
                                                            String uid=childDataSnapshot.getKey().toString();
                                                            DatabaseReference waitref1=FirebaseDatabase.getInstance().getReference("Admin Waiting List");
                                                            waitref1.child(t.getBookname()).child(uid).child("waitno").setValue(waitno);
                                                            if(waitno==0)
                                                            {
                                                            Toast.makeText(BookReturn.this, uid, Toast.LENGTH_SHORT).show();

                                                                Intent intent=new Intent(Intent.ACTION_SEND);
                                                                String recepientList1=email;
                                                                String recepient1[]=recepientList1.split(",");

                                                                intent.putExtra(Intent.EXTRA_EMAIL,recepient1);

                                                                intent.putExtra(Intent.EXTRA_SUBJECT,"Book available");
                                                                intent.putExtra(Intent.EXTRA_TEXT,"The following book has become available: "+t.getBookname());
                                                                intent.setType("message/rfc822");
                                                                startActivity(Intent.createChooser(intent,"Choose an email client"));

                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
//                                                SmsManager smgr = SmsManager.getDefault();
//                                                smgr.sendTextMessage("9820607032",null,"Hey Mom",null,null);
                                            }


        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int ide=menuItem.getItemId();
            switch(ide)
            {

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
