package com.example.amanda.academicdashboard;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.ContentHandler;

public class CoursesActivity extends AppCompatActivity {

    private String action;
    private String termFilter;
    ListView listView;
    SimpleCursorAdapter adapter;
    int REQUEST_CODE = 7007;
    String [] selectionArgs = {};
    SQLiteDatabase db;
    //String termID;
    String courseID;
    TextView tvTermName, tvStartDate, tvEndDate;
    long termID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTermName = (TextView) findViewById(R.id.tvTermNameDisplay);
        tvStartDate = (TextView) findViewById(R.id.tvStartDateDisplay);
        tvEndDate = (TextView) findViewById(R.id.tvEndDateDisplay);
        listView = (ListView) findViewById(R.id.lvCourses);
        String [] from ={DBOpenHelper.COURSE_NAME};
        int [] to = {R.id.tvListViewItem};
        adapter = new SimpleCursorAdapter(this, R.layout.listview_item,null, from, to, 0);

        DataProvider.CONTENT_ITEM_TYPE="courses";


        Intent intent = getIntent();


        termID = intent.getLongExtra("termID",0);
        String selection = DBOpenHelper.COURSE_TERM_ID + " = " +termID;
        final Uri courseURI = DataProvider.CONTENT_URI_COURSES;//.withAppendedPath(DataProvider.CONTENT_URI_COURSES,courseTermID);//TODO: fix this logic

        ContentResolver resolver = getContentResolver();

        Cursor cursor = resolver.query(courseURI, DBOpenHelper.ALL_COLUMNS_COURSES, selection,null, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.listview_item,cursor,from, to, 0);

        listView.setAdapter(adapter);

        DataProvider.CONTENT_ITEM_TYPE="terms";

        ContentResolver resolver1 = getContentResolver();

        selection = DBOpenHelper.TERM_ID + " = " + termID;

        Cursor cursor1;

        try {
            cursor1 = resolver1.query(courseURI, DBOpenHelper.ALL_COLUMNS_TERMS, selection, null, null);
        }
        catch (Exception e){
            throw e;
        }

        String fromExtraStartDate, fromExtraEndDate, fromExtraTermName, fromExtraCourseNotes;
        cursor1.moveToFirst();
        try{
        fromExtraStartDate = cursor1.getString(2);}
        catch (Exception e){
            throw e;
        }
        fromExtraEndDate = cursor1.getString(3);
        fromExtraTermName = cursor1.getString(1);
       // fromExtraCourseNotes = cursor.getString(5);

        tvTermName.setText(fromExtraTermName);
        tvStartDate.setText(fromExtraStartDate);
        tvEndDate.setText(fromExtraEndDate);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
             //   intent.putExtra("URI", courseURI);
                intent.setClass(CoursesActivity.this, AddCourseActivity.class);
                intent.putExtra("termID", termID);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CoursesActivity.this, AssessmentsActivity.class);
                //  Uri uri = Uri.parse(DataProvider.CONTENT_URI_TERMS + "/"+ id);
                intent.putExtra("courseID", id );//TODO: fix this hardcoded value
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_delete_term, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        if (id == R.id.action_delete_term) {

            deleteTerm(termID);
        }
        else if (id==R.id.action_edit_term){

            Intent intent = new Intent();
            intent.setClass(getBaseContext(), EditTermsActivity.class);
            intent.putExtra("termID",termID);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteTerm(long termID) {

        DataProvider.CONTENT_ITEM_TYPE = "courses";

        String whereClause1 = DBOpenHelper.COURSE_TERM_ID + " = " + termID;

        Cursor cursor = getContentResolver().query(DataProvider.CONTENT_URI_COURSES, DBOpenHelper.ALL_COLUMNS_COURSES, whereClause1, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()==0){

        DataProvider.CONTENT_ITEM_TYPE="terms";
        String whereClause = DBOpenHelper.TERM_ID + " = " + termID;

        int deletedRows = getContentResolver().delete(DataProvider.CONTENT_URI_TERMS, whereClause, null);
        Intent intent = new Intent();
        intent.setClass(getBaseContext(), MainActivity.class);
        startActivity(intent);
        }
        else{
            Toast.makeText(this, "Unable to delete: Term has one or more courses assigned.", Toast.LENGTH_LONG).show();
        }

        DataProvider.CONTENT_ITEM_TYPE="courses";

    }




}
