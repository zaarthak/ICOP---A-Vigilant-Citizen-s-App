package com.sarthak.icop.icop.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.sarthak.icop.icop.models.Police;

import java.util.ArrayList;

public class CallDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "sqlite.sql";
    private static final int DATABASE_VERSION = 1;
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String POST = "post";
    private static final String PHONE = "phone";

    public CallDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ArrayList<Police> getPoliceDetails(int type) {

        String TABLE_NAME = "";

        if (type == 1) {
            TABLE_NAME = "police";
        } else if (type == 2) {
            TABLE_NAME = "administration";
        }

        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {CallDatabase.ID, CallDatabase.NAME, CallDatabase.POST, CallDatabase.PHONE};

        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);

        ArrayList<Police> policeArrayList = new ArrayList<>();

        while (cursor.moveToNext()) {

            Police police = new Police();
            police.setId(cursor.getInt(cursor.getColumnIndex(CallDatabase.ID)));
            police.setName(cursor.getString(cursor.getColumnIndex(CallDatabase.NAME)));
            police.setPost(cursor.getString(cursor.getColumnIndex(CallDatabase.POST)));
            police.setPhone(cursor.getString(cursor.getColumnIndex(CallDatabase.PHONE)));
            policeArrayList.add(police);
        }

        cursor.close();

        return policeArrayList;
    }
}
