package com.example.mnnit.mnnitdean;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class studentLogin extends AppCompatActivity {

  TextView textView;
  ImageButton dob;
  Calendar calendar;
  DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        Spinner branch = findViewById(R.id.branch);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.branch, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(adapter);


        textView=(TextView)findViewById(R.id.text3);
        dob=(ImageButton)findViewById(R.id.dob);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);

                dpd=new DatePickerDialog(studentLogin.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                        textView.setText("   DOB:"+mday+"/"+mmonth+1+"/"+myear);

                    }
                },year,month,day);
                dpd.show();
            }
        });




    }




}
