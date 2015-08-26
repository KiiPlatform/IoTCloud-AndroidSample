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
public class CommandsFragment extends Fragment {


    public CommandsFragment() {
        // Required empty public constructor
    }

    public static CommandsFragment newCommandsFragment() {
        return new CommandsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.commands_view, container);
        return view;
    }


}
