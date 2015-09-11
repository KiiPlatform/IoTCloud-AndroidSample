package com.kii.iotcloudsample.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.trigger.Trigger;
import com.kii.iotcloudsample.R;

public class TriggerDetailFragment extends DialogFragment {

    private IoTCloudAPI api;
    private Trigger trigger;

    public TriggerDetailFragment() {
        // Required empty public constructor
    }

    public static TriggerDetailFragment newFragment(IoTCloudAPI api, Trigger trigger) {
        TriggerDetailFragment fragment = new TriggerDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("IoTCloudAPI", api);
        arguments.putParcelable("Trigger", trigger);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("IoTCloudAPI", this.api);
        outState.putParcelable("Trigger", this.trigger);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.api = savedInstanceState.getParcelable("IoTCloudAPI");
            this.trigger = savedInstanceState.getParcelable("Trigger");
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.api = arguments.getParcelable("IoTCloudAPI");
            this.trigger = arguments.getParcelable("Trigger");
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.trigger_detail_view, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

}
