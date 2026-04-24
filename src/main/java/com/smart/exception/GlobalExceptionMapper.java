package com.smart.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

// The Global Safety Net: Catches any unexpected runtime errors (500 Internal Server Error).
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        // Log the actual error on the server console for debugging
        LOGGER.log(Level.SEVERE, "An unexpected error occurred: ", exception);

        // Return a generic, user-friendly JSON message to the client
        String jsonError = "{"
                + "\"error\": \"Internal Server Error\","
                + "\"message\": \"An unexpected condition was encountered on the server.\","
                + "\"status\": 500"
                + "}";

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(jsonError)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}