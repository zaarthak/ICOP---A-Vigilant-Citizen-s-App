package com.sarthak.icop.icop.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.sarthak.icop.icop.models.Report;

import java.util.ArrayList;
import java.util.List;

public class ReportDatabase extends SQLiteOpenHelper {

    private Context mContext;

    // database version
    private static final int DATABASE_VERSION = 1;

    // database Name
    private static final String DATABASE_NAME = "ReportManager";

    // city table name
    private static final String TABLE_NAME = "Report";

    private static final String KEY_ID = "id";

    private static final String KEY_CATEGORY = "category";
    private static final String KEY_INFORMATION = "information";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_IMAGE = "image";

    public ReportDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CITY_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " TEXT PRIMARY KEY,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_INFORMATION + " TEXT,"
                + KEY_CONTACT + " TEXT,"
                + KEY_IMAGE + " TEXT" + ")";

        db.execSQL(CREATE_CITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        // drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // create table again
        onCreate(db);
    }

    public void addReport(String category, String information, String contact, String image) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, String.format("CT-%05d", getReportCount() + 1));
        values.put(KEY_CATEGORY, category);
        values.put(KEY_INFORMATION, information);
        values.put(KEY_CONTACT, contact);
        values.put(KEY_IMAGE, image);

        // inserting row
        db.insert(TABLE_NAME, null, values);
        // closing database connection
        db.close();

        Toast.makeText(mContext, "Report recorded with ID: " + String.format("CT-%05d", getReportCount()), Toast.LENGTH_SHORT).show();
    }

    public Report getReportDetails(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Report report = new Report();

            Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_CATEGORY, KEY_INFORMATION
                    , KEY_CONTACT, KEY_IMAGE }, KEY_ID + "=?", new String[] { id }, null, null, null);

            if (cursor != null) {

                cursor.moveToFirst();
                report = new Report(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            }

            // return city
            return report;
        } catch (CursorIndexOutOfBoundsException e) {

            Toast.makeText(mContext, "Please enter valid Report ID.", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public ArrayList<Report> getAll() {

        ArrayList<Report> reportList = new ArrayList<>();

        // select all query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Report report = new Report();

                report.setId(cursor.getString(0));
                report.setCategory(cursor.getString(1));
                report.setInformation(cursor.getString(2));
                report.setContact(cursor.getString(3));
                report.setImage(cursor.getString(4));

                // adding city to list
                reportList.add(report);
            } while (cursor.moveToNext());
        }

        // return city list
        return reportList;
    }

    private void updateReportCount(int id) {

        int updated_id = id - 1;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("UPDATE " + TABLE_NAME
                + " SET " + KEY_ID + " = " + updated_id + " WHERE " + KEY_ID + " = " + id, null);

        cursor.moveToFirst();
        cursor.close();
    }

    public void deleteCity(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_NAME, KEY_ID + " = ?",
                    new String[] { String.valueOf(id) });

            for (int i = id + 1 ; i <= getReportCount() + 1; i++) {
                updateReportCount(i);
            }

            db.close();
        }
        catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public int getReportCount() {

        String countQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }
}
