package com.theotherian.handlebars;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("messages")
public class MessagesResource {
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Args get() {
    return new Args("Ian", "Good Afternoon", "Good Morrow");
  }

}
