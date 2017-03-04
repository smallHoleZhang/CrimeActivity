package com.bignerdranch.android.crimeactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hasee on 2017/2/25.
 */

public class DatePickerFragment extends DialogFragment{
    public static final String ARG_DATE = " date";
    private DatePicker mDatePicker;
    public static  final  String EXTRA_DATE = "DATE";
    public static DatePickerFragment newInstance(Date date)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);
        DatePickerFragment datePickerFragment = new DatePickerFragment();

        datePickerFragment.setArguments(args);
        return datePickerFragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       Calendar mCalendar  = Calendar.getInstance();
        Date mDate = (Date) getArguments().getSerializable(ARG_DATE);
        mCalendar.setTime(mDate);
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date,null);
        mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(year,month,day,null);
        return  new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year,month,day).getTime();
                        sendResult(Activity.RESULT_OK,date);
                    }
                })
                .create();
    }
    public  void sendResult(int resultCode,Date date)
    {
        if(getTargetFragment() == null)
        {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
