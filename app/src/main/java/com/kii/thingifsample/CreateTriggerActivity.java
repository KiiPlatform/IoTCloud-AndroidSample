package com.kii.thingifsample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.kii.thingif.ThingIFAPI;
import com.kii.thingif.trigger.ServerCode;
import com.kii.thingif.trigger.TriggeredCommandForm;
import com.kii.thingifsample.fragments.wizard.CreateTriggerCommandFragment;
import com.kii.thingifsample.fragments.wizard.CreateTriggerPredicateFragment;
import com.kii.thingifsample.fragments.wizard.CreateTriggerServerCodeFragment;
import com.kii.thingifsample.fragments.wizard.CreateTriggersWhenFragment;
import com.kii.thingifsample.fragments.wizard.WizardFragment;
import com.kii.thingifsample.uimodel.Trigger;
import com.kii.thingifsample.promise_api_wrapper.IoTCloudPromiseAPIWrapper;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateTriggerActivity extends AppCompatActivity implements WizardFragment.WizardController {

    public enum TriggerType {
        COMMAND,
        SERVER_CODE
    }

    public static final String TAG = CreateTriggerActivity.class.getSimpleName();
    public static final String INTENT_TRIGGER = "INTENT_TRIGGER";
    public static final String INTENT_TRIGGER_TYPE = "INTENT_TRIGGER_TYPE";
    private static final int WIZARD_PAGE_SIZE = 3;
    private ThingIFAPI api;
    private FragmentStatePagerAdapter adapter;
    private ViewPager viewPager;
    private Button nextButton;
    private Button previousButton;
    private Trigger editingTrigger;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_command_trigger);
        Intent i = getIntent();
        this.api = (ThingIFAPI)i.getParcelableExtra("ThingIFAPI");
        if (i.hasExtra(INTENT_TRIGGER)) {
            this.editingTrigger = new Trigger((com.kii.thingif.trigger.Trigger)i.getParcelableExtra(INTENT_TRIGGER));
        } else {
            this.editingTrigger = new Trigger();
        }
        TriggerType triggerType = (TriggerType)i.getSerializableExtra(INTENT_TRIGGER_TYPE);
        if (triggerType == TriggerType.COMMAND) {
            this.adapter = new CommandTriggerWizardPagerAdapter(getSupportFragmentManager());
        } else if (triggerType == TriggerType.SERVER_CODE) {
            this.adapter = new ServerCodeTriggerWizardPagerAdapter(getSupportFragmentManager());
        }
        this.viewPager = (ViewPager)findViewById(R.id.step_pager);
        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                View currentFocus = CreateTriggerActivity.this.getCurrentFocus();
                if (currentFocus != null) {
                    InputMethodManager imm = (InputMethodManager) CreateTriggerActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
                if (currentPosition < position) {
                    ((WizardFragment) adapter.instantiateItem(viewPager, currentPosition)).onInactivate(WizardFragment.EXIT_NEXT);
                } else {
                    ((WizardFragment) adapter.instantiateItem(viewPager, currentPosition)).onInactivate(WizardFragment.EXIT_PREVIOUS);
                }
                WizardFragment wizardFragment = (WizardFragment) adapter.instantiateItem(viewPager, position);
                wizardFragment.onActivate();
                currentPosition = position;
            }
        };
        this.viewPager.addOnPageChangeListener(onPageChangeListener);
        this.viewPager.setAdapter(this.adapter);
        this.nextButton = (Button)findViewById(R.id.wizard_next_button);
        this.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                WizardFragment wizardFragment = (WizardFragment) adapter.instantiateItem(viewPager, position);
                if (position + 1 < WIZARD_PAGE_SIZE) {
                    position++;
                    viewPager.setCurrentItem(position);
                    wizardFragment = (WizardFragment) adapter.getItem(position);
                    nextButton.setText(wizardFragment.getNextButtonText());
                    previousButton.setText(wizardFragment.getPreviousButtonText());

                } else if (position + 1 == WIZARD_PAGE_SIZE) {
                    wizardFragment.onInactivate(WizardFragment.EXIT_NEXT);
                    IoTCloudPromiseAPIWrapper wp = new IoTCloudPromiseAPIWrapper(api);
                    if (editingTrigger.getTriggerID() == null) {
                        if (editingTrigger.getServerCode() != null) {
                            JSONObject parameters = null;
                            if (editingTrigger.getServerCode().parameters.size() > 0) {
                                parameters = new JSONObject();
                                for (Pair<String, Object> parameter : editingTrigger.getServerCode().parameters) {
                                    try {
                                        parameters.put(parameter.first, parameter.second);
                                    } catch (JSONException e) {
                                    }
                                }
                            }
                            ServerCode serverCode = new ServerCode(
                                    editingTrigger.getServerCode().endpoint,
                                    editingTrigger.getServerCode().executorAccessToken,
                                    editingTrigger.getServerCode().targetAppID,
                                    parameters);
                            wp.postNewTrigger(serverCode, editingTrigger.getPredicate()).then(new DoneCallback<com.kii.thingif.trigger.Trigger>() {
                                @Override
                                public void onDone(com.kii.thingif.trigger.Trigger result) {
                                    Toast.makeText(CreateTriggerActivity.this, "New Trigger is created. TriggerID=" + result.getTriggerID(), Toast.LENGTH_LONG).show();
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                }
                            }, new FailCallback<Throwable>() {
                                @Override
                                public void onFail(Throwable result) {
                                    Toast.makeText(CreateTriggerActivity.this, "Failed to create new trigger!: " + result.getMessage(), Toast.LENGTH_LONG).show();
                                    setResult(Activity.RESULT_CANCELED);
                                    finish();
                                }
                            });
                        } else {
                            TriggeredCommandForm.Builder builder =
                                    TriggeredCommandForm.Builder.newBuilder(
                                            AppConstants.SCHEMA_NAME,
                                            AppConstants.SCHEMA_VERSION,
                                            editingTrigger.getActions());
                            if (editingTrigger.getCommandTargetID() != null) {
                                builder.setTargetID(editingTrigger.getCommandTargetID());
                            }
                            wp.postNewTrigger(builder.build(), editingTrigger.getPredicate(), null).then(new DoneCallback<com.kii.thingif.trigger.Trigger>() {
                                @Override
                                public void onDone(com.kii.thingif.trigger.Trigger result) {
                                    Toast.makeText(CreateTriggerActivity.this, "New Trigger is created. TriggerID=" + result.getTriggerID(), Toast.LENGTH_LONG).show();
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                }
                            }, new FailCallback<Throwable>() {
                                @Override
                                public void onFail(Throwable result) {
                                    Toast.makeText(CreateTriggerActivity.this, "Failed to create new trigger!: " + result.getMessage(), Toast.LENGTH_LONG).show();
                                    setResult(Activity.RESULT_CANCELED);
                                    finish();
                                }
                            });
                        }
                    } else {
                        if (editingTrigger.getServerCode() != null) {
                            JSONObject parameters = null;
                            if (editingTrigger.getServerCode().parameters.size() > 0) {
                                parameters = new JSONObject();
                                for (Pair<String, Object> parameter : editingTrigger.getServerCode().parameters) {
                                    try {
                                        parameters.put(parameter.first, parameter.second);
                                    } catch (JSONException e) {
                                    }
                                }
                            }
                            ServerCode serverCode = new ServerCode(
                                    editingTrigger.getServerCode().endpoint,
                                    editingTrigger.getServerCode().executorAccessToken,
                                    editingTrigger.getServerCode().targetAppID,
                                    parameters);
                            wp.patchTrigger(editingTrigger.getTriggerID(), serverCode, editingTrigger.getPredicate()).then(new DoneCallback<com.kii.thingif.trigger.Trigger>() {
                                @Override
                                public void onDone(com.kii.thingif.trigger.Trigger result) {
                                    Toast.makeText(CreateTriggerActivity.this, "Trigger is updated. TriggerID=" + result.getTriggerID(), Toast.LENGTH_LONG).show();
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                }
                            }, new FailCallback<Throwable>() {
                                @Override
                                public void onFail(Throwable result) {
                                    Toast.makeText(CreateTriggerActivity.this, "Failed to update trigger!: " + result.getMessage(), Toast.LENGTH_LONG).show();
                                    setResult(Activity.RESULT_CANCELED);
                                    finish();
                                }
                            });
                        } else {
                            TriggeredCommandForm.Builder builder =
                                    TriggeredCommandForm.Builder.newBuilder(
                                            AppConstants.SCHEMA_NAME,
                                            AppConstants.SCHEMA_VERSION,
                                            editingTrigger.getActions());
                            if (editingTrigger.getCommandTargetID() != null) {
                                builder.setTargetID(editingTrigger.getCommandTargetID());
                            }
                            wp.patchTrigger(editingTrigger.getTriggerID(), builder.build(), editingTrigger.getPredicate(), null).then(new DoneCallback<com.kii.thingif.trigger.Trigger>() {
                                @Override
                                public void onDone(com.kii.thingif.trigger.Trigger result) {
                                    Toast.makeText(CreateTriggerActivity.this, "Trigger is updated. TriggerID=" + result.getTriggerID(), Toast.LENGTH_LONG).show();
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                }
                            }, new FailCallback<Throwable>() {
                                @Override
                                public void onFail(Throwable result) {
                                    Toast.makeText(CreateTriggerActivity.this, "Failed to update trigger!: " + result.getMessage(), Toast.LENGTH_LONG).show();
                                    setResult(Activity.RESULT_CANCELED);
                                    finish();
                                }
                            });
                        }
                    }
                }
            }
        });
        this.nextButton.setText("Next");
        this.previousButton = (Button)findViewById(R.id.wizard_previous_button);
        this.previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                WizardFragment wizardFragment = (WizardFragment) adapter.instantiateItem(viewPager, position);
                if (position > 0) {
                    position--;
                    viewPager.setCurrentItem(position);
                    wizardFragment = (WizardFragment) adapter.getItem(position);
                    nextButton.setText(wizardFragment.getNextButtonText());
                    previousButton.setText(wizardFragment.getPreviousButtonText());
                } else {
                    finish();
                }
            }
        });
        this.previousButton.setText("Cancel");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ThingIFAPI", this.api);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.api = savedInstanceState.getParcelable("ThingIFAPI");
    }
    public ThingIFAPI getApi() {
        return this.api;
    }

    @Override
    public void setNextButtonEnabled(boolean enabled) {
        this.nextButton.setEnabled(enabled);
    }

    private class CommandTriggerWizardPagerAdapter extends FragmentStatePagerAdapter {
        private static final int PAGE_COMMAND_SETTING  = 0;
        private static final int PAGE_PREDICATE_SETTING = 1;
        private static final int PAGE_TRIGGER_WHEN_SETTING = 2;

        public CommandTriggerWizardPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            WizardFragment fragment = null;
            switch (position) {
                case PAGE_COMMAND_SETTING:
                    fragment = CreateTriggerCommandFragment.newFragment(api);
                    break;
                case PAGE_PREDICATE_SETTING:
                    fragment = CreateTriggerPredicateFragment.newFragment(api);
                    break;
                case PAGE_TRIGGER_WHEN_SETTING:
                    fragment = CreateTriggersWhenFragment.newFragment(api);
                    break;
            }
            fragment.setController(CreateTriggerActivity.this);
            fragment.setEditingTrigger(editingTrigger);
            return fragment;
        }
        @Override
        public int getCount() {
            return WIZARD_PAGE_SIZE;
        }
    }
    private class ServerCodeTriggerWizardPagerAdapter extends FragmentStatePagerAdapter {
        private static final int PAGE_SERVER_CODE_SETTING  = 0;
        private static final int PAGE_PREDICATE_SETTING = 1;
        private static final int PAGE_TRIGGER_WHEN_SETTING = 2;

        public ServerCodeTriggerWizardPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            WizardFragment fragment = null;
            switch (position) {
                case PAGE_SERVER_CODE_SETTING:
                    fragment = CreateTriggerServerCodeFragment.newFragment(api);
                    break;
                case PAGE_PREDICATE_SETTING:
                    fragment = CreateTriggerPredicateFragment.newFragment(api);
                    break;
                case PAGE_TRIGGER_WHEN_SETTING:
                    fragment = CreateTriggersWhenFragment.newFragment(api);
                    break;
            }
            fragment.setController(CreateTriggerActivity.this);
            fragment.setEditingTrigger(editingTrigger);
            return fragment;
        }
        @Override
        public int getCount() {
            return WIZARD_PAGE_SIZE;
        }
    }
}
