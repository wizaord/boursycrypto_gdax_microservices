package com.wizaord.boursycrypto.gatewaywebsocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizaord.boursycrypto.gatewaywebsocket.beans.SignatureHeader;
import com.wizaord.boursycrypto.gatewaywebsocket.beans.SubscribeRequest;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Component
@ClientEndpoint
public class FeedListener {

  private static final Logger LOG = LoggerFactory.getLogger(FeedListener.class);

  @Autowired
  private ObjectMapper jsonMapper;
  @Autowired
  private SignatureService signatureService;
  @Value("${gdax.productName}")
  private String productName;
  @Value("${gdax.feedurl}")
  private String feedurl;
  //    @Autowired
  //    private MessageDispatcherService handleFeedMessageService;
  @Autowired
  private WebSocketContainer webSocketContainer;


  /**
   * Open the connection with the webSocket server
   */
  public void startConnection() {
    ClientManager client = ClientManager.createClient(webSocketContainer);
    client.getProperties().put(ClientProperties.RECONNECT_HANDLER, new ReconnectHandler());

    LOG.info("Connecting WebSocket to URL : {}", feedurl);
    try {
      client.connectToServer(this, new URI(feedurl));
    }
    catch (DeploymentException | URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @OnOpen
  public void onOpen(Session session) throws IOException {
    LOG.info("Sending subscribe request to the webSocket");

    final SubscribeRequest subscriberequest = SubscribeRequest.builder()
            .type("subscribe")
            .product_id(productName)
            .channel("ticker")
            .channel("user")
            .build();

    final String subscribeJson = jsonMapper.writeValueAsString(subscriberequest);
    LOG.debug("Sig content {}", subscribeJson);

    final SignatureHeader signature = signatureService.getSignature("/users/self/verify", "GET", null);

    subscriberequest.setKey(signature.getCbAccessKey());
    subscriberequest.setSignature(signature.getCbAccessSign());
    subscriberequest.setTimestamp(signature.getCbAccessTimestamp());
    subscriberequest.setPassphrase(signature.getCbAccessPassphrase());

    final String subscribeMsg = jsonMapper.writeValueAsString(subscriberequest);
    LOG.debug("Sending subscribe request : {}", subscribeMsg);
    session.getBasicRemote().sendText(subscribeMsg);
  }

  @OnMessage
  public void processMessage(String message) {
    LOG.debug("GDAX FEED : receive message : {}", message);
//    this.handleFeedMessageService.handleJsonMessage(message);
  }

  @OnClose
  public void processClose(Session session, CloseReason reason) throws IOException {
    LOG.debug("WebSocket has been close with reason " + reason.toString());
  }

  @OnError
  public void processError(Throwable t) {
    t.printStackTrace();
  }

}
