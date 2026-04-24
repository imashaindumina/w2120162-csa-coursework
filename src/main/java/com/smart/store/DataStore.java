package com.smart.store;

import com.smart.model.Room;
import com.smart.model.Sensor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Singleton DataStore to hold data in memory
public class DataStore {
    private static DataStore instance;
    
    // Thread-safe maps for rooms and sensors
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final Map<String, Sensor> sensors = new ConcurrentHashMap<>();

    // Private constructor for Singleton pattern
    private DataStore() {
        // Sample data removed for clean initial state
    }

    // Get the single instance of DataStore
    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // Get the rooms map
    public Map<String, Room> getRooms() {
        return rooms;
    }

    // Get the sensors map
    public Map<String, Sensor> getSensors() {
        return sensors;
    }
}