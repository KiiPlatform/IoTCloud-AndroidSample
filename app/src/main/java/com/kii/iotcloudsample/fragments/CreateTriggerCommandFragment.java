package com.kii.iotcloudsample.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.kii.iotcloudsample.R;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

public class CreateTriggerCommandFragment extends WizardStep {

    public static final String TAG = CreateTriggerCommandFragment.class.getSimpleName();

    @ContextVariable
    private boolean turnPowerEnabled;
    @ContextVariable
    private boolean setBrightnessEnabled;
    @ContextVariable
    private boolean setColorEnabled;
    @ContextVariable
    private boolean setColorTemperatureEnabled;
    @ContextVariable
    private boolean power;
    @ContextVariable
    private int brightness;
    @ContextVariable
    private int colorR;
    @ContextVariable
    private int colorG;
    @ContextVariable
    private int colorB;
    @ContextVariable
    private int colorTemperature;

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

    public CreateTriggerCommandFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        this.validateRequiredField();
        this.loadDataFields();
        return view;
    }
    @Override
    public void onExit(int exitCode) {
        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                this.saveDataFields();
                break;
            case WizardStep.EXIT_PREVIOUS:
                break;
        }
    }
    private void validateRequiredField() {
        if (!this.chkPower.isChecked() &&
            !this.chkBrightness.isChecked() &&
            !this.chkColor.isChecked() &&
            !this.chkColorTemperature.isChecked()) {
            this.notifyIncomplete();
        } else {
            this.notifyCompleted();
        }
    }
    private void loadDataFields() {
        this.switchPower.setChecked(this.power);
        this.seekBrightness.setProgress(this.brightness);
        this.seekR.setProgress(this.colorR);
        this.seekG.setProgress(this.colorG);
        this.seekB.setProgress(this.colorB);
        this.seekColorTemperature.setProgress(this.colorTemperature);
    }
    private void saveDataFields() {
        this.turnPowerEnabled = this.chkPower.isChecked();
        if (chkPower.isChecked()) {
            this.power = this.switchPower.isChecked();
        }
        this.setBrightnessEnabled = this.chkBrightness.isChecked();
        if (chkBrightness.isChecked()) {
            this.brightness = this.seekBrightness.getProgress();
        }
        this.setColorEnabled = this.chkColor.isChecked();
        if (this.chkColor.isChecked()) {
            this.colorR = this.seekR.getProgress();
            this.colorG = this.seekG.getProgress();
            this.colorB = this.seekB.getProgress();
        }
        this.setColorTemperatureEnabled = this.chkColorTemperature.isChecked();
        if (this.chkColorTemperature.isChecked()) {
            this.colorTemperature = this.seekColorTemperature.getProgress();
        }
    }
}
