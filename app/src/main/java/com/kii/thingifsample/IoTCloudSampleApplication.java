package com.kii.thingifsample;

import android.app.Application;
import android.content.res.AssetManager;

import com.kii.cloud.storage.Kii;
import com.kii.thingif.ThingIFAPI;
import com.kii.thingif.Target;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class IoTCloudSampleApplication extends Application {

    private static IoTCloudSampleApplication context;
    public static IoTCloudSampleApplication getInstance() {
        return context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Properties properties = new Properties();
        AssetManager am = getAssets();
        try {
            InputStream is = am.open("settings.properties");
            properties.load(is);
            appId = properties.getProperty("appId");
            appKey = properties.getProperty("appKey");
            appHost = properties.getProperty("appHost");
            senderID = properties.getProperty("senderId");
        } catch (IOException e) {
            appId = AppConstants.APPID;
            appKey = AppConstants.APPKEY;
            appHost = AppConstants.APPHOST;
            senderID = AppConstants.SENDER_ID;
        }
        appBaseUrl = "https://" + appHost + "/api";
        Kii.initialize(this, appId, appKey, appBaseUrl);
    }

    private String appId;
    private String appKey;
    private String appHost;
    private String appBaseUrl;
    private String senderID;

    public String getAppId() {
        return this.appId;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public String getAppBaseUrl() {
        return this.appBaseUrl;
    }

    public String getAppHost() {
        return this.appHost;
    }

    public String getSenderID() {
        return this.senderID;
    }
}
