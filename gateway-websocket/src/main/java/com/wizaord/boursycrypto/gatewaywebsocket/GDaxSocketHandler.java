package com.wizaord.boursycrypto.gatewaywebsocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizaord.boursycrypto.gatewaywebsocket.gdaxwebsocketlistener.SignatureService;
import com.wizaord.boursycrypto.gatewaywebsocket.gdaxwebsocketlistener.beans.SignatureHeader;
import com.wizaord.boursycrypto.gatewaywebsocket.gdaxwebsocketlistener.beans.SubscribeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class GDaxSocketHandler extends TextWebSocketHandler {

  private static final Logger LOG = LoggerFactory.getLogger(GDaxSocketHandler.class);

  private ObjectMapper jsonMapper;
  private SignatureService signatureService;

  @Autowired
  public GDaxSocketHandler(
          final ObjectMapper jsonMapper, final SignatureService signatureService) {
    this.jsonMapper = jsonMapper;
    this.signatureService = signatureService;
    LOG.info("Creating GDAXSOCHETHANDLER");
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    LOG.info("After connection opened ");

    LOG.info("Sending subscribe request to the webSocket");

    final SubscribeRequest subscriberequest = SubscribeRequest.builder()
            .type("subscribe")
            .product_id("BTC-EUR")
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

    TextMessage message = new TextMessage(subscribeMsg);
    session.sendMessage(message);
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message)
          throws Exception {
    LOG.info("Received: {} ", message );
    session.close();
  }
}
