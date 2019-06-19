package com.example.amanda.academicdashboard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddCourseActivity extends AppCompatActivity {

    Spinner spinnerStatus;
    DatePicker dpStart, dpEnd;
    EditText etCourseName, etInstructorName, etPhone, etEmail;
    long termID;
    Button btnScheduleNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        List<String> statuses = new ArrayList<>();
        statuses.add("In Progress");
        statuses.add("Completed");
        statuses.add("Dropped");
        statuses.add("Plan to Take");

        DataProvider.CONTENT_ITEM_TYPE="courses";


        spinnerStatus = (Spinner)findViewById(R.id.spinnerCourseStatus);
        dpStart = (DatePicker) findViewById(R.id.dpStart);
        dpEnd = (DatePicker) findViewById(R.id.dpEnd);
        etCourseName = (EditText) findViewById(R.id.etCourseTitle);
        etInstructorName = (EditText) findViewById(R.id.etInstructorName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        btnScheduleNotification = (Button) findViewById(R.id.btnSchedule);

        Intent i = getIntent();
        termID = i.getLongExtra("termID",0);
      //  final String termNO = termURI.getLastPathSegment();
      //  final int termID = Integer.parseInt(termNO);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statuses);

       spinnerStatus.setAdapter(dataAdapter);

       btnScheduleNotification.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               int startMo, startYr, startDay;
               startDay=dpStart.getDayOfMonth();
               startMo=dpStart.getMonth();
               startYr=dpStart.getYear();
               scheduleGoalNotification(startMo, startDay, startYr);

               int endMo, endYr, endDay;
               endDay = dpEnd.getDayOfMonth();
               endMo = dpEnd.getMonth();
               endYr = dpEnd.getYear();
               scheduleGoalNotification(endMo, endDay, endYr);

               Intent intent = new Intent();
               intent.setClass(AddCourseActivity.this,MainActivity.class);
               startActivity(intent);
           }
       });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status = spinnerStatus.getSelectedItem().toString();
                String courseName = etCourseName.getText().toString();
                int startMo, startYr, startDay;
                startDay=dpStart.getDayOfMonth();
                startMo=dpStart.getMonth()+1;
                startYr=dpStart.getYear();
                String startDate = startMo + "/" + startDay + "/" + startYr;
                int endMo, endYr, endDay;
                endDay = dpEnd.getDayOfMonth();
                endMo = dpEnd.getMonth()+1;
                endYr = dpEnd.getYear();
                String endDate = endMo + "/" + endDay + "/" +endYr;
                String courseMentorName = etInstructorName.getText().toString();
                String courseMentorPhone = etPhone.getText().toString();
                String courseMentorEmail = etEmail.getText().toString();

                insertNewCourse(startDate,endDate,courseName,"",termID, status, courseMentorName, courseMentorPhone, courseMentorEmail);//TODO: remove hardcoded values

                btnScheduleNotification.setEnabled(true);
            }
        });

    }

    public void insertNewCourse(String startDate, String endDate, String courseName, String notes, long termID, String status, String mentorName, String mentorPhone, String mentorEmail){

        DataProvider.CONTENT_ITEM_TYPE= "courses";

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_START_DATE,startDate);
        values.put(DBOpenHelper.COURSE_END_DATE,endDate);
        values.put(DBOpenHelper.COURSE_NAME, courseName);
        values.put(DBOpenHelper.COURSE_NOTES, notes);
        values.put(DBOpenHelper.COURSE_TERM_ID, termID);
        values.put(DBOpenHelper.COURSE_STATUS, status);
        values.put(DBOpenHelper.COURSE_MENTOR_NAME, mentorName);
        values.put(DBOpenHelper.COURSE_MENTOR_EMAIL, mentorEmail);
        values.put(DBOpenHelper.COURSE_MENTOR_PHONE, mentorPhone);

        Uri URI;
        try{URI = getContentResolver().insert(DataProvider.CONTENT_URI_COURSES, values);}
        catch (Exception e){
            throw e;
        }


        String temp = URI.getLastPathSegment();
        Log.d("AddCourseActivity", "Data inserted: " + URI.getLastPathSegment());
    }

    public void scheduleGoalNotification(int month, int dayOfMonth, int year){


        java.util.Date date;
        long millis = 0;
        try {
            //date = java.sql.Date.valueOf(goalDate);//this is the crashing line
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,dayOfMonth);
            millis = calendar.getTimeInMillis();
        }
        catch (Exception e){
            String error = e.getMessage();
        }


        //long millis = System.currentTimeMillis();
        Intent intent = new Intent(AddCourseActivity.this, MyReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(AddCourseActivity.this, 0, intent, 0 );
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,millis,sender);

        Intent i = new Intent();
        i.setClass(AddCourseActivity.this, MainActivity.class);
        startActivity(i);

    }
}
