package com.kii.thingifsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kii.thingif.ThingIFAPI;
import com.kii.thingifsample.fragments.CreateCommandFragment;

public class CreateCommandActivity extends AppCompatActivity {

    private ThingIFAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_command);
        Intent i = getIntent();
        this.api = i.getParcelableExtra("ThingIFAPI");

        CreateCommandFragment fragment = CreateCommandFragment.newFragment(this.api);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, "CreateCommandFragment").commit();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ThingIFAPI", this.api);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.api = (ThingIFAPI)savedInstanceState.getParcelable("ThingIFAPI");
    }
}
