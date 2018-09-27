package com.example.mnnit.mnnitdean;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread background=new Thread(){
            public void run(){
                try{
                    Intent intent=new Intent(getBaseContext(),loginType.class);
                    sleep(1000);
                    finish();
                    startActivity(intent);
                }catch (Exception e){

                }
            }

        };
        background.start();


    }



//        if (logintype.equals("N/A"))
//            startActivity(new Intent(this, login.class));
//        else if(logintype.equals("Teacher"))
//            startActivity(new Intent(this,teacherLogin.class));
//        else if(logintype.equals("Student"))
//            startActivity(new Intent(this,studentLogin.class));






    //        SharedPreferences.Editor editor=sharedPreferences.edit();


}
