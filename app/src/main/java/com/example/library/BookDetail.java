package com.example.library;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookDetail extends AppCompatActivity implements Serializable {
    DatabaseReference takenref, bookref, adminissueref, userissuref, availref;
    FirebaseUser firebaseUser;
    Books b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        takenref = FirebaseDatabase.getInstance().getReference("Issue");

        bookref = FirebaseDatabase.getInstance().getReference("Books");

        adminissueref = FirebaseDatabase.getInstance().getReference("AdminIssue");

        userissuref = FirebaseDatabase.getInstance().getReference("Issue");



//        if (Login.f==0)
//        {Toast.makeText(this, AddAccountDetails.role, Toast.LENGTH_SHORT).show();}
        TextView name, author, available, rackno, subject;

        b = (Books) getIntent().getSerializableExtra("Books");
        name = findViewById(R.id.bookname);
        author = findViewById(R.id.authorbook);
        subject = findViewById(R.id.booksubject);
        available = findViewById(R.id.available);
        rackno = findViewById(R.id.rackno);

        name.setText("Name:" + b.getBookname());
        author.setText("Author:" + b.getAuthor());
        subject.setText("Subject:" + b.getSubject());
        available.setText("Available copies:" + Integer.toString(b.getAvailable()));
        //rackno.setText("Rack no:"+Integer.toString(Login.f));
        rackno.setText("Rack no:" + Integer.toString(b.getRackno()));

        Button b1 = findViewById(R.id.buttonIssue);
        if (firebaseUser == null) {
            b1.setVisibility(View.INVISIBLE);


        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    IntentIntegrator intentIntegrator = new IntentIntegrator(BookDetail.this);
                    intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    intentIntegrator.setCameraId(0);
                    intentIntegrator.setOrientationLocked(false);
                    intentIntegrator.setPrompt("");
                    intentIntegrator.setBeepEnabled(true);
                    intentIntegrator.setBarcodeImageEnabled(true);
                    intentIntegrator.initiateScan();


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        super.onActivityResult(requestCode, resultCode, data);

        final IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null && intentResult.getContents() != null) {
            new AlertDialog.Builder(BookDetail.this)
                    .setTitle("Scan")
                    .setMessage(intentResult.getContents())
                    .setPositiveButton("Issue", new DialogInterface.OnClickListener() {
                        final String ans = intentResult.getContents();

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (ans.equalsIgnoreCase(b.getBookname())) {
//                                Toast.makeText(BookDetail.this, "Correct book name", Toast.LENGTH_SHORT).show();

                                takenref = FirebaseDatabase.getInstance().getReference("Issue");

                                takenref = takenref.child(firebaseUser.getUid());
                                takenref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        long number = dataSnapshot.getChildrenCount();
                                        if (number == 2)
                                            Toast.makeText(BookDetail.this, "You have already issued the maximum limit of 2 books and cannot issue another book", Toast.LENGTH_SHORT).show();
                                        else {
                                            if (dataSnapshot.child(ans).exists()) {
                                                Toast.makeText(BookDetail.this, "You already issued a copy of this book", Toast.LENGTH_SHORT).show();
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
                                                            Calendar c = Calendar.getInstance();
                                                            int day = c.get(c.DAY_OF_WEEK);
                                                            c.set(Calendar.DAY_OF_WEEK, day);
                                                            DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                                            String startDate = "", endDate = "";

                                                            startDate = df.format(c.getTime());
                                                            c.add(Calendar.DATE, 7);

                                                            endDate = df.format(c.getTime());

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


                                                            Taken taken = new Taken(startDate, endDate, "null", ans, firebaseUser.getEmail(), firebaseUser.getUid());
                                                            takenref.child(ans).setValue(taken);

                                                            adminissueref.child(ans).child(firebaseUser.getUid()).setValue(taken);
                                                            Intent i = new Intent(getApplicationContext(), JustIssue.class);
                                                            i.putExtra("Taken", taken);
                                                            i.putExtra("bookname", ans);
                                                            startActivity(i);

                                                        } else {
                                                            new AlertDialog.Builder(BookDetail.this)
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
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }
    }
}
