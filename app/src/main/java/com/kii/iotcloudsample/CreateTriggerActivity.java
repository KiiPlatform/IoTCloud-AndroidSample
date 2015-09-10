package com.kii.iotcloudsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloudsample.fragments.CreateTriggerFragment;

public class CreateTriggerActivity extends AppCompatActivity {

    private IoTCloudAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_command);
        Intent i = getIntent();
        this.api = (IoTCloudAPI)i.getParcelableExtra("IoTCloudAPI");

        CreateTriggerFragment fragment = CreateTriggerFragment.newFragment(this.api);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, "CreateTriggerFragment").commit();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("IoTCloudAPI", this.api);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.api = (IoTCloudAPI)savedInstanceState.getParcelable("IoTCloudAPI");
    }
    public IoTCloudAPI getApi() {
        return this.api;
    }
}
