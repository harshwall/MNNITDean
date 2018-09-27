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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class studentlogin extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    WebView webView;
    StorageReference storageReference;
    URL s;
    Uri sak;
    ProgressDialog dialog;


    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlogin);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        auth=FirebaseAuth.getInstance();
        webView=(WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        storageReference= FirebaseStorage.getInstance().getReference();
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
                            case R.id.downloads:break;
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

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
