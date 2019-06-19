package com.example.amanda.academicdashboard;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class AssessmentDetailActivity extends AppCompatActivity {

    TextView tvAssessmentName, tvDueDate, tvAssessmentType;

    long assessmentID;

    String dueDate, assessmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        DataProvider.CONTENT_ITEM_TYPE = "assessments";

        tvAssessmentName = (TextView) findViewById(R.id.tvAssessmentNameDisplay);
        tvDueDate = (TextView) findViewById(R.id.tvDueDateDisplay);
        tvAssessmentType = (TextView) findViewById(R.id.tvAssessmentTypeDisplay);

        Intent intent = getIntent();
        assessmentID = intent.getLongExtra("assessmentID", 0);
        dueDate = intent.getStringExtra("dueDate");

        String selection = DBOpenHelper.ASSESSMENT_ID + " = " + assessmentID;

        Cursor cursor = getContentResolver().query(DataProvider.CONTENT_URI_ASSESSMENTS, DBOpenHelper.ALL_COLUMNS_ASSESSMENTS, selection,null,null);

        cursor.moveToFirst();
        assessmentName = cursor.getString(3);

        String assessmentType = cursor.getString(2);
        tvAssessmentName.setText(assessmentName);
        tvAssessmentType.setText(assessmentType);
        tvDueDate.setText(dueDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_delete_assessment, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_assessment) {

            deleteTerm(assessmentID);
        }
        else if (id==R.id.action_edit_assessment){

            Intent intent = new Intent();
            intent.setClass(getBaseContext(), EditAssessmentActivity.class);
            intent.putExtra("assessmentID", assessmentID);
            intent.putExtra("dueDate", dueDate);
            intent.putExtra("assessmentName", assessmentName);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteTerm(long termID) {

        String whereClause = DBOpenHelper.ASSESSMENT_ID + " = " + assessmentID;

        int deletedRows = getContentResolver().delete(DataProvider.CONTENT_URI_ASSESSMENTS, whereClause, null);
        Intent intent = new Intent();
        intent.setClass(getBaseContext(), MainActivity.class);
        startActivity(intent);

    }
}
