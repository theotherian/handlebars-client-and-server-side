package com.theotherian.handlebars;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.github.jknack.handlebars.Template;

@Path("mobile")
public class MobileView {
  
  @GET
  public Response get() throws IOException {
    Template template = HandlebarsManager.get().compile("home.mobile");
    return Response.ok(template.apply(new Args("Ian", "Denny", "Chris"))).build();
  }

}
