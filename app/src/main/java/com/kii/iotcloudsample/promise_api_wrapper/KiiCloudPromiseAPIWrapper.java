package com.kii.iotcloudsample.promise_api_wrapper;

import com.kii.cloud.storage.KiiThing;
import com.kii.cloud.storage.KiiUser;
import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.Target;

import org.jdeferred.Promise;
import org.jdeferred.android.AndroidDeferredManager;
import org.jdeferred.android.DeferredAsyncTask;

public class KiiCloudPromiseAPIWrapper {

    private AndroidDeferredManager adm;
    private IoTCloudAPI api;

    public KiiCloudPromiseAPIWrapper(IoTCloudAPI api) {
        this.adm = new AndroidDeferredManager();
        this.api = api;
    }

    public KiiCloudPromiseAPIWrapper(AndroidDeferredManager manager) {
        this.adm = manager;
    }

    public Promise <Void, Throwable, Void> login(final String identifier, final String password) {
        return adm.when(new DeferredAsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackgroundSafe(Void... voids) throws Exception {
                KiiUser.logIn(identifier, password);
                return null;
            }
        });
    }
    public Promise <KiiThing, Throwable, Void> loadWithThingID(final String thingID) {
        return adm.when(new DeferredAsyncTask<Void, Void, KiiThing>() {
            @Override
            protected KiiThing doInBackgroundSafe(Void... voids) throws Exception {
                if (!KiiUser.isLoggedIn()) {
                    KiiUser.loginWithToken(api.getOwner().getAccessToken());
                }
                return KiiThing.loadWithThingID(thingID);
            }
        });
    }
}
