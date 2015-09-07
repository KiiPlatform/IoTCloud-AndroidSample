package com.kii.iotcloudsample;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiUser;
import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.Owner;
import com.kii.iotcloud.TypedID;
import com.kii.iotcloudsample.fragments.ProgressDialogFragment;
import com.kii.iotcloudsample.promise_api_wrapper.KiiCloudPromiseAPIWrapper;
import com.kii.iotcloudsample.sliding_tab.SlidingTabLayout;
import com.kii.iotcloudsample.fragments.CommandsFragment;
import com.kii.iotcloudsample.fragments.InfoFragment;
import com.kii.iotcloudsample.fragments.OnboardFragment;
import com.kii.iotcloudsample.fragments.StatesFragment;
import com.kii.iotcloudsample.fragments.TriggersFragment;
import com.kii.iotcloudsample.smart_light_demo.ApiBuilder;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

public class MainActivity extends AppCompatActivity {

    private IoTCloudAPI api;

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

        KiiCloudPromiseAPIWrapper wp = new KiiCloudPromiseAPIWrapper();

        final ProgressDialogFragment pdf = new ProgressDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(ProgressDialogFragment.TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        pdf.show(ft, ProgressDialogFragment.TAG);

        wp.loginWithCredentials().then(new DoneCallback<Void>() {
            @Override
            public void onDone(Void result) {
                pdf.dismiss();
                Owner owner = new Owner(new TypedID(TypedID.Types.USER, Kii.user().getID()), Kii
                        .user().getAccessToken());
                api = ApiBuilder.buildApi(getApplicationContext(), owner);
                Toast.makeText(getApplicationContext(), "Login succeeded", Toast.LENGTH_LONG).show();
            }
        }, new FailCallback<Throwable>() {
            @Override
            public void onFail(Throwable result) {
                // Launch Login Screen.
                Intent i = new Intent();
                i.setClass(getApplicationContext(), LoginActivity.class);
                startActivityForResult(i, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && KiiUser.getCurrentUser() != null) {
            Owner owner = new Owner(new TypedID(TypedID.Types.USER, KiiUser.getCurrentUser().getID()), Kii
                    .user().getAccessToken());
            api = ApiBuilder.buildApi(getApplicationContext(), owner);
            ProgressDialogFragment pdf = (ProgressDialogFragment) getSupportFragmentManager().findFragmentByTag
                    (ProgressDialogFragment.TAG);
            if (pdf != null) {
                pdf.dismiss();
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("IoTCloudAPI", this.api);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.api = (IoTCloudAPI)savedInstanceState.getParcelable("IoTCloudAPI");
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
                    return OnboardFragment.newOnboardFragment(api);
                case 1:
                    return CommandsFragment.newCommandsFragment(api);
                case 2:
                    return TriggersFragment.newTriggersFragment(api);
                case 3:
                    return StatesFragment.newStatesFragment(api);
                case 4:
                    return InfoFragment.newInfoFragment(api);
                default:
                    throw new RuntimeException("Unknown flow");
            }
        }

    }

}
