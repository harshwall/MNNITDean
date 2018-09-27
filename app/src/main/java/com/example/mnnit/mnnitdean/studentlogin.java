package com.example.mnnit.mnnitdean;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class studentlogin extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    URL s;
    ProgressDialog dialog;
    int notice,currNotice;
    TextView t1;
    Button next,prev;


    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlogin);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        auth=FirebaseAuth.getInstance();
        t1=(TextView)findViewById(R.id.no);
        next=(Button)findViewById(R.id.next);
        prev=(Button)findViewById(R.id.prev);
        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        dialog.show();
        databaseReference.child("Notice").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notice n=dataSnapshot.getValue(notice.class);
                notice=Integer.parseInt(n.notice);
                currNotice=notice;
                t1.setText("The current notice number is: "+currNotice);
                prev.setEnabled(false);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        menuItem.setChecked(false);
                        int id=menuItem.getItemId();
                        switch (id){
                            case R.id.profile:
                                final Intent intent=new Intent(studentlogin.this,profileStudent.class);
                                startActivity(intent);
                                break;
                            case R.id.calendar:
                                dialog.show();
                                storageReference.child("AcademicCalander/academiccalender.pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Intent io=new Intent(Intent.ACTION_VIEW);
                                        io.setDataAndType(uri,"application/pdf");
                                        io.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(io);
                                        dialog.dismiss();
                                    }
                                });

                                break;
                            case R.id.downloads:
                                Intent dow=new Intent(studentlogin.this,downstudent.class);
                                startActivity(dow);
                                break;
                            case R.id.fee:
                                dialog.show();
                                storageReference.child("Fees/fees.pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Intent io=new Intent(Intent.ACTION_VIEW);
                                        io.setDataAndType(uri,"application/pdf");
                                        io.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(io);
                                        dialog.dismiss();
                                    }
                                });
                                break;
                            case R.id.signout:
                                auth.signOut();
                                finish();
                                Intent i=new Intent(studentlogin.this,loginType.class);
                                startActivity(i);
                                break;
                        }

                        return true;
                    }
                }
        );
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
//pop up back press for sign out
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder altdial=new AlertDialog.Builder(studentlogin.this);
        altdial.setMessage("Do you want to quit?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.signOut();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_LONG).show();
                    }
                });

        AlertDialog alert=altdial.create();
        alert.setTitle("Dialog Header");
        alert.show();
    }
//for drawer toggle
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //button press
    public void nextPress(View v){
        if(currNotice>1)
        {
            currNotice--;
            if(currNotice<notice)
                prev.setEnabled(true);
            if(currNotice==1)
                next.setEnabled(false);
        }
        t1.setText("The current notice number is: "+currNotice);



    }
    public void prevPress(View v){
        if(currNotice<notice)
        {
            currNotice++;
            if(currNotice==notice)
                prev.setEnabled(false);
            if(currNotice>1)
                next.setEnabled(true);

        }
        t1.setText("The current notice number is: "+currNotice);

    }

    public void fetch(View v){
        dialog.show();
        storageReference.child("Notice/"+currNotice+".pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Intent io=new Intent(Intent.ACTION_VIEW);
                io.setDataAndType(uri,"application/pdf");
                io.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(io);
                dialog.dismiss();
            }
        });

    }
}
