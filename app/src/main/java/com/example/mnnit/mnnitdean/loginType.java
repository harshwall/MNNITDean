package com.example.mnnit.mnnitdean;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class loginType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_type);
    }

    public void loginclicked(View v){

    }
    public void signupclicked(View v){
        Intent intent=new Intent(this,signup.class);
        startActivity(intent);

    }


}
