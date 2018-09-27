package com.example.mnnit.mnnitdean;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;

public class admnotice extends AppCompatActivity {

    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog dialog;
    int notice,currNotice;
    TextView t1;
    Button next,prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admnotice);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        t1=(TextView)findViewById(R.id.no);
        next=(Button)findViewById(R.id.next);
        prev=(Button)findViewById(R.id.prev);
        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        dialog.show();
        databaseReference.child("Notice").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notice n=dataSnapshot.getValue(notice.class);
                notice=Integer.parseInt(n.notice);
                currNotice=notice;
                t1.setText("The current notice number is: "+currNotice);
                prev.setEnabled(false);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //button press
    public void nextPress(View v){
        if(currNotice>1)
        {
            currNotice--;
            if(currNotice<notice)
                prev.setEnabled(true);
            if(currNotice==1)
                next.setEnabled(false);
        }
        t1.setText("The current notice number is: "+currNotice);



    }
    public void prevPress(View v){
        if(currNotice<notice)
        {
            currNotice++;
            if(currNotice==notice)
                prev.setEnabled(false);
            if(currNotice>1)
                next.setEnabled(true);

        }
        t1.setText("The current notice number is: "+currNotice);

    }

    public void fetch(View v){
        dialog.show();
        storageReference.child("Notice/"+currNotice+".pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Intent io=new Intent(Intent.ACTION_VIEW);
                io.setDataAndType(uri,"application/pdf");
                io.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(io);
                dialog.dismiss();
            }
        });

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
