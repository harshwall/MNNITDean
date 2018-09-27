package com.example.mnnit.mnnitdean;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class forgotpass extends AppCompatActivity {
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    EditText e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        auth=FirebaseAuth.getInstance();
        e=(EditText)findViewById(R.id.email);
        progressDialog=new ProgressDialog(this);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Bundle extras=getIntent().getExtras();
        e.setText(extras.getString("email"));
    }
    public void resetclicked(View view){
        String email=e.getText().toString();
        if(email.length()!=0) {
            progressDialog.setMessage("Sending reset request");
            progressDialog.show();
        }
        if(email.length()!=0) {
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Password reset link sent to your email", Toast.LENGTH_LONG).show();
                                finish();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else
            e.setError("Enter email");


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
