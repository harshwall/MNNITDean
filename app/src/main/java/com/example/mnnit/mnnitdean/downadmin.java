package com.example.mnnit.mnnitdean;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class downadmin extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5,t6;
    ProgressDialog dialog;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Uri uri;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downadmin);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        storageReference= FirebaseStorage.getInstance().getReference().child("Downloads");
        databaseReference= FirebaseDatabase.getInstance().getReference();
        t1=(TextView)findViewById(R.id.first);
        t2=(TextView)findViewById(R.id.academic);
        t3=(TextView)findViewById(R.id.rule);
        t4=(TextView)findViewById(R.id.performa);
        t5=(TextView)findViewById(R.id.hostel);
        t6=(TextView)findViewById(R.id.notice);
    }

    public void openGallery(View v)
    {
        name=v.getResources().getResourceName(v.getId());
        name=name.replace("com.example.mnnit.mnnitdean:id/","");
        Intent i = new Intent();
        i.setType("application/pdf");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==10&&resultCode==RESULT_OK&&data!=null)
        {
            uri=data.getData();
            StorageReference myref=storageReference.child(name+".pdf");
            dialog.setMessage("Uploading File");
            dialog.show();
            myref.putFile(uri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
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


}
