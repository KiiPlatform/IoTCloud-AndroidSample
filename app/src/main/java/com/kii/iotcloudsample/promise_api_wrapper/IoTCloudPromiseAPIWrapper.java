package com.kii.iotcloudsample.promise_api_wrapper;

import android.util.Pair;

import com.kii.cloud.storage.KiiUser;
import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.Target;
import com.kii.iotcloud.command.Command;
import com.kii.iotcloud.trigger.Trigger;

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

    public Promise<Target, Throwable, Void> onBoard(final String thingID, final String thingPassword) {
        return adm.when(new DeferredAsyncTask<Void, Void, Target>() {
            @Override
            protected Target doInBackgroundSafe(Void... voids) throws Exception {
                return api.onboard(thingID, thingPassword);
            }
        });
    }
    public Promise<Target, Throwable, Void> onBoard(final String venderThingID, final String thingPassword, final String thingType) {
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
}
