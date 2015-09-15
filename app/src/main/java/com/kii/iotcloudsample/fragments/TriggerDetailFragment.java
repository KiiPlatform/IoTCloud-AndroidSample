package com.kii.iotcloudsample.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.Target;
import com.kii.iotcloud.command.Action;
import com.kii.iotcloud.command.ActionResult;
import com.kii.iotcloud.command.Command;
import com.kii.iotcloud.trigger.StatePredicate;
import com.kii.iotcloud.trigger.Trigger;
import com.kii.iotcloudsample.CreateTriggerActivity;
import com.kii.iotcloudsample.R;
import com.kii.iotcloudsample.adapter.ActionArrayAdapter;
import com.kii.iotcloudsample.adapter.ClauseAdapter;
import com.kii.iotcloudsample.model.Clause;
import com.kii.iotcloudsample.model.ClauseParser;
import com.kii.iotcloudsample.promise_api_wrapper.IoTCloudPromiseAPIWrapper;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TriggerDetailFragment extends DialogFragment {

    private IoTCloudAPI api;
    private Trigger trigger;

    public TriggerDetailFragment() {
        // Required empty public constructor
    }

    public static TriggerDetailFragment newFragment(IoTCloudAPI api, Trigger trigger, Fragment targetFragment, int requestCode) {
        TriggerDetailFragment fragment = new TriggerDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("IoTCloudAPI", api);
        arguments.putParcelable("Trigger", trigger);
        fragment.setArguments(arguments);
        fragment.setTargetFragment(targetFragment, requestCode);
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

        ((TextView)view.findViewById(R.id.textTriggerId)).setText(trigger.getTriggerID());
        final Switch switchTriggerEnabled = (Switch)view.findViewById(R.id.switchTriggerEnabled);
        switchTriggerEnabled.setChecked(!trigger.disabled());
        switchTriggerEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(api);
                wp.enableTrigger(trigger.getTriggerID(), isChecked).then(new DoneCallback<Trigger>() {
                    @Override
                    public void onDone(Trigger result) {
                        if (isChecked) {
                            Toast.makeText(getContext(), "Trigger is enabled!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Trigger is disabled!", Toast.LENGTH_LONG).show();
                        }
                        getTargetFragment().onActivityResult(0, Activity.RESULT_OK, null);
                    }
                }, new FailCallback<Throwable>() {
                    @Override
                    public void onFail(Throwable result) {
                        if (isChecked) {
                            Toast.makeText(getContext(), "Failed to enable this trigger!: " + result.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to disable this trigger!: " + result.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        switchTriggerEnabled.setChecked(!isChecked);
                    }
                });
            }
        });

        ((FloatingActionButton)view.findViewById(R.id.fabEditTrigger)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getContext(), CreateTriggerActivity.class);
                i.putExtra("IoTCloudAPI", api);
                i.putExtra(CreateTriggerActivity.INTENT_TRIGGER, trigger);
                getTargetFragment().startActivityForResult(i, 0);
//                startActivity(i);
                dismiss();
            }
        });
        ((FloatingActionButton)view.findViewById(R.id.fabDeleteTrigger)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to delete trigger?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(api);
                                wp.deleteTrigger(trigger.getTriggerID()).then(new DoneCallback<Trigger>() {
                                    @Override
                                    public void onDone(Trigger result) {
                                        Toast.makeText(getContext(), "Trigger is deleted!", Toast.LENGTH_LONG).show();
                                        getTargetFragment().onActivityResult(0, Activity.RESULT_OK, null);
                                        dismiss();
                                    }
                                }, new FailCallback<Throwable>() {
                                    @Override
                                    public void onFail(Throwable result) {
                                        Toast.makeText(getContext(), "Failed to delete trigger!: " + result.getMessage(), Toast.LENGTH_LONG).show();
                                        dismiss();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
            }
        });

        // Show the command info
        Command command = trigger.getCommand();
        ((TextView) view.findViewById(R.id.textSchemaName)).setText(command.getSchemaName());
        ((TextView)view.findViewById(R.id.textSchemaVersion)).setText(String.valueOf(command.getSchemaVersion()));

        ((TextView)view.findViewById(R.id.textTargetID)).setText(command.getTargetID().getID());
        if (command.getIssuerID() != null) {
            ((TextView) view.findViewById(R.id.textIssuerID)).setText(command.getIssuerID().getID());
        } else {
            ((TextView) view.findViewById(R.id.textIssuerID)).setText("---");
        }

        ListView listViewActions = (ListView)view.findViewById(R.id.listViewActions);
        List<Pair<Action, ActionResult>> actions = new ArrayList<Pair<Action, ActionResult>>();
        for (Action action : command.getActions()) {
            ActionResult actionResult = command.getActionResult(action);
            actions.add(new Pair<Action, ActionResult>(action, actionResult));
        }
        ActionArrayAdapter actionAdapter = new ActionArrayAdapter(getContext());
        actionAdapter.addAll(actions);
        listViewActions.setAdapter(actionAdapter);

        int height = 0;
        for (int i = 0; i < actions.size(); i++) {
            View item = actionAdapter.getView(i, null, listViewActions);
            item.measure(0, 0);
            height += item.getMeasuredHeight();
        }
        ViewGroup.LayoutParams lp = listViewActions.getLayoutParams();
        lp.height = height + (listViewActions.getDividerHeight() * (actions.size() - 1));
        listViewActions.setLayoutParams(lp);

        // Show the predicate info
        StatePredicate predicate = (StatePredicate)trigger.getPredicate();
        ((TextView) view.findViewById(R.id.textTriggersWhen)).setText(predicate.getTriggersWhen().name());

        ListView listViewCondition = (ListView)view.findViewById(R.id.listViewCondition);
        List<Clause> clauses = ClauseParser.parseClause(predicate.getCondition().getClause());

        ClauseAdapter conditionAdapter = new ClauseAdapter(getContext());
        conditionAdapter.addAll(clauses);
        listViewCondition.setAdapter(conditionAdapter);

        height = 0;
        for (int i = 0; i < clauses.size(); i++) {
            View item = conditionAdapter.getView(i, null, listViewCondition);
            item.measure(0, 0);
            height += item.getMeasuredHeight();
        }
        lp = listViewCondition.getLayoutParams();
        lp.height = height + (listViewCondition.getDividerHeight() * (clauses.size() - 1));
        listViewCondition.setLayoutParams(lp);

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }
}
