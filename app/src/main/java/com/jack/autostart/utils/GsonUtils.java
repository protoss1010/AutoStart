package com.jack.autostart.utils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.lang.reflect.Type;

public class GsonUtils {

    private static final Gson sGson = new Gson();
    private static final Gson sPrettyPrintingGson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Serializes an object into json.
     *
     * @param object The object to serialize.
     * @return object serialized into json.
     */
    public static String toJson(final Object object) {
        return toJson(object, false);
    }

    /**
     * Serializes an object into json.
     *
     * @param object The object to serialize.
     * @return object serialized into json.
     */
    public static String toJson(final Object object, boolean prettyPrinting) {
        if (prettyPrinting) {
            return sPrettyPrintingGson.toJson(object);
        } else {
            return sGson.toJson(object);
        }
    }

    /**
     * Converts {@link String} to given type.
     *
     * @param json the json to convert.
     * @param type type type json will be converted to.
     * @return instance of type
     */
    public static <T> T fromJson(final String json, @NonNull final Type type) {
        return sGson.fromJson(json, type);
    }

    /**
     * Converts {@link String} to given type.
     *
     * @param json The json to convert.
     * @param type Type json will be converted to.
     * @return instance of type
     */
    public static <T> T fromJson(final String json, @NonNull final Class<T> type) {
        return sGson.fromJson(json, type);
    }

    /**
     * Converts {@link String} to given type.
     *
     * @param json The json to convert.
     * @param type Type json will be converted to.
     * @return instance of type
     */
    public static <T> T fromJson(final Reader json, @NonNull final Class<T> type) {
        return sGson.fromJson(json, type);
    }

}
