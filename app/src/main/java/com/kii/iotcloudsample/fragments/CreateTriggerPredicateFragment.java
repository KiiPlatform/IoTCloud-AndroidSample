package com.kii.iotcloudsample.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.command.Action;
import com.kii.iotcloud.trigger.Condition;
import com.kii.iotcloud.trigger.StatePredicate;
import com.kii.iotcloud.trigger.Trigger;
import com.kii.iotcloud.trigger.TriggersWhen;
import com.kii.iotcloudsample.AppConstants;
import com.kii.iotcloudsample.CreateTriggerActivity;
import com.kii.iotcloudsample.R;
import com.kii.iotcloudsample.adapter.ImageViewHolder;
import com.kii.iotcloudsample.model.And;
import com.kii.iotcloudsample.model.Clause;
import com.kii.iotcloudsample.model.Equals;
import com.kii.iotcloudsample.model.NotEquals;
import com.kii.iotcloudsample.model.Or;
import com.kii.iotcloudsample.model.Range;
import com.kii.iotcloudsample.promise_api_wrapper.IoTCloudPromiseAPIWrapper;
import com.kii.iotcloudsample.smart_light_demo.SetBrightness;
import com.kii.iotcloudsample.smart_light_demo.SetColor;
import com.kii.iotcloudsample.smart_light_demo.SetColorTemperature;
import com.kii.iotcloudsample.smart_light_demo.TurnPower;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CreateTriggerPredicateFragment extends WizardStep implements AdapterView.OnItemClickListener {

    private static final int REQUEST_CODE_ADD_CLAUSE = 100;
    private static final int REQUEST_CODE_EDIT_CLAUSE = 101;

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

    public CreateTriggerPredicateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        this.validateClauses();
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
    public void onExit(int exitCode) {
        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                this.createTrigger();
                getActivity().finish();
                break;
            case WizardStep.EXIT_PREVIOUS:
                break;
        }
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
            com.kii.iotcloud.trigger.clause.Clause clause = this.parseClauses(this.adapter.getItems());
            if (clause != null) {
                if (clause instanceof com.kii.iotcloud.trigger.clause.ContainerClause) {
                    if (!((com.kii.iotcloud.trigger.clause.ContainerClause)clause).hasClause()) {
                        this.notifyIncomplete();
                        return;
                    }
                }
                this.notifyCompleted();
                return;
            }
        }
        this.notifyIncomplete();
    }
    private void createTrigger() {
        List<Action> actions = new ArrayList<Action>();
        if (this.turnPowerEnabled) {
            actions.add(new TurnPower(this.power));
        }
        if (this.setBrightnessEnabled) {
            actions.add(new SetBrightness(this.brightness));
        }
        if (this.setColorEnabled) {
            actions.add(new SetColor(this.colorR, this.colorG, this.colorB));
        }
        if (this.setColorTemperatureEnabled) {
            actions.add(new SetColorTemperature(this.colorTemperature));
        }
        com.kii.iotcloud.trigger.clause.Clause clause = parseClauses(this.adapter.getItems());
        StatePredicate predicate = new StatePredicate(new Condition(clause), TriggersWhen.CONDITION_TRUE);

        // FIXME: WizarDroid does not support the persistence of Parcelable.
        //        So we need to get the IoTCloudAPI from parent activity.
        // see:https://github.com/Nimrodda/WizarDroid/wiki/WizarDroid-Context
        IoTCloudAPI api = ((CreateTriggerActivity)getActivity()).getApi();
        IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(api);
        wp.postNewTrigger(AppConstants.SCHEMA_NAME, AppConstants.SCHEMA_VERSION, actions, predicate).then(new DoneCallback<Trigger>() {
            @Override
            public void onDone(Trigger result) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        }, new FailCallback<Throwable>() {
            @Override
            public void onFail(Throwable result) {
                Toast.makeText(getContext(), "Failed to create new trigger: !" + result.getMessage(), Toast.LENGTH_LONG).show();
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });
    }
    private com.kii.iotcloud.trigger.clause.Clause parseClauses(List<Clause> clauses) {
        if (clauses.size() == 0) {
            return null;
        }
        Deque<com.kii.iotcloud.trigger.clause.ContainerClause> deque = new ArrayDeque<com.kii.iotcloud.trigger.clause.ContainerClause>();
        com.kii.iotcloud.trigger.clause.Clause rootClause = null;

        for (Clause clause : clauses) {
            if (clause instanceof And.AndOpen) {
                if (rootClause == null) {
                    rootClause = new com.kii.iotcloud.trigger.clause.And();
                    deque.offerFirst((com.kii.iotcloud.trigger.clause.ContainerClause)rootClause);
                } else if (deque.peekFirst() != null) {
                    com.kii.iotcloud.trigger.clause.And andClause = new com.kii.iotcloud.trigger.clause.And();
                    deque.peekFirst().addClause(andClause);
                    deque.offerFirst(andClause);
                } else {
                    // too many root clause
                    return null;
                }
            } else if (clause instanceof And.AndClose) {
                if (!(deque.pollFirst() instanceof com.kii.iotcloud.trigger.clause.And)) {
                    // position of close braces is invalid
                    return null;
                }
            } else if (clause instanceof Or.OrOpen) {
                if (rootClause == null) {
                    rootClause = new com.kii.iotcloud.trigger.clause.Or();
                    deque.offerFirst((com.kii.iotcloud.trigger.clause.ContainerClause)rootClause);
                } else if (deque.peekFirst() != null) {
                    com.kii.iotcloud.trigger.clause.Or orClause = new com.kii.iotcloud.trigger.clause.Or();
                    deque.peekFirst().addClause(orClause);
                    deque.offerFirst(orClause);
                } else {
                    // too many root clause
                    return null;
                }
            } else if (clause instanceof Or.OrClose) {
                if (!(deque.pollFirst() instanceof com.kii.iotcloud.trigger.clause.Or)) {
                    // position of close brackets is invalid
                    return null;
                }
            } else {
                if (rootClause == null) {
                    rootClause = clause.getClause();
                } else if (deque.peekFirst() != null) {
                    deque.peekFirst().addClause(clause.getClause());
                } else {
                    // too many root clause
                    return null;
                }
            }
        }
        return rootClause;
    }

    public class ClauseAdapter extends ArrayAdapter<Clause> {
        private final LayoutInflater inflater;
        private ClauseAdapter(Context context) {
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
        public List<Clause> getItems() {
            List<Clause> clauses = new ArrayList<Clause>();
            for (int i = 0; i < this.getCount(); i++) {
                clauses.add(getItem(i));
            }
            return clauses;
        }
    }
}
