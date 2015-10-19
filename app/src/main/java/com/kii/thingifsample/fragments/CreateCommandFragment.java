package com.kii.thingifsample.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.command.Action;
import com.kii.iotcloud.command.Command;
import com.kii.thingifsample.AppConstants;
import com.kii.thingifsample.R;
import com.kii.thingifsample.promise_api_wrapper.IoTCloudPromiseAPIWrapper;
import com.kii.thingifsample.smart_light_demo.SetBrightness;
import com.kii.thingifsample.smart_light_demo.SetColor;
import com.kii.thingifsample.smart_light_demo.SetColorTemperature;
import com.kii.thingifsample.smart_light_demo.TurnPower;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateCommandFragment extends Fragment {

    public static final String TAG = CreateCommandFragment.class.getSimpleName();

    private IoTCloudAPI api;
    private CheckBox chkPower;
    private Switch switchPower;
    private CheckBox chkBrightness;
    private SeekBar seekBrightness;
    private CheckBox chkColor;
    private TextView txtR;
    private SeekBar seekR;
    private TextView txtG;
    private SeekBar seekG;
    private TextView txtB;
    private SeekBar seekB;
    private CheckBox chkColorTemperature;
    private SeekBar seekColorTemperature;
    private Button btnSendCommand;

    public static CreateCommandFragment newFragment(IoTCloudAPI api) {
        CreateCommandFragment fragment = new CreateCommandFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("IoTCloudAPI", api);
        fragment.setArguments(arguments);
        return fragment;
    }

    public CreateCommandFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.create_command_view, null);
        this.chkPower = (CheckBox)view.findViewById(R.id.checkboxPower);
        this.chkPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchPower.setEnabled(isChecked);
            }
        });
        this.switchPower = (Switch)view.findViewById(R.id.switchPower);
        this.chkBrightness = (CheckBox)view.findViewById(R.id.checkboxBrightness);
        this.chkBrightness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                seekBrightness.setEnabled(isChecked);
            }
        });
        this.seekBrightness = (SeekBar)view.findViewById(R.id.seekBarBrightness);
        this.seekBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                chkBrightness.setText("Brightness: " + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.chkColor = (CheckBox)view.findViewById(R.id.checkboxColor);
        this.chkColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                txtR.setEnabled(isChecked);
                seekR.setEnabled(isChecked);
                txtG.setEnabled(isChecked);
                seekG.setEnabled(isChecked);
                txtB.setEnabled(isChecked);
                seekB.setEnabled(isChecked);
            }
        });
        this.txtR = (TextView)view.findViewById(R.id.textR);
        this.seekR = (SeekBar)view.findViewById(R.id.seekBarR);
        this.seekR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtR.setText("R: " + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.txtG = (TextView)view.findViewById(R.id.textG);
        this.seekG = (SeekBar)view.findViewById(R.id.seekBarG);
        this.seekG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtG.setText("G: " + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.txtB = (TextView)view.findViewById(R.id.textB);
        this.seekB = (SeekBar)view.findViewById(R.id.seekBarB);
        this.seekB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtB.setText("B: " + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.chkColorTemperature = (CheckBox)view.findViewById(R.id.checkboxColorTemperature);
        this.chkColorTemperature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                seekColorTemperature.setEnabled(isChecked);
            }
        });
        this.seekColorTemperature = (SeekBar)view.findViewById(R.id.seekBarColorTemperature);
        this.seekColorTemperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                chkColorTemperature.setText("Color temperature: " + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        this.btnSendCommand = (Button)view.findViewById(R.id.buttonSendCommand);
        this.btnSendCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Action> actions = new ArrayList<Action>();
                if (chkPower.isChecked()) {
                    actions.add(new TurnPower(switchPower.isChecked()));
                }
                if (chkBrightness.isChecked()) {
                    actions.add(new SetBrightness(seekBrightness.getProgress()));
                }
                if (chkColor.isChecked()) {
                    actions.add(new SetColor(seekR.getProgress(), seekG.getProgress(), seekB.getProgress()));
                }
                if (chkColorTemperature.isChecked()) {
                    actions.add(new SetColorTemperature(seekColorTemperature.getProgress()));
                }
                if (actions.size() == 0) {
                    Toast.makeText(getContext(), "Requires at least one action", Toast.LENGTH_LONG).show();
                    return;
                }
                IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(api);
                wp.postNewCommand(AppConstants.SCHEMA_NAME, AppConstants.SCHEMA_VERSION, actions).then(new DoneCallback<Command>() {
                    @Override
                    public void onDone(Command result) {
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                }, new FailCallback<Throwable>() {
                    @Override
                    public void onFail(Throwable result) {
                        Toast.makeText(getContext(), "Failed to create new command!: " + result.getMessage(), Toast.LENGTH_LONG).show();
                        getActivity().setResult(Activity.RESULT_CANCELED);
                        getActivity().finish();
                    }
                });
            }
        });
        return view;
    }
}
