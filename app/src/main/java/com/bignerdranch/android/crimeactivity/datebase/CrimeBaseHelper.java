package com.bignerdranch.android.crimeactivity.datebase;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.bignerdranch.android.crimeactivity.datebase.CrimeDbScheme.CrimeTable.*;

/**
 * Created by hasee on 2017/2/27.
 */

public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "CrimeBaseHelper";
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + NAME + "(" +
                " _id integer primary key autoincrement, " +
                Cols.UUID + ", " +
                Cols.TITLE + ", " +
                Cols.DATE + ", " +
                Cols.SUSPECT + ", " +
                Cols.SOLVED +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}