package com.example.mnnit.mnnitdean;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class downstudent extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5,t6;
    ProgressDialog dialog;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downstudent);
        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait ...");
        t1=(TextView)findViewById(R.id.first);
        t2=(TextView)findViewById(R.id.academic);
        t3=(TextView)findViewById(R.id.rule);
        t4=(TextView)findViewById(R.id.performa);
        t5=(TextView)findViewById(R.id.hostel);
        t6=(TextView)findViewById(R.id.notice);
    }


    public void fetch(View v)
    {
        dialog.show();
        name=v.getResources().getResourceName(v.getId());
        name=name.replace("com.example.mnnit.mnnitdean:id/","");
        storageReference.child("Downloads/"+name+".pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
