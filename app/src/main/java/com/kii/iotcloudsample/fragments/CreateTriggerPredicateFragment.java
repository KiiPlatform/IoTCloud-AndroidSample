package com.kii.iotcloudsample.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.kii.iotcloudsample.R;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

public class CreateTriggerPredicateFragment extends WizardStep implements AdapterView.OnItemLongClickListener {

    private static final int REQUEST_CODE_ADD_CLAUSE = 100;

    @ContextVariable
    private boolean turnPowerEnabled;
    @ContextVariable
    private boolean setBrightnessEnabled;
    @ContextVariable
    private boolean setColorEnabled;
    @ContextVariable
    private boolean setColorTemperatureEnabled;
    @ContextVariable
    private boolean power;
    @ContextVariable
    private int brightness;
    @ContextVariable
    private int colorR;
    @ContextVariable
    private int colorG;
    @ContextVariable
    private int colorB;
    @ContextVariable
    private int colorTemperature;

    private DragSortListView listView;
    private DragSortController controller;
    private ClauseAdapter adapter;

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                com.kii.iotcloudsample.model.Clause item = adapter.getItem(from);
                adapter.remove(item);
                adapter.insert(item, to);
            }
        }
    };
    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int which) {
            adapter.remove(adapter.getItem(which));
        }
    };

    public CreateTriggerPredicateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_trigger_predicate_view, null);
        ((FloatingActionButton)view.findViewById(R.id.fabAddClause)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SelectClauseDialogFragment dialog = SelectClauseDialogFragment.newFragment();
                dialog.setTargetFragment(CreateTriggerPredicateFragment.this, REQUEST_CODE_ADD_CLAUSE);
                dialog.show(getFragmentManager(), "SelectClauseDialogFragment");
            }
        });
        this.adapter = new ClauseAdapter(getContext());
        this.listView = (DragSortListView)view.findViewById(android.R.id.list);
        this.controller = buildController(this.listView);
        this.listView.setFloatViewManager(this.controller);
        this.listView.setOnTouchListener(this.controller);
        this.listView.setDragEnabled(true);
        this.listView.setOnItemLongClickListener(this);
        this.listView.setDropListener(this.onDrop);
        this.listView.setRemoveListener(this.onRemove);
        this.listView.setAdapter(this.adapter);
        return view;
    }
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE_ADD_CLAUSE) {
            if (resultCode == DialogInterface.BUTTON_POSITIVE) {

            }
        }
    }
    @Override
    public void onExit(int exitCode) {
        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                Toast.makeText(getContext(), "Create trigger!! power=" + power + " brightness=" + brightness + " colorR=" + colorR + " colorG=" + colorG + " colorB=" + colorB, Toast.LENGTH_LONG).show();
                break;
            case WizardStep.EXIT_PREVIOUS:
                break;
        }
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
    private DragSortController buildController(DragSortListView dslv) {
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.row_icon);
        controller.setRemoveEnabled(false); // TODO
        controller.setSortEnabled(true);
        controller.setDragInitMode(DragSortController.ON_DOWN);
        controller.setRemoveMode(DragSortController.FLING_REMOVE);
        return controller;
    }

    public class ClauseAdapter extends ArrayAdapter<com.kii.iotcloudsample.model.Clause> {
        private final LayoutInflater inflater;
        private ClauseAdapter(Context context) {
            super(context, R.layout.command_list_item);
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return convertView;
        }
    }
}
