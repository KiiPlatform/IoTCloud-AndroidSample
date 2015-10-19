package com.kii.thingifsample.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.cloud.storage.KiiThing;
import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.Target;
import com.kii.thingifsample.promise_api_wrapper.IoTCloudPromiseAPIWrapper;
import com.kii.thingifsample.R;
import com.kii.thingifsample.promise_api_wrapper.KiiCloudPromiseAPIWrapper;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardFragment extends Fragment implements PagerFragment {

    private View mOnboardWithIDFormView;
    private View mOnboardWithVenderIDFormView;
    private View mOnboardFormView;
    private View mOnboardedFormView;
    private View mOnboardOperationFormView;

    private TextView txtThingId;
    private TextView txtVenderThingId;
    private TextView txtThingType;
    private TextView txtVender;
    private TextView txtProductName;
    private TextView txtFirmwareVersion;
    private TextView txtLot;

    private IoTCloudAPI api;

    public OnboardFragment() {
        // Required empty public constructor
    }

    public static OnboardFragment newFragment(IoTCloudAPI api) {
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
            this.api = savedInstanceState.getParcelable("IoTCloudAPI");
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.api = arguments.getParcelable("IoTCloudAPI");
        }
        View view = inflater.inflate(R.layout.onboard_view, null);
        mOnboardWithIDFormView = view.findViewById(R.id.onboard_with_id_form);
        mOnboardWithVenderIDFormView = view.findViewById(R.id.onboard_with_vender_id_form);
        mOnboardFormView = view.findViewById(R.id.onboard_form);
        mOnboardedFormView = view.findViewById(R.id.onboarded_form);
        mOnboardOperationFormView = view.findViewById(R.id.onboard_operation_form);
        final Button buttonOnboard = (Button) view.findViewById(R.id.buttonOnboard);
        final EditText editTextThingID = (EditText) view.findViewById(R.id.editTextThingId);
        final EditText editTextThingPassword = (EditText) view.findViewById(R.id.editTextThingPassword);
        final EditText editTextVenderThingID = (EditText) view.findViewById(R.id.editTextVenderThingId);
        final EditText editTextVenderThingPassword = (EditText) view.findViewById(R.id.editTextVenderThingPassword);
        final EditText editTextThingType = (EditText) view.findViewById(R.id.editTextThingType);
        final Switch aSwitch = (Switch) view.findViewById(R.id.switchThingIDType);

        txtThingId = (TextView)view.findViewById(R.id.textThingId);
        txtVenderThingId = (TextView)view.findViewById(R.id.textVenderThingId);
        txtThingType = (TextView)view.findViewById(R.id.textThingType);
        txtVender = (TextView)view.findViewById(R.id.textVender);
        txtProductName = (TextView)view.findViewById(R.id.textProductName);
        txtFirmwareVersion = (TextView)view.findViewById(R.id.textFirmwareVersion);
        txtLot = (TextView)view.findViewById(R.id.textLot);

        showVenderThingForm(false);
        buttonOnboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (api.onboarded()) {
                }
                if (aSwitch.isChecked()) {
                    final String venderThingID = editTextVenderThingID.getText().toString();
                    final String thingPassword = editTextVenderThingPassword.getText().toString();
                    final String thingType = editTextThingType.getText().toString();
                    IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(api);
                    wp.onboard(venderThingID, thingPassword, thingType).then(new DoneCallback<Target>() {
                        @Override
                        public void onDone(Target result) {
                            Toast.makeText(getContext(), "On board succeeded!", Toast.LENGTH_LONG).show();
                            showOnboardedForm(api.onboarded());
                        }
                    }, new FailCallback<Throwable>() {
                        @Override
                        public void onFail(Throwable result) {
                            Toast.makeText(getContext(), "On board failed!: " + result.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    final String thingID = editTextThingID.getText().toString();
                    final String thingPassword = editTextThingPassword.getText().toString();
                    IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(api);
                    wp.onboard(thingID, thingPassword).then(new DoneCallback<Target>() {
                        @Override
                        public void onDone(Target result) {
                            Toast.makeText(getContext(), "On board succeeded!", Toast.LENGTH_LONG).show();
                        }
                    }, new FailCallback<Throwable>() {
                        @Override
                        public void onFail(Throwable result) {
                            Toast.makeText(getContext(), "On board failed!: " + result.getMessage(), Toast.LENGTH_LONG).show();
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
        showOnboardedForm(api.onboarded());
        return view;
    }
    @Override
    public void onVisible(boolean visible) {
        if (api != null) {
            showOnboardedForm(api.onboarded());
        }
    }
    private void showVenderThingForm(boolean useVenderThingID) {
        mOnboardWithVenderIDFormView.setVisibility(useVenderThingID ? View.VISIBLE : View.GONE);
        mOnboardWithIDFormView.setVisibility(!useVenderThingID ? View.VISIBLE : View.GONE);
    }
    private void showOnboardedForm(boolean onboarded) {
        if (onboarded) {
            mOnboardedFormView.setVisibility(View.VISIBLE);
            mOnboardFormView.setVisibility(View.GONE);
            mOnboardOperationFormView.setVisibility(View.GONE);
            KiiCloudPromiseAPIWrapper wp = new KiiCloudPromiseAPIWrapper(this.api);
            wp.loadWithThingID(api.getTarget().getTypedID().getID()).then(new DoneCallback<KiiThing>() {
                @Override
                public void onDone(KiiThing thing) {
                    txtThingId.setText(thing.getID());
                    if (thing.getVendorThingID() != null) {
                        txtVenderThingId.setText(thing.getVendorThingID());
                    } else {
                        txtVenderThingId.setText("---");
                    }
                    if (thing.getThingType() != null) {
                        txtThingType.setText(thing.getThingType());
                    } else {
                        txtThingType.setText("---");
                    }
                    if (thing.getVendor() != null) {
                        txtVender.setText(thing.getVendor());
                    } else {
                        txtVender.setText("---");
                    }
                    if (thing.getProductName() != null) {
                        txtProductName.setText(thing.getProductName());
                    } else {
                        txtProductName.setText("---");
                    }
                    if (thing.getFirmwareVersion() != null) {
                        txtFirmwareVersion.setText(thing.getFirmwareVersion());
                    } else {
                        txtFirmwareVersion.setText("---");
                    }
                    if (thing.getLot() != null) {
                        txtLot.setText(thing.getLot());
                    } else {
                        txtLot.setText("---");
                    }
                }
            }, new FailCallback<Throwable>() {
                @Override
                public void onFail(Throwable result) {
                    Toast.makeText(getContext(), "Unable to get target thing!: " + result.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            mOnboardedFormView.setVisibility(View.GONE);
            mOnboardFormView.setVisibility(View.VISIBLE);
            mOnboardOperationFormView.setVisibility(View.VISIBLE);
        }
    }
}
