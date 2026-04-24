package com.smart.exception;

import com.smart.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

// Exception Mapper to handle LinkedResourceNotFoundException.
@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        // Create a structured error message
        ErrorMessage error = new ErrorMessage(
            ex.getMessage(), 
            422, 
            "https://smartcampus.edu/docs/errors"
        );

        // Return HTTP 422 response with the error entity
        return Response.status(422)
                .entity(error)
                .build();
    }
}