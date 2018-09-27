package com.example.mnnit.mnnitdean;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.Random;

public class upnotice extends AppCompatActivity {

    StorageReference storageReference;
    FirebaseUser user;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    ProgressDialog dialog;
    Uri pdfUri;
    int l;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upnotice);
        //init variables
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference().child("Notice");
        databaseReference=FirebaseDatabase.getInstance().getReference();
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        dialog=new ProgressDialog(this);
    }


    public void openGallery(View v) {
        databaseReference.child("Notice").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notice n=dataSnapshot.getValue(notice.class);
                l=Integer.parseInt(n.notice);
                l++;
                Intent i = new Intent();
                i.setType("application/pdf");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, 10);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==10&&resultCode==RESULT_OK&&data!=null)
        {
            pdfUri=data.getData();
            StorageReference myref=storageReference.child(Integer.toString(l)+".pdf");
            dialog.setMessage("Uploading File");
            dialog.show();
            myref.putFile(pdfUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                databaseReference.child("Notice").child("notice").setValue(Integer.toString(l));
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),"File Uploaded Successfully",Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(),"Please Select a file",Toast.LENGTH_SHORT).show();
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
