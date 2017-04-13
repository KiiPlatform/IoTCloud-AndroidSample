package com.kii.thingifsample.smart_light_demo;

import com.google.gson.annotations.SerializedName;

public class SetBrightness implements BaseAction {
    public static String actionName = "setBrightness";

    @SerializedName("setBrightness")
    public int brightness;
    public SetBrightness() {
    }
    public SetBrightness(int brightness) {
        this.brightness = brightness;
    }

    public String getActionName() { return actionName; }
}
