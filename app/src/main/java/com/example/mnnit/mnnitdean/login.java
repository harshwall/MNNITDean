package com.example.mnnit.mnnitdean;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends Activity {
    FirebaseAuth auth;
    FirebaseUser user;
    EditText email,pass;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference();

        email=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.pass);
        progressDialog=new ProgressDialog(this);
        button=(ImageButton)findViewById(R.id.hide);


        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        pass.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        pass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });

    }
    public void loginclicked(View v){
        if(email.getText().toString().equals("")||pass.getText().toString().equals(""))
        {
            if(email.getText().toString().equals(""))
                email.setError("Enter email");
            if(pass.getText().toString().equals(""))
                pass.setError("Enter password");
        }
        else
        {
            progressDialog.setMessage("Logging in");
            progressDialog.show();
            auth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                databaseReference.child("Profile:/"+user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        user prin=dataSnapshot.getValue(user.class);
                                        if(prin.flag.equals("-1"))
                                        {
                                            Toast.makeText(getApplicationContext(),"Admin has not yet approved your request",Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                        else if(prin.flag.equals("0"))
                                        {
                                            progressDialog.dismiss();
                                            Intent intent=new Intent(login.this,signupflag.class);
                                            startActivity(intent);
                                        }
                                        else if(prin.flag.equals("1"))
                                        {

                                            progressDialog.dismiss();
                                            Intent intent=new Intent(login.this,studentlogin.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),"User not found...",Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"User not found",Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                }
            });
        }

    }
    public void signupclicked(View v){
        Intent intent=new Intent(login.this,signup.class);
        startActivity(intent);

    }
    public void forgotpassclicked(View v){

        Intent intent=new Intent(this,forgotpass.class);
        intent.putExtra("email",email.getText().toString());
        startActivity(intent);
    }




}
