package com.example.library;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Issue extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static TextView t1;
    DrawerLayout drawer;
    DatabaseReference takenref,bookref,availref,adminissueref;
    FirebaseUser firebaseUser;

    DatabaseReference userissuref;
    Button b;
    ArrayAdapter<String> adapter;

    ListView listViewBooks;
    List<Taken> booksList;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        flag=0;

        NavigationView navigationView= (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        takenref= FirebaseDatabase.getInstance().getReference("Issue");

        bookref=FirebaseDatabase.getInstance().getReference("Books");

        adminissueref=FirebaseDatabase.getInstance().getReference("AdminIssue");

        userissuref=FirebaseDatabase.getInstance().getReference("Issue");
        userissuref=userissuref.child(firebaseUser.getUid());

        listViewBooks=(ListView) findViewById(R.id.listViewBooks);

        booksList=new ArrayList<>();



        drawer = (DrawerLayout) findViewById(R.id.enter);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(Issue.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
                //Intent i= new Intent(getApplicationContext(), Scanner.class);
                //startActivity(i);
            }
        });

        View navHeaderView = navigationView.getHeaderView(0);

        ImageView displayImageView=(ImageView)navHeaderView.findViewById(R.id.imageView_display);
        TextView nameTextView=(TextView)navHeaderView.findViewById(R.id.textView_name);
        TextView emailTextView=(TextView)navHeaderView.findViewById(R.id.textView_email);

//        FirebaseDatabase.getInstance().getReference("UserDetails").child()
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        if(firebaseUser.getPhotoUrl()!=null) {

            Glide.with(this)
                    .load(firebaseUser.getPhotoUrl().toString()).into(displayImageView);


        }

        nameTextView.setText(firebaseUser.getDisplayName());
        emailTextView.setText(firebaseUser.getEmail());



    }

    @Override
    protected void onStart() {
        super.onStart();

        userissuref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                booksList.clear();
                for(DataSnapshot bookSnapshot:dataSnapshot.getChildren())
                {
//
                    Taken taken=bookSnapshot.getValue(Taken.class);
                    booksList.add(taken);
                }
                IssueList adapter=new IssueList(Issue.this,booksList);
                listViewBooks.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Taken t= (Taken) listViewBooks.getItemAtPosition(position);
                final String name=t.getBookname();
                bookref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(name).exists())
                        {
                            int avail= Integer.parseInt(dataSnapshot.child(name).child("available").getValue().toString());
                            if(avail>0)
                                Toast.makeText(Issue.this, "Can reissue", Toast.LENGTH_SHORT).show();
                            else
                            {
                                DatabaseReference adminwait=FirebaseDatabase.getInstance().getReference("Admin Waiting List").child(name);
                                adminwait.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        long waitno=dataSnapshot.getChildrenCount();
                                        if(waitno==0) {
                                            Toast.makeText(Issue.this, "Can reissue", Toast.LENGTH_SHORT).show();

                                        }
                                        else
                                            Toast.makeText(Issue.this, "Cannot reissue", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        super.onActivityResult(requestCode, resultCode, data);
        final IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null && intentResult.getContents() != null) {
            new AlertDialog.Builder(Issue.this)
                    .setTitle("Scan")
                    .setMessage(intentResult.getContents())
                    .setPositiveButton("Issue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final int a;
                            final String ans = intentResult.getContents();
                            flag=1;

                            if(ans.contains("."))
                            Toast.makeText(Issue.this, "Incorrect qr code", Toast.LENGTH_SHORT).show();
                            else {
                                bookref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
                                    if (!dataSnapshot.child(ans).exists())
                                    {Toast.makeText(Issue.this, "Book doesnt exist in the library", Toast.LENGTH_SHORT).show();

                                    flag=0;}
                                    else {
                                        flag=1;

                                        takenref = FirebaseDatabase.getInstance().getReference("Issue");

                                        takenref = takenref.child(firebaseUser.getUid());
                                        takenref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                long number = dataSnapshot.getChildrenCount();
                                                if (number == 2)
                                                    Toast.makeText(Issue.this, "You have already issued the maximum limit of 2 books and cannot issue another book", Toast.LENGTH_SHORT).show();
                                                else {
                                                    if (dataSnapshot.child(ans).exists()) {
                                                        Toast.makeText(Issue.this, "You already issued a copy of this book", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        availref = bookref.child(ans).child("available");
                                                        availref.addListenerForSingleValueEvent(new ValueEventListener() {

                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                                                int avail = Integer.parseInt(dataSnapshot1.getValue().toString());
                                                                if (avail > 0) {
                                                                    avail = avail - 1;
                                                                    availref.setValue(avail);
                                                                    //startActivity(new Intent(getApplicationContext(), Login.class));
                                                                    final Calendar c = Calendar.getInstance();
                                                                    int day = c.get(c.DAY_OF_WEEK);
                                                                    c.set(Calendar.DAY_OF_WEEK, day);

//                                                                    c.add(Calendar.DATE, 7);

                                                                    DatabaseReference useref=FirebaseDatabase.getInstance().getReference("UserDetails");
                                                                    useref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                                                                            String role = dataSnapshot.child(firebaseUser.getUid()).child("role").getValue().toString();
                                                                            Toast.makeText(Issue.this, role, Toast.LENGTH_SHORT).show();
                                                                            if (role.equalsIgnoreCase("Teacher")) {

                                                                                String startDate = "", endDate = "";

                                                                                startDate = df.format(c.getTime());
                                                                                c.add(Calendar.DATE, 30);
                                                                                endDate = df.format(c.getTime());
                                                                                Taken taken = new Taken(startDate, endDate, "null", ans, firebaseUser.getEmail(), firebaseUser.getUid());
                                                                                takenref.child(ans).setValue(taken);

                                                                                adminissueref.child(ans).child(firebaseUser.getUid()).setValue(taken);
                                                                                Intent i = new Intent(getApplicationContext(), JustIssue.class);
                                                                                i.putExtra("Taken", taken);
                                                                                i.putExtra("bookname", ans);
                                                                                startActivity(i);

                                                                            } else if (role.equalsIgnoreCase("Student")) {
                                                                                String startDate = "", endDate = "";
                                                                                startDate = df.format(c.getTime());
                                                                                c.add(Calendar.DATE, 7);
                                                                                endDate = df.format(c.getTime());
                                                                                Taken taken = new Taken(startDate, endDate, "null", ans, firebaseUser.getEmail(), firebaseUser.getUid());
                                                                                takenref.child(ans).setValue(taken);

                                                                                adminissueref.child(ans).child(firebaseUser.getUid()).setValue(taken);
                                                                                Intent i = new Intent(getApplicationContext(), JustIssue.class);
                                                                                i.putExtra("Taken", taken);
                                                                                i.putExtra("bookname", ans);
                                                                                startActivity(i);

                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

//                                                                Date d1=null,d2=null;
//                                                                try {
//                                                                    d1 = sdf.parse(startDate);
//                                                                    d2=sdf.parse(endDate);
//                                                                } catch (ParseException e) {
//                                                                    e.printStackTrace();
//                                                                }
////                                                                    //Date d1=sdf.parse(startDate,new ParsePosition(0));
//
////                                                                    //Date d2=sdf.parse(endDate,new ParsePosition(0));
//                                                                    long diff=d2.getTime()-d1.getTime();
//                                                                    float days=(float) diff/(1000*60*60*24);
//                                                                    int dayint=(int)days;
//                                                                    Toast.makeText(Issue.this, "No of days:"+dayint, Toast.LENGTH_SHORT).show();




                                                                } else {
                                                                    new AlertDialog.Builder(Issue.this)
                                                                            .setTitle("Unavailable book")
                                                                            .setMessage("do you want to add your name to the waiting list for this particular book : " + intentResult.getContents())
                                                                            .setPositiveButton("+Waiting list", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    Intent intent = new Intent(getApplicationContext(), WaitingList.class);
                                                                                    intent.putExtra("Name", ans);
                                                                                    startActivity(intent);
                                                                                }
                                                                            })
                                                                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    dialog.dismiss();
                                                                                }
                                                                            }).create().show();
//                                                                    Toast.makeText(getApplicationContext(), "No copies of the book available", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }


                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
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
                            }

//                            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                            ClipData data = ClipData.newPlainText("Result", intentResult.getContents());
//                            clipboardManager.setPrimaryClip(data);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }
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
            case R.id.searchBySubject:
                startActivity(new Intent(getApplicationContext(),SearchSubject.class));
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



            case R.id.home1:
                Intent i3= new Intent(getApplicationContext(), Login.class);
                startActivity(i3);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;

            case R.id.Help:
                startActivity(new Intent(getApplicationContext(),Help.class));
                break;

            case R.id.RequestBook:
                startActivity(new Intent(getApplicationContext(),RequestBooks.class));
        }
        return false;
    }
}




