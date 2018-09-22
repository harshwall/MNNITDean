package com.example.mnnit.mnnitdean;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

    EditText e1,e2,e3,e4,e5;


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
                        textView.setText("Date of BIrth:"+mday+"/"+(mmonth+1)+"/"+myear);

                    }
                },year,month,day);
                dpd.show();
            }
        });


        auth=FirebaseAuth.getInstance();
        rootreference= FirebaseDatabase.getInstance().getReference();
        e1=(EditText)findViewById(R.id.name);
        e2=(EditText)findViewById(R.id.father);
        e3=(EditText)findViewById(R.id.regno);
        e4=(EditText)findViewById(R.id.email);
        e5=(EditText)findViewById(R.id.pass);

    }

    public void createUser(View v){
        final String name=e1.getText().toString();
        final String fathername=e2.getText().toString();
        final String regno=e3.getText().toString();
        final String email=e4.getText().toString();
        final String password=e5.getText().toString();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            user=auth.getCurrentUser();
                            user myuser=new user(e1.getText().toString(),e2.getText().toString(),e3.getText().toString(),e4.getText().toString());
                            rootreference.child(user.getUid()).setValue(myuser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(getApplicationContext(),"DONE",Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                        }
                    }
                });

    }

}
