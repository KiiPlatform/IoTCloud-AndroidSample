package com.kii.thingifsample.fragments.wizard;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.thingif.ThingIFAPI;
import com.kii.thingif.TypedID;
import com.kii.thingif.command.Action;
import com.kii.thingifsample.R;
import com.kii.thingifsample.smart_light_demo.SetBrightness;
import com.kii.thingifsample.smart_light_demo.SetColor;
import com.kii.thingifsample.smart_light_demo.SetColorTemperature;
import com.kii.thingifsample.smart_light_demo.TurnPower;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateTriggerCommandFragment extends WizardFragment {

    public static final String TAG = CreateTriggerCommandFragment.class.getSimpleName();

    private ThingIFAPI api;
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
    private EditText editTextTargetID;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextMetadata;

    public static CreateTriggerCommandFragment newFragment(ThingIFAPI api) {
        CreateTriggerCommandFragment fragment = new CreateTriggerCommandFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("ThingIFAPI", api);
        fragment.setArguments(arguments);
        return fragment;
    }

    public CreateTriggerCommandFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ThingIFAPI", this.api);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.api = savedInstanceState.getParcelable("ThingIFAPI");
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.api = arguments.getParcelable("ThingIFAPI");
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
        this.editTextTargetID = (EditText)view.findViewById(R.id.editTextTargetID);
        this.editTextTitle = (EditText)view.findViewById(R.id.editTextTitle);
        this.editTextDescription = (EditText)view.findViewById(R.id.editTextDescription);
        this.editTextMetadata = (EditText)view.findViewById(R.id.editTextMetadata);
        this.onActivate();
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
    private void clearEditingTrigger() {
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
        this.clearEditingTrigger();
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
        if (this.editingTrigger.getCommandTargetID() != null) {
            this.editTextTargetID.setText(this.editingTrigger.getCommandTargetID().getID());
        }
        if (this.editingTrigger.getCommandTitle() != null) {
            this.editTextTitle.setText(this.editingTrigger.getCommandTitle());
        }
        if (this.editingTrigger.getCommandDescription() != null) {
            this.editTextDescription.setText(this.editingTrigger.getCommandDescription());
        }
        if (this.editingTrigger.getCommandMetadata() != null) {
            this.editTextMetadata.setText(this.editingTrigger.getCommandMetadata().toString());
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
            if (!TextUtils.isEmpty(this.editTextTargetID.getText().toString())) {
                this.editingTrigger.setCommandTargetID(
                        new TypedID(TypedID.Types.THING, this.editTextTargetID.getText().toString()));
            }
            if (!TextUtils.isEmpty(this.editTextTitle.getText().toString())) {
                this.editingTrigger.setCommandTitle(this.editTextTitle.getText().toString());
            }
            if (!TextUtils.isEmpty(this.editTextDescription.getText().toString())) {
                this.editingTrigger.setCommandDescription(this.editTextDescription.getText().toString());
            }
            if (!TextUtils.isEmpty(this.editTextMetadata.getText().toString())) {
                try {
                    this.editingTrigger.setCommandMetadata(
                            new JSONObject(this.editTextMetadata.getText().toString()));
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Metadata to JSON failed.", Toast.LENGTH_LONG).show();
                }
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
