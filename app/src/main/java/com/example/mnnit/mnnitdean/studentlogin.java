package com.example.mnnit.mnnitdean;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class studentlogin extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlogin);
        auth=FirebaseAuth.getInstance();
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
                            case R.id.notice:break;
                            case R.id.profile:
                                Intent intent=new Intent(studentlogin.this,profileStudent.class);
                                startActivity(intent);
                                break;
                            case R.id.calendar:break;
                            case R.id.downloads:break;
                            case R.id.fee:break;
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

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
