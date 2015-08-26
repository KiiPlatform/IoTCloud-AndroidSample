package com.kii.iotcloudsample.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.Target;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppSingletonFragment extends Fragment {

    private IoTCloudAPI api;
    private Target target;

    public AppSingletonFragment() {
        // Required empty public constructor
    }

    public void setApi(IoTCloudAPI api) {
        this.api = api;
    }

    public IoTCloudAPI getApi() {
        return this.api;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Target getTarget() {
        return this.target;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("IoTCloudAPI", this.api);
        outState.putParcelable("IoTCloudTarget", this.target);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.api = savedInstanceState.getParcelable("IoTCloudAPI");
            this.target = savedInstanceState.getParcelable("IoTCloudTarget");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

    public static AppSingletonFragment getInstance(FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AppSingletonFragment fragment = (AppSingletonFragment) fragmentManager.findFragmentByTag("AppSingleton");
        if (fragment == null) {
            fragment = new AppSingletonFragment();
            fragmentTransaction.add(fragment, "AppSingleton").commit();
        }
        return fragment;
    }

}
