package com.kii.iotcloudsample;

import com.kii.cloud.storage.KiiUser;
import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.Target;

import org.jdeferred.Promise;
import org.jdeferred.android.AndroidDeferredManager;
import org.jdeferred.android.DeferredAsyncTask;

public class KiiCloudPromiseAPIWrapper {

    private AndroidDeferredManager adm;

    public KiiCloudPromiseAPIWrapper() {
        this.adm = new AndroidDeferredManager();
    }

    public KiiCloudPromiseAPIWrapper(AndroidDeferredManager manager) {
        this.adm = manager;
    }

    public Promise<Void, Throwable, Void> loginWithCredentials() {
        return adm.when(new DeferredAsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackgroundSafe(Void... voids) throws Exception {
                KiiUser.loginWithStoredCredentials();
                return null;
            }
        });
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

}
