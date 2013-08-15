package com.theotherian.handlebars;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ApplicationHandler;
import org.joda.time.DateTime;

import com.github.jknack.handlebars.Template;
import com.google.common.collect.Lists;
import com.theotherian.handlebars.client.MessageClient;

@Path("desktop")
public class DesktopView {
  
  @Context private ApplicationHandler applicationHandler;
  
  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response get() throws IOException, InterruptedException, ExecutionException {
    MessageClient.put("Ian", Lists.newArrayList(Message.at("Foo", DateTime.now())));
    Template template = HandlebarsManager.get().compile("home");
    return Response.ok(template.apply(new Args("Ian", "Denny", "Chris"))).build();
  }

}
