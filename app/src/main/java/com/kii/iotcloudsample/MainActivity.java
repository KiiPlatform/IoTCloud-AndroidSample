package com.kii.iotcloudsample;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kii.iotcloudsample.view.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new MyAdapter());

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(viewPager);
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
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
        public Object instantiateItem(ViewGroup container, int position) {
            int layoutId = 0;
            switch (position) {
                case 0:
                    layoutId = R.layout.onboard_view;
                    break;
                case 1:
                    layoutId = R.layout.commands_view;
                    break;
                case 2:
                    layoutId = R.layout.triggers_view;
                    break;
                case 3:
                    layoutId = R.layout.states_view;
                    break;
                case 4:
                    layoutId = R.layout.info_view;
                    break;
                default:
                    throw new RuntimeException("Unknown flow");
            }
            // Inflate a new layout from our resources
            View view = MainActivity.this.getLayoutInflater().inflate(layoutId,
                    container, false);
            // Add the newly created View to the ViewPager
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

}
