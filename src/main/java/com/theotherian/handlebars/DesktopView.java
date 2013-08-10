package com.theotherian.handlebars;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.jknack.handlebars.Template;

@Path("desktop")
public class DesktopView {
  
  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response get() throws IOException {
    Template template = HandlebarsManager.get().compile("home");
    return Response.ok(template.apply(new Args("Ian", "Hello"))).build();
  }

}
