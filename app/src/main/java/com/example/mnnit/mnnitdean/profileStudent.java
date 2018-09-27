package com.example.mnnit.mnnitdean;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class profileStudent extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    StorageReference storageReference,pathreference;
    ImageView il;
    ProgressDialog dialog;
    Uri imageUri;
    TextView t1,t2,t3,t4,t5,t6;
    EditText mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_student);
        t1=(TextView)findViewById(R.id.name);
        t2=(TextView)findViewById(R.id.regno);
        t3=(TextView)findViewById(R.id.dob);
        t4=(TextView)findViewById(R.id.gender);
        t5=(TextView)findViewById(R.id.email);
        t6=(TextView)findViewById(R.id.fathername);
        mobile=(EditText)findViewById(R.id.mobile);
        il=(ImageView)findViewById(R.id.pic);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //inisializing variables
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Profile:/"+user.getUid());
        storageReference= FirebaseStorage.getInstance().getReference().child("Image");
        pathreference=storageReference.child("Image/"+user.getUid()+".jpg");
        //dialog opens
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.show();

        //fetching data

        new Thread(new Runnable() {
            public void run() {
                // a potentially  time consuming task
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user prin = dataSnapshot.getValue(user.class);
                        t1.setText("Name:  " + prin.name);
                        t2.setText("Registration No:  " + prin.regno);
                        t3.setText("Date of Birth:  " + prin.dob);
                        t4.setText("Gender:  "+prin.gender);
                        t6.setText("Father's Name:  " + prin.fathername);
                        t5.setText("Email:  " + prin.email);
                        mobile.setText(prin.mobile);
                        dialog.dismiss();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }).start();

//fetching image

        storageReference.child(user.getUid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                new imageLoadTask(uri.toString(),il).execute();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Upload an image",Toast.LENGTH_SHORT).show();
            }
        });



    }

    //choosing photo

    public void openGallery(View v)
    {
        Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==12 && resultCode==RESULT_OK && data!=null)
        {
            imageUri=data.getData();
            StorageReference myref=storageReference.child(user.getUid()+".jpg");
            dialog.setMessage("Uploading Image");
            dialog.show();
            myref.putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                dialog.dismiss();
                                il.setImageURI(imageUri);
                                Toast.makeText(getApplicationContext(),"Image Uploaded Successfully",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Uploading Failed",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            dialog.setMessage(Integer.toString((int)progress)+"% Uploaded. Please wait...");
                        }
                    });

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
        }
    }
//submit button

    public void submit(View v){
        String mob=mobile.getText().toString();
        if(mob.length()<10)
            mobile.setError("Enter correct Mobile no.");
        else {
            try {
                dialog.setMessage("Updating mobile no.");
                dialog.show();
                databaseReference.child("mobile").setValue(mob);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Mobile no. updated",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//sets the back button
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }
}
