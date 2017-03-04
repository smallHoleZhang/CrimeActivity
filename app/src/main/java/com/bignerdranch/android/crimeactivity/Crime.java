package com.bignerdranch.android.crimeactivity;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by hasee on 2017/2/22.
 */

public class Crime  {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;

    @RequiresApi(api = Build.VERSION_CODES.N)
/*    public Date getDate() {

 *//*       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(mDate);


      return dateString;*//*
        return  mDate;
    }*/
    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Crime()
    {
        this(UUID.randomUUID());

    }

    public String getmSupect() {
        return mSuspect;
    }

    public void setmSupect(String suspect) {
        mSuspect = suspect;
    }

    public Crime(UUID id)
     {
         mId = id;
         mDate = new Date();
     }

    public UUID getId() {

        return mId;
    }
    public String getPhotoFilename()
    {
        return "IMG_"+getId().toString()+".jpg";
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setId(UUID id) {
        mId = id;
    }
}
