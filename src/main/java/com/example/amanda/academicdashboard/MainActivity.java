package com.example.amanda.academicdashboard;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.CursorLoader;
//import android.support.v4.content.Loader;
//import android.support.v4.widget.CursorAdapter;
import android.content.CursorLoader;
import android.content.Loader;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    SimpleCursorAdapter adapter;
    private static final  int EDITOR_REQUEST_CODE = 7007;
    private static final  int REQUEST_CODE = 003;
    ListView list;
    String termID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String [] from ={DBOpenHelper.TERM_NAME};
        int [] to = {R.id.tvListViewItem};
        adapter = new SimpleCursorAdapter(this, R.layout.listview_item,null, from, to, 0);

        list = (ListView) findViewById(R.id.lvTerms);
        DataProvider.CONTENT_ITEM_TYPE="terms";

       list.setAdapter(adapter);//this is the crashing line
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, CoursesActivity.class);
              //  Uri uri = Uri.parse(DataProvider.CONTENT_URI_TERMS + "/"+ id);
               intent.putExtra("termID", id );

                startActivity(intent);
            }
        });
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Intent intent = new Intent();
        //        intent.setClass(getBaseContext(), AddTermActivity.class);
        //        startActivityForResult(intent, REQUEST_CODE);
        //    }
        //});

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_term) {

            Intent intent = new Intent();
            intent.setClass(getBaseContext(), AddTermActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //callback. designates where data is coming from
        android.content.CursorLoader cursorLoader;
        cursorLoader = new CursorLoader(this, DataProvider.CONTENT_URI_TERMS,null,null,null,null);
        return cursorLoader;

    }




    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //callback method. called when data comes back
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //called whenever data needs to be wiped out. callback method
        adapter.swapCursor(null);
    }

    private android.content.Loader<Cursor> restartLoader() {

      //  getLoaderManager().restartLoader(0,null, MainActivity.this);
       // android.app.LoaderManager manager = getLoaderManager();
        return getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== EDITOR_REQUEST_CODE&&resultCode==RESULT_OK){
            restartLoader();
        }
    }
}
