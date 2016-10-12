package com.kii.thingifsample.promise_api_wrapper;

import android.util.Pair;

import com.kii.thingif.ThingIFAPI;
import com.kii.thingif.Target;
import com.kii.thingif.TargetState;
import com.kii.thingif.command.Action;
import com.kii.thingif.command.Command;
import com.kii.thingif.command.CommandForm;
import com.kii.thingif.exception.ThingIFException;
import com.kii.thingif.trigger.Predicate;
import com.kii.thingif.trigger.ServerCode;
import com.kii.thingif.trigger.Trigger;
import com.kii.thingif.trigger.TriggerOptions;
import com.kii.thingif.trigger.TriggeredCommandForm;
import com.kii.thingif.trigger.TriggeredServerCodeResult;
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
                return api.onboard(thingID, thingPassword);
            }
        });
    }
    public Promise<Target, Throwable, Void> onboard(final String venderThingID, final String thingPassword, final String thingType) {
        return adm.when(new DeferredAsyncTask<Void, Void, Target>() {
            @Override
            protected Target doInBackgroundSafe(Void... voids) throws Exception {
                return api.onboard(venderThingID, thingPassword, thingType, null);
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
                return api.postNewCommand(schemaName, schemaVersion, actions);
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
                return api.postNewTrigger(schemaName, schemaVersion, actions, predicate);
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

    public Promise<Trigger, Throwable, Void> patchTrigger(
            final String triggerID,
            final String schemaName,
            final int schemaVersion,
            final List<Action> actions,
            final Predicate predicate) {
        return adm.when(new DeferredAsyncTask<Void, Void, Trigger>() {
            @Override
            protected Trigger doInBackgroundSafe(Void... voids) throws Exception {
                return api.patchTrigger(triggerID, schemaName, schemaVersion, actions, predicate);
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
                return api.patchTrigger(triggerID, form, predicate, options);
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
                return api.patchTrigger(triggerID, serverCode, predicate);
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
                return api.getTargetState(LightState.class);
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

}
