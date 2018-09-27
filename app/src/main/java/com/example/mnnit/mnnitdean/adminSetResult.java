package com.example.mnnit.mnnitdean;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminSetResult extends AppCompatActivity {
    DatabaseReference rootReference;

    EditText e1,e2,e3,e4,e5,e6,e7,e8,e9;
    Button search,submit;
    String uid;
    int cnt1=1,cnt2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_set_result);
        e1=(EditText)findViewById(R.id.regno);
        e2=(EditText)findViewById(R.id.maths);
        e3=(EditText)findViewById(R.id.phy);
        e4=(EditText)findViewById(R.id.chem);
        e5=(EditText)findViewById(R.id.workshop);
        e6=(EditText)findViewById(R.id.mechanics);
        e7=(EditText)findViewById(R.id.eng);
        e8=(EditText)findViewById(R.id.eco);
        e9=(EditText)findViewById(R.id.comm);
        search=(Button)findViewById(R.id.search);
        submit=(Button)findViewById(R.id.set);
        submit.setEnabled(false);
        rootReference= FirebaseDatabase.getInstance().getReference();

    }
    //fetching and setting data in fields
    public void search(View v){
        if(e1.getText().toString().length()==8)
        {
            rootReference.child("uid:").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    cnt2=(int)dataSnapshot.getChildrenCount();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getKey().toString().equals(e1.getText().toString())) {
                            uid u = snapshot.getValue(uid.class);
                            uid = u.uid;
                            rootReference.child("Result/Result1:/" + uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    result1 r1 = dataSnapshot.getValue(result1.class);
                                    e2.setText(r1.maths);
                                    e3.setText(r1.physics);
                                    e4.setText(r1.chemistry);
                                    e5.setText(r1.workshop);
                                    rootReference.child("Result/Result2:/" + uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            result2 r2 = dataSnapshot.getValue(result2.class);
                                            e6.setText(r2.mechanics);
                                            e7.setText(r2.english);
                                            e8.setText(r2.ecology);
                                            e9.setText(r2.communication);
                                            submit.setEnabled(true);
                                            cnt1=1;
                                            cnt2=0;
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else
                        {
                            cnt1++;
                            if(cnt1==cnt2)
                            {
                                Toast.makeText(getApplicationContext(),"Student not found",Toast.LENGTH_SHORT).show();
                                submit.setEnabled(false);
                                cnt1=1;
                                cnt2=0;
                            }
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"Student not found",Toast.LENGTH_SHORT).show();

                }
            });
        }
        else
            e1.setError("Enter Registration No.");
        cnt1=1;
        cnt2=0;
    }
    //uploading data
    public void setResult(View view) {
        if (Integer.parseInt(e2.getText().toString())<101&&Integer.parseInt(e3.getText().toString())<101&&Integer.parseInt(e4.getText().toString())<101&&Integer.parseInt(e5.getText().toString())<101&&Integer.parseInt(e6.getText().toString())<101&&Integer.parseInt(e7.getText().toString())<101&&Integer.parseInt(e8.getText().toString())<101&&Integer.parseInt(e9.getText().toString())<101) {

            try {
                rootReference.child("Result/Result1:/" + uid).child("maths").setValue(e2.getText().toString());
                rootReference.child("Result/Result1:/" + uid).child("physics").setValue(e3.getText().toString());
                rootReference.child("Result/Result1:/" + uid).child("chemistry").setValue(e4.getText().toString());
                rootReference.child("Result/Result1:/" + uid).child("workshop").setValue(e5.getText().toString());
                rootReference.child("Result/Result2:/" + uid).child("mechanics").setValue(e6.getText().toString());
                rootReference.child("Result/Result2:/" + uid).child("english").setValue(e7.getText().toString());
                rootReference.child("Result/Result2:/" + uid).child("ecology").setValue(e8.getText().toString());
                rootReference.child("Result/Result2:/" + uid).child("communication").setValue(e9.getText().toString());
                submit.setEnabled(false);
                Toast.makeText(getApplicationContext(), "Result uploaded successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            Toast.makeText(getApplicationContext(),"Marks exceed 100",Toast.LENGTH_SHORT).show();

    }
}
