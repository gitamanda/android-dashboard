package com.example.amanda.academicdashboard;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.SimpleCursorAdapter;

public class EditTermsActivity extends AppCompatActivity {

    //private DatePicker dpStart, dpEnd;
    private EditText etTermTitle, etStartDate, etEndDate;
    long termID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_terms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        termID = intent.getLongExtra("termID", 0);

        etTermTitle = (EditText)findViewById(R.id.etTermTitle);
        etEndDate = (EditText) findViewById(R.id.etEnd);
        etStartDate = (EditText) findViewById(R.id.etStart);

        DataProvider.CONTENT_ITEM_TYPE="terms";

        String selection = DBOpenHelper.TERM_ID + " = " +termID;

        Cursor cursor = getContentResolver().query(DataProvider.CONTENT_URI_TERMS,DBOpenHelper.ALL_COLUMNS_TERMS,selection,null, null );
        cursor.moveToFirst();
        String start = cursor.getString(2);
        String end = cursor.getString(3);
        String termName = cursor.getString(1);
        etTermTitle.setText(termName);
        etEndDate.setText(end);
        etStartDate.setText(start);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String startDateString = etStartDate.getText().toString();
                String endDateString = etEndDate.getText().toString();
                String termTitle = etTermTitle.getText().toString();
                updateTerm(startDateString,endDateString,termTitle);
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateTerm(String startDateString, String endDateString, String termTitle) {

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.START_DATE,startDateString);
        values.put(DBOpenHelper.END_DATE,endDateString);
        values.put(DBOpenHelper.TERM_NAME, termTitle);

        String whereClause = DBOpenHelper.TERM_ID + " = " + termID;
        int updatedRows = getContentResolver().update(DataProvider.CONTENT_URI_TERMS,values,whereClause,null);

        Intent intent = new Intent(EditTermsActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
