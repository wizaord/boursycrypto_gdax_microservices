package com.wizaord.boursycrypto.gatewaywebsocket;

import com.wizaord.boursycrypto.gatewaywebsocket.gdaxwebsocketlistener.FeedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("PROD")
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
