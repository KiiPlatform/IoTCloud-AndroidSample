package com.kii.iotcloudsample.smart_light_demo;

import com.kii.iotcloud.command.Action;

public class SetColorTemperature extends Action {
    public int colorTemperature;
    public SetColorTemperature() {
    }
    public SetColorTemperature(int colorTemperature) {
        this.colorTemperature = colorTemperature;
    }
    @Override
    public String getActionName() {
        return "setColorTemperature";
    }
}
