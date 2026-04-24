package com.smart.exception;

import com.smart.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

// Handles RoomNotEmptyException and returns a 409 Conflict response.
@Provider
public class ConflictExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException ex) {
        // Build the structured error response
        ErrorMessage error = new ErrorMessage(
            ex.getMessage(), 
            409, 
            "https://smartcampus.edu/docs/errors"
        );

        // Return HTTP 409 status with the JSON body
        return Response.status(Response.Status.CONFLICT)
                .entity(error)
                .build();
    }
}