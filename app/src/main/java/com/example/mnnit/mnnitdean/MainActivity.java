package com.example.mnnit.mnnitdean;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void buttonClicked(View v){
        finish();
        SharedPreferences sharedPreferences = getSharedPreferences("loginType", 0);
        String logintype = sharedPreferences.getString("logintype", "N/A");
        if (logintype.equals("N/A"))
            startActivity(new Intent(this, loginType.class));
//        else if(logintype.equals("Teacher"))
//            startActivity(new Intent(this,teacherLogin.class));
//        else if(logintype.equals("Student"))
//            startActivity(new Intent(this,studentLogin.class));

    }



    //        SharedPreferences.Editor editor=sharedPreferences.edit();


}
