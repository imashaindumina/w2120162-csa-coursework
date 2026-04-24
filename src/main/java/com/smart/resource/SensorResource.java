package com.smart.resource;

import com.smart.model.Sensor;
import com.smart.model.Room;
import com.smart.model.ErrorMessage;
import com.smart.store.DataStore;
import com.smart.exception.LinkedResourceNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private DataStore db = DataStore.getInstance(); // Access the shared data storage

    @GET // Handle GET request to retrieve all sensors or filter by type
    public Response getAllSensors(@QueryParam("type") String type) {
        Collection<Sensor> allSensors = db.getSensors().values();
        
        if (type == null || type.isEmpty()) {
            return Response.ok(new ArrayList<>(allSensors)).build(); // Return all sensors if no type filter
        }

        List<Sensor> filteredSensors = new ArrayList<>();
        for (Sensor s : allSensors) {
            if (s.getType() != null && s.getType().equalsIgnoreCase(type)) {
                filteredSensors.add(s); // Add sensor to list if type matches
            }
        }
        return Response.ok(filteredSensors).build();
    }

    @GET // Handle GET request to fetch a specific sensor by ID
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = db.getSensors().get(sensorId);
        
        if (sensor == null) {
            ErrorMessage error = new ErrorMessage("Sensor not found with ID: " + sensorId, 404, "https://smartcampus.edu/docs/errors");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build(); 
        }
        
        return Response.ok(sensor).build();
    }

    @POST // Handle POST request to register a new sensor and link it to a room
    public Response createSensor(Sensor sensor, @QueryParam("roomId") String roomId) {
        if (sensor.getId() == null || sensor.getId().isEmpty()) {
            ErrorMessage error = new ErrorMessage("Sensor ID is required", 400, "https://smartcampus.edu/docs/errors");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        if (roomId != null && !db.getRooms().containsKey(roomId)) {
            throw new LinkedResourceNotFoundException("Dependency Error: Room ID " + roomId + " not found."); // Part 5 Task 2
        }

        db.getSensors().put(sensor.getId(), sensor); // Save sensor to data store
        
        if (roomId != null) {
            db.getRooms().get(roomId).getSensorIds().add(sensor.getId()); // Maintain integrity by linking sensor to room
        }

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    @DELETE // Part 5 Task 1: Handle DELETE request to remove a sensor
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        if (!db.getSensors().containsKey(sensorId)) {
            ErrorMessage error = new ErrorMessage("Sensor not found", 404, "https://smartcampus.edu/docs/errors");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        db.getSensors().remove(sensorId); 

        // Maintain referential integrity by removing the ID from all room assignments
        for (Room room : db.getRooms().values()) {
            room.getSensorIds().remove(sensorId);
        }

        return Response.noContent().build(); 
    }

    @Path("/{sensorId}/readings") 
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        if (!db.getSensors().containsKey(sensorId)) {
            ErrorMessage error = new ErrorMessage("Cannot provide readings. Sensor not found.", 404, "https://smartcampus.edu/docs/errors");
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(error).build());
        }
        return new SensorReadingResource(sensorId); 
    }
}