package com.bignerdranch.android.crimeactivity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private static final String EXTRA_CRIME_ID =
            "com.bignerdranch.android.criminalintent.crime_id";
    public  static Intent newIntent(Context packageContext, UUID uuid)
    {
        Intent intent = new Intent(packageContext,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,uuid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager_acyivity);
        UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        /*
        预加载数目
          mViewPager.setOffscreenPageLimit(2);
         */

        mCrimes = CrimeLab.getCrimeLab(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return  CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        /*
        显示当前点击的View
         */
        for(int i = 0; i<mCrimes.size();i++)
        {
            if(mCrimes.get(i).getId().equals(id))
            {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
