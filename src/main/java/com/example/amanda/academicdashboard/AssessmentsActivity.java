package com.example.amanda.academicdashboard;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AssessmentsActivity extends AppCompatActivity {

    ListView listView;

    TextView tvCourseName, tvStartDate, tvEndDate, tvCourseStatus, tvCourseMentorName, tvCourseMentorPhone, tvCourseMentorEmail;
    Button btnNotes;

    String assessmentDueDate;
    Long courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DataProvider.CONTENT_ITEM_TYPE="assessments";
        listView = (ListView)findViewById(R.id.lvAssessments);
        tvCourseName= (TextView)findViewById(R.id.tvCourseNameDisplay);
        tvStartDate = (TextView)findViewById(R.id.tvStartDateDisplay);
        tvEndDate = (TextView)findViewById(R.id.tvEndDateDisplay);
        tvCourseStatus = (TextView) findViewById(R.id.tvStatusDisplay);
        tvCourseMentorName = (TextView) findViewById(R.id.tvMentorNameDisplay) ;
        tvCourseMentorEmail = (TextView) findViewById(R.id.tvMentorEmailDisplay);
        tvCourseMentorPhone = (TextView) findViewById(R.id.tvMentorPhoneDisplay);
        btnNotes = (Button)findViewById(R.id.btnNotes);


        Intent i = getIntent();

        courseID = i.getLongExtra("courseID",0);

        String selection = DBOpenHelper.ASSESSMENT_COURSE_ID + " = " +courseID;
        final Uri assessmentURI = DataProvider.CONTENT_URI_ASSESSMENTS;//.withAppendedPath(DataProvider.CONTENT_URI_COURSES,courseTermID);//TODO: fix this logic

        String [] from ={DBOpenHelper.ASSESSMENT_NAME};
        int [] to = {R.id.tvListViewItem};
        ContentResolver resolver = getContentResolver();

        Cursor cursor = resolver.query(assessmentURI, DBOpenHelper.ALL_COLUMNS_ASSESSMENTS, selection,null, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.listview_item,cursor,from, to, 0);

        listView.setAdapter(adapter);


        DataProvider.CONTENT_ITEM_TYPE="courses";

        ContentResolver resolver1 = getContentResolver();

        selection = DBOpenHelper.COURSE_ID + " = " + courseID;

        String selection1 = DBOpenHelper.COURSE_ID + " = " + courseID;
        Cursor cursor1;

        try {
            cursor1 = resolver1.query(DataProvider.CONTENT_URI_COURSES, DBOpenHelper.ALL_COLUMNS_COURSES, selection1, null, null);
        }
        catch (Exception e){
            throw e;
        }

        DataProvider.CONTENT_ITEM_TYPE="courses";
        //String mentorSelection = DBOpenHelper.MENTOR_ID = DBOpenHelper.COURSE_MENTOR_ID;

        //COURSE_TERM_ID, COURSE_ID, COURSE_START_DATE, COURSE_END_DATE, COURSE_NAME, COURSE_NOTES, COURSE_STATUS, COURSE_MENTOR_NAME, COURSE_MENTOR_EMAIL, COURSE_MENTOR_PHONE
        final String fromExtraStartDate, fromExtraEndDate, fromExtraCourseName, fromExtraCourseNotes, fromExtraStatus, fromExtraMentorName, fromExtraEmail, fromExtraPhone;
        cursor1.moveToFirst();
        try{
            fromExtraStartDate = cursor1.getString(2);}
        catch (Exception e){
            throw e;
        }
        fromExtraEndDate = cursor1.getString(3);
        fromExtraCourseName = cursor1.getString(4);
        try{fromExtraCourseNotes = cursor1.getString(5);}
        catch (Exception e)
        {throw e;}
        fromExtraStatus = cursor1.getString(6);
        fromExtraMentorName = cursor1.getString(7);
        fromExtraEmail = cursor1.getString(8);
        fromExtraPhone = cursor1.getString(9);

        

        tvCourseName.setText(fromExtraCourseName);
        tvStartDate.setText(fromExtraStartDate);
        tvEndDate.setText(fromExtraEndDate);
        tvCourseStatus.setText(fromExtraStatus);
        tvCourseMentorName.setText(fromExtraMentorName);
        tvCourseMentorPhone.setText(fromExtraPhone);
        tvCourseMentorEmail.setText(fromExtraEmail);

        assessmentDueDate = fromExtraEndDate;


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), AssessmentDetailActivity.class);
                intent.putExtra("assessmentID", id);
                intent.putExtra("dueDate", assessmentDueDate);
                startActivity(intent);
            }
        });
        DataProvider.CONTENT_ITEM_TYPE="assessments";
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("courseID", courseID);
                intent.setClass(getBaseContext(), AddAssessmentActivity.class);
                startActivity(intent);
            }
        });


        btnNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("courseNotes", fromExtraCourseNotes);
                intent.putExtra("courseID", courseID);
                intent.setClass(getBaseContext(), CourseNotesActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_delete_course, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_course) {

            deleteCourse(courseID);
        }
        else if (id==R.id.action_edit_course){

            Intent intent = new Intent();
            intent.setClass(getBaseContext(), EditCoursesActivity.class);
            intent.putExtra("courseID", courseID);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteCourse(long tempCourseID) {

        DataProvider.CONTENT_ITEM_TYPE = "courses";
        String whereClause = DBOpenHelper.COURSE_ID + " = " + tempCourseID;
        try {
            int deletedRows = getContentResolver().delete(DataProvider.CONTENT_URI_COURSES, whereClause, null);
        }
        catch (Exception e){
            throw e;
        }
        Intent intent = new Intent();
        intent.setClass(getBaseContext(), MainActivity.class);
        startActivity(intent);

    }



}
