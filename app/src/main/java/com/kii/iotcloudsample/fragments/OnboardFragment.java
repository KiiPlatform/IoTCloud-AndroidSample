package com.kii.iotcloudsample.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.Target;
import com.kii.iotcloudsample.R;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;
import org.jdeferred.android.AndroidDeferredManager;
import org.jdeferred.android.DeferredAsyncTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardFragment extends Fragment {

    public OnboardFragment() {
        // Required empty public constructor
    }

    private IoTCloudAPI api;
    public static OnboardFragment newOnboardFragment(IoTCloudAPI api) {
        OnboardFragment fragment = new OnboardFragment();
        Bundle b = new Bundle();
        b.putParcelable("IoTCloudAPI", api);
        fragment.setArguments(b);
        return new OnboardFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("IoTCloudAPI", api);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null)
            api = savedInstanceState.getParcelable("IoTCloudAPI");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        if (b != null)
            this.api = b.getParcelable("IoTCloudAPI");
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
                onboard(thingID, thingPassword, isVendorId).then(new DoneCallback<Target>() {
                    @Override
                    public void onDone(Target result) {

                    }
                }, new FailCallback<Throwable>() {
                    @Override
                    public void onFail(Throwable result) {

                    }
                });
            }
        });
        return view;
    }

    private Promise<Target, Throwable, Void> onboard(final String thingID, final String thingPassword, final boolean isVendorThingID) {
        AndroidDeferredManager adm = new AndroidDeferredManager();
        return adm.when(new DeferredAsyncTask<Void, Void, Target>() {
            @Override
            protected Target doInBackgroundSafe(Void... voids) throws Exception {
                if (!isVendorThingID) {
                    return OnboardFragment.this.api.onBoard(thingID, thingPassword);
                } else {
                    return OnboardFragment.this.api.onBoard(thingID, thingPassword, null, null);
                }
            }
        });
    }

}
