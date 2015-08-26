package com.kii.iotcloudsample.promise_api_wrapper;

import com.kii.cloud.storage.KiiUser;
import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.Target;

import org.jdeferred.Promise;
import org.jdeferred.android.AndroidDeferredManager;
import org.jdeferred.android.DeferredAsyncTask;

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

    public Promise<Target, Throwable, Void> onBoard(final String thingID, final String thingPassword, final boolean isVendorThingID) {
        return adm.when(new DeferredAsyncTask<Void, Void, Target>() {
            @Override
            protected Target doInBackgroundSafe(Void... voids) throws Exception {
                if (!isVendorThingID) {
                    return api.onBoard(thingID, thingPassword);
                } else {
                    return api.onBoard(thingID, thingPassword, null, null);
                }
            }
        });
    }

}
