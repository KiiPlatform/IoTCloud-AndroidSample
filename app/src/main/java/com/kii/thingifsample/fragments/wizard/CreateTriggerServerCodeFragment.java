package com.kii.thingifsample.fragments.wizard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kii.thingif.ThingIFAPI;
import com.kii.thingifsample.R;
import com.kii.thingifsample.uimodel.Trigger;

public class CreateTriggerServerCodeFragment extends WizardFragment {

    public static final String TAG = CreateTriggerServerCodeFragment.class.getSimpleName();

    private ThingIFAPI api;
    private EditText editTextEndpoint;
    private EditText editTextExecutorAccessToken;
    private EditText editTextTargetAppID;

    public static CreateTriggerServerCodeFragment newFragment(ThingIFAPI api) {
        CreateTriggerServerCodeFragment fragment = new CreateTriggerServerCodeFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("ThingIFAPI", api);
        fragment.setArguments(arguments);
        return fragment;
    }

    public CreateTriggerServerCodeFragment() {
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
        View view = inflater.inflate(R.layout.create_trigger_servercode_view, null);

        this.editTextEndpoint = (EditText)view.findViewById(R.id.editTextEndpoint);
        this.editTextEndpoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                validateRequiredField();
            }
        });
        this.editTextExecutorAccessToken = (EditText)view.findViewById(R.id.editTextExecutorAccessToken);
        this.editTextExecutorAccessToken.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                validateRequiredField();
            }
        });
        this.editTextTargetAppID = (EditText)view.findViewById(R.id.editTextTargetAppID);

        this.onActivate();
        return view;
    }

    private void validateRequiredField() {
        if (TextUtils.isEmpty(this.editTextEndpoint.getText()) ||
            TextUtils.isEmpty(this.editTextExecutorAccessToken.getText())) {
            this.setNextButtonEnabled(false);
        } else {
            this.setNextButtonEnabled(true);
        }
    }

    @Override
    public void onActivate() {
        if (this.editingTrigger.getServerCode() == null) {
            this.editingTrigger.setServerCode(new Trigger.ServerCode());
        }
        if (TextUtils.isEmpty(this.editingTrigger.getServerCode().endpoint)) {
            this.editTextEndpoint.setText("");
        } else {
            this.editTextEndpoint.setText(this.editingTrigger.getServerCode().endpoint);
        }
        if (TextUtils.isEmpty(this.editingTrigger.getServerCode().executorAccessToken)) {
            this.editTextExecutorAccessToken.setText("");
        } else {
            this.editTextExecutorAccessToken.setText(this.editingTrigger.getServerCode().executorAccessToken);
        }
        if (TextUtils.isEmpty(this.editingTrigger.getServerCode().targetAppID)) {
            this.editTextTargetAppID.setText("");
        } else {
            this.editTextTargetAppID.setText(this.editingTrigger.getServerCode().targetAppID);
        }
        this.validateRequiredField();
    }
    @Override
    public void onInactivate(int exitCode) {
        if (exitCode == EXIT_NEXT) {
            this.editingTrigger.getServerCode().endpoint = this.editTextEndpoint.getText().toString();
            this.editingTrigger.getServerCode().executorAccessToken = this.editTextExecutorAccessToken.getText().toString();
            this.editingTrigger.getServerCode().targetAppID = this.editTextTargetAppID.getText().toString();
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
