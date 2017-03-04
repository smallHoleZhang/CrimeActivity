package com.bignerdranch.android.crimeactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by hasee on 2017/2/22.
 */

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private File mPhotoFile;
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static  final  int  REQUEST_DATE = 0;
    private static final  int REQUWST_COUNT = 1;
    private static final  int REQUWST_PHOTO = 2;
    private ImageView mPhotoView;
    private ImageButton mPhotoButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);*/
        /* Toast.makeText(getActivity(),crimeId.toString(),Toast.LENGTH_SHORT).show();*/
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime= CrimeLab.getCrimeLab(getActivity()).getCrime(crimeId);
         mPhotoFile = CrimeLab.getCrimeLab(getActivity()).getphoto(mCrime);

    }
    public  static CrimeFragment newInstance(UUID crimeId)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout,container,false);

        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mSuspectButton = (Button) view.findViewById(R.id.crime_suspect);
        mDateButton = (Button) view.findViewById(R.id.crime_date);
        updateDate();
        mReportButton = (Button)view.findViewById(R.id.crime_report);
        final  Intent   pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact,REQUWST_COUNT);
            }
        });
        if(mCrime.getmSupect() != null)
        {
            mSuspectButton.setText(mCrime.getmSupect());
        }
        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY) == null)
        {
            mSuspectButton.setEnabled(false);
        }
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));

                startActivity(i);
            }
        });
       /* mDateButton.setText(mCrime.getDate().toString());*/
     /*   mDateButton.setEnabled(false);*/
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                FragmentManager manager =  getFragmentManager();
           /*    DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(manager,DIALOG_DATE );*/
                DatePickerFragment fragment  = DatePickerFragment.newInstance(mCrime.getDate());
                fragment.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                fragment.show(manager,DIALOG_DATE);
            }
        });
        mPhotoButton = (ImageButton) view.findViewById(R.id.crime_camera);
        mPhotoView = (ImageView) view.findViewById(R.id.crime_photo);
        final  Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager)!= null;
        mPhotoButton.setEnabled(canTakePhoto);
        if(canTakePhoto)
        {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,REQUWST_PHOTO);
        }
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage,REQUWST_PHOTO);
            }
        });
        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        updatePhotoView();
        return view;

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateDate()
    {
       /* mDateButton.setText(mCrime.getDate().toString());*/
        Date date = mCrime.getDate();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString =formatter.format(date);
        mDateButton.setText(dateString);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getmSupect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
    private void updatePhotoView()
    {

        if(mPhotoFile == null || mPhotoFile.exists())
        {
            mPhotoView.setImageDrawable(null);
        }else {
            Bitmap bitmap = pcitureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
        {
            return;
        }
        if(requestCode == REQUEST_DATE)
        {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();

        }else  if(requestCode == REQUWST_COUNT && REQUWST_COUNT != 0 )
        {
            Uri contactUri = data.getData();
            String[] queryFieds = new String[]
                    {
                            ContactsContract.Contacts.DISPLAY_NAME
                    };
            Cursor c = getActivity().getContentResolver().query(contactUri,queryFieds,null,null,null);
            try{
                if(c.getCount()  == 0)
                {
                    return;
                }
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setmSupect(suspect);
                mSuspectButton.setText(suspect);
            }finally {
                c.close();
            }
        }else  if(requestCode == REQUWST_PHOTO)
        {
            updatePhotoView();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getCrimeLab(getActivity()).updateCrime(mCrime);
    }
}
