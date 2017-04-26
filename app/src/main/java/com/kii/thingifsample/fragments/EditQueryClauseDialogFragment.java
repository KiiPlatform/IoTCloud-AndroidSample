package com.kii.thingifsample.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kii.thingif.clause.query.EqualsClauseInQuery;
import com.kii.thingif.clause.query.NotEqualsClauseInQuery;
import com.kii.thingif.clause.query.QueryClause;
import com.kii.thingif.clause.query.RangeClauseInQuery;
import com.kii.thingifsample.AppConstants;
import com.kii.thingifsample.R;
import com.kii.thingifsample.uimodel.Clause;

public class EditQueryClauseDialogFragment extends DialogFragment {

    public static final String EXTRA_CLAUSE = "EXTRA_CLAUSE";
    public static final String EXTRA_EDITING_LIST_POSITION = "EXTRA_EDITING_LIST_POSITION";

    public static EditQueryClauseDialogFragment newFragment(Fragment target, int requestCode, Clause.ClauseType clauseType, QueryClause clause, int editingListPosition) {
        EditQueryClauseDialogFragment fragment = new EditQueryClauseDialogFragment();
        fragment.setTargetFragment(target, requestCode);

        Bundle arguments = new Bundle();
        arguments.putSerializable("clauseType", clauseType);
        arguments.putParcelable("clause", clause);
        arguments.putInt("editingListPosition", editingListPosition);
        fragment.setArguments(arguments);

        return fragment;
    }

    private EditText editTextField;
    private EditText editTextValue;
    private Clause.ClauseType clauseType;
    private QueryClause clause;
    private int editingListPosition;

    public EditQueryClauseDialogFragment() {
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.clauseType = (Clause.ClauseType)getArguments().getSerializable("clauseType");
        this.clause = getArguments().getParcelable("clause");
        this.editingListPosition = getArguments().getInt("editingListPosition");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_clause_dialog_view, null, false);

        this.editTextField = (EditText) view.findViewById(R.id.editTextField);
        this.editTextValue = (EditText) view.findViewById(R.id.editTextValue);
        this.editTextField.setInputType(InputType.TYPE_CLASS_TEXT);
        if (this.clauseType == Clause.ClauseType.EQUALS || this.clauseType == Clause.ClauseType.NOT_EQUALS) {
            this.editTextValue.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            // Range clause
            this.editTextValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        this.editTextField.setText(this.getField(this.clause));
        this.editTextValue.setText(this.getValue(this.clause));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(clauseType.getCaption());
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btnOK = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment target = getTargetFragment();
                        if (target != null) {
                            Intent data = new Intent();
                            QueryClause clause = createClause();
                            if (clause == null) {
                                return;
                            }
                            data.putExtra(EXTRA_CLAUSE, clause);
                            if (editingListPosition >= 0) {
                                data.putExtra(EXTRA_EDITING_LIST_POSITION, editingListPosition);
                            }
                            target.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
                        }
                        dismiss();
                    }
                });
            }
        });
        return dialog;
    }
    private QueryClause createClause() {
        String field = this.editTextField.getText().toString();
        String value = this.editTextValue.getText().toString();
        if (TextUtils.isEmpty(field)) {
            Toast.makeText(getContext(), "Field is required", Toast.LENGTH_LONG).show();
            return null;
        }
        if (TextUtils.isEmpty(value)) {
            Toast.makeText(getContext(), "Value is required", Toast.LENGTH_LONG).show();
            return null;
        }
        switch (this.clauseType) {
            case EQUALS:
                // FIXME: cannot determine correct type.
                if (this.isBoolean(value)) {
                    return new EqualsClauseInQuery(field, Boolean.parseBoolean(value));
                } else if (this.isLong(value)) {
                    return new EqualsClauseInQuery(field, Long.parseLong(value));
                } else {
                    return new EqualsClauseInQuery(field, value);
                }
            case NOT_EQUALS:
                // FIXME: cannot determine correct type.
                if (this.isBoolean(value)) {
                    return new NotEqualsClauseInQuery(
                            new EqualsClauseInQuery(field, Boolean.parseBoolean(value)));
                } else if (this.isLong(value)) {
                    return new NotEqualsClauseInQuery(
                            new EqualsClauseInQuery(field, Long.parseLong(value)));
                } else {
                    return new NotEqualsClauseInQuery(
                            new EqualsClauseInQuery(field, value));
                }
            case GREATER_THAN:
                return RangeClauseInQuery.greaterThan(field, Long.valueOf(value));
            case GREATER_THAN_EQUALS:
                return RangeClauseInQuery.greaterThanOrEqualTo(field, Long.valueOf(value));
            case LESS_THAN:
                return RangeClauseInQuery.lessThan(field, Long.valueOf(value));
            case LESS_THAN_EQUALS:
                return RangeClauseInQuery.lessThanOrEqualTo(field, Long.valueOf(value));
            default:
                return null;
        }
    }
    private boolean isLong(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isBoolean(String s) {
        return "true".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s);
    }
    private String getField(QueryClause clause) {
        if (clause != null) {
            if (clause instanceof EqualsClauseInQuery) {
                return ((EqualsClauseInQuery)clause).getField();
            } else if (clause instanceof NotEqualsClauseInQuery) {
                return ((NotEqualsClauseInQuery)clause).getEquals().getField();
            } else if (clause instanceof RangeClauseInQuery) {
                return ((RangeClauseInQuery)clause).getField();
            }
        }
        return "";
    }
    private String getValue(QueryClause clause) {
        if (clause != null) {
            if (clause instanceof EqualsClauseInQuery) {
                Object value = ((EqualsClauseInQuery) clause).getValue();
                if (value != null) {
                    return value.toString();
                }
            } else if (clause instanceof NotEqualsClauseInQuery) {
                Object value = ((NotEqualsClauseInQuery) clause).getEquals().getValue();
                if (value != null) {
                    return value.toString();
                }
            } else if (clause instanceof RangeClauseInQuery) {
                Number upperLimit = ((RangeClauseInQuery) clause).getUpperLimit();
                if (upperLimit != null) {
                    return upperLimit.toString();
                }
                Number lowerLimit = ((RangeClauseInQuery) clause).getLowerLimit();
                if (lowerLimit != null) {
                    return lowerLimit.toString();
                }
            }
        }
        return "";
    }
}
