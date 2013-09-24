package com.theotherian.handlebars.client;

import java.util.concurrent.Future;

import javax.ws.rs.ProcessingException;

import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientRequest;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.client.spi.AsyncConnectorCallback;
import org.glassfish.jersey.client.spi.Connector;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;

public class HystrixProxyConnector implements Connector {
  
  public static final String RESOURCE_COMMAND_NAME = "HYSTRIX_RESOURCE_COMMAND_NAME";
  
  private final Connector connector;
  
  private final String groupName;
  
  public HystrixProxyConnector(Connector connector, String groupName) {
    this.connector = connector;
    this.groupName = groupName;
  }

  @Override
  public ClientResponse apply(ClientRequest request) throws ProcessingException {
    return new HystrixApacheCommand(request, connector, groupName).execute();
  }

  @Override
  public Future<?> apply(final ClientRequest request, final AsyncConnectorCallback callback) {
    HystrixApacheCommand command = new HystrixApacheCommand(request, connector, groupName);
    Future<ClientResponse> future = command.queue();
    try {
      callback.response(future.get());
    }
    catch (ProcessingException e) {
      callback.failure(e);
    }
    catch (Throwable t) {
      callback.failure(t);
    }
    return future;
  }
  
  private static final class HystrixApacheCommand extends HystrixCommand<ClientResponse> {

    private final ClientRequest request;
    
    private final Connector connector;
    
    private static final Logger LOGGER = Logger.getLogger(HystrixApacheCommand.class);
    
    protected HystrixApacheCommand(ClientRequest request, Connector connector, String groupName) {
      super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupName))
          .andCommandKey(buildCommandKeyFromRequest(request))
          .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(groupName + "-" + request.getMethod() + "-ThreadPool")));
      this.request = request;
      this.connector = connector;
    }
    
    private static HystrixCommandKey buildCommandKeyFromRequest(ClientRequest request) {
      Object commandName = request.getProperty(RESOURCE_COMMAND_NAME);
      if (commandName == null || !(commandName instanceof String) || ((String) commandName).isEmpty()) {
        LOGGER.warn("A command for the request '" + request.getMethod() + " " + request.getUri().getPath() + 
            "' has been created without a String based command name or with an empty String.\n  "
            + "Please call .property(HystrixProxyConnector.RESOURCE_COMMAND_NAME, \"name of resource\") against your "
            + "Builder instance or requests will be tracked at the path level which may include variable information");
        commandName = request.getUri().getPath();
      }
      return HystrixCommandKey.Factory.asKey(request.getMethod() + " " + commandName);
    }

    @Override
    protected ClientResponse run() throws Exception {
      return connector.apply(request);
    }
    
  }

  @Override
  public String getName() {
    return "Hystrix Apache Connector";
  }

  @Override
  public void close() {
    connector.close();
  }

}
