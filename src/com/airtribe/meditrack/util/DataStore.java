package com.airtribe.meditrack.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

// Generic in-memory storage, keyed by id, reusable for any entity type T
public class DataStore<T> {

    private Map<String, T> store = new HashMap<>();

    public void add(String id, T item) {
        store.put(id, item); // inserts, or overwrites if id already exists
    }

    public T getById(String id) {
        return store.get(id); // returns null automatically if id isn't found
    }

    public void remove(String id) {
        store.remove(id); // no-op if id doesn't exist, safe to call either way
    }

    public Collection<T> getAll() {
        return store.values(); // all stored objects, ignoring the keys
    }

    public boolean exists(String id) {
        return store.containsKey(id); // true/false check without retrieving the object
    }
}