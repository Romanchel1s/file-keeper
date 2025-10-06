package org.example.server;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AppContext {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public <T> void register(Class<T> type, Supplier<T> factory) {
        beans.put(type, factory.get());
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        return (T) beans.get(type);
    }
}