package com.wizaord.boursycrypto.influxdb.influxdbstoreservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

  /**
   * Nom de l'exchange dans lequel les messages GDAX sont envoyés
   */
  public static final String RECEIVE_EVENT_EXCHANGE_NAME = "receiveGDAXEvent";
  public static final String INFLUXDB_INPUT_QUEUE = "influxdbQueue";

  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(RECEIVE_EVENT_EXCHANGE_NAME);
  }


  @Bean
  public Queue influxDbQueue() {
    return new Queue(INFLUXDB_INPUT_QUEUE);
  }

  @Bean
  public Binding bindQueue(TopicExchange exchange, Queue influxDbQueue) {
    return BindingBuilder.bind(influxDbQueue).to(exchange).with("*");
  }
}
