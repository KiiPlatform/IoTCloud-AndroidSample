package com.kii.iotcloudsample.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kii.iotcloudsample.CreateCommandActivity;
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
        View view = inflater.inflate(R.layout.commands_view, null);
        Button btnNewCommand = (Button) view.findViewById(R.id.buttonNewCommand);
        btnNewCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(getContext(), CreateCommandActivity.class);
                getActivity().startActivity(i);
            }
        });
        return view;
    }


}
