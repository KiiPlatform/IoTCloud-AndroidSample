package com.kii.iotcloudsample.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloudsample.R;

public class CreateTriggerFragment extends Fragment {

    public static final String TAG = CreateTriggerFragment.class.getSimpleName();

    private IoTCloudAPI api;

    public static CreateTriggerFragment newFragment(IoTCloudAPI api) {
        CreateTriggerFragment fragment = new CreateTriggerFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("IoTCloudAPI", api);
        fragment.setArguments(arguments);
        return fragment;
    }

    public CreateTriggerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("IoTCloudAPI", this.api);
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
        View view = inflater.inflate(R.layout.create_command_view, null);
        return view;
    }

}
