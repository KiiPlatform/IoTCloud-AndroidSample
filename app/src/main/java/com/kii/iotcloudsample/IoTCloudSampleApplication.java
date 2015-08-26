package com.kii.iotcloudsample;

import android.app.Application;

import com.kii.cloud.storage.Kii;

public class IoTCloudSampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Kii.initialize(this, AppConstants.APPID, AppConstants.APPKEY, AppConstants.APPSITEURL);
    }

}
