package com.example.mnnit.mnnitdean;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class loginType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_type);
    }

    public void adminClicked(View v){
        Intent intent=new Intent(this,adminLogin.class);
        startActivity(intent);


    }
    public void studentClicked(View v){
        Intent intent=new Intent(this,login.class);
        startActivity(intent);


    }


}
