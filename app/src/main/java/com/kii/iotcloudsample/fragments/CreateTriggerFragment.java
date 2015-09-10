package com.kii.iotcloudsample.fragments;

import android.os.Bundle;

import com.kii.iotcloud.IoTCloudAPI;

import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.layouts.BasicWizardLayout;

public class CreateTriggerFragment extends BasicWizardLayout {

    public static final String TAG = CreateTriggerFragment.class.getSimpleName();

    private IoTCloudAPI api;

    public static CreateTriggerFragment newFragment(IoTCloudAPI api) {
        CreateTriggerFragment fragment = new CreateTriggerFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("IoTCloudAPI", api);
        fragment.setArguments(arguments);
        return fragment;
    }

    public CreateTriggerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("IoTCloudAPI", this.api);
    }

    @Override
    public WizardFlow onSetup() {
        this.setNextButtonText("Next");
        this.setBackButtonText("Previous");
        this.setFinishButtonText("Create");
        return new WizardFlow.Builder()
                .addStep(CreateTriggerCommandFragment.class, true)
                .addStep(CreateTriggerPredicateFragment.class, true)
                .create();
    }
}
