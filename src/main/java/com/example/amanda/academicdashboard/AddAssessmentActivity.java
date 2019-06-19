package com.example.amanda.academicdashboard;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddAssessmentActivity extends AppCompatActivity {

    ListView listView;
    EditText etName;
    Spinner spinnerAssessmentType;
    TextView tvDueDate;
    Button btnScheduleNotification;
    DatePicker dpGoalDate;
    long courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        Intent intent = getIntent();
        courseID = intent.getLongExtra("courseID", 0);

        String dueDate = getDueDate(courseID);

        List<String> types = new ArrayList<>();
        types.add("Objective");
        types.add("Performance");

        listView = (ListView) findViewById(R.id.lvAssessments);

        tvDueDate = (TextView) findViewById(R.id.tvDueDateDisplay);

        spinnerAssessmentType = (Spinner) findViewById(R.id.spinnerAssessmentType);

        etName = (EditText) findViewById(R.id.etAssessmentName);

        btnScheduleNotification = (Button) findViewById(R.id.btnSetGoalDate);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);

        spinnerAssessmentType.setAdapter(dataAdapter);

        dpGoalDate = (DatePicker) findViewById(R.id.dpGoalDate);

        tvDueDate.setText(dueDate);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String assessmentName = etName.getText().toString();
                String assessmentType = spinnerAssessmentType.getSelectedItem().toString();
                insertNewAssessment(assessmentName, courseID, assessmentType);

            }
        });

        btnScheduleNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleGoalNotification();
            }
        });
    }

    public String getDueDate(long courseID){

        DataProvider.CONTENT_ITEM_TYPE = "courses";
        String selection = DBOpenHelper.COURSE_ID + "=" + courseID;
        String [] columns = {DBOpenHelper.COURSE_END_DATE};
        Cursor dueDateCursor = getContentResolver().query(DataProvider.CONTENT_URI_COURSES, columns, selection, null, null);
        DataProvider.CONTENT_ITEM_TYPE = "assessments";
        String temp="";
        dueDateCursor.moveToFirst();
        try{temp= dueDateCursor.getString(0);}
        catch (Exception e){
            throw e;
        }
        return temp;
    }


    public void insertNewAssessment(String assessmentName, long assessmentCourseID, String assessmentType){

        String selection = DBOpenHelper.ASSESSMENT_COURSE_ID + " = " + courseID;
        Cursor cursor = getContentResolver().query(DataProvider.CONTENT_URI_ASSESSMENTS, DBOpenHelper.ALL_COLUMNS_ASSESSMENTS, selection, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()<5) {

            ContentValues values = new ContentValues();
            values.put(DBOpenHelper.ASSESSMENT_COURSE_ID, assessmentCourseID);
            values.put(DBOpenHelper.ASSESSMENT_NAME, assessmentName);
            values.put(DBOpenHelper.ASSESSMENT_TYPE, assessmentType);

            Uri URI = getContentResolver().insert(DataProvider.CONTENT_URI_ASSESSMENTS, values);
            String temp = URI.getLastPathSegment();
            Log.d("AddAssessmentActivity", "Data inserted: " + URI.getLastPathSegment());
            btnScheduleNotification.setEnabled(true);

            Toast.makeText(this, "Assessment saved.", Toast.LENGTH_LONG).show();
            //Intent intent = new Intent();
            //intent.setClass(AddAssessmentActivity.this, MainActivity.class);
            //startActivity(intent);
        }
        else {
            Toast.makeText(this, "Unable to save: Maximum number of assessments reached (5).", Toast.LENGTH_LONG).show();
        }

    }

    public void scheduleGoalNotification(){

       int goalMo, goalYr, goalDay;
       goalDay=dpGoalDate.getDayOfMonth();
       goalMo=dpGoalDate.getMonth();
       goalYr=dpGoalDate.getYear();
       String goalDate = goalMo + "/" + goalDay + "/" + goalYr;
       java.util.Date date;
       long millis = 0;
       try {
         //date = java.sql.Date.valueOf(goalDate);//this is the crashing line
           Calendar calendar = Calendar.getInstance();
           calendar.set(goalYr,goalMo,goalDay);
           millis = calendar.getTimeInMillis();
       }
       catch (Exception e){
           String error = e.getMessage();
       }


        //long millis = System.currentTimeMillis();
        Intent intent = new Intent(AddAssessmentActivity.this, MyReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(AddAssessmentActivity.this, 0, intent, 0 );
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,millis,sender);

        Toast.makeText(this,"Assessment saved.", Toast.LENGTH_LONG);
        Intent i = new Intent();
        i.setClass(AddAssessmentActivity.this, MainActivity.class);
        startActivity(i);

    }
}
