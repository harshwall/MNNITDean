package com.example.mnnit.mnnitdean;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class upacademic extends AppCompatActivity {

    StorageReference storageReference;
    ProgressDialog dialog;
    Uri pdfUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upacademic);
        storageReference= FirebaseStorage.getInstance().getReference().child("AcademicCalander");
        dialog=new ProgressDialog(this);
    }

    public void openGallery(View v) {
        Intent i = new Intent();
        i.setType("application/pdf");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==10&&resultCode==RESULT_OK&&data!=null)
        {
            pdfUri=data.getData();
            StorageReference myref=storageReference.child("abc"+".pdf");
            dialog.setMessage("Uploading File");
            dialog.show();
            myref.putFile(pdfUri)
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
