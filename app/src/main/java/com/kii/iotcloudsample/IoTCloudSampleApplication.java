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
            appId = properties.getProperty("appId");
            appKey = properties.getProperty("appKey");
            String appHost = properties.getProperty("appHost");
            ioTappBaseUrl = "https://" + appHost;
            appBaseUrl = ioTappBaseUrl + "/api";
        } catch (IOException e) {
            appId = AppConstants.APPID;
            appKey = AppConstants.APPKEY;
            appBaseUrl = AppConstants.APPSITEURL;
            ioTappBaseUrl = AppConstants.APPSITEURL_IOT;
        }
        Kii.initialize(this, appId, appKey, appBaseUrl);
    }

    private String appId;
    private String appKey;
    private String appBaseUrl;
    private String ioTappBaseUrl;

    public String getAppId() {
        return this.appId;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public String getAppBaseUrl() {
        return this.appBaseUrl;
    }

    public String getIoTappBaseUrlAppBaseUrl() {
        return this.ioTappBaseUrl;
    }

}
