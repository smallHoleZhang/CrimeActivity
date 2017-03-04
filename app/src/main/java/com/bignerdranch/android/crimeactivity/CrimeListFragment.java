package com.bignerdranch.android.crimeactivity;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hasee on 2017/2/22.
 */

public class CrimeListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private int mPosititon;
    private boolean mSutitleVisible;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private  void updateUI()
    {

        CrimeLab crimeLab = CrimeLab.getCrimeLab(getContext());
        List<Crime> crimes = crimeLab.getCrimes();
        if(mCrimeAdapter == null)
        {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mRecyclerView.setAdapter(mCrimeAdapter);
        }
        else {
            mCrimeAdapter.setCrimes(crimes);
      /*     mCrimeAdapter.notifyItemChanged(mPosititon);
           */ mCrimeAdapter.notifyDataSetChanged();
        }
        updateSubtitle();

    }
    public class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }



        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d("TAG", "onCreateViewHolder: ");
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime =  mCrimes.get(position);
            holder.bindCrime(crime);
            //holder.mTextView.setText(crime.getTitle());
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
        public  void setCrimes(List<Crime> crimes)
        {
            mCrimes = crimes;
        }
    }
    public class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //private TextView mTextView;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
          //  mTextView = (TextView) itemView;
            mTitleTextView = (TextView) itemView.findViewById(R.id.lite_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.lite_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.lite_item_crime_title_check_box);
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            Date date = mCrime.getDate();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString =formatter.format(date);
            mDateTextView.setText(dateString);
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),mCrime.getId().toString(),Toast.LENGTH_SHORT).show();
            Intent intent = CrimePagerActivity.newIntent(getContext(),mCrime.getId());
            startActivity(intent);
       }
    }

    private  void updateSubtitle()
    {
        CrimeLab crimeLab = CrimeLab.getCrimeLab(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String sutitle = getString(R.string.subtitle_format,crimeCount);
        if(!mSutitleVisible)
        {
            sutitle = null;
        }
        AppCompatActivity Activity = (AppCompatActivity) getActivity();
        Log.d("TAG", "updateSubtitle: ");
        /*
        ????????
         */
        Activity.getSupportActionBar().setSubtitle(sutitle);
    

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        {
            if(mSutitleVisible){
                subtitleItem.setTitle(R.string.hide_subtitle);
            }else {
                subtitleItem.setTitle(R.string.show_subtitle);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                Log.d("TAG", "onOptionsItemSelected: ");
                CrimeLab.getCrimeLab(getActivity()).CrimeAdd(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
                startActivity(intent);
                return  true;
            case R.id.menu_item_show_subtitle:
                mSutitleVisible = !mSutitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
