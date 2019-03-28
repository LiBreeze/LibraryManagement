package com.example.library;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class AddAccountDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    DatabaseReference UserDetail;
    EditText  acc_name, acc_phoneno;
    FirebaseUser firebaseUser;
    Spinner spinner_course;


    private static final int CHOOSE_IMAGE=101;
    ImageView imageView;
    String profileImageUrl;

    Spinner spinner_stream;
    TextView tvcourse;
    Button acc_button;
    TextView tv;
    String email,password;
    String course="",stream,sapid;
    long phoneno;
    RadioGroup radioGroup;
    RadioButton radioButton;
    ProgressDialog progressDialog;
    Uri uriProfileImage;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {

            uriProfileImage=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                imageView.setImageBitmap(bitmap);

                final StorageReference profileImageRef= FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
                if(uriProfileImage!=null)
                {
                    profileImageRef.putFile(uriProfileImage)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot1) {
                                    profileImageUrl=profileImageRef.getDownloadUrl().toString();
                                    saveUserInformation(uriProfileImage);
                                    Toast.makeText(AddAccountDetails.this, "Added profile picture", Toast.LENGTH_LONG).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        tvcourse=findViewById(R.id.course);
        radioGroup=findViewById(R.id.radioGroup);

        firebaseAuth=FirebaseAuth.getInstance();

        sapid=getIntent().getStringExtra("SAPid");

        email=getIntent().getStringExtra("Email");
        password=getIntent().getStringExtra("Password");

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        imageView=(ImageView)findViewById(R.id.profile_pic);



        database=FirebaseDatabase.getInstance();
        UserDetail=database.getReference("UserDetails");
        //spinner_course = findViewById(R.id.acc_course);
        spinner_stream = findViewById(R.id.acc_stream);
        acc_button = findViewById(R.id.acc_button);

        acc_name=findViewById(R.id.acc_name);
        acc_phoneno=findViewById(R.id.acc_phoneno);

        progressDialog= new ProgressDialog(this);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent,"Select profile image"),CHOOSE_IMAGE);
            }
        });



        acc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkButton(v);
                progressDialog.setMessage("Adding personal details");
                progressDialog.show();
                phoneno=Long.parseLong(acc_phoneno.getText().toString());



                final UserDetails userDetails=new UserDetails(password,email,sapid,acc_name.getText().toString(),course,stream,phoneno,"Teacher",firebaseAuth.getCurrentUser().getUid());



                             UserDetail.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(firebaseUser.getUid()).exists())
                        {
                            Toast.makeText(getApplicationContext(),"User already exists", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                        else
                        {

                            UserDetail.child(firebaseUser.getUid()).setValue(userDetails);
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));


                        }

                     }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Cant add details", Toast.LENGTH_SHORT).show();

                    }


                });
            }
        });

        //ArrayAdapter<CharSequence> adapter_course = ArrayAdapter.createFromResource(this, R.array.Course, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_stream = ArrayAdapter.createFromResource(this, R.array.Stream, android.R.layout.simple_spinner_item);
        //adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_stream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner_course.setAdapter(adapter_course);
        spinner_stream.setAdapter(adapter_stream);
        //spinner_course.setOnItemSelectedListener(this);
        spinner_stream.setOnItemSelectedListener(this);
    }

    private void saveUserInformation(final Uri profileImageUrl) {
        String displayName=acc_name.getText().toString();
        if(displayName.isEmpty())
        {
            acc_name.setError("Name required");
            acc_name.requestFocus();
            return;
        }
        FirebaseUser user=firebaseAuth.getCurrentUser();

        if(user!=null&&profileImageUrl!=null)
        {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(profileImageUrl)
                    .build();
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())

                            {
                                Toast.makeText(AddAccountDetails.this, "Profile updated", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

           stream = parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Nothing selected", Toast.LENGTH_SHORT).show();

    }

    public void checkButton(View v)
    {
        int radioid=radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioid);
        course=radioButton.getText().toString();

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Cannot go back. Please enter details", Toast.LENGTH_SHORT).show();
        //super.onBackPressed();
    }
}
