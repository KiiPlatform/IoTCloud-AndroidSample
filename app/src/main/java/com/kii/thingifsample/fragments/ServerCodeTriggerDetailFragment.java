package com.kii.thingifsample.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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

import com.kii.thingif.ThingIFAPI;
import com.kii.thingif.trigger.ServerCode;
import com.kii.thingif.trigger.StatePredicate;
import com.kii.thingif.trigger.Trigger;
import com.kii.thingifsample.CreateTriggerActivity;
import com.kii.thingifsample.R;
import com.kii.thingifsample.adapter.ClauseAdapter;
import com.kii.thingifsample.adapter.ParameterArrayAdapter;
import com.kii.thingifsample.promise_api_wrapper.IoTCloudPromiseAPIWrapper;
import com.kii.thingifsample.uimodel.Clause;
import com.kii.thingifsample.uimodel.ClauseParser;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class ServerCodeTriggerDetailFragment extends DialogFragment {

    private ThingIFAPI api;
    private Trigger trigger;

    public ServerCodeTriggerDetailFragment() {
        // Required empty public constructor
    }

    public static ServerCodeTriggerDetailFragment newFragment(ThingIFAPI api, Trigger trigger, Fragment targetFragment, int requestCode) {
        ServerCodeTriggerDetailFragment fragment = new ServerCodeTriggerDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("ThingIFAPI", api);
        arguments.putParcelable("Trigger", trigger);
        fragment.setArguments(arguments);
        fragment.setTargetFragment(targetFragment, requestCode);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ThingIFAPI", this.api);
        outState.putParcelable("Trigger", this.trigger);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.api = savedInstanceState.getParcelable("ThingIFAPI");
            this.trigger = savedInstanceState.getParcelable("Trigger");
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.api = arguments.getParcelable("ThingIFAPI");
            this.trigger = arguments.getParcelable("Trigger");
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.server_code_trigger_detail_view, null, false);

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
                i.putExtra("ThingIFAPI", api);
                i.putExtra(CreateTriggerActivity.INTENT_TRIGGER, trigger);
                if (trigger.getCommand() != null) {
                    i.putExtra(CreateTriggerActivity.INTENT_TRIGGER_TYPE, CreateTriggerActivity.TriggerType.COMMAND);
                } else {
                    i.putExtra(CreateTriggerActivity.INTENT_TRIGGER_TYPE, CreateTriggerActivity.TriggerType.SERVER_CODE);
                }
                getTargetFragment().startActivityForResult(i, 0);
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

        // Show the server code info
        ServerCode serverCode = trigger.getServerCode();
        ((TextView) view.findViewById(R.id.textEndpoint)).setText(serverCode.getEndpoint());
        ((TextView) view.findViewById(R.id.textExecutorAccessToken)).setText(serverCode.getExecutorAccessToken());
        if (!TextUtils.isEmpty(serverCode.getTargetAppID())) {
            ((TextView) view.findViewById(R.id.textTargetAppID)).setText(serverCode.getTargetAppID());
        }

        ParameterArrayAdapter adapter = new ParameterArrayAdapter(getActivity());
        JSONObject parameters = serverCode.getParameters();
        if (parameters != null) {
            for(Iterator<String> keys = parameters.keys(); keys.hasNext();) {
                String key = keys.next();
                try {
                    adapter.add(new Pair<String, Object>(key, parameters.get(key)));
                } catch (JSONException e) {
                }
            }
        }
        ((ListView) view.findViewById(R.id.listViewParameters)).setAdapter(adapter);

        // Show the predicate info
        StatePredicate predicate = (StatePredicate)trigger.getPredicate();
        ((TextView) view.findViewById(R.id.textTriggersWhen)).setText(predicate.getTriggersWhen().name());

        ListView listViewCondition = (ListView)view.findViewById(R.id.listViewCondition);
        List<Clause> clauses = ClauseParser.parseClause(predicate.getCondition().getClause());

        ClauseAdapter conditionAdapter = new ClauseAdapter(getContext());
        conditionAdapter.addAll(clauses);
        listViewCondition.setAdapter(conditionAdapter);

        int height = 0;
        for (int i = 0; i < clauses.size(); i++) {
            View item = conditionAdapter.getView(i, null, listViewCondition);
            item.measure(0, 0);
            height += item.getMeasuredHeight();
        }
        ViewGroup.LayoutParams lp = listViewCondition.getLayoutParams();
        lp.height = height + (listViewCondition.getDividerHeight() * (clauses.size() - 1));
        listViewCondition.setLayoutParams(lp);

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }
}
