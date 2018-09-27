package com.example.mnnit.mnnitdean;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class resultStudent extends AppCompatActivity {
    DatabaseReference rootReference;
    FirebaseAuth auth;
    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10;
    ProgressDialog dialog;
    GraphView graphView;
    int spi1,spi2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_student);
        auth=FirebaseAuth.getInstance();
        rootReference=FirebaseDatabase.getInstance().getReference();
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait");
        dialog.show();
        t1=(TextView)findViewById(R.id.maths);
        t2=(TextView)findViewById(R.id.phy);
        t3=(TextView)findViewById(R.id.chem);
        t4=(TextView)findViewById(R.id.work);
        t5=(TextView)findViewById(R.id.mech);
        t6=(TextView)findViewById(R.id.eco);
        t7=(TextView)findViewById(R.id.english);
        t8=(TextView)findViewById(R.id.comm);
        t9=(TextView)findViewById(R.id.spi1);
        t10=(TextView)findViewById(R.id.spi2);
        graphView=(GraphView)findViewById(R.id.graph);
        rootReference.child("Result/Result1:/"+auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                result1 r1=dataSnapshot.getValue(result1.class);
                t1.setText(r1.maths);
                t2.setText(r1.physics);
                t3.setText(r1.chemistry);
                t4.setText(r1.workshop);
                spi1=(int)((Integer.parseInt(r1.maths)*4+Integer.parseInt(r1.workshop)*2+Integer.parseInt(r1.chemistry)*3+Integer.parseInt(r1.physics)*4)/13);
                String s=Integer.toString(spi1);
                t9.setText("Average Marks: "+s);
                rootReference.child("Result/Result2:/"+auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        result2 r2=dataSnapshot.getValue(result2.class);
                        t5.setText(r2.mechanics);
                        t6.setText(r2.ecology);
                        t7.setText(r2.english);
                        t8.setText(r2.communication);
                        spi2=(int)(Integer.parseInt(r2.mechanics)*4+Integer.parseInt(r2.ecology)*2+Integer.parseInt(r2.english)*3+Integer.parseInt(r2.communication)*2)/11;
                        String l=Integer.toString(spi2);
                        t10.setText("Average Marks: "+l);
                        LineGraphSeries<DataPoint> series=new LineGraphSeries<DataPoint>(
                                new DataPoint[]{
                                        new DataPoint(1,spi1),
                                        new DataPoint(2,spi2)
                                }
                        );
                        graphView.addSeries(series);
                        dialog.dismiss();
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
}
