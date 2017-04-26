package com.kii.thingifsample.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.kii.thingif.clause.query.AllClause;
import com.kii.thingif.clause.query.AndClauseInQuery;
import com.kii.thingif.clause.query.EqualsClauseInQuery;
import com.kii.thingif.clause.query.NotEqualsClauseInQuery;
import com.kii.thingif.clause.query.OrClauseInQuery;
import com.kii.thingif.clause.query.QueryClause;
import com.kii.thingif.clause.query.RangeClauseInQuery;
import com.kii.thingifsample.R;
import com.kii.thingifsample.adapter.ClauseAdapter;
import com.kii.thingifsample.uimodel.And;
import com.kii.thingifsample.uimodel.Clause;
import com.kii.thingifsample.uimodel.ClauseParser;
import com.kii.thingifsample.uimodel.Equals;
import com.kii.thingifsample.uimodel.NotEquals;
import com.kii.thingifsample.uimodel.Or;
import com.kii.thingifsample.uimodel.Range;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

public class CreateQueryClauseFragment extends DialogFragment implements AdapterView.OnItemClickListener {

public static final String KEY = "New Query Clause";

    private static final int REQUEST_CODE_ADD_CLAUSE = 100;
    private static final int REQUEST_CODE_EDIT_CLAUSE = 101;

    private DragSortListView listView;
    private DragSortController controller;
    private ClauseAdapter adapter;
    private QueryClause clause;
    private Button buttonCancel;
    private Button buttonChange;

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

    public static CreateQueryClauseFragment newFragment(Fragment target, QueryClause clause) {
        CreateQueryClauseFragment fragment = new CreateQueryClauseFragment();
        fragment.setTargetFragment(target, 0);
        Bundle arguments = new Bundle();
        fragment.setArguments(arguments);
        fragment.setQueryClause(clause);
        return fragment;
    }
    public CreateQueryClauseFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_query_clause_view, null);
        ((FloatingActionButton)view.findViewById(R.id.fabAddClause)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SelectClauseDialogFragment dialog = SelectClauseDialogFragment.newFragment(CreateQueryClauseFragment.this, REQUEST_CODE_ADD_CLAUSE);
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
        this.buttonCancel = (Button)view.findViewById(R.id.buttonCancel);
        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment target = getTargetFragment();
                if (target != null) {
                    target.onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
                }
                dismiss();
            }
        });
        this.buttonChange = (Button)view.findViewById(R.id.buttonChange);
        this.buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment target = getTargetFragment();
                if (target != null && clause != null) {
                    Intent data = new Intent();
                    data.putExtra(CreateQueryClauseFragment.KEY, clause);
                    target.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
                }
                dismiss();
            }
        });
        this.adapter.addAll(ClauseParser.parseQueryClause(this.clause));
        this.adapter.notifyDataSetChanged();
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
                 QueryClause clause = data.getParcelableExtra(EditQueryClauseDialogFragment.EXTRA_CLAUSE);
                 int editingListPosition = data.getIntExtra(EditQueryClauseDialogFragment.EXTRA_EDITING_LIST_POSITION, -1);
                 if (editingListPosition >= 0) {
                     this.adapter.getItem(editingListPosition).setQueryClause(clause);
                 } else {
                     if (clause instanceof AndClauseInQuery) {
                         And and = new And();
                         and.setQueryClause(clause);
                         this.adapter.add(and);
                     } else if (clause instanceof OrClauseInQuery) {
                         Or or = new Or();
                         or.setQueryClause(clause);
                         this.adapter.add(or);
                     } else if (clause instanceof EqualsClauseInQuery) {
                         Equals equals = new Equals();
                         equals.setQueryClause(clause);
                         this.adapter.add(equals);
                     } else if (clause instanceof NotEqualsClauseInQuery) {
                         NotEquals notEquals = new NotEquals();
                         notEquals.setQueryClause(clause);
                         this.adapter.add(notEquals);
                     } else if (clause instanceof RangeClauseInQuery) {
                         RangeClauseInQuery range = (RangeClauseInQuery)clause;
                         if (range.getLowerLimit() != null) {
                             if (range.getLowerIncluded() == Boolean.TRUE) {
                                 Range.GreaterThanEquals greaterThanEquals = new Range.GreaterThanEquals();
                                 greaterThanEquals.setQueryClause(range);
                                 this.adapter.add(greaterThanEquals);
                             } else {
                                 Range.GreaterThan greaterThan = new Range.GreaterThan();
                                 greaterThan.setQueryClause(range);
                                 this.adapter.add(greaterThan);
                             }
                         }
                         if (range.getUpperLimit() != null) {
                             if (range.getUpperIncluded() == Boolean.TRUE) {
                                 Range.LessThanEquals lessThanEquals = new Range.LessThanEquals();
                                 lessThanEquals.setQueryClause(range);
                                 this.adapter.add(lessThanEquals);
                             } else {
                                 Range.LessThan lessThan = new Range.LessThan();
                                 lessThan.setQueryClause(range);
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

    private DragSortController buildController(DragSortListView dslv) {
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.row_icon);
        controller.setRemoveEnabled(true);
        controller.setSortEnabled(true);
        controller.setDragInitMode(DragSortController.ON_DOWN);
        controller.setRemoveMode(DragSortController.FLING_REMOVE);
        return controller;
    }

    private void setQueryClause(QueryClause clause) {this.clause = clause;}

    private void editClause(Clause.ClauseType type, Clause clause, int editingListPosition) {
        EditQueryClauseDialogFragment dialog = EditQueryClauseDialogFragment.newFragment(
                CreateQueryClauseFragment.this,
                REQUEST_CODE_EDIT_CLAUSE,
                type,
                clause == null ? null : clause.getQueryClause(),
                editingListPosition);
        dialog.show(getFragmentManager(), "EditQueryClauseDialogFragment");
    }
    private void validateClauses() {
        if (this.adapter != null) {
            QueryClause clause = ClauseParser.parseQueryClause(this.adapter.getItems());
            if (clause != null) {
                if ((clause instanceof AndClauseInQuery) &&
                        (((AndClauseInQuery)clause).getClauses().isEmpty()) ||
                   ((clause instanceof OrClauseInQuery) &&
                        (((OrClauseInQuery)clause).getClauses().isEmpty())))
                {
                    this.buttonChange.setEnabled(false);
                    return;
                }
                this.buttonChange.setEnabled(true);
                this.clause = clause;
                return;
            } else if (this.adapter.getItems().isEmpty()) {
                this.buttonChange.setEnabled(true);
                this.clause = new AllClause();
                return;
            }
        }
        this.buttonChange.setEnabled(false);
    }
}
