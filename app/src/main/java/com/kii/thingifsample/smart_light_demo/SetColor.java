package com.kii.thingifsample.smart_light_demo;

import com.google.gson.annotations.SerializedName;

public class SetColor implements BaseAction {
    public static String actionName = "setColor";

    @SerializedName("setColor")
    public int[] color = new int[3];
    public SetColor() {
    }
    public SetColor(int r, int g, int b) {
        this.color[0] = r;
        this.color[1] = g;
        this.color[2] = b;
    }

    public String getActionName() { return actionName; }
}
