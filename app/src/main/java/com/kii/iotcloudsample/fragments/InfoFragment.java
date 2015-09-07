package com.kii.iotcloudsample.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kii.cloud.storage.KiiUser;
import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloudsample.MainActivity;
import com.kii.iotcloudsample.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private IoTCloudAPI api;

    public InfoFragment() {
        // Required empty public constructor
    }
    public static InfoFragment newInfoFragment(IoTCloudAPI api) {
        InfoFragment fragment = new InfoFragment();
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
            this.api = (IoTCloudAPI) savedInstanceState.getParcelable("IoTCloudAPI");
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.api = (IoTCloudAPI) arguments.getParcelable("IoTCloudAPI");
        }
        View view = inflater.inflate(R.layout.info_view, null);
        Button logoutButton = (Button)view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KiiUser.logOut();
                Intent i = new Intent();
                i.setClass(getContext().getApplicationContext(), MainActivity.class);
                startActivityForResult(i, 0);
            }
        });
        TextView textOwner = (TextView)view.findViewById(R.id.textOwner);
        if (this.api.getOwner() != null) {
            textOwner.setText(this.api.getOwner().getID().getID());
        }
        TextView textTarget = (TextView)view.findViewById(R.id.textTarget);
        if (this.api.getTarget() != null) {
            textTarget.setText(this.api.getTarget().getID().getID());
        }
        return view;
    }


}
