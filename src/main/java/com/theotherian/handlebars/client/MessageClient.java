package com.theotherian.handlebars.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnector;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.theotherian.handlebars.Message;
import com.theotherian.handlebars.ServerConnectorProvider.ServerConnectorFactory;

public final class MessageClient {
  
  private static final MessageClient INSTANCE = new MessageClient();
  
  private final Client client;
  
  private static final Logger LOGGER = Logger.getLogger(MessageClient.class);

  private MessageClient() {
    ClientConfig clientConfig = new ClientConfig();
    clientConfig.property(ClientProperties.READ_TIMEOUT, 2000);
    clientConfig.property(ClientProperties.CONNECT_TIMEOUT, 500);
    
    PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
    connectionManager.setMaxTotal(100);
    connectionManager.setDefaultMaxPerRoute(20);
    
    clientConfig.property(ApacheClientProperties.CONNECTION_MANAGER, connectionManager);
//    ApacheConnector connector = new ApacheConnector(clientConfig);
    HystrixProxyConnector connector = new HystrixProxyConnector(ServerConnectorFactory.build(), "MessageClient");
    
//    ServerSideConnector connector = ServerConnectorFactory.build();
    clientConfig.connector(connector);
    
    client = ClientBuilder.newClient(clientConfig);
    client.register(JacksonFeature.class);
  }
  
  public static List<Message> get(String name) {
    long start = System.currentTimeMillis();

    try {
      return INSTANCE.client.target("http://localhost:8080/myapp").path("latestmessages/" + name)
          .request(MediaType.APPLICATION_JSON).property(HystrixProxyConnector.RESOURCE_COMMAND_NAME, "messages")
            .get(new GenericType<List<Message>>(){});
    } finally {
      LOGGER.info("Time elapsed (get): " + (System.currentTimeMillis() - start) + "ms");
    }
  }
  
  public static String getAsString(String name) {
    long start = System.currentTimeMillis();

    try {
      return INSTANCE.client.target("http://localhost:8080/myapp").path("latestmessages/" + name)
          .request(MediaType.APPLICATION_JSON).get(String.class);
    } finally {
      LOGGER.info("Time elapsed (get): " + (System.currentTimeMillis() - start) + "ms");
    }
  }
  
  
  
  public static void put(String name, List<Message> messages) {
    Response response = INSTANCE.client.target("http://localhost:8080/myapp").path("latestmessages/" + name)
      .request(MediaType.APPLICATION_JSON).put(Entity.entity(messages, MediaType.APPLICATION_JSON));
    if (response.getStatus() != 200) {
      throw new RuntimeException("Response was " + response.getStatus());
    }
  }
}
