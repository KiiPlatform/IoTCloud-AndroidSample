package com.kii.thingifsample.promise_api_wrapper;

import android.util.Pair;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.Target;
import com.kii.iotcloud.TargetState;
import com.kii.iotcloud.command.Action;
import com.kii.iotcloud.command.Command;
import com.kii.iotcloud.exception.IoTCloudException;
import com.kii.iotcloud.trigger.Predicate;
import com.kii.iotcloud.trigger.Trigger;
import com.kii.thingifsample.smart_light_demo.LightState;

import org.jdeferred.Promise;
import org.jdeferred.android.AndroidDeferredManager;
import org.jdeferred.android.DeferredAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class IoTCloudPromiseAPIWrapper {

    private AndroidDeferredManager adm;
    private IoTCloudAPI api;

    public IoTCloudPromiseAPIWrapper(IoTCloudAPI api) {
        this.adm = new AndroidDeferredManager();
        this.api = api;
    }

    public IoTCloudPromiseAPIWrapper(AndroidDeferredManager manager, IoTCloudAPI api) {
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

    public Promise<Trigger, Throwable, Void> deleteTrigger(
            final String triggerID) {
        return adm.when(new DeferredAsyncTask<Void, Void, Trigger>() {
            @Override
            protected Trigger doInBackgroundSafe(Void... voids) throws Exception {
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
}
