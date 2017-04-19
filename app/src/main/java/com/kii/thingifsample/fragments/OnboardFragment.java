package com.kii.thingifsample.fragments;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
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

import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiThing;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.utils.Log;
import com.kii.thingif.Owner;
import com.kii.thingif.ThingIFAPI;
import com.kii.thingif.Target;
import com.kii.thingif.TypedID;
import com.kii.thingif.exception.StoredInstanceNotFoundException;
import com.kii.thingif.exception.UnloadableInstanceVersionException;
import com.kii.thingifsample.AppConstants;
import com.kii.thingifsample.promise_api_wrapper.IoTCloudPromiseAPIWrapper;
import com.kii.thingifsample.R;
import com.kii.thingifsample.promise_api_wrapper.KiiCloudPromiseAPIWrapper;
import com.kii.thingifsample.smart_light_demo.ApiBuilder;

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

    private TextView txtThingId;
    private TextView txtVenderThingId;
    private TextView txtThingType;
    private TextView txtVender;
    private TextView txtProductName;
    private TextView txtFirmwareVersion;
    private TextView txtLot;

    private ThingIFAPI api;

    public OnboardFragment() {
        // Required empty public constructor
    }

    public static OnboardFragment newFragment() {
        OnboardFragment fragment = new OnboardFragment();
        Bundle arguments = new Bundle();
        fragment.setArguments(arguments);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            this.api = ThingIFAPI.loadFromStoredInstance(this.getContext());
        } catch (StoredInstanceNotFoundException e) {
            if (KiiUser.getCurrentUser() != null) {
                Owner owner = new Owner(new TypedID(TypedID.Types.USER, KiiUser.getCurrentUser().getID()), Kii
                        .user().getAccessToken());
                this.api = ApiBuilder.buildApi(this.getContext().getApplicationContext(), owner);
            }
        } catch (UnloadableInstanceVersionException e) {
            e.printStackTrace();
        }
        View view = inflater.inflate(R.layout.onboard_view, null);
        mOnboardWithIDFormView = view.findViewById(R.id.onboard_with_id_form);
        mOnboardWithVenderIDFormView = view.findViewById(R.id.onboard_with_vender_id_form);
        mOnboardFormView = view.findViewById(R.id.onboard_form);
        mOnboardedFormView = view.findViewById(R.id.onboarded_form);
        final Button buttonOnboard = (Button) view.findViewById(R.id.buttonOnboard);
        final EditText editTextThingID = (EditText) view.findViewById(R.id.editTextThingId);
        final EditText editTextThingPassword = (EditText) view.findViewById(R.id.editTextThingPassword);
        final EditText editTextVenderThingID = (EditText) view.findViewById(R.id.editTextVenderThingId);
        final EditText editTextVenderThingPassword = (EditText) view.findViewById(R.id.editTextVenderThingPassword);
        final EditText editTextThingType = (EditText) view.findViewById(R.id.editTextThingType);
        final EditText editTextFirmwareVersion = (EditText) view.findViewById(R.id.editTextFirmwareVersion);
        final Switch aSwitch = (Switch) view.findViewById(R.id.switchThingIDType);

        editTextThingType.setText(AppConstants.DEFAULT_THING_TYPE);
        editTextFirmwareVersion.setText(AppConstants.DEFAULT_FIRMWARE_VERSION);

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
                if (api != null) {
                    if (aSwitch.isChecked()) {
                        final String venderThingID = editTextVenderThingID.getText().toString();
                        final String thingPassword = editTextVenderThingPassword.getText().toString();
                        final String thingType = editTextThingType.getText().toString();
                        final String firmwareVersion = editTextFirmwareVersion.getText().toString();
                        IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(api);
                        wp.onboard(venderThingID, thingPassword, thingType, firmwareVersion).then(new DoneCallback<Target>() {
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
                                showOnboardedForm(api.onboarded());
                            }
                        }, new FailCallback<Throwable>() {
                            @Override
                            public void onFail(Throwable result) {
                                Toast.makeText(getContext(), "On board failed!: " + result.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showVenderThingForm(isChecked);
            }
        });
        showOnboardedForm(this.api != null && this.api.onboarded());
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
            KiiCloudPromiseAPIWrapper wp = new KiiCloudPromiseAPIWrapper(this.api);
            wp.loadWithThingID(api.getTarget().getTypedID().getID()).then(new DoneCallback<KiiThing>() {
                @Override
                public void onDone(final KiiThing thing) {
                    txtThingId.setText(thing.getID());
                    if (thing.getVendorThingID() != null) {
                        txtVenderThingId.setText(thing.getVendorThingID());
                    } else {
                        txtVenderThingId.setText("---");
                    }
                    txtThingId.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (thing.getID() != null) {
                                ClipData.Item item = new ClipData.Item(thing.getID());
                                String[] mimeType = new String[1];
                                mimeType[0] = ClipDescription.MIMETYPE_TEXT_PLAIN;
                                ClipData cd = new ClipData(new ClipDescription("text_data", mimeType), item);
                                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                cm.setPrimaryClip(cd);
                                Toast.makeText(getActivity(), thing.getID() +  " copy to clipboard", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    txtVenderThingId.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (thing.getVendorThingID() != null) {
                                ClipData.Item item = new ClipData.Item(thing.getVendorThingID());
                                String[] mimeType = new String[1];
                                mimeType[0] = ClipDescription.MIMETYPE_TEXT_PLAIN;
                                ClipData cd = new ClipData(new ClipDescription("text_data", mimeType), item);
                                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                cm.setPrimaryClip(cd);
                                Toast.makeText(getActivity(), thing.getVendorThingID() +  " copy to clipboard", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
        }
    }
}
