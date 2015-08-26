package com.kii.iotcloudsample.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kii.iotcloudsample.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TriggersFragment extends Fragment {

    public TriggersFragment() {
        // Required empty public constructor
    }

    public static TriggersFragment newTriggersFragment() {
        return new TriggersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.triggers_view, null);
        return view;
    }


}
