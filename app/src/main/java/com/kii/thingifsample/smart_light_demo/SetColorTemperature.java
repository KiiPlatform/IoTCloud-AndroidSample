package com.kii.thingifsample.smart_light_demo;

import com.google.gson.annotations.SerializedName;

public class SetColorTemperature implements BaseAction {
    public static String actionName = "setColorTemperature";

    @SerializedName("setColorTemperature")
    public int colorTemperature;
    public SetColorTemperature() {
    }
    public SetColorTemperature(int colorTemperature) {
        this.colorTemperature = colorTemperature;
    }


    public String getActionName() { return actionName; }
}
