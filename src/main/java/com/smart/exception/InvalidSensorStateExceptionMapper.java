package com.smart.exception;

import com.smart.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

// Exception Mapper for InvalidSensorStateException.
@Provider
public class InvalidSensorStateExceptionMapper implements ExceptionMapper<InvalidSensorStateException> {

    @Override
    public Response toResponse(InvalidSensorStateException ex) {
        // Create a structured error message for the state violation
        ErrorMessage error = new ErrorMessage(
            ex.getMessage(), 
            403, 
            "https://smartcampus.edu/docs/errors"
        );

        // Return HTTP 403 Forbidden with the error details
        return Response.status(Response.Status.FORBIDDEN)
                .entity(error)
                .build();
    }
}