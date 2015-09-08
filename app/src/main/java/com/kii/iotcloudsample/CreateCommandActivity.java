package com.kii.iotcloudsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloudsample.fragments.CreateCommandFragment;

public class CreateCommandActivity extends AppCompatActivity {

    private IoTCloudAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_command);
        Intent i = getIntent();
        this.api = (IoTCloudAPI)i.getParcelableExtra("IoTCloudAPI");

        CreateCommandFragment fragment = CreateCommandFragment.newCommandsFragment(this.api);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, "fragment").commit();
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
}
