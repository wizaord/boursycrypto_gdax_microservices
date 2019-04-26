package com.wizaord.boursycrypto.gatewaywebsocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizaord.boursycrypto.gatewaywebsocket.gdaxwebsocketlistener.SignatureService;
import com.wizaord.boursycrypto.gatewaywebsocket.gdaxwebsocketlistener.beans.SignatureHeader;
import com.wizaord.boursycrypto.gatewaywebsocket.gdaxwebsocketlistener.beans.SubscribeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class MyStompSessionHandler extends StompSessionHandlerAdapter {

  private static final Logger LOG = LoggerFactory.getLogger(MyStompSessionHandler.class);

  @Autowired
  private ObjectMapper jsonMapper;
  @Autowired
  private SignatureService signatureService;

  @Override
  public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    LOG.info("New session established : " + session.getSessionId());

    LOG.info("After connection opened ");

    LOG.info("Sending subscribe request to the webSocket");

    final SubscribeRequest subscriberequest = SubscribeRequest.builder()
            .type("subscribe")
            .product_id("BTC-EUR")
            .channel("ticker")
            .channel("user")
            .build();

    String subscribxeJson = null;
    try {
      subscribxeJson = jsonMapper.writeValueAsString(subscriberequest);
    }
    catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    LOG.debug("Sig content {}", subscribxeJson);

    final SignatureHeader signature = signatureService.getSignature("/users/self/verify", "GET", null);

    subscriberequest.setKey(signature.getCbAccessKey());
    subscriberequest.setSignature(signature.getCbAccessSign());
    subscriberequest.setTimestamp(signature.getCbAccessTimestamp());
    subscriberequest.setPassphrase(signature.getCbAccessPassphrase());

    String subscribeMsg = null;
    try {
      subscribeMsg = jsonMapper.writeValueAsString(subscriberequest);
    }
    catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    LOG.debug("Sending subscribe request : {}", subscribeMsg);

    session.send("/", subscribeMsg);

    LOG.info("Message sent to websocket server");
  }

  @Override
  public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
    LOG.error("Got an exception", exception);
  }

  @Override
  public void handleTransportError(final StompSession stompSession, final Throwable throwable) {
    LOG.error("PLOP");
  }

  @Override
  public Type getPayloadType(StompHeaders headers) {
    LOG.info("getpayload type");
    return String.class;
  }


}
