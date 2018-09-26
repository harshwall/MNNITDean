package com.example.mnnit.mnnitdean;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminLogin extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    EditText email,pass;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        auth=FirebaseAuth.getInstance();

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
    public void loginClicked(View v){
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
                                //special flag for admin
                                user=auth.getCurrentUser();
                                databaseReference= FirebaseDatabase.getInstance().getReference().child("Profile:/"+user.getUid());
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        user prin=dataSnapshot.getValue(user.class);
                                        if(prin.flag.toString().equals("2"))
                                        {
                                            progressDialog.dismiss();
                                            finish();
                                            Intent intent=new Intent(adminLogin.this,adminActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),"Admin not Found",Toast.LENGTH_SHORT).show();
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
                    });
        }

    }
}
