package com.jupiter.europa.entity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by nathan on 5/25/15.
 */
public class ContextData {

    // Fields
    private final Map<Class<?>, Map<String, Object>> data = new LinkedHashMap<>();


    // Public Methods
    public <T> void put(String key, T value) {
        Class<?> type = value.getClass();
        if (!this.data.containsKey(type)) {
            this.data.put(type, new HashMap<>());
        }
        this.data.get(type).put(key, value);
    }

    public <T> void put(String key, T value, Class<T> type) {
        if (!this.data.containsKey(type)) {
            this.data.put(type, new HashMap<>());
        }
        this.data.get(type).put(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        if (this.data.containsKey(type)) {
            return (T) this.data.get(type).get(key);
        } else {
            return null;
        }
    }
}
