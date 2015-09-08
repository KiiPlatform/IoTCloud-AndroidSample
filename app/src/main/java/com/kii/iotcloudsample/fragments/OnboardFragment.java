package com.kii.iotcloudsample.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.Target;
import com.kii.iotcloudsample.promise_api_wrapper.IoTCloudPromiseAPIWrapper;
import com.kii.iotcloudsample.R;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardFragment extends Fragment implements PagerFragment {

    private View mOnboardWithIDFormView;
    private View mOnboardWithVenderIDFormView;

    private IoTCloudAPI api;

    public OnboardFragment() {
        // Required empty public constructor
    }

    public static OnboardFragment newOnboardFragment(IoTCloudAPI api) {
        OnboardFragment fragment = new OnboardFragment();
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
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
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
        View view = inflater.inflate(R.layout.onboard_view, null);
        mOnboardWithIDFormView = view.findViewById(R.id.onboard_with_id_form);
        mOnboardWithVenderIDFormView = view.findViewById(R.id.onboard_with_vender_id_form);
        final Button buttonOnboard = (Button) view.findViewById(R.id.buttonOnboard);
        final EditText editTextThingID = (EditText) view.findViewById(R.id.editTextThingId);
        final EditText editTextThingPassword = (EditText) view.findViewById(R.id.editTextThingPassword);
        final EditText editTextVenderThingID = (EditText) view.findViewById(R.id.editTextVenderThingId);
        final EditText editTextVenderThingPassword = (EditText) view.findViewById(R.id.editTextVenderThingPassword);
        final EditText editTextThingType = (EditText) view.findViewById(R.id.editTextThingType);
        final Switch aSwitch = (Switch) view.findViewById(R.id.switchThingIDType);

        showVenderThingForm(false);
        buttonOnboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aSwitch.isChecked()) {
                    final String venderThingID = editTextVenderThingID.getText().toString();
                    final String thingPassword = editTextVenderThingPassword.getText().toString();
                    final String thingType = editTextThingType.getText().toString();
                    IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(api);
                    wp.onBoard(venderThingID, thingPassword, thingType).then(new DoneCallback<Target>() {
                        @Override
                        public void onDone(Target result) {
                            Toast.makeText(getContext(), "On board succeeded!", Toast.LENGTH_LONG).show();
                        }
                    }, new FailCallback<Throwable>() {
                        @Override
                        public void onFail(Throwable result) {
                            Toast.makeText(getContext(), "On board failed: !" + result.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    final String thingID = editTextThingID.getText().toString();
                    final String thingPassword = editTextThingPassword.getText().toString();
                    IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(api);
                    wp.onBoard(thingID, thingPassword).then(new DoneCallback<Target>() {
                        @Override
                        public void onDone(Target result) {
                            Toast.makeText(getContext(), "On board succeeded!", Toast.LENGTH_LONG).show();
                        }
                    }, new FailCallback<Throwable>() {
                        @Override
                        public void onFail(Throwable result) {
                            Toast.makeText(getContext(), "On board failed: !" + result.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showVenderThingForm(isChecked);
            }
        });

        return view;
    }
    @Override
    public void onVisible(boolean visible) {
    }
    private void showVenderThingForm(boolean useVenderThingID) {
        mOnboardWithVenderIDFormView.setVisibility(useVenderThingID ? View.VISIBLE : View.GONE);
        mOnboardWithIDFormView.setVisibility(!useVenderThingID ? View.VISIBLE : View.GONE);
    }

}
