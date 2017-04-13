package com.kii.thingifsample.fragments.wizard;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.kii.thingif.ThingIFAPI;
import com.kii.thingif.trigger.TriggerOptions;
import com.kii.thingifsample.R;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateTriggerOptionFragment extends WizardFragment {

    private ThingIFAPI api;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextMetadata;

    public static CreateTriggerOptionFragment newFragment(ThingIFAPI api) {
        CreateTriggerOptionFragment fragment = new CreateTriggerOptionFragment();
        Bundle arguments = new Bundle();
        //arguments.putParcelable("ThingIFAPI", api);
        fragment.setArguments(arguments);
        return fragment;
    }

    public CreateTriggerOptionFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable("ThingIFAPI", this.api);
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
        View view = inflater.inflate(R.layout.common_options, null);
        this.editTextTitle = (EditText)view.findViewById(R.id.editTextTitle);
        this.editTextDescription = (EditText)view.findViewById(R.id.editTextDescription);
        this.editTextMetadata = (EditText)view.findViewById(R.id.editTextMetadata);
        return view;
    }

    @Override
    public void onActivate() {
        TriggerOptions options = this.editingTrigger.getOptions();
        if (options != null) {
            if (options.getTitle() != null) {
                this.editTextTitle.setText(options.getTitle());
            }
            if (options.getDescription() != null) {
                this.editTextDescription.setText(options.getDescription());
            }
            if (options.getMetadata() != null) {
                this.editTextMetadata.setText(options.getMetadata().toString());
            }
        }
    }
    @Override
    public void onInactivate(int exitCode) {
        boolean callBuild = false;
        TriggerOptions.Builder builder = TriggerOptions.Builder.newBuilder();
        if (!TextUtils.isEmpty(this.editTextTitle.getText().toString())) {
            builder.setTitle(this.editTextTitle.getText().toString());
            callBuild = true;
        }
        if (!TextUtils.isEmpty(this.editTextDescription.getText().toString())) {
            builder.setDescription(this.editTextDescription.getText().toString());
            callBuild = true;
        }
        if (!TextUtils.isEmpty(this.editTextMetadata.getText().toString())) {
            try {
                builder.setMetadata(new JSONObject(this.editTextMetadata.getText().toString()));
                callBuild = true;
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Metadata to JSON failed.", Toast.LENGTH_LONG).show();
            }
        }
        if (callBuild) {
            this.editingTrigger.setOptions(builder.build());
        }
    }

    @Override
    public String getNextButtonText() {
        return "Save";
    }
    @Override
    public String getPreviousButtonText() {
        return "Previous";
    }
}
