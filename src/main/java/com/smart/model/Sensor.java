package com.smart.model;

import java.util.ArrayList; // Required for creating the historical readings list
import java.util.List;      // Required for defining the list of readings

/**
 * Model class representing a Sensor.
 * Includes fields for identification, type, status, and historical data logging.
 */
public class Sensor {
    private String id; // Unique identifier for the sensor
    private String type; // Type of sensor (e.g., Temperature, CO2)
    private double lastReading; // Stores the most recent numeric reading value
    private String status; // Current state of the sensor (e.g., ACTIVE, MAINTENANCE)
    
    // List to store the historical log of all numeric readings (Part 4 Requirement)
    private List<Double> readings = new ArrayList<>(); 

    // Default constructor essential for JSON deserialization by JAX-RS
    public Sensor() {}

    // Overloaded constructor to initialize sensor with specific details
    public Sensor(String id, String type, double lastReading, String status) {
        this.id = id;
        this.type = type;
        this.lastReading = lastReading;
        this.status = status;
    }

    // Getter for sensor ID
    public String getId() { return id; }
    
    // Setter for sensor ID
    public void setId(String id) { this.id = id; }

    // Getter for sensor type
    public String getType() { return type; }
    
    // Setter for sensor type
    public void setType(String type) { this.type = type; }

    // Getter for the most recent reading value
    public double getLastReading() { return lastReading; }
    
    // Setter for the most recent reading value
    public void setLastReading(double lastReading) { this.lastReading = lastReading; }

    // Getter for the current status (Used for MAINTENANCE check in Task 5.3)
    public String getStatus() { return status; }
    
    // Setter for the sensor status
    public void setStatus(String status) { this.status = status; }

    /**
     * Returns the historical log of readings for this sensor.
     * This method resolves the 'cannot find symbol' error in SensorReadingResource.
     */
    public List<Double> getReadings() {
        return readings;
    }

    // Setter to update the entire readings history list
    public void setReadings(List<Double> readings) {
        this.readings = readings;
    }
}