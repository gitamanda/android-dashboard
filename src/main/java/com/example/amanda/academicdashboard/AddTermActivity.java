package com.example.amanda.academicdashboard;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddTermActivity extends AppCompatActivity {

    private DatePicker dpStart, dpEnd;
    private EditText etTermTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpStart = (DatePicker)findViewById(R.id.dpStart);
                dpEnd = (DatePicker)findViewById(R.id.dpEnd);
                etTermTitle = (EditText)findViewById(R.id.etTermTitle);
                String startDateString = dpStart.getMonth() + "/" + dpStart.getDayOfMonth() + "/" +dpStart.getYear();
                String endDateString = dpEnd.getMonth() + "/" + dpEnd.getDayOfMonth() + "/" + dpEnd.getYear();
                String termTitle = etTermTitle.getText().toString();
                insertNewTerm(startDateString,endDateString,termTitle);
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpStart = (DatePicker)findViewById(R.id.dpStart);
                dpEnd = (DatePicker)findViewById(R.id.dpEnd);
                etTermTitle = (EditText)findViewById(R.id.etTermTitle);
                int startMonth = dpStart.getMonth()+1;
                int endMonth = dpEnd.getMonth()+1;

                String startDateString = startMonth + "/" + dpStart.getDayOfMonth() + "/" +dpStart.getYear();
                String endDateString = endMonth + "/" + dpEnd.getDayOfMonth() + "/" + dpEnd.getYear();
                String termTitle = etTermTitle.getText().toString();
                insertNewTerm(startDateString,endDateString,termTitle);
                Intent intent = new Intent(AddTermActivity.this, MainActivity.class);
            }
        });
    }

    public void insertNewTerm(String startDate, String endDate, String termTitle){


        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.START_DATE,startDate);
        values.put(DBOpenHelper.END_DATE,endDate);
        values.put(DBOpenHelper.TERM_NAME, termTitle);

        Uri URI = getContentResolver().insert(DataProvider.CONTENT_URI_TERMS, values);
        String temp = URI.getLastPathSegment();
        Log.d("AddTermActivity", "Data inserted: " + URI.getLastPathSegment());

        Intent intent = new Intent(AddTermActivity.this, MainActivity.class);
        startActivity(intent);

    }

}
