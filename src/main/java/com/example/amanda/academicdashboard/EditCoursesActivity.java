package com.example.amanda.academicdashboard;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class EditCoursesActivity extends AppCompatActivity {

    Spinner spinnerStatus;
    DatePicker dpStart, dpEnd;
    EditText etCourseName, etStart, etEnd, etMentorName, etStatus, etMentorEmail, etMentorPhone;
    Button btnNotes;
    long courseID;
    String courseNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_courses);

        List<String> statuses = new ArrayList<>();
        statuses.add("In Progress");
        statuses.add("Completed");
        statuses.add("Dropped");
        statuses.add("Plan to Take");

        DataProvider.CONTENT_ITEM_TYPE="courses";


        spinnerStatus = (Spinner)findViewById(R.id.spinnerCourseStatus);
        etStart = (EditText) findViewById(R.id.etStart);
        etEnd = (EditText) findViewById(R.id.etEnd);
        etCourseName = (EditText) findViewById(R.id.etCourseTitle);
        etMentorName = (EditText) findViewById(R.id.etInstructor);
        etMentorEmail = (EditText) findViewById(R.id.etEmail);
        etMentorPhone = (EditText) findViewById(R.id.etPhone);
        btnNotes = (Button) findViewById(R.id.btnNotes);


        Intent i = getIntent();
        courseID = i.getLongExtra("courseID",0);
        //  final String termNO = termURI.getLastPathSegment();
        //  final int termID = Integer.parseInt(termNO);
        String whereClause = DBOpenHelper.COURSE_ID + " = " + courseID;

        Cursor cursor = getContentResolver().query(DataProvider.CONTENT_URI_COURSES,DBOpenHelper.ALL_COLUMNS_COURSES,whereClause,null,null);

        cursor.moveToFirst();
        String startDate = cursor.getString(2);
        String endDate = cursor.getString(3);
        String courseName = cursor.getString(4);
        courseNotes = cursor.getString(5);
        String courseStatus = cursor.getString(6);
        String mentorName = cursor.getString(7);
        String mentorPhone = cursor.getString(8);
        final String mentorEmail = cursor.getString(9);

        etStart.setText(startDate);
        etEnd.setText(endDate);
        etCourseName.setText(courseName);
        etMentorName.setText(mentorName);
        etMentorEmail.setText(mentorEmail);
        etMentorPhone.setText(mentorPhone);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statuses);

        spinnerStatus.setAdapter(dataAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status = spinnerStatus.getSelectedItem().toString();
                String courseName = etCourseName.getText().toString();
                String startDate = etStart.getText().toString();
                String endDate = etEnd.getText().toString();
                String name = etMentorName.getText().toString();
                String phone = etMentorPhone.getText().toString();
                String email = etMentorEmail.getText().toString();


                updateCourse(startDate,endDate,courseName, status, name, email, phone);//TODO: remove hardcoded values
                Intent intent = new Intent();
                intent.setClass(EditCoursesActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btnNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditCoursesActivity.this, CourseNotesActivity.class);
                i.putExtra("courseNotes", courseNotes);
                i.putExtra("courseID", courseID);
            }
        });

    }

    private void updateCourse(String startDate, String endDate, String courseName, String status, String mentorName, String mentorEmail, String mentorPhone) {


        DataProvider.CONTENT_ITEM_TYPE="courses";

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_START_DATE,startDate);
        values.put(DBOpenHelper.COURSE_END_DATE,endDate);
        values.put(DBOpenHelper.COURSE_NAME, courseName);


        //values.put(DBOpenHelper.COURSE_TERM_ID, termID);

        String whereClause = DBOpenHelper.COURSE_ID + " = " + courseID;

        int updatedRows = getContentResolver().update(DataProvider.CONTENT_URI_COURSES, values, whereClause,null);
    }

    public void insertNewCourse(String startDate, String endDate, String courseName, String notes, long termID, int courseInstructorID, String status){



        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_START_DATE,startDate);
        values.put(DBOpenHelper.COURSE_END_DATE,endDate);
        values.put(DBOpenHelper.COURSE_NAME, courseName);
        values.put(DBOpenHelper.COURSE_NOTES, notes);
        values.put(DBOpenHelper.COURSE_TERM_ID, termID);

        // values.put(DBOpenHelper.COURSE_INSTRUCTOR_ID, courseInstructorID);

        Uri URI;
        try{URI = getContentResolver().insert(DataProvider.CONTENT_URI_COURSES, values);}
        catch (Exception e){
            throw e;
        }
        String temp = URI.getLastPathSegment();
        Log.d("AddCourseActivity", "Data inserted: " + URI.getLastPathSegment());



        //values.put(DBOpenHelper., noteText);
        //Uri noteUri = getContentResolver().insert(NotesProvider.CONTENT_URI,values);
        //Log.d("MainActivity", "Inserted note "+ noteUri.getLastPathSegment());
    }
}
