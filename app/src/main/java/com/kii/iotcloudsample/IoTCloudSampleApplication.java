package com.kii.iotcloudsample;

import android.app.Application;
import android.content.res.AssetManager;

import com.kii.cloud.storage.Kii;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class IoTCloudSampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Properties properties = new Properties();
        AssetManager am = getAssets();
        try {
            InputStream is = am.open("settings.properties");
            properties.load(is);
            AppConstants.APPID = properties.getProperty("appId");
            AppConstants.APPKEY = properties.getProperty("appKey");
            AppConstants.APPHOST = properties.getProperty("appHost");
        } catch (IOException e) {
            // Just Use Default.
        }
        Kii.initialize(this, AppConstants.APPID, AppConstants.APPKEY, AppConstants.APPSITEURL);
    }

}
