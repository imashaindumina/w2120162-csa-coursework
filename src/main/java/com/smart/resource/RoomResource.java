package com.smart.resource;

import com.smart.model.Room;
import com.smart.model.ErrorMessage;
import com.smart.store.DataStore;
import com.smart.exception.RoomNotEmptyException; 
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private DataStore db = DataStore.getInstance(); // Get the shared data store instance

    @GET // Handle GET request to retrieve all rooms
    public Response getAllRooms() {
        Collection<Room> rooms = db.getRooms().values(); // Fetch rooms from memory
        return Response.ok(new ArrayList<>(rooms)).build(); 
    }

    @POST // Handle POST request to create a new room
    public Response createRoom(Room room) {
        // Validate if Room ID is present in the request body
        if (room.getId() == null || room.getId().trim().isEmpty()) {
            ErrorMessage error = new ErrorMessage("Room ID required", 400, "https://smartcampus.edu/docs/errors");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        db.getRooms().put(room.getId(), room); // Store the room in the map
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    @GET // Handle GET request to find a room by its ID
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = db.getRooms().get(roomId); // Lookup room by ID
        // Return 404 error if room does not exist
        if (room == null) {
            ErrorMessage error = new ErrorMessage("Room not found", 404, "https://smartcampus.edu/docs/errors");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        return Response.ok(room).build();
    }

    @DELETE // Handle DELETE request for a specific room
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = db.getRooms().get(roomId); // Find the room to be deleted

        // Return 404 if the room ID provided is invalid
        if (room == null) {
            ErrorMessage error = new ErrorMessage("Room not found", 404, "https://smartcampus.edu/docs/errors");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            // Throw custom exception to be handled by ConflictExceptionMapper
            throw new RoomNotEmptyException("Cannot delete Room " + roomId + ". It is occupied by active sensors.");
        }

        db.getRooms().remove(roomId); // Successfully remove room from data store
        return Response.noContent().build(); 
    }
}