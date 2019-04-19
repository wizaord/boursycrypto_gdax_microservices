package com.wizaord.boursycrypto.gatewaywebsocket.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

  /**
   * Nom de l'exchange dans lequel les messages GDAX sont envoyés
   */
  public static final String RECEIVE_EVENT_EXCHANGE_NAME = "receiveGDAXEvent";

  @Bean
  TopicExchange exchange() {
    return new TopicExchange(RECEIVE_EVENT_EXCHANGE_NAME);
  }

}
