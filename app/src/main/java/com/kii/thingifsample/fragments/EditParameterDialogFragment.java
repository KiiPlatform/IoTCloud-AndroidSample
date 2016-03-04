package com.kii.thingifsample.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.kii.thingifsample.R;

public class EditParameterDialogFragment extends DialogFragment {

    public static final String EXTRA_PARAMETER_TYPE = "EXTRA_PARAMETER_TYPE";
    public static final String EXTRA_PARAMETER_NAME = "EXTRA_PARAMETER_NAME";
    public static final String EXTRA_STRING_VALUE = "EXTRA_STRING_VALUE";
    public static final String EXTRA_NUMBER_VALUE = "EXTRA_NUMBER_VALUE";
    public static final String EXTRA_BOOL_VALUE = "EXTRA_BOOL_VALUE";
    public static final String EXTRA_EDITING_LIST_POSITION = "EXTRA_EDITING_LIST_POSITION";

    public static EditParameterDialogFragment newFragment(Fragment target, int requestCode, int type, int editingListPosition) {
        EditParameterDialogFragment fragment = new EditParameterDialogFragment();
        fragment.setTargetFragment(target, requestCode);

        Bundle arguments = new Bundle();
        arguments.putInt("type", type);
        arguments.putInt("editingListPosition", editingListPosition);
        fragment.setArguments(arguments);

        return fragment;
    }
    public static EditParameterDialogFragment newFragment(Fragment target, int requestCode, int type, int editingListPosition, Pair<String, Object> parameter) {
        EditParameterDialogFragment fragment = new EditParameterDialogFragment();
        fragment.setTargetFragment(target, requestCode);

        Bundle arguments = new Bundle();
        arguments.putInt("type", type);
        arguments.putInt("editingListPosition", editingListPosition);
        arguments.putString("name", parameter.first);
        arguments.putString("value", parameter.second.toString());
        fragment.setArguments(arguments);

        return fragment;
    }

    private EditText editTextKey;
    private EditText editTextStringValue;
    private EditText editTextNumberValue;
    private Spinner spinnerBoolValue;
    private int editingListPosition;

    public EditParameterDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final int type = getArguments().getInt("type");
        this.editingListPosition = getArguments().getInt("editingListPosition");
        String name = getArguments().getString("name");
        String value = getArguments().getString("value");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_parameter_fragment_dialog, null, false);

        this.editTextKey = (EditText) view.findViewById(R.id.editTextKey);
        this.editTextStringValue = (EditText) view.findViewById(R.id.editTextStringValue);
        this.editTextNumberValue = (EditText) view.findViewById(R.id.editTextNumberValue);
        this.spinnerBoolValue = (Spinner) view.findViewById(R.id.spinnerBoolValue);

        if (!TextUtils.isEmpty(name)) {
            this.editTextKey.setText(name);
        }
        switch (type) {
            case 0:
                this.editTextStringValue.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(value)) {
                    this.editTextStringValue.setText(value);
                }
                break;
            case 1:
                this.editTextNumberValue.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(value)) {
                    this.editTextNumberValue.setText(value);
                }
                break;
            case 2:
                this.spinnerBoolValue.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(value)) {
                    if (Boolean.valueOf(value)) {
                        this.spinnerBoolValue.setSelection(0);
                    } else {
                        this.spinnerBoolValue.setSelection(1);
                    }
                }
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Parameter");
        builder.setPositiveButton("OK", null);
        builder.setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Fragment target = getTargetFragment();
                if (target != null) {
                    Intent data = new Intent();
                    data.putExtra(EXTRA_EDITING_LIST_POSITION, editingListPosition);
                    target.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
                }
                dismiss();
            }
        });
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
                Button btnOK = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment target = getTargetFragment();
                        if (target != null) {
                            Intent data = new Intent();
                            data.putExtra(EXTRA_PARAMETER_TYPE, type);
                            data.putExtra(EXTRA_PARAMETER_NAME, editTextKey.getText().toString());
                            switch (type) {
                                case 0:
                                    data.putExtra(EXTRA_STRING_VALUE, editTextStringValue.getText().toString());
                                    break;
                                case 1:
                                    data.putExtra(EXTRA_NUMBER_VALUE, Long.valueOf(editTextNumberValue.getText().toString()));
                                    break;
                                case 2:
                                    data.putExtra(EXTRA_BOOL_VALUE, spinnerBoolValue.getSelectedItemPosition() == 0 ? Boolean.TRUE : Boolean.FALSE);
                                    break;
                            }
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

}
