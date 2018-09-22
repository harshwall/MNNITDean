package com.example.mnnit.mnnitdean;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class login extends Activity {
    FirebaseAuth auth;
    FirebaseUser user;
    EditText email,pass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();

        email=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.pass);
        progressDialog=new ProgressDialog(this);
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
                                progressDialog.dismiss();
                                finish();
                                Intent intent=new Intent(login.this,studentlogin.class);
                                startActivity(intent);
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
    public void signupclicked(View v){
        Intent intent=new Intent(login.this,signup.class);
        startActivity(intent);

    }
    public void forgotpassclicked(View v){

        Intent intent=new Intent(this,forgotpass.class);
        //intent.putExtra("email",email.getText().toString());
        intent.putExtra("id",email.getText().toString());
        startActivity(intent);
    }


}
