package com.kii.thingifsample.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.cloud.storage.utils.Log;
import com.kii.thingif.ThingIFAPI;
import com.kii.thingif.clause.query.AllClause;
import com.kii.thingif.clause.query.QueryClause;
import com.kii.thingif.exception.StoredInstanceNotFoundException;
import com.kii.thingif.exception.UnloadableInstanceVersionException;
import com.kii.thingif.query.HistoryState;
import com.kii.thingif.query.HistoryStatesQuery;
import com.kii.thingifsample.AppConstants;
import com.kii.thingifsample.R;
import com.kii.thingifsample.adapter.ClauseAdapter;
import com.kii.thingifsample.adapter.ImageViewHolder;
import com.kii.thingifsample.promise_api_wrapper.IoTCloudPromiseAPIWrapper;
import com.kii.thingifsample.smart_light_demo.LightState;
import com.kii.thingifsample.uimodel.Clause;
import com.kii.thingifsample.uimodel.ClauseParser;
import com.kii.thingifsample.utils.Utils;

import org.jdeferred.AlwaysCallback;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;

import java.util.List;

public class HistoryStatesFragment extends Fragment implements PagerFragment {

    private ThingIFAPI api;
    private QueryClause editingQueryClause;
    private ClauseAdapter clauseAdapter;
    private ListView listQueryClause;
    private ListView listHistoryStates;
    private HistoryStateArrayAdapter adapter;
    private ProgressBar progressLoading;
    private Button btnChangeQuery;

    public HistoryStatesFragment() {
        // Required empty public constructor
    }

    public static HistoryStatesFragment newFragment() {
        HistoryStatesFragment fragment = new HistoryStatesFragment();
        Bundle arguments = new Bundle();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            this.api = ThingIFAPI.loadFromStoredInstance(this.getContext());
        } catch (StoredInstanceNotFoundException e) {
            e.printStackTrace();
        } catch (UnloadableInstanceVersionException e) {
            e.printStackTrace();
        }

        View view = inflater.inflate(R.layout.history_states_view, null);
        this.listQueryClause = (ListView) view.findViewById(R.id.listViewClause);
        this.listQueryClause.setEmptyView(view.findViewById(R.id.textListViewClauseEmpty));

        this.clauseAdapter = new ClauseAdapter(getContext());
        if (this.editingQueryClause != null) {
            List<Clause> clauses = ClauseParser.parseQueryClause(this.editingQueryClause);
            this.clauseAdapter.addAll(clauses);
        }
        this.listQueryClause.setAdapter(this.clauseAdapter);

        this.btnChangeQuery = (Button) view.findViewById(R.id.buttonChangeQuery);
        this.btnChangeQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateQueryClauseFragment dialog = CreateQueryClauseFragment.newFragment(
                        HistoryStatesFragment.this,
                        editingQueryClause != null ? editingQueryClause : null);
                dialog.show(getFragmentManager(), "CreateQueryClauseFragment");
            }
        });
        this.listHistoryStates = (ListView) view.findViewById(R.id.listViewHistoryStates);
        this.adapter = new HistoryStateArrayAdapter(getContext());
        this.listHistoryStates.setAdapter(this.adapter);
        this.listHistoryStates.setEmptyView(view.findViewById(R.id.textListViewHistoryStatesEmpty));
        this.progressLoading = (ProgressBar) view.findViewById(R.id.progressLoading);

        return view;
    }
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data.hasExtra(CreateQueryClauseFragment.KEY)) {
                this.editingQueryClause = data.getParcelableExtra(CreateQueryClauseFragment.KEY);
            }
            this.clauseAdapter.clear();
            if (this.editingQueryClause != null) {
                this.clauseAdapter.addAll(ClauseParser.parseQueryClause(this.editingQueryClause));
            }
            this.clauseAdapter.notifyDataSetChanged();
            this.loadHistoryStates();
        }
    }

    @Override
    public void onVisible(boolean visible) {
        if (visible) {
            boolean enable = this.api != null && this.api.onboarded();
            this.loadHistoryStates();
        }
    }

    private void loadHistoryStates() {
        HistoryStatesQuery query = HistoryStatesQuery.Builder
                    .newBuilder(AppConstants.ALIAS,
                            editingQueryClause != null ? editingQueryClause : new AllClause())
                    .setFirmwareVersion(AppConstants.DEFAULT_FIRMWARE_VERSION)
                    .build();

        if (this.api != null) {
            IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(this.api);
            this.showLoading(true);
            wp.query(query).then(new DoneCallback<Pair<List<HistoryState<LightState>>,String>>() {
                @Override
                public void onDone(final Pair<List<HistoryState<LightState>>, String> result) {
                    adapter.clear();
                    adapter.addAll(result.first);
                    adapter.notifyDataSetChanged();
                }
            }, new FailCallback<Throwable>() {
                @Override
                public void onFail(final Throwable result) {
                    Toast.makeText(getContext(), "Unable to list history states!: " + result.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).always(new AlwaysCallback<Pair<List<HistoryState<LightState>>,String>, Throwable>() {
                @Override
                public void onAlways(Promise.State state, Pair<List<HistoryState<LightState>>,String> resolved, Throwable rejected) {
                    showLoading(false);
                }
            });
        }
    }
    private void showLoading(boolean loading) {
        if (this.progressLoading != null && this.listHistoryStates != null) {
            if (loading) {
                this.progressLoading.setVisibility(View.VISIBLE);
                this.listHistoryStates.setVisibility(View.GONE);
            } else {
                this.progressLoading.setVisibility(View.GONE);
                this.listHistoryStates.setVisibility(View.VISIBLE);
            }
        }
    }

    private class HistoryStateArrayAdapter extends ArrayAdapter<HistoryState<LightState>> {
        private final LayoutInflater inflater;
        private HistoryStateArrayAdapter(Context context) {
            super(context, R.layout.history_state_list_item);
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageViewHolder holder = null;
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.history_state_list_item, parent, false);
                holder = new ImageViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.row_icon);
                holder.text = (TextView) convertView.findViewById(R.id.row_text);
                holder.rightText = (TextView) convertView.findViewById(R.id.right_text);
                convertView.setTag(holder);
            } else {
                holder = (ImageViewHolder)convertView.getTag();
            }
            HistoryState<LightState> item = this.getItem(position);

            holder.text.setText(item.getCreatedAt().toString());
            holder.rightText.setText(Utils.lightStateToString(item.getState()));
            return convertView;
        }
    }
}
