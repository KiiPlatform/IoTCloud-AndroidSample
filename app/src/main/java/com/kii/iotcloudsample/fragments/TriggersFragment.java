package com.kii.iotcloudsample.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloudsample.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TriggersFragment extends Fragment implements PagerFragment {

    private IoTCloudAPI api;

    public TriggersFragment() {
        // Required empty public constructor
    }

    public static TriggersFragment newTriggersFragment(IoTCloudAPI api) {
        TriggersFragment fragment = new TriggersFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("IoTCloudAPI", api);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("IoTCloudAPI", this.api);
    }
    @Override
    public void onVisible(boolean visible) {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.api = (IoTCloudAPI) savedInstanceState.getParcelable("IoTCloudAPI");
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.api = (IoTCloudAPI) arguments.getParcelable("IoTCloudAPI");
        }
        View view = inflater.inflate(R.layout.triggers_view, null);
        return view;
    }


}
