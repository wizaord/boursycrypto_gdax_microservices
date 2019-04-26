package com.wizaord.boursycrypto.gatewaywebsocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;

@Component
@Profile("PROD")
public class GatewayWebsocketApplicationRunner implements ApplicationRunner {

  private static final Logger LOG = LoggerFactory.getLogger(GatewayWebsocketApplication.class);
//  private FeedListener feedListener;

  @Autowired
  MyStompSessionHandler handler;

//  @Autowired
//  public GatewayWebsocketApplicationRunner(FeedListener feedListener) {
//    this.feedListener = feedListener;
//  }

  @Override
  public void run(final ApplicationArguments args) throws ExecutionException, InterruptedException {
    System.out.println("HO YEAHHHHHHHHHHH");
    WebSocketClient transport = new StandardWebSocketClient();
    WebSocketStompClient stompClient = new WebSocketStompClient(transport);
    stompClient.setMessageConverter(new StringMessageConverter());
    final ListenableFuture<StompSession> connect = stompClient.connect("wss://ws-feed.pro.coinbase.com/", handler);
    connect.addCallback(stompSession -> {
      LOG.info("Success callback");
    }, (x) -> LOG.info("Failure callback"));
    //    this.feedListener.startConnection();
  }
}
