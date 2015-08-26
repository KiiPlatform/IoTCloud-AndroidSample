package com.kii.iotcloudsample.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kii.iotcloudsample.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardFragment extends Fragment {


    public OnboardFragment() {
        // Required empty public constructor
    }

    public static OnboardFragment newOnboardFragment() {
        return new OnboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onboard_view, container);
        return view;
    }


}
