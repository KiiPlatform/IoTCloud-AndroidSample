package com.kii.iotcloudsample.fragments;


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
public class CreateCommandFragment extends Fragment {


    public static final String TAG = "CreateCommandFragment";

    public CreateCommandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_command_view, null);
        return view;
    }


}
