package com.wizaord.boursycrypto.influxdb.influxdbstoreservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

  /**
   * Nom de l'exchange dans lequel les messages GDAX sont envoyés
   */
  private static final String RECEIVE_EVENT_EXCHANGE_NAME = "receiveGDAXEvent";
  private static final String INFLUXDB_INPUT_QUEUE = "influxdbQueue";

  @Bean
  public FanoutExchange exchange() {
    return new FanoutExchange(RECEIVE_EVENT_EXCHANGE_NAME);
  }


  @Bean
  public Queue influxDbQueue() {
    return new Queue(INFLUXDB_INPUT_QUEUE);
  }

  @Bean
  public Binding bindQueue(FanoutExchange exchange, Queue influxDbQueue) {
    return BindingBuilder.bind(influxDbQueue).to(exchange);
  }


}
