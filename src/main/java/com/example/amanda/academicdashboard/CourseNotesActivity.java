package com.example.amanda.academicdashboard;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.net.URI;

public class CourseNotesActivity extends AppCompatActivity {

    EditText etCourseNotes;
    String courseNotes = "";
    long courseID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etCourseNotes = (EditText) findViewById(R.id.etCourseNotes);

        Intent intent = getIntent();

        courseNotes = intent.getStringExtra("courseNotes");
        courseID = intent.getLongExtra("courseID", 0);

        etCourseNotes.setText(courseNotes);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //   intent.putExtra("URI", courseURI);
                String notes = etCourseNotes.getText().toString();
                saveNotes(notes);
                intent.setClass(CourseNotesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_delete_share_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save_notes) {

            saveNotes(etCourseNotes.getText().toString());
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }

        else if(id == R.id.action_delete_notes){
            deleteNotes(courseID);
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }

        else if(id==R.id.action_email_notes){
            String notes = etCourseNotes.getText().toString();
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), EmailNotesActivity.class);
            intent.putExtra("courseID",courseID);
            intent.putExtra("courseNotes", notes);
            startActivity(intent);
            return true;

        }


        return super.onOptionsItemSelected(item);
    }

    private void deleteNotes(long courseID) {
        DataProvider.CONTENT_ITEM_TYPE="courses";
        String whereClause = DBOpenHelper.TERM_ID + " = " + courseID;

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NOTES, "");

        int updatedRows = getContentResolver().update(DataProvider.CONTENT_URI_COURSES, values, whereClause,null);

       // int deletedRows = getContentResolver().delete(DataProvider.CONTENT_URI_COURSES, whereClause, null);
        Intent intent = new Intent();
        intent.setClass(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }



    private void saveNotes(String notes) {
        DataProvider.CONTENT_ITEM_TYPE="courses";

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NOTES, notes);

        String whereClause = DBOpenHelper.COURSE_ID + " = " + courseID;

        int updatedRows = getContentResolver().update(DataProvider.CONTENT_URI_COURSES,values, whereClause, null);

    }

}
