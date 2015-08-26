package com.kii.iotcloudsample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kii.iotcloudsample.view.SlidingTabLayout;
import com.kii.iotcloudsample.fragments.CommandsFragment;
import com.kii.iotcloudsample.fragments.InfoFragment;
import com.kii.iotcloudsample.fragments.OnboardFragment;
import com.kii.iotcloudsample.fragments.StatesFragment;
import com.kii.iotcloudsample.fragments.TriggersFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(viewPager);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return "Onboard";
                case 1:
                    return "Commands";
                case 2:
                    return "Triggers";
                case 3:
                    return "States";
                case 4:
                    return "Info";
                default:
                    throw new RuntimeException("Unxepected flow.");
            }
        }

        @Override
        public Fragment getItem(int position) {
                        switch (position) {
                case 0:
                    return OnboardFragment.newOnboardFragment();
                case 1:
                    return CommandsFragment.newCommandsFragment();
                case 2:
                    return TriggersFragment.newTriggersFragment();
                case 3:
                    return StatesFragment.newStatesFragment();
                case 4:
                    return InfoFragment.newInfoFragment();
                default:
                    throw new RuntimeException("Unknown flow");
            }
        }

    }

}
