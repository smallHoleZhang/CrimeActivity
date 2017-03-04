package com.bignerdranch.android.crimeactivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;

import com.bignerdranch.android.crimeactivity.datebase.CrimeBaseHelper;
import com.bignerdranch.android.crimeactivity.datebase.CrimeCursorWrapper;
import com.bignerdranch.android.crimeactivity.datebase.CrimeDbScheme;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bignerdranch.android.crimeactivity.datebase.CrimeDbScheme.CrimeTable.*;

/**
 * Created by hasee on 2017/2/22.
 */

public class CrimeLab  {
    //private static  CrimeLab sCrimeLab;
  /*  private List<Crime> mCrimes;*/
    private static CrimeLab sCrimeLab;
    public Context mContext;
    public SQLiteDatabase msqLiteDatabase;

 /*  *//* public static  CrimeLab get(Context context)
    {
      if(sCrimeLab == null) {
         sCrimeLab = new CrimeLab(context);
      }
        return sCrimeLab;*//*
    }*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void CrimeAdd(Crime crime)
    {
        ContentValues values = getContentValues(crime);
        msqLiteDatabase.insert(CrimeDbScheme.CrimeTable.NAME,null,values);
     /*   mCrimes.add(crime);*/
    }
     private CrimeLab(Context context)
     {
         mContext = context.getApplicationContext();
         msqLiteDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
        /* mCrimes = new ArrayList<>();
*/
     }
    public static CrimeLab getCrimeLab(Context context)
    {
        if(sCrimeLab == null)
        {
            sCrimeLab = new CrimeLab( context);
        }
        return sCrimeLab;
    }
    public  List<Crime> getCrimes()
    {
        /*return mCrimes;*/
        List<Crime> crimes  = new ArrayList<>();
        CrimeCursorWrapper cursorWrapper = queryCrimes(null,null);
        try{
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast())
            {
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }
        return crimes;
    }
    public  Crime getCrime(UUID id) {
        /*for(Crime crime:mCrimes)
        {
            if(crime.getId().equals(id))
                return crime;
        }
        return null;*/
        CrimeCursorWrapper cursor = queryCrimes(Cols.UUID + "=?",new String[]
                {
                        id.toString()
                });
        try {
            if(cursor.getCount() == 0)
            {
                return  null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }

    }
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = msqLiteDatabase.query(
                CrimeDbScheme.CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new CrimeCursorWrapper(cursor);
    }
    public File getphoto(Crime crime)
    {
        File externalFilesDir =  mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir == null)
        {
            return  null;
        }
        return  new File (externalFilesDir,crime.getPhotoFilename());
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public  void updateCrime(Crime crime)
    {
        String uuidString = crime.getId().toString();
        ContentValues v = getContentValues(crime);
        msqLiteDatabase.update(CrimeDbScheme.CrimeTable.NAME,v, Cols.UUID + "= ?",new String[]
                {
                        uuidString
                });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ContentValues getContentValues(Crime crime)
    {
        ContentValues values  = new ContentValues();
        values.put(Cols.UUID,crime.getId().toString());
        values.put(Cols.TITLE,crime.getTitle());
        values.put(Cols.DATE,crime.getDate().getTime());
        values.put(Cols.SOLVED,crime.isSolved()?1:0);
        values.put(Cols.SUSPECT,crime.getmSupect());
        return values;
    }
}
