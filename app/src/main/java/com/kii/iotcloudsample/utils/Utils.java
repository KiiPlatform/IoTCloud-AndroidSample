package com.kii.iotcloudsample.utils;

public class Utils {
    public static String toColorString(int[] color) {
        return "#" + (color[0] > 15 ? "" : "0") + Integer.toHexString(color[0])
                + (color[1] > 15 ? "" : "0") + Integer.toHexString(color[1])
                + (color[2] > 15 ? "" : "0") + Integer.toHexString(color[2]);


    }
}
