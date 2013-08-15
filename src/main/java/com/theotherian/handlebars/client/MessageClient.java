package com.theotherian.handlebars.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.log4j.Logger;
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
//    clientConfig.property(ClientProperties.READ_TIMEOUT, 20000);
    clientConfig.property(ClientProperties.CONNECT_TIMEOUT, 500);
    
    PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
    connectionManager.setMaxTotal(100);
    connectionManager.setDefaultMaxPerRoute(20);
    connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("localhost")), 10);
    
//    clientConfig.property(ApacheClientProperties.CONNECTION_MANAGER, connectionManager);
//    ApacheConnector connector = new ApacheConnector(clientConfig);
    
    ServerSideConnector connector = ServerConnectorFactory.build();
    clientConfig.connector(connector);
    
    client = ClientBuilder.newClient(clientConfig);
    client.register(JacksonFeature.class);
  }
  
  public static List<Message> get(String name) {
    long start = System.currentTimeMillis();

    try {
      return INSTANCE.client.target("http://localhost:8080/myapp").path("messages/" + name)
          .request(MediaType.APPLICATION_JSON).get(new GenericType<List<Message>>(){});
    } finally {
      LOGGER.info("Time elapsed (get): " + (System.currentTimeMillis() - start) + "ms");
    }
  }
}
