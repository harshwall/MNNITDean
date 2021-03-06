package com.example.mnnit.mnnitdean;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class adminActivity extends AppCompatActivity {

    DatabaseReference rootreference;
    user prin;
    ArrayList<user> arrayList;
    RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        auth=FirebaseAuth.getInstance();
        mDrawerLayout=(DrawerLayout)findViewById(R.id.admindrawer);
        NavigationView navigationView=findViewById(R.id.nav_admin);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                int id=menuItem.getItemId();
                switch (id){
                    case R.id.notice:
                        Intent admnot=new Intent(adminActivity.this,admnotice.class);
                        startActivity(admnot);
                        break;
                    case R.id.up_notice:
                        Intent intent=new Intent(adminActivity.this,upnotice.class);
                        startActivity(intent);
                        break;
                    case R.id.calendar:
                        Intent no=new Intent(adminActivity.this,upacademic.class);
                        startActivity(no);
                        break;
                    case R.id.downloads:
                        Intent down=new Intent(adminActivity.this,downadmin.class);
                        startActivity(down);
                        break;
                    case R.id.fee:
                        Intent fee=new Intent(adminActivity.this,upfees.class);
                        startActivity(fee);
                        break;
                    case R.id.setResult:
                        Intent setResult=new Intent(adminActivity.this,adminSetResult.class);
                        startActivity(setResult);
                        break;
                    case R.id.signout:
                        auth.signOut();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        }
                        else
                            finish();
                        System.exit(0);
                        break;
                }

                return true;
            }
        });
        mToggle=new ActionBarDrawerToggle(adminActivity.this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rootreference= FirebaseDatabase.getInstance().getReference();
        rootreference.child("Profile:").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList=new ArrayList<user>();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {

                    prin = snapshot.getValue(user.class);
                    if (prin.flag.equals("-1")) {
                        arrayList.add(prin);
                    }

                }
                recyclerView.setAdapter(new program(arrayList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        if(mToggle.onOptionsItemSelected(menuItem)){
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
    //pop up back press for sign out
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder altdial=new AlertDialog.Builder(adminActivity.this);
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
}
