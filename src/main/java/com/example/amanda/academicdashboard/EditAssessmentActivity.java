package com.example.amanda.academicdashboard;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EditAssessmentActivity extends AppCompatActivity {

    ListView listView;
    EditText etName;
    Spinner spinnerAssessmentType;
    TextView tvDueDate;
    long assessmentID;
    String dueDate, assessmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);

        Intent intent = getIntent();
        dueDate = intent.getStringExtra("dueDate");
        assessmentID = intent.getLongExtra("assessmentID",0);
        assessmentName = intent.getStringExtra("assessmentName");

        //String dueDate = getDueDate(courseID);

        List<String> types = new ArrayList<>();
        types.add("Objective");
        types.add("Performance");

        listView = (ListView) findViewById(R.id.lvAssessments);

        tvDueDate = (TextView) findViewById(R.id.tvDueDateDisplay);

        spinnerAssessmentType = (Spinner) findViewById(R.id.spinnerAssessmentType);

        etName = (EditText) findViewById(R.id.etAssessmentName);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);

        spinnerAssessmentType.setAdapter(dataAdapter);

        tvDueDate.setText(dueDate);

        etName.setText(assessmentName);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String assessmentName = etName.getText().toString();
                String assessmentType = spinnerAssessmentType.getSelectedItem().toString();
                updateAssessment(assessmentName, assessmentType);
            }
        });
    }


    public void updateAssessment(String assessmentName,  String assessmentType){

        DataProvider.CONTENT_ITEM_TYPE = "assessments";
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_NAME, assessmentName);
        values.put(DBOpenHelper.ASSESSMENT_TYPE, assessmentType);

        String whereClause = DBOpenHelper.ASSESSMENT_ID + " = " + assessmentID;
        try {
            int updatedRows = getContentResolver().update(DataProvider.CONTENT_URI_ASSESSMENTS, values, whereClause, null);
        }
        catch (Exception e){
            throw e;
        }
        Intent intent = new Intent();
        intent.setClass(EditAssessmentActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
