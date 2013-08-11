package com.theotherian.handlebars;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("handlebars.js")
public class HandlebarsJsResource {
  
  @GET
  @Produces("text/javascript")
  public Response get() {
    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("handlebars.js");
    return Response.ok(inputStream).build();
  }

}
