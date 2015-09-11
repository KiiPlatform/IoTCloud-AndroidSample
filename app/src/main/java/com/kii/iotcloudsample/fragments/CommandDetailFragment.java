package com.kii.iotcloudsample.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.command.Action;
import com.kii.iotcloud.command.ActionResult;
import com.kii.iotcloud.command.Command;
import com.kii.iotcloudsample.R;
import com.kii.iotcloudsample.adapter.ActionViewHolder;
import com.kii.iotcloudsample.adapter.ImageViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.kii.iotcloudsample.R.layout.action_list_item;

public class CommandDetailFragment extends DialogFragment {

    private IoTCloudAPI api;
    private Command command;

    public CommandDetailFragment() {
        // Required empty public constructor
    }

    public static CommandDetailFragment newFragment(IoTCloudAPI api, Command command) {
        CommandDetailFragment fragment = new CommandDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("IoTCloudAPI", api);
        arguments.putParcelable("Command", command);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("IoTCloudAPI", this.api);
        outState.putParcelable("Command", this.command);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.api = savedInstanceState.getParcelable("IoTCloudAPI");
            this.command = savedInstanceState.getParcelable("Command");

        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.api = arguments.getParcelable("IoTCloudAPI");
            this.command = arguments.getParcelable("Command");
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.command_detail_view, null, false);

        ((TextView)view.findViewById(R.id.textCommandId)).setText(command.getCommandID());
        ((TextView)view.findViewById(R.id.textSchemaName)).setText(command.getSchemaName());
        ((TextView)view.findViewById(R.id.textSchemaVersion)).setText(String.valueOf(command.getSchemaVersion()));

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
        List<Pair<Action, ActionResult>> actions = new ArrayList<Pair<Action, ActionResult>>();
        for (Action action : command.getActions()) {
            ActionResult actionResult = command.getActionResult(action);
            actions.add(new Pair<Action, ActionResult>(action, actionResult));
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

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    private class ActionArrayAdapter extends ArrayAdapter<Pair<Action, ActionResult>> {
        private final LayoutInflater inflater;
        private ActionArrayAdapter(Context context) {
            super(context, action_list_item);
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ActionViewHolder holder = null;
            if (convertView == null) {
                convertView = this.inflater.inflate(action_list_item, parent, false);
                holder = new ActionViewHolder();
                holder.icon = (ImageView)convertView.findViewById(R.id.row_icon);
                holder.textCommandID = (TextView)convertView.findViewById(R.id.row_id);
                holder.textResult = (TextView)convertView.findViewById(R.id.row_result);
                convertView.setTag(holder);
            } else {
                holder = (ActionViewHolder)convertView.getTag();
            }
            Pair<Action, ActionResult> item = this.getItem(position);
            holder.textCommandID.setText(item.first.getActionName());
            if (item.second == null) {
                holder.textResult.setText("unfinished");
            } else {
                if (item.second.succeeded) {
                    holder.textResult.setText("succeeded");
                } else {
                    holder.textResult.setText("failed");
                }
            }
            return convertView;
        }
    }
}
