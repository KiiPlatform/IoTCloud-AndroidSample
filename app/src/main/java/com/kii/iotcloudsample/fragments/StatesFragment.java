package com.kii.iotcloudsample.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloudsample.R;
import com.kii.iotcloudsample.promise_api_wrapper.IoTCloudPromiseAPIWrapper;
import com.kii.iotcloudsample.smart_light_demo.LightState;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatesFragment extends Fragment implements PagerFragment {

    private IoTCloudAPI api;
    private TextView txtPower;
    private TextView txtBrightness;
    private TextView txtColor;
    private TextView txtColorTemperature;

    public StatesFragment() {
        // Required empty public constructor
    }

    public static StatesFragment newFragment(IoTCloudAPI api) {
        StatesFragment fragment = new StatesFragment();
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
            this.api = savedInstanceState.getParcelable("IoTCloudAPI");
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.api = arguments.getParcelable("IoTCloudAPI");
        }
        View view = inflater.inflate(R.layout.states_view, null);
        String caption = ((TextView)view.findViewById(R.id.textInformation)).getText().toString();
        caption = caption.replace ("${thingID}", this.api.onboarded() ? this.api.getTarget().getID().getID() : "---");
        ((TextView)view.findViewById(R.id.textInformation)).setText(caption);

        this.txtPower = (TextView)view.findViewById(R.id.textPower);
        this.txtBrightness = (TextView)view.findViewById(R.id.textBrightness);
        this.txtColor = (TextView)view.findViewById(R.id.textColor);
        this.txtColorTemperature = (TextView)view.findViewById(R.id.textColorTemperature);

        return view;
    }
    @Override
    public void onVisible(boolean visible) {
        if (visible) {
            this.loadLightState();
        }
    }
    private void loadLightState() {
        IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(api);
        wp.getLightState().then(new DoneCallback<LightState>() {
            @Override
            public void onDone(LightState state) {
                if (state != null) {
                    txtPower.setText(state.power ? "ON" : "OFF");
                    txtBrightness.setText(String.valueOf(state.brightness));
                    txtColor.setText("#"
                            + Integer.toHexString(state.color[0])
                            + Integer.toHexString(state.color[1])
                            + Integer.toHexString(state.color[2]));
                    txtColorTemperature.setText(String.valueOf(state.colorTemperature));
                } else {
                    txtPower.setText("---");
                    txtBrightness.setText("---");
                    txtColor.setText("---");
                    txtColorTemperature.setText("---");
                }
            }
        }, new FailCallback<Throwable>() {
            @Override
            public void onFail(Throwable result) {
                Toast.makeText(getContext(), "Unable to get the LightState!: " + result.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
