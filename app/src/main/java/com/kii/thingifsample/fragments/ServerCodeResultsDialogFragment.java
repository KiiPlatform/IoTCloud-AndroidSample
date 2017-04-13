package com.kii.thingifsample.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.thingif.ThingIFAPI;
import com.kii.thingif.exception.StoredInstanceNotFoundException;
import com.kii.thingif.exception.UnloadableInstanceVersionException;
import com.kii.thingif.trigger.TriggeredServerCodeResult;
import com.kii.thingifsample.R;
import com.kii.thingifsample.adapter.ImageViewHolder;
import com.kii.thingifsample.promise_api_wrapper.IoTCloudPromiseAPIWrapper;

import org.jdeferred.AlwaysCallback;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;

import java.util.Date;
import java.util.List;

public class ServerCodeResultsDialogFragment extends DialogFragment {

    private ThingIFAPI api;
    private String triggerID;
    private ListView lstServerCodeResults;
    private TriggeredServerCodeResultArrayAdapter adapter;
    private ProgressBar progressLoading;

    public ServerCodeResultsDialogFragment() {
        // Required empty public constructor
    }

    public static ServerCodeResultsDialogFragment newFragment(String triggerID) {
        ServerCodeResultsDialogFragment fragment = new ServerCodeResultsDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString("triggerID", triggerID);
        fragment.setArguments(arguments);
        return fragment;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            this.api = ThingIFAPI.loadFromStoredInstance(this.getContext());
        } catch (StoredInstanceNotFoundException e) {
            e.printStackTrace();
        } catch (UnloadableInstanceVersionException e) {
            e.printStackTrace();
        }

        Bundle arguments = getArguments();
        if (arguments != null) {
            this.triggerID = arguments.getString("triggerID");
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.server_code_results_view, null);

        String thingID = "---";
        if (this.api != null && this.api.onboarded() && this.api.getTarget() != null) {
            thingID = this.api.getTarget().getTypedID().getID();
        }
        String caption = ((TextView)view.findViewById(R.id.textServerCodeResults)).getText().toString();
        caption = caption.replace ("${thingID}", thingID);
        ((TextView)view.findViewById(R.id.textServerCodeResults)).setText(caption);

        this.lstServerCodeResults = (ListView) view.findViewById(R.id.listViewResults);
        this.lstServerCodeResults.setEmptyView(view.findViewById(R.id.textListEmpty));
        this.adapter = new TriggeredServerCodeResultArrayAdapter(getContext());
        this.lstServerCodeResults.setAdapter(this.adapter);
        this.progressLoading = (ProgressBar) view.findViewById(R.id.progressLoading);
        loadResultList();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("OK", null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        return dialog;
    }

    private void loadResultList() {
        if (this.api != null) {
            IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(this.api);
            this.showLoading(true);
            wp.listTriggeredServerCodeResults(this.triggerID).then(new DoneCallback<List<TriggeredServerCodeResult>>() {
                @Override
                public void onDone(List<TriggeredServerCodeResult> results) {
                    adapter.clear();
                    adapter.addAll(results);
                    adapter.notifyDataSetChanged();
                }
            }, new FailCallback<Throwable>() {
                @Override
                public void onFail(Throwable result) {
                    Toast.makeText(getContext(), "Unable to list triggers!: " + result.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).always(new AlwaysCallback<List<TriggeredServerCodeResult>, Throwable>() {
                @Override
                public void onAlways(Promise.State state, List<TriggeredServerCodeResult> resolved, Throwable rejected) {
                    showLoading(false);
                }
            });
        }
    }
    private void showLoading(boolean loading) {
        if (this.progressLoading != null && this.lstServerCodeResults != null) {
            if (loading) {
                this.progressLoading.setVisibility(View.VISIBLE);
                this.lstServerCodeResults.setVisibility(View.GONE);
            } else {
                this.progressLoading.setVisibility(View.GONE);
                this.lstServerCodeResults.setVisibility(View.VISIBLE);
            }
        }
    }
    private class TriggeredServerCodeResultArrayAdapter extends ArrayAdapter<TriggeredServerCodeResult> {
        private final LayoutInflater inflater;
        private TriggeredServerCodeResultArrayAdapter(Context context) {
            super(context, R.layout.trigger_list_item);
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageViewHolder holder = null;
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.server_code_result_list_item, parent, false);
                holder = new ImageViewHolder();
                holder.icon = (ImageView)convertView.findViewById(R.id.row_icon);
                holder.text = (TextView)convertView.findViewById(R.id.row_text);
                holder.rightText = (TextView)convertView.findViewById(R.id.right_text);
                convertView.setTag(holder);
            } else {
                holder = (ImageViewHolder)convertView.getTag();
            }
            TriggeredServerCodeResult item = this.getItem(position);
            if (item.isSucceeded()) {
                holder.icon.setImageResource(R.drawable.ic_check_circle_outline_black_36dp);
                holder.text.setText("Succeeded");
            } else {
                holder.icon.setImageResource(R.drawable.ic_close_circle_outline_black_36dp);
                holder.text.setText(item.getError().getErrorMessage());
            }
            holder.rightText.setText(new Date(item.getExecutedAt()).toString());
            return convertView;
        }
    }
}
