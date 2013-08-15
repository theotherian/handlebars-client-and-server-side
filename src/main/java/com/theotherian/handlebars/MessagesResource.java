package com.theotherian.handlebars;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("messages/{name}")
public class MessagesResource {
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Message> get(@PathParam("name") String name) {
    return MessagesDatastore.getMessagesByName(name);
  }
  
  @PUT
  public Response put(@PathParam("name") String name, List<Message> messages) {
    return Response.ok().build();
  }

}
