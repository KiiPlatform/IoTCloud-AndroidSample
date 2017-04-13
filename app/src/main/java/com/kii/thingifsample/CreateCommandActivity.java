package com.kii.thingifsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kii.thingif.ThingIFAPI;
import com.kii.thingif.exception.StoredInstanceNotFoundException;
import com.kii.thingif.exception.UnloadableInstanceVersionException;
import com.kii.thingifsample.fragments.CreateCommandFragment;

public class CreateCommandActivity extends AppCompatActivity {

    private ThingIFAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_command);
        Intent i = getIntent();
        try {
            this.api = ThingIFAPI.loadFromStoredInstance(this);
        } catch (StoredInstanceNotFoundException e) {
            e.printStackTrace();
        } catch (UnloadableInstanceVersionException e) {
            e.printStackTrace();
        }

        CreateCommandFragment fragment = CreateCommandFragment.newFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, "CreateCommandFragment").commit();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            this.api = ThingIFAPI.loadFromStoredInstance(this);
        } catch (StoredInstanceNotFoundException e) {
            e.printStackTrace();
        } catch (UnloadableInstanceVersionException e) {
            e.printStackTrace();
        }
    }
}
