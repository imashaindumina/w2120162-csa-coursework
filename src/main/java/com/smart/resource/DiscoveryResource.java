package com.smart.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

// Root discovery endpoint to provide API metadata and resource links.
@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscoveryInfo() {
        // Create a map to hold the JSON structure
        Map<String, Object> apiInfo = new HashMap<>();
        
        // Basic metadata
        apiInfo.put("version", "v1.0.0");
        apiInfo.put("description", "Smart Campus Energy & Occupancy Monitoring API");
        apiInfo.put("admin_contact", "w2120162@my.westminster.ac.uk"); // Update with your email

        // Hypermedia Links
        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        
        apiInfo.put("resources", links);

        // Return 200 OK with the JSON body
        return Response.ok(apiInfo).build();
    }
}