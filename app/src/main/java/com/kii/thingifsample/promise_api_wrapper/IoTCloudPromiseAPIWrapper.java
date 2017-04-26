package com.kii.thingifsample.promise_api_wrapper;

import android.util.Pair;

import com.kii.thingif.OnboardWithVendorThingIDOptions;
import com.kii.thingif.ThingIFAPI;
import com.kii.thingif.Target;
import com.kii.thingif.command.Action;
import com.kii.thingif.command.AliasAction;
import com.kii.thingif.command.Command;
import com.kii.thingif.command.CommandForm;
import com.kii.thingif.query.HistoryState;
import com.kii.thingif.query.HistoryStatesQuery;
import com.kii.thingif.trigger.Predicate;
import com.kii.thingif.trigger.ServerCode;
import com.kii.thingif.trigger.Trigger;
import com.kii.thingif.trigger.TriggerOptions;
import com.kii.thingif.trigger.TriggeredCommandForm;
import com.kii.thingif.trigger.TriggeredServerCodeResult;
import com.kii.thingifsample.AppConstants;
import com.kii.thingifsample.smart_light_demo.LightState;

import org.jdeferred.Promise;
import org.jdeferred.android.AndroidDeferredManager;
import org.jdeferred.android.DeferredAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class IoTCloudPromiseAPIWrapper {

    private AndroidDeferredManager adm;
    private ThingIFAPI api;

    public IoTCloudPromiseAPIWrapper(ThingIFAPI api) {
        this.adm = new AndroidDeferredManager();
        this.api = api;
    }

    public IoTCloudPromiseAPIWrapper(AndroidDeferredManager manager, ThingIFAPI api) {
        this.adm = manager;
        this.api = api;
    }

    public Promise<Target, Throwable, Void> onboard(final String thingID, final String thingPassword) {
        return adm.when(new DeferredAsyncTask<Void, Void, Target>() {
            @Override
            protected Target doInBackgroundSafe(Void... voids) throws Exception {
                return api.onboardWithThingID(thingID, thingPassword);
            }
        });
    }
    public Promise<Target, Throwable, Void> onboard(
            final String venderThingID,
            final String thingPassword,
            final String thingType,
            final String firmwareVerssion)
    {
        return adm.when(new DeferredAsyncTask<Void, Void, Target>() {
            @Override
            protected Target doInBackgroundSafe(Void... voids) throws Exception {
                OnboardWithVendorThingIDOptions.Builder builder = new OnboardWithVendorThingIDOptions.Builder();
                builder.setThingType(thingType);
                builder.setFirmwareVersion(firmwareVerssion);
                return api.onboardWithVendorThingID(venderThingID, thingPassword, builder.build());
            }
        });
    }
    public Promise<List<Command>, Throwable, Void> listCommands() {
        return adm.when(new DeferredAsyncTask<Void, Void, List<Command>>() {
            @Override
            protected List<Command> doInBackgroundSafe(Void... voids) throws Exception {
                List<Command> commands = new ArrayList<Command>();
                String paginationKey = null;
                do {
                    Pair<List<Command>, String> result = api.listCommands(0, paginationKey);
                    commands.addAll(result.first);
                    paginationKey = result.second;
                } while (paginationKey != null);
                return commands;
            }
        });
    }
    public Promise<Command, Throwable, Void> postNewCommand(final String schemaName, final int schemaVersion, final List<Action> actions) {
        return adm.when(new DeferredAsyncTask<Void, Void, Command>() {
            @Override
            protected Command doInBackgroundSafe(Void... voids) throws Exception {
                List<AliasAction> aliasActions = new ArrayList<>();
                aliasActions.add(new AliasAction(AppConstants.ALIAS, actions));
                CommandForm.Builder builder = CommandForm.Builder.newBuilder(aliasActions);
                return api.postNewCommand(builder.build());
            }
        });
    }
    public Promise<Command, Throwable, Void> postNewCommand(final CommandForm form) {
        return adm.when(new DeferredAsyncTask<Void, Void, Command>() {
            @Override
            protected Command doInBackgroundSafe(Void... voids) throws Exception {
                return api.postNewCommand(form);
            }
        });
    }

    public Promise<List<Trigger>, Throwable, Void> listTriggers() {
        return adm.when(new DeferredAsyncTask<Void, Void, List<Trigger>>() {
            @Override
            protected List<Trigger> doInBackgroundSafe(Void... voids) throws Exception {
                List<Trigger> triggers = new ArrayList<Trigger>();
                String paginationKey = null;
                do {
                    Pair<List<Trigger>, String> result = api.listTriggers(0, paginationKey);
                    triggers.addAll(result.first);
                    paginationKey = result.second;
                } while (paginationKey != null);
                return triggers;
            }
        });
    }

    public Promise<Trigger, Throwable, Void> postNewTrigger(
            final String schemaName,
            final int schemaVersion,
            final List<Action> actions,
            final Predicate predicate) {
        return adm.when(new DeferredAsyncTask<Void, Void, Trigger>() {
            @Override
            protected Trigger doInBackgroundSafe(Void... voids) throws Exception {
                List<AliasAction> aliasActions = new ArrayList<>();
                aliasActions.add(new AliasAction(AppConstants.ALIAS, actions));
                TriggeredCommandForm.Builder builder = TriggeredCommandForm.Builder.newBuilder(aliasActions);
                return api.postNewTrigger(builder.build(), predicate);
            }
        });
    }
    public Promise<Trigger, Throwable, Void> postNewTrigger(
            final TriggeredCommandForm form,
            final Predicate predicate,
            final TriggerOptions options) {
        return adm.when(new DeferredAsyncTask<Void, Void, Trigger>() {
            @Override
            protected Trigger doInBackgroundSafe(Void... voids) throws Exception {
                return api.postNewTrigger(form, predicate, options);
            }
        });
    }
    public Promise<Trigger, Throwable, Void> postNewTrigger(
            final ServerCode serverCode,
            final Predicate predicate) {
        return adm.when(new DeferredAsyncTask<Void, Void, Trigger>() {
            @Override
            protected Trigger doInBackgroundSafe(Void... voids) throws Exception {
                return api.postNewTrigger(serverCode, predicate);
            }
        });
    }
    public Promise<Trigger, Throwable, Void> postNewTrigger(
            final ServerCode serverCode,
            final Predicate predicate,
            final TriggerOptions options) {
        return adm.when(new DeferredAsyncTask<Void, Void, Trigger>() {
            @Override
            protected Trigger doInBackgroundSafe(Void... voids) throws Exception {
                return api.postNewTrigger(serverCode, predicate, options);
            }
        });
    }

    public Promise<Trigger, Throwable, Void> patchTrigger(
            final String triggerID,
            final String schemaName,
            final int schemaVersion,
            final List<Action> actions,
            final Predicate predicate) {
        return adm.when(new DeferredAsyncTask<Void, Void, Trigger>() {
            @Override
            protected Trigger doInBackgroundSafe(Void... voids) throws Exception {
                List<AliasAction> aliasActions = new ArrayList<>();
                aliasActions.add(new AliasAction(AppConstants.ALIAS, actions));
                TriggeredCommandForm.Builder builder = TriggeredCommandForm.Builder.newBuilder(aliasActions);
                return api.patchCommandTrigger(triggerID, builder.build(), predicate);
            }
        });
    }
    public Promise<Trigger, Throwable, Void> patchTrigger(
            final String triggerID,
            final TriggeredCommandForm form,
            final Predicate predicate,
            final TriggerOptions options) {
        return adm.when(new DeferredAsyncTask<Void, Void, Trigger>() {
            @Override
            protected Trigger doInBackgroundSafe(Void... voids) throws Exception {
                return api.patchCommandTrigger(triggerID, form, predicate, options);
            }
        });
    }
    public Promise<Trigger, Throwable, Void> patchTrigger(
            final String triggerID,
            final ServerCode serverCode,
            final Predicate predicate) {
        return adm.when(new DeferredAsyncTask<Void, Void, Trigger>() {
            @Override
            protected Trigger doInBackgroundSafe(Void... voids) throws Exception {
                return api.patchServerCodeTrigger(triggerID, serverCode, predicate);
            }
        });
    }
    public Promise<Trigger, Throwable, Void> patchTrigger(
            final String triggerID,
            final ServerCode serverCode,
            final Predicate predicate,
            final TriggerOptions options) {
        return adm.when(new DeferredAsyncTask<Void, Void, Trigger>() {
            @Override
            protected Trigger doInBackgroundSafe(Void... voids) throws Exception {
                return api.patchServerCodeTrigger(triggerID, serverCode, predicate, options);
            }
        });
    }

    public Promise<Trigger, Throwable, Void> enableTrigger(
            final String triggerID,
            final boolean enable) {
        return adm.when(new DeferredAsyncTask<Void, Void, Trigger>() {
            @Override
            protected Trigger doInBackgroundSafe(Void... voids) throws Exception {
                return api.enableTrigger(triggerID, enable);
            }
        });
    }

    public Promise<String, Throwable, Void> deleteTrigger(
            final String triggerID) {
        return adm.when(new DeferredAsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackgroundSafe(Void... voids) throws Exception {
                return api.deleteTrigger(triggerID);
            }
        });
    }

    public Promise<LightState, Throwable, Void> getLightState() {
        return adm.when(new DeferredAsyncTask<Void, Void, LightState>() {
            @Override
            protected LightState doInBackgroundSafe(Void... voids) throws Exception {
                return api.getTargetState(AppConstants.ALIAS, LightState.class);
            }
        });
    }
    public Promise<List<TriggeredServerCodeResult>, Throwable, Void> listTriggeredServerCodeResults(final String triggerID) {
        return adm.when(new DeferredAsyncTask<Void, Void, List<TriggeredServerCodeResult>>() {
            @Override
            protected List<TriggeredServerCodeResult> doInBackgroundSafe(Void... voids) throws Exception {
                List<TriggeredServerCodeResult> results = new ArrayList<TriggeredServerCodeResult>();
                String paginationKey = null;
                do {
                    Pair<List<TriggeredServerCodeResult>, String> result = api.listTriggeredServerCodeResults(triggerID, 0, paginationKey);
                    results.addAll(result.first);
                    paginationKey = result.second;
                } while (paginationKey != null);
                return results;
            }
        });
    }

    public Promise<Pair<List<HistoryState<LightState>>, String>, Throwable, Void> query(final HistoryStatesQuery query) {
        return adm.when(new DeferredAsyncTask<Void, Void, Pair<List<HistoryState<LightState>>, String>>() {
            @Override
            protected Pair<List<HistoryState<LightState>>, String> doInBackgroundSafe(Void... voids) throws Exception {
                return api.query(query, LightState.class);
            }
        });
    }
}
