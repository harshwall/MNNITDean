package com.example.mnnit.mnnitdean;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class profileStudent extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    StorageReference storageReference,pathreference;
    ImageView il;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_student);

        il=(ImageView)findViewById(R.id.pic);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Profile:/"+user.getUid());
        storageReference= FirebaseStorage.getInstance().getReference().child("Image");
        pathreference=storageReference.child("Image/"+user.getUid()+".jpg");
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
}
