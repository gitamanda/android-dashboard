package com.example.amanda.academicdashboard;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Amanda on 3/29/2019.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    //Constants for db name and version
    public static final String DATABASE_NAME = "studentdashboard.db";
    private static final int DATABASE_VERSION = 1;
    //Constants for identifying table and columns
    public static final String TERMS_TABLE = "terms";
    public static final String COURSES_TABLE = "courses";
    public static final String ASSESSMENTS_TABLE = "assessments";

    public static final String TERM_ID = "_id";
    public static final String TERM_NAME = "termName";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";

    public static final String COURSE_TERM_ID = "termID";
    public static final String COURSE_ID = "_id";
    public static final String COURSE_START_DATE = "startDate";
    public static final String COURSE_END_DATE = "endDate";
    public static final String COURSE_NAME = "courseName";
    public static final String COURSE_NOTES = "courseNotes";
    public static final String COURSE_STATUS = "courseStatus";
    public static final String COURSE_MENTOR_NAME = "mentorName";
    public static final String COURSE_MENTOR_EMAIL = "mentorEmail";
    public static final String COURSE_MENTOR_PHONE = "mentorPhone";

    public static final String ASSESSMENT_COURSE_ID = "courseID";
    public static final String ASSESSMENT_NAME = "assessmentName";
    public static final String ASSESSMENT_ID = "_id";
    public static final String ASSESSMENT_TYPE = "type";




    public static final String[] ALL_COLUMNS_TERMS = {TERM_ID, TERM_NAME, START_DATE, END_DATE};//can be referenced from anywhere in the app
    public static final String[] ALL_COLUMNS_COURSES = {COURSE_TERM_ID, COURSE_ID, COURSE_START_DATE, COURSE_END_DATE, COURSE_NAME, COURSE_NOTES, COURSE_STATUS, COURSE_MENTOR_NAME, COURSE_MENTOR_EMAIL, COURSE_MENTOR_PHONE};
    public static final String[] ALL_COLUMNS_ASSESSMENTS = {ASSESSMENT_COURSE_ID, ASSESSMENT_ID, ASSESSMENT_TYPE, ASSESSMENT_NAME};
    //SQL to create table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TERMS_TABLE + " (" +
                    TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_NAME + " TEXT, " +
                    START_DATE + " TEXT default CURRENT_TIMESTAMP, " +
                    END_DATE + " TEXT default CURRENT_TIMESTAMP)";

    private static final String TABLE_CREATE_COURSES =
            "CREATE TABLE " + COURSES_TABLE +" ("+
                    COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_NAME+ " TEXT, " +
                    COURSE_NOTES+ " TEXT, " +
                    COURSE_STATUS+ " TEXT, " +
                    COURSE_TERM_ID+ " INTEGER, " +
                    COURSE_START_DATE + " TEXT default CURRENT_TIMESTAMP, " +
                    COURSE_END_DATE + " TEXT default CURRENT_TIMESTAMP, "+
                    COURSE_MENTOR_NAME+ " TEXT, " +
                    COURSE_MENTOR_EMAIL+ " TEXT, " +
                    COURSE_MENTOR_PHONE+ " TEXT, " +
                    "FOREIGN KEY (" + COURSE_TERM_ID + ") REFERENCES " +
                    TERMS_TABLE + "(" + TERM_ID + "))";

    private static final String TABLE_CREATE_ASSESSMENTS =
            "CREATE TABLE " + ASSESSMENTS_TABLE +" ("+
                    ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ASSESSMENT_NAME+ " TEXT, " +
                    ASSESSMENT_TYPE+ " TEXT, " +
                    ASSESSMENT_COURSE_ID+ " INTEGER, " +
                    "FOREIGN KEY (" + ASSESSMENT_COURSE_ID + ") REFERENCES " +
                    COURSES_TABLE + "(" + COURSE_ID + "))";



    //INSERT INTO prod_mast(prod_id, prod_name, prod_rate, prod_qc)
    //VALUES(1, 'Pancakes', 75, 'OK');


    //FOREIGN KEY(trackartist) REFERENCES artist(artistid)

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String temp = TABLE_CREATE_COURSES;
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_CREATE_COURSES);
        db.execSQL(TABLE_CREATE_ASSESSMENTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TERMS_TABLE );
        db.execSQL("DROP TABLE IF EXISTS " + COURSES_TABLE );
        db.execSQL("DROP TABLE IF EXISTS " + ASSESSMENTS_TABLE );
        onCreate(db);
    }

}
