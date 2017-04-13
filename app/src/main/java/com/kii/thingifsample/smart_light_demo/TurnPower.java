package com.kii.thingifsample.smart_light_demo;

import com.google.gson.annotations.SerializedName;

public class TurnPower implements BaseAction {
    public static String actionName = "turnPower";

    @SerializedName("turnPower")
    public boolean power;

    public TurnPower() {
    }
    public TurnPower(boolean power) {
        this.power = power;
    }

    public String getActionName() { return actionName; }
}
