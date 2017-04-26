package com.kii.thingifsample.utils;

import android.os.Bundle;

import com.kii.thingifsample.smart_light_demo.LightState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Utils {
    private Utils() {
    }
    public static String toColorString(int[] color) {
        return "#" + (color[0] > 15 ? "" : "0") + Integer.toHexString(color[0])
                + (color[1] > 15 ? "" : "0") + Integer.toHexString(color[1])
                + (color[2] > 15 ? "" : "0") + Integer.toHexString(color[2]);
    }
    public static JSONObject bundleToJson(Bundle bundle) {
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                json.put(key, wrap(bundle.get(key)));
            } catch (JSONException e) {
            }
        }
        return json;
    }
    public static Object wrap(Object o) {
        if (o == null) {
            return JSONObject.NULL;
        }
        if (o instanceof JSONArray || o instanceof JSONObject) {
            return o;
        }
        if (o.equals(JSONObject.NULL)) {
            return o;
        }
        try {
            if (o instanceof Collection) {
                return new JSONArray((Collection) o);
            } else if (o.getClass().isArray()) {
                final int length = Array.getLength(o);
                List<Object> values = new ArrayList<Object>(length);
                for (int i = 0; i < length; ++i) {
                    values.add(Array.get(o, i));
                }
                return new JSONArray(values);
            }
            if (o instanceof Map) {
                return new JSONObject((Map) o);
            }
            if (o instanceof Boolean ||
                    o instanceof Byte ||
                    o instanceof Character ||
                    o instanceof Double ||
                    o instanceof Float ||
                    o instanceof Integer ||
                    o instanceof Long ||
                    o instanceof Short ||
                    o instanceof String) {
                return o;
            }
            if (o.getClass().getPackage().getName().startsWith("java.")) {
                return o.toString();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public static String lightStateToString(LightState state) {
        return "{ " + state.power +  ", "
                + state.brightness + ", "
                + toColorString(state.color) + ", "
                + state.colorTemperature +"}";
    }
}
