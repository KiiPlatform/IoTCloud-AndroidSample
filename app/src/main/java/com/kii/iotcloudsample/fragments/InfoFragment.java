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
import com.kii.iotcloudsample.IoTCloudSampleApplication;
import com.kii.iotcloudsample.MainActivity;
import com.kii.iotcloudsample.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {


    public InfoFragment() {
        // Required empty public constructor
    }

    public static InfoFragment newInfoFragment() {
        return new InfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        IoTCloudAPI api = IoTCloudSampleApplication.getInstance().getAPI();
        TextView textOwner = (TextView)view.findViewById(R.id.textOwner);
        if (api.getOwner() != null) {
            textOwner.setText(api.getOwner().getID().getID());
        }
        TextView textTarget = (TextView)view.findViewById(R.id.textTarget);
        if (IoTCloudSampleApplication.getInstance().getCurrentTarget() != null) {
            textTarget.setText(IoTCloudSampleApplication.getInstance().getCurrentTarget().getID().getID());
        }

        return view;
    }


}
