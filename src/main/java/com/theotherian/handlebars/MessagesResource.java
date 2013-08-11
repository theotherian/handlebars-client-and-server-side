package com.theotherian.handlebars;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

@Path("messages/{name}")
public class MessagesResource implements Helper<String> {
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Message> get(@PathParam("name") String name) {
    return MessagesDatastore.getMessagesByName(name);
  }

  @Override
  public CharSequence apply(String context, Options options) throws IOException {
    return null;
  }
  
}
