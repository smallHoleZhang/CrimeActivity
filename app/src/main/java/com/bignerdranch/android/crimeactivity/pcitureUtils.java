package com.bignerdranch.android.crimeactivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by hasee on 2017/3/1.
 */

public class pcitureUtils {
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
    public static  Bitmap getScaledBitmap(String path, Activity activity)
    {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
                .getSize(size);
        return getScaledBitmap(path,size.x,size.y);
    }
    public  static Bitmap getScaledBitmap(String path,int destWidth,int destHeight)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        float srcWidth = options.outWidth;
        float srcHeigth = options.outHeight;
        int inSampleSize = 1;
        if(srcHeigth > destHeight || srcWidth >destWidth)
        {
            if(srcWidth> srcHeigth)
            {
                inSampleSize = Math.round(srcHeigth/destHeight);
            }else {
                inSampleSize = Math.round(srcWidth/destWidth);
            }
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(path,options);
    }
}
