package com.bignerdranch.android.crimeactivity.datebase;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.crimeactivity.Crime;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hasee on 2017/2/27.
 */

public class CrimeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Crime getCrime()
    {
        String uuidString = getString(getColumnIndex(CrimeDbScheme.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeDbScheme.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDbScheme.CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeDbScheme.CrimeTable.Cols.SOLVED));
        String suspect =  getString(getColumnIndex(CrimeDbScheme.CrimeTable.Cols.SUSPECT));
        Crime crime  =  new Crime(UUID.fromString(uuidString));
        crime.setDate(new Date(date));
        crime.setSolved(isSolved!=0);
        crime.setTitle(title);
        crime.setmSupect(suspect);
        return  crime;
    }
}
