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

import com.kii.iotcloud.trigger.clause.Equals;
import com.kii.iotcloud.trigger.clause.NotEquals;
import com.kii.iotcloud.trigger.clause.Range;
import com.kii.thingifsample.R;
import com.kii.thingifsample.uimodel.Clause;

public class EditClauseDialogFragment extends DialogFragment {

    public static final String EXTRA_CLAUSE = "EXTRA_CLAUSE";
    public static final String EXTRA_EDITING_LIST_POSITION = "EXTRA_EDITING_LIST_POSITION";

    public static EditClauseDialogFragment newFragment(Fragment target, int requestCode, Clause.ClauseType clauseType, com.kii.iotcloud.trigger.clause.Clause clause, int editingListPosition) {
        EditClauseDialogFragment fragment = new EditClauseDialogFragment();
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
    private com.kii.iotcloud.trigger.clause.Clause clause;
    private int editingListPosition;

    public EditClauseDialogFragment() {
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
                            com.kii.iotcloud.trigger.clause.Clause clause = createClause();
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
    private com.kii.iotcloud.trigger.clause.Clause createClause() {
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
                    return new com.kii.iotcloud.trigger.clause.Equals(field, Boolean.parseBoolean(value));
                } else if (this.isLong(value)) {
                    return new com.kii.iotcloud.trigger.clause.Equals(field, Long.parseLong(value));
                } else {
                    return new com.kii.iotcloud.trigger.clause.Equals(field, value);
                }
            case NOT_EQUALS:
                // FIXME: cannot determine correct type.
                if (this.isBoolean(value)) {
                    return new com.kii.iotcloud.trigger.clause.NotEquals(
                            new com.kii.iotcloud.trigger.clause.Equals(field, Boolean.parseBoolean(value)));
                } else if (this.isLong(value)) {
                    return new com.kii.iotcloud.trigger.clause.NotEquals(
                            new com.kii.iotcloud.trigger.clause.Equals(field, Long.parseLong(value)));
                } else {
                    return new com.kii.iotcloud.trigger.clause.NotEquals(
                            new com.kii.iotcloud.trigger.clause.Equals(field, value));
                }
            case GREATER_THAN:
                return com.kii.iotcloud.trigger.clause.Range.greaterThan(field, Long.valueOf(value));
            case GREATER_THAN_EQUALS:
                return com.kii.iotcloud.trigger.clause.Range.greaterThanEquals(field, Long.valueOf(value));
            case LESS_THAN:
                return com.kii.iotcloud.trigger.clause.Range.lessThan(field, Long.valueOf(value));
            case LESS_THAN_EQUALS:
                return com.kii.iotcloud.trigger.clause.Range.lessThanEquals(field, Long.valueOf(value));
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
    private String getField(com.kii.iotcloud.trigger.clause.Clause clause) {
        if (clause != null) {
            if (clause instanceof Equals) {
                return ((Equals)clause).getField();
            } else if (clause instanceof NotEquals) {
                return ((NotEquals)clause).getEquals().getField();
            } else if (clause instanceof Range) {
                return ((Range)clause).getField();
            }
        }
        return "";
    }
    private String getValue(com.kii.iotcloud.trigger.clause.Clause clause) {
        if (clause != null) {
            if (clause instanceof Equals) {
                Object value = ((Equals) clause).getValue();
                if (value != null) {
                    return value.toString();
                }
            } else if (clause instanceof NotEquals) {
                Object value = ((NotEquals) clause).getEquals().getValue();
                if (value != null) {
                    return value.toString();
                }
            } else if (clause instanceof Range) {
                Long upperLimit = ((Range) clause).getUpperLimit();
                if (upperLimit != null) {
                    return upperLimit.toString();
                }
                Long lowerLimit = ((Range) clause).getLowerLimit();
                if (lowerLimit != null) {
                    return lowerLimit.toString();
                }
            }
        }
        return "";
    }
}
