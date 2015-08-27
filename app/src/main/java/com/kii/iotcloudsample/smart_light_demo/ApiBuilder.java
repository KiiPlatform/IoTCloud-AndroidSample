package com.kii.iotcloudsample.smart_light_demo;

import android.content.Context;

import com.kii.iotcloud.IoTCloudAPI;
import com.kii.iotcloud.IoTCloudAPIBuilder;
import com.kii.iotcloud.Owner;
import com.kii.iotcloud.schema.SchemaBuilder;
import com.kii.iotcloudsample.AppConstants;

public class ApiBuilder {

    public static IoTCloudAPI buildApi(Context context, Owner owner) {
        IoTCloudAPIBuilder builder = IoTCloudAPIBuilder.newBuilder(context, AppConstants.APPID,
                AppConstants.APPKEY, AppConstants.APPSITEURL_IOT, owner);
        SchemaBuilder schemaBuilder = SchemaBuilder.newSchemaBuilder("SmartLight-Demo",
                "Smart-Light-Demo", 1, LightState.class);
        schemaBuilder.addActionClass(TurnPower.class, TurnPowerResult.class);
        schemaBuilder.addActionClass(SetBrightness.class, SetBrightnessResult.class);
        schemaBuilder.addActionClass(SetColor.class, SetColorResult.class);
        schemaBuilder.addActionClass(SetColorTemperature.class, SetColorTemperatureResult.class);
        builder.addSchema(schemaBuilder.build());
        return builder.build();
    }

}
