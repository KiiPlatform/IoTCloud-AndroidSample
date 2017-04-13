package com.kii.thingifsample.smart_light_demo;

import com.kii.thingif.TargetState;

public class LightState implements TargetState {

    public boolean power;
    public int brightness;
    public int[] color = new int[3];
    public int colorTemperature;

}
