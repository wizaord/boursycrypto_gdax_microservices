package com.wizaord.boursycrypto.gatewaywebsocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class GatewayWebsocketApplicationRunner implements ApplicationRunner {

  private FeedListener feedListener;

  @Autowired
  public GatewayWebsocketApplicationRunner(FeedListener feedListener) {
    this.feedListener = feedListener;
  }

  @Override
  public void run(final ApplicationArguments args) {
    this.feedListener.startConnection();
  }
}
