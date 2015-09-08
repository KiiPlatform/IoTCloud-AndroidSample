package com.kii.iotcloudsample.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.Target;
import com.kii.iotcloud.command.Command;
import com.kii.iotcloudsample.CreateCommandActivity;
import com.kii.iotcloudsample.R;
import com.kii.iotcloudsample.adapter.ImageViewHolder;
import com.kii.iotcloudsample.promise_api_wrapper.IoTCloudPromiseAPIWrapper;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommandsFragment extends Fragment implements PagerFragment {

    private IoTCloudAPI api;
    private CommandArrayAdapter adapter;
    private ListView lstCommands;
    private Button btnNewCommand;
    private Button btnRefreshCommands;

    public CommandsFragment() {
        // Required empty public constructor
    }

    public static CommandsFragment newCommandsFragment(IoTCloudAPI api) {
        CommandsFragment fragment = new CommandsFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("IoTCloudAPI", api);
        fragment.setArguments(arguments);
        return fragment;
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
            this.api = (IoTCloudAPI) savedInstanceState.getParcelable("IoTCloudAPI");
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.api = (IoTCloudAPI) arguments.getParcelable("IoTCloudAPI");
        }
        View view = inflater.inflate(R.layout.commands_view, null);
        String caption = ((TextView)view.findViewById(R.id.textCommands)).getText().toString();
        caption = caption.replace ("${thingID}", this.api.onboarded() ? this.api.getTarget().getID().getID() : "---");
        ((TextView)view.findViewById(R.id.textCommands)).setText(caption);

        this.btnNewCommand = (Button) view.findViewById(R.id.buttonNewCommand);
        this.btnNewCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(getContext(), CreateCommandActivity.class);
                i.putExtra("IoTCloudAPI", api);
                startActivityForResult(i, 0);
            }
        });
        this.btnRefreshCommands = (Button) view.findViewById(R.id.buttonRefreshCommands);
        this.btnRefreshCommands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadCommandList();
            }
        });
        this.lstCommands = (ListView) view.findViewById(R.id.listViewCommands);
        this.adapter = new CommandArrayAdapter(getContext());
        this.loadCommandList();
        this.lstCommands.setAdapter(this.adapter);

        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            this.loadCommandList();
        }
    }

    @Override
    public void onVisible(boolean visible) {
        if (visible) {
            this.btnNewCommand.setEnabled(this.api.onboarded());
        }
    }
    private void loadCommandList() {
        IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(api);
        wp.listCommands().then(new DoneCallback<List<Command>>() {
            @Override
            public void onDone(List<Command> commands) {
                adapter.clear();
                adapter.addAll(commands);
                adapter.notifyDataSetChanged();
            }
        }, new FailCallback<Throwable>() {
            @Override
            public void onFail(Throwable result) {
                Toast.makeText(getContext(), "Unable to list commands: !" + result.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private class CommandArrayAdapter extends ArrayAdapter<Command> {
        private final LayoutInflater inflater;
        private CommandArrayAdapter(Context context) {
            super(context, R.layout.command_list_item);
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageViewHolder holder = null;
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.command_list_item, parent, false);
                holder = new ImageViewHolder();
                holder.icon = (ImageView)convertView.findViewById(R.id.row_icon);
                holder.text = (TextView)convertView.findViewById(R.id.row_text);
                convertView.setTag(holder);
            } else {
                holder = (ImageViewHolder)convertView.getTag();
            }
            Command item = this.getItem(position);
            holder.text.setText(item.getCommandID());
            return convertView;
        }
    }

}
