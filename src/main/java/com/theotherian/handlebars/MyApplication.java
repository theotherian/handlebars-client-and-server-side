package com.theotherian.handlebars;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("myapp")
public class MyApplication extends ResourceConfig {
  
  public MyApplication() {
    packages("com.theotherian.handlebars");
    register(JacksonFeature.class);
    register(ServerConnectorProvider.class);
    register(HandlebarsResourceFeature.class);
  }

}
