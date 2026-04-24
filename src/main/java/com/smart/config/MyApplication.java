package com.smart.config;

import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

// Entry point for the Smart Campus API
@ApplicationPath("/api/v1")
public class MyApplication extends ResourceConfig {
    
    public MyApplication() {
        // Automatically scan packages for resources, mappers, and filters
        packages("com.smart.resource", "com.smart.exception", "com.smart.filter");
    }
}