package com.theotherian.handlebars;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("myapp")
public class MyApplication extends ResourceConfig {
  
  @Context private ApplicationHandler applicationHandler;
  
  public MyApplication() {
    packages("com.theotherian.handlebars");
    register(JacksonFeature.class);
    register(ServerConnectorProvider.class);
  }

}
