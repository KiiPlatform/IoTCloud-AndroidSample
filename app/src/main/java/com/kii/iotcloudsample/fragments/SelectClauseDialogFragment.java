package com.kii.iotcloudsample.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kii.iotcloudsample.R;
import com.kii.iotcloudsample.adapter.ImageViewHolder;
import com.kii.iotcloudsample.uimodel.Clause;

public class SelectClauseDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    public static final String EXTRA_CLAUSE_TYPE = "EXTRA_CLAUSE_TYPE";
    private ListView listView;

    public static SelectClauseDialogFragment newFragment(Fragment target, int requestCode) {
        SelectClauseDialogFragment fragment = new SelectClauseDialogFragment();
        fragment.setTargetFragment(target, requestCode);
        return fragment;
    }
    public SelectClauseDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.select_clause_dialog_view, null, false);

        this.listView = (ListView)view.findViewById(R.id.list_view);
        this.listView.setItemsCanFocus(false);
        ClauseListAdapter adapter = new ClauseListAdapter(getActivity());
        adapter.addAll(Clause.ALL_CLAUSES);
        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        builder.setView(view);
        return builder.create();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment target = getTargetFragment();
        if (target != null) {
            Clause clause = (Clause)parent.getItemAtPosition(position);
            Intent data = new Intent();
            data.putExtra(EXTRA_CLAUSE_TYPE, clause.getType());
            target.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
        }
        dismiss();
    }

    private class ClauseListAdapter extends ArrayAdapter<Clause> {
        private final LayoutInflater inflater;
        private ClauseListAdapter(Context context) {
            super(context, R.layout.image_list_item);
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
            Clause item = this.getItem(position);
            holder.text.setText(item.getSummary());
            holder.icon.setImageResource(item.getIcon());
            return convertView;
        }
    }
}
