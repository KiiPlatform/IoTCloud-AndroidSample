package com.kii.iotcloudsample.fragments.wizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.command.Action;
import com.kii.iotcloudsample.R;
import com.kii.iotcloudsample.smart_light_demo.SetBrightness;
import com.kii.iotcloudsample.smart_light_demo.SetColor;
import com.kii.iotcloudsample.smart_light_demo.SetColorTemperature;
import com.kii.iotcloudsample.smart_light_demo.TurnPower;

public class CreateTriggerCommandFragment extends WizardFragment {

    public static final String TAG = CreateTriggerCommandFragment.class.getSimpleName();

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

    public static CreateTriggerCommandFragment newFragment(IoTCloudAPI api) {
        CreateTriggerCommandFragment fragment = new CreateTriggerCommandFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("IoTCloudAPI", api);
        fragment.setArguments(arguments);
        return fragment;
    }

    public CreateTriggerCommandFragment() {
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
        View view = inflater.inflate(R.layout.create_trigger_command_view, null);
        this.chkPower = (CheckBox)view.findViewById(R.id.checkboxPower);
        this.chkPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchPower.setEnabled(isChecked);
                validateRequiredField();
            }
        });
        this.switchPower = (Switch)view.findViewById(R.id.switchPower);
        this.chkBrightness = (CheckBox)view.findViewById(R.id.checkboxBrightness);
        this.chkBrightness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                seekBrightness.setEnabled(isChecked);
                validateRequiredField();
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
                validateRequiredField();
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
                validateRequiredField();
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
        return view;
    }
    private void validateRequiredField() {
        if (!this.chkPower.isChecked() &&
            !this.chkBrightness.isChecked() &&
            !this.chkColor.isChecked() &&
            !this.chkColorTemperature.isChecked()) {
            this.setNextButtonEnabled(false);
        } else {
            this.setNextButtonEnabled(true);
        }
    }
    private void clearView() {
        this.switchPower.setChecked(false);
        this.seekBrightness.setProgress(0);
        this.seekR.setProgress(0);
        this.seekR.setProgress(0);
        this.seekB.setProgress(0);
        this.seekColorTemperature.setProgress(0);
        this.chkPower.setChecked(false);
        this.chkBrightness.setChecked(false);
        this.chkColor.setChecked(false);
        this.chkColorTemperature.setChecked(false);
    }

    @Override
    public void onActivate() {
        this.clearView();
        for (Action action : this.editingTrigger.getActions()) {
            if (action instanceof TurnPower) {
                this.chkPower.setChecked(true);
                this.switchPower.setChecked(((TurnPower)action).power);
            } else if (action instanceof SetBrightness) {
                this.chkBrightness.setChecked(true);
                this.seekBrightness.setProgress(((SetBrightness)action).brightness);
            } else if (action instanceof SetColor) {
                this.chkColor.setChecked(true);
                this.seekR.setProgress(((SetColor) action).color[0]);
                this.seekG.setProgress(((SetColor) action).color[1]);
                this.seekB.setProgress(((SetColor) action).color[2]);
            } else if (action instanceof SetColorTemperature) {
                this.chkColorTemperature.setChecked(true);
                this.seekColorTemperature.setProgress(((SetColorTemperature) action).colorTemperature);
            }
        }
        this.validateRequiredField();
    }
    @Override
    public void onInactivate(int exitCode) {
        if (exitCode == EXIT_NEXT) {
            this.editingTrigger.clearActions();
            if (this.chkPower.isChecked()) {
                this.editingTrigger.addAction(new TurnPower(this.switchPower.isChecked()));
            }
            if (this.chkBrightness.isChecked()) {
                this.editingTrigger.addAction(new SetBrightness(this.seekBrightness.getProgress()));
            }
            if (this.chkColor.isChecked()) {
                this.editingTrigger.addAction(new SetColor(this.seekR.getProgress(), this.seekG.getProgress(), this.seekB.getProgress()));
            }
            if (this.chkColorTemperature.isChecked()) {
                this.editingTrigger.addAction(new SetColorTemperature(this.seekColorTemperature.getProgress()));
            }
        }
    }
    @Override
    public String getNextButtonText() {
        return "Next";
    }
    @Override
    public String getPreviousButtonText() {
        return "Cancel";
    }
}
