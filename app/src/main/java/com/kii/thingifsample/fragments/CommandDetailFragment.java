package com.kii.thingifsample.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.kii.thingif.ThingIFAPI;
import com.kii.thingif.command.Action;
import com.kii.thingif.command.ActionResult;
import com.kii.thingif.command.AliasAction;
import com.kii.thingif.command.AliasActionResult;
import com.kii.thingif.command.Command;
import com.kii.thingif.exception.StoredInstanceNotFoundException;
import com.kii.thingif.exception.UnloadableInstanceVersionException;
import com.kii.thingifsample.R;
import com.kii.thingifsample.adapter.ActionArrayAdapter;
import com.kii.thingifsample.smart_light_demo.BaseAction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandDetailFragment extends DialogFragment {

    private Command command;

    public CommandDetailFragment() {
        // Required empty public constructor
    }

    public static CommandDetailFragment newFragment(Command command, Fragment targetFragment, int requestCode) {
        CommandDetailFragment fragment = new CommandDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("Command", command);
        fragment.setArguments(arguments);
        fragment.setTargetFragment(targetFragment, requestCode);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("Command", this.command);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.command = savedInstanceState.getParcelable("Command");
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.command = arguments.getParcelable("Command");
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.command_detail_view, null, false);

        ((TextView)view.findViewById(R.id.textCommandId)).setText(command.getCommandID());
        //((TextView)view.findViewById(R.id.textSchemaName)).setText(command.getSchemaName());
        //((TextView)view.findViewById(R.id.textSchemaVersion)).setText(String.valueOf(command.getSchemaVersion()));

        ((TextView)view.findViewById(R.id.textTargetID)).setText(command.getTargetID().getID());
        if (command.getIssuerID() != null) {
            ((TextView) view.findViewById(R.id.textIssuerID)).setText(command.getIssuerID().getID());
        } else {
            ((TextView) view.findViewById(R.id.textIssuerID)).setText("---");
        }
        if (command.getCommandState() != null) {
            ((TextView) view.findViewById(R.id.textCommandState)).setText(command.getCommandState().name());
        } else {
            ((TextView) view.findViewById(R.id.textCommandState)).setText("---");
        }
        if (command.getFiredByTriggerID() != null) {
            ((TextView) view.findViewById(R.id.textFiredByTriggerID)).setText(command.getFiredByTriggerID());
        } else {
            ((TextView) view.findViewById(R.id.textFiredByTriggerID)).setText("---");
        }
        ((TextView)view.findViewById(R.id.textCreated)).setText(new Date(command.getCreated()).toString());
        ((TextView)view.findViewById(R.id.textModified)).setText(new Date(command.getModified()).toString());

        ListView listViewActions = (ListView)view.findViewById(R.id.listViewActions);
        List<Pair<Action, ActionResult>> actions = new ArrayList<>();
        for (AliasAction aliasAction : command.getAliasActions()) {
            for (Action action : aliasAction.getActions()) {
                List<ActionResult> actionResults = command.getActionResult(aliasAction.getAlias(),
                        ((BaseAction) action).getActionName());
                if (actionResults.isEmpty()) {
                    actions.add(new Pair<Action, ActionResult>(action, null));
                } else {
                    // Note: actionResults has only one because of UI.
                    for (ActionResult actionResult : actionResults) {
                        actions.add(new Pair<Action, ActionResult>(action, actionResult));
                    }
                }
            }
        }
        ActionArrayAdapter adapter = new ActionArrayAdapter(getContext());
        adapter.addAll(actions);
        listViewActions.setAdapter(adapter);

        int height = 0;
        for (int i = 0; i < actions.size(); i++) {
            View item = adapter.getView(i, null, listViewActions);
            item.measure(0, 0);
            height += item.getMeasuredHeight();
        }
        ViewGroup.LayoutParams lp = listViewActions.getLayoutParams();
        lp.height = height + (listViewActions.getDividerHeight() * (actions.size() - 1));
        listViewActions.setLayoutParams(lp);

        if (!TextUtils.isEmpty(command.getTitle())) {
            ((TextView) view.findViewById(R.id.textTitle)).setText(command.getTitle());
        } else {
            ((TextView) view.findViewById(R.id.textTitle)).setText("---");
        }
        if (!TextUtils.isEmpty(command.getDescription())) {
            ((TextView) view.findViewById(R.id.textDescription)).setText(command.getDescription());
        } else {
            ((TextView) view.findViewById(R.id.textDescription)).setText("---");
        }
        if (command.getMetadata() != null) {
            ((TextView)view.findViewById(R.id.textMetadata)).setText(command.getMetadata().toString());
        } else {
            ((TextView) view.findViewById(R.id.textMetadata)).setText("---");
        }

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

}
