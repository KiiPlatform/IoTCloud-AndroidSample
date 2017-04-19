package com.kii.thingifsample;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiUser;
import com.kii.thingif.ThingIFAPI;
import com.kii.thingif.Owner;
import com.kii.thingif.PushBackend;
import com.kii.thingif.TypedID;
import com.kii.thingif.exception.StoredInstanceNotFoundException;
import com.kii.thingif.exception.ThingIFException;
import com.kii.thingif.exception.UnloadableInstanceVersionException;
import com.kii.thingifsample.fragments.PagerFragment;
import com.kii.thingifsample.fragments.ProgressDialogFragment;
import com.kii.thingifsample.sliding_tab.SlidingTabLayout;
import com.kii.thingifsample.fragments.CommandsFragment;
import com.kii.thingifsample.fragments.InfoFragment;
import com.kii.thingifsample.fragments.OnboardFragment;
import com.kii.thingifsample.fragments.StatesFragment;
import com.kii.thingifsample.fragments.TriggersFragment;
import com.kii.thingifsample.smart_light_demo.ApiBuilder;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ThingIFAPI api;

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
        final ProgressDialogFragment pdf = new ProgressDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(ProgressDialogFragment.TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        pdf.show(ft, ProgressDialogFragment.TAG);

        try {
            this.api = ThingIFAPI.loadFromStoredInstance(this);
            new GCMRegisterTask(this.api).execute();
            pdf.dismiss();
        } catch (StoredInstanceNotFoundException e) {
            Intent i = new Intent();
            i.setClass(getApplicationContext(), LoginActivity.class);
            startActivityForResult(i, 0);
        } catch (UnloadableInstanceVersionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && KiiUser.getCurrentUser() != null) {
            Owner owner = new Owner(new TypedID(TypedID.Types.USER, KiiUser.getCurrentUser().getID()), Kii
                    .user().getAccessToken());
            this.api = ApiBuilder.buildApi(getApplicationContext(), owner);
            new GCMRegisterTask(this.api).execute();
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
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            this.api = ThingIFAPI.loadFromStoredInstance(this);
        } catch (StoredInstanceNotFoundException e) {
            e.printStackTrace();
        } catch (UnloadableInstanceVersionException e) {
            e.printStackTrace();
        }
    }

    public class GCMRegisterTask extends AsyncTask<Void, Void, Exception> {

        private final ThingIFAPI api;

        GCMRegisterTask(ThingIFAPI api) {
            this.api = api;
        }
        @Override
        protected Exception doInBackground(Void... params) {
            if (TextUtils.isEmpty(IoTCloudSampleApplication.getInstance().getSenderID())) {
                return null;
            }
            String registrationId = null;
            int retry = 0;
            Exception lastException = null;
            while (retry < 3) {
                try {
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
                    registrationId = gcm.register(IoTCloudSampleApplication.getInstance().getSenderID());
                    break;
                } catch (IOException ignore) {
                    lastException = ignore;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        lastException = e;
                    }
                    retry++;
                }
            }
            if (TextUtils.isEmpty(registrationId)) {
                return lastException;
            }
            try {
                this.api.installPush(registrationId, PushBackend.GCM);
            } catch (ThingIFException e) {
                return e;
            }
            return null;
        }
        @Override
        protected void onPostExecute(final Exception e) {
            if (e != null) {
                Toast.makeText(MainActivity.this, "Unable to register GCM!: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    class MyAdapter extends FragmentPagerAdapter {

        private PagerFragment currentFragment;
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
                    return OnboardFragment.newFragment();
                case 1:
                    return CommandsFragment.newFragment();
                case 2:
                    return TriggersFragment.newFragment();
                case 3:
                    return StatesFragment.newFragment();
                case 4:
                    return InfoFragment.newFragment();
                default:
                    throw new RuntimeException("Unknown flow");
            }
        }
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (object instanceof PagerFragment && this.currentFragment != object) {
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                ((PagerFragment) object).onVisible(true);
                if (this.currentFragment != null) {
                    this.currentFragment.onVisible(false);
                }
                this.currentFragment = ((PagerFragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }

    }

}
