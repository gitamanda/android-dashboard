package com.example.amanda.academicdashboard;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Amanda on 3/30/2019.
 *
 */

public class DataProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.amanda.academicdashboard";
    private static final String BASE_PATH_TERMS = "terms";
    private static final String BASE_PATH_COURSES = "courses";
    private static final String BASE_PATH_ASSESSMENTS = "assessments";
    public static final Uri CONTENT_URI_TERMS = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_TERMS );
    public static final Uri CONTENT_URI_COURSES = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_COURSES );
    public static final Uri CONTENT_URI_ASSESSMENTS = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_ASSESSMENTS );
    private SQLiteDatabase database;
    public static String CONTENT_ITEM_TYPE;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        uriMatcher.addURI(AUTHORITY, BASE_PATH_TERMS,1);
        uriMatcher.addURI(AUTHORITY, BASE_PATH_TERMS + "/#",2);//hashmark is a wildcard representing numeric values

    }



    @Override
    public boolean onCreate() {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(getContext());
        database = dbOpenHelper.getWritableDatabase();
        return true;
    }



    public void setContentItemType(String contentItemType){

        CONTENT_ITEM_TYPE=contentItemType;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        switch (CONTENT_ITEM_TYPE) {
            case "terms":
                cursor= database.query(DBOpenHelper.TERMS_TABLE,projection,selection, null, null, null, DBOpenHelper.TERM_NAME+ " DESC");
                break;
            case "courses":
                cursor= database.query(DBOpenHelper.COURSES_TABLE,projection,selection, null, null, null, DBOpenHelper.COURSE_NAME+ " DESC");
                break;
            case "assessments":
                cursor= database.query(DBOpenHelper.ASSESSMENTS_TABLE,projection,selection, null, null, null, DBOpenHelper.ASSESSMENT_NAME+ " DESC");
                break;
            default:
                cursor= database.query(DBOpenHelper.TERMS_TABLE,projection,selection, null, null, null, DBOpenHelper.TERM_NAME+ " DESC");
                break;
        }

        return cursor;
        //return database.query(DBOpenHelper.TERMS_TABLE,DBOpenHelper.ALL_COLUMNS_TERMS,selection, null, null, null, DBOpenHelper.START_DATE+ " ASC");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri tempURI = Uri.EMPTY;

        switch (CONTENT_ITEM_TYPE) {
            case "terms":
                long id = database.insert(DBOpenHelper.TERMS_TABLE, null, values);
                tempURI = Uri.parse(BASE_PATH_TERMS+"/"+id);
                break;
            case "courses":
                long id1 = database.insert(DBOpenHelper.COURSES_TABLE, null, values);
                tempURI = Uri.parse(BASE_PATH_COURSES+"/"+id1);
                break;
            case "assessments":
                long id2 = database.insert(DBOpenHelper.ASSESSMENTS_TABLE, null, values);
                tempURI = Uri.parse(BASE_PATH_ASSESSMENTS+"/"+id2);
                break;

        }

        return tempURI;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int deletedRowCount = 0;
        if(CONTENT_ITEM_TYPE=="terms") {
            deletedRowCount =  database.delete(DBOpenHelper.TERMS_TABLE, selection, selectionArgs);//returns # of rows deleted}
        }
        else if(CONTENT_ITEM_TYPE=="courses") {
            deletedRowCount = database.delete(DBOpenHelper.COURSES_TABLE, selection, selectionArgs);//returns # of rows deleted}
        }
        else if(CONTENT_ITEM_TYPE=="assessments") {
            deletedRowCount = database.delete(DBOpenHelper.ASSESSMENTS_TABLE, selection, selectionArgs);//returns # of rows deleted}
        }
        return deletedRowCount;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int updatedRowCount = 0;
        if(CONTENT_ITEM_TYPE == "terms") {
            updatedRowCount = database.update(DBOpenHelper.TERMS_TABLE, values, selection, selectionArgs);
        }
        else if(CONTENT_ITEM_TYPE == "courses") {
            updatedRowCount = database.update(DBOpenHelper.COURSES_TABLE, values, selection, selectionArgs);
        }
        else if (CONTENT_ITEM_TYPE == "assessments") {
            updatedRowCount = database.update(DBOpenHelper.ASSESSMENTS_TABLE, values, selection, selectionArgs);
        }
        return updatedRowCount;
    }
}
