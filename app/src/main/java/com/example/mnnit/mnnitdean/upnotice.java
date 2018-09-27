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
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class upnotice extends AppCompatActivity {

    StorageReference storageReference;
    FirebaseUser user;
    FirebaseAuth auth;
    ProgressDialog dialog;
    Uri pdfUri;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upnotice);
        //init variables
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference().child("Notice");
        dialog=new ProgressDialog(this);
        webView=(WebView)findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);
        String pdf= "https://firebasestorage.googleapis.com/v0/b/mnnitdean-b7f65.appspot.com/o/AcademicCalander%2Facademiccalender.pdf?alt=media&token=3a1c081f-0cef-49d3-b1c0-3f4664499efc.pdf";
        webView.loadUrl(pdf);
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
