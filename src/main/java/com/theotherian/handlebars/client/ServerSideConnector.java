package com.theotherian.handlebars.client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientRequest;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.client.spi.AsyncConnectorCallback;
import org.glassfish.jersey.client.spi.Connector;
import org.glassfish.jersey.message.internal.OutboundMessageContext.StreamProvider;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ContainerResponse;

import com.google.common.util.concurrent.MoreExecutors;

public class ServerSideConnector implements Connector {

  private ApplicationHandler applicationHandler;

  private Logger logger = Logger.getLogger(getClass());

  public ServerSideConnector(ApplicationHandler applicationHandler) {
    this.applicationHandler = applicationHandler;
  }

  @Override
  public ClientResponse apply(ClientRequest request) throws ProcessingException {
    ContainerRequest containerRequest = buildContainerRequestFromClientRequest(request);
    Future<ContainerResponse> future = applicationHandler.apply(containerRequest);
    ContainerResponse containerResponse = null;
    try {
      containerResponse = future.get();
    } catch (InterruptedException | ExecutionException e) {
      logger.error(e);
      throw new ProcessingException(e);
    }
    Response response = buildResponseFromContainerResponse(containerResponse);
    ClientResponse clientResponse = new ClientResponse(request, response);
    return clientResponse;
  }

  private Response buildResponseFromContainerResponse(ContainerResponse containerResponse) {
    ResponseBuilder responseBuilder = Response.status(containerResponse.getStatusInfo())
        .entity(containerResponse.getEntity()).lastModified(containerResponse.getLastModified())
        .type(containerResponse.getMediaType());

    for (String key : containerResponse.getHeaders().keySet()) {
      // For some bizarre reason, the Content-Type header can get written out
      // twice and cause an exception to be thrown
      if (!"Content-Type".equals(key)) {
        List<Object> values = containerResponse.getHeaders().get(key);
        for (Object value : values) {
          responseBuilder.header(key, value);
        }
      }
    }

    Response response = responseBuilder.build();
    return response;
  }

  private ContainerRequest buildContainerRequestFromClientRequest(ClientRequest request) {
    URI uri = request.getUri();
    String method = request.getMethod();
    ContainerRequest containerRequest = new ContainerRequest(URI.create("http://localhost:8080/myapp"), uri, method,
        null, null);

    MultivaluedMap<String, Object> headers = request.getHeaders();
    for (String key : headers.keySet()) {
      List<Object> values = headers.get(key);
      for (Object value : values) {
        containerRequest.header(key, value);
      }
    }

    if (request.getEntity() != null) {
      try {
        final StringWriter writer = new StringWriter();
        final OutputStream output = new WriterOutputStream(writer);
        request.setStreamProvider(new StreamProvider() {

          @Override
          public OutputStream getOutputStream(int contentLength) throws IOException {
            return output;
          }
        });
        request.writeEntity();
        containerRequest.setEntityStream(IOUtils.toInputStream(writer.toString()));
      } catch (IOException e) {
        logger.error(e);
        throw new RuntimeException(e);
      }
    }

    return containerRequest;
  }

  /* (non-Javadoc)
   * This is a total ripoff of the implementation in ApacheConnector#apply(request, callback)
   * @see org.glassfish.jersey.client.spi.Connector#apply(org.glassfish.jersey.client.ClientRequest, org.glassfish.jersey.client.spi.AsyncConnectorCallback)
   */
  @Override
  public Future<?> apply(final ClientRequest request, final AsyncConnectorCallback callback) {
    return MoreExecutors.sameThreadExecutor().submit(new Runnable() {
      @Override
      public void run() {
        try {
          callback.response(apply(request));
        } catch (ProcessingException ex) {
          callback.failure(ex);
        } catch (Throwable t) {
          callback.failure(t);
        }
      }
    });
  }

  @Override
  public String getName() {
    return "ServerSideConnector";
  }

  @Override
  public void close() {
  }

}
