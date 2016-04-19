package com.kii.thingifsample.fragments.wizard;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.kii.cloud.storage.KiiUser;
import com.kii.thingif.ThingIFAPI;
import com.kii.thingifsample.R;
import com.kii.thingifsample.adapter.ParameterArrayAdapter;
import com.kii.thingifsample.fragments.EditParameterDialogFragment;
import com.kii.thingifsample.uimodel.Trigger;

public class CreateTriggerServerCodeFragment extends WizardFragment {

    public static final String TAG = CreateTriggerServerCodeFragment.class.getSimpleName();
    private static final int REQUEST_CODE_NEW_PARAMETER = 100;
    private static final int REQUEST_CODE_EDIT_PARAMETER = 101;

    private ThingIFAPI api;
    private EditText editTextEndpoint;
    private EditText editTextExecutorAccessToken;
    private EditText editTextTargetAppID;
    private ListView listView;
    private ParameterArrayAdapter adapter;


    public static CreateTriggerServerCodeFragment newFragment(ThingIFAPI api) {
        CreateTriggerServerCodeFragment fragment = new CreateTriggerServerCodeFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("ThingIFAPI", api);
        fragment.setArguments(arguments);
        return fragment;
    }

    public CreateTriggerServerCodeFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ThingIFAPI", this.api);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.api = savedInstanceState.getParcelable("ThingIFAPI");
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.api = arguments.getParcelable("ThingIFAPI");
        }
        View view = inflater.inflate(R.layout.create_trigger_servercode_view, null);

        this.editTextEndpoint = (EditText)view.findViewById(R.id.editTextEndpoint);
        this.editTextEndpoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                validateRequiredField();
            }
        });
        this.editTextExecutorAccessToken = (EditText)view.findViewById(R.id.editTextExecutorAccessToken);
        this.editTextExecutorAccessToken.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                validateRequiredField();
            }
        });
        this.editTextTargetAppID = (EditText)view.findViewById(R.id.editTextTargetAppID);
        ((FloatingActionButton)view.findViewById(R.id.fabAddParameter)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Choose the type of Parameter");
                dialog.setItems(new String[]{"String", "Number", "Boolean"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditParameterDialogFragment parameterDialog = EditParameterDialogFragment.newFragment(CreateTriggerServerCodeFragment.this, REQUEST_CODE_NEW_PARAMETER, which, -1);
                                parameterDialog.show(getFragmentManager(), "parameterDialog");
                            }
                        }
                );
                dialog.create().show();
            }
        });
        this.listView = (ListView)view.findViewById(R.id.listViewParameters);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pair<String, Object> item = (Pair<String, Object>)parent.getItemAtPosition(position);
                int type = 0;
                if (item.second instanceof String) {
                    type = 0;
                } else if (item.second instanceof Number) {
                    type = 1;
                } else if (item.second instanceof Boolean) {
                    type = 2;
                }
                EditParameterDialogFragment parameterDialog = EditParameterDialogFragment.newFragment(CreateTriggerServerCodeFragment.this, REQUEST_CODE_EDIT_PARAMETER, type, position, item);
                parameterDialog.show(getFragmentManager(), "parameterDialog");
            }
        });
        this.onActivate();
        return view;
    }
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE_NEW_PARAMETER && resultCode == Activity.RESULT_OK) {
            int type = data.getIntExtra(EditParameterDialogFragment.EXTRA_PARAMETER_TYPE, -1);
            String parameterName = data.getStringExtra(EditParameterDialogFragment.EXTRA_PARAMETER_NAME);
            Pair<String, Object> parameter = null;
            switch (type) {
                case 0:
                    parameter = new Pair<String, Object>(parameterName, data.getStringExtra(EditParameterDialogFragment.EXTRA_STRING_VALUE));
                    break;
                case 1:
                    parameter = new Pair<String, Object>(parameterName, data.getLongExtra(EditParameterDialogFragment.EXTRA_NUMBER_VALUE, 0));
                    break;
                case 2:
                    parameter = new Pair<String, Object>(parameterName, data.getBooleanExtra(EditParameterDialogFragment.EXTRA_BOOL_VALUE, Boolean.TRUE));
                    break;
            }
            if (parameter != null) {
                this.adapter.add(parameter);
                this.adapter.notifyDataSetChanged();
            }
        } else if (requestCode == REQUEST_CODE_EDIT_PARAMETER && resultCode == Activity.RESULT_OK) {
            int type = data.getIntExtra(EditParameterDialogFragment.EXTRA_PARAMETER_TYPE, -1);
            int index = data.getIntExtra(EditParameterDialogFragment.EXTRA_EDITING_LIST_POSITION, -1);
            String parameterName = data.getStringExtra(EditParameterDialogFragment.EXTRA_PARAMETER_NAME);
            Pair<String, Object> parameter = null;
            switch (type) {
                case 0:
                    if (data.hasExtra(EditParameterDialogFragment.EXTRA_STRING_VALUE)) {
                        parameter = new Pair<String, Object>(parameterName, data.getStringExtra(EditParameterDialogFragment.EXTRA_STRING_VALUE));
                    }
                    break;
                case 1:
                    if (data.hasExtra(EditParameterDialogFragment.EXTRA_NUMBER_VALUE)) {
                        parameter = new Pair<String, Object>(parameterName, data.getLongExtra(EditParameterDialogFragment.EXTRA_NUMBER_VALUE, 0));
                    }
                    break;
                case 2:
                    if (data.hasExtra(EditParameterDialogFragment.EXTRA_BOOL_VALUE)) {
                        parameter = new Pair<String, Object>(parameterName, data.getBooleanExtra(EditParameterDialogFragment.EXTRA_BOOL_VALUE, Boolean.TRUE));
                    }
                    break;
            }
            if (parameter != null) {
                Pair<String, Object> oldParameter = this.adapter.getItem(index);
                this.adapter.remove(oldParameter);
                this.adapter.insert(parameter, index);
                this.adapter.notifyDataSetChanged();
            } else {
                Pair<String, Object> oldParameter = this.adapter.getItem(index);
                this.adapter.remove(oldParameter);
                this.adapter.notifyDataSetChanged();
            }
        }
    }

    private void validateRequiredField() {
        if (TextUtils.isEmpty(this.editTextEndpoint.getText()) ||
            TextUtils.isEmpty(this.editTextExecutorAccessToken.getText())) {
            this.setNextButtonEnabled(false);
        } else {
            this.setNextButtonEnabled(true);
        }
    }

    @Override
    public void onActivate() {
        if (this.editingTrigger.getServerCode() == null) {
            this.editingTrigger.setServerCode(new Trigger.ServerCode());
        }
        if (TextUtils.isEmpty(this.editingTrigger.getServerCode().endpoint)) {
            this.editTextEndpoint.setText("");
        } else {
            this.editTextEndpoint.setText(this.editingTrigger.getServerCode().endpoint);
        }
        if (TextUtils.isEmpty(this.editingTrigger.getServerCode().executorAccessToken)) {
            this.editTextExecutorAccessToken.setText(KiiUser.getCurrentUser().getAccessToken());
        } else {
            this.editTextExecutorAccessToken.setText(this.editingTrigger.getServerCode().executorAccessToken);
        }
        if (TextUtils.isEmpty(this.editingTrigger.getServerCode().targetAppID)) {
            this.editTextTargetAppID.setText("");
        } else {
            this.editTextTargetAppID.setText(this.editingTrigger.getServerCode().targetAppID);
        }
        this.adapter = new ParameterArrayAdapter(getActivity());
        for (Pair<String, Object> parameter : this.editingTrigger.getServerCode().parameters) {
            this.adapter.add(parameter);
        }
        this.listView.setAdapter(this.adapter);
        this.validateRequiredField();
    }
    @Override
    public void onInactivate(int exitCode) {
        if (exitCode == EXIT_NEXT) {
            this.editingTrigger.getServerCode().endpoint = this.editTextEndpoint.getText().toString();
            this.editingTrigger.getServerCode().executorAccessToken = this.editTextExecutorAccessToken.getText().toString();
            this.editingTrigger.getServerCode().targetAppID = this.editTextTargetAppID.getText().toString();
            this.editingTrigger.getServerCode().parameters.clear();
            for (int i = 0; i < this.adapter.getCount(); i++) {
                Pair<String, Object> parameter = this.adapter.getItem(i);
                this.editingTrigger.getServerCode().parameters.add(parameter);
            }
        }
    }
    @Override
    public String getNextButtonText() {
        return "Next";
    }
    @Override
    public String getPreviousButtonText() {
        return "Cancel";
    }

}
