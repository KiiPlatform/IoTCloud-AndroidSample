package com.kii.iotcloudsample.fragments;


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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.trigger.Trigger;
import com.kii.iotcloudsample.CreateTriggerActivity;
import com.kii.iotcloudsample.R;
import com.kii.iotcloudsample.adapter.ImageViewHolder;
import com.kii.iotcloudsample.promise_api_wrapper.IoTCloudPromiseAPIWrapper;

import org.jdeferred.AlwaysCallback;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TriggersFragment extends Fragment implements PagerFragment {

    private IoTCloudAPI api;
    private TriggerArrayAdapter adapter;
    private ListView lstTriggers;
    private ProgressBar progressLoading;
    private Button btnNewTrigger;
    private Button btnRefreshTriggers;

    public TriggersFragment() {
        // Required empty public constructor
    }

    public static TriggersFragment newTriggersFragment(IoTCloudAPI api) {
        TriggersFragment fragment = new TriggersFragment();
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
        View view = inflater.inflate(R.layout.triggers_view, null);
        String caption = ((TextView)view.findViewById(R.id.textTriggers)).getText().toString();
        caption = caption.replace ("${thingID}", this.api.onboarded() ? this.api.getTarget().getID().getID() : "---");
        ((TextView)view.findViewById(R.id.textTriggers)).setText(caption);

        this.btnNewTrigger = (Button) view.findViewById(R.id.buttonNewTrigger);
        this.btnNewTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(getContext(), CreateTriggerActivity.class);
                i.putExtra("IoTCloudAPI", api);
                startActivityForResult(i, 0);
            }
        });
        this.btnRefreshTriggers = (Button) view.findViewById(R.id.buttonRefreshTriggers);
        this.btnRefreshTriggers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTriggerList();
            }
        });
        this.lstTriggers = (ListView) view.findViewById(R.id.listViewTriggers);
        this.adapter = new TriggerArrayAdapter(getContext());
        this.loadTriggerList();
        this.lstTriggers.setAdapter(this.adapter);
        this.progressLoading = (ProgressBar) view.findViewById(R.id.progressLoading);

        return view;
    }
    @Override
    public void onVisible(boolean visible) {
        if (visible) {
            this.btnNewTrigger.setEnabled(this.api.onboarded());
            this.btnRefreshTriggers.setEnabled(this.api.onboarded());
        }
    }
    private void loadTriggerList() {
        IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(api);
        this.showLoading(true);
        wp.listTriggers().then(new DoneCallback<List<Trigger>>() {
            @Override
            public void onDone(List<Trigger> triggers) {
                adapter.clear();
                adapter.addAll(triggers);
                adapter.notifyDataSetChanged();
            }
        }, new FailCallback<Throwable>() {
            @Override
            public void onFail(Throwable result) {
                Toast.makeText(getContext(), "Unable to list triggers: !" + result.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).always(new AlwaysCallback<List<Trigger>, Throwable>() {
            @Override
            public void onAlways(Promise.State state, List<Trigger> resolved, Throwable rejected) {
                showLoading(false);
            }
        });
    }
    private void showLoading(boolean loading) {
        if (this.progressLoading != null && this.lstTriggers != null) {
            if (loading) {
                this.progressLoading.setVisibility(View.VISIBLE);
                this.lstTriggers.setVisibility(View.GONE);
            } else {
                this.progressLoading.setVisibility(View.GONE);
                this.lstTriggers.setVisibility(View.VISIBLE);
            }
        }
    }

    private class TriggerArrayAdapter extends ArrayAdapter<Trigger> {
        private final LayoutInflater inflater;
        private TriggerArrayAdapter(Context context) {
            super(context, R.layout.trigger_list_item);
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageViewHolder holder = null;
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.trigger_list_item, parent, false);
                holder = new ImageViewHolder();
                holder.icon = (ImageView)convertView.findViewById(R.id.row_icon);
                holder.text = (TextView)convertView.findViewById(R.id.row_text);
                convertView.setTag(holder);
            } else {
                holder = (ImageViewHolder)convertView.getTag();
            }
            Trigger item = this.getItem(position);
            holder.text.setText(item.getTriggerID());
            return convertView;
        }
    }

}
