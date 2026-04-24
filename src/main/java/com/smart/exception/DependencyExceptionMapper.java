package com.smart.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DependencyExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        //use 422 Unprocessable Entity for semantic errors
        String jsonError = "{\"error\": \"" + exception.getMessage() + "\", \"status\": 422}";
        
        return Response.status(422) // Unprocessable Entity
                .entity(jsonError)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}