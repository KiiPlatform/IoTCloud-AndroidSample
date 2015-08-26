package com.kii.iotcloudsample;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.kii.iotcloudsample.fragments.AppSingletonFragment;
import com.kii.iotcloudsample.fragments.ProgressDialogFragment;
import com.kii.iotcloudsample.promise_api_wrapper.KiiCloudPromiseAPIWrapper;
import com.kii.iotcloudsample.sliding_tab.SlidingTabLayout;
import com.kii.iotcloudsample.fragments.CommandsFragment;
import com.kii.iotcloudsample.fragments.InfoFragment;
import com.kii.iotcloudsample.fragments.OnboardFragment;
import com.kii.iotcloudsample.fragments.StatesFragment;
import com.kii.iotcloudsample.fragments.TriggersFragment;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

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

        login();
    }

    private void login() {
        AppSingletonFragment asf = AppSingletonFragment.getInstance(getSupportFragmentManager());
        if (asf.getApi() != null) {
            return;
        }

        KiiCloudPromiseAPIWrapper wp = new KiiCloudPromiseAPIWrapper();

        final ProgressDialogFragment pdf = new ProgressDialogFragment();
        getSupportFragmentManager().beginTransaction().add(pdf, ProgressDialogFragment.TAG);

        wp.loginWithCredentials().then(new DoneCallback<Void>() {
            @Override
            public void onDone(Void result) {
                pdf.dismiss();
                Toast.makeText(getApplicationContext(), "Login succeeded", Toast.LENGTH_LONG).show();
            }
        }, new FailCallback<Throwable>() {
            @Override
            public void onFail(Throwable result) {
                // Launch Login Screen.
                Intent i = new Intent();
                i.setClass(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });
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
