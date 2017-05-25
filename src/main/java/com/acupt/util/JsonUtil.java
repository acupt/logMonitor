package com.acupt.util;

import com.google.gson.Gson;

/**
 * Created by liujie on 2017/5/17.
 */
public class JsonUtil {

    private static Gson gson = new Gson();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> tClass) {
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, tClass);
    }
}
