package com.kii.iotcloudsample.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloudsample.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatesFragment extends Fragment implements PagerFragment {

    private IoTCloudAPI api;

    public StatesFragment() {
        // Required empty public constructor
    }

    public static StatesFragment newFragment(IoTCloudAPI api) {
        StatesFragment fragment = new StatesFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.api = savedInstanceState.getParcelable("IoTCloudAPI");
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.api = arguments.getParcelable("IoTCloudAPI");
        }
        View view = inflater.inflate(R.layout.states_view, null);
        String caption = ((TextView)view.findViewById(R.id.textState)).getText().toString();
        caption = caption.replace ("${thingID}", this.api.onboarded() ? this.api.getTarget().getID().getID() : "---");
        ((TextView)view.findViewById(R.id.textState)).setText(caption);
        return view;
    }
    @Override
    public void onVisible(boolean visible) {
    }
}
