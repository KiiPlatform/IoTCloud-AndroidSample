package com.kii.iotcloudsample.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.Target;
import com.kii.iotcloudsample.MainActivity;
import com.kii.iotcloudsample.promise_api_wrapper.IoTCloudPromiseAPIWrapper;
import com.kii.iotcloudsample.R;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardFragment extends Fragment {

    public OnboardFragment() {
        // Required empty public constructor
    }

    public static OnboardFragment newOnboardFragment() {
        OnboardFragment fragment = new OnboardFragment();
        return new OnboardFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onboard_view, null);
        final Button buttonOnboard = (Button) view.findViewById(R.id.buttonOnboard);
        final EditText editTextThingID = (EditText) view.findViewById(R.id.editTextThingId);
        final EditText editTextThingPassword = (EditText) view.findViewById(R.id.editTextThingPassword);
        final Switch aSwitch = (Switch) view.findViewById(R.id.switchThingIDType);

        buttonOnboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String thingID = editTextThingID.getText().toString();
                final String thingPassword = editTextThingPassword.getText().toString();
                final boolean isVendorId = aSwitch.isChecked();

                IoTCloudAPI api = ((MainActivity)getActivity()).getApi();
                IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(api);
                wp.onBoard(thingID, thingPassword, isVendorId).then(new DoneCallback<Target>() {
                    @Override
                    public void onDone(Target result) {
                        Toast.makeText(getContext(), "On board succeeded!", Toast.LENGTH_LONG).show();
                    }
                }, new FailCallback<Throwable>() {
                    @Override
                    public void onFail(Throwable result) {
                        result.printStackTrace();
                        Toast.makeText(getContext(), "On board failed: !" + result.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return view;
    }

}
