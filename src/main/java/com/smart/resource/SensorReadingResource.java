package com.smart.resource;

import com.smart.model.Sensor;
import com.smart.model.ErrorMessage;
import com.smart.store.DataStore;
import com.smart.exception.InvalidSensorStateException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private DataStore db = DataStore.getInstance(); // Get the singleton data store instance
    private String sensorId; // Store the sensor ID passed from parent resource

    // Constructor to set the context for this specific sensor
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET // Handle GET requests to fetch reading history
    public Response getReadings() {
        Sensor sensor = db.getSensors().get(sensorId); // Find the sensor in memory
        List<Double> history = sensor.getReadings(); // Get its historical readings list
        return Response.ok(history).build(); 
    }

    @POST // Handle POST requests to add a new reading
    public Response addReading(Double newValue) {
        Sensor sensor = db.getSensors().get(sensorId); // Retrieve the target sensor
        
        // Return 404 error if the sensor object is null
        if (sensor == null) {
            ErrorMessage error = new ErrorMessage("Sensor not found", 404, "https://smartcampus.edu/docs/errors");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        // Block updates if sensor status is MAINTENANCE 
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new InvalidSensorStateException("Access Denied: Sensor " + sensorId + " is in MAINTENANCE mode.");
        }

        // Return 400 error if the numeric value is missing in the request
        if (newValue == null) {
            ErrorMessage error = new ErrorMessage("Reading value is required", 400, "https://smartcampus.edu/docs/errors");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        sensor.getReadings().add(newValue); // Append the new value to history list
        sensor.setLastReading(newValue); // Update the current status of the sensor

        return Response.status(Response.Status.CREATED).entity(newValue).build(); // Return 201 Created
    }
}