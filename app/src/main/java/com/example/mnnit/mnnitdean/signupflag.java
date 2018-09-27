package com.example.mnnit.mnnitdean;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class signupflag extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String uid;
    EditText e1,e2,e3,e4,e7;
    RadioButton male,female,other;
    ProgressDialog progressDialog;


    String dateob,stream,gender;
    TextView textView;
    ImageButton dob;
    Calendar calendar;
    DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupflag);
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        user=auth.getCurrentUser();
        uid=user.getUid().toString();
        //Creates a list of branches using array adapter
        Spinner branch = findViewById(R.id.branch);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.branch, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(adapter);
        stream=branch.getSelectedItem().toString();
        dob=(ImageButton)findViewById(R.id.dob);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);
//calendar dialog implementation
                dpd=new DatePickerDialog(signupflag.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                        textView.setText("Date of Birth:"+mday+"/"+(mmonth+1)+"/"+myear);
                        dateob=Integer.toString(mday)+"/"+Integer.toString(mmonth+1)+"/"+Integer.toString(myear);

                    }
                },year,month,day);
                dpd.show();
            }
        });
        //inisializing xml fields
        textView=(TextView)findViewById(R.id.text3);
        e1=(EditText)findViewById(R.id.name);
        e2=(EditText)findViewById(R.id.father);
        e3=(EditText)findViewById(R.id.regno);
        e4=(EditText)findViewById(R.id.email);
        e7=(EditText)findViewById(R.id.mobile);
        male=(RadioButton)findViewById(R.id.male);
        female=(RadioButton)findViewById(R.id.female);
        other=(RadioButton)findViewById(R.id.other);
        progressDialog=new ProgressDialog(this);
        databaseReference.child("Profile:/"+user.getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user prin=dataSnapshot.getValue(user.class);
                e1.setText(prin.name);
                e2.setText(prin.fathername);
                e3.setText(prin.regno);
                e4.setText(prin.email);
                e7.setText(prin.mobile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void resetUser(View v) {

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
        if(e1.getText().toString().equals("") ||e2.getText().toString().equals("")||textView.getText().toString().equals(" Set Date of Birth")||a<8||e3.getText().toString().equals("")||e4.getText().toString().equals("")||!Patterns.EMAIL_ADDRESS.matcher(e4.getText().toString()).matches()||e7.getText().toString().equals("")||b<10)
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
            if(textView.getText().toString().equals(" Set Date of Birth"))
            {
                textView.setError("Select Date of Birth");
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(e4.getText().toString()).matches())
            {
                e4.setError("Type correct email");
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

            try {
                progressDialog.setMessage("Updating details");
                progressDialog.show();
                databaseReference.child("Profile:/"+user.getUid().toString()).child("name").setValue(e1.getText().toString());
                databaseReference.child("Profile:/"+user.getUid().toString()).child("fathername").setValue(e2.getText().toString());
                databaseReference.child("Profile:/"+user.getUid().toString()).child("regno").setValue(e3.getText().toString());
                databaseReference.child("Profile:/"+user.getUid().toString()).child("dob").setValue(dateob);
                databaseReference.child("Profile:/"+user.getUid().toString()).child("branch").setValue(stream);
                databaseReference.child("Profile:/"+user.getUid().toString()).child("email").setValue(e4.getText().toString());
                databaseReference.child("Profile:/"+user.getUid().toString()).child("mobile").setValue(e7.getText().toString());
                databaseReference.child("Profile:/"+user.getUid().toString()).child("gender").setValue(gender);
                databaseReference.child("Profile:/"+user.getUid().toString()).child("flag").setValue("-1");

                progressDialog.dismiss();
                auth.signOut();
                Toast.makeText(getApplicationContext(),"Details uploaded wait for approval by admin",Toast.LENGTH_LONG).show();

            }catch (Exception e) {
                e.printStackTrace();
            }
            finish();



        }
    }
}
