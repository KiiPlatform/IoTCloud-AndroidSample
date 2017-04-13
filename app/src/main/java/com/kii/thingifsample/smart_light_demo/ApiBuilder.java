package com.kii.thingifsample.smart_light_demo;

import android.content.Context;

import com.kii.thingif.KiiApp;
import com.kii.thingif.ThingIFAPI;
import com.kii.thingif.Owner;
import com.kii.thingifsample.AppConstants;
import com.kii.thingifsample.IoTCloudSampleApplication;

public class ApiBuilder {

    public static ThingIFAPI buildApi(Context context, Owner owner, String alias) {
        String appId = ((IoTCloudSampleApplication) context).getAppId();
        String appKey = ((IoTCloudSampleApplication) context).getAppKey();
        String appHost = ((IoTCloudSampleApplication) context).getAppHost();

        KiiApp app = KiiApp.Builder.builderWithHostName(appId, appKey, appHost).build();
        ThingIFAPI.Builder builder = ThingIFAPI.Builder.newBuilder(context, app, owner);
        builder.registerAction(alias, TurnPower.actionName, TurnPower.class);
        builder.registerAction(alias, SetBrightness.actionName, SetBrightness.class);
        builder.registerAction(alias, SetColor.actionName, SetColor.class);
        builder.registerAction(alias, SetColorTemperature.actionName, SetColorTemperature.class);
        builder.registerTargetState(alias, LightState.class);
        return builder.build();
    }

}
