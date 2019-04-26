package com.wizaord.boursycrypto.gatewaywebsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
//@EnableWebSocketMessageBroker
@EnableWebSocket
public class GatewayWebsocketApplication {

  public static void main(String[] args) {
    SpringApplication.run(GatewayWebsocketApplication.class, args);
  }

}
