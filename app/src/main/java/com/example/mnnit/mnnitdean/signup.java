package com.example.mnnit.mnnitdean;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class signup extends Activity {
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference rootreference;
    ProgressDialog progressDialog;

    EditText e1,e2,e3,e4,e5,e6,e7;
    RadioButton male,female,other;


    String dateob,stream,gender;
    TextView textView;
    ImageButton dob;
    Calendar calendar;
    DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//Creates a list of branches using array adapter
        Spinner branch = findViewById(R.id.branch);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.branch, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(adapter);
        stream=branch.getSelectedItem().toString();

        textView=(TextView)findViewById(R.id.text3);
        dob=(ImageButton)findViewById(R.id.dob);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);
//calendar dialog implementation
                dpd=new DatePickerDialog(signup.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                        textView.setText("Date of Birth:"+mday+"/"+(mmonth+1)+"/"+myear);
                        dateob=Integer.toString(mday)+"/"+Integer.toString(mmonth+1)+"/"+Integer.toString(myear);

                    }
                },year,month,day);
                dpd.show();
            }
        });


        //intializing variables
        auth=FirebaseAuth.getInstance();
        rootreference= FirebaseDatabase.getInstance().getReference();
        e1=(EditText)findViewById(R.id.name);
        e2=(EditText)findViewById(R.id.father);
        e3=(EditText)findViewById(R.id.regno);
        e4=(EditText)findViewById(R.id.email);
        e5=(EditText)findViewById(R.id.pass);
        e6=(EditText)findViewById(R.id.pass2);
        e7=(EditText)findViewById(R.id.mobile);
        male=(RadioButton)findViewById(R.id.male);
        female=(RadioButton)findViewById(R.id.female);
        other=(RadioButton)findViewById(R.id.other);
        progressDialog=new ProgressDialog(this);
    }



    //uploading data
    public void createUser(View v) {

        final String email = e4.getText().toString();
        final String password = e5.getText().toString();

        int a=e3.getText().toString().length();
        int b=e7.getText().toString().length();
        //Toast.makeText(getApplicationContext(),a+" "+e3.getText().toString(),Toast.LENGTH_SHORT).show();
        if(male.isChecked())
            gender="Male";
        else if(female.isChecked())
            gender="Female";
        else if(other.isChecked())
            gender="Other";

//check if any field is blank or contains incorrect data
        if(e1.getText().toString().equals("") ||e2.getText().toString().equals("")||textView.getText().toString().equals(" Set Date of Birth")||a<8||e3.getText().toString().equals("")||e4.getText().toString().equals("")||e5.getText().toString().equals("")||e6.getText().toString().equals("")||!(e5.getText().toString().equals(e6.getText().toString()))||!Patterns.EMAIL_ADDRESS.matcher(e4.getText().toString()).matches()||e5.getText().toString().length()<6||e7.getText().toString().equals("")||b<10)
        {
            if(e1.getText().toString().equals(""))
            {
                e1.setError("Enter name");
            }
            if(e2.getText().toString().equals(""))
            {
                e2.setError("Enter Father's name");
            }
            if(e3.getText().toString().equals(""))
            {
                e3.setError("Enter Registration no.");
            }
            if(a<8)
            {
                e3.setError("Length is less than eight");
            }
            if(e4.getText().toString().equals(""))
            {
                e4.setError("Enter email");
            }
            if(e5.getText().toString().equals(""))
            {
                e5.setError("Enter password");
            }
            if(e6.getText().toString().equals(""))
            {
                e6.setError("Confirm password");
            }
            if(textView.getText().toString().equals(" Set Date of Birth"))
            {
                textView.setError("Select Date of Birth");
            }
            if(!(e5.getText().toString().equals(e6.getText().toString())))
            {
                e6.setError("Passwords do not match");
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(e4.getText().toString()).matches())
            {
                e4.setError("Type correct email");
            }
            if(e5.getText().toString().length()<6)
            {
                e5.setError("Password too short");
            }
            if(e7.getText().toString().equals(""))
            {
                e7.setError("Enter Mobile no.");
            }
            if(b<10)
            {
                e7.setError("Mobile no. incomplete");
            }
        }
        else
        {
            progressDialog.setMessage("Registering, Please wait");
            progressDialog.show();

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = auth.getCurrentUser();
                            uid myuid=new uid(user.getUid().toString());
                            user myuser = new user(e1.getText().toString(), e2.getText().toString(), e3.getText().toString(), dateob,stream,e4.getText().toString(),e7.getText().toString(),gender,"-1");
                            rootreference.child("uid:/"+e3.getText().toString()).setValue(myuid);
                            rootreference.child("Profile:/"+user.getUid()).setValue(myuser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Registered. Wait for approval by Admin", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"User cannot be created",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        }
    }

}
