package com.smart.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<InvalidSensorStateException> {

    @Override
    public Response toResponse(InvalidSensorStateException exception) {
        String jsonError = "{\"error\": \"" + exception.getMessage() + "\", \"status\": 403}";
        
        return Response.status(Response.Status.FORBIDDEN)
                .entity(jsonError)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}