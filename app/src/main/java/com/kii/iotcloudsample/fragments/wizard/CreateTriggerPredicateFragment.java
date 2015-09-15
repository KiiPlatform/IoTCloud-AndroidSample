package com.kii.iotcloudsample.fragments.wizard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.trigger.Condition;
import com.kii.iotcloud.trigger.StatePredicate;
import com.kii.iotcloud.trigger.TriggersWhen;
import com.kii.iotcloudsample.R;
import com.kii.iotcloudsample.adapter.ClauseAdapter;
import com.kii.iotcloudsample.fragments.EditClauseDialogFragment;
import com.kii.iotcloudsample.fragments.SelectClauseDialogFragment;
import com.kii.iotcloudsample.model.And;
import com.kii.iotcloudsample.model.Clause;
import com.kii.iotcloudsample.model.ClauseParser;
import com.kii.iotcloudsample.model.Equals;
import com.kii.iotcloudsample.model.NotEquals;
import com.kii.iotcloudsample.model.Or;
import com.kii.iotcloudsample.model.Range;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CreateTriggerPredicateFragment extends WizardFragment implements AdapterView.OnItemClickListener {

    private static final int REQUEST_CODE_ADD_CLAUSE = 100;
    private static final int REQUEST_CODE_EDIT_CLAUSE = 101;

    private IoTCloudAPI api;
    private DragSortListView listView;
    private DragSortController controller;
    private ClauseAdapter adapter;

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                Clause item = adapter.getItem(from);
                adapter.remove(item);
                adapter.insert(item, to);
                validateClauses();
            }
        }
    };
    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int which) {
            Clause clause = adapter.getItem(which);
            if (clause instanceof And.AndOpen) {
                adapter.remove(((And.AndOpen)clause).getClose());
            } else if (clause instanceof And.AndClose) {
                adapter.remove(((And.AndClose)clause).getOpen());
            } else if (clause instanceof Or.OrOpen) {
                adapter.remove(((Or.OrOpen)clause).getClose());
            } else if (clause instanceof Or.OrClose) {
                adapter.remove(((Or.OrClose)clause).getOpen());
            }
            adapter.remove(clause);
            validateClauses();
        }
    };

    public static CreateTriggerPredicateFragment newFragment(IoTCloudAPI api) {
        CreateTriggerPredicateFragment fragment = new CreateTriggerPredicateFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("IoTCloudAPI", api);
        fragment.setArguments(arguments);
        return fragment;
    }
    public CreateTriggerPredicateFragment() {
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
            this.api = savedInstanceState.getParcelable("IoTCloudAPI");
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.api = arguments.getParcelable("IoTCloudAPI");
        }
        View view = inflater.inflate(R.layout.create_trigger_predicate_view, null);
        ((FloatingActionButton)view.findViewById(R.id.fabAddClause)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SelectClauseDialogFragment dialog = SelectClauseDialogFragment.newFragment(CreateTriggerPredicateFragment.this, REQUEST_CODE_ADD_CLAUSE);
                dialog.show(getFragmentManager(), "SelectClauseDialogFragment");
            }
        });
        this.adapter = new ClauseAdapter(getContext());
        this.listView = (DragSortListView)view.findViewById(android.R.id.list);
        this.controller = buildController(this.listView);
        this.listView.setFloatViewManager(this.controller);
        this.listView.setOnTouchListener(this.controller);
        this.listView.setDragEnabled(true);
        this.listView.setOnItemClickListener(this);
        this.listView.setDropListener(this.onDrop);
        this.listView.setRemoveListener(this.onRemove);
        this.listView.setAdapter(this.adapter);
        return view;
    }
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
         if (requestCode == REQUEST_CODE_ADD_CLAUSE) {
            Clause.ClauseType clauseType = (Clause.ClauseType)data.getSerializableExtra(SelectClauseDialogFragment.EXTRA_CLAUSE_TYPE);
            switch (clauseType) {
                case AND:
                    And.AndOpen andOpen = new And.AndOpen();
                    And.AndClose andClose = new And.AndClose();
                    andOpen.setClose(andClose);
                    andClose.setOpen(andOpen);
                    this.adapter.add(andOpen);
                    this.adapter.add(andClose);
                    this.adapter.notifyDataSetChanged();
                    break;
                case OR:
                    Or.OrOpen orOpen = new Or.OrOpen();
                    Or.OrClose orClose = new Or.OrClose();
                    orOpen.setClose(orClose);
                    orClose.setOpen(orOpen);
                    this.adapter.add(orOpen);
                    this.adapter.add(orClose);
                    this.adapter.notifyDataSetChanged();
                    break;
                default:
                    this.editClause(clauseType, null, -1);
                    break;
            }
        } else if (requestCode == REQUEST_CODE_EDIT_CLAUSE) {
             if (resultCode == Activity.RESULT_OK) {
                 com.kii.iotcloud.trigger.clause.Clause clause = data.getParcelableExtra(EditClauseDialogFragment.EXTRA_CLAUSE);
                 int editingListPosition = data.getIntExtra(EditClauseDialogFragment.EXTRA_EDITING_LIST_POSITION, -1);
                 if (editingListPosition >= 0) {
                     this.adapter.getItem(editingListPosition).setClause(clause);
                 } else {
                     if (clause instanceof com.kii.iotcloud.trigger.clause.And) {
                         And and = new And();
                         and.setClause(clause);
                         this.adapter.add(and);
                     } else if (clause instanceof com.kii.iotcloud.trigger.clause.Or) {
                         Or or = new Or();
                         or.setClause(clause);
                         this.adapter.add(or);
                     } else if (clause instanceof com.kii.iotcloud.trigger.clause.Equals) {
                         Equals equals = new Equals();
                         equals.setClause(clause);
                         this.adapter.add(equals);
                     } else if (clause instanceof com.kii.iotcloud.trigger.clause.NotEquals) {
                         NotEquals notEquals = new NotEquals();
                         notEquals.setClause(clause);
                         this.adapter.add(notEquals);
                     } else if (clause instanceof com.kii.iotcloud.trigger.clause.Range) {
                         com.kii.iotcloud.trigger.clause.Range range = (com.kii.iotcloud.trigger.clause.Range)clause;
                         if (range.getLowerLimit() != null) {
                             if (range.getLowerIncluded() == Boolean.TRUE) {
                                 Range.GreaterThanEquals greaterThanEquals = new Range.GreaterThanEquals();
                                 greaterThanEquals.setClause(range);
                                 this.adapter.add(greaterThanEquals);
                             } else {
                                 Range.GreaterThan greaterThan = new Range.GreaterThan();
                                 greaterThan.setClause(range);
                                 this.adapter.add(greaterThan);
                             }
                         }
                         if (range.getUpperLimit() != null) {
                             if (range.getUpperIncluded() == Boolean.TRUE) {
                                 Range.LessThanEquals lessThanEquals = new Range.LessThanEquals();
                                 lessThanEquals.setClause(range);
                                 this.adapter.add(lessThanEquals);
                             } else {
                                 Range.LessThan lessThan = new Range.LessThan();
                                 lessThan.setClause(range);
                                 this.adapter.add(lessThan);
                             }
                         }
                     }
                 }
                 this.adapter.notifyDataSetChanged();
             }
         }
        this.validateClauses();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Clause clause = (Clause)this.listView.getItemAtPosition(position);
        if (!clause.isContainer()) {
            this.editClause(clause.getType(), clause, position);
        }
    }
    @Override
    public void onActivate() {
        if (this.editingTrigger.getPredicate() != null) {
            List<Clause> clauses = ClauseParser.parseClause(this.editingTrigger.getPredicate().getCondition().getClause());
            this.adapter.clear();
            for (Clause clause : clauses) {
                this.adapter.add(clause);
            }
        }
        this.validateClauses();
    }
    @Override
    public void onInactivate(int exitCode) {
        com.kii.iotcloud.trigger.clause.Clause clause = ClauseParser.parseClause(this.adapter.getItems());
        StatePredicate predicate = new StatePredicate(new Condition(clause), TriggersWhen.CONDITION_TRUE);
        this.editingTrigger.setPredicate(predicate);
    }
    @Override
    public String getNextButtonText() {
        return "Next";
    }
    @Override
    public String getPreviousButtonText() {
        return "Previous";
    }
    private DragSortController buildController(DragSortListView dslv) {
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.row_icon);
        controller.setRemoveEnabled(true);
        controller.setSortEnabled(true);
        controller.setDragInitMode(DragSortController.ON_DOWN);
        controller.setRemoveMode(DragSortController.FLING_REMOVE);
        return controller;
    }
    private void editClause(Clause.ClauseType type, Clause clause, int editingListPosition) {
        EditClauseDialogFragment dialog = EditClauseDialogFragment.newFragment(
                CreateTriggerPredicateFragment.this,
                REQUEST_CODE_EDIT_CLAUSE,
                type,
                clause == null ? null : clause.getClause(),
                editingListPosition);
        dialog.show(getFragmentManager(), "EditClauseDialogFragment");
    }
    private void validateClauses() {
        if (this.adapter != null) {
            com.kii.iotcloud.trigger.clause.Clause clause = ClauseParser.parseClause(this.adapter.getItems());
            if (clause != null) {
                if (clause instanceof com.kii.iotcloud.trigger.clause.ContainerClause) {
                    if (!((com.kii.iotcloud.trigger.clause.ContainerClause)clause).hasClause()) {
                        this.setNextButtonEnabled(false);
                        return;
                    }
                }
                this.setNextButtonEnabled(true);
                return;
            }
        }
        this.setNextButtonEnabled(false);
    }
}
