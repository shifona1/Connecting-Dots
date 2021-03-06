package com.connecting_dots.android.ccd;

/**
 * Created by Dell on 02-12-2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import static com.connecting_dots.android.ccd.Home.ONE_TIME_PREF;

public class ScreenSlidePagerActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 8;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    private boolean justfinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        if(getIntent().hasExtra("JUSTSHOW"))
            justfinish = true;
        else justfinish =false;
        findViewById(R.id.fab_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                if(!justfinish) {
                    Intent i = new Intent(ScreenSlidePagerActivity.this, LoginActivity.class);
                    startActivity(i);
                    PreferenceManager.getDefaultSharedPreferences(ScreenSlidePagerActivity.this)
                            .edit().putBoolean(ONE_TIME_PREF, true).commit();
                }
            }
        });
        if(!justfinish)
            findViewById(R.id.fab_close).setVisibility(View.GONE);
        
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

    }



    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }



    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            if(position==NUM_PAGES-1) {
                if(!justfinish)
                    findViewById(R.id.fab_close).setVisibility(View.VISIBLE);


            }

            return ScreenSlidePageFragment.create(position);
        }


        @Override
        public int getCount() {
            return NUM_PAGES;
        }




    }
}

