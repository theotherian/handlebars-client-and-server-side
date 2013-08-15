package com.theotherian.handlebars.client;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.jersey.client.ClientRequest;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.client.spi.AsyncConnectorCallback;
import org.glassfish.jersey.client.spi.Connector;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ContainerResponse;

public class ServerSideConnector implements Connector {
  
  private ApplicationHandler applicationHandler;
  
  public ServerSideConnector(ApplicationHandler applicationHandler) {
    this.applicationHandler = applicationHandler;
  }

  @Override
  public ClientResponse apply(ClientRequest request) throws ProcessingException {
    ContainerRequest containerRequest = buildFromClientRequest(request);
    Future<ContainerResponse> future = applicationHandler.apply(containerRequest);
    ContainerResponse containerResponse = null;
    try {
      containerResponse = future.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    ResponseBuilder responseBuilder = Response
      .status(containerResponse.getStatusInfo())
      .entity(containerResponse.getEntity())
      .lastModified(containerResponse.getLastModified())
      .type(containerResponse.getMediaType());
    
//    for (String key : containerResponse.getHeaders().keySet()) {
//      List<Object> values = containerResponse.getHeaders().get(key);
//      for (Object value : values) {
//        responseBuilder.header(key, value);
//      }
//    }

    ClientResponse clientResponse = new ClientResponse(request, responseBuilder.build());
    return clientResponse;
  }

  private ContainerRequest buildFromClientRequest(ClientRequest request) {
    URI uri = request.getUri();
    String method = request.getMethod();
    ContainerRequest containerRequest = 
        new ContainerRequest(URI.create("http://localhost:8080/myapp"), uri, method, null, null);
    return containerRequest;
  }

  @Override
  public Future<?> apply(ClientRequest request, AsyncConnectorCallback callback) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getName() {
    return "ServerSideConnector";
  }

  @Override
  public void close() {}

}
